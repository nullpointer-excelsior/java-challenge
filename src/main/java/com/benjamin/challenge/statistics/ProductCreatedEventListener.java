package com.benjamin.challenge.statistics;

import com.benjamin.challenge.products.ProductCreatedEvent;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class ProductCreatedEventListener {

    @Autowired
    private StatisticsService statistics;

    @EventListener
    public void handleProductCreatedEvent(ProductCreatedEvent ev) {
        log.info("Event: " + ev.getPayload());
        var category = ev.getPayload().getCategory();
        this.statistics.updateStatistics(category);
    }
}
