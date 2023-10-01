package com.mergin.elasticsearchspringboot;

import com.github.javafaker.Faker;
import com.mergin.elasticsearchspringboot.model.Employee;
import com.mergin.elasticsearchspringboot.service.ESService;
import java.io.IOException;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class ElasticsearchSpringbootApplication implements CommandLineRunner {

  private final ESService esService;

  public static void main(String[] args) {
    SpringApplication.run(ElasticsearchSpringbootApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {

      Faker faker = new Faker();

    IntStream.range(0, 1000)
        .forEach(
            value -> {
                String firstName = faker.name().firstName();
                String lastName = faker.name().lastName();
                int size = faker.number().randomDigit();
                String email = faker.internet().emailAddress();
                String id = faker.internet().uuid();

                Employee m =
                  Employee.builder()
                      .id(id)
                      .email(email)
                      .gender("M")
                      .firstName(firstName)
                      .lastName(lastName)
                      .size(size)
                      .build();

              try {
                esService.insertEmployee(m);
              } catch (IOException e) {
                throw new RuntimeException(e);
              }
            });
  }
}
