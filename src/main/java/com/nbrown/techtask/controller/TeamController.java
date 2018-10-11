package com.nbrown.techtask.controller;

import com.nbrown.techtask.dto.Team;
import com.nbrown.techtask.service.TeamService;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/teams")
public class TeamController {

  private TeamService service;

  public TeamController(TeamService service) {
    this.service = service;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<Team>> getTeamStandings() {
    return ResponseEntity.ok().body(service.getTeamStandings());
  }
}
