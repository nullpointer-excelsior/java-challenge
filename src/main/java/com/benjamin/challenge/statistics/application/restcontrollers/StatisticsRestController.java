package com.benjamin.challenge.statistics.application.restcontrollers;

import com.benjamin.challenge.statistics.application.StatisticsService;
import com.benjamin.challenge.statistics.domain.ProductStatistics;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/statistics")
@SecurityRequirement(name = "basicAuth")
@Tag(name = "Statistics API", description = "Operations related to system statistics")
public class StatisticsRestController {

    @Autowired
    private StatisticsService statisticsService;

    @Operation(summary = "Get all statistics", description = "Retrieves all system statistics")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Statistics successfully retrieved")
    })
    @GetMapping(value = "/categories",produces = "application/json")
    public List<ProductStatistics> getAllStatistics() {
        return statisticsService.getAllStatistics();
    }
}
