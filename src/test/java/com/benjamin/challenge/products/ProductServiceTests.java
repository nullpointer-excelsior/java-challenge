package com.benjamin.challenge.products;

import com.benjamin.challenge.shared.EventBus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class ProductServiceTests {

    @Autowired
    private ProductService productService;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    EventBus eventBus;


    @Test
    void testCreateProduct() {
        UpsertProductDTO dto = new UpsertProductDTO("Product1", 10, 100, "Category1");
        Product product = Product.builder()
                .id(1234L)
                .name(dto.name())
                .quantity(dto.quantity())
                .price(dto.price())
                .category(dto.category())
                .build();

        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product createdProduct = productService.create(dto);

        assertThat(createdProduct.getName()).isEqualTo(dto.name());
        assertThat(createdProduct.getQuantity()).isEqualTo(dto.quantity());
        assertThat(createdProduct.getPrice()).isEqualTo(dto.price());
        assertThat(createdProduct.getCategory()).isEqualTo(dto.category());
        verify(eventBus, times(1)).publish(any());
    }

    @Test
    void testUpdateProduct() {
        Long id = 1L;
        UpsertProductDTO dto = new UpsertProductDTO("UpdatedProduct", 20, 200, "UpdatedCategory");
        Product existingProduct = new Product(id, "Product1", 10, 100, "Category1");

        when(productRepository.findById(id)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(existingProduct);

        Product updatedProduct = productService.update(id, dto);

        assertThat(updatedProduct.getName()).isEqualTo(dto.name());
        assertThat(updatedProduct.getQuantity()).isEqualTo(dto.quantity());
        assertThat(updatedProduct.getPrice()).isEqualTo(dto.price());
        assertThat(updatedProduct.getCategory()).isEqualTo(dto.category());
    }

    @Test
    void testDeleteProduct() {
        Long id = 1L;
        doNothing().when(productRepository).deleteById(id);

        productService.delete(id);

        verify(productRepository, times(1)).deleteById(id);
    }

    @Test
    void testFindAll() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Product> products = Arrays.asList(new Product(1L, "Product1", 10, 100, "Category1"),
                new Product(2L, "Product2", 20, 200, "Category2"));
        Page<Product> page = new PageImpl<>(products, pageable, products.size());

        when(productRepository.findAll(pageable)).thenReturn(page);

        PageableResponse<Product> response = productService.findAll(pageable);

        assertThat(response.totalElement()).isEqualTo(products.size());
        assertThat(response.page()).isEqualTo(pageable.getPageNumber());
        assertThat(response.pageSize()).isEqualTo(pageable.getPageSize());
        assertThat(response.data().size()).isEqualTo(products.size());
    }

    @Test
    void testFindByContainsName() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Product> products = Arrays.asList(new Product(1L, "Product1", 10, 100, "Category1"),
                new Product(2L, "Product2", 20, 200, "Category2"));
        Page<Product> page = new PageImpl<>(products, pageable, products.size());

        when(productRepository.findByNameContainingIgnoreCase("Product", pageable)).thenReturn(page);

        PageableResponse<Product> response = productService.findByContainsName("Product", pageable);

        assertThat(response.totalElement()).isEqualTo(products.size());
        assertThat(response.page()).isEqualTo(pageable.getPageNumber());
        assertThat(response.pageSize()).isEqualTo(pageable.getPageSize());
        assertThat(response.data().size()).isEqualTo(products.size());
    }

    @Test
    void testFindById() {
        Long id = 1L;
        Product product = new Product(id, "Product1", 10, 100, "Category1");

        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        Product foundProduct = productService.findById(id);

        assertThat(foundProduct.getId()).isEqualTo(id);
        assertThat(foundProduct.getName()).isEqualTo(product.getName());
        assertThat(foundProduct.getQuantity()).isEqualTo(product.getQuantity());
        assertThat(foundProduct.getPrice()).isEqualTo(product.getPrice());
        assertThat(foundProduct.getCategory()).isEqualTo(product.getCategory());
    }
}
