package Entities;

import javax.json.bind.annotation.JsonbProperty;

/**
 This entity represents a record from (or sent to) the Kafka topic "responses"
 */

public class Response {

    @JsonbProperty("responseId")
    private String responseId;
    @JsonbProperty("accountExists")
    private boolean accountExists;
    @JsonbProperty("loginSuccessful")
    private boolean loginSuccessful;
    @JsonbProperty("loginToken")
    private String loginToken;

    public Response(String responseId, boolean accountExists, boolean loginSuccessful, String loginToken) {
        this.responseId = responseId;
        this.accountExists = accountExists;
        this.loginSuccessful = loginSuccessful;
        this.loginToken = loginToken;
    }

    public Response() {
    }

    public String getResponseId() {
        return responseId;
    }

    public void setResponseId(String responseId) {
        this.responseId = responseId;
    }

    public boolean isAccountExists() {
        return accountExists;
    }

    public void setAccountExists(boolean accountExists) {
        this.accountExists = accountExists;
    }

    public boolean isLoginSuccessful() {
        return loginSuccessful;
    }

    public void setLoginSuccessful(boolean loginSuccessful) {
        this.loginSuccessful = loginSuccessful;
    }

    public String getLoginToken() {
        return loginToken;
    }

    public void setLoginToken(String loginToken) {
        this.loginToken = loginToken;
    }
}