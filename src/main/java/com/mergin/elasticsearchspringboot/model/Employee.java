package com.mergin.elasticsearchspringboot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Employee extends BaseModel {
  private String firstName;
  private String lastName;
  private String email;
  private String gender;
  private int size;
  private String country;
}
