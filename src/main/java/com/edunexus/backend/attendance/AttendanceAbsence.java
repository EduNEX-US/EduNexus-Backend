package com.edunexus.backend.attendance;

import jakarta.persistence.*;

@Entity
@Table(
    name = "attendance_absence",
    uniqueConstraints = @UniqueConstraint(columnNames = {"session_id", "student_id"})
)
public class AttendanceAbsence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="session_id", nullable=false)
    private Long sessionId;

    @Column(name="student_id", nullable=false, length=50)
    private String studentId;

    @Enumerated(EnumType.STRING)
    @Column(name="status", nullable=false)
    private AttendanceStatus status = AttendanceStatus.ABSENT;

    public Long getId() { return id; }

    public Long getSessionId() { return sessionId; }
    public void setSessionId(Long sessionId) { this.sessionId = sessionId; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public AttendanceStatus getStatus() { return status; }
    public void setStatus(AttendanceStatus status) { this.status = status; }
}
