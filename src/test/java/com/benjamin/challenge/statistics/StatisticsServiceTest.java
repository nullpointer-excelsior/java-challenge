package com.benjamin.challenge.statistics;

import com.benjamin.challenge.products.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class StatisticsServiceTest {
    @Autowired
    private StatisticsService statisticsService;

    @MockBean
    private ProductStatisticsRepository statisticsRepository;

    @Test
    void testUpdateStatisticsNewCategory() {
        String category = "Electronics";
        var newStatistics = ProductStatistics.builder()
                .category(category)
                .productCount(1L)
                .build();

        when(statisticsRepository.findByCategory(category)).thenReturn(Optional.empty());
        when(statisticsRepository.save(any(ProductStatistics.class))).thenReturn(newStatistics);

        statisticsService.updateStatistics(category);

        verify(statisticsRepository, times(1)).findByCategory(category);
        verify(statisticsRepository, times(1)).save(any(ProductStatistics.class));
    }

    @Test
    void testUpdateStatisticsExistingCategory() {
        String category = "Electronics";
        var existingStatistics = ProductStatistics.builder()
                .category(category)
                .productCount(1L)
                .build();

        when(statisticsRepository.findByCategory(category)).thenReturn(Optional.of(existingStatistics));
        when(statisticsRepository.save(any(ProductStatistics.class))).thenReturn(existingStatistics);

        statisticsService.updateStatistics(category);

        verify(statisticsRepository, times(1)).findByCategory(category);
        verify(statisticsRepository, times(1)).save(existingStatistics);

        assertThat(existingStatistics.getProductCount()).isEqualTo(2L);
    }

    @Test
    void testGetAllStatistics() {
        List<ProductStatistics> statisticsList = Arrays.asList(
                ProductStatistics.builder().category("Electronics").productCount(10L).build(),
                ProductStatistics.builder().category("Books").productCount(5L).build()
        );

        when(statisticsRepository.findAll()).thenReturn(statisticsList);

        List<ProductStatistics> result = statisticsService.getAllStatistics();

        assertThat(result.size()).isEqualTo(2);

        verify(statisticsRepository, times(1)).findAll();
    }
}
