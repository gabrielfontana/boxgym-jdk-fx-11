package boxgym.dao;

import boxgym.helper.ExcelFileHelper;
import boxgym.jdbc.ConnectionFactory;
import boxgym.model.Sale;
import boxgym.model.SaleProduct;
import com.google.common.collect.Ordering;
import com.google.common.collect.TreeMultimap;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
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

public class SaleDao {

    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;

    public SaleDao() {
        this.conn = new ConnectionFactory().getConnection();
    }

    public boolean create(Sale sale) {
        String sql = "INSERT INTO `sale` (`fkCustomer`, `saleDate`) VALUES (?, ?);";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, sale.getFkCustomer());
            ps.setDate(2, java.sql.Date.valueOf(sale.getSaleDate()));
            ps.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(SaleDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            //DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }

    public int getSaleId() {
        String sql = "SELECT `saleId` FROM `sale` ORDER BY `saleId` DESC LIMIT 1;";
        int saleId = 0;

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                saleId = rs.getInt("saleId");
            }
        } catch (SQLException ex) {
            Logger.getLogger(SaleDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return saleId;
    }

    public boolean deleteLastEntry() {
        String sql = "DELETE FROM `sale` ORDER BY `saleId` DESC LIMIT 1;";

        try {
            ps = conn.prepareStatement(sql);
            ps.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(SaleDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }

    public List<Sale> read() {
        List<Sale> salesList = new ArrayList<>();
        String sql = "SELECT s.saleId, s.fkCustomer, c.name AS `tempCustomerName`, s.saleDate, s.createdAt, s.updatedAt "
                + "FROM `sale` AS s INNER JOIN `customer` AS c "
                + "ON s.fkCustomer = c.customerId "
                + "ORDER BY s.saleId ASC;";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Sale s = new Sale();
                s.setSaleId(rs.getInt("saleId"));
                s.setFkCustomer(rs.getInt("fkCustomer"));
                s.setTempCustomerName(rs.getString("tempCustomerName"));
                s.setSaleDate(rs.getDate("saleDate").toLocalDate());
                s.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
                s.setUpdatedAt(rs.getTimestamp("updatedAt").toLocalDateTime());

                String sqlTotal
                        = "SELECT SUM(s_p.amount * s_p.unitPrice) AS `tempTotal` "
                        + "FROM `sale_product` AS s_p INNER JOIN `product` AS p "
                        + "ON s_p.fkProduct = p.productId "
                        + "WHERE s_p.fkSale = " + rs.getInt("saleId") + ";";
                Connection connTotal = new ConnectionFactory().getConnection();
                PreparedStatement psTotal = connTotal.prepareStatement(sqlTotal);
                ResultSet rsTotal = psTotal.executeQuery();
                if (rsTotal.next()) {
                    s.setTempTotal(rsTotal.getBigDecimal("tempTotal"));
                }
                DbUtils.closeQuietly(connTotal);
                DbUtils.closeQuietly(psTotal);
                DbUtils.closeQuietly(rsTotal);

                salesList.add(s);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SaleDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return salesList;
    }

    public boolean delete(Sale sale) {
        String sql = "DELETE FROM `sale` WHERE `saleId` = ?;";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, sale.getSaleId());
            ps.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(SaleDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }

    public int getAmountOfSalesLast90DaysForDashboard() {
        String sql = "SELECT COUNT(*) AS `amount` FROM `sale` WHERE `createdAt` >= DATE_ADD(NOW(), INTERVAL -3 MONTH);";
        int amount = 0;

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                amount = rs.getInt("amount");
            }
        } catch (SQLException ex) {
            Logger.getLogger(SaleDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return amount;
    }

    public int getAmountOfSalesThisMonthForDashboard() {
        String sql = "SELECT COUNT(*) AS `amount` FROM `sale` WHERE MONTH(`saleDate`) = MONTH(CURRENT_DATE()) AND YEAR(`saleDate`) = YEAR(CURRENT_DATE());";
        int amount = 0;

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                amount = rs.getInt("amount");
            }
        } catch (SQLException ex) {
            Logger.getLogger(SaleDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return amount;
    }

    public BigDecimal getAvgTicketThisMonthForDashboard() {
        String sql
                = "SELECT "
                + "TRUNCATE("
                    + "SUM("
                        + "s_p.amount * s_p.unitPrice"
                    + ") / ("
                            + "SELECT COUNT(*) "
                            + "FROM `sale` "
                            + "WHERE MONTH(`saleDate`) = MONTH(CURRENT_DATE()) AND YEAR(`saleDate`) = YEAR(CURRENT_DATE())"
                        + ")"
                + ", 2) AS `avgTicket` "
                + "FROM `sale_product` AS s_p INNER JOIN `sale` AS s "
                + "ON s_p.fkSale = s.saleId "
                + "WHERE MONTH(s.saleDate) = MONTH(CURRENT_DATE()) AND YEAR(s.saleDate) = YEAR(CURRENT_DATE());";
        BigDecimal avgTicket = new BigDecimal("0");

        //Consulta para conferir se o ticket médio está correto
        //SELECT SUM(s_p.amount * s_p.unitPrice) AS `sumSalesOrderBy`, s_p.fkSale FROM `sale_product` AS s_p INNER JOIN `sale` AS s ON s_p.fkSale = s.saleId WHERE MONTH(s.saleDate) = MONTH(CURRENT_DATE()) AND YEAR(s.saleDate) = YEAR(CURRENT_DATE()) GROUP BY s_p.fkSale;
        
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                avgTicket = rs.getBigDecimal("avgTicket");
            }
        } catch (SQLException ex) {
            Logger.getLogger(SaleDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return avgTicket;
    }

    public BigDecimal getLowestSaleThisMonthForDashboard() {
        String sql
                = "SELECT MIN(temp.sumOrderBy) AS `lowestSale` "
                + "FROM ("
                    + "SELECT SUM(s_p.amount * s_p.unitPrice) AS `sumOrderBy`, s_p.fkSale "
                    + "FROM `sale_product` AS s_p INNER JOIN `sale` AS s "
                    + "ON s_p.fkSale = s.saleId "
                    + "WHERE MONTH(s.saleDate) = MONTH(CURRENT_DATE()) AND YEAR(s.saleDate) = YEAR(CURRENT_DATE()) "
                    + "GROUP BY s_p.fkSale"
                + ") AS temp;";
        BigDecimal lowestSale = new BigDecimal("0");

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                lowestSale = rs.getBigDecimal("lowestSale");
            }
        } catch (SQLException ex) {
            Logger.getLogger(SaleDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return lowestSale;
    }

    public BigDecimal getBiggestSaleThisMonthForDashboard() {
        String sql
                = "SELECT MAX(temp.sumOrderBy) AS `biggestSale` "
                + "FROM ("
                    + "SELECT SUM(s_p.amount * s_p.unitPrice) AS `sumOrderBy`, s_p.fkSale "
                    + "FROM `sale_product` AS s_p INNER JOIN `sale` AS s "
                    + "ON s_p.fkSale = s.saleId "
                    + "WHERE MONTH(s.saleDate) = MONTH(CURRENT_DATE()) AND YEAR(s.saleDate) = YEAR(CURRENT_DATE()) "
                    + "GROUP BY s_p.fkSale"
                + ") AS temp;";
        BigDecimal biggestSale = new BigDecimal("0");

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                biggestSale = rs.getBigDecimal("biggestSale");
            }
        } catch (SQLException ex) {
            Logger.getLogger(SaleDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return biggestSale;
    }

    public List<BigDecimal> getAnnualSalesHistoryForDashboard() {
        List<BigDecimal> sumOfSalesByMonthList = new ArrayList<>();
        String sql
                = "SELECT m.month, IFNULL(SUM(s_p.amount * s_p.unitPrice), 0) AS `sumOfSalesByMonth` "
                + "FROM "
                + "("
                    + "SELECT 1 AS month UNION "
                    + "SELECT 2 UNION "
                    + "SELECT 3 UNION "
                    + "SELECT 4 UNION "
                    + "SELECT 5 UNION "
                    + "SELECT 6 UNION "
                    + "SELECT 7 UNION "
                    + "SELECT 8 UNION "
                    + "SELECT 9 UNION "
                    + "SELECT 10 UNION "
                    + "SELECT 11 UNION "
                    + "SELECT 12"
                + ") AS m LEFT JOIN `sale` AS s "
                + "ON m.month = MONTH(s.saleDate) "
                + "LEFT JOIN `sale_product` AS s_p "
                + "ON s.saleId = s_p.fkSale "
                + "GROUP BY (m.month);";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                sumOfSalesByMonthList.add(rs.getBigDecimal("sumOfSalesByMonth"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(SaleDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return sumOfSalesByMonthList;
    }

    public TreeMultimap<Integer, String> getMostFrequentCustomersLast90DaysForDashoboard() {
        TreeMultimap<Integer, String> descMap = TreeMultimap.create(Ordering.natural().reverse(), Ordering.natural());
        String sql = "SELECT COUNT(*) AS `amount`, c.name AS `tempCustomerName` "
                + "FROM `sale` AS s INNER JOIN `customer` AS c "
                + "ON s.fkCustomer = c.customerId "
                + "WHERE s.saleDate >= DATE_ADD(NOW(), INTERVAL -3 MONTH) "
                + "GROUP by s.fkCustomer "
                + "ORDER BY `amount` DESC "
                + "LIMIT 5;";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                descMap.put(rs.getInt("amount"), rs.getString("tempCustomerName"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(SaleDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return descMap;
    }

    public boolean createExcelFile(String filePath) {
        String sql = "SELECT s.saleId, c.name AS `tempCustomerName`, s.saleDate, s.createdAt, s.updatedAt "
                + "FROM `sale` AS s INNER JOIN `customer` AS c "
                + "ON s.fkCustomer = c.customerId "
                + "ORDER BY s.saleId ASC;";

        try {
            XSSFWorkbook workbook = new XSSFWorkbook();

            XSSFCellStyle infoStyle = ExcelFileHelper.excelStyle(workbook, "Arial", true, BorderStyle.NONE, new byte[]{(byte) 255, (byte) 255, (byte) 255});
            XSSFCellStyle headerStyle = ExcelFileHelper.excelStyle(workbook, "Arial", true, BorderStyle.THIN, new byte[]{(byte) 205, (byte) 205, (byte) 205});
            XSSFCellStyle subHeaderStyle = ExcelFileHelper.excelStyle(workbook, "Arial", true, BorderStyle.THIN, new byte[]{(byte) 230, (byte) 230, (byte) 230});
            XSSFCellStyle defaultStyle = ExcelFileHelper.excelStyle(workbook, "Arial", false, BorderStyle.THIN, new byte[]{(byte) 255, (byte) 255, (byte) 255});

            XSSFSheet sheet = workbook.createSheet("Vendas");
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));
            ExcelFileHelper.createStyledCell(sheet.createRow(0), 0, "Relatório gerado em: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")), infoStyle);

            List<String> fields = Arrays.asList("ID", "Cliente", "Data de Venda", "Criação", "Modificação");
            XSSFRow headerRow = sheet.createRow(2);
            for (int i = 0; i < fields.size(); i++) {
                ExcelFileHelper.createStyledCell(headerRow, i, fields.get(i), headerStyle);
            }

            List<String> relatedProductsFields = Arrays.asList("Produto", "Quantidade", "Preço de Custo", "Subtotal");

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            int rowIndex = 3;
            while (rs.next()) {
                // Sale
                int currentSaleId = rs.getInt("saleId");
                XSSFRow row = sheet.createRow(rowIndex);
                ExcelFileHelper.createStyledCell(row, 0, rs.getInt("saleId"), defaultStyle);
                ExcelFileHelper.createStyledCell(row, 1, rs.getString("tempCustomerName"), defaultStyle);
                ExcelFileHelper.createStyledDateCell(row, 2, rs.getString("saleDate"), DateTimeFormatter.ofPattern("dd/MM/yyyy"), defaultStyle);
                ExcelFileHelper.createStyledDateTimeCell(row, 3, rs.getString("createdAt"), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"), defaultStyle);
                ExcelFileHelper.createStyledDateTimeCell(row, 4, rs.getString("updatedAt"), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"), defaultStyle);
                rowIndex++;

                // SaleProduct
                XSSFRow row2 = sheet.createRow(rowIndex);
                SaleProductDao dao = new SaleProductDao();
                List<SaleProduct> relatedProducts = dao.read(currentSaleId);
                int firstRowForGroup = rowIndex;
                for (int i = 0; i < relatedProductsFields.size(); i++) {
                    ExcelFileHelper.createStyledCell(row2, i + 1, relatedProductsFields.get(i), subHeaderStyle);
                }
                rowIndex++;
                for (int i = 0; i < relatedProducts.size(); i++) {
                    XSSFRow row3 = sheet.createRow(rowIndex);
                    ExcelFileHelper.createStyledCell(row3, 1, relatedProducts.get(i).getTempProductName(), defaultStyle);
                    ExcelFileHelper.createStyledCell(row3, 2, relatedProducts.get(i).getAmount(), defaultStyle);
                    ExcelFileHelper.createStyledCell(row3, 3, relatedProducts.get(i).getUnitPrice().doubleValue(), defaultStyle);
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
            Logger.getLogger(SaleDao.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SaleDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }
}
