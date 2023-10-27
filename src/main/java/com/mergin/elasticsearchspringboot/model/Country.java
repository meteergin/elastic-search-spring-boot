package com.mergin.elasticsearchspringboot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Country extends BaseModel {
  @JsonProperty("Country")
  private String name;

  @JsonProperty("Region")
  private String region;

  @JsonProperty("Population")
  private String population;

  @JsonProperty("Area (sq. mi.)")
  private String area;

  @JsonProperty("Pop. Density (per sq. mi.)")
  private String pop;

  @JsonProperty("Coastline (coast/area ratio)")
  private String coastline;

  @JsonProperty("Net migration")
  private String netMigration;

  @JsonProperty("Infant mortality (per 1000 births)")
  private String infantMortality;

  @JsonProperty("GDP ($ per capita)")
  private String gdp;

  @JsonProperty("Literacy (%)")
  private String literacy;

  @JsonProperty("Phones (per 1000)")
  private String phones;

  @JsonProperty("Arable (%)")
  private String arable;

  @JsonProperty("Crops (%)")
  private String crops;

  @JsonProperty("Other (%)")
  private String other;

  @JsonProperty("Climate")
  private String climate;

  @JsonProperty("Birthrate")
  private String birthrate;

  @JsonProperty("Deathrate")
  private String deathrate;

  @JsonProperty("Agriculture")
  private String agriculture;

  @JsonProperty("Industry")
  private String industry;

  @JsonProperty("Service")
  private String service;
}
