package com.ajudaqui.billmanager.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

@Configuration
public class SqsConfig {
  // us-east-2a

  @Value("${aws.profile}")
  private String awsProfile;

  @Bean
  public SqsClient sqsClient() {
    return SqsClient.builder()
        .region(Region.US_EAST_2) // Escolha a região que sua fila está
        // .credentialsProvider(DefaultCredentialsProvider.create()) // Usa as
        // credenciais padrão da AWS
        .credentialsProvider(ProfileCredentialsProvider.create(awsProfile)) // Usar o perfil "prod"
        .build();
  }
}
