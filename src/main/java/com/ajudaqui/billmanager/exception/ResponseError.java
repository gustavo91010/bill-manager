
package com.ajudaqui.billmanager.exception;

public class ResponseError {

  private String messageError;

  public String getMessageError() {
    return messageError;
  }

  public void setMessageError(String messageError) {
    this.messageError = messageError;
  }

  public ResponseError(String messageError) {
    this.messageError = messageError;
  }
}
