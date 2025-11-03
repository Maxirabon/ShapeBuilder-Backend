package com.example.shapebuilderbackend.Repository;

import com.example.shapebuilderbackend.Model.WeightHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WeightHistoryRepository extends JpaRepository<WeightHistory, Long> {
    List<WeightHistory> findByUserIdOrderByDateDesc(Long userId);
}
