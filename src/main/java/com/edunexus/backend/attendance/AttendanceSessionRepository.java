package com.edunexus.backend.attendance;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceSessionRepository extends JpaRepository<AttendanceSession, Long> {
    Optional<AttendanceSession> findByClassIdAndDate(int classId, LocalDate date);
    List<AttendanceSession> findByClassIdAndDateBetween(int classId, LocalDate start, LocalDate end);
}

