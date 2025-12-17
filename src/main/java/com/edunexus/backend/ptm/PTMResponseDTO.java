package com.edunexus.backend.ptm;

import java.time.LocalDate;
import java.time.LocalTime;

public class PTMResponseDTO {

	    private String id;
	    private String sClass;
	    private String student;
	    private String teacher;
	    private LocalDate date;
	    private LocalTime time;

	    public PTMResponseDTO(PTM ptm) {
	        this.id = ptm.getPtmId();
	        this.sClass = ptm.getClassName();
	        this.student = ptm.getStudent().getStud_name();
	        this.teacher = ptm.getTeacher().getTeacher_name();
	        this.date = ptm.getPtmDate();
	        this.time = ptm.getPtmTime();
	    }

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getsClass() {
			return sClass;
		}

		public void setsClass(String sClass) {
			this.sClass = sClass;
		}

		public String getStudent() {
			return student;
		}

		public void setStudent(String student) {
			this.student = student;
		}

		public String getTeacher() {
			return teacher;
		}

		public void setTeacher(String teacher) {
			this.teacher = teacher;
		}

		public LocalDate getDate() {
			return date;
		}

		public void setDate(LocalDate date) {
			this.date = date;
		}

		public LocalTime getTime() {
			return time;
		}

		public void setTime(LocalTime time) {
			this.time = time;
		}
	    
	    
}


