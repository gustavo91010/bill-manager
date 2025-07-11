package com.ajudaqui.billmanager.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

@Configuration
public class SqsConfig {

  @Value("${aws.region}")
  private String region;
  @Value("${aws.profile}")
  private String profile;

  @Bean
  public SqsClient sqsClient() {
    return SqsClient.builder()
        .region(Region.of(region))
        .credentialsProvider(ProfileCredentialsProvider.create(profile))
        .build();
  }
}
