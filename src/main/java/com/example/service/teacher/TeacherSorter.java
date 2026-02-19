package com.example.service.teacher;

import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.example.model.Teacher;

@Component
public class TeacherSorter {

    private final Map<String, TeacherSortStrategy> strategies;

    public TeacherSorter(List<TeacherSortStrategy> strategies) {
        this.strategies = strategies.stream().collect(Collectors.toMap(TeacherSortStrategy::getKey, s -> s));
    }


    public List<Teacher> sort(List<Teacher> input, String sortKey) {
        if (input == null) return List.of();
        String key = (sortKey == null) ? "name" : sortKey.trim().toLowerCase();
        TeacherSortStrategy strat = strategies.getOrDefault(key, strategies.get("name"));
        return strat.sort(input);
    }
}
