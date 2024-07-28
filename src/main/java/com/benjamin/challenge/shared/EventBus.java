package com.benjamin.challenge.shared;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class EventBus {
    private final ApplicationEventPublisher applicationEventPublisher;

    public EventBus(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void publish(final ApplicationEvent event) {
        applicationEventPublisher.publishEvent(event);
    }
}
