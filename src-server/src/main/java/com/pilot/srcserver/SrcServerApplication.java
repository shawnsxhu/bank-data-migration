package com.pilot.srcserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SrcServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SrcServerApplication.class, args);
	}

}
