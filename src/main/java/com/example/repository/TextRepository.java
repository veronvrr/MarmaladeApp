package com.example.repository;

import com.example.model.Text;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TextRepository extends JpaRepository<Text, Long> {
    List<Text> findByLevel(String level);
}
