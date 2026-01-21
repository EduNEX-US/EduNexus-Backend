// ===================== PtmController.java (UPDATED: student fetching only) =====================
package com.edunexus.backend.ptm;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edunexus.backend.student.Student;
import com.edunexus.backend.student.StudentRepository;
import com.edunexus.backend.teacher.Teacher;


@RestController
@RequestMapping("/api/ptm")
@CrossOrigin
public class PtmController {

  @Autowired
  private PtmRepository repo;

  @Autowired
  private PtmNotificationService notificationService;
  
  @Autowired
  private com.edunexus.backend.teacher.TeacherRepository teacherRepo;

  // ✅ ADD THIS (only for student fetching)
  @Autowired
  private StudentRepository studentRepo;

  
  @PostMapping("/create")
  public PtmSession create(@RequestBody PtmSession ptm) {

      if (ptm.getPurpose() != null && ptm.getPurpose().length() > 255) {
          ptm.setPurpose(ptm.getPurpose().substring(0, 255));
      }

      ptm.setStatus("SCHEDULED");

      // ===================== CASE 1: Admin schedules for ALL teachers =====================
      // Frontend will send: ptmTarget="ADMIN", ptmScope="ALL", adminId filled
      if ("ADMIN".equalsIgnoreCase(ptm.getPtmTarget()) &&
          "ALL".equalsIgnoreCase(ptm.getPtmScope())) {

          // ✅ Save ONE master record for admin (admin sees only one entry)
          PtmSession adminMaster = new PtmSession();
          adminMaster.setPtmTarget("ADMIN");
          adminMaster.setPtmScope("ALL");
          adminMaster.setAdminId(ptm.getAdminId());
          adminMaster.setMeetLink(ptm.getMeetLink());
          adminMaster.setPurpose(ptm.getPurpose());
          adminMaster.setPtmDate(ptm.getPtmDate());
          adminMaster.setStartTime(ptm.getStartTime());
          adminMaster.setEndTime(ptm.getEndTime());
          adminMaster.setStatus("SCHEDULED");
          PtmSession savedMaster = repo.save(adminMaster);

          // ✅ Fetch all teachers
          List<com.edunexus.backend.teacher.Teacher> teachers = teacherRepo.findAll();

          // ✅ Create ONE_TO_ONE PTM for each teacher (teachers get their own link)
          for (com.edunexus.backend.teacher.Teacher t : teachers) {
              PtmSession teacherPtm = new PtmSession();
              teacherPtm.setPtmTarget("TEACHER");
              teacherPtm.setPtmScope("ONE_TO_ONE");
              teacherPtm.setTeacherId(t.getTeacher_id()); // <-- IMPORTANT: use your actual getter
              teacherPtm.setAdminId(ptm.getAdminId());
              teacherPtm.setMeetLink(ptm.getMeetLink());
              teacherPtm.setPurpose(ptm.getPurpose());
              teacherPtm.setPtmDate(ptm.getPtmDate());
              teacherPtm.setStartTime(ptm.getStartTime());
              teacherPtm.setEndTime(ptm.getEndTime());
              teacherPtm.setStatus("SCHEDULED");

              repo.save(teacherPtm);

              // ✅ notify each teacher
              String subject = "New PTM Scheduled";
              String body =
                  "A PTM has been scheduled.\n\n" +
                  "Purpose: " + (ptm.getPurpose() == null ? "-" : ptm.getPurpose()) + "\n" +
                  "Date: " + ptm.getPtmDate() + "\n" +
                  "Time: " + ptm.getStartTime() + " - " + ptm.getEndTime() + "\n" +
                  "Meeting Link: " + ptm.getMeetLink();

              notificationService.notifyTeacher(t.getTeacher_id(), subject, body);
          }

          return savedMaster; // admin sees only one meeting
      }

      // ===================== CASE 2: Normal create (Teacher->Student OR Admin->Single Teacher) =====================
      PtmSession saved = repo.save(ptm);

      // notify teacher if teacherId is present
      if (ptm.getTeacherId() != null && !ptm.getTeacherId().isBlank()) {
          String subject = "New PTM Scheduled";
          String body =
              "A PTM has been scheduled.\n\n" +
              "Purpose: " + (ptm.getPurpose() == null ? "-" : ptm.getPurpose()) + "\n" +
              "Date: " + ptm.getPtmDate() + "\n" +
              "Time: " + ptm.getStartTime() + " - " + ptm.getEndTime() + "\n" +
              "Meeting Link: " + ptm.getMeetLink();

          notificationService.notifyTeacher(ptm.getTeacherId(), subject, body);
      }

      return saved;
  }


  // ===== STUDENT fetch =====
  @GetMapping("/student/class/{classNo}")
  public List<PtmSession> getByClass(@PathVariable Integer classNo) {
    return repo.findByClassNo(classNo);
  }

  @GetMapping("/student/one/{studentId}")
  public List<PtmSession> getByStudent(@PathVariable String studentId) {
    return repo.findByStudentId(studentId);
  }

  /**
   * ✅ NEW (recommended for student UI):
   * Student should see:
   *  1) CLASS PTMs of their class (ptmScope=CLASS, ptmTarget=STUDENT, classNo=their class)
   *  2) ONE_TO_ONE PTMs assigned to them (ptmScope=ONE_TO_ONE, ptmTarget=STUDENT, studentId=their id)
   *
   * Frontend will call:
   *   GET /api/ptm/student/{studentId}
   */
  @GetMapping("/student/{studentId}")
  public List<PtmSession> getForStudent(@PathVariable String studentId) {

    Student s = studentRepo.findById(studentId)
        .orElseThrow(() -> new RuntimeException("Student not found"));

    int classNo = s.getStud_class(); // ✅ IMPORTANT: matches your Student entity

    List<PtmSession> oneToOne = repo.findByStudentId(studentId);
    List<PtmSession> classMeetings = repo.findByClassNo(classNo);

    // ✅ merge + remove duplicates safely (in case someone saved both)
    List<PtmSession> merged = new ArrayList<>();
    for (PtmSession p : oneToOne) merged.add(p);

    for (PtmSession p : classMeetings) {
      boolean exists = false;
      for (PtmSession x : merged) {
        if (x.getId() != null && p.getId() != null && x.getId().equals(p.getId())) {
          exists = true;
          break;
        }
      }
      if (!exists) merged.add(p);
    }

    // Optional: sort by date+startTime if you want (can also sort in frontend)
    // merged.sort((a,b) -> (a.getPtmDate()+" "+a.getStartTime()).compareTo(b.getPtmDate()+" "+b.getStartTime()));

    return merged;
  }

  // ===== TEACHER fetch =====
  @GetMapping("/teacher/{teacherId}")
  public List<PtmSession> getByTeacher(@PathVariable String teacherId) {
    return repo.findByTeacherId(teacherId);
  }

  // ===== ADMIN fetch =====
  @GetMapping("/admin/{adminId}")
  public List<PtmSession> getByAdmin(@PathVariable String adminId) {
    return repo.findByAdminId(adminId);
  }
}
