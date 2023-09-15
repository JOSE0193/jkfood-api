package com.delivery.jkfood;

import com.delivery.jkfood.domain.repository.CustomJpaRepository;
import com.delivery.jkfood.infrastructure.repository.CustomJpaRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = CustomJpaRepositoryImpl.class)
public class JkfoodApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(JkfoodApiApplication.class, args);
	}

}
