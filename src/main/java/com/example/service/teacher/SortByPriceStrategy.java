package com.example.service.teacher;

import com.example.model.Teacher;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component("price")
public class SortByPriceStrategy implements TeacherSortStrategy {

    @Override
    public List<Teacher> sort(List<Teacher> input) {
        List<Teacher> copy = new ArrayList<>(input);

        copy.sort(Comparator.comparing((Teacher t) -> t.getPrice() == null ? Double.MAX_VALUE : t.getPrice()));
        return copy;
    }

    @Override
    public String getKey() { return "price"; }
}
