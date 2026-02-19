package com.example.service;

import com.example.model.Teacher;
import com.example.repository.TeacherRepository;
import com.example.service.teacher.TeacherSorter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeacherService {
    private final TeacherRepository repo;
    private final TeacherSorter sorter;

    public TeacherService(TeacherRepository repo, TeacherSorter sorter) {
        this.repo = repo;
        this.sorter = sorter;
    }

    public List<Teacher> findAll() {
        return repo.findAll();
    }

    public List<Teacher> findAllSorted(String sortKey) {
        List<Teacher> all = repo.findAll();
        return sorter.sort(all, sortKey);
    }

    public Optional<Teacher> findById(Long id) {
        return repo.findById(id);
    }
        //  метод обновления
        public void updateTeacher(Long id, Teacher updated) {
            Teacher t = repo.findById(id).orElseThrow();
            t.setFullName(updated.getFullName());
            t.setLanguage(updated.getLanguage());
            t.setLevel(updated.getLevel());
            t.setPrice(updated.getPrice());
            t.setPhoto(updated.getPhoto());
            t.setBio(updated.getBio());
            repo.save(t);
        }


        public Teacher createTeacher(Teacher teacher) {
            return repo.save(teacher);
        }


        public void deleteTeacher(Long id) {
            repo.deleteById(id);
        }


}
