package Topology;

import Entities.JoinedLoginEntity;
import Entities.LoginRequest;
import Entities.Response;
import io.quarkus.kafka.client.serialization.JsonbSerde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import java.time.Duration;

@ApplicationScoped
public class TopologyProducer {
    // Kafka Topics
    private static final String LOGIN_REQUESTS_TOPIC = "login-requests";
    private static final String RESPONSES_TOPIC = "responses";
    private static final String VALIDATE_LOGIN_TOPIC = "validate-login-requests";

    /**
     As kafka stores information in the form of a Key and a value, A Stream represents a continuous flow of
     data from one node in your kafka "network" to another holding a key and a value.

     A "Topology" is a graphical representation of stream "flow" in a kafka cluster, it serves as an anchor-point
     for where a stream starts and does its logic - By annotating a method with @Produces and having said method
     return a Topology, kafka is asked to continuously update itself with the information from one or multiple streams
     whenever these hypothetical streams produce or consume something.

     Confusing? Complicated? Don't worry, this is not meant to be understood in two seconds. For now, just think of a
     topology as "That which we return from a method if we want one or multiple stream(s) to do SOMETHING with a Kafka broker

     This topology "reads" login-requests from frontend and joins them with results from backend by id.
     This way, we can easily tell what response belongs to which requests and weather or not it succeeded.
     */

    @Produces
    public Topology getLoginRequests() {
        // The "StreamsBuilder" object is used to create and return the topology
        StreamsBuilder builder = new StreamsBuilder();

        // Serializing/deserializing archived with JsonB, alternatives exist for Jackson
        JsonbSerde<Response> responseSerde = new JsonbSerde<>(Response.class);
        JsonbSerde<LoginRequest> loginRequestSerde = new JsonbSerde<>(LoginRequest.class);
        JsonbSerde<JoinedLoginEntity> validateLoginRequestsSerde = new JsonbSerde<>(JoinedLoginEntity.class);

        /*
        Turning streams into objects with injected serializers/deserializers can dramatically minimize the code
        one would have to write each time you'd want to interact with your streams programmatically
        */
        // Stream of responses, this contains returned records from backend/processor
        KStream<String, Response> responseStream = builder.stream(RESPONSES_TOPIC, Consumed.with(Serdes.String(), responseSerde));
        // Stream of login requests coming from a frontend
        KStream<String, LoginRequest> loginRequestStream = builder.stream(LOGIN_REQUESTS_TOPIC, Consumed.with(Serdes.String(), loginRequestSerde));





        /*
        THIS is a valueJoiner, it takes in one object, a second object and then the third object is returned,
        combining the two first values into a new JOINED value.

                    |1:st value| |2:nd value| |value to be returned|                                */
        ValueJoiner<LoginRequest, Response, JoinedLoginEntity> valueJoiner = (loginRequest, response) -> {
            /*
            A valueJoiner CAN be a good place to do some custom error-handling,
            you could, for example make a switch/if-statement depending on the information that was received
            and return different entities if one entity is NULL or contains the wrong information.
            */
            return new JoinedLoginEntity(loginRequest.getRequestId(), response.getResponseId(), loginRequest, response);
        };





        // A left-join will attempt to join EACH record of the first/primary topic (in this case "loginRequestStream")
        // with an equivalent record in the secondary topic ("responseStream") that shares the same ID.
        // Should no data with said ID be available in the secondary topic, it will produce a join with a NULL value
        loginRequestStream.leftJoin(
                        responseStream,
                        valueJoiner,
                        JoinWindows.ofTimeDifferenceAndGrace(Duration.ofSeconds(5), Duration.ZERO),
                        StreamJoined.with(Serdes.String(), loginRequestSerde, responseSerde))

                // Returning result to KafkaTopic by name, manually providing Serializing
                .to(
                        VALIDATE_LOGIN_TOPIC,
                        Produced.with(Serdes.String(), validateLoginRequestsSerde)
                );



        return builder.build();
    }
}