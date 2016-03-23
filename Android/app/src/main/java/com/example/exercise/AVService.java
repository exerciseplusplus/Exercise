package com.example.exercise;
import android.content.Context;
import android.util.Log;
import com.avos.avoscloud.*;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class AVService {
  

  public static void signUp(String username, String password, String email, SignUpCallback signUpCallback) {
    AVUser user = new AVUser();
    user.setUsername(username);
    user.setPassword(password);
    user.setEmail(email);
    user.signUpInBackground(signUpCallback);
    AVObject avo=new AVObject("UserInfo");
    avo.put("user", user);
    avo.put("username", username);
    avo.put("password", password);
    avo.saveInBackground();
  }

  public static void logout() {
    AVUser.logOut();
  }
}

