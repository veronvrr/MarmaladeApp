//package com.example.repository;
//
//import com.example.model.LessonRequest;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.List;
//
//public interface LessonRequestRepository extends JpaRepository<LessonRequest, Long> {
////    List<LessonRequest> findByTeacherId(Long teacherId);
//    List<LessonRequest> findByTeacherIdOrderByIdDesc(Long teacherId);
//    List<LessonRequest> findByStudentUsernameOrderByIdDesc(String studentUsername);
//}
package com.example.repository;
import com.example.model.LessonRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LessonRequestRepository extends JpaRepository<LessonRequest, Long> {
    List<LessonRequest> findByStudentUsernameOrderByCreatedAtDesc(String studentUsername);
    List<LessonRequest> findByTeacherIdOrderByCreatedAtDesc(Long teacherId);
}
