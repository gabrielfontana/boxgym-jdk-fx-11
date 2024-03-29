package boxgym.dao;

import boxgym.jdbc.ConnectionFactory;
import boxgym.model.StockEntry;
import boxgym.model.StockEntryProduct;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.dbutils.DbUtils;

public class StockEntryProductDao {

    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;

    public StockEntryProductDao() {
        this.conn = new ConnectionFactory().getConnection();
    }

    public boolean create(StockEntryProduct entry) {
        String sql = "INSERT INTO `stockentry_product` (`fkStockEntry`, `fkProduct`, `amount`, `costPrice`) VALUES (?, ?, ?, ?);";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, entry.getFkStockEntry());
            ps.setInt(2, entry.getFkProduct());
            ps.setInt(3, entry.getAmount());
            ps.setBigDecimal(4, entry.getCostPrice());
            ps.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(StockEntryProductDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }

    public List<StockEntryProduct> read(int selectedStockEntry) {
        List<StockEntryProduct> productsList = new ArrayList<>();
        String sql = "SELECT p.name AS `tempProductName`, se_p.amount, se_p.costPrice, se_p.amount * se_p.costPrice AS `subtotal`, se_p.createdAt, se_p.updatedAt "
                + "FROM `stockentry_product` AS se_p INNER JOIN `product` AS p "
                + "ON se_p.fkProduct = p.productId "
                + "WHERE se_p.fkStockEntry = " + selectedStockEntry + ";";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                StockEntryProduct sep = new StockEntryProduct();
                sep.setTempProductName(rs.getString("tempProductName"));
                sep.setAmount(rs.getInt("amount"));
                sep.setCostPrice(rs.getBigDecimal("costPrice"));
                sep.setSubtotal(rs.getBigDecimal("subtotal"));
                sep.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
                sep.setUpdatedAt(rs.getTimestamp("updatedAt").toLocalDateTime());
                productsList.add(sep);
            }
        } catch (SQLException ex) {
            Logger.getLogger(StockEntryProductDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return productsList;
    }

    public boolean delete(StockEntry entry) {
        String sql = "DELETE FROM `stockentry_product` WHERE `fkStockEntry` = ?;";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, entry.getStockEntryId());
            ps.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(StockEntryProductDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }

}
