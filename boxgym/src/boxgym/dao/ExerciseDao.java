package boxgym.dao;

import boxgym.helper.ExcelFileHelper;
import boxgym.jdbc.ConnectionFactory;
import boxgym.model.Exercise;
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
import java.util.LinkedHashMap;
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

public class ExerciseDao {

    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;

    public ExerciseDao() {
        this.conn = new ConnectionFactory().getConnection();
    }
    
    public boolean create(Exercise exercise) {
        String sql = "INSERT INTO `exercise` (`name`, `abbreviation`, `exerciseType`, `exerciseGroup`, `description`, `instruction`) "
                + "VALUES (?, ?, ?, ?, ?, ?);";

        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, exercise.getName());
            ps.setString(2, exercise.getAbbreviation());
            ps.setString(3, exercise.getExerciseType());
            ps.setString(4, exercise.getExerciseGroup());
            ps.setString(5, exercise.getDescription());
            ps.setString(6, exercise.getInstruction());
            ps.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ExerciseDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }
    
    public List<Exercise> read() {
        List<Exercise> exercisesList = new ArrayList<>();
        String sql = "SELECT * FROM `exercise`;";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Exercise e = new Exercise();
                e.setExerciseId(rs.getInt("exerciseId"));
                e.setName(rs.getString("name"));
                e.setAbbreviation(rs.getString("abbreviation"));
                e.setExerciseType(rs.getString("exerciseType"));
                e.setExerciseGroup(rs.getString("exerciseGroup"));
                e.setDescription(rs.getString("description"));
                e.setInstruction(rs.getString("instruction"));
                e.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
                e.setUpdatedAt(rs.getTimestamp("updatedAt").toLocalDateTime());
                exercisesList.add(e);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ExerciseDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return exercisesList;
    }
    
    public boolean update(Exercise exercise) {
        String sql = "UPDATE `exercise` SET `name` = ?, `abbreviation` = ?, `exerciseType` = ?, `exerciseGroup` = ?, `description` = ?, `instruction` = ? "
                + "WHERE `exerciseId` = ?;";

        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, exercise.getName());
            ps.setString(2, exercise.getAbbreviation());
            ps.setString(3, exercise.getExerciseType());
            ps.setString(4, exercise.getExerciseGroup());
            ps.setString(5, exercise.getDescription());
            ps.setString(6, exercise.getInstruction());
            ps.setInt(7, exercise.getExerciseId());
            ps.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ExerciseDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }
    
    public boolean delete(Exercise exercise) {
        String sql = "DELETE FROM `exercise` WHERE `exerciseId` = ?;";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, exercise.getExerciseId());
            ps.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ExerciseDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }
}
