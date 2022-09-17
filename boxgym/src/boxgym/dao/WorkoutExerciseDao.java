package boxgym.dao;

import boxgym.jdbc.ConnectionFactory;
import boxgym.model.Workout;
import boxgym.model.WorkoutExercise;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.dbutils.DbUtils;

public class WorkoutExerciseDao {
    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;

    public WorkoutExerciseDao() {
        this.conn = new ConnectionFactory().getConnection();
    }
    
    public boolean create(WorkoutExercise entry) {
        String sql = "INSERT INTO `workout_exercise` (`fkWorkout`, `fkExercise`, `sets`, `reps`, `rest`) VALUES (?, ?, ?, ?, ?);";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, entry.getFkWorkout());
            ps.setInt(2, entry.getFkExercise());
            ps.setInt(3, entry.getSets());
            ps.setInt(4, entry.getReps());
            ps.setInt(5, entry.getRest());
            ps.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(WorkoutExerciseDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }
    
    public boolean delete(Workout workout) {
        String sql = "DELETE FROM `workout_exercise` WHERE `fkWorkout` = ?;";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, workout.getWorkoutId());
            ps.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(WorkoutExerciseDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }
}
