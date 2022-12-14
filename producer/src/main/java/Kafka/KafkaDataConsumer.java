package Kafka;

import DummyLogic.User;
import Entities.JoinedLoginEntity;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import javax.enterprise.context.ApplicationScoped;
import java.util.Objects;

@ApplicationScoped
public class KafkaDataConsumer {

    /**
     When a user sends a login-request from Frontend to Backend a Login Token is generated in the Backend,
     this is how we interact with the service, if a token exists connected to a User entity that means they are
     logged-in! But we need to have access to each user loginToken (created in Backend) here in the Frontend/producer

     That's where this method comes in, it takes every new record sent to the topic "validate-login-requests"
     and turns them/it into a "joinedLoginEntity". The data on this topic is a joining of the login-request
     (from Frontend/Producer) and the response coming back from the Backend/Producer

     Having the information from both the request and the result allows this method to assign a loginToken
     to its respective user by combining the username from the login-request with the login token from the response
     since both results are merged in the topic "validate-login-requests"
     */
    @Incoming("validate-login-requests")
    public void setUsersToken (JoinedLoginEntity joinedLoginEntity) {
        String username = joinedLoginEntity.getLoginRequest().getUsername();
        String loginToken = joinedLoginEntity.getResponse().getLoginToken();

        /*
        Goes through every user inside the static list "usersList" from class "KafkaRequestGenerator",
        IF a users username matches the username of the joinedLoginEntity then set users loginToken locally to
        the same value as was sent from the backend and is now in the joinedLoginEntity
       */
        for (User user : KafkaRequestGenerator.usersList) {
            if (Objects.equals(user.getUsername(), username) && loginToken != null) {
                user.setLoginToken(loginToken);

                System.out.println("User: " + username + " " + joinedLoginEntity.getLoginRequest().getUsername() +
                        "has been assigned his/her loginToken: " + loginToken);

                System.out.println("User.getLoginToken = " + user.getLoginToken());
                return;
            } else if (Objects.equals(user.getUsername(), username) && loginToken == null) {
                System.out.println("Login token = null! This login-attempt was unsuccessful");
                return;
            }
        }
    }



}