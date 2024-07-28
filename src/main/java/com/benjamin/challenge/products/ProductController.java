package com.benjamin.challenge.products;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@RestController
@Validated
@RequestMapping("/products")
@Tag(name = "Products API", description = "Operations related to products")
public class ProductController {

    @Autowired
    private ProductService service;

    @Operation(summary = "Create a product", description = "Creates a new product and returns the created product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product successfully created"),
            //@ApiResponse(responseCode = "400", description = "Invalid input", content = { @Content(schema = @Schema(implementation = GenericResponse.class), mediaType = "application/json") }),
            //@ApiResponse(responseCode = "500", description = "Internal server error" , content = { @Content(schema = @Schema(implementation = GenericResponse.class), mediaType = "application/json") })
    })
    @PostMapping(produces = "application/json")
    public Product create(@RequestBody @Valid UpsertProductDTO dto) {
        return this.service.create(dto);
    }

    @Operation(summary = "Update a product", description = "Updates an existing product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product successfully updated"),
    })
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody @Valid UpsertProductDTO dto) {
        return this.service.update(id, dto);
    }

    @Operation(summary = "Delete a product", description = "Deletes a product by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product successfully deleted")
    })
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        this.service.delete(id);
    }

    @Operation(summary = "Find products", description = "Find products by pageable")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products successfully retrieved")
    })
    @GetMapping
    public PageableResponse<Product> findAll(@RequestParam(defaultValue = "0") @Min(0) int page, @RequestParam(defaultValue = "3") @Min(0) @Max(100) int size) {
        var pageable = PageRequest.of(page, size);
        return service.findAll(pageable);
    }

    @Operation(summary = "Find products by name", description = "Find products by name and pageable")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products successfully retrieved")
    })
    @GetMapping("/search")
    public PageableResponse<Product> findBySearch(@RequestParam @NotBlank String name, @RequestParam(defaultValue = "0") @Min(0) int page, @RequestParam(defaultValue = "3") @Min(0) @Max(100) int size) {
        var pageable = PageRequest.of(page, size);
        return service.findByContainsName(name, pageable);
    }
    @Operation(summary = "Find product by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product successfully retrieved")
    })
    @GetMapping("/{id}")
    public Product findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public Map<String, String> handleValidationExceptions(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(error -> {
            String errorMessage = error.getMessage();
            errors.put(error.getPropertyPath().toString().split("\\.")[1], errorMessage);
        });

        return errors;
    }

    record GenericResponse(String message, Integer status) {
    }

}
