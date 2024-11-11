package com.example.bank.publisher;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;
import software.amazon.awssdk.services.sns.model.SnsException;

import java.math.BigDecimal;
import java.util.Random;

public class SnsEventPublisher implements EventPublisher {

    private final SnsClient snsClient;
    private static final int MAX_RETRIES = 3;
    private static final long BASE_DELAY_MS = 200;

    public SnsEventPublisher() {
        this.snsClient = SnsClient.builder()
                .region(Region.US_EAST_1) // Replace with your AWS region
                .build();
    }

    @Override
    public void publishWithdrawalEvent(Long accountId, BigDecimal amount) {
        String message = String.format("{\"accountId\":%d,\"amount\":\"%s\",\"status\":\"SUCCESSFUL\"}", accountId, amount);
        PublishRequest request = PublishRequest.builder()
                .message(message)
                .topicArn("arn:aws:sns:YOUR_REGION:YOUR_ACCOUNT_ID:YOUR_TOPIC_NAME") // Replace with your ARN
                .build();

        int attempt = 0;
        while (attempt < MAX_RETRIES) {
            try {
                attempt++;
                PublishResponse response = snsClient.publish(request);
                System.out.println("SNS publish successful: " + response.messageId());
                break;
            } catch (SnsException e) {
                System.err.println("SNS publish attempt " + attempt + " failed: " + e.awsErrorDetails().errorMessage());

                if (attempt >= MAX_RETRIES) {
                    System.err.println("SNS publish failed after " + MAX_RETRIES + " attempts");
                    throw new RuntimeException("Failed to publish SNS event after retries", e);
                }

                long delay = (long) (Math.pow(2, attempt) * BASE_DELAY_MS) + (new Random().nextInt(100));
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Retry interrupted", ie);
                }
            }
        }
    }
}
