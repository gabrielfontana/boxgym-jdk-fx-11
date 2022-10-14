package boxgym.dao;

import boxgym.helper.ExcelFileHelper;
import boxgym.jdbc.ConnectionFactory;
import boxgym.model.Customer;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.apache.commons.dbutils.DbUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CustomerDao {

    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;

    public CustomerDao() {
        this.conn = new ConnectionFactory().getConnection();
    }

    public boolean checkExistingCustomer(String personRegistry) {
        String cpf = personRegistry;
        String sql = "SELECT `personRegistry` FROM `customer` WHERE `personRegistry` = '" + cpf + "';";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return (cpf.equals(rs.getString("personRegistry")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            //DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }

    public boolean create(Customer customer) {
        String sql = "INSERT INTO `customer` (`personRegistry`, `name`, `sex`, `birthDate`, `email`, `phone`, `zipCode`, `address`, `addressComplement`, "
                + "`district`, `city`, `federativeUnit`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, customer.getPersonRegistry());
            ps.setString(2, customer.getName());
            ps.setString(3, customer.getSex());
            ps.setDate(4, java.sql.Date.valueOf(customer.getBirthDate()));
            ps.setString(5, customer.getEmail());
            ps.setString(6, customer.getPhone());
            ps.setString(7, customer.getZipCode());
            ps.setString(8, customer.getAddress());
            ps.setString(9, customer.getAddressComplement());
            ps.setString(10, customer.getDistrict());
            ps.setString(11, customer.getCity());
            ps.setString(12, customer.getFederativeUnit());
            ps.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }

    public LinkedHashMap<Integer, String> getCustomerForHashMap() {
        LinkedHashMap<Integer, String> map = new LinkedHashMap<>();
        String sql = "SELECT `customerId`, `name` FROM `customer`;";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            Customer c;
            while (rs.next()) {
                c = new Customer(rs.getInt("customerId"), rs.getString("name"));
                map.put(c.getCustomerId(), c.getName());
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return map;
    }

    public List<Customer> read() {
        List<Customer> customersList = new ArrayList<>();
        String sql = "SELECT * FROM `customer`;";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Customer c = new Customer();
                c.setCustomerId(rs.getInt("customerId"));
                c.setPersonRegistry(rs.getString("personRegistry"));
                c.setName(rs.getString("name"));
                c.setSex(rs.getString("sex"));
                c.setBirthDate(rs.getDate("birthDate").toLocalDate());
                c.setEmail(rs.getString("email"));
                c.setPhone(rs.getString("phone"));
                c.setZipCode(rs.getString("zipCode"));
                c.setAddress(rs.getString("address"));
                c.setAddressComplement(rs.getString("addressComplement"));
                c.setDistrict(rs.getString("district"));
                c.setCity(rs.getString("city"));
                c.setFederativeUnit(rs.getString("federativeUnit"));
                c.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
                c.setUpdatedAt(rs.getTimestamp("updatedAt").toLocalDateTime());
                customersList.add(c);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return customersList;
    }

    public boolean update(Customer customer) {
        String sql = "UPDATE `customer` SET `name` = ?, `sex` = ?, `birthDate` = ?, `email` = ?, `phone` = ?, `zipCode` = ?, `address` = ?, `addressComplement` = ?, "
                + "`district` = ?, `city` = ?, `federativeUnit` = ? WHERE `customerId` = ?;";

        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getSex());
            ps.setDate(3, java.sql.Date.valueOf(customer.getBirthDate()));
            ps.setString(4, customer.getEmail());
            ps.setString(5, customer.getPhone());
            ps.setString(6, customer.getZipCode());
            ps.setString(7, customer.getAddress());
            ps.setString(8, customer.getAddressComplement());
            ps.setString(9, customer.getDistrict());
            ps.setString(10, customer.getCity());
            ps.setString(11, customer.getFederativeUnit());
            ps.setInt(12, customer.getCustomerId());
            ps.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }

    public boolean delete(Customer customer) {
        String sql = "DELETE FROM `customer` WHERE `customerId` = ?;";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, customer.getCustomerId());
            ps.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }
    
    public String getCustomerSex(int customerId){
        String sql = "SELECT `sex` FROM `customer` WHERE `customerId` = " + customerId + ";";
        String customerSex = "";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                customerSex = rs.getString("sex");
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return customerSex;
    }
    
    public LocalDate getCustomerBirthDate(int customerId){
        String sql = "SELECT `birthDate` FROM `customer` WHERE `customerId` = " + customerId + ";";
        LocalDate customerBirthDate = LocalDate.now();

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                customerBirthDate = rs.getDate("birthDate").toLocalDate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return customerBirthDate;
    }
    
    public boolean checkSaleDeleteConstraint(int customerId) {
        String sql = "SELECT `fkCustomer` FROM `sale` WHERE `fkCustomer` = " + customerId + ";";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            //DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }
    
    public boolean checkMeasurementDeleteConstraint(int customerId) {
        String sql = "SELECT `fkCustomer` FROM `measurement` WHERE `fkCustomer` = " + customerId + ";";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            //DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }
    
    public int getAmountOfCustomersLast90DaysForDashboard(){
        String sql = "SELECT COUNT(*) AS `amount` FROM `customer` WHERE `createdAt` >= DATE_ADD(NOW(), INTERVAL -3 MONTH);";
        int amount = 0;
        
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                amount = rs.getInt("amount");
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return amount;
    }
    
    public int getAmountOfEachSexForDashboard(String sex){
        String sql = "SELECT COUNT(*) AS `amount` FROM `customer` WHERE `sex` = '" + sex + "';";
        int amount = 0;
        
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                amount = rs.getInt("amount");
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return amount;
    }
    
    public MultiValuedMap<Integer, String> getAmountOfCustomersByCityForDashboard() {
        MultiValuedMap<Integer, String> map = new ArrayListValuedHashMap<>();
        String sql = "SELECT COUNT(*) AS `amount`, `city` "
                + "FROM `customer` "
                + "WHERE `city` <> '' OR `city` <> NULL "
                + "GROUP BY `city` "
                + "ORDER BY `amount` ASC "
                + "LIMIT 10;";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                map.put(rs.getInt("amount"), rs.getString("city"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return map;
    }
    
    public int getAmountOfCustomersWithoutCityForDashboard(){
        String sql = "SELECT COUNT(*) AS `amount` FROM `customer` WHERE `city` = '' OR `city` = NULL;";
        int amount = 0;
        
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                amount = rs.getInt("amount");
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return amount;
    }
    
    public List<Integer> getMaleAgeRangeForDashboard(){
        List<Integer> list = new ArrayList<>();
        String sql = "SELECT "
                + "SUM(CASE WHEN `sex` = 'Masculino' AND TIMESTAMPDIFF(YEAR, `birthDate`, CURDATE()) BETWEEN 0 AND 20 THEN 1 ELSE 0 END) AS 'Até 20', "
                + "SUM(CASE WHEN `sex` = 'Masculino' AND TIMESTAMPDIFF(YEAR, `birthDate`, CURDATE()) BETWEEN 21 AND 30 THEN 1 ELSE 0 END) AS '21 a 30', "
                + "SUM(CASE WHEN `sex` = 'Masculino' AND TIMESTAMPDIFF(YEAR, `birthDate`, CURDATE()) BETWEEN 31 AND 40 THEN 1 ELSE 0 END) AS '31 a 40', "
                + "SUM(CASE WHEN `sex` = 'Masculino' AND TIMESTAMPDIFF(YEAR, `birthDate`, CURDATE()) BETWEEN 41 AND 50 THEN 1 ELSE 0 END) AS '41 a 50', "
                + "SUM(CASE WHEN `sex` = 'Masculino' AND TIMESTAMPDIFF(YEAR, `birthDate`, CURDATE()) > 50 THEN 1 ELSE 0 END) AS 'Acima de 50' "
                + "FROM `customer`;";
        
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                list.add(rs.getInt(1));
                list.add(rs.getInt(2));
                list.add(rs.getInt(3));
                list.add(rs.getInt(4));
                list.add(rs.getInt(5));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return list;
    }
    
    public List<Integer> getFemaleAgeRangeForDashboard(){
        List<Integer> list = new ArrayList<>();
        String sql = "SELECT "
                + "SUM(CASE WHEN `sex` = 'Feminino' AND TIMESTAMPDIFF(YEAR, `birthDate`, CURDATE()) BETWEEN 0 AND 20 THEN 1 ELSE 0 END) AS 'Até 20', "
                + "SUM(CASE WHEN `sex` = 'Feminino' AND TIMESTAMPDIFF(YEAR, `birthDate`, CURDATE()) BETWEEN 21 AND 30 THEN 1 ELSE 0 END) AS '21 a 30', "
                + "SUM(CASE WHEN `sex` = 'Feminino' AND TIMESTAMPDIFF(YEAR, `birthDate`, CURDATE()) BETWEEN 31 AND 40 THEN 1 ELSE 0 END) AS '31 a 40', "
                + "SUM(CASE WHEN `sex` = 'Feminino' AND TIMESTAMPDIFF(YEAR, `birthDate`, CURDATE()) BETWEEN 41 AND 50 THEN 1 ELSE 0 END) AS '41 a 50', "
                + "SUM(CASE WHEN `sex` = 'Feminino' AND TIMESTAMPDIFF(YEAR, `birthDate`, CURDATE()) > 50 THEN 1 ELSE 0 END) AS 'Acima de 50' "
                + "FROM `customer`;";
        
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                list.add(rs.getInt(1));
                list.add(rs.getInt(2));
                list.add(rs.getInt(3));
                list.add(rs.getInt(4));
                list.add(rs.getInt(5));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return list;
    }

    public boolean createExcelFile(String filePath) {
        String sql = "SELECT * FROM `customer`";

        try {
            XSSFWorkbook workbook = new XSSFWorkbook();

            XSSFCellStyle infoStyle = ExcelFileHelper.excelStyle(workbook, "Arial", true, BorderStyle.NONE, new byte[]{(byte) 255, (byte) 255, (byte) 255});
            XSSFCellStyle headerStyle = ExcelFileHelper.excelStyle(workbook, "Arial", true, BorderStyle.THIN, new byte[]{(byte) 205, (byte) 205, (byte) 205});
            XSSFCellStyle defaultStyle = ExcelFileHelper.excelStyle(workbook, "Arial", false, BorderStyle.THIN, new byte[]{(byte) 255, (byte) 255, (byte) 255});

            XSSFSheet sheet = workbook.createSheet("Clientes");
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));
            ExcelFileHelper.createStyledCell(sheet.createRow(0), 0, "Relatório gerado em: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")), infoStyle);

            List<String> fields = Arrays.asList("ID", "CPF", "Nome", "Sexo", "E-mail", "Telefone", "CEP",
                    "Endereço", "Complemento", "Bairro", "Cidade", "UF", "Criação", "Modificação");
            XSSFRow headerRow = sheet.createRow(2);
            for (int i = 0; i < fields.size(); i++) {
                ExcelFileHelper.createStyledCell(headerRow, i, fields.get(i), headerStyle);
            }

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            int rowIndex = 3;
            while (rs.next()) {
                XSSFRow row = sheet.createRow(rowIndex);
                ExcelFileHelper.createStyledCell(row, 0, rs.getInt("customerId"), defaultStyle);
                ExcelFileHelper.createStyledCell(row, 1, rs.getString("personRegistry"), defaultStyle);
                ExcelFileHelper.createStyledCell(row, 2, rs.getString("name"), defaultStyle);
                ExcelFileHelper.createStyledCell(row, 3, rs.getString("sex"), defaultStyle);
                ExcelFileHelper.createStyledCell(row, 4, rs.getString("email"), defaultStyle);
                ExcelFileHelper.createStyledCell(row, 5, rs.getString("phone"), defaultStyle);
                ExcelFileHelper.createStyledCell(row, 6, rs.getString("zipCode"), defaultStyle);
                ExcelFileHelper.createStyledCell(row, 7, rs.getString("address"), defaultStyle);
                ExcelFileHelper.createStyledCell(row, 8, rs.getString("addressComplement"), defaultStyle);
                ExcelFileHelper.createStyledCell(row, 9, rs.getString("district"), defaultStyle);
                ExcelFileHelper.createStyledCell(row, 10, rs.getString("city"), defaultStyle);
                ExcelFileHelper.createStyledCell(row, 11, rs.getString("federativeUnit"), defaultStyle);
                ExcelFileHelper.createStyledDateTimeCell(row, 12, rs.getString("createdAt"), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"), defaultStyle);
                ExcelFileHelper.createStyledDateTimeCell(row, 13, rs.getString("updatedAt"), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"), defaultStyle);
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
            Logger.getLogger(CustomerDao.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CustomerDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }
}
