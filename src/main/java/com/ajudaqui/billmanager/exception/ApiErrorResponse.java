package com.ajudaqui.billmanager.exception;

import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;

public class ApiErrorResponse {

  private String message;
  private String timestamp;
  private Integer status;
  private String error;

  public ApiErrorResponse(String message, Integer status) {
    setTimestamp();
    setMessage(message);
    HttpStatus httpStatus = HttpStatus.resolve(status);
    setStatus(httpStatus.value());
    setError(httpStatus.toString());
  }

  public ApiErrorResponse() {

  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp() {
    this.timestamp = LocalDateTime.now().toString();
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  public String toJson() {
    return String.format(
        "{ \"message\": \"%s\", \"error\": \"%s\", \"timestamp\": \"%s\", \"status\": %d }",
        getMessage(), getError(), getTimestamp(), getStatus());
  }

}
