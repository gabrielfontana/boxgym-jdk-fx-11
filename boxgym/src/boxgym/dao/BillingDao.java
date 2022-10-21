package boxgym.dao;

import boxgym.jdbc.ConnectionFactory;
import boxgym.model.Billing;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
}
