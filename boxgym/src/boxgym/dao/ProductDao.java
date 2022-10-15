package boxgym.dao;

import boxgym.helper.ExcelFileHelper;
import boxgym.jdbc.ConnectionFactory;
import boxgym.model.Product;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.apache.commons.dbutils.DbUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ProductDao {

    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;

    public ProductDao() {
        this.conn = new ConnectionFactory().getConnection();
    }

    public boolean create(Product product) {
        String sql = "INSERT INTO `product` (`name`, `category`, `description`, `amount`, `minimumStock`, `costPrice`, `sellingPrice`, `image`) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, product.getName());
            ps.setString(2, product.getCategory());
            ps.setString(3, product.getDescription());
            ps.setInt(4, product.getAmount());
            ps.setInt(5, product.getMinimumStock());
            ps.setBigDecimal(6, product.getCostPrice());
            ps.setBigDecimal(7, product.getSellingPrice());
            ps.setBytes(8, product.getImage());
            ps.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ProductDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }

    public LinkedHashMap<Integer, String> getProductForHashMap() {
        LinkedHashMap<Integer, String> map = new LinkedHashMap<>();
        String sql = "SELECT `productId`, `name` FROM `product`;";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            Product p;
            while (rs.next()) {
                p = new Product(rs.getInt("productId"), rs.getString("name"));
                map.put(p.getProductId(), p.getName());
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return map;
    }

    public BigDecimal getProductCostPrice(int key) {
        String sql = "SELECT `costPrice` FROM `product` WHERE `productId` = '" + key + "';";
        BigDecimal costPrice = new BigDecimal("0");

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                costPrice = rs.getBigDecimal("costPrice");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return costPrice;
    }

    public int getProductAmount(int key) {
        String sql = "SELECT `amount` FROM `product` WHERE `productId` = '" + key + "';";
        int amount = 0;

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                amount = rs.getInt("amount");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return amount;
    }

    public List<Product> read() {
        List<Product> productsList = new ArrayList<>();
        String sql = "SELECT * FROM `product`";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Product p = new Product();
                p.setProductId(rs.getInt("productId"));
                p.setName(rs.getString("name"));
                p.setCategory(rs.getString("category"));
                p.setDescription(rs.getString("description"));
                p.setAmount(rs.getInt("amount"));
                p.setMinimumStock(rs.getInt("minimumStock"));
                p.setCostPrice(rs.getBigDecimal("costPrice"));
                p.setSellingPrice(rs.getBigDecimal("sellingPrice"));
                p.setImage(rs.getBytes("image"));
                p.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
                p.setUpdatedAt(rs.getTimestamp("updatedAt").toLocalDateTime());
                productsList.add(p);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return productsList;
    }

    public boolean update(Product product) {
        String sql = "UPDATE `product` SET `name` = ?, `category` = ?, `description` = ?, `minimumStock` = ?, "
                + "`costPrice` = ?, `sellingPrice` = ?, `image` = ? WHERE `productId` = ?;";

        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, product.getName());
            ps.setString(2, product.getCategory());
            ps.setString(3, product.getDescription());
            ps.setInt(4, product.getMinimumStock());
            ps.setBigDecimal(5, product.getCostPrice());
            ps.setBigDecimal(6, product.getSellingPrice());
            ps.setBytes(7, product.getImage());
            ps.setInt(8, product.getProductId());
            ps.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ProductDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }

    public boolean delete(Product product) {
        String sql = "DELETE FROM `product` WHERE `productId` = ?;";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, product.getProductId());
            ps.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ProductDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }

    public List<String> checkProductsBelowMinimumStock() {
        List<String> productsBelowMinimumStockList = new ArrayList<>();
        String sql = "SELECT name FROM `product` WHERE `amount` < `minimumStock`";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                productsBelowMinimumStockList.add(rs.getString("name"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return productsBelowMinimumStockList;
    }
    
    public boolean checkSaleDeleteConstraint(int productId) {
        String sql = "SELECT `fkProduct` FROM `sale_product` WHERE `fkProduct` = " + productId + ";";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            //DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }
    
    public boolean checkStockEntryDeleteConstraint(int productId) {
        String sql = "SELECT `fkProduct` FROM `stockentry_product` WHERE `fkProduct` = " + productId + ";";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            //DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }
    
    public int getAmountOfProductsLast90DaysForDashboard() {
        String sql = "SELECT COUNT(*) AS `amount` FROM `product` WHERE `createdAt` >= DATE_ADD(NOW(), INTERVAL -3 MONTH);";
        int amount = 0;

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                amount = rs.getInt("amount");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return amount;
    }
    
    public int getAmountOfProductsLast30DaysForDashboard() {
        String sql = "SELECT COUNT(*) AS `amount` FROM `product` WHERE `createdAt` >= DATE_ADD(NOW(), INTERVAL -1 MONTH);";
        int amount = 0;

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                amount = rs.getInt("amount");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return amount;
    }
    
    public BigDecimal getTotalStockValueForDashboard() {
        String sql = "SELECT SUM(`amount` * `costPrice`) AS `total` FROM `product`;";
        BigDecimal total = new BigDecimal("0");

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                total = rs.getBigDecimal("total");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return total;
    }
    
    public TreeMultimap<Integer, String> getMostPopularProductsForDashboard() {
        TreeMultimap<Integer, String> sortedMap = TreeMultimap.create(Ordering.natural().reverse(), Ordering.natural());
        String sql = "SELECT SUM(s_p.amount) AS `sumAmount`, p.name AS `tempProductName` "
                + "FROM `sale_product` AS s_p INNER JOIN `product` AS p "
                + "ON s_p.fkProduct = p.productId "
                + "GROUP BY s_p.fkProduct "
                + "ORDER BY `sumAmount` DESC "
                + "LIMIT 10;";
        
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                sortedMap.put(rs.getInt("sumAmount"), rs.getString("tempProductName"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return sortedMap;
    }

    public boolean createExcelFile(String filePath) {
        String sql = "SELECT * FROM `product`";

        try {
            XSSFWorkbook workbook = new XSSFWorkbook();

            XSSFCellStyle infoStyle = ExcelFileHelper.excelStyle(workbook, "Arial", true, BorderStyle.NONE, new byte[]{(byte) 255, (byte) 255, (byte) 255});
            XSSFCellStyle headerStyle = ExcelFileHelper.excelStyle(workbook, "Arial", true, BorderStyle.THIN, new byte[]{(byte) 205, (byte) 205, (byte) 205});
            XSSFCellStyle defaultStyle = ExcelFileHelper.excelStyle(workbook, "Arial", false, BorderStyle.THIN, new byte[]{(byte) 255, (byte) 255, (byte) 255});

            XSSFSheet sheet = workbook.createSheet("Produtos");
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));
            ExcelFileHelper.createStyledCell(sheet.createRow(0), 0, "Relatório gerado em: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")), infoStyle);

            List<String> fields = Arrays.asList("ID", "Nome", "Categoria", "Descrição", "Estoque Atual", "Estoque Mínimo",
                    "Preço de Custo", "Preço de Venda", "Criação", "Modificação");
            XSSFRow headerRow = sheet.createRow(2);
            for (int i = 0; i < fields.size(); i++) {
                ExcelFileHelper.createStyledCell(headerRow, i, fields.get(i), headerStyle);
            }

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            int rowIndex = 3;
            while (rs.next()) {
                XSSFRow row = sheet.createRow(rowIndex);
                ExcelFileHelper.createStyledCell(row, 0, rs.getInt("productId"), defaultStyle);
                ExcelFileHelper.createStyledCell(row, 1, rs.getString("name"), defaultStyle);
                ExcelFileHelper.createStyledCell(row, 2, rs.getString("category"), defaultStyle);
                ExcelFileHelper.createStyledCell(row, 3, rs.getString("description"), defaultStyle);
                ExcelFileHelper.createStyledCell(row, 4, rs.getInt("amount"), defaultStyle);
                ExcelFileHelper.createStyledCell(row, 5, rs.getInt("minimumStock"), defaultStyle);
                ExcelFileHelper.createStyledCell(row, 6, rs.getDouble("costPrice"), defaultStyle);
                ExcelFileHelper.createStyledCell(row, 7, rs.getDouble("sellingPrice"), defaultStyle);
                ExcelFileHelper.createStyledDateTimeCell(row, 8, rs.getString("createdAt"), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"), defaultStyle);
                ExcelFileHelper.createStyledDateTimeCell(row, 9, rs.getString("updatedAt"), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"), defaultStyle);
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
            Logger.getLogger(ProductDao.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ProductDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }
}
