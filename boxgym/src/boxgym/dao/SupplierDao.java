package boxgym.dao;

import boxgym.helper.ExcelFileHelper;
import boxgym.jdbc.ConnectionFactory;
import boxgym.model.Supplier;
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
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.apache.commons.dbutils.DbUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class SupplierDao {

    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;

    public SupplierDao() {
        this.conn = new ConnectionFactory().getConnection();
    }

    public boolean checkExistingSupplier(String companyRegistry) {
        String cnpj = companyRegistry;
        String sql = "SELECT `companyRegistry` FROM `supplier` WHERE `companyRegistry` = '" + cnpj + "';";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return (cnpj.equals(rs.getString("companyRegistry")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(SupplierDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            //DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }

    public boolean create(Supplier supplier) {
        String sql = "INSERT INTO `supplier` (`companyRegistry`, `corporateName`, `tradeName`, `email`, `phone`, `zipCode`, `address`, `addressComplement`, "
                + "`district`, `city`, `federativeUnit`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, supplier.getCompanyRegistry());
            ps.setString(2, supplier.getCorporateName());
            ps.setString(3, supplier.getTradeName());
            ps.setString(4, supplier.getEmail());
            ps.setString(5, supplier.getPhone());
            ps.setString(6, supplier.getZipCode());
            ps.setString(7, supplier.getAddress());
            ps.setString(8, supplier.getAddressComplement());
            ps.setString(9, supplier.getDistrict());
            ps.setString(10, supplier.getCity());
            ps.setString(11, supplier.getFederativeUnit());
            ps.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(SupplierDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }

    public LinkedHashMap<Integer, String> getSupplierForHashMap() {
        LinkedHashMap<Integer, String> map = new LinkedHashMap<>();
        String sql = "SELECT `supplierId`, `corporateName` FROM `supplier`;";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            Supplier s;
            while (rs.next()) {
                s = new Supplier(rs.getInt("supplierId"), rs.getString("corporateName"));
                map.put(s.getSupplierId(), s.getCorporateName());
            }
        } catch (SQLException ex) {
            Logger.getLogger(SupplierDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return map;
    }

    public List<Supplier> read() {
        List<Supplier> suppliersList = new ArrayList<>();
        String sql = "SELECT * FROM `supplier`;";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Supplier s = new Supplier();
                s.setSupplierId(rs.getInt("supplierId"));
                s.setCompanyRegistry(rs.getString("companyRegistry"));
                s.setCorporateName(rs.getString("corporateName"));
                s.setTradeName(rs.getString("tradeName"));
                s.setEmail(rs.getString("email"));
                s.setPhone(rs.getString("phone"));
                s.setZipCode(rs.getString("zipCode"));
                s.setAddress(rs.getString("address"));
                s.setAddressComplement(rs.getString("addressComplement"));
                s.setDistrict(rs.getString("district"));
                s.setCity(rs.getString("city"));
                s.setFederativeUnit(rs.getString("federativeUnit"));
                s.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
                s.setUpdatedAt(rs.getTimestamp("updatedAt").toLocalDateTime());
                suppliersList.add(s);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SupplierDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return suppliersList;
    }

    public boolean update(Supplier supplier) {
        String sql = "UPDATE `supplier` SET `corporateName` = ?, `tradeName` = ?, `email` = ?, `phone` = ?, `zipCode` = ?, `address` = ?, `addressComplement` = ?, "
                + "`district` = ?, `city` = ?, `federativeUnit` = ? WHERE `supplierId` = ?;";

        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, supplier.getCorporateName());
            ps.setString(2, supplier.getTradeName());
            ps.setString(3, supplier.getEmail());
            ps.setString(4, supplier.getPhone());
            ps.setString(5, supplier.getZipCode());
            ps.setString(6, supplier.getAddress());
            ps.setString(7, supplier.getAddressComplement());
            ps.setString(8, supplier.getDistrict());
            ps.setString(9, supplier.getCity());
            ps.setString(10, supplier.getFederativeUnit());
            ps.setInt(11, supplier.getSupplierId());
            ps.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(SupplierDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }

    public boolean delete(Supplier supplier) {
        String sql = "DELETE FROM `supplier` WHERE `supplierId` = ?;";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, supplier.getSupplierId());
            ps.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(SupplierDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }

    public boolean checkStockEntryDeleteConstraint(int supplierId) {
        String sql = "SELECT `fkSupplier` FROM `stockentry` WHERE `fkSupplier` = " + supplierId + ";";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(SupplierDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            //DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }

    public int getAmountOfSuppliersLast90DaysForDashboard() {
        String sql = "SELECT COUNT(*) AS `amount` FROM `supplier` WHERE `createdAt` >= DATE_ADD(NOW(), INTERVAL -3 MONTH);";
        int amount = 0;

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                amount = rs.getInt("amount");
            }
        } catch (SQLException ex) {
            Logger.getLogger(SupplierDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return amount;
    }

    public MultiValuedMap<Integer, String> getAmountOfSuppliersByFUForDashboard() {
        MultiValuedMap<Integer, String> map = new ArrayListValuedHashMap<>();
        String sql = "SELECT COUNT(*) AS `amount`, `federativeUnit` "
                + "FROM `supplier` "
                + "WHERE `federativeUnit` <> '' OR `federativeUnit` <> NULL "
                + "GROUP BY `federativeUnit` "
                + "ORDER BY `federativeUnit` ASC;";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                map.put(rs.getInt("amount"), rs.getString("federativeUnit"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(SupplierDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return map;
    }

    public int getAmountOfSuppliersWithoutFUForDashboard() {
        String sql = "SELECT COUNT(*) AS `amount` FROM `supplier` WHERE `federativeUnit` = '' OR `federativeUnit` = NULL;";
        int amount = 0;

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                amount = rs.getInt("amount");
            }
        } catch (SQLException ex) {
            Logger.getLogger(SupplierDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return amount;
    }

    public MultiValuedMap<Integer, String> getMostFrequentSuppliersSEForDashboard() {
        MultiValuedMap<Integer, String> map = new ArrayListValuedHashMap<>();
        String sql = "SELECT COUNT(*) AS `amount`, s.tradeName AS `tempSupplierName` "
                + "FROM `stockentry` AS se INNER JOIN `supplier` AS s "
                + "ON se.fkSupplier = s.supplierId "
                + "GROUP BY `fkSupplier` "
                + "ORDER BY `amount` DESC "
                + "LIMIT 10;";
        
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                map.put(rs.getInt("amount"), rs.getString("tempSupplierName"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(SupplierDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return map;
    }

    public boolean createExcelFile(String filePath) {
        String sql = "SELECT * FROM `supplier`";

        try {
            XSSFWorkbook workbook = new XSSFWorkbook();

            XSSFCellStyle infoStyle = ExcelFileHelper.excelStyle(workbook, "Arial", true, BorderStyle.NONE, new byte[]{(byte) 255, (byte) 255, (byte) 255});
            XSSFCellStyle headerStyle = ExcelFileHelper.excelStyle(workbook, "Arial", true, BorderStyle.THIN, new byte[]{(byte) 205, (byte) 205, (byte) 205});
            XSSFCellStyle defaultStyle = ExcelFileHelper.excelStyle(workbook, "Arial", false, BorderStyle.THIN, new byte[]{(byte) 255, (byte) 255, (byte) 255});

            XSSFSheet sheet = workbook.createSheet("Fornecedores");
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));
            ExcelFileHelper.createStyledCell(sheet.createRow(0), 0, "Relatório gerado em: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")), infoStyle);

            List<String> fields = Arrays.asList("ID", "CNPJ", "Razão Social", "Nome Fantasia", "E-mail", "Telefone", "CEP",
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
                ExcelFileHelper.createStyledCell(row, 0, rs.getInt("supplierId"), defaultStyle);
                ExcelFileHelper.createStyledCell(row, 1, rs.getString("companyRegistry"), defaultStyle);
                ExcelFileHelper.createStyledCell(row, 2, rs.getString("corporateName"), defaultStyle);
                ExcelFileHelper.createStyledCell(row, 3, rs.getString("tradeName"), defaultStyle);
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
            Logger.getLogger(SupplierDao.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SupplierDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }
}
