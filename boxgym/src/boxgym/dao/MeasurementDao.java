package boxgym.dao;

import boxgym.helper.ExcelFileHelper;
import boxgym.jdbc.ConnectionFactory;
import boxgym.model.Measurement;
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

public class MeasurementDao {

    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;

    public MeasurementDao() {
        this.conn = new ConnectionFactory().getConnection();
    }

    public boolean create(Measurement measurement) {
        String sql = "INSERT INTO `measurement` (`fkCustomer`, `measurementDate`, `height`, `weight`, "
                + "`neck`, `shoulder`, `rightArm`, `leftArm`, `rightForearm`, `leftForearm`, `thorax`, `waist`, `abdomen`, `hip`, "
                + "`rightThigh`, `leftThigh`, `rightCalf`, `leftCalf`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, measurement.getFkCustomer());
            ps.setDate(2, java.sql.Date.valueOf(measurement.getMeasurementDate()));
            ps.setInt(3, measurement.getHeight());
            ps.setFloat(4, measurement.getWeight());
            ps.setFloat(5, measurement.getNeck());
            ps.setFloat(6, measurement.getShoulder());
            ps.setFloat(7, measurement.getRightArm());
            ps.setFloat(8, measurement.getLeftArm());
            ps.setFloat(9, measurement.getRightForearm());
            ps.setFloat(10, measurement.getLeftForearm());
            ps.setFloat(11, measurement.getThorax());
            ps.setFloat(12, measurement.getWaist());
            ps.setFloat(13, measurement.getAbdomen());
            ps.setFloat(14, measurement.getHip());
            ps.setFloat(15, measurement.getRightThigh());
            ps.setFloat(16, measurement.getLeftThigh());
            ps.setFloat(17, measurement.getRightCalf());
            ps.setFloat(18, measurement.getLeftCalf());
            ps.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(MeasurementDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }

    public List<Measurement> read() {
        List<Measurement> measurementsList = new ArrayList<>();
        String sql = "SELECT m.measurementId, m.fkCustomer, c.name AS `tempCustomerName`, m.measurementDate, m.height, m.weight, "
                + "m.neck, m.shoulder, m.rightArm, m.leftArm, m.rightForearm, m.leftForearm, m.thorax, "
                + "m.waist, m.abdomen, m.hip, m.rightThigh, m.leftThigh, m.rightCalf, m.leftCalf, m.createdAt, m.updatedAt "
                + "FROM `measurement` AS m INNER JOIN `customer` AS c "
                + "ON m.fkCustomer = c.customerId "
                + "ORDER BY m.measurementId ASC;";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Measurement m = new Measurement();
                m.setMeasurementId(rs.getInt("measurementId"));
                m.setFkCustomer(rs.getInt("fkCustomer"));
                m.setTempCustomerName(rs.getString("tempCustomerName"));
                m.setMeasurementDate(rs.getDate("measurementDate").toLocalDate());
                m.setHeight(rs.getInt("height"));
                m.setWeight(rs.getFloat("weight"));
                m.setNeck(rs.getFloat("neck"));
                m.setShoulder(rs.getFloat("shoulder"));
                m.setRightArm(rs.getFloat("rightArm"));
                m.setLeftArm(rs.getFloat("leftArm"));
                m.setRightForearm(rs.getFloat("rightForearm"));
                m.setLeftForearm(rs.getFloat("leftForearm"));
                m.setThorax(rs.getFloat("thorax"));
                m.setWaist(rs.getFloat("waist"));
                m.setAbdomen(rs.getFloat("abdomen"));
                m.setHip(rs.getFloat("hip"));
                m.setRightThigh(rs.getFloat("rightThigh"));
                m.setLeftThigh(rs.getFloat("leftThigh"));
                m.setRightCalf(rs.getFloat("rightCalf"));
                m.setLeftCalf(rs.getFloat("leftCalf"));
                m.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
                m.setUpdatedAt(rs.getTimestamp("updatedAt").toLocalDateTime());
                measurementsList.add(m);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MeasurementDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return measurementsList;
    }

    public boolean update(Measurement measurement) {
        String sql = "UPDATE `measurement` SET `measurementDate` = ?, `height` = ?, `weight` = ?, "
                + "`neck` = ?, `shoulder` = ?, `rightArm` = ?, `leftArm` = ?, `rightForearm` = ?, `leftForearm` = ?, "
                + "`thorax` = ?, `waist` = ?, `abdomen` = ?, `hip` = ?, `rightThigh` = ?, `leftThigh` = ?, "
                + "`rightCalf` = ?, `leftCalf` = ? WHERE `measurementId` = ?;";

        try {
            ps = conn.prepareStatement(sql);
            ps.setDate(1, java.sql.Date.valueOf(measurement.getMeasurementDate()));
            ps.setInt(2, measurement.getHeight());
            ps.setFloat(3, measurement.getWeight());
            ps.setFloat(4, measurement.getNeck());
            ps.setFloat(5, measurement.getShoulder());
            ps.setFloat(6, measurement.getRightArm());
            ps.setFloat(7, measurement.getLeftArm());
            ps.setFloat(8, measurement.getRightForearm());
            ps.setFloat(9, measurement.getLeftForearm());
            ps.setFloat(10, measurement.getThorax());
            ps.setFloat(11, measurement.getWaist());
            ps.setFloat(12, measurement.getAbdomen());
            ps.setFloat(13, measurement.getHip());
            ps.setFloat(14, measurement.getRightThigh());
            ps.setFloat(15, measurement.getLeftThigh());
            ps.setFloat(16, measurement.getRightCalf());
            ps.setFloat(17, measurement.getLeftCalf());
            ps.setInt(18, measurement.getMeasurementId());
            ps.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(MeasurementDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }

    public boolean delete(Measurement measurement) {
        String sql = "DELETE FROM `measurement` WHERE `measurementId` = ?;";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, measurement.getMeasurementId());
            ps.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(MeasurementDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }

    public boolean createExcelFile(String filePath) {
        String sql = "SELECT m.measurementId, m.fkCustomer, c.name AS `tempCustomerName`, m.measurementDate, m.height, m.weight, "
                + "m.neck, m.shoulder, m.rightArm, m.leftArm, m.rightForearm, m.leftForearm, m.thorax, "
                + "m.waist, m.abdomen, m.hip, m.rightThigh, m.leftThigh, m.rightCalf, m.leftCalf, m.createdAt, m.updatedAt "
                + "FROM `measurement` AS m INNER JOIN `customer` AS c "
                + "ON m.fkCustomer = c.customerId "
                + "ORDER BY m.measurementId ASC;";

        try {
            XSSFWorkbook workbook = new XSSFWorkbook();

            XSSFCellStyle infoStyle = ExcelFileHelper.excelStyle(workbook, "Arial", true, BorderStyle.NONE, new byte[]{(byte) 255, (byte) 255, (byte) 255});
            XSSFCellStyle headerStyle = ExcelFileHelper.excelStyle(workbook, "Arial", true, BorderStyle.THIN, new byte[]{(byte) 205, (byte) 205, (byte) 205});
            XSSFCellStyle defaultStyle = ExcelFileHelper.excelStyle(workbook, "Arial", false, BorderStyle.THIN, new byte[]{(byte) 255, (byte) 255, (byte) 255});

            XSSFSheet sheet = workbook.createSheet("Medidas");
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));
            ExcelFileHelper.createStyledCell(sheet.createRow(0), 0, "Relatório gerado em: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")), infoStyle);

            List<String> fields = Arrays.asList("ID", "Cliente", "Data", "Altura (em cm)", "Peso (em kg)",
                    "Pescoço", "Ombro", "Braço Direito", "Braço Esquerdo", "Antebraço Direito", "Antebraço Esquerdo",
                    "Tórax", "Cintura", "Abdômen", "Quadril", "Coxa Direita", "Coxa Esquerda",
                    "Panturrilha Direita", "Panturrilha Esquerda", "Criação", "Modificação");
            XSSFRow headerRow = sheet.createRow(2);
            for (int i = 0; i < fields.size(); i++) {
                ExcelFileHelper.createStyledCell(headerRow, i, fields.get(i), headerStyle);
            }

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            int rowIndex = 3;
            while (rs.next()) {
                XSSFRow row = sheet.createRow(rowIndex);
                ExcelFileHelper.createStyledCell(row, 0, rs.getInt("measurementId"), defaultStyle);
                ExcelFileHelper.createStyledCell(row, 1, rs.getString("tempCustomerName"), defaultStyle);
                ExcelFileHelper.createStyledDateCell(row, 2, rs.getString("measurementDate"), DateTimeFormatter.ofPattern("dd/MM/yyyy"), defaultStyle);
                ExcelFileHelper.createStyledCell(row, 3, rs.getInt("height"), defaultStyle);
                ExcelFileHelper.createStyledCell(row, 4, rs.getDouble("weight"), defaultStyle);
                ExcelFileHelper.createStyledCell(row, 5, rs.getDouble("neck"), defaultStyle);
                ExcelFileHelper.createStyledCell(row, 6, rs.getDouble("shoulder"), defaultStyle);
                ExcelFileHelper.createStyledCell(row, 7, rs.getDouble("rightArm"), defaultStyle);
                ExcelFileHelper.createStyledCell(row, 8, rs.getDouble("leftArm"), defaultStyle);
                ExcelFileHelper.createStyledCell(row, 9, rs.getDouble("rightForearm"), defaultStyle);
                ExcelFileHelper.createStyledCell(row, 10, rs.getDouble("leftForearm"), defaultStyle);
                ExcelFileHelper.createStyledCell(row, 11, rs.getDouble("thorax"), defaultStyle);
                ExcelFileHelper.createStyledCell(row, 12, rs.getDouble("waist"), defaultStyle);
                ExcelFileHelper.createStyledCell(row, 13, rs.getDouble("abdomen"), defaultStyle);
                ExcelFileHelper.createStyledCell(row, 14, rs.getDouble("hip"), defaultStyle);
                ExcelFileHelper.createStyledCell(row, 15, rs.getDouble("rightThigh"), defaultStyle);
                ExcelFileHelper.createStyledCell(row, 16, rs.getDouble("leftThigh"), defaultStyle);
                ExcelFileHelper.createStyledCell(row, 17, rs.getDouble("rightCalf"), defaultStyle);
                ExcelFileHelper.createStyledCell(row, 18, rs.getDouble("leftCalf"), defaultStyle);
                ExcelFileHelper.createStyledDateTimeCell(row, 19, rs.getString("createdAt"), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"), defaultStyle);
                ExcelFileHelper.createStyledDateTimeCell(row, 20, rs.getString("updatedAt"), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"), defaultStyle);
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
            Logger.getLogger(MeasurementDao.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MeasurementDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }
}
