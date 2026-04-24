package com.souvanik.eventplatform.event_ingestion_service;

import com.souvanik.eventplatform.event_ingestion_service.config.property.SqsProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@ConfigurationPropertiesScan
public class EventIngestionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventIngestionServiceApplication.class, args);
	}

}
