//
//package com.example.service;
//
//import com.example.model.LessonRequest;
//import com.example.model.Teacher;
//import com.example.repository.LessonRequestRepository;
//import com.example.repository.TeacherRepository;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class LessonRequestService {
//    private final LessonRequestRepository reqRepo;
//    private final TeacherRepository teacherRepo;
//
//    public LessonRequestService(LessonRequestRepository reqRepo, TeacherRepository teacherRepo) {
//        this.reqRepo = reqRepo;
//        this.teacherRepo = teacherRepo;
//    }
//
//    /**
//     * Создаёт заявку, привязанную к teacherId и username пользователя.
//     * studentDisplayName может быть null.
//     */
//    public LessonRequest createRequest(Long teacherId, String studentUsername, String studentDisplayName) {
//        Optional<Teacher> tOpt = teacherRepo.findById(teacherId);
//        if (tOpt.isEmpty()) return null;
//        LessonRequest r = new LessonRequest(studentUsername, studentDisplayName, tOpt.get());
//        return reqRepo.save(r);
//    }
//
//    public List<LessonRequest> findByStudentUsername(String username) {
//        return reqRepo.findByStudentUsernameOrderByIdDesc(username);
//    }
//
//    public List<LessonRequest> findByTeacherId(Long teacherId) {
//        return reqRepo.findByTeacherIdOrderByIdDesc(teacherId);
//    }
//
//    public void deleteById(Long id) { reqRepo.deleteById(id); }
//
//    public List<LessonRequest> findAll() { return reqRepo.findAll(); }
//
//    public boolean updateStatus(Long requestId, String status) {
//        Optional<LessonRequest> r = reqRepo.findById(requestId);
//        if (r.isEmpty()) return false;
//        LessonRequest req = r.get();
//        req.setStatus(status);
//        reqRepo.save(req);
//        return true;
//    }
//    // src/main/java/com/example/service/LessonRequestService.java
//    public boolean cancelByIdIfOwner(Long requestId, String username) {
//        Optional<LessonRequest> o = reqRepo.findById(requestId);
//        if (o.isEmpty()) return false;
//        LessonRequest r = o.get();
//        // проверяем, что заявка принадлежит пользователю (логин/username)
//        if (r.getStudentUsername() == null || !r.getStudentUsername().equals(username)) {
//            return false;
//        }
//        r.setStatus("CANCELLED");
//        reqRepo.save(r);
//        return true;
//    }
//
//    // если хочешь и метод удаления:
//    public boolean deleteByIdIfOwner(Long requestId, String username) {
//        Optional<LessonRequest> o = reqRepo.findById(requestId);
//        if (o.isEmpty()) return false;
//        LessonRequest r = o.get();
//        if (r.getStudentUsername() == null || !r.getStudentUsername().equals(username)) {
//            return false;
//        }
//        reqRepo.deleteById(requestId);
//        return true;
//    }
//
//}
package com.example.service;

import com.example.model.LessonRequest;
import com.example.model.Teacher;
import com.example.repository.LessonRequestRepository;
import com.example.repository.TeacherRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LessonRequestService {
    private final LessonRequestRepository reqRepo;
    private final TeacherRepository teacherRepo;

    public LessonRequestService(LessonRequestRepository reqRepo, TeacherRepository teacherRepo) {
        this.reqRepo = reqRepo;
        this.teacherRepo = teacherRepo;
    }

    public LessonRequest createRequest(Long teacherId, String studentUsername, String studentName) {
        Optional<Teacher> tOpt = teacherRepo.findById(teacherId);
        if (tOpt.isEmpty()) return null;
        LessonRequest r = new LessonRequest(studentUsername, studentName, tOpt.get());
        return reqRepo.save(r);
    }

//    public List<LessonRequest> findByStudentUsername(String username) {
//        return reqRepo.findByStudentUsernameOrderByCreatedAtDesc(username);
//    }
public List<LessonRequest> findByStudentUsername(String username) {
    List<LessonRequest> res = reqRepo.findByStudentUsernameOrderByCreatedAtDesc(username);
    return res == null ? Collections.emptyList() : res.stream().filter(Objects::nonNull).collect(Collectors.toList());
}

    public List<LessonRequest> findByTeacherId(Long teacherId) {
        return reqRepo.findByTeacherIdOrderByCreatedAtDesc(teacherId);
    }

    public List<LessonRequest> findAllSorted() {
        return reqRepo.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    public boolean setStatus(Long requestId, String newStatus) {
        Optional<LessonRequest> o = reqRepo.findById(requestId);
        if (o.isEmpty()) return false;
        LessonRequest r = o.get();
        r.setStatus(newStatus);
        reqRepo.save(r);
        return true;
    }

    public boolean cancelIfOwner(Long requestId, String username) {
        Optional<LessonRequest> o = reqRepo.findById(requestId);
        if (o.isEmpty()) return false;
        LessonRequest r = o.get();
        if (!username.equals(r.getStudentUsername())) return false;
        r.setStatus("CANCELLED");
        reqRepo.save(r);
        return true;
    }
}
