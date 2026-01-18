package com.edunexus.backend.student;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudentRepository extends JpaRepository<Student, String> {

    // ✅ your entity field is stud_class (int)
    @Query("SELECT s FROM Student s WHERE s.stud_class = :cls")
    List<Student> findByStudClass(@Param("cls") int cls);

    // (optional) if you want only IDs or name later, you can add more queries similarly
}
