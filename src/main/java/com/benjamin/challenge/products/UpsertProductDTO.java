package com.benjamin.challenge.products;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Schema(description = "Data transfer object for creating a product")
public record UpsertProductDTO(
        @Schema(description = "Product Name", example = "Guitar")
        @NotBlank(message = "Name is required")
        String name,

        @Schema(description = "Quantity of the product", example = "10")
        @Positive(message = "Quantity must be positive")
        Integer quantity,

        @Schema(description = "Price of the product in cents", example = "19999")
        @Positive(message = "Price must be positive")
        Integer price,

        @NotBlank(message = "Category is required")
        @Schema(description = "Category of the product", example = "Instruments")
        String category
) {
}