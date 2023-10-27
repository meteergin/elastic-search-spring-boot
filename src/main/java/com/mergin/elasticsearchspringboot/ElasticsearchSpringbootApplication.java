package com.mergin.elasticsearchspringboot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mergin.elasticsearchspringboot.model.CountryList;
import com.mergin.elasticsearchspringboot.service.ESService;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class ElasticsearchSpringbootApplication implements CommandLineRunner {

  private final ESService esService;
  private final ObjectMapper objectMapper;

  @Value("${elastic-search.index}")
  private String indexName;

  public static void main(String[] args) {
    SpringApplication.run(ElasticsearchSpringbootApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {

    File file = new File("src/main/resources/countries.json");

    objectMapper.registerModule(new JavaTimeModule());
    CountryList countryList = objectMapper.readValue(file, CountryList.class);
    countryList
        .getCountries()
        .forEach(
            country -> {
              country.setIndexName(indexName);
              country.setCreatedAt(new Date());
              try {
                esService.insertModel(country);
                System.out.println(country);
              } catch (IOException e) {
                throw new RuntimeException(e);
              }
            });

    //      Faker faker = new Faker();
    //
    //    IntStream.range(0, 1000)
    //        .forEach(
    //            value -> {
    //                String firstName = faker.name().firstName();
    //                String lastName = faker.name().lastName();
    //                int size = faker.number().randomDigit();
    //                String email = faker.internet().emailAddress();
    //                String id = faker.internet().uuid();
    //
    //                Employee m =
    //                  Employee.builder()
    //                      .id(id)
    //                      .email(email)
    //                      .gender("M")
    //                      .firstName(firstName)
    //                      .lastName(lastName)
    //                      .size(size)
    //                      .build();
    //
    //              try {
    //                esService.insertModel(m);
    //              } catch (IOException e) {
    //                throw new RuntimeException(e);
    //              }
    //            });
  }
}
