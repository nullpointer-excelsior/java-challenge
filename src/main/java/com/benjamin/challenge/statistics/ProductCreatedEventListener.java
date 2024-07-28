package com.benjamin.challenge.statistics;

import com.benjamin.challenge.products.ProductCreatedEvent;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class ProductCreatedEventListener {

    @EventListener
    public void handleProductCreatedEvent(ProductCreatedEvent ev) {
        log.info("Event: " + ev.getPayload());
    }
}
