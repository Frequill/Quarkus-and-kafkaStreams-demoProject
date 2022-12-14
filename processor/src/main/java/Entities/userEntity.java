package Entities;

/**
 This class represents an account that has been created at an earlier point by a user.
 
 We will assume that all login-requests are received by users who have registered an account at an earlier point
 AE: This class exists for demo purposes only
 */
public class userEntity {

    private int id;
    private String username;
    private String password;

    public userEntity(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public userEntity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
