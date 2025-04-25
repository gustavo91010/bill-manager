package com.ajudaqui.billmanager.response;

import java.util.ArrayList;
import java.util.List;

public class ApiResponseList {

  private List<String> response= new ArrayList<>();

  public List<String> getResponse() {
    return response;
  }

  public void setResponse(List<String> response) {
    this.response = response;
  }

  public ApiResponseList(List<String> response) {
    this.response = response;
  }

 }
