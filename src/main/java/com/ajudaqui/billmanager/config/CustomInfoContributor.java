package com.ajudaqui.billmanager.config;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

@Component
public class CustomInfoContributor implements InfoContributor {

  @Value("${info.app.environment}")
  private String environment;

  @Value("${info.app.description}")
  private String description;

  @Value("${info.app.name}")
  private String applicationName;

  @Value("${info.app.version}")
  private String version;

  @Value("${info.app.contact}")
  private String contact;
  private String buildTime = LocalDateTime.now().toString(); 

  @Override
  public void contribute(Info.Builder builder) {
    Map<String, Object> appDetails = new HashMap<>();
    appDetails.put("name", applicationName);
    appDetails.put("description", description);
    appDetails.put("version", version);

    Map<String, Object> developerDetails = new HashMap<>();
    developerDetails.put("name", "Gustavo");
    developerDetails.put("contact", contact);

    builder.withDetail("app", appDetails)
        .withDetail("developer", developerDetails)
        .withDetail("environment", environment)
        .withDetail("buildTime", buildTime);
  }
}
