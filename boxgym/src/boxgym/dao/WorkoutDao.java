package boxgym.dao;

import boxgym.helper.ExcelFileHelper;
import boxgym.jdbc.ConnectionFactory;
import boxgym.model.Workout;
import boxgym.model.WorkoutExercise;
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
        String sql = "INSERT INTO `workout` (`description`, `goal`, `sessions`, `day`) VALUES (?, ?, ?, ?);";

        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, workout.getDescription());
            ps.setString(2, workout.getGoal());
            ps.setInt(3, workout.getSessions());
            ps.setString(4, workout.getDay());
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
                w.setDay(rs.getString("day"));
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

    public boolean createExcelFile(String filePath) {
        String sql = "SELECT * FROM `workout` ORDER BY workoutId ASC;";

        try {
            XSSFWorkbook workbook = new XSSFWorkbook();

            XSSFCellStyle infoStyle = ExcelFileHelper.excelStyle(workbook, "Arial", true, BorderStyle.NONE, new byte[]{(byte) 255, (byte) 255, (byte) 255});
            XSSFCellStyle headerStyle = ExcelFileHelper.excelStyle(workbook, "Arial", true, BorderStyle.THIN, new byte[]{(byte) 205, (byte) 205, (byte) 205});
            XSSFCellStyle subHeaderStyle = ExcelFileHelper.excelStyle(workbook, "Arial", true, BorderStyle.THIN, new byte[]{(byte) 230, (byte) 230, (byte) 230});
            XSSFCellStyle defaultStyle = ExcelFileHelper.excelStyle(workbook, "Arial", false, BorderStyle.THIN, new byte[]{(byte) 255, (byte) 255, (byte) 255});

            XSSFSheet sheet = workbook.createSheet("Treinos Predefinidos");
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));
            ExcelFileHelper.createStyledCell(sheet.createRow(0), 0, "Relatório gerado em: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")), infoStyle);

            List<String> fields = Arrays.asList("ID", "Descrição", "Objetivo", "Sessões", "Dia da Semana", "Criação", "Modificação");
            XSSFRow headerRow = sheet.createRow(2);
            for (int i = 0; i < fields.size(); i++) {
                ExcelFileHelper.createStyledCell(headerRow, i, fields.get(i), headerStyle);
            }

            List<String> relatedExercisesFields = Arrays.asList("Nome do Exercício", "Séries", "Repetições", "Descanso");

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            int rowIndex = 3;
            while (rs.next()) {
                // Workout
                int currentWorkoutId = rs.getInt("workoutId");
                XSSFRow row = sheet.createRow(rowIndex);
                ExcelFileHelper.createStyledCell(row, 0, rs.getInt("workoutId"), defaultStyle);
                ExcelFileHelper.createStyledCell(row, 1, rs.getString("description"), defaultStyle);
                ExcelFileHelper.createStyledCell(row, 2, rs.getString("goal"), defaultStyle);
                ExcelFileHelper.createStyledCell(row, 3, rs.getInt("sessions"), defaultStyle);
                ExcelFileHelper.createStyledCell(row, 4, rs.getString("day"), defaultStyle);
                ExcelFileHelper.createStyledDateTimeCell(row, 5, rs.getString("createdAt"), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"), defaultStyle);
                ExcelFileHelper.createStyledDateTimeCell(row, 6, rs.getString("updatedAt"), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"), defaultStyle);
                rowIndex++;

                // WorkoutExercise
                XSSFRow row2 = sheet.createRow(rowIndex);
                WorkoutExerciseDao dao = new WorkoutExerciseDao();
                List<WorkoutExercise> relatedExercises = dao.read(currentWorkoutId);
                int firstRowForGroup = rowIndex;
                for (int i = 0; i < relatedExercisesFields.size(); i++) {
                    ExcelFileHelper.createStyledCell(row2, i + 1, relatedExercisesFields.get(i), subHeaderStyle);
                }
                rowIndex++;
                for (int i = 0; i < relatedExercises.size(); i++) {
                    XSSFRow row3 = sheet.createRow(rowIndex);
                    ExcelFileHelper.createStyledCell(row3, 1, relatedExercises.get(i).getTempExerciseName(), defaultStyle);
                    ExcelFileHelper.createStyledCell(row3, 2, relatedExercises.get(i).getSets(), defaultStyle);
                    ExcelFileHelper.createStyledCell(row3, 3, relatedExercises.get(i).getReps(), defaultStyle);
                    ExcelFileHelper.createStyledCell(row3, 4, relatedExercises.get(i).getRest(), defaultStyle);
                    rowIndex++;
                }
                int lastRowForGroup = rowIndex;
                sheet.groupRow(firstRowForGroup, lastRowForGroup);
                rowIndex++;
            }
            for (int i = 0; i < fields.size(); i++) {
                sheet.autoSizeColumn(i);
            }

            FileOutputStream fileOut = new FileOutputStream(filePath);
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();
            return true;
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(StockEntryDao.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(StockEntryDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }
}
