package com.nbrown.techtask.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties (prefix = "results")
public class SourceDataProperties {
  private String sourceUrl;
  private String driverTableClass;
  private String driverNameElementClass;
  private String driverPointsElementClass;
  private String teamTableClass;
  private String teamNameElementClass;
  private String teamPointsElementClass;
  private int teamStandingsCount;
  private int driverStandingsCount;
}
