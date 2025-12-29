package repository.impl;

import database.DatabaseConnection;
import models.SupportRequest;
import repository.ISupportRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp triển khai cho ISupportRepository
 */
public class SupportRepositoryImpl implements ISupportRepository {
    private Connection getConnection() {
        return DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public List<SupportRequest> getAllRequests() {
        List<SupportRequest> list = new ArrayList<>();
        String sql = "SELECT * FROM tbl_support_requests ORDER BY created_at DESC";
        try (Statement stmt = getConnection().createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSetToRequest(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean addRequest(SupportRequest request) {
        String sql = "INSERT INTO tbl_support_requests (requester_id, requester_name, requester_email, requester_phone, subject, message, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            if (request.getRequesterId() != null) {
                stmt.setInt(1, request.getRequesterId());
            } else {
                stmt.setNull(1, Types.INTEGER);
            }
            stmt.setString(2, request.getRequesterName());
            stmt.setString(3, request.getRequesterEmail());
            stmt.setString(4, request.getRequesterPhone());
            stmt.setString(5, request.getSubject());
            stmt.setString(6, request.getMessage());
            stmt.setString(7, request.getStatus() != null ? request.getStatus().getDisplayName() : "Mới");
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateStatus(int id, String status, Integer staffId) {
        String sql = "UPDATE tbl_support_requests SET status = ?, assigned_to = ? WHERE id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, status);
            if (staffId != null) {
                stmt.setInt(2, staffId);
            } else {
                stmt.setNull(2, Types.INTEGER);
            }
            stmt.setInt(3, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteRequest(int id) {
        String sql = "DELETE FROM tbl_support_requests WHERE id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private SupportRequest mapResultSetToRequest(ResultSet rs) throws SQLException {
        SupportRequest req = new SupportRequest();
        req.setId(rs.getInt("id"));
        int requesterId = rs.getInt("requester_id");
        req.setRequesterId(rs.wasNull() ? null : requesterId);
        req.setRequesterName(rs.getString("requester_name"));
        req.setRequesterEmail(rs.getString("requester_email"));
        req.setRequesterPhone(rs.getString("requester_phone"));
        req.setSubject(rs.getString("subject"));
        req.setMessage(rs.getString("message"));
        req.setStatus(utils.enums.SupportStatus.fromDisplayName(rs.getString("status")));
        int assignedTo = rs.getInt("assigned_to");
        req.setAssignedTo(rs.wasNull() ? null : assignedTo);

        Timestamp created = rs.getTimestamp("created_at");
        if (created != null)
            req.setCreatedAt(created.toLocalDateTime());

        Timestamp updated = rs.getTimestamp("updated_at");
        if (updated != null)
            req.setUpdatedAt(updated.toLocalDateTime());

        return req;
    }
}
