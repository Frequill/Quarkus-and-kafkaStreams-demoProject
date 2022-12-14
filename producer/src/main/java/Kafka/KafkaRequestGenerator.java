package Kafka;

import DummyLogic.User;
import Entities.LoginRequest;
import io.smallrye.mutiny.Multi;
import io.smallrye.reactive.messaging.kafka.Record;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@ApplicationScoped
public class KafkaRequestGenerator {
    /**
     Static list "usersList" exists to keep a few "dummy" users that we can use for templates for sending out
     fake login-requests. This exists since this project is meant to be demonstrating kafka, not intended
     for field-use.

     List is filled in constructor
     */

    public static List<User> usersList = new ArrayList<>();

    public KafkaRequestGenerator() {
        usersList.add(new User("360noScope!", "password"));
        usersList.add(new User("Mange Uggla", "barKnugen"));
        usersList.add(new User("Michael Bay", "IllegalC4"));
        usersList.add(new User("JediLuke", "Sky-walking"));
        usersList.add(new User("AliensAreReal!", "Mr.Cruise"));
        usersList.add(new User("Farsan", "hemligtLösenord"));
        usersList.add(new User("Arne-Barnarne", "Trädkoja"));
        usersList.add(new User("Nisse-Tandborste", "hundStund"));
    }


    public Random random = new Random();
    @Inject
    Logger LOG;

    /**
     Counter that is incremented by one each time it is retrieved with the "makeId" method. This is how we
     generate an ID for each request being sent to backend/processor.
     */
    private static int loginRequestIdCounter = 0;
    public int makeLoginId() {
        loginRequestIdCounter++;
        return loginRequestIdCounter;
    }

    /**
     Spits out a randomly generated login-request to the "login-requests" table every 4 seconds,
     uses the content inside "usersList" as a list of templates, those "users" will attempt to log in
     */
    @Outgoing("login-requests")
    public Multi<Record<String, LoginRequest>> generateLoginRequests() {
        return Multi.createFrom().ticks().every(Duration.ofSeconds(4))
                .onOverflow().drop()
                .map(tick -> {
                    int id = makeLoginId();
                    User user = usersList.get(random.nextInt(usersList.size()));
                    LOG.info("loginRequestID: " + id + "  Username: " + user.getUsername());

                    return Record.of(String.valueOf(id), new LoginRequest(String.valueOf(id), user.getUsername(), user.getPassword()));
                });
    }

}