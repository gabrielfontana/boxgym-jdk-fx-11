package boxgym.dao;

import boxgym.helper.ExcelFileHelper;
import boxgym.jdbc.ConnectionFactory;
import boxgym.model.StockEntry;
import boxgym.model.StockEntryProduct;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.dbutils.DbUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class StockEntryDao {

    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;

    public StockEntryDao() {
        this.conn = new ConnectionFactory().getConnection();
    }

    public boolean create(StockEntry entry) {
        String sql = "INSERT INTO `stockentry` (`fkSupplier`, `invoiceIssueDate`, `invoiceNumber`) VALUES (?, ?, ?);";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, entry.getFkSupplier());
            ps.setDate(2, java.sql.Date.valueOf(entry.getInvoiceIssueDate()));
            ps.setString(3, entry.getInvoiceNumber());
            ps.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(StockEntryDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            //DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }

    public int getStockEntryId() {
        String sql = "SELECT `stockEntryId` FROM `stockentry` ORDER BY `stockEntryId` DESC LIMIT 1;";
        int stockEntryId = 0;

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                stockEntryId = rs.getInt("stockEntryId");
            }
        } catch (SQLException ex) {
            Logger.getLogger(StockEntryDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return stockEntryId;
    }

    public boolean deleteLastEntry() {
        String sql = "DELETE FROM `stockentry` ORDER BY `stockEntryId` DESC LIMIT 1;";

        try {
            ps = conn.prepareStatement(sql);
            ps.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(StockEntryDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }

    public List<StockEntry> read() {
        List<StockEntry> stockEntriesList = new ArrayList<>();
        String sql = "SELECT se.stockEntryId, se.fkSupplier, s.corporateName AS `tempSupplierName`, se.invoiceIssueDate, se.invoiceNumber, se.createdAt, se.updatedAt "
                + "FROM `stockentry` AS se INNER JOIN `supplier` AS s "
                + "ON se.fkSupplier = s.supplierId "
                + "ORDER BY se.stockEntryId ASC;";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                StockEntry se = new StockEntry();
                se.setStockEntryId(rs.getInt("stockEntryId"));
                se.setFkSupplier(rs.getInt("fkSupplier"));
                se.setTempSupplierName(rs.getString("tempSupplierName"));
                se.setInvoiceIssueDate(rs.getDate("invoiceIssueDate").toLocalDate());
                se.setInvoiceNumber(rs.getString("invoiceNumber"));
                se.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
                se.setUpdatedAt(rs.getTimestamp("updatedAt").toLocalDateTime());
                stockEntriesList.add(se);
            }
        } catch (SQLException ex) {
            Logger.getLogger(StockEntryDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return stockEntriesList;
    }

    public boolean delete(StockEntry entry) {
        String sql = "DELETE FROM `stockentry` WHERE `stockEntryId` = ?;";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, entry.getStockEntryId());
            ps.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(StockEntryDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }

    public boolean createExcelFile(String filePath) {
        String sql = "SELECT se.stockEntryId, s.corporateName AS `tempSupplierName`, se.invoiceIssueDate, se.invoiceNumber, se.createdAt, se.updatedAt "
                + "FROM `stockentry` AS se INNER JOIN `supplier` AS s "
                + "ON se.fkSupplier = s.supplierId "
                + "ORDER BY se.stockEntryId ASC;";

        try {
            XSSFWorkbook workbook = new XSSFWorkbook();

            XSSFCellStyle infoStyle = ExcelFileHelper.excelStyle(workbook, "Arial", true, BorderStyle.NONE, new byte[]{(byte) 255, (byte) 255, (byte) 255});
            XSSFCellStyle headerStyle = ExcelFileHelper.excelStyle(workbook, "Arial", true, BorderStyle.THIN, new byte[]{(byte) 205, (byte) 205, (byte) 205});
            XSSFCellStyle subHeaderStyle = ExcelFileHelper.excelStyle(workbook, "Arial", true, BorderStyle.THIN, new byte[]{(byte) 230, (byte) 230, (byte) 230});
            XSSFCellStyle defaultStyle = ExcelFileHelper.excelStyle(workbook, "Arial", false, BorderStyle.THIN, new byte[]{(byte) 255, (byte) 255, (byte) 255});

            XSSFSheet sheet = workbook.createSheet("Entradas de Estoque");
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));
            ExcelFileHelper.createStyledCell(sheet.createRow(0), 0, "Relatório gerado em: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")), infoStyle);

            List<String> fields = Arrays.asList("ID", "Fornecedor", "Data de Emissão", "Nota Fiscal", "Criação", "Modificação");
            XSSFRow headerRow = sheet.createRow(2);
            for (int i = 0; i < fields.size(); i++) {
                ExcelFileHelper.createStyledCell(headerRow, i, fields.get(i), headerStyle);
            }

            List<String> relatedProductsFields = Arrays.asList("Produto", "Quantidade", "Preço de Custo", "Subtotal");

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            int rowIndex = 3;
            while (rs.next()) {
                // StockEntry
                int currentStockEntryId = rs.getInt("stockEntryId");
                XSSFRow row = sheet.createRow(rowIndex);
                ExcelFileHelper.createStyledCell(row, 0, rs.getInt("stockEntryId"), defaultStyle);
                ExcelFileHelper.createStyledCell(row, 1, rs.getString("tempSupplierName"), defaultStyle);
                ExcelFileHelper.createStyledDateCell(row, 2, rs.getString("invoiceIssueDate"), DateTimeFormatter.ofPattern("dd/MM/yyyy"), defaultStyle);
                ExcelFileHelper.createStyledCell(row, 3, rs.getString("invoiceNumber"), defaultStyle);
                ExcelFileHelper.createStyledDateTimeCell(row, 4, rs.getString("createdAt"), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"), defaultStyle);
                ExcelFileHelper.createStyledDateTimeCell(row, 5, rs.getString("updatedAt"), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"), defaultStyle);
                rowIndex++;

                // StockEntryProduct
                XSSFRow row2 = sheet.createRow(rowIndex);
                StockEntryProductDao dao = new StockEntryProductDao();
                List<StockEntryProduct> relatedProducts = dao.read(currentStockEntryId);
                int firstRowForGroup = rowIndex;
                for (int i = 0; i < relatedProductsFields.size(); i++) {
                    ExcelFileHelper.createStyledCell(row2, i + 1, relatedProductsFields.get(i), subHeaderStyle);
                }
                rowIndex++;
                for (int i = 0; i < relatedProducts.size(); i++) {
                    XSSFRow row3 = sheet.createRow(rowIndex);
                    ExcelFileHelper.createStyledCell(row3, 1, relatedProducts.get(i).getTempProductName(), defaultStyle);
                    ExcelFileHelper.createStyledCell(row3, 2, relatedProducts.get(i).getAmount(), defaultStyle);
                    ExcelFileHelper.createStyledCell(row3, 3, relatedProducts.get(i).getCostPrice().doubleValue(), defaultStyle);
                    ExcelFileHelper.createStyledCell(row3, 4, relatedProducts.get(i).getSubtotal().doubleValue(), defaultStyle);
                    rowIndex++;
                }
                int lastRowForGroup = rowIndex;
                sheet.groupRow(firstRowForGroup, lastRowForGroup);
                rowIndex++;
            }
            for (int i = 0; i < fields.size(); i++) {
                sheet.autoSizeColumn(i);
            }

            FileOutputStream fileOut = new FileOutputStream(filePath);
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();
            return true;
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(StockEntryDao.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(StockEntryDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }
}
