package boxgym.dao;

import boxgym.jdbc.ConnectionFactory;
import boxgym.model.Membership;
import boxgym.model.Sale;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
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
    
    public boolean update(Membership membership) {
        String sql = "UPDATE `membership` SET `dueDate` = ?, `price` = ? WHERE `membershipId` = ?;";

        try {
            ps = conn.prepareStatement(sql);
            ps.setDate(1, java.sql.Date.valueOf(membership.getDueDate()));
            ps.setBigDecimal(2, membership.getPrice());
            ps.setInt(3, membership.getMembershipId());
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

    public boolean checkExistingBillingDescription(int fkCustomer) {
        String sql = "SELECT b.description "
                + "FROM `billing` AS b INNER JOIN `membership` AS m "
                + "ON b.fkMembership = m.membershipId INNER JOIN `customer` as C "
                + "ON m.fkCustomer = c.customerId "
                + "WHERE b.fkMembership IS NOT NULL AND m.fkCustomer = " + fkCustomer + ";";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(MembershipDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            //DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return false;
    }

    public List<Integer> getBillingIdToRename(int fkCustomer) {
        List<Integer> idList = new ArrayList<>();
        String sql = "SELECT b.billingId "
                + "FROM `billing` AS b INNER JOIN `membership` AS m "
                + "ON b.fkMembership = m.membershipId INNER JOIN `customer` as C "
                + "ON m.fkCustomer = c.customerId "
                + "WHERE b.fkMembership IS NOT NULL AND m.fkCustomer = " + fkCustomer + ";";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                idList.add(rs.getInt("billingId"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(MembershipDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return idList;
    }

    public List<String> getBillingDescriptionToRename(int fkCustomer) {
        List<String> descriptionList = new ArrayList<>();
        String sql = "SELECT b.description "
                + "FROM `billing` AS b INNER JOIN `membership` AS m "
                + "ON b.fkMembership = m.membershipId INNER JOIN `customer` as C "
                + "ON m.fkCustomer = c.customerId "
                + "WHERE b.fkMembership IS NOT NULL AND m.fkCustomer = " + fkCustomer + ";";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                descriptionList.add(rs.getString("description"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(MembershipDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
        return descriptionList;
    }
    
    public void updateBillingDescription(List<Integer> idList, List<String> newDescriptionList) {
        if (idList.size() == newDescriptionList.size()) {
            for (int i = 0; i < idList.size(); i++) {
                String sql = "UPDATE `billing` SET `description` = '" + newDescriptionList.get(i) + "' WHERE `billingId` = " + idList.get(i) + ";";

                try {
                    ps = conn.prepareStatement(sql);
                    ps.execute();
                } catch (SQLException ex) {
                    Logger.getLogger(MembershipDao.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
    }
}
