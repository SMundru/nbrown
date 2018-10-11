package com.nbrown.techtask.service;

import com.nbrown.techtask.config.SourceDataProperties;
import com.nbrown.techtask.dto.Driver;
import com.nbrown.techtask.repository.FormulaOneRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DriverService {

  private FormulaOneRepository repository;
  private SourceDataProperties properties;

  @Autowired
  public DriverService(FormulaOneRepository repository,
      SourceDataProperties properties) {
    this.repository = repository;
    this.properties = properties;
  }

  public List<Driver> getDriverStandings() {
    List<Driver> drivers = new ArrayList<>();
    final List<Element> rows = repository
        .getStandingsData(properties.getSourceUrl(), properties.getDriverTableClass(),
            properties.getDriverNameElementClass(),
            properties.getDriverStandingsCount());
    rows.forEach(row -> drivers.add(extractDriverDataFromRow(row)));
    drivers.sort(Comparator.comparing(Driver::getPoints).reversed());
    return drivers;
  }

  private Driver extractDriverDataFromRow(Element row) {
    return new Driver(row.getElementsByClass(properties.getDriverNameElementClass()).first().text(),
        Integer.valueOf(
            row.getElementsByClass(properties.getDriverPointsElementClass()).first().text()));
  }
}
