package service;

import models.SupportRequest;
import repository.ISupportRepository;
import repository.impl.SupportRepositoryImpl;

import java.util.List;

public class SupportService {
    private ISupportRepository supportRepository;

    public SupportService() {
        this.supportRepository = new SupportRepositoryImpl();
    }

    public List<SupportRequest> getAllRequests() {
        return supportRepository.getAllRequests();
    }

    public boolean createRequest(SupportRequest request) {
        // Business logic validation if needed
        return supportRepository.addRequest(request);
    }

    public boolean updateStatus(int requestId, String newStatus, int staffId) {
        return supportRepository.updateStatus(requestId, newStatus, staffId);
    }

    public boolean deleteRequest(int requestId) {
        return supportRepository.deleteRequest(requestId);
    }
}
