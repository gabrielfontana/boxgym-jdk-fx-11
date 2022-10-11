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
    
    public List<SheetWorkout> read(int selectedSheet) {
        List<SheetWorkout> workoutsList = new ArrayList<>();
        String sql = "SELECT w.description AS `tempWorkoutDescription`, s_w.dayOfTheWeek, s_w.createdAt, s_w.updatedAt "
                + "FROM `sheet_workout` AS s_w INNER JOIN `workout` AS w "
                + "ON s_w.fkWorkout = w.workoutId "
                + "WHERE s_w.fkSheet = " + selectedSheet + ";";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                SheetWorkout sw = new SheetWorkout();
                sw.setTempWorkoutDescription(rs.getString("tempWorkoutDescription"));
                sw.setDayOfTheWeek(rs.getString("dayOfTheWeek"));
                sw.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
                sw.setUpdatedAt(rs.getTimestamp("updatedAt").toLocalDateTime());
                workoutsList.add(sw);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SheetWorkoutDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return workoutsList;
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
