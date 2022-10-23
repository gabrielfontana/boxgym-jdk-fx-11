package boxgym.dao;

import boxgym.jdbc.ConnectionFactory;
import boxgym.model.Payment;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.dbutils.DbUtils;

public class PaymentDao {
    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;

    public PaymentDao() {
        this.conn = new ConnectionFactory().getConnection();
    }

    public boolean create(Payment payment) {
        String sql = "INSERT INTO `payment`(`fkBilling`, `description`, `paymentDate`, `paidValue`) VALUES (?, ?, ?, ?)";
        
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, payment.getFkBilling());
            ps.setString(2, payment.getDescription());
            ps.setDate(3, java.sql.Date.valueOf(payment.getPaymentDate()));
            ps.setBigDecimal(4, payment.getPaidValue());
            ps.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(PaymentDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }
    
    public List<Payment> read() {
        List<Payment> paymentsList = new ArrayList<>();
        String sql = "SELECT * FROM `payments`;";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Payment p = new Payment();
                p.setPaymentId(rs.getInt("paymentId"));
                p.setFkBilling(rs.getInt("fkBilling"));
                p.setDescription(rs.getString("description"));
                p.setPaymentDate(rs.getDate("paymentDate").toLocalDate());
                p.setPaidValue(rs.getBigDecimal("paidValue"));
                paymentsList.add(p);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PaymentDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return paymentsList;
    }
}
