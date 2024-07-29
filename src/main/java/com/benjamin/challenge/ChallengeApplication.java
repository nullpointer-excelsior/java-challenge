package com.benjamin.challenge;

import com.benjamin.challenge.products.application.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class ChallengeApplication {

	@Autowired
	ProductService products;

	public static void main(String[] args) {
		SpringApplication.run(ChallengeApplication.class, args);
	}


	@EventListener(ApplicationReadyEvent.class)
	public void onApplicationReady() {
		products.insertSampleProducts();
	}
}
