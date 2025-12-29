package models;

import utils.enums.SupportStatus;

import java.time.LocalDateTime;

/**
 * SupportRequest - Model yêu cầu hỗ trợ
 */
public class SupportRequest {
    private int id;
    private Integer requesterId;
    private String requesterName;
    private String requesterEmail;
    private String requesterPhone;
    private String subject;
    private String message;
    private SupportStatus status;
    private Integer assignedTo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public SupportRequest() {
    }

    public SupportRequest(int id, Integer requesterId, String requesterName, String requesterEmail,
            String requesterPhone, String subject, String message, SupportStatus status, Integer assignedTo,
            LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.requesterId = requesterId;
        this.requesterName = requesterName;
        this.requesterEmail = requesterEmail;
        this.requesterPhone = requesterPhone;
        this.subject = subject;
        this.message = message;
        this.status = status;
        this.assignedTo = assignedTo;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters & Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(Integer requesterId) {
        this.requesterId = requesterId;
    }

    public String getRequesterName() {
        return requesterName;
    }

    public void setRequesterName(String requesterName) {
        this.requesterName = requesterName;
    }

    public String getRequesterEmail() {
        return requesterEmail;
    }

    public void setRequesterEmail(String requesterEmail) {
        this.requesterEmail = requesterEmail;
    }

    public String getRequesterPhone() {
        return requesterPhone;
    }

    public void setRequesterPhone(String requesterPhone) {
        this.requesterPhone = requesterPhone;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public SupportStatus getStatus() {
        return status;
    }

    public void setStatus(SupportStatus status) {
        this.status = status;
    }

    public Integer getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(Integer assignedTo) {
        this.assignedTo = assignedTo;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
