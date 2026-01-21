package com.edunexus.backend.ptm;



import java.util.List;

import org.springframework.stereotype.Service;

import com.edunexus.backend.emailService.MailService;
import com.edunexus.backend.teacher.Teacher;
import com.edunexus.backend.teacher.TeacherRepository;
// import com.edunexus.backend.student.StudentRepository;  // when you share it
// import com.edunexus.backend.admin.AdminRepository;      // when you share it

@Service
public class PtmNotificationService {

  private final TeacherRepository teacherRepo;
  private final MailService mailService;

  public PtmNotificationService(TeacherRepository teacherRepo, MailService mailService) {
    this.teacherRepo = teacherRepo;
    this.mailService = mailService;
  }

  public void notifyTeacher(String teacherId, String subject, String body) {
    Teacher t = teacherRepo.findById(teacherId).orElse(null);
    if (t == null) return;

    String email = t.getTeacher_email();
    if (email == null || email.isBlank()) return;

    mailService.sendMail(email, subject, body);
  }

  // ✅ If Admin schedules for ALL teachers (bulk), call this:
  public void notifyTeachers(List<String> teacherIds, String subject, String body) {
    for (String teacherId : teacherIds) {
      notifyTeacher(teacherId, subject, body);
    }
  }

  // 🔜 Add these when you share Student/Admin repo:
  // public void notifyStudent(String studentId, String subject, String body) { ... }
  // public void notifyAdmin(String adminId, String subject, String body) { ... }
}
