package io.mercer.workshop;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.Properties;
import java.util.regex.Pattern;

@SpringBootApplication
public class WorkshopApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorkshopApplication.class, args);

        Properties streamsConfiguration = new Properties();
        // Give the Streams application a unique name.  The name must be unique in the Kafka cluster
        // against which the application is run.
        streamsConfiguration.put(StreamsConfig.APPLICATION_ID_CONFIG, "wordcount-lambda-example");
        // Where to find Kafka broker(s).
        streamsConfiguration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka1:9092");
        // Where to find the corresponding ZooKeeper ensemble.
        streamsConfiguration.put(StreamsConfig.ZOOKEEPER_CONNECT_CONFIG, "zoo1:2181");
        // Specify default (de)serializers for record keys and for record values.
        streamsConfiguration.put(StreamsConfig.KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        streamsConfiguration.put(StreamsConfig.VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());

        // Set up serializers and deserializers, which we will use for overriding the default serdes
        // specified above.
        final Serde<String> stringSerde = Serdes.String();
        final Serde<Long> longSerde = Serdes.Long();

        // In the subsequent lines we define the processing topology of the Streams application.
        KStreamBuilder builder = new KStreamBuilder();

        // Construct a `KStream` from the input topic "TextLinesTopic", where message values
        // represent lines of text (for the sake of this example, we ignore whatever may be stored
        // in the message keys).
        //
        // Note: We could also just call `builder.stream("TextLinesTopic")` if we wanted to leverage
        // the default serdes specified in the Streams configuration above, because these defaults
        // match what's in the actual topic.  However we explicitly set the deserializers in the
        // call to `stream()` below in order to show how that's done, too.
        KStream<String, String> textLines = builder.stream(stringSerde, stringSerde, "TextLinesTopic");

        Pattern pattern = Pattern.compile("\\W+", Pattern.UNICODE_CHARACTER_CLASS);

        KStream<String, Long> wordCounts = textLines
                // Split each text line, by whitespace, into words.  The text lines are the record
                // values, i.e. we can ignore whatever data is in the record keys and thus invoke
                // `flatMapValues` instead of the more generic `flatMap`.
                .flatMapValues(value -> Arrays.asList(pattern.split(value.toLowerCase())))
                // We will subsequently invoke `countByKey` to count the occurrences of words, so we use
                // `map` to ensure the key of each record contains the respective word.
                .map((key, word) -> new KeyValue<>(word, word))
                // Required in 0.10.0 to re-partition the data because we re-keyed the stream in the `map`
                // step.  Upcoming Kafka 0.10.1 does this automatically for you (no need for `through`).
                .through("RekeyedIntermediateTopic")
                // Count the occurrences of each word (record key).
                //
                // This will change the stream type from `KStream<String, String>` to
                // `KTable<String, Long>` (word -> count).  We must provide a name for
                // the resulting KTable, which will be used to name e.g. its associated
                // state store and changelog topic.
                .groupByKey().count()
                // Convert the `KTable<String, Long>` into a `KStream<String, Long>`.
                .toStream();

        // Write the `KStream<String, Long>` to the output topic.
        wordCounts.to(stringSerde, longSerde, "WordsWithCountsTopic");

        // Now that we have finished the definition of the processing topology we can actually run
        // it via `start()`.  The Streams application as a whole can be launched just like any
        // normal Java application that has a `main()` method.
        KafkaStreams streams = new KafkaStreams(builder, streamsConfiguration);
        streams.start();

        // Add shutdown hook to respond to SIGTERM and gracefully close Kafka Streams
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }
}
