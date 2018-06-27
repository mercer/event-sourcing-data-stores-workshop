# event-sourcing-data-stores-workshop

In which we explore different data stores for read/write models of event sourcing with CQRS

# workshop

```
install docker
```

- https://www.youtube.com/watch?v=bdBfbIiR95k first 30 minutes


```
/etc/hosts

127.0.0.1     kafka1
127.0.0.1     kafka2
127.0.0.1     kafka3
127.0.0.1     zoo1
127.0.0.1     zoo2
127.0.0.1     zoo3
127.0.0.1     kafka-schema-registry
127.0.0.1     kafka-schema-registry-ui
127.0.0.1     kafka-rest-proxy
127.0.0.1     kafka-topics-ui
127.0.0.1     kafka-connect-ui
127.0.0.1     zoonavigator-web
127.0.0.1     zoonavigator-api
```

```
→ git clone ...
```

```
→ docker-compose scale kafka1=1 zoo1=1 app=0
```

```
→ docker-compose logs -f
```

```
ctrl-c
```

```
→ docker exec -it event-sourcing-data-stores-workshop_kafka1_1 \
    kafka-topics --create \
    --topic TextLinesTopic \
    --zookeeper zoo1:2181 \
    --partitions 1 \
    --replication-factor 1
```

```
→ docker exec -it event-sourcing-data-stores-workshop_kafka1_1 \
    kafka-topics --create \
    --topic RekeyedIntermediateTopic \
    --zookeeper zoo1:2181 \
    --partitions 1 \
    --replication-factor 1
```

```
→ docker exec -it event-sourcing-data-stores-workshop_kafka1_1 \
    kafka-topics --create \
    --topic WordsWithCountsTopic \
    --zookeeper zoo1:2181 \
    --partitions 1 \
    --replication-factor 1
```


- https://www.youtube.com/watch?v=Q5wOegcVa8E

```
→ docker-compose scale kafka1=1 zoo1=1 app=1
```

```
→ docker-compose logs -f
```

```
# produce some values ...
```

```
→ docker-compose scale kafka1=1 zoo1=1 app=0
```

```
# produce some values ...
```

```
→ docker-compose scale kafka1=1 zoo1=1 app=1
```

```
→ docker exec -it event-sourcing-data-stores-workshop_kafka1_1 \
    kafka-console-producer \
    --broker-list kafka1:9092 \
    --topic TextLinesTopic
```

```
→ docker exec -it event-sourcing-data-stores-workshop_kafka1_1 \
    kafka-console-consumer \
    --topic WordsWithCountsTopic \
    --from-beginning \
    --zookeeper zoo1:2181 \
    --property print.key=true \
    --property value.deserializer=org.apache.kafka.common.serialization.LongDeserializer
```
- https://www.youtube.com/watch?v=p9wcx3aTjuo

# building app
```
./gradlew docker dockerPush
```

# run app outside docker
```
./gradlew bootRun
```

# references

- https://github.com/confluentinc/examples/blob/kafka-0.10.0.0-cp-3.0.0/kafka-streams/src/main/java/io/confluent/examples/streams/WordCountLambdaExample.java
- https://www.confluent.io/blog/event-sourcing-cqrs-stream-processing-apache-kafka-whats-connection/
- https://github.com/simplesteph/kafka-stack-docker-compose

# todo
- explain tutorial
- material on kafka
- material on kafka event sourcing
