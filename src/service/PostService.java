package service;

import models.Post;
import repository.IPostRepository;
import repository.impl.PostRepositoryImpl;
import java.time.LocalDateTime;
import java.util.List;

public class PostService {
    private IPostRepository postRepository;

    public PostService() {
        this.postRepository = new PostRepositoryImpl();
    }

    public List<Post> getAllPosts() {
        return postRepository.getAllPosts();
    }

    public boolean createPost(Post post) {
        if ("Đã đăng".equals(post.getStatus()) && post.getPublishedAt() == null) {
            post.setPublishedAt(LocalDateTime.now());
        }
        return postRepository.addPost(post);
    }

    public boolean updatePost(Post post) {
        if ("Đã đăng".equals(post.getStatus()) && post.getPublishedAt() == null) {
            post.setPublishedAt(LocalDateTime.now());
        }
        return postRepository.updatePost(post);
    }

    public boolean deletePost(int id) {
        return postRepository.deletePost(id);
    }
}
