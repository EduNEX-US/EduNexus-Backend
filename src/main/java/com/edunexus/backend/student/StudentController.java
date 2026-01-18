package com.edunexus.backend.student;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
public class StudentController {
	
	  @Autowired
	    private StudentRepository studentRepo;
	  
	  @GetMapping("/students")
	    public List<StudentDTO> getStudents() {
	       return studentRepo.findAll()
	       .stream()
           .map(s -> new StudentDTO(
        		   s.getStud_id(),
                   s.getStud_name(),
                   s.getStud_email(),
                   s.getStud_address(),
                   s.getStud_guardian(),
                   s.getStud_mobile()
           ))
           .toList();
	    }
}
