package com.example.repository;

import com.example.model.Audio;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AudioRepository extends JpaRepository<Audio, Long> {
    List<Audio> findByLevel(String level);
}
