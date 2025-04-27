package com.Gyro.back_end_gyro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling

public class BackEndGyroApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackEndGyroApplication.class, args);
	}


}
