package com.benjamin.challenge.statistics;

import com.benjamin.challenge.products.Product;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@Testcontainers
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class StatisticsServiceTest {
    @Autowired
    private StatisticsService statisticsService;

    @MockBean
    private ProductStatisticsRepository statisticsRepository;

    @Container
    static PostgreSQLContainer databaseContainer = new PostgreSQLContainer("postgres:latest")
            .withDatabaseName("integration-tests-db")
            .withUsername("sa")
            .withPassword("sa");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url",() -> databaseContainer.getJdbcUrl());
        registry.add("spring.datasource.username", () -> databaseContainer.getUsername());
        registry.add("spring.datasource.password", () -> databaseContainer.getPassword());
    }


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
