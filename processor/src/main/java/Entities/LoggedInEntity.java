package Entities;

/**
 This entity represents a user that is logged in, we only know username and their loginToken.
 The entity itself exists for demo purposes and doesn't contain all vital info one would want to know about
 a logged user in a proper system made for "field-use"
 */

public class LoggedInEntity {

    private String username;
    private String loginToken;


    public LoggedInEntity(String username, String loginToken) {
        this.username = username;
        this.loginToken = loginToken;
    }

    public LoggedInEntity() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLoginToken() {
        return loginToken;
    }

    public void setLoginToken(String loginToken) {
        this.loginToken = loginToken;
    }
}