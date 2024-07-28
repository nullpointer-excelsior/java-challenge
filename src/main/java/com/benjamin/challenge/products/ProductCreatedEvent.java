package com.benjamin.challenge.products;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
@ToString
public class ProductCreatedEvent extends ApplicationEvent {

    private Product payload;

    public ProductCreatedEvent(Object source, Product payload) {
        super(source);
        this.payload = payload;
    }
}
