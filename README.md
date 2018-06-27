# event-sourcing-data-stores-workshop
In which we explore different data stores for read/write models of event sourcing with CQRS

# presentation
- https://www.youtube.com/watch?v=bdBfbIiR95k
- tbd kafka presentation

# workshop

`→ git clone ...`

`→ docker-compose up`

`→ docker exec -it event-sourcing-data-stores-workshop_kafka1_1 kafka-topics --create --topic TextLinesTopic  --zookeeper zoo1:2181 --partitions 1 --replication-factor 1`

`→ docker exec -it event-sourcing-data-stores-workshop_kafka1_1 kafka-topics --create --topic RekeyedIntermediateTopic  --zookeeper zoo1:2181 --partitions 1 --replication-factor 1`

`→ docker exec -it event-sourcing-data-stores-workshop_kafka1_1 kafka-topics --create --topic WordsWithCountsTopic --zookeeper zoo1:2181 --partitions 1 --replication-factor 1`

`→ docker exec -it event-sourcing-data-stores-workshop_kafka1_1 kafka-console-consumer --topic WordsWithCountsTopic --from-beginning --zookeeper zoo1:2181 --property print.key=true --property value.deserializer=org.apache.kafka.common.serialization.LongDeserializer`


# references

- https://github.com/confluentinc/examples/blob/kafka-0.10.0.0-cp-3.0.0/kafka-streams/src/main/java/io/confluent/examples/streams/WordCountLambdaExample.java
- https://www.confluent.io/blog/event-sourcing-cqrs-stream-processing-apache-kafka-whats-connection/
- https://github.com/simplesteph/kafka-stack-docker-compose
