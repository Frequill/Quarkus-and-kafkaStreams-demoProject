package Entities;

import javax.json.bind.annotation.JsonbProperty;

/**
 This entity represents a record from the Kafka topic "validate-login-requests", which is dynamically
 updated with records consisting of joined records from the topics "login-requests" and "responses" respectively.

 We can "get" a record from "validate-login-requests" and turn it into a JoinedLoginEntity to have request and response
 information in one single entity
 */

public class JoinedLoginEntity {
    @JsonbProperty("requestId")
    private String requestId;
    @JsonbProperty("responseId")
    private String responseId;
    @JsonbProperty("loginRequest")
    private LoginRequest loginRequest;
    @JsonbProperty("response")
    private Response response;

    public JoinedLoginEntity(String requestId, String responseId, LoginRequest loginRequest, Response response) {
        this.requestId = requestId;
        this.responseId = responseId;
        this.loginRequest = loginRequest;
        this.response = response;
    }

    public JoinedLoginEntity() {
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getResponseId() {
        return responseId;
    }

    public void setResponseId(String responseId) {
        this.responseId = responseId;
    }

    public LoginRequest getLoginRequest() {
        return loginRequest;
    }

    public void setLoginRequest(LoginRequest loginRequest) {
        this.loginRequest = loginRequest;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
