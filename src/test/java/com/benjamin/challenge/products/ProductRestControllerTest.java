package com.benjamin.challenge.products;

import com.benjamin.challenge.products.application.ProductService;
import com.benjamin.challenge.products.domain.Product;
import com.benjamin.challenge.products.dto.PageableResponse;
import com.benjamin.challenge.products.dto.UpsertProductDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class ProductRestControllerTest {

    static String USERNAME_AUTH = "test";
    static String PASSWORD_AUTH = "test";
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProductService productService;
    @Autowired
    private ObjectMapper objectMapper;

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
        registry.add("spring.security.user.name", () -> USERNAME_AUTH);
        registry.add("spring.security.user.password", () -> PASSWORD_AUTH);
    }

    @Test
    void testCreateProduct() throws Exception {
        UpsertProductDTO dto = new UpsertProductDTO("Product1", 10, 100, "Category1");
        Product product = Product.builder()
                .id(1L)
                .name(dto.name())
                .quantity(dto.quantity())
                .price(dto.price())
                .category(dto.category())
                .build();

        when(productService.create(any(UpsertProductDTO.class))).thenReturn(product);

        mockMvc.perform(post("/products")
                        .with(httpBasic(USERNAME_AUTH, PASSWORD_AUTH))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.name").value(dto.name()))
                .andExpect(jsonPath("$.quantity").value(dto.quantity()))
                .andExpect(jsonPath("$.price").value(dto.price()))
                .andExpect(jsonPath("$.category").value(dto.category()));

    }

    @Test
    void testCreateProductValidation() throws Exception {
        mockMvc.perform(post("/products")
                        .with(httpBasic(USERNAME_AUTH, PASSWORD_AUTH))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\",\"quantity\":-1,\"price\":-1,\"category\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").value("Name is required"))
                .andExpect(jsonPath("$.quantity").value("Quantity must be positive"))
                .andExpect(jsonPath("$.price").value("Price must be positive"))
                .andExpect(jsonPath("$.category").value("Category is required"));
    }


    @Test
    void testUpdateProduct() throws Exception {
        Long id = 1L;
        UpsertProductDTO dto = new UpsertProductDTO("UpdatedProduct", 20, 200, "UpdatedCategory");
        Product product = new Product(id, dto.name(), dto.quantity(), dto.price(), dto.category());

        when(productService.update(eq(id), any(UpsertProductDTO.class))).thenReturn(product);

        mockMvc.perform(put("/products/{id}", id)
                        .with(httpBasic(USERNAME_AUTH, PASSWORD_AUTH))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"UpdatedProduct\",\"quantity\":20,\"price\":200,\"category\":\"UpdatedCategory\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(dto.name()))
                .andExpect(jsonPath("$.quantity").value(dto.quantity()))
                .andExpect(jsonPath("$.price").value(dto.price()))
                .andExpect(jsonPath("$.category").value(dto.category()));
    }

    @Test
    void testUpdateProductValidation() throws Exception {
        Long id = 1L;

        mockMvc.perform(put("/products/{id}", id)
                        .with(httpBasic(USERNAME_AUTH, PASSWORD_AUTH))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\",\"quantity\":-1,\"price\":-1,\"category\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").value("Name is required"))
                .andExpect(jsonPath("$.quantity").value("Quantity must be positive"))
                .andExpect(jsonPath("$.price").value("Price must be positive"))
                .andExpect(jsonPath("$.category").value("Category is required"));
    }


    @Test
    void testDeleteProduct() throws Exception {
        Long id = 1L;
        doNothing().when(productService).delete(id);

        mockMvc.perform(delete("/products/{id}", id).with(httpBasic(USERNAME_AUTH, PASSWORD_AUTH)))
                .andExpect(status().isOk());

        verify(productService, times(1)).delete(id);
    }

    @Test
    void testFindAll() throws Exception {
        List<Product> products = Arrays.asList(new Product(1L, "Product1", 10, 100, "Category1"),
                new Product(2L, "Product2", 20, 200, "Category2"));
        PageableResponse<Product> response = new PageableResponse<>(2L, 0, 10, products);

        when(productService.findAll(any(Pageable.class))).thenReturn(response);

        mockMvc.perform(get("/products")
                        .with(httpBasic(USERNAME_AUTH, PASSWORD_AUTH)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElement").value(response.totalElement()))
                .andExpect(jsonPath("$.page").value(response.page()))
                .andExpect(jsonPath("$.pageSize").value(response.pageSize()))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void testFindAllValidation() throws Exception {
        mockMvc.perform(get("/products")
                        .with(httpBasic(USERNAME_AUTH, PASSWORD_AUTH))
                        .param("page", "-1")
                        .param("size", "101"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.page").value("must be greater than or equal to 0"))
                .andExpect(jsonPath("$.size").value("must be less than or equal to 100"));
    }

    @Test
    void testFindByContainsNameValidation() throws Exception {
        mockMvc.perform(get("/products/search")
                        .with(httpBasic(USERNAME_AUTH, PASSWORD_AUTH))
                        .param("name", "")
                        .param("page", "-1")
                        .param("size", "101"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").value("must not be blank"))
                .andExpect(jsonPath("$.page").value("must be greater than or equal to 0"))
                .andExpect(jsonPath("$.size").value("must be less than or equal to 100"));
    }

    @Test
    void testFindByContainsName() throws Exception {
        List<Product> products = Arrays.asList(new Product(1L, "Product1", 10, 100, "Category1"),
                new Product(2L, "Product2", 20, 200, "Category2"));
        PageableResponse<Product> response = new PageableResponse<>(2L, 0, 10, products);

        when(productService.findByContainsName(eq("Product"), any(Pageable.class))).thenReturn(response);

        mockMvc.perform(get("/products/search").param("name", "Product").with(httpBasic(USERNAME_AUTH, PASSWORD_AUTH)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElement").value(response.totalElement()))
                .andExpect(jsonPath("$.page").value(response.page()))
                .andExpect(jsonPath("$.pageSize").value(response.pageSize()))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void testFindById() throws Exception {
        Long id = 1L;
        Product product = new Product(id, "Product1", 10, 100, "Category1");

        when(productService.findById(id)).thenReturn(product);

        mockMvc.perform(get("/products/{id}", id).with(httpBasic(USERNAME_AUTH, PASSWORD_AUTH)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.name").value(product.getName()))
                .andExpect(jsonPath("$.quantity").value(product.getQuantity()))
                .andExpect(jsonPath("$.price").value(product.getPrice()))
                .andExpect(jsonPath("$.category").value(product.getCategory()));
    }

}
