package com.example.service.teacher;

import com.example.model.Teacher;
import java.util.List;

public interface TeacherSortStrategy {

    List<Teacher> sort(List<Teacher> input);


    String getKey();
}
