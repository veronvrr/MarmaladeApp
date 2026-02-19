//package com.example.model;
//
//import jakarta.persistence.*;
//
//@Entity
//public class LessonRequest {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String studentName;
//    private String studentEmail;
//
//    @ManyToOne
//    @JoinColumn(name = "teacher_id")
//    private Teacher teacher;
//
//
//    public LessonRequest() {}
//
//    public LessonRequest(String studentName, String studentEmail, Teacher teacher) {
//        this.studentName = studentName;
//        this.studentEmail = studentEmail;
//        this.teacher = teacher;
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public String getStudentName() {
//        return studentName;
//    }
//
//    public void setStudentName(String studentName) {
//        this.studentName = studentName;
//    }
//
//    public String getStudentEmail() {
//        return studentEmail;
//    }
//
//    public void setStudentEmail(String studentEmail) {
//        this.studentEmail = studentEmail;
//    }
//
//    public Teacher getTeacher() {
//        return teacher;
//    }
//
//    public void setTeacher(Teacher teacher) {
//        this.teacher = teacher;
//    }
//}
package com.example.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "lesson_request")
public class LessonRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Отображаемое имя (опционально, например "Вероника" — поможет преподавателю)
    @Column(name = "student_name")
    private String studentName;

    // Логин (username) — основной идентификатор пользователя, сохраняется в заявке
    @Column(name = "student_username")
    private String studentUsername;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;
    @Column(length = 16)
    private String status = "PENDING";
    private LocalDateTime createdAt = LocalDateTime.now();
    public LessonRequest() {}

    // конструктор
    public LessonRequest(String studentUsername, String studentName, Teacher teacher) {
        this.studentUsername = studentUsername;
        this.studentName = studentName;
        this.teacher = teacher;
        this.status = "PENDING";
        this.createdAt = LocalDateTime.now();
    }

    // getters / setters
    public Long getId() { return id; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getStudentUsername() { return studentUsername; }
    public void setStudentUsername(String studentUsername) { this.studentUsername = studentUsername; }

    public Teacher getTeacher() { return teacher; }
    public void setTeacher(Teacher teacher) { this.teacher = teacher; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
}
