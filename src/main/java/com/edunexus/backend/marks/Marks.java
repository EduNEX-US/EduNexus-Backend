package com.edunexus.backend.marks;

import java.time.LocalDate;

import com.edunexus.backend.student.Student;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "marks")
public class Marks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "student_id", nullable = false)
    private Student student;

    @Column(name = "english")
    private Double english;

    @Column(name = "hindi")
    private Double hindi;

    @Column(name = "math")
    private Double math;

    @Column(name = "science")
    private Double science;

    @Column(name = "social_science")
    private Double socialScience;

    @Column(name = "general_knowledge")
    private Double generalKnowledge;

    @Column(name = "computer")
    private Double computer;

    @Column(name = "result_date")
    private LocalDate resultDate;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Double getEnglish() {
        return english;
    }

    public void setEnglish(Double english) {
        this.english = english;
    }

    public Double getHindi() {
        return hindi;
    }

    public void setHindi(Double hindi) {
        this.hindi = hindi;
    }

    public Double getMath() {
        return math;
    }

    public void setMath(Double math) {
        this.math = math;
    }

    public Double getScience() {
        return science;
    }

    public void setScience(Double science) {
        this.science = science;
    }

    public Double getSocialScience() {
        return socialScience;
    }

    public void setSocialScience(Double socialScience) {
        this.socialScience = socialScience;
    }

    public Double getGeneralKnowledge() {
        return generalKnowledge;
    }

    public void setGeneralKnowledge(Double generalKnowledge) {
        this.generalKnowledge = generalKnowledge;
    }

    public Double getComputer() {
        return computer;
    }

    public void setComputer(Double computer) {
        this.computer = computer;
    }

    public LocalDate getResultDate() {
        return resultDate;
    }

    public void setResultDate(LocalDate resultDate) {
        this.resultDate = resultDate;
    }
}
