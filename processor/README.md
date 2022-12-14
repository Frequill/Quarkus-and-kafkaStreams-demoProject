A proof of concept of a "processor" application for kafka built with Quarkus. As the name implies, this is a processing node inside a kafka cluster. 
It takes information sent from the frontend (producer), handles data and returns it to a kafka broker.

This project can easily be made into a docker image and run inside a kafka cluster using docker compose. One simply has to follow the description inside "Dockerfile.jvm"
