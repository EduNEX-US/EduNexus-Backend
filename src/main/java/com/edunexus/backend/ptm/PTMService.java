package com.edunexus.backend.ptm;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edunexus.backend.student.Student;
import com.edunexus.backend.student.StudentRepository;
import com.edunexus.backend.teacher.Teacher;
import com.edunexus.backend.teacher.TeacherRepository;

@Service
public class PTMService {
	@Autowired
	private PTMRepository ptmRepo;
	
	@Autowired
	private TeacherRepository teacherRepo;
	
	@Autowired
	private StudentRepository studentRepo;
	
	public PTM schedulePTM(PTMScheduleRequest req) {

        Student student = studentRepo.findById(req.getStudent()) 
                .orElseThrow(() -> new RuntimeException("Student not found")); 
        			//handles find student with given ID.
        Teacher teacher = teacherRepo.findById(req.getTeacher())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
        			//handles find teacher with given ID.
        
        PTM ptm = new PTM();
        ptm.setPtmId("PTM" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        ptm.setClassName(req.getSClass());
        ptm.setStudent(student);
        ptm.setTeacher(teacher);
        ptm.setPtmDate(req.getDate());
        ptm.setPtmTime(req.getTime());
        ptm.setStatus("SCHEDULED");
        
        return ptmRepo.save(ptm);
	}
	
	public List<PTMResponseDTO> getCompletedPTMs(String sClass) {

        List<PTM> ptms = (sClass == null || sClass.isEmpty())
                ? ptmRepo.findByStatus("COMPLETED")
                : ptmRepo.findByStatusAndClassName("COMPLETED", sClass);

        return ptms.stream()
                .map(PTMResponseDTO::new)
                .toList();
    }
	
}
