package com.example.studionagran;

public class UserSession {
    private static UserSession instance;
    private boolean loggedIn = false;
    private String username;

    private UserSession() {}

    public static synchronized UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void login(String username) {
        loggedIn = true;
        this.username = username;
    }

    public void logout() {
        loggedIn = false;
        username = null;
    }

    public String getUsername() {
        return username;
    }
}