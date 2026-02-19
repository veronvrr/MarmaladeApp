package com.example.repository;

import com.example.model.TextTask;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TextTaskRepository extends JpaRepository<TextTask, Long> {
    List<TextTask> findByTextIdOrderByOrderIndex(Long textId);
}
