package boxgym.dao;

import boxgym.jdbc.ConnectionFactory;
import boxgym.model.Sheet;
import boxgym.model.SheetWorkout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.dbutils.DbUtils;

public class SheetWorkoutDao {
    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;

    public SheetWorkoutDao() {
        this.conn = new ConnectionFactory().getConnection();
    }
    
    public boolean create(SheetWorkout entry) {
        String sql = "INSERT INTO `sheet_workout` (`fkSheet`, `fkWorkout`, `dayOfTheWeek`) VALUES (?, ?, ?);";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, entry.getFkSheet());
            ps.setInt(2, entry.getFkWorkout());
            ps.setString(3, entry.getDayOfTheWeek());
            ps.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(SheetWorkoutDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }
    
    public boolean delete(Sheet sheet) {
        String sql = "DELETE FROM `sheet_workout` WHERE `fkSheet` = ?;";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, sheet.getSheetId());
            ps.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(SheetWorkoutDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }
}
