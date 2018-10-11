package com.nbrown.techtask.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.nbrown.techtask.config.SourceDataProperties;
import com.nbrown.techtask.dto.Driver;
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
public class DriverServiceTest {

  @InjectMocks
  private DriverService onTest;

  @Mock
  private FormulaOneRepository repository;

  @Mock
  private SourceDataProperties properties;

  @Before
  public void setUp() {
    when(properties.getSourceUrl()).thenReturn("someUrl");
    when(properties.getDriverTableClass()).thenReturn("someTableClass");
    when(properties.getDriverNameElementClass()).thenReturn("someNameClass");
    when(properties.getDriverPointsElementClass()).thenReturn("somePointsClass");
    when(properties.getDriverStandingsCount()).thenReturn(10);
  }

  @Test
  public void test_getDriverStandings() {
    List<Element> rows = new ArrayList<>();
    Element mockedParentElement = mock(Element.class);
    Elements childElements = mock(Elements.class);
    Element finalElement = mock(Element.class);
    when(finalElement.text()).thenReturn("SuperMan", "999");
    when(childElements.first()).thenReturn(finalElement, finalElement);
    when(mockedParentElement.getElementsByClass("someNameClass")).thenReturn(childElements);
    when(mockedParentElement.getElementsByClass("somePointsClass")).thenReturn(childElements);

    rows.add(mockedParentElement);

    when(repository.getStandingsData("someUrl", "someTableClass", "someNameClass", 10))
        .thenReturn(rows);
    final List<Driver> driverStandings = onTest.getDriverStandings();
    Mockito.verify(repository).getStandingsData("someUrl", "someTableClass", "someNameClass", 10);
    Assert.assertEquals("SuperMan", driverStandings.get(0).getName());
    Assert.assertEquals(999, driverStandings.get(0).getPoints());
  }
}