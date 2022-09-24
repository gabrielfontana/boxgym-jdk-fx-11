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

    public List<WorkoutExercise> read(int selectedWorkout) {
        List<WorkoutExercise> exercisesList = new ArrayList<>();
        String sql = "SELECT e.name AS `tempExerciseName`, w_e.sets, w_e.reps, w_e.rest "
                + "FROM `workout_exercise` AS w_e INNER JOIN `exercise` AS e "
                + "ON w_e.fkExercise = e.exerciseId "
                + "WHERE w_e.fkWorkout = " + selectedWorkout + ";";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                WorkoutExercise we = new WorkoutExercise();
                we.setTempExerciseName(rs.getString("tempExerciseName"));
                we.setSets(rs.getInt("sets"));
                we.setReps(rs.getInt("reps"));
                we.setRest(rs.getInt("rest"));
                exercisesList.add(we);
            }
        } catch (SQLException ex) {
            Logger.getLogger(WorkoutExerciseDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return exercisesList;
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
