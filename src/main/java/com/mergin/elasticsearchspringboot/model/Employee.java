package com.mergin.elasticsearchspringboot.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class Employee {
  private String id;
  private String firstName;
  private String lastName;
  private String email;
  private String gender;
  private int size;
  private String country;
}
