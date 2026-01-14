package com.edunexus.backend.ptm;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PTMRepository extends JpaRepository<PTM, String> {

	List<PTM> findByStatus(String status);
	
	Optional<PTM> findByPtmId(String meetingId);
	
    List<PTM> findByStatusAndClassName(String status, String sClass);
}
