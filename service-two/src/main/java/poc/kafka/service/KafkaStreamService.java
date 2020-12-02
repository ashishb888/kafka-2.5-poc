package poc.kafka.service;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import poc.kafka.domain.Message;
import poc.kafka.domain.MessageStatus;
import poc.kafka.properties.KafkaProperties;
import poc.kafka.serialization.MessageDeserializer;
import poc.kafka.serialization.MessageSerializer;
import poc.kafka.serialization.MessageStatusDeserializer;
import poc.kafka.serialization.MessageStatusSerializer;

@Service
public class KafkaStreamService {
	@Autowired
	private KafkaProperties kp;
	private static Logger log = LoggerFactory.getLogger(KafkaStreamService.class);

	public void processMessages() throws Exception {
		final StreamsBuilder builder = new StreamsBuilder();
		String fromTopic = kp.getMetaData().get("fromTopic");
		String toTopic = kp.getMetaData().get("toTopic");

		KStream<Long, Message> source = builder.stream(fromTopic,
				Consumed.with(Serdes.Long(), Serdes.serdeFrom(new MessageSerializer(), new MessageDeserializer())));

		source.peek((k, v) -> {
			log.debug("k: " + k + ", v: " + v);
		}).map((k, v) -> KeyValue.pair(k, test(v))).to(toTopic, Produced.with(Serdes.Long(),
				Serdes.serdeFrom(new MessageStatusSerializer(), new MessageStatusDeserializer())));

		final Topology topology = builder.build();
		final KafkaStreams streams = new KafkaStreams(topology, configs());
		final CountDownLatch latch = new CountDownLatch(1);

		log.info("topology: " + topology.describe());

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			streams.close();
			latch.countDown();
		}, "streams-shutdown-hook"));

		try {
			streams.start();
			latch.await();
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
		}
	}

	private MessageStatus test(Message message) {
		if (message.getId() % 2 == 0)
			return new MessageStatus(message.getId(), "success");

		return new MessageStatus(message.getId(), "error");
	}

	private Properties configs() {
		Properties configs = new Properties();

		kp.getKafkaStreams().forEach((k, v) -> {
			configs.put(k, v);
		});

		return configs;
	}

}
