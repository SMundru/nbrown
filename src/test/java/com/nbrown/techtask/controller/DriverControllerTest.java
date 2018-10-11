package com.nbrown.techtask.controller;

import com.nbrown.techtask.dto.Driver;
import com.nbrown.techtask.service.DriverService;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

@RunWith(MockitoJUnitRunner.class)
public class DriverControllerTest {

  @InjectMocks
  private DriverController onTest;

  @Mock
  private DriverService service;

  @Test
  public void test_getDriverStandings() {
    List<Driver> drivers = new ArrayList<>();
    drivers.add(new Driver("Superman", 999));

    Mockito.when(service.getDriverStandings()).thenReturn(drivers);

    final ResponseEntity<List<Driver>> driverStandings = onTest.getDriverStandings();

    Mockito.verify(service).getDriverStandings();
    Assert.assertEquals(drivers, driverStandings.getBody());
  }
}