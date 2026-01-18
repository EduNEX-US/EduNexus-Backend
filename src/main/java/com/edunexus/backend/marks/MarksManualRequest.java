package com.edunexus.backend.marks;

import java.util.List;

public class MarksManualRequest {
    private int classId;
    private String examSession; // "UNIT_1" | "MID_SEM" | "UNIT_2" | "END"
    private String resultDate;  // "YYYY-MM-DD"
    private List<Entry> entries;

    public int getClassId() { return classId; }
    public void setClassId(int classId) { this.classId = classId; }

    public String getExamSession() { return examSession; }
    public void setExamSession(String examSession) { this.examSession = examSession; }

    public String getResultDate() { return resultDate; }
    public void setResultDate(String resultDate) { this.resultDate = resultDate; }

    public List<Entry> getEntries() { return entries; }
    public void setEntries(List<Entry> entries) { this.entries = entries; }

    public static class Entry {
        private String studentId;

        private Double english;
        private Double hindi;
        private Double math;
        private Double science;

        // interchangeable:
        private Double gk;            // for class <=5
        private Double socialScience; // for class >=6

        private Double computer;

        public String getStudentId() { return studentId; }
        public void setStudentId(String studentId) { this.studentId = studentId; }

        public Double getEnglish() { return english; }
        public void setEnglish(Double english) { this.english = english; }

        public Double getHindi() { return hindi; }
        public void setHindi(Double hindi) { this.hindi = hindi; }

        public Double getMath() { return math; }
        public void setMath(Double math) { this.math = math; }

        public Double getScience() { return science; }
        public void setScience(Double science) { this.science = science; }

        public Double getGk() { return gk; }
        public void setGk(Double gk) { this.gk = gk; }

        public Double getSocialScience() { return socialScience; }
        public void setSocialScience(Double socialScience) { this.socialScience = socialScience; }

        public Double getComputer() { return computer; }
        public void setComputer(Double computer) { this.computer = computer; }
    }
}
