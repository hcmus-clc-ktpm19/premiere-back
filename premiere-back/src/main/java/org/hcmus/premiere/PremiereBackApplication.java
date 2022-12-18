package org.hcmus.premiere;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PremiereBackApplication {

  public static void main(String[] args) {
    SpringApplication.run(PremiereBackApplication.class, args);
  }

}
