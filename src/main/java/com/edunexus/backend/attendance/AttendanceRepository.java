package com.edunexus.backend.attendance;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AttendanceRepository extends JpaRepository<AttendanceMonth, Long> {

    @Query("""
      SELECT a FROM AttendanceMonth a
      WHERE a.studentId = :studentId AND a.yearMonth = :yearMonth
    """)
    Optional<AttendanceMonth> findByStudentAndMonth(
            @Param("studentId") String studentId,
            @Param("yearMonth") String yearMonth
    );

    @Query("select count(a) from AttendanceMonth a where a.classId = :classId and a.yearMonth = :yearMonth")
    int countByClassAndMonth(@Param("classId") int classId, @Param("yearMonth") String yearMonth);

    
    @Query("""
      SELECT a FROM AttendanceMonth a
      WHERE a.classId = :classId AND a.yearMonth = :yearMonth
      ORDER BY a.studentId
    """)
    List<AttendanceMonth> findByClassAndMonth(
            @Param("classId") int classId,
            @Param("yearMonth") String yearMonth
    );

    @Query("""
      SELECT a FROM AttendanceMonth a
      WHERE a.studentId = :studentId
      ORDER BY a.yearMonth DESC
    """)
    List<AttendanceMonth> findAllForStudent(@Param("studentId") String studentId);

    @Query("""
    		  SELECT COALESCE(SUM(a.totalDays),0),
    		         COALESCE(SUM(a.present),0),
    		         COALESCE(SUM(a.absent),0),
    		         COALESCE(SUM(a.late),0)
    		  FROM AttendanceMonth a
    		  WHERE a.studentId = :studentId
    		""")
    		List<Object[]> totalsForStudent(@Param("studentId") String studentId);

}
