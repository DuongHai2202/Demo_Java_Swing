package controller.staff;

import models.Attendance;
import models.ClassSchedule;
import models.Post;
import models.SupportRequest;
import models.User;
import service.AttendanceService;
import service.PostService;
import service.ScheduleService;
import service.SupportService;
import view.staff.StaffDashboard;

import javax.swing.*;
import java.util.List;

/**
 * StaffController - Controller cho Staff dashboard
 */
public class StaffController {
    private StaffDashboard view;
    private final SupportService supportService;
    private final PostService postService;
    private final ScheduleService scheduleService;
    private final AttendanceService attendanceService;
    private User currentUser;

    /**
     * Constructor chính - Nhận services qua dependency injection
     */
    public StaffController(StaffDashboard view, User user,
            SupportService supportService,
            PostService postService,
            ScheduleService scheduleService,
            AttendanceService attendanceService) {
        this.view = view;
        this.currentUser = user;
        this.supportService = supportService;
        this.postService = postService;
        this.scheduleService = scheduleService;
        this.attendanceService = attendanceService;
    }

    /**
     * Constructor mặc định - Tạo services (backward compatible)
     */
    public StaffController(StaffDashboard view, User user) {
        this(view, user,
                new SupportService(),
                new PostService(),
                new ScheduleService(),
                new AttendanceService());
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

    // Schedule Logic
    public List<ClassSchedule> getTodaySchedules() {
        return scheduleService.getTodaySchedules();
    }

    public ClassSchedule getScheduleById(int id) {
        return scheduleService.getScheduleById(id);
    }

    // Attendance Logic
    public List<Attendance> getAttendanceBySchedule(int scheduleId) {
        return attendanceService.getAttendanceBySchedule(scheduleId);
    }

    public boolean markAttendanceBatch(List<Attendance> attendanceList) {
        return attendanceService.markAttendanceBatch(attendanceList);
    }

    public boolean hasAttendance(int scheduleId, int studentId) {
        return attendanceService.hasAttendance(scheduleId, studentId);
    }
}
