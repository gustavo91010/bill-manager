package com.ajudaqui.billmanager.controller.from;

import java.util.List;

import com.ajudaqui.billmanager.entity.Users;

public class UsersListFrom {
  private List<Users> users;

  public List<Users> getUsers() {
    return users;
  }

  public void setUsers(List<Users> users) {
    this.users = users;
  }

  public UsersListFrom(List<Users> users) {
    this.users = users;
  }

}
