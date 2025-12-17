//package com.edunexus.backend.teacher;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/teachers")
//@CrossOrigin("*")
//public class TeacherController {
//	@Autowired
//    private TeacherRepository teacherRepo;
//
//    @GetMapping("/by-class/{className}")
//    public List<TeacherDTO> getByClass(@PathVariable String className) {
//        return teacherRepo.findByTeacherClass(className)
//                .stream()
//                .map(t -> new TeacherDTO(
//                        t.getTeacher_id(),
//                        t.getTeacher_name()
//                ))
//                .toList();
//    }
//}
