package com.edunexus.backend.lostfound;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LostFoundRepository extends JpaRepository<LostFound, String> {
    List<LostFound> findByDeliveredFalseOrderByDateDesc();
    
    @Query("""
    	    SELECT lf FROM LostFound lf
    	    WHERE lf.delivered = false
    	      AND lf.itemId NOT IN (
    	          SELECT c.item.itemId FROM LostFoundClaim c
    	          WHERE c.studentId = :studentId
    	      )
    	    ORDER BY lf.date DESC
    	""")
    	List<LostFound> findAvailableForStudent(@Param("studentId") String studentId);

}
