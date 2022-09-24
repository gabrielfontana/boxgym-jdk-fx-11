package boxgym.dao;

import boxgym.jdbc.ConnectionFactory;
import boxgym.model.Sale;
import boxgym.model.SaleProduct;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.dbutils.DbUtils;

public class SaleProductDao {

    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;

    public SaleProductDao() {
        this.conn = new ConnectionFactory().getConnection();
    }

    public boolean create(SaleProduct item) {
        String sql = "INSERT INTO `sale_product` (`fkSale`, `fkProduct`, `amount`, `unitPrice`) VALUES (?, ?, ?, ?);";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, item.getFkSale());
            ps.setInt(2, item.getFkProduct());
            ps.setInt(3, item.getAmount());
            ps.setBigDecimal(4, item.getUnitPrice());
            ps.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(SaleProductDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }

    public List<SaleProduct> read(int selectedSale) {
        List<SaleProduct> productsList = new ArrayList<>();
        String sql = "SELECT p.name AS `tempProductName`, s_p.amount, s_p.unitPrice, s_p.amount * s_p.unitPrice AS `subtotal` "
                + "FROM `sale_product` AS s_p INNER JOIN `product` AS p "
                + "ON s_p.fkProduct = p.productId "
                + "WHERE s_p.fkSale = " + selectedSale + ";";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                SaleProduct sp = new SaleProduct();
                sp.setTempProductName(rs.getString("tempProductName"));
                sp.setAmount(rs.getInt("amount"));
                sp.setUnitPrice(rs.getBigDecimal("unitPrice"));
                sp.setSubtotal(rs.getBigDecimal("subtotal"));
                productsList.add(sp);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SaleProductDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return productsList;
    }

    public boolean delete(Sale sale) {
        String sql = "DELETE FROM `sale_product` WHERE `fkSale` = ?;";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, sale.getSaleId());
            ps.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(SaleProductDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }
}
