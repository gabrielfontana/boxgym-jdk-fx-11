package boxgym.dao;

import boxgym.jdbc.ConnectionFactory;
import boxgym.model.Customer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.dbutils.DbUtils;

public class CustomerDao {

    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;

    public CustomerDao() {
        this.conn = new ConnectionFactory().getConnection();
    }
    
    public boolean checkExistingCustomer(String personRegistry) {
        String cpf = personRegistry;
        String sql = "SELECT `personRegistry` FROM `customer` WHERE `personRegistry` = '" + cpf + "';";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return (cpf.equals(rs.getString("personRegistry")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            //DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }

    public boolean create(Customer customer) {
        String sql = "INSERT INTO `customer` (`personRegistry`, `name`, `sex`, `email`, `phone`, `zipCode`, `address`, `addressComplement`, "
                + "`district`, `city`, `federativeUnit`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, customer.getPersonRegistry());
            ps.setString(2, customer.getName());
            ps.setString(3, customer.getSex());
            ps.setString(4, customer.getEmail());
            ps.setString(5, customer.getPhone());
            ps.setString(6, customer.getZipCode());
            ps.setString(7, customer.getAddress());
            ps.setString(8, customer.getAddressComplement());
            ps.setString(9, customer.getDistrict());
            ps.setString(10, customer.getCity());
            ps.setString(11, customer.getFederativeUnit());
            ps.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }
    
    public List<Customer> read() {
        List<Customer> customersList = new ArrayList<>();
        String sql = "SELECT * FROM `customer`;";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Customer c = new Customer();
                c.setCustomerId(rs.getInt("customerId"));
                c.setPersonRegistry(rs.getString("personRegistry"));
                c.setName(rs.getString("name"));
                c.setSex(rs.getString("sex"));
                c.setEmail(rs.getString("email"));
                c.setPhone(rs.getString("phone"));
                c.setZipCode(rs.getString("zipCode"));
                c.setAddress(rs.getString("address"));
                c.setAddressComplement(rs.getString("addressComplement"));
                c.setDistrict(rs.getString("district"));
                c.setCity(rs.getString("city"));
                c.setFederativeUnit(rs.getString("federativeUnit"));
                c.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
                c.setUpdatedAt(rs.getTimestamp("updatedAt").toLocalDateTime());
                customersList.add(c);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return customersList;
    }
    
    public boolean update(Customer customer) {
        String sql = "UPDATE `customer` SET `name` = ?, `sex` = ?, `email` = ?, `phone` = ?, `zipCode` = ?, `address` = ?, `addressComplement` = ?, "
                + "`district` = ?, `city` = ?, `federativeUnit` = ? WHERE `customerId` = ?;";

        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getSex());
            ps.setString(3, customer.getEmail());
            ps.setString(4, customer.getPhone());
            ps.setString(5, customer.getZipCode());
            ps.setString(6, customer.getAddress());
            ps.setString(7, customer.getAddressComplement());
            ps.setString(8, customer.getDistrict());
            ps.setString(9, customer.getCity());
            ps.setString(10, customer.getFederativeUnit());
            ps.setInt(11, customer.getCustomerId());
            ps.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }

    public boolean delete(Customer customer) {
        String sql = "DELETE FROM `customer` WHERE `customerId` = ?;";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, customer.getCustomerId());
            ps.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }
}
