package com.example.repository;

import com.example.model.AudioTask;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AudioTaskRepository extends JpaRepository<AudioTask, Long> {
    List<AudioTask> findByAudioIdOrderByOrderIndex(Long audioId);
}
