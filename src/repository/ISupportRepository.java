package repository;

import models.SupportRequest;
import java.util.List;

/**
 * Interface định nghĩa thao tác với bảng tbl_support_requests (Yêu cầu hỗ trợ)
 */
public interface ISupportRepository {

    /**
     * Lấy tất cả yêu cầu hỗ trợ (sắp xếp mới nhất trước)
     * 
     * @return List<SupportRequest>
     */
    List<SupportRequest> getAllRequests();

    /**
     * Thêm yêu cầu hỗ trợ mới
     * 
     * @param request SupportRequest
     * @return true nếu thành công
     */
    boolean addRequest(SupportRequest request);

    /**
     * Cập nhật trạng thái yêu cầu
     * 
     * @param id      Request ID
     * @param status  Trạng thái mới (Mới, Đang xử lý, v.v...)
     * @param staffId ID nhân viên xử lý
     * @return true nếu thành công
     */
    boolean updateStatus(int id, String status, Integer staffId);

    /**
     * Xóa yêu cầu
     * 
     * @param id Request ID
     * @return true nếu thành công
     */
    boolean deleteRequest(int id);
}
