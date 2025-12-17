package com.edunexus.backend.ptm;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PTMRepository extends JpaRepository<PTM, String> {

	List<PTM> findByStatus(String status);

    List<PTM> findByStatusAndClassName(String status, String sClass);
}
