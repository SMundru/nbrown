package com.nbrown.techtask.service;

import com.nbrown.techtask.config.SourceDataProperties;
import com.nbrown.techtask.dto.Team;
import com.nbrown.techtask.repository.FormulaOneRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeamService {

  private FormulaOneRepository repository;
  private SourceDataProperties properties;

  @Autowired
  public TeamService(FormulaOneRepository repository,
      SourceDataProperties properties) {
    this.repository = repository;
    this.properties = properties;
  }

  public List<Team> getTeamStandings() {
    List<Team> teams = new ArrayList<>();
    final List<Element> rows = repository
        .getStandingsData(properties.getSourceUrl(), properties.getTeamTableClass(),
            properties.getTeamNameElementClass(),
            properties.getTeamStandingsCount());
    rows.forEach(row -> teams.add(extractTeamDataFromRow(row)));
    teams.sort(Comparator.comparing(Team::getPoints).reversed());
    return teams;
  }

  private Team extractTeamDataFromRow(Element row) {
    return new Team(row.getElementsByClass(properties.getTeamNameElementClass()).first().text(),
        Integer
            .valueOf(
                row.getElementsByClass(properties.getTeamPointsElementClass()).first().text()));
  }

}
