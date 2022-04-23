package boxgym.dao;

import boxgym.helper.ExcelFileHelper;
import boxgym.jdbc.ConnectionFactory;
import boxgym.model.StockEntry;
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

    public boolean createExcelFile(String filePath) {
        String sql = "SELECT se.stockEntryId, s.corporateName AS `tempSupplierName`, se.invoiceIssueDate, se.invoiceNumber, se.createdAt, se.updatedAt "
                + "FROM `stockentry` AS se INNER JOIN `supplier` AS s "
                + "ON se.fkSupplier = s.supplierId "
                + "ORDER BY se.stockEntryId ASC;";

        try {
            XSSFWorkbook workbook = new XSSFWorkbook();

            XSSFCellStyle infoStyle = ExcelFileHelper.excelStyle(workbook, "Arial", true, BorderStyle.NONE);
            XSSFCellStyle headerStyle = ExcelFileHelper.excelStyle(workbook, "Arial", true, BorderStyle.THIN);
            XSSFCellStyle defaultStyle = ExcelFileHelper.excelStyle(workbook, "Arial", false, BorderStyle.THIN);

            XSSFSheet sheet = workbook.createSheet("Entradas de Estoque");
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));
            ExcelFileHelper.createStyledCell(sheet.createRow(0), 0, "Relatório gerado em: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")), infoStyle);

            List<String> fields = Arrays.asList("ID", "Fornecedor", "Data de Emissão da Nota Fiscal", "Nota Fiscal", "Criação", "Modificação");
            for (int i = 0; i < fields.size(); i++) {
                ExcelFileHelper.createStyledCell(sheet.createRow(2), i, fields.get(i), headerStyle);
            }

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            int contentRow = 3;
            while (rs.next()) {
                XSSFRow row = sheet.createRow(contentRow);
                ExcelFileHelper.createStyledCell(row, 0, rs.getInt("stockEntryId"), defaultStyle);
                ExcelFileHelper.createStyledCell(row, 1, rs.getString("tempSupplierName"), defaultStyle);
                ExcelFileHelper.createStyledDateCell(row, 2, rs.getString("invoiceIssueDate"), DateTimeFormatter.ofPattern("dd/MM/yyyy"), defaultStyle);
                ExcelFileHelper.createStyledCell(row, 3, rs.getString("invoiceNumber"), defaultStyle);
                ExcelFileHelper.createStyledDateTimeCell(row, 4, rs.getString("createdAt"), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"), defaultStyle);
                ExcelFileHelper.createStyledDateTimeCell(row, 5, rs.getString("updatedAt"), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"), defaultStyle);
                contentRow++;
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
            Logger.getLogger(SupplierDao.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SupplierDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }
}
