package com.nbrown.techtask.controller;

import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nbrown.techtask.config.SourceDataProperties;
import com.nbrown.techtask.dto.Driver;
import com.nbrown.techtask.dto.Team;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
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
public class TeamControllerIntegrationTest {

  private static final String BASE_URL = "http://localhost:";
  private static final String TEAMS = "/teams";
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

    final ResponseEntity<List<Team>> exchange = testRestTemplate
        .exchange(BASE_URL + port + TEAMS, HttpMethod.GET, null,
            new ParameterizedTypeReference<List<Team>>() {
            });

    assertEquals(HttpStatus.OK, exchange.getStatusCode());

    List<Team> expected = Arrays.asList(new ObjectMapper().readValue(
        "[\n"
            + "  {\n"
            + "    \"name\": \"Mercedes\",\n"
            + "    \"points\": 538\n"
            + "  },\n"
            + "  {\n"
            + "    \"name\": \"Ferrari\",\n"
            + "    \"points\": 460\n"
            + "  },\n"
            + "  {\n"
            + "    \"name\": \"Red Bull\",\n"
            + "    \"points\": 319\n"
            + "  },\n"
            + "  {\n"
            + "    \"name\": \"Renault\",\n"
            + "    \"points\": 92\n"
            + "  },\n"
            + "  {\n"
            + "    \"name\": \"Haas F1 Team\",\n"
            + "    \"points\": 84\n"
            + "  }\n"
            + "]",
        Team[].class));

    assertEquals(expected, exchange.getBody());
  }

}
