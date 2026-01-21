package com.edunexus.backend.ptm;

import com.edunexus.backend.ptm.PtmSession;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PtmRepository extends JpaRepository<PtmSession,Long> {

 List<PtmSession> findByClassNo(Integer classNo);

 List<PtmSession> findByStudentId(String studentId);

 List<PtmSession> findByAdminId(String adminId);

 List<PtmSession> findByTeacherId(String teacherId);
}
