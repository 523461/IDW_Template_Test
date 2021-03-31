package com.zdlc.idw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.zdlc.idw.property.FileStorageProperties;

@SpringBootApplication
@EnableConfigurationProperties({
	FileStorageProperties.class
})
public class IDWApplication {
	public static void main(String[] args) {
		SpringApplication.run(IDWApplication.class, args);
	}
}
