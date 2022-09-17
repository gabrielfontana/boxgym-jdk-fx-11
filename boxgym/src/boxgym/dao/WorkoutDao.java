package boxgym.dao;

import boxgym.helper.ExcelFileHelper;
import boxgym.jdbc.ConnectionFactory;
import boxgym.model.Workout;
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

public class WorkoutDao {

    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;

    public WorkoutDao() {
        this.conn = new ConnectionFactory().getConnection();
    }

    public boolean create(Workout workout) {
        String sql = "INSERT INTO `workout` (`description`, `goal`, `sessions`) VALUES (?, ?, ?);";

        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, workout.getDescription());
            ps.setString(2, workout.getGoal());
            ps.setInt(3, workout.getSessions());
            ps.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(WorkoutDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            //DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }

    public int getWorkoutId() {
        String sql = "SELECT `workoutId` FROM `workout` ORDER BY `workoutId` DESC LIMIT 1;";
        int workoutId = 0;

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                workoutId = rs.getInt("workoutId");
            }
        } catch (SQLException ex) {
            Logger.getLogger(WorkoutDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return workoutId;
    }

    public boolean deleteLastEntry() {
        String sql = "DELETE FROM `workout` ORDER BY `workoutId` DESC LIMIT 1;";

        try {
            ps = conn.prepareStatement(sql);
            ps.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(WorkoutDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }
    
    public List<Workout> read() {
        List<Workout> workoutsList = new ArrayList<>();
        String sql = "SELECT * FROM `workout`;";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Workout w = new Workout();
                w.setWorkoutId(rs.getInt("workoutId"));
                w.setDescription(rs.getString("description"));
                w.setGoal(rs.getString("goal"));
                w.setSessions(rs.getInt("sessions"));
                w.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
                w.setUpdatedAt(rs.getTimestamp("updatedAt").toLocalDateTime());
                workoutsList.add(w);
            }
        } catch (SQLException ex) {
            Logger.getLogger(WorkoutDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return workoutsList;
    }
    
    public boolean delete(Workout workout) {
        String sql = "DELETE FROM `workout` WHERE `workoutId` = ?;";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, workout.getWorkoutId());
            ps.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(WorkoutDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }
}
