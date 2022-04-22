package boxgym.dao;

import boxgym.jdbc.ConnectionFactory;
import boxgym.model.StockEntry;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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

    public boolean create(StockEntry entry) {
        String sql = "INSERT INTO `stockentry` (`fkSupplier`, `invoiceIssueDate`, `invoiceNumber`) VALUES (?, ?, ?);";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, entry.getFkSupplier());
            ps.setDate(2, java.sql.Date.valueOf(entry.getInvoiceIssueDate()));
            ps.setString(3, entry.getInvoiceNumber());
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

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                stockEntryId = rs.getInt("stockEntryId");
            }
        } catch (SQLException ex) {
            Logger.getLogger(StockEntryDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return stockEntryId;
    }

    public boolean deleteLastEntry() {
        String sql = "DELETE FROM `stockentry` ORDER BY `stockEntryId` DESC LIMIT 1;";

        try {
            ps = conn.prepareStatement(sql);
            ps.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(StockEntryDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }

    public List<StockEntry> read() {
        List<StockEntry> stockEntriesList = new ArrayList<>();
        String sql = "SELECT se.stockEntryId, se.fkSupplier, s.corporateName AS `tempSupplierName`, se.invoiceIssueDate, se.invoiceNumber, se.createdAt, se.updatedAt "
                + "FROM `stockentry` AS se INNER JOIN `supplier` AS s "
                + "ON se.fkSupplier = s.supplierId "
                + "ORDER BY se.stockEntryId ASC;";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                StockEntry se = new StockEntry();
                se.setStockEntryId(rs.getInt("stockEntryId"));
                se.setFkSupplier(rs.getInt("fkSupplier"));
                se.setTempSupplierName(rs.getString("tempSupplierName"));
                se.setInvoiceIssueDate(rs.getDate("invoiceIssueDate").toLocalDate());
                se.setInvoiceNumber(rs.getString("invoiceNumber"));
                se.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
                se.setUpdatedAt(rs.getTimestamp("updatedAt").toLocalDateTime());
                stockEntriesList.add(se);
            }
        } catch (SQLException ex) {
            Logger.getLogger(StockEntryDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return stockEntriesList;
    }
    
    public int count() {
        int count = 0;
        String sql = "SELECT count(*) AS `count` FROM `stockentry`;";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException ex) {
            Logger.getLogger(StockEntryDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return count;
    }
}
