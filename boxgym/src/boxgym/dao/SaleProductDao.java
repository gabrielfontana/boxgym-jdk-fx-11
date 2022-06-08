package boxgym.dao;

import boxgym.jdbc.ConnectionFactory;
import boxgym.model.SaleProduct;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.dbutils.DbUtils;

public class SaleProductDao {
    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;

    public SaleProductDao() {
        this.conn = new ConnectionFactory().getConnection();
    }
    
    public boolean create(SaleProduct item) {
        String sql = "INSERT INTO `sale_product` (`fkSale`, `fkProduct`, `amount`, `unitPrice`) VALUES (?, ?, ?, ?);";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, item.getFkSale());
            ps.setInt(2, item.getFkProduct());
            ps.setInt(3, item.getAmount());
            ps.setBigDecimal(4, item.getUnitPrice());
            ps.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(SaleProductDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }
}
