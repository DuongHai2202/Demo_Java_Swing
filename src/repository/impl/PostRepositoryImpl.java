package repository.impl;

import database.DatabaseConnection;
import models.Post;
import repository.IPostRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp triển khai cho IPostRepository
 */
public class PostRepositoryImpl implements IPostRepository {
    private Connection getConnection() {
        return DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public List<Post> getAllPosts() {
        List<Post> list = new ArrayList<>();
        String sql = "SELECT * FROM tbl_posts ORDER BY created_at DESC";
        try (Statement stmt = getConnection().createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSetToPost(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean addPost(Post post) {
        String sql = "INSERT INTO tbl_posts (title, content, author_id, category, status, published_at) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, post.getTitle());
            stmt.setString(2, post.getContent());
            stmt.setInt(3, post.getAuthorId());
            stmt.setString(4, post.getCategory());
            stmt.setString(5, post.getStatus() != null ? post.getStatus().getDisplayName() : "Bản nháp");

            if (post.getPublishedAt() != null) {
                stmt.setTimestamp(6, Timestamp.valueOf(post.getPublishedAt()));
            } else {
                stmt.setNull(6, Types.TIMESTAMP);
            }

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updatePost(Post post) {
        String sql = "UPDATE tbl_posts SET title = ?, content = ?, category = ?, status = ?, published_at = ? WHERE id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, post.getTitle());
            stmt.setString(2, post.getContent());
            stmt.setString(3, post.getCategory());
            stmt.setString(4, post.getStatus() != null ? post.getStatus().getDisplayName() : "Bản nháp");

            if (post.getPublishedAt() != null) {
                stmt.setTimestamp(5, Timestamp.valueOf(post.getPublishedAt()));
            } else {
                stmt.setNull(5, Types.TIMESTAMP);
            }
            stmt.setInt(6, post.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deletePost(int id) {
        String sql = "DELETE FROM tbl_posts WHERE id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Post mapResultSetToPost(ResultSet rs) throws SQLException {
        Post post = new Post();
        post.setId(rs.getInt("id"));
        post.setTitle(rs.getString("title"));
        post.setContent(rs.getString("content"));
        post.setAuthorId(rs.getInt("author_id"));
        post.setCategory(rs.getString("category"));
        post.setStatus(utils.enums.PostStatus.fromDisplayName(rs.getString("status")));

        Timestamp published = rs.getTimestamp("published_at");
        if (published != null)
            post.setPublishedAt(published.toLocalDateTime());

        Timestamp created = rs.getTimestamp("created_at");
        if (created != null)
            post.setCreatedAt(created.toLocalDateTime());

        Timestamp updated = rs.getTimestamp("updated_at");
        if (updated != null)
            post.setUpdatedAt(updated.toLocalDateTime());

        return post;
    }
}
