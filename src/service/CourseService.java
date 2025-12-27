package service;

import models.Course;
import repository.ICourseRepository;
import repository.impl.CourseRepositoryImpl;

import java.util.List;

/**
 * Service class for Course operations
 */
public class CourseService {
    private ICourseRepository courseRepository;

    public CourseService() {
        this.courseRepository = new CourseRepositoryImpl();
    }

    public List<Course> getAllActiveCourses() {
        return courseRepository.getAllActiveCourses();
    }

    public List<Course> getAllCourses() {
        return courseRepository.getAllCourses();
    }

    public List<Course> getCoursesByLevel(String level) {
        return courseRepository.getCoursesByLevel(level);
    }

    public Course getCourseById(int id) {
        return courseRepository.getCourseById(id);
    }

    public boolean createCourse(Course course) {
        return courseRepository.createCourse(course);
    }

    public boolean updateCourse(Course course) {
        return courseRepository.updateCourse(course);
    }

    public boolean deleteCourse(int id) {
        return courseRepository.deleteCourse(id);
    }

    public java.util.Map<String, Integer> getCourseEnrollmentStats() {
        return courseRepository.getCourseEnrollmentStats();
    }
}
