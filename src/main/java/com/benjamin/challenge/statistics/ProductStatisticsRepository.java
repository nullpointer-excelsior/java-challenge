package com.benjamin.challenge.statistics;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductStatisticsRepository extends JpaRepository<ProductStatistics, Long> {
    Optional<ProductStatistics> findByCategory(String category);
}
