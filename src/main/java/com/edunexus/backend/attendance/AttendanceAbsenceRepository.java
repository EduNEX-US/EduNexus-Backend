package com.edunexus.backend.attendance;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceAbsenceRepository extends JpaRepository<AttendanceAbsence, Long> {
    void deleteBySessionId(Long sessionId);
    List<AttendanceAbsence> findBySessionId(Long sessionId);
    long countBySessionId(Long sessionId);
    long countByStudentIdAndSessionIdIn(String studentId, List<Long> sessionIds);
}
