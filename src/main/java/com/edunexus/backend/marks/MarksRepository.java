package com.edunexus.backend.marks;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarksRepository extends JpaRepository<Marks, Long> {
    // Custom query methods if needed, e.g., to find marks by student
    // List<Marks> findByStudent(Student student);
}
