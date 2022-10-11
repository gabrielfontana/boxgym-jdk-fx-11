package boxgym.dao;

import boxgym.helper.ExcelFileHelper;
import boxgym.jdbc.ConnectionFactory;
import boxgym.model.Sheet;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.dbutils.DbUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class SheetDao {
    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;

    public SheetDao() {
        this.conn = new ConnectionFactory().getConnection();
    }
    
    public boolean create(Sheet sheet) {
        String sql = "INSERT INTO `sheet` (`fkCustomer`, `description`, `expirationDate`, `comments`, `status`) VALUES (?, ?, ?, ?, ?);";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, sheet.getFkCustomer());
            ps.setString(2, sheet.getDescription());
            ps.setDate(3, java.sql.Date.valueOf(sheet.getExpirationDate()));
            ps.setString(4, sheet.getComments());
            ps.setString(5, sheet.getStatus());
            ps.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(SheetDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            //DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }
    
    public int getSheetId() {
        String sql = "SELECT `sheetId` FROM `sheet` ORDER BY `sheetId` DESC LIMIT 1;";
        int sheetId = 0;

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                sheetId = rs.getInt("sheetId");
            }
        } catch (SQLException ex) {
            Logger.getLogger(SheetDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return sheetId;
    }
    
    public boolean deleteLastEntry() {
        String sql = "DELETE FROM `sheet` ORDER BY `sheetId` DESC LIMIT 1;";

        try {
            ps = conn.prepareStatement(sql);
            ps.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(SheetDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }
    
    public List<Sheet> readOnlyRowsWithActiveStatus() {
        List<Sheet> sheetsList = new ArrayList<>();
        String sql = "SELECT s.sheetId, s.fkCustomer, c.name AS `tempCustomerName`, s.description, s.expirationDate, s.comments, s.status, s.createdAt, s.updatedAt "
                + "FROM `sheet` AS s INNER JOIN `customer` AS c "
                + "ON s.fkCustomer = c.customerId "
                + "WHERE s.status = 1 "
                + "ORDER BY s.sheetId ASC;";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Sheet s = new Sheet();
                s.setSheetId(rs.getInt("sheetId"));
                s.setFkCustomer(rs.getInt("fkCustomer"));
                s.setTempCustomerName(rs.getString("tempCustomerName"));
                s.setDescription(rs.getString("description"));
                s.setExpirationDate(rs.getDate("expirationDate").toLocalDate());
                s.setComments(rs.getString("comments"));
                s.setStatus(rs.getString("status"));
                s.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
                s.setUpdatedAt(rs.getTimestamp("updatedAt").toLocalDateTime());
                sheetsList.add(s);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SheetDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return sheetsList;
    }
    
    public boolean updateComments(String comments, int sheetId) {
        String sql = "UPDATE `sheet` SET `comments` = '" + comments + "' WHERE `sheetId` = " + sheetId + ";";

        try {
            ps = conn.prepareStatement(sql);
            ps.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(SheetDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }
    
    public int countSheets(int fkCustomer) {
        String sql = "SELECT COUNT(*) AS `amount` FROM `sheet` WHERE `fkCustomer` = " + fkCustomer + " AND `status` = 1;";
        int amount = 0;
                
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                amount = rs.getInt("amount");
            }
        } catch (SQLException ex) {
            Logger.getLogger(SheetDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return amount;
    }
    
    public boolean delete(Sheet sheet) {
        String sql = "DELETE FROM `sheet` WHERE `sheetId` = ?;";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, sheet.getSheetId());
            ps.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(SheetDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }
}
