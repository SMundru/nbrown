package com.nbrown.techtask.repository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Repository
public class RestFormulaOneRepository implements FormulaOneRepository {

  private static final String TABLE_ROW = "tr";
  private static final String TABLE_BODY = "tbody";
  private RestTemplate restTemplate;

  @Autowired
  public RestFormulaOneRepository(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  @Override
  public List<Element> getStandingsData(String sourceUrl, String tableClass, String nameClass,
      int count) {

    ResponseEntity<String> exchange = getDataFromSource(sourceUrl);

    if (exchange != null && exchange.getStatusCode().is2xxSuccessful()
        && exchange.getBody() != null) {
      return filterTableRows(Jsoup.parse(exchange.getBody()), tableClass, nameClass, count);
    }
    return Collections.emptyList();
  }

  private ResponseEntity<String> getDataFromSource(String sourceUrl) {
    ResponseEntity<String> exchange = null;
    try {
      exchange = restTemplate
          .exchange(sourceUrl, HttpMethod.GET, null, String.class);
    } catch (Exception e) {
      log.error("Failed to fetch standings from source : {}", sourceUrl);
      e.printStackTrace();
    }
    return exchange;
  }

  private List<Element> filterTableRows(Document document, String clazz, String nameClass,
      int count) {
    if (document != null) {
      final List<Element> rows = document.body()
          .getElementsByClass(clazz).select(TABLE_BODY).select(TABLE_ROW).stream()
          .filter(element -> !element.getElementsByClass(nameClass).isEmpty())
          .collect(Collectors.toList());
      return rows.size() >= count ? rows.subList(0, count) : rows;
    }
    return Collections.emptyList();
  }
}
