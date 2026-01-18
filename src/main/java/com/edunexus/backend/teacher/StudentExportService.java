package com.edunexus.backend.teacher;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edunexus.backend.student.Student;
import com.edunexus.backend.student.StudentRepository;

@Service
public class StudentExportService {

    @Autowired
    private StudentRepository studentRepo;

    public String exportStudentsCsv(int teacherClass) {

        StringBuilder sb = new StringBuilder();
        sb.append("id,name,email,mobile,altMob,guardian,address,class\n");
        
        List<Student> students = studentRepo.findByStudClass(teacherClass);

        for (Student s : students) {
            sb.append(csv(s.getStud_id())).append(",")
              .append(csv(s.getStud_name())).append(",")
              .append(csv(s.getStud_email())).append(",")
              .append(csv(String.valueOf(s.getStud_mobile()))).append(",")
              .append(csv(String.valueOf(s.getStud_alt_mob()))).append(",")
              .append(csv(s.getStud_guardian())).append(",")
              .append(csv(s.getStud_address())).append(",")
              .append(csv(String.valueOf(s.getStud_class())))
              .append("\n");
        }

        return sb.toString();
    }

    private String csv(String i) {
        if (i == null) return "";
        String escaped = i.replace("\"", "\"\"");
        if (escaped.contains(",") || escaped.contains("\n") || escaped.contains("\r")) {
            return "\"" + escaped + "\"";
        }
        return escaped;
    }
}
