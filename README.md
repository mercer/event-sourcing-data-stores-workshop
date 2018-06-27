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
127.0.0.1     zoo1
```

```
→ git clone https://github.com/mercer/event-sourcing-data-stores-workshop.git
```

```
→ docker-compose create
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

- https://www.youtube.com/watch?v=Q5wOegcVa8E 10 mins


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

- (bonus) https://www.youtube.com/watch?v=LDW0QWie21s 50 min

# to build, run, publish app
```
./gradlew docker dockerPush
```

```
./gradlew bootRun
```

# references

- https://www.confluent.io/blog/event-sourcing-cqrs-stream-processing-apache-kafka-whats-connection/
- https://github.com/simplesteph/kafka-stack-docker-compose

