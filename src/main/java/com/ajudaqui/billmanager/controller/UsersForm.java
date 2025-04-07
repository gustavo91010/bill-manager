package com.ajudaqui.billmanager.controller;

import com.ajudaqui.billmanager.entity.Users;

public class UsersForm {
  private Users user;

  public UsersForm(Users user) {
    this.user = user;
  }

  public Users getUser() {
    return user;
  }

  public void setUser(Users user) {
    this.user = user;
  }

}
