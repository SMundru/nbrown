package com.nbrown.techtask.controller;

import com.nbrown.techtask.dto.Driver;
import com.nbrown.techtask.service.DriverService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/drivers")
public class DriverController {

  private DriverService service;

  @Autowired
  public DriverController(DriverService service) {
    this.service = service;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<List<Driver>> getDriverStandings() {
    return ResponseEntity.ok().body(service.getDriverStandings());
  }

}
