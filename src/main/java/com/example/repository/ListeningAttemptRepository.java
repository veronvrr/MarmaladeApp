package com.example.repository;

import com.example.model.ListeningAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ListeningAttemptRepository extends JpaRepository<ListeningAttempt, Long> {
    List<ListeningAttempt> findByAudioIdOrderByCreatedAtDesc(Long audioId);
    List<ListeningAttempt> findByUserIdOrderByCreatedAtDesc(Long userId);
}
