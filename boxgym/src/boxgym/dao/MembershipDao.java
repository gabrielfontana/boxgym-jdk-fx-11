package boxgym.dao;

import boxgym.jdbc.ConnectionFactory;
import boxgym.model.Membership;
import boxgym.model.Sale;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.dbutils.DbUtils;

public class MembershipDao {

    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;

    public MembershipDao() {
        this.conn = new ConnectionFactory().getConnection();
    }

    public boolean create(Membership membership) {
        String sql = "INSERT INTO `membership` (`fkCustomer`, `dueDate`, `price`, `status`) VALUES (?, ?, ?, ?);";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, membership.getFkCustomer());
            ps.setDate(2, java.sql.Date.valueOf(membership.getDueDate()));
            ps.setBigDecimal(3, membership.getPrice());
            ps.setString(4, membership.getStatus());
            ps.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(MembershipDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }

    public int getMembershipId() {
        String sql = "SELECT `membershipId` FROM `membership` ORDER BY `membershipId` DESC LIMIT 1;";
        int membershipId = 0;

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                membershipId = rs.getInt("membershipId");
            }
        } catch (SQLException ex) {
            Logger.getLogger(MembershipDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return membershipId;
    }

    public List<Membership> read() {
        List<Membership> membershipsList = new ArrayList<>();
        String sql = "SELECT m.membershipId, m.fkCustomer, c.name AS `tempCustomerName`, m.dueDate, m.price, m.status, m.createdAt, m.updatedAt "
                + "FROM `membership` AS m INNER JOIN `customer` AS c "
                + "ON m.fkCustomer = c.customerId "
                + "ORDER BY m.membershipId ASC;";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Membership m = new Membership();
                m.setMembershipId(rs.getInt("membershipId"));
                m.setFkCustomer(rs.getInt("fkCustomer"));
                m.setTempCustomerName(rs.getString("tempCustomerName"));
                m.setDueDate(rs.getDate("dueDate").toLocalDate());
                m.setPrice(rs.getBigDecimal("price"));
                m.setStatus(rs.getString("status"));
                m.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
                m.setUpdatedAt(rs.getTimestamp("updatedAt").toLocalDateTime());
                membershipsList.add(m);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MembershipDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return membershipsList;
    }
    
    public boolean delete(Membership membership) {
        String sql = "DELETE FROM `membership` WHERE `membershipId` = ?;";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, membership.getMembershipId());
            ps.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(MembershipDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }

}
