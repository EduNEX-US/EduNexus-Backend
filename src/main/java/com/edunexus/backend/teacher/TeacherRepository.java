package com.edunexus.backend.teacher;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher, String> {
	Optional<Teacher> findByIsAdmin(int isAdmin);
//	List<Teacher> findByTeacherClass(String teacher_class);
}
