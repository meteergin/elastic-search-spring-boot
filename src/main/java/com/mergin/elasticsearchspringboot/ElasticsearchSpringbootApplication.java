package com.mergin.elasticsearchspringboot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mergin.elasticsearchspringboot.model.BaseModel;
import com.mergin.elasticsearchspringboot.model.Country;
import com.mergin.elasticsearchspringboot.model.CountryList;
import com.mergin.elasticsearchspringboot.service.ESService;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
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
                log.info(String.valueOf(country));
              } catch (IOException e) {
                throw new RuntimeException(e);
              }
            });


//      Country country = new Country();
//      country.setName("0");
//
//      esService.fetchModelsWithShouldQuery(country).forEach(model -> System.out.println(model));
  }
}
