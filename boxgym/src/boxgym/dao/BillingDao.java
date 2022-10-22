package boxgym.dao;

import boxgym.jdbc.ConnectionFactory;
import boxgym.model.Billing;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.dbutils.DbUtils;

public class BillingDao {
    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;

    public BillingDao() {
        this.conn = new ConnectionFactory().getConnection();
    }
    
    public boolean createSaleBilling(Billing billing) {
        String sql = "INSERT INTO `billing` (`fkSale`, `description`, `expirationDate`, `valueToPay`) VALUES (?, ?, ?, ?);";
        
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, billing.getFkSale());
            ps.setString(2, billing.getDescription());
            ps.setDate(3, java.sql.Date.valueOf(billing.getExpirationDate()));
            ps.setBigDecimal(4, billing.getValueToPay());
            ps.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(BillingDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }
    
    public List<Billing> readAll() {
        List<Billing> billingsList = new ArrayList<>();
        String sql 
                = "SELECT b.billingId AS `billingId`, c.name AS `tempCustomerName`, b.description, b.expirationDate, b.valueToPay, b.createdAt, b.updatedAt "
                + "FROM `billing` AS b INNER JOIN `sale` AS v "
                + "ON b.fkSale = v.saleId INNER JOIN `customer` as C "
                + "ON v.fkCustomer = c.customerId "
                + "WHERE b.fkSale IS NOT NULL "
                + "UNION "
                + "SELECT b.billingId, c.name AS `tempCustomerName`, b.description, b.expirationDate, b.valueToPay, b.createdAt, b.updatedAt "
                + "FROM `billing` AS b INNER JOIN `membership` AS m "
                + "ON b.fkMembership = m.membershipId INNER JOIN `customer` as C "
                + "ON m.fkCustomer = c.customerId "
                + "WHERE b.fkMembership IS NOT NULL "
                + "ORDER BY `billingId`;";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Billing b = new Billing();
                b.setBillingId(rs.getInt("billingId"));
                b.setDescription(rs.getString("description"));
                b.setExpirationDate(rs.getDate("expirationDate").toLocalDate());
                b.setValueToPay(rs.getBigDecimal("valueToPay"));
                b.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
                b.setUpdatedAt(rs.getTimestamp("updatedAt").toLocalDateTime());
                b.setTempCustomerName(rs.getString("tempCustomerName"));
                billingsList.add(b);
            }
        } catch (SQLException ex) {
            Logger.getLogger(BillingDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return billingsList;
    }
    
    public List<Billing> readSales() {
        List<Billing> billingsList = new ArrayList<>();
        String sql = "SELECT b.billingId, c.name AS `tempCustomerName`, b.description, b.expirationDate, b.valueToPay, b.createdAt, b.updatedAt "
                + "FROM `billing` AS b INNER JOIN `sale` AS v "
                + "ON b.fkSale = v.saleId INNER JOIN `customer` as C "
                + "ON v.fkCustomer = c.customerId "
                + "WHERE b.fkSale IS NOT NULL;";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Billing b = new Billing();
                b.setBillingId(rs.getInt("billingId"));
                b.setDescription(rs.getString("description"));
                b.setExpirationDate(rs.getDate("expirationDate").toLocalDate());
                b.setValueToPay(rs.getBigDecimal("valueToPay"));
                b.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
                b.setUpdatedAt(rs.getTimestamp("updatedAt").toLocalDateTime());
                b.setTempCustomerName(rs.getString("tempCustomerName"));
                billingsList.add(b);
            }
        } catch (SQLException ex) {
            Logger.getLogger(BillingDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return billingsList;
    }
    
    public List<Billing> readMembership() {
        List<Billing> billingsList = new ArrayList<>();
        String sql = "SELECT b.billingId, c.name AS `tempCustomerName`, b.description, b.expirationDate, b.valueToPay, b.createdAt, b.updatedAt "
                + "FROM `billing` AS b INNER JOIN `membership` AS m "
                + "ON b.fkMembership = m.membershipId INNER JOIN `customer` as C "
                + "ON m.fkCustomer = c.customerId "
                + "WHERE b.fkMembership IS NOT NULL;";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Billing b = new Billing();
                b.setBillingId(rs.getInt("billingId"));
                b.setDescription(rs.getString("description"));
                b.setExpirationDate(rs.getDate("expirationDate").toLocalDate());
                b.setValueToPay(rs.getBigDecimal("valueToPay"));
                b.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
                b.setUpdatedAt(rs.getTimestamp("updatedAt").toLocalDateTime());
                b.setTempCustomerName(rs.getString("tempCustomerName"));
                billingsList.add(b);
            }
        } catch (SQLException ex) {
            Logger.getLogger(BillingDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return billingsList;
    }
}
