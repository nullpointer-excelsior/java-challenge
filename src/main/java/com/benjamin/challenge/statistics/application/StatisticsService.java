package com.benjamin.challenge.statistics.application;

import com.benjamin.challenge.statistics.domain.ProductStatistics;
import com.benjamin.challenge.statistics.domain.ProductStatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatisticsService {

    @Autowired
    private ProductStatisticsRepository statisticsRepository;

    public void updateStatistics(String category) {
        var statistics = statisticsRepository.findByCategory(category)
                .orElse(ProductStatistics.builder()
                        .category(category)
                        .productCount(0L)
                        .build());
        statistics.setProductCount(statistics.getProductCount() + 1);
        statisticsRepository.save(statistics);
    }

    public List<ProductStatistics> getAllStatistics() {
        return statisticsRepository.findAll();
    }
}
