package com.ajudaqui.billmanager.exception;

import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;

public class ApiException {

  private String message;
  private String timestamp;
  private Integer status;
  private String error;

  public ApiException(String message, Integer status) {
    setTimestamp();
    setMessage(message);
    HttpStatus httpStatus = HttpStatus.resolve(status);
    setStatus(httpStatus.value());
    setError(httpStatus.toString());
  }

  public ApiException() {

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

  @Override
  public String toString() {
    return "{ \"message\":" + "\"" + getMessage() + "\"" + "," + "\"error\":" + "\"" + getError() + "\"" + ","
        + "\"timestamp\":" + "\"" + getTimestamp() + "\"" + "," + "\"status\":" + getStatus() + "}";
  }

}
