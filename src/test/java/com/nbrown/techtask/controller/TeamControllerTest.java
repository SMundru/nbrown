package com.nbrown.techtask.controller;

import com.nbrown.techtask.dto.Team;
import com.nbrown.techtask.service.TeamService;
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
public class TeamControllerTest {

  @InjectMocks
  private TeamController onTest;

  @Mock
  private TeamService service;

  @Test
  public void test_getDriverStandings() {
    List<Team> teams = new ArrayList<>();
    teams.add(new Team("Avengers", 9999999));

    Mockito.when(service.getTeamStandings()).thenReturn(teams);

    final ResponseEntity<List<Team>> teamStandings = onTest.getTeamStandings();

    Mockito.verify(service).getTeamStandings();
    Assert.assertEquals(teams, teamStandings.getBody());
  }
}