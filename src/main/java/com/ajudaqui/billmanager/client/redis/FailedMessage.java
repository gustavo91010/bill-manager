package com.ajudaqui.billmanager.client.redis;

import java.util.UUID;

// @Data
// @AllArgsConstructor
// @NoArgsConstructor
public class FailedMessage {
  private String id;
  private String originalTopic;
  private Object payload;
  private long retryTime; // timestamp do próximo retry
  private long delay; // delay atual em ms
  private int retryCount;
  private String errorMessage;


  public FailedMessage(String id, String originalTopic, Object payload, long retryTime, long delay, int retryCount,
      String error) {
    this.id = id;
    this.originalTopic = originalTopic;
    this.payload = payload;
    this.retryTime = retryTime;
    this.delay = delay;
    this.retryCount = retryCount;
    this.errorMessage = error;
  }

  public FailedMessage() {
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getOriginalTopic() {
    return originalTopic;
  }

  public void setOriginalTopic(String originalTopic) {
    this.originalTopic = originalTopic;
  }

  public Object getPayload() {
    return payload;
  }

  public void setPayload(Object payload) {
    this.payload = payload;
  }

  public long getRetryTime() {
    return retryTime;
  }

  public void setRetryTime(long retryTime) {
    this.retryTime = retryTime;
  }

  public long getDelay() {
    return delay;
  }

  public void setDelay(long delay) {
    this.delay = delay;
  }

  public int getRetryCount() {
    return retryCount;
  }

  public void setRetryCount(int retryCount) {
    this.retryCount = retryCount;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

}
