package com.nbrown.techtask.controller;

import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nbrown.techtask.config.SourceDataProperties;
import com.nbrown.techtask.dto.Driver;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DriverControllerIntegrationTest {

  private static final String BASE_URL = "http://localhost:";
  private static final String DRIVERS = "/drivers";
  private TestRestTemplate testRestTemplate;

  private MockRestServiceServer mockServer;

  @Autowired
  private SourceDataProperties properties;

  @Autowired
  private RestTemplate restTemplate;

  @LocalServerPort
  private int port;

  private MediaType mediaType;

  @Before
  public void setUp() {
    testRestTemplate = new TestRestTemplate();
    mockServer = MockRestServiceServer.bindTo(restTemplate).build();
    mediaType = new MediaType("text", "html", Charset.forName("UTF-8"));
  }

  @Test
  public void test_getDriverStandings_WhenStandingsFound() throws IOException {
    final String content = new String(
        Files.readAllBytes(Paths.get("src", "test", "resources", "fixture-full.html")));

    mockServer.expect(MockRestRequestMatchers
        .requestTo(properties.getSourceUrl()))
        .andRespond(MockRestResponseCreators
            .withSuccess()
            .contentType(mediaType)
            .body(content));

    final ResponseEntity<List<Driver>> exchange = testRestTemplate
        .exchange(BASE_URL + port + DRIVERS, HttpMethod.GET, null,
            new ParameterizedTypeReference<List<Driver>>() {
            });

    assertEquals(HttpStatus.OK, exchange.getStatusCode());

    List<Driver> expected = Arrays.asList(new ObjectMapper().readValue(
        "[\n"
            + "  {\n"
            + "    \"name\": \"Lewis Hamilton\",\n"
            + "    \"points\": 331\n"
            + "  },\n"
            + "  {\n"
            + "    \"name\": \"Sebastian Vettel\",\n"
            + "    \"points\": 264\n"
            + "  },\n"
            + "  {\n"
            + "    \"name\": \"Valtteri Bottas\",\n"
            + "    \"points\": 207\n"
            + "  },\n"
            + "  {\n"
            + "    \"name\": \"Kimi Räikkönen\",\n"
            + "    \"points\": 196\n"
            + "  },\n"
            + "  {\n"
            + "    \"name\": \"Max Verstappen\",\n"
            + "    \"points\": 173\n"
            + "  },\n"
            + "  {\n"
            + "    \"name\": \"Daniel Ricciardo\",\n"
            + "    \"points\": 146\n"
            + "  },\n"
            + "  {\n"
            + "    \"name\": \"Sergio Pérez\",\n"
            + "    \"points\": 53\n"
            + "  },\n"
            + "  {\n"
            + "    \"name\": \"Kevin Magnussen\",\n"
            + "    \"points\": 53\n"
            + "  },\n"
            + "  {\n"
            + "    \"name\": \"Nico Hülkenberg\",\n"
            + "    \"points\": 53\n"
            + "  },\n"
            + "  {\n"
            + "    \"name\": \"Fernando Alonso\",\n"
            + "    \"points\": 50\n"
            + "  }\n"
            + "]",
        Driver[].class));

    assertEquals(expected, exchange.getBody());
  }

  @Test
  public void test_getDriverStandings_WhenNoStandingsReturned() {
    mockServer.expect(MockRestRequestMatchers.requestTo(properties.getSourceUrl())).andRespond(
        MockRestResponseCreators.withNoContent());

    final ResponseEntity<List<Driver>> exchange = testRestTemplate
        .exchange(BASE_URL + port + DRIVERS, HttpMethod.GET, null,
            new ParameterizedTypeReference<List<Driver>>() {
            });

    assertEquals(HttpStatus.OK, exchange.getStatusCode());

    assertEquals(Collections.emptyList(), exchange.getBody());
  }

  @Test
  public void test_getDriverStandings_WhenStandingsCountIsLessThanDesiredCount()
      throws IOException {
    final String content = new String(
        Files.readAllBytes(Paths.get("src", "test", "resources", "fixture-incomplete.html")));

    mockServer.expect(MockRestRequestMatchers.requestTo(properties.getSourceUrl())).andRespond(
        MockRestResponseCreators.withSuccess().contentType(mediaType).body(content));

    final ResponseEntity<List<Driver>> exchange = testRestTemplate
        .exchange(BASE_URL + port + DRIVERS, HttpMethod.GET, null,
            new ParameterizedTypeReference<List<Driver>>() {
            });

    assertEquals(HttpStatus.OK, exchange.getStatusCode());

    List<Driver> expected = Arrays.asList(new ObjectMapper().readValue(
        "[\n"
            + "  {\n"
            + "    \"name\": \"Lewis Hamilton\",\n"
            + "    \"points\": 331\n"
            + "  },\n"
            + "  {\n"
            + "    \"name\": \"Sebastian Vettel\",\n"
            + "    \"points\": 264\n"
            + "  },\n"
            + "  {\n"
            + "    \"name\": \"Valtteri Bottas\",\n"
            + "    \"points\": 207\n"
            + "  },\n"
            + "  {\n"
            + "    \"name\": \"Kimi Räikkönen\",\n"
            + "    \"points\": 196\n"
            + "  },\n"
            + "  {\n"
            + "    \"name\": \"Max Verstappen\",\n"
            + "    \"points\": 173\n"
            + "  },\n"
            + "  {\n"
            + "    \"name\": \"Daniel Ricciardo\",\n"
            + "    \"points\": 146\n"
            + "  }\n"
            + "]",
        Driver[].class));

    assertEquals(expected, exchange.getBody());
  }
}