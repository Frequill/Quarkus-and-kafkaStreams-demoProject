# Quarkus-and-kafkaStreams-demoProject
These three Quarkus projects serves as a demo/sandbox environment for learning the fundamentals of using Apache Kafka with 
the Quarkus Java application framework.

Learning Quarkus is difficult and takes time... but learning how to use Kafka with Quarkus at the same time is even harder!
But fear not! These three applications will work as your entry point for how to write basic syntax with Quarkus using the reasteasy-reactive-kafka-messaging
library and a bit of mutiny to send records (messages) from one application to another using Apache Kafka. 

Not only that, but you will also be able to play with the basics of the 'Kafka Streams' library and see how to materialize streams programatically, how to
serialize/deserialize kafka records and how to join multiple streams together to create all new results and java entities containing information from
multiple different Kafka Topics!

With google by your side, this project will take you from complete beginner to intermediate, just follow along how my code is written and be thankful
for me taking all the heavy headaches figuring these basic kafka-concepts out myself so that you don't have to! :)

These projects are all designed to run in Quarkus Dev-mode so that you do NOT need to port any of these projects over to a cluster of docker-images
that you would then have to run through Docker Compose, instead these will all run and function right from the comfort of your IDE!

You can easily download Offset Explorer (Formerly known as "The Kafka tool") and inspect the broker and each Kafka Topic individually and see the data 
appearing and moving from app to app in real time. The only setup you'd have to do is to download Offset Explorer and connect to the Kafka Cluster
by (in Offset Explorer) clicking on "Add cluster" -> giving it a name ("testCluster" works fine)  -> Click on the "Advanced" tab -> Finally input 
the connection address that appears in the "Producer" project as you run it in dev mode from your IDE or a terminal window
(Output from Producer should be "localhost:xxxxx" - where 'x' represents a randomized number) copy-paste said localhost address 
into the "Bootstrap servers" field and you are up and running!

This is super simple and will allow you to see, inspect and modify the Kafka Broker and the different Topics.

This project works by simply randomizing some fake users that send "login requests" to the backend by placing said requests on a kafka topic called
"login-requests". The backend (processor) will intercept these requests, read them and generate a response based on weather or not the user is already
logged in or not. The processor places its response in the "responses" topic. The "Streams" application will then combine each request from the topic
"login-requests" with each response from the "responses" topic IF both request and response share the same ID and both appear within a time-frame of
5 seconds (if no response appears within 5 seconds we will assume that the backend is unresponsive).

If a login-request and a response sharing the same ID's appear within 5 seconds they are joined together with a left-join and sent to the final topic
"validate-login-requests" which holds each request and response in one convenient location! The frontend (producer) finally intercepts this topic being
updated and saves each "loginToken" (generated in backend) to the corresponding user that made the original request in the frist place (in the frontend)

The logic is basic and easy to understand, the goal wasn't to have the worlds most advanced kafka logic, instead these separate applications will show you
the basics of using Quarkus and Kafka together without falling into the big headaches of figuring EVERYTHING out on your own.

IF you'd like to take the whole thing further then you should know that each project contains a "dockerfile.jvm" inside src/main/docker, with these
instructions you should be more than capable of making each application into a docker image, placing each image inside a Compose cluster of containers 
and booting them up manually using Docker Compose! The only thing you would have to do then is to write your own YML-file (Advanced difficulty)


FINALLY, I do recommend that you take an extra look at my documentation. Each project should contain some Markdown containing tips and tricks I came up
with during my months of experementing with kafka as well as some do's and don't's. I've also provided links to some of the guides/resources that 
I found useful.

I've also made sure that each class contains (mostly) useful comments so that it should be easy for anybody to understand exactly what happens
from step to step inside each class and how exactly information is sent to and read from the Kafka Broker.

Good hunting, consult google if you fail and don't blame me for your problems! :D
