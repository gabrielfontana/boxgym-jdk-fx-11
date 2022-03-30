package boxgym.dao;

import boxgym.jdbc.ConnectionFactory;
import boxgym.model.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.dbutils.DbUtils;

public class ProductDao {

    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;

    public ProductDao() {
        this.conn = new ConnectionFactory().getConnection();
    }

    public boolean create(Product product) {
        String sql = "INSERT INTO `product` (`name`, `category`, `description`, `amount`, `minimumStock`, `costPrice`, `sellingPrice`, `image`, `fkSupplier`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, product.getName());
            ps.setString(2, product.getCategory());
            ps.setString(3, product.getDescription());
            ps.setInt(4, product.getAmount());
            ps.setInt(5, product.getMinimumStock());
            ps.setBigDecimal(6, product.getCostPrice());
            ps.setBigDecimal(7, product.getSellingPrice());
            ps.setBytes(8, product.getImage());
            ps.setInt(9, product.getFkSupplier());
            ps.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ProductDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }

    public List<Product> read() {
        List<Product> productsList = new ArrayList<>();
        String sql = "SELECT * FROM `product`";
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Product p = new Product();
                p.setProductId(rs.getInt("productId"));
                p.setName(rs.getString("name"));
                p.setCategory(rs.getString("category"));
                p.setDescription(rs.getString("description"));
                p.setAmount(rs.getInt("amount"));
                p.setMinimumStock(rs.getInt("minimumStock"));
                p.setCostPrice(rs.getBigDecimal("costPrice"));
                p.setSellingPrice(rs.getBigDecimal("sellingPrice"));
                p.setImage(rs.getBytes("image"));
                p.setFkSupplier(rs.getInt("fkSupplier"));
                p.setCreatedAt(rs.getString("createdAt"));
                p.setUpdatedAt(rs.getString("updatedAt"));
                productsList.add(p);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return productsList;
    }

    public boolean update(Product product) {
        String sql = "UPDATE `product` SET `name` = ?, `category` = ?, `description` = ?, `minimumStock` = ?, "
                + "`costPrice` = ?, `sellingPrice` = ?, `image` = ?, `fkSupplier` = ? WHERE `productId` = ?;";

        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, product.getName());
            ps.setString(2, product.getCategory());
            ps.setString(3, product.getDescription());
            ps.setInt(4, product.getMinimumStock());
            ps.setBigDecimal(5, product.getCostPrice());
            ps.setBigDecimal(6, product.getSellingPrice());
            ps.setBytes(7, product.getImage());
            ps.setInt(8, product.getFkSupplier());
            ps.setInt(9, product.getProductId());
            ps.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ProductDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }

    public boolean delete(Product product) {
        String sql = "DELETE FROM `product` WHERE `productId` = ?;";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, product.getProductId());
            ps.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ProductDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }
}
