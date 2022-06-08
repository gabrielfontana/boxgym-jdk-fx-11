package boxgym.dao;

import boxgym.jdbc.ConnectionFactory;
import boxgym.model.Sale;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.dbutils.DbUtils;

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
}
