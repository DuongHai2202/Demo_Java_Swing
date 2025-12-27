package controller.staff;

import models.Post;
import models.SupportRequest;
import models.User;
import service.PostService;
import service.SupportService;
import view.staff.StaffDashboard;

import javax.swing.*;
import java.util.List;

public class StaffController {
    private StaffDashboard view;
    private SupportService supportService;
    private PostService postService;
    private User currentUser;

    public StaffController(StaffDashboard view, User user) {
        this.view = view;
        this.currentUser = user;
        this.supportService = new SupportService();
        this.postService = new PostService();
    }

    // Support Request Logic
    public List<SupportRequest> getAllSupportRequests() {
        return supportService.getAllRequests();
    }

    public boolean updateSupportStatus(int requestId, String status) {
        return supportService.updateStatus(requestId, status, currentUser.getId());
    }

    // Post Logic
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    public boolean createPost(Post post) {
        post.setAuthorId(currentUser.getId());
        return postService.createPost(post);
    }

    public boolean updatePost(Post post) {
        return postService.updatePost(post);
    }

    public boolean deletePost(int id) {
        return postService.deletePost(id);
    }
}
