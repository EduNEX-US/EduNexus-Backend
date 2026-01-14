package com.edunexus.backend.admin;

import java.util.StringJoiner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edunexus.backend.teacher.Teacher;
import com.edunexus.backend.teacher.TeacherRepository;

@Service
public class AdminExportService {

    @Autowired
    private TeacherRepository teacherRepo;

    public String exportTeachersCsv(String defaultPassword) {
        // header
        StringBuilder sb = new StringBuilder();
        sb.append("id,name,email,mobile,experience,address,classes,qualification,role,password\n");

        for (Teacher t : teacherRepo.findAll()) {
            String role = (t.getIsAdmin() == 1) ? "admin" : "teacher";

            sb.append(csv(t.getTeacher_id())).append(",")
              .append(csv(t.getTeacher_name())).append(",")
              .append(csv(t.getTeacher_email())).append(",")
              .append(csv(String.valueOf(t.getTeacher_mob()))).append(",")
              .append(csv(String.valueOf(t.getTeacher_exp()))).append(",")
              .append(csv(t.getTeacher_add())).append(",")
              .append(csv(t.getTeacher_class())).append(",")
              .append(csv(t.getTeacher_qualification())).append(",")
              .append(csv(role)).append(",")
              .append(csv(defaultPassword))
              .append("\n");
        }

        return sb.toString();
    }

    private String csv(String s) {
        if (s == null) return "";
        // escape quotes + wrap if contains comma/newline
        String escaped = s.replace("\"", "\"\"");
        if (escaped.contains(",") || escaped.contains("\n") || escaped.contains("\r")) {
            return "\"" + escaped + "\"";
        }
        return escaped;
    }
}
