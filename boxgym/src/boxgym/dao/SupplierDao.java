package boxgym.dao;

import boxgym.jdbc.ConnectionFactory;
import boxgym.model.Supplier;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.dbutils.DbUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
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

    public boolean checkDuplicate(Supplier supplier) {
        try {
            String cnpj = supplier.getCompanyRegistry();
            String sql = "SELECT `companyRegistry` FROM `supplier` WHERE `companyRegistry` = '" + cnpj + "';";
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

    public LinkedHashMap<Integer, String> readId() {
        LinkedHashMap<Integer, String> map = new LinkedHashMap<>();
        String sql = "SELECT `supplierId`, `tradeName` FROM `supplier`;";
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            Supplier s;
            while (rs.next()) {
                s = new Supplier(rs.getInt("supplierId"), rs.getString("tradeName"));
                map.put(s.getSupplierId(), s.getTradeName());
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
                s.setCreatedAt(rs.getString("createdAt"));
                s.setUpdatedAt(rs.getString("updatedAt"));
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

    public boolean createExcelFile(String filePath) {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Fornecedores");

            XSSFCellStyle infoStyle = excelStyle(workbook, "Arial", true, BorderStyle.NONE);
            XSSFCellStyle headerStyle = excelStyle(workbook, "Arial", true, BorderStyle.THIN);
            XSSFCellStyle defaultStyle = excelStyle(workbook, "Arial", false, BorderStyle.THIN);

            CellRangeAddress mergedRegion = new CellRangeAddress(0, 0, 0, 3);
            sheet.addMergedRegion(mergedRegion);
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();
            XSSFRow dateRow = sheet.createRow(0);
            createStyledCell(dateRow, 0, "Relatório gerado em: " + formatter.format(date), infoStyle);

            List<String> fields = Arrays.asList("ID", "CNPJ", "Razão Social", "Nome Fantasia", "E-mail", "Telefone", "CEP",
                    "Endereço", "Complemento", "Bairro", "Cidade", "UF", "Criação", "Modificação");
            XSSFRow headerRow = sheet.createRow(2);
            for (int i = 0; i < fields.size(); i++) {
                createStyledCell(headerRow, i, fields.get(i), headerStyle);
            }

            String sql = "SELECT * FROM `supplier`";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            int contentRow = 3;
            while (rs.next()) {
                XSSFRow row = sheet.createRow(contentRow);
                createStyledCell(row, 0, rs.getInt("supplierId"), defaultStyle);
                createStyledCell(row, 1, rs.getString("companyRegistry"), defaultStyle);
                createStyledCell(row, 2, rs.getString("corporateName"), defaultStyle);
                createStyledCell(row, 3, rs.getString("tradeName"), defaultStyle);
                createStyledCell(row, 4, rs.getString("email"), defaultStyle);
                createStyledCell(row, 5, rs.getString("phone"), defaultStyle);
                createStyledCell(row, 6, rs.getString("zipCode"), defaultStyle);
                createStyledCell(row, 7, rs.getString("address"), defaultStyle);
                createStyledCell(row, 8, rs.getString("addressComplement"), defaultStyle);
                createStyledCell(row, 9, rs.getString("district"), defaultStyle);
                createStyledCell(row, 10, rs.getString("city"), defaultStyle);
                createStyledCell(row, 11, rs.getString("federativeUnit"), defaultStyle);
                createStyledCell(row, 12, rs.getString("createdAt"), defaultStyle);
                createStyledCell(row, 13, rs.getString("updatedAt"), defaultStyle);
                contentRow++;
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

    private XSSFCellStyle excelStyle(XSSFWorkbook workbook, String fontName, boolean bold, BorderStyle border) {
        XSSFFont font = workbook.createFont();
        font.setFontName(fontName);
        font.setBold(bold);
        XSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);
        style.setBorderTop(border);
        style.setBorderBottom(border);
        style.setBorderLeft(border);
        style.setBorderRight(border);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    private XSSFCell createStyledCell(XSSFRow row, int cellIndex, String cellValue, XSSFCellStyle style) {
        XSSFCell cell = row.createCell(cellIndex);
        cell.setCellValue(cellValue);
        cell.setCellStyle(style);
        return cell;
    }

    private XSSFCell createStyledCell(XSSFRow row, int cellIndex, Integer cellValue, XSSFCellStyle style) {
        XSSFCell cell = row.createCell(cellIndex);
        cell.setCellValue(cellValue);
        cell.setCellStyle(style);
        return cell;
    }

}
