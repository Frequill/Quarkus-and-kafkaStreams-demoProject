package RequestProcessing;

import Entities.LoggedInEntity;
import Entities.LoginRequest;
import Entities.Response;
import Entities.userEntity;
import Functionality.LogginTokenGenerator;
import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.kafka.Record;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import javax.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.Objects;

@ApplicationScoped
public class RequestProcessor {

    public HashMap<String, userEntity> allAccounts = new HashMap<>();
    public HashMap<String, LoggedInEntity> loggedInUsers = new HashMap<>();

    /**
     Small dummy constructor places the fake "accounts" into hashmap "allAccounts" which acts as a fake-database
     This way we can assume that all login requests from these account come from users who have registered these
     accounts at an earlier time.
     */
    public RequestProcessor() {
        allAccounts.put("360noScope!", new userEntity(0, "360noScope!", "password"));
        allAccounts.put("Mange Uggla", new userEntity(1, "Mange Uggla", "barKnugen"));
        allAccounts.put("Michael Bay", new userEntity(2, "Michael Bay", "IllegalC4"));
        allAccounts.put("JediLuke", new userEntity(3, "JediLuke", "Sky-walking"));
        allAccounts.put("AliensAreReal!", new userEntity(4, "AliensAreReal!", "Mr.Cruise"));
        allAccounts.put("Farsan", new userEntity(5, "Farsan", "hemligtLösenord"));
        allAccounts.put("Arne-Barnarne", new userEntity(6, "Arne-Barnarne", "Trädkoja"));
        allAccounts.put("Nisse-Tandborste", new userEntity(6, "Nisse-Tandborste", "hundStund"));
    }


    /**
     This method reads each request found in kafka topic "login-requests" and checks weather or not the account exists,
     if the inputted password actually matches the recorded password that has been registered and returns an
     appropriate response entity to kafka topic "responses" based on said conditions.
     */
    @Incoming("login-requests")     // Read from this topic
    @Outgoing("responses")          // Write to this topic
    public Uni<Record<String, Response>> takeRequest(LoginRequest loginRequest) {
        // Data to be worked with...
        String requestId = loginRequest.getRequestId();
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        // Just to specify that the request has been received in the processor from producer
        System.out.println("Got loginRequest: " + requestId + "  Username: " + username + "  Password: " + password);

        // Ensures user is not already logged-in, you can NOT log in the same account twice during one session
        if (loggedInUsers.containsKey(username)) {
            return Uni.createFrom().item(Record.of(requestId, new Response(requestId, true, false, null)));
        }


        // If we get here, the user is not already logged in
        // If the account exists and password matches - Login successful
        if (allAccounts.containsKey(username)
                && Objects.equals(allAccounts.get(username).getPassword(), password)){

            // Create a new login token that consists of the username + current dateTime
            String loginToken = LogginTokenGenerator.getInstance().makeNewToken(username);

            loggedInUsers.put(loginRequest.getUsername(), new LoggedInEntity(username, loginToken));
            return Uni.createFrom().item(Record.of(requestId, new Response(requestId, true, true, loginToken)));
        }


        // Else if account exists but password does not match - Login failed
        else if (allAccounts.containsKey(username)
                && !Objects.equals(allAccounts.get(username).getPassword(), password)) {
            return Uni.createFrom().item(Record.of(requestId, new Response(requestId, true, false, null)));
        }


        // Else - assume account does not exist, should output some type of error
        else {return Uni.createFrom().item(Record.of(requestId, new Response(requestId, false, false, null)));}
    }


}