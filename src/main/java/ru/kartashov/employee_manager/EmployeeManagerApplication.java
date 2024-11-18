package ru.kartashov.employee_manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class EmployeeManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeeManagerApplication.class, args);
	}

}
