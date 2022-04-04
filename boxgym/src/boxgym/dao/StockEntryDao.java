package boxgym.dao;

import boxgym.jdbc.ConnectionFactory;
import boxgym.model.StockEntry;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.dbutils.DbUtils;

public class StockEntryDao {

    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;

    public StockEntryDao() {
        this.conn = new ConnectionFactory().getConnection();
    }

    public boolean create(StockEntry stockEntry) {
        String sql = "INSERT INTO `stockentry` (`invoiceIssueDate`, `invoiceNumber`, `fkSupplier`) VALUES (?, ?, ?);";

        try {
            ps = conn.prepareStatement(sql);
            ps.setDate(1, java.sql.Date.valueOf(stockEntry.getInvoiceIssueDate()));
            ps.setString(2, stockEntry.getInvoiceNumber());
            ps.setInt(3, stockEntry.getFkSupplier());
            ps.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(StockEntryDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            //DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }
    
    public int getStockEntryId() {
        String sql = "SELECT `stockEntryId` FROM `stockentry` ORDER BY `stockEntryId` DESC LIMIT 1;";        
        int stockEntryId = 0;
        
        try{
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if(rs.next()) {
                stockEntryId = rs.getInt("stockEntryId");
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return stockEntryId;
    }
}
