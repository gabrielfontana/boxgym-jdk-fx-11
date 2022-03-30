package boxgym.dao;

import boxgym.jdbc.ConnectionFactory;
import boxgym.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.dbutils.DbUtils;

public class UserDao {

    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;

    public UserDao() {
        this.conn = new ConnectionFactory().getConnection();
    }
    
    public boolean checkDuplicate(String username) {
        String sql = "SELECT * FROM `user` WHERE `username` = '" + username + "';";
        
        try{
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if(rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            //DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }

    public boolean create(User user) {
        String sql = "INSERT INTO `user` (`username`, `password`, `confirmPassword`) VALUES (?, ?, ?);";

        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getConfirmPassword());
            ps.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }

    public boolean authenticate(User user) {
        String sql = "SELECT `username`, `password` FROM `user` WHERE `username` = '" + user.getUsername() + "' AND `password` = '" + user.getPassword() + "';";
        
        try{
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if(rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }
}
