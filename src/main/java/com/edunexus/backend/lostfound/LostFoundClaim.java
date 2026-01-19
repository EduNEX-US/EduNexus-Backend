package com.edunexus.backend.lostfound;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "lost_found_claim")
public class LostFoundClaim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id", referencedColumnName = "item_id", nullable = false)
    private LostFound item;

    @Column(name = "student_id", nullable = false)
    private String studentId;

    @Column(name = "student_note")
    private String studentNote;

    // PENDING / APPROVED / REJECTED
    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "actioned_at")
    private LocalDateTime actionedAt;

    @Column(name = "actioned_by_teacher")
    private String actionedByTeacher;

    public Long getId() { return id; }

    public LostFound getItem() { return item; }
    public void setItem(LostFound item) { this.item = item; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getStudentNote() { return studentNote; }
    public void setStudentNote(String studentNote) { this.studentNote = studentNote; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getActionedAt() { return actionedAt; }
    public void setActionedAt(LocalDateTime actionedAt) { this.actionedAt = actionedAt; }

    public String getActionedByTeacher() { return actionedByTeacher; }
    public void setActionedByTeacher(String actionedByTeacher) { this.actionedByTeacher = actionedByTeacher; }
}
