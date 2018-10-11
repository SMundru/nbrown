package com.nbrown.techtask.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.nbrown.techtask.config.SourceDataProperties;
import com.nbrown.techtask.dto.Team;
import com.nbrown.techtask.repository.FormulaOneRepository;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TeamServiceTest {

  @InjectMocks
  private TeamService onTest;

  @Mock
  private FormulaOneRepository repository;

  @Mock
  private SourceDataProperties properties;

  @Before
  public void setUp() {
    when(properties.getSourceUrl()).thenReturn("someUrl");
    when(properties.getTeamTableClass()).thenReturn("someTableClass");
    when(properties.getTeamNameElementClass()).thenReturn("someNameClass");
    when(properties.getTeamPointsElementClass()).thenReturn("somePointsClass");
    when(properties.getTeamStandingsCount()).thenReturn(10);
  }

  @Test
  public void test_getTeamStandings() {
    List<Element> rows = new ArrayList<>();
    Element parentElement = mock(Element.class);
    Elements childElements = mock(Elements.class);
    Element finalElement = mock(Element.class);
    when(finalElement.text()).thenReturn("Avengers", "999999");
    when(childElements.first()).thenReturn(finalElement, finalElement);
    when(parentElement.getElementsByClass("someNameClass")).thenReturn(childElements);
    when(parentElement.getElementsByClass("somePointsClass")).thenReturn(childElements);

    rows.add(parentElement);

    when(repository.getStandingsData("someUrl", "someTableClass", "someNameClass", 10))
        .thenReturn(rows);
    final List<Team> teamStandings = onTest.getTeamStandings();
    Mockito.verify(repository).getStandingsData("someUrl", "someTableClass", "someNameClass", 10);
    Assert.assertEquals("Avengers", teamStandings.get(0).getName());
    Assert.assertEquals(999999, teamStandings.get(0).getPoints());
  }
}