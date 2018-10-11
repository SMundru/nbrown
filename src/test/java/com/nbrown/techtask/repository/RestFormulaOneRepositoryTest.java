package com.nbrown.techtask.repository;

import java.util.List;
import org.jsoup.nodes.Element;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@RunWith(MockitoJUnitRunner.class)
public class RestFormulaOneRepositoryTest {

  @InjectMocks
  private RestFormulaOneRepository onTest;

  @Mock
  private RestTemplate restTemplate;

  @Before
  public void setUp() {
  }

  @Test
  public void test_getStandingData() {
    Mockito.when(restTemplate.exchange("someUrl", HttpMethod.GET, null, String.class)).thenReturn(
        ResponseEntity
            .ok("<html>\n"
                + "<table class=\"someTableClass\">\n"
                + "    <tbody>\n"
                + "        <tr>\n"
                + "            <td class=\"msr_pos\">1</td>\n"
                + "            <td class=\"msr_driver\"><img alt=\"United Kingdom\" title=\"United Kingdom\" src=\"https://www.f1-fansite.com/wp-content/plugins/motor-sport-results/images/flags/gb.png\"> <a href=\"https://www.f1-fansite.com/f1-drivers/lewis-hamilton/\" title=\"Lewis Hamilton\">Lewis Hamilton</a></td>\n"
                + "            <td class=\"msr_position msr_pole msr_points\" title=\"18 Pts\">2</td>\n"
                + "            <td class=\"msr_position msr_points\" title=\"15 Pts\">3</td>\n"
                + "            <td class=\"msr_position msr_points\" title=\"12 Pts\">4</td>\n"
                + "            <td class=\"msr_position msr_points\" title=\"25 Pts\">1</td>\n"
                + "            <td class=\"msr_position msr_pole msr_points\" title=\"25 Pts\">1</td>\n"
                + "            <td class=\"msr_position msr_points\" title=\"15 Pts\">3</td>\n"
                + "            <td class=\"msr_position msr_points\" title=\"10 Pts\">5</td>\n"
                + "            <td class=\"msr_position msr_pole msr_points\" title=\"25 Pts\">1</td>\n"
                + "            <td class=\"msr_position msr_retired\" title=\"Retired - Fuel pressure\">Ret</td>\n"
                + "            <td class=\"msr_position msr_pole msr_points\" title=\"18 Pts\">2</td>\n"
                + "            <td class=\"msr_position msr_points\" title=\"25 Pts\">1</td>\n"
                + "            <td class=\"msr_position msr_pole msr_points\" title=\"25 Pts\">1</td>\n"
                + "            <td class=\"msr_position msr_pole msr_points\" title=\"18 Pts\">2</td>\n"
                + "            <td class=\"msr_position msr_points\" title=\"25 Pts\">1</td>\n"
                + "            <td class=\"msr_position msr_pole msr_points\" title=\"25 Pts\">1</td>\n"
                + "            <td class=\"msr_position msr_points\" title=\"25 Pts\">1</td>\n"
                + "            <td class=\"msr_position msr_pole msr_points\" title=\"25 Pts\">1</td>\n"
                + "            <td class=\"msr_position\">&nbsp;</td>\n"
                + "            <td class=\"msr_position\">&nbsp;</td>\n"
                + "            <td class=\"msr_position\">&nbsp;</td>\n"
                + "            <td class=\"msr_position\">&nbsp;</td>\n"
                + "            <td class=\"msr_total\">331</td>\n"
                + "        </tr>\n"
                + "</table>\n"
                + "</tbody>\n"
                + "</html>"));
    final List<Element> standingData = onTest
        .getStandingsData("someUrl", "someTableClass", "msr_driver", 10);
    Assert.assertFalse(standingData.isEmpty());
    Assert.assertEquals("Lewis Hamilton",
        standingData.get(0).getElementsByClass("msr_driver").first().text());
    Assert.assertEquals("331", standingData.get(0).getElementsByClass("msr_total").first().text());
  }

  @Test
  public void test_getStandingsData_whenRestCallFailsGracefully() {
    Mockito.when(restTemplate.exchange("someUrl", HttpMethod.GET, null, String.class))
        .thenReturn(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());

    final List<Element> standingData = onTest
        .getStandingsData("someUrl", "someTableClass", "msr_driver", 10);

    Assert.assertTrue(standingData.isEmpty());
  }

  @Test
  public void test_getStandingsData_whenRestCallFailsWithException() {
    Mockito.when(restTemplate.exchange("someUrl", HttpMethod.GET, null, String.class))
        .thenThrow(new HttpServerErrorException(HttpStatus.BAD_GATEWAY));

    final List<Element> standingData = onTest
        .getStandingsData("someUrl", "someTableClass", "msr_driver", 10);

    Assert.assertTrue(standingData.isEmpty());
  }
}