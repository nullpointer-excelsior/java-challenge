package com.benjamin.challenge.products;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    public Product create(UpsertProductDTO p) {
        var product = Product.builder()
                .name(p.name())
                .quantity(p.quantity())
                .price(p.price())
                .category(p.category())
                .build();
        return this.repository.save(product);
    }

    public Product update(Long id, UpsertProductDTO p) {
        var entity = this.repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity not found"));
        entity.setCategory(p.category());
        entity.setName(p.name());
        entity.setPrice(p.price());
        entity.setQuantity(p.quantity());
        this.repository.save(entity);
        return entity;
    }

    public void delete(Long id) {
        this.repository.deleteById(id);
    }

    public PageableResponse<Product> findAll(Pageable pageable) {
        var page = this.repository.findAll(pageable);
        return new PageableResponse<>(page.getTotalElements(), page.getNumber(), page.getSize(), page.stream().toList());
    }

    public PageableResponse<Product> findByContainsName(String name, Pageable pageable) {
        var page = this.repository.findByNameContainingIgnoreCase(name, pageable);
        return new PageableResponse<>(page.getTotalElements(), page.getNumber(), page.getSize(), page.stream().toList());
    }

    public Product findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity not found"));
    }

    public void insertSampleProducts() {
        List<Product> products = Arrays.asList(
                Product.builder().name("Product 1").quantity(10).price(100).category("Category A").build(),
                Product.builder().name("Product 2").quantity(20).price(200).category("Category B").build(),
                Product.builder().name("Product 3").quantity(30).price(300).category("Category C").build(),
                Product.builder().name("Product 4").quantity(40).price(400).category("Category D").build(),
                Product.builder().name("Product 5").quantity(50).price(500).category("Category E").build(),
                Product.builder().name("Product 6").quantity(60).price(600).category("Category F").build(),
                Product.builder().name("Product 7").quantity(70).price(700).category("Category G").build(),
                Product.builder().name("Product 8").quantity(80).price(800).category("Category H").build(),
                Product.builder().name("Product 9").quantity(90).price(900).category("Category I").build(),
                Product.builder().name("Product 10").quantity(100).price(1000).category("Category J").build(),
                Product.builder().name("Product 11").quantity(10).price(100).category("Category A").build(),
                Product.builder().name("Product 12").quantity(20).price(200).category("Category B").build(),
                Product.builder().name("Product 13").quantity(30).price(300).category("Category C").build(),
                Product.builder().name("Product 14").quantity(40).price(400).category("Category D").build(),
                Product.builder().name("Product 15").quantity(50).price(500).category("Category E").build(),
                Product.builder().name("Product 16").quantity(60).price(600).category("Category F").build(),
                Product.builder().name("Product 17").quantity(70).price(700).category("Category G").build(),
                Product.builder().name("Product 18").quantity(80).price(800).category("Category H").build(),
                Product.builder().name("Product 19").quantity(90).price(900).category("Category I").build(),
                Product.builder().name("Product 20").quantity(100).price(1000).category("Category J").build(),
                Product.builder().name("Product 21").quantity(100).price(1000).category("Category J").build()
        );

        repository.saveAll(products);
    }

}
