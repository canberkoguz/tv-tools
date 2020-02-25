package com.elsevier.tvtools;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class AwsClient {

  @Value("${AWS_ACCESS_KEY}")
  private String accessKey;
  @Value("${AWS_SECRET_KEY}")
  private String secretKey;

  @Bean
  public AmazonSQS awsSqsClient() {
    AWSCredentials credentials = new BasicAWSCredentials(
        accessKey,
        secretKey);
    return AmazonSQSClientBuilder.standard()
        .withCredentials(new AWSStaticCredentialsProvider(credentials))
        .withRegion(Regions.EU_WEST_1)
        .build();
  }

}
