package boxgym.dao;

import boxgym.helper.ExcelFileHelper;
import boxgym.jdbc.ConnectionFactory;
import boxgym.model.Measurement;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
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

public class MeasurementDao {
    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;

    public MeasurementDao() {
        this.conn = new ConnectionFactory().getConnection();
    }
    
    public boolean create(Measurement measurement) {
        String sql = "INSERT INTO `measurement` (`fkCustomer`, `measurementDate`, `age`, `height`, `weight`, "
                + "`neck`, `shoulder`, `rightArm`, `leftArm`, `rightForearm`, `leftForearm`, `thorax`, `waist`, `abdomen`, `hip`, "
                + "`rightThigh`, `leftThigh`, `rightCalf`, `leftCalf`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, measurement.getFkCustomer());
            ps.setDate(2, java.sql.Date.valueOf(measurement.getMeasurementDate()));
            ps.setInt(3, measurement.getAge());
            ps.setInt(4, measurement.getHeight());
            ps.setFloat(5, measurement.getWeight());
            ps.setFloat(6, measurement.getNeck());
            ps.setFloat(7, measurement.getShoulder());
            ps.setFloat(8, measurement.getRightArm());
            ps.setFloat(9, measurement.getLeftArm());
            ps.setFloat(10, measurement.getRightForearm());
            ps.setFloat(11, measurement.getLeftForearm());
            ps.setFloat(12, measurement.getThorax());
            ps.setFloat(13, measurement.getWaist());
            ps.setFloat(14, measurement.getAbdomen());
            ps.setFloat(15, measurement.getHip());
            ps.setFloat(16, measurement.getRightThigh());
            ps.setFloat(17, measurement.getLeftThigh());
            ps.setFloat(18, measurement.getRightCalf());
            ps.setFloat(19, measurement.getLeftCalf());
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
        String sql = "SELECT * FROM `measurement`;";
    
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Measurement m = new Measurement();
                m.setFkCustomer(rs.getInt("fkCustomer"));
                m.setTempCustomerName(rs.getString("tempCustomerName"));
                m.setMeasurementDate(rs.getDate("measurementDate").toLocalDate());
                m.setAge(rs.getInt("age"));
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
        String sql = "UPDATE `measurement` SET `measurementDate` = ?, `age` = ?, `height` = ?, `weight` = ?, "
                + "`neck` = ?, `shoulder` = ?, `rightArm` = ?, `leftArm` = ?, `rightForearm` = ?, `leftForearm` = ?, "
                + "`thorax` = ?, `waist` = ?, `abdomen` = ?, `hip` = ?, `rightThigh` = ?, `leftThigh` = ?, "
                + "`rightCalf` = ?, `leftCalf` = ? WHERE `measurementId` = ?;";

        try {
            ps = conn.prepareStatement(sql);
            ps.setDate(1, java.sql.Date.valueOf(measurement.getMeasurementDate()));
            ps.setInt(2, measurement.getAge());
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
            ps.setInt(19, measurement.getMeasurementId());
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
}
