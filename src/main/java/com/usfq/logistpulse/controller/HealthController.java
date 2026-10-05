package com.usfq.logistpulse.controller;

import javax.sql.DataSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Map;

@RestController
public class HealthController {
  private final DataSource dataSource;
  public HealthController(DataSource dataSource) { this.dataSource = dataSource; }

  @GetMapping("/health")
  public ResponseEntity<Map<String, String>> health() {
    try (Connection c = dataSource.getConnection(); Statement s = c.createStatement()) {
      s.execute("SELECT 1");
      return ResponseEntity.ok(Map.of("status", "UP", "service", "LOGISTPULSE", "database", "UP"));
    } catch (Exception ex) {
      return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
        .body(Map.of("status", "DOWN", "service", "LOGISTPULSE", "database", "DOWN"));
    }
  }
}
