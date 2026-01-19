package com.edunexus.backend.lostfound;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lostfound")
@CrossOrigin("*")
public class LostFoundClaimController {

    @Autowired private LostFoundRepository itemRepo;
    @Autowired private LostFoundClaimRepository claimRepo;

    @GetMapping("/items")
    public List<LostFound> listItemsForStudents(Authentication auth) {
        String studentId = auth.getName(); // eduId from JWT
        return itemRepo.findAvailableForStudent(studentId);
    }

    
    @PostMapping("/items/{itemId}/claim")
    public ResponseEntity<?> claimItem(Authentication auth, @PathVariable String itemId) {
        String studentId = auth.getName();

        LostFound item = itemRepo.findById(itemId)
            .orElseThrow(() -> new RuntimeException("ITEM_NOT_FOUND"));

        if (item.isDelivered()) {
            return ResponseEntity.badRequest().body(Map.of("error","ITEM_ALREADY_DELIVERED"));
        }

        if (claimRepo.existsByItem_ItemIdAndStudentId(itemId, studentId)) {
            return ResponseEntity.badRequest().body(Map.of("error","ALREADY_CLAIMED"));
        }

        LostFoundClaim claim = new LostFoundClaim();
        claim.setItem(item);
        claim.setStudentId(studentId);
        claim.setStatus("PENDING");
        claim.setCreatedAt(java.time.LocalDateTime.now());

        claimRepo.save(claim);

        return ResponseEntity.ok(Map.of("message","CLAIM_SUBMITTED"));
    }


    // ✅ Teacher: view pending claims for items assigned to me
    @GetMapping("/claims/pending")
    public List<LostFoundClaim> teacherPendingClaims(Authentication auth) {
        String teacherId = auth.getName();
        return claimRepo.findByItem_AssignedToAndStatusOrderByCreatedAtDesc(teacherId, "PENDING");
    }

    // ✅ Teacher: approve -> mark delivered true + reject other pending claims for same item
    @PutMapping("/claims/{claimId}/approve")
    public ResponseEntity<?> approveClaim(Authentication auth, @PathVariable Long claimId) {
        String teacherId = auth.getName();

        LostFoundClaim claim = claimRepo.findById(claimId)
                .orElseThrow(() -> new RuntimeException("CLAIM_NOT_FOUND"));

        LostFound item = claim.getItem();

        if (item.getAssignedTo() == null || !teacherId.equals(item.getAssignedTo())) {
            return ResponseEntity.status(403).body(Map.of("error", "FORBIDDEN"));
        }

        if (item.isDelivered()) {
            return ResponseEntity.badRequest().body(Map.of("error", "ITEM_ALREADY_DELIVERED"));
        }

        // approve claim
        claim.setStatus("APPROVED");
        claim.setActionedAt(LocalDateTime.now());
        claim.setActionedByTeacher(teacherId);
        claimRepo.save(claim);

        // mark delivered
        item.setDelivered(true);
        itemRepo.save(item);

        // reject other pending claims
        List<LostFoundClaim> others = claimRepo.findByItem_ItemIdAndStatus(item.getItemId(), "PENDING");
        for (LostFoundClaim c : others) {
            if (!c.getId().equals(claimId)) {
                c.setStatus("REJECTED");
                c.setActionedAt(LocalDateTime.now());
                c.setActionedByTeacher(teacherId);
                claimRepo.save(c);
            }
        }

        return ResponseEntity.ok(Map.of("message", "APPROVED_AND_DELIVERED"));
    }

    // ✅ Teacher: reject claim
    @PutMapping("/claims/{claimId}/reject")
    public ResponseEntity<?> rejectClaim(Authentication auth, @PathVariable Long claimId) {
        String teacherId = auth.getName();

        LostFoundClaim claim = claimRepo.findById(claimId)
                .orElseThrow(() -> new RuntimeException("CLAIM_NOT_FOUND"));

        LostFound item = claim.getItem();

        if (item.getAssignedTo() == null || !teacherId.equals(item.getAssignedTo())) {
            return ResponseEntity.status(403).body(Map.of("error", "FORBIDDEN"));
        }

        claim.setStatus("REJECTED");
        claim.setActionedAt(LocalDateTime.now());
        claim.setActionedByTeacher(teacherId);
        claimRepo.save(claim);

        return ResponseEntity.ok(Map.of("message", "REJECTED"));
    }
}
