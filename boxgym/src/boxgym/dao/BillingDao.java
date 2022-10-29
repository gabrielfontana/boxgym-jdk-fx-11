package boxgym.dao;

import boxgym.jdbc.ConnectionFactory;
import boxgym.model.Billing;
import boxgym.model.Membership;
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
        String sql = "INSERT INTO `billing` (`fkSale`, `description`, `dueDate`, `valueToPay`, `status`) VALUES (?, ?, ?, ?, ?);";
        
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, billing.getFkSale());
            ps.setString(2, billing.getDescription());
            ps.setDate(3, java.sql.Date.valueOf(billing.getDueDate()));
            ps.setBigDecimal(4, billing.getValueToPay());
            ps.setString(5, billing.getStatus());
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
    
    public boolean createMembershipBilling(Billing billing) {
        String sql = "INSERT INTO `billing` (`fkMembership`, `description`, `dueDate`, `valueToPay`, `status`) VALUES (?, ?, ?, ?, ?);";
        
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, billing.getFkMembership());
            ps.setString(2, billing.getDescription());
            ps.setDate(3, java.sql.Date.valueOf(billing.getDueDate()));
            ps.setBigDecimal(4, billing.getValueToPay());
            ps.setString(5, billing.getStatus());
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
    
    public List<Billing> readSale() {
        List<Billing> billingsList = new ArrayList<>();
        String sql = "SELECT b.billingId, b.fkSale, c.name AS `tempCustomerName`, b.description, b.dueDate, b.status, b.valueToPay, b.createdAt, b.updatedAt "
                + "FROM `billing` AS b INNER JOIN `sale` AS s "
                + "ON b.fkSale = s.saleId INNER JOIN `customer` as C "
                + "ON s.fkCustomer = c.customerId "
                + "WHERE b.fkSale IS NOT NULL AND b.status = 1;";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Billing b = new Billing();
                b.setBillingId(rs.getInt("billingId"));
                b.setFkSale(rs.getInt("fkSale"));
                b.setDescription(rs.getString("description"));
                b.setDueDate(rs.getDate("dueDate").toLocalDate());
                b.setValueToPay(rs.getBigDecimal("valueToPay"));
                b.setStatus(rs.getString("status"));
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
        String sql = "SELECT b.billingId, b.fkMembership, c.name AS `tempCustomerName`, b.description, b.dueDate, b.status, b.valueToPay, b.createdAt, b.updatedAt "
                + "FROM `billing` AS b INNER JOIN `membership` AS m "
                + "ON b.fkMembership = m.membershipId INNER JOIN `customer` as C "
                + "ON m.fkCustomer = c.customerId "
                + "WHERE b.fkMembership IS NOT NULL AND b.status = 1;";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Billing b = new Billing();
                b.setBillingId(rs.getInt("billingId"));
                b.setFkMembership(rs.getInt("fkMembership"));
                b.setDescription(rs.getString("description"));
                b.setDueDate(rs.getDate("dueDate").toLocalDate());
                b.setValueToPay(rs.getBigDecimal("valueToPay"));
                b.setStatus(rs.getString("status"));
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
    
    public boolean update(Membership membership) {
        String sql = "UPDATE `billing` SET `dueDate` = ?, `valueToPay` = ? WHERE `fkMembership` = ?;";

        try {
            ps = conn.prepareStatement(sql);
            ps.setDate(1, java.sql.Date.valueOf(membership.getDueDate()));
            ps.setBigDecimal(2, membership.getPrice());
            ps.setInt(3, membership.getMembershipId());
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
}
