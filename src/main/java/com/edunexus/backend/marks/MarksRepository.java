package com.edunexus.backend.marks;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MarksRepository extends JpaRepository<Marks, Long> {

    // ✅ Upsert key: one row per student per exam session
    @Query("""
        SELECT m FROM Marks m
        WHERE m.student.stud_id = :studentId
          AND m.examSession = :examSession
    """)
    Optional<Marks> findOneByStudentIdAndSession(
            @Param("studentId") String studentId,
            @Param("examSession") ExamSession examSession
    );

    // ✅ Teacher view (all students marks of class for a session)
    @Query("""
        SELECT m FROM Marks m
        WHERE m.classId = :classId
          AND m.examSession = :examSession
        ORDER BY m.student.stud_id
    """)
    List<Marks> findAllByClassIdAndSession(
            @Param("classId") int classId,
            @Param("examSession") ExamSession examSession
    );

    // ✅ Student view (my marks for a session)
    @Query("""
        SELECT m FROM Marks m
        WHERE m.student.stud_id = :studentId
          AND m.examSession = :examSession
    """)
    Optional<Marks> findMyMarksForSession(
            @Param("studentId") String studentId,
            @Param("examSession") ExamSession examSession
    );
}
