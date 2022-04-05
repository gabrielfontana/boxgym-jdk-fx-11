package boxgym.dao;

import boxgym.jdbc.ConnectionFactory;
import boxgym.model.StockEntryProduct;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
}
