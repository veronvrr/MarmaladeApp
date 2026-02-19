package com.example.service.teacher;

import com.example.model.Teacher;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component("name")
public class SortByNameStrategy implements TeacherSortStrategy {

    @Override
    public List<Teacher> sort(List<Teacher> input) {
        List<Teacher> copy = new ArrayList<>(input);
        copy.sort(Comparator.comparing(t -> t.getFullName() == null ? "" : t.getFullName().toLowerCase()));
        return copy;
    }

    @Override
    public String getKey() { return "name"; }
}
