package com.benjamin.challenge.statistics;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class StatisticsRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StatisticsService statisticsService;

    @Test
    void testGetAllStatistics() throws Exception {
        var statisticsList = Arrays.asList(
                ProductStatistics.builder().category("Electronics").productCount(10L).build(),
                ProductStatistics.builder().category("Books").productCount(5L).build()
        );

        when(statisticsService.getAllStatistics()).thenReturn(statisticsList);

        mockMvc.perform(get("/statistics/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].category").value("Electronics"))
                .andExpect(jsonPath("$[0].productCount").value(10))
                .andExpect(jsonPath("$[1].category").value("Books"))
                .andExpect(jsonPath("$[1].productCount").value(5));

        verify(statisticsService, times(1)).getAllStatistics();
    }
}
