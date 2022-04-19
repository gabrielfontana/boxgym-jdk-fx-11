package boxgym.dao;

import boxgym.jdbc.ConnectionFactory;
import boxgym.model.Product;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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
        String sql = "INSERT INTO `product` (`name`, `category`, `description`, `amount`, `minimumStock`, `costPrice`, `sellingPrice`, `image`) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

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

    public LinkedHashMap<Integer, String> getProductForHashMap() {
        LinkedHashMap<Integer, String> map = new LinkedHashMap<>();
        String sql = "SELECT `productId`, `name` FROM `product`;";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            Product p;
            while (rs.next()) {
                p = new Product(rs.getInt("productId"), rs.getString("name"));
                map.put(p.getProductId(), p.getName());
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return map;
    }

    public BigDecimal getProductCostPrice(int key) {
        String sql = "SELECT `costPrice` FROM `product` WHERE `productId` = '" + key + "';";
        BigDecimal costPrice = new BigDecimal("0");

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                costPrice = rs.getBigDecimal("costPrice");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return costPrice;
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
                p.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
                p.setUpdatedAt(rs.getTimestamp("updatedAt").toLocalDateTime());
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
                + "`costPrice` = ?, `sellingPrice` = ?, `image` = ? WHERE `productId` = ?;";

        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, product.getName());
            ps.setString(2, product.getCategory());
            ps.setString(3, product.getDescription());
            ps.setInt(4, product.getMinimumStock());
            ps.setBigDecimal(5, product.getCostPrice());
            ps.setBigDecimal(6, product.getSellingPrice());
            ps.setBytes(7, product.getImage());
            ps.setInt(8, product.getProductId());
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
    
    public int count() {
        int count = 0;
        String sql = "SELECT count(*) AS `count` FROM `product`;";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return count;
    }
}
