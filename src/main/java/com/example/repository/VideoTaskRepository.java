package com.example.repository;

import com.example.model.VideoTask;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VideoTaskRepository extends JpaRepository<VideoTask, Long> {
    List<VideoTask> findByVideoIdOrderByOrderIndex(Long videoId);
}
