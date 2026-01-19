package com.edunexus.backend.lostfound;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LostFoundClaimRepository extends JpaRepository<LostFoundClaim, Long> {

    List<LostFoundClaim> findByItem_AssignedToAndStatusOrderByCreatedAtDesc(String assignedTo, String status);

    boolean existsByItem_ItemIdAndStudentId(String itemId, String studentId);

    List<LostFoundClaim> findByItem_ItemIdAndStatus(String itemId, String status);
}
