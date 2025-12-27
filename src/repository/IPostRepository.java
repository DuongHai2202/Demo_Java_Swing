package repository;

import models.Post;
import java.util.List;

/**
 * Interface định nghĩa thao tác với bảng tbl_posts (Bài viết/Tin tức)
 */
public interface IPostRepository {

    /**
     * Lấy tất cả bài viết (sắp xếp mới nhất trước)
     * 
     * @return List<Post>
     */
    List<Post> getAllPosts();

    /**
     * Thêm bài viết mới
     * 
     * @param post Đối tượng Post
     * @return true nếu thành công
     */
    boolean addPost(Post post);

    /**
     * Cập nhật bài viết
     * 
     * @param post Đối tượng Post
     * @return true nếu thành công
     */
    boolean updatePost(Post post);

    /**
     * Xóa bài viết
     * 
     * @param id Post ID
     * @return true nếu thành công
     */
    boolean deletePost(int id);
}
