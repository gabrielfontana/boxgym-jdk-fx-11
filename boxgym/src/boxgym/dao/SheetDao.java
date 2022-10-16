package boxgym.dao;

import boxgym.helper.ExcelFileHelper;
import boxgym.jdbc.ConnectionFactory;
import boxgym.model.Sheet;
import boxgym.model.SheetWorkout;
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
    
    public int checkExpiredSheets() {
        String sql = "SELECT COUNT(*) AS `expiredSheets` FROM `sheet` WHERE `expirationDate` < NOW() AND `status` = 1;";
        int expiredSheets = 0;

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                expiredSheets = rs.getInt("expiredSheets");
                return expiredSheets;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return expiredSheets;
    }
    
    public boolean updateExpiredSheetsStatus() {
        String sql = "UPDATE `sheet` SET `status` = 0 WHERE `expirationDate` < NOW();";

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
    
    public boolean createExcelFile(String filePath) {
        String sql = "SELECT s.sheetId, s.fkCustomer, c.name AS `tempCustomerName`, s.description, s.expirationDate, s.comments, s.status, s.createdAt, s.updatedAt "
                + "FROM `sheet` AS s INNER JOIN `customer` AS c "
                + "ON s.fkCustomer = c.customerId "
                + "ORDER BY s.sheetId ASC;";
        
        try {
            XSSFWorkbook workbook = new XSSFWorkbook();

            XSSFCellStyle infoStyle = ExcelFileHelper.excelStyle(workbook, "Arial", true, BorderStyle.NONE, new byte[]{(byte) 255, (byte) 255, (byte) 255});
            XSSFCellStyle headerStyle = ExcelFileHelper.excelStyle(workbook, "Arial", true, BorderStyle.THIN, new byte[]{(byte) 205, (byte) 205, (byte) 205});
            XSSFCellStyle subHeaderStyle = ExcelFileHelper.excelStyle(workbook, "Arial", true, BorderStyle.THIN, new byte[]{(byte) 230, (byte) 230, (byte) 230});
            XSSFCellStyle defaultStyle = ExcelFileHelper.excelStyle(workbook, "Arial", false, BorderStyle.THIN, new byte[]{(byte) 255, (byte) 255, (byte) 255});
            
            XSSFSheet sheet = workbook.createSheet("Fichas");
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));
            ExcelFileHelper.createStyledCell(sheet.createRow(0), 0, "Relatório gerado em: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")), infoStyle);
            
            List<String> fields = Arrays.asList("ID", "Cliente", "Descrição", "Data de Validade", "Observações", "Status", "Criação", "Modificação");
            XSSFRow headerRow = sheet.createRow(2);
            for (int i = 0; i < fields.size(); i++) {
                ExcelFileHelper.createStyledCell(headerRow, i, fields.get(i), headerStyle);
            }
            
            List<String> relatedWorkoutsFields = Arrays.asList("Nome do Treino", "Dia da Semana");
            
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            int rowIndex = 3;
            while (rs.next()) {
                // Sheet
                int currentSheetId = rs.getInt("sheetId");
                XSSFRow row = sheet.createRow(rowIndex);
                ExcelFileHelper.createStyledCell(row, 0, rs.getInt("sheetId"), defaultStyle);
                ExcelFileHelper.createStyledCell(row, 1, rs.getString("tempCustomerName"), defaultStyle);
                ExcelFileHelper.createStyledCell(row, 2, rs.getString("description"), defaultStyle);
                ExcelFileHelper.createStyledDateCell(row, 3, rs.getString("expirationDate"), DateTimeFormatter.ofPattern("dd/MM/yyyy"), defaultStyle);
                ExcelFileHelper.createStyledCell(row, 4, rs.getString("comments"), defaultStyle);
                ExcelFileHelper.createStyledCell(row, 5, rs.getString("status"), defaultStyle);
                ExcelFileHelper.createStyledDateTimeCell(row, 6, rs.getString("createdAt"), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"), defaultStyle);
                ExcelFileHelper.createStyledDateTimeCell(row, 7, rs.getString("updatedAt"), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"), defaultStyle);
                rowIndex++;
                
                // SheetWorkout
                XSSFRow row2 = sheet.createRow(rowIndex);
                SheetWorkoutDao dao = new SheetWorkoutDao();
                List<SheetWorkout> relatedWorkouts = dao.read(currentSheetId);
                int firstRowForGroup = rowIndex;
                for (int i = 0; i < relatedWorkoutsFields.size(); i++) {
                    ExcelFileHelper.createStyledCell(row2, i + 1, relatedWorkoutsFields.get(i), subHeaderStyle);
                }
                rowIndex++;
                for (int i = 0; i < relatedWorkouts.size(); i++) {
                    XSSFRow row3 = sheet.createRow(rowIndex);
                    ExcelFileHelper.createStyledCell(row3, 1, relatedWorkouts.get(i).getTempWorkoutDescription(), defaultStyle);
                    ExcelFileHelper.createStyledCell(row3, 2, relatedWorkouts.get(i).getDayOfTheWeek(), defaultStyle);
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
