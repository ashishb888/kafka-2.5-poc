package poc.kafka.service;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import poc.kafka.domain.Person;
import poc.kafka.properties.KafkaProperties;

@Service
@Slf4j
public class ProducerService {

	@Autowired
	private KafkaProperties kp;
	private AtomicInteger count = new AtomicInteger(0);

	private void produce() {
		log.debug("produce service");

		Producer<Long, Person> producer = producer();
		String topic = kp.getMetaData().get("topic");
		int limit = Integer.valueOf(kp.getMetaData().get("records"));

		benchmark();

		log.debug("Start: " + Instant.now());

		LongStream.iterate(0, i -> i + 1).limit(limit).forEach(i -> {
			producer.send(new ProducerRecord<Long, Person>(topic, i, new Person(i, UUID.randomUUID().toString())));
			count.getAndIncrement();
		});

		log.debug("End: " + Instant.now());
	}

	private void benchmark() {
		ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();

		timer.scheduleAtFixedRate(() -> {
			log.debug("count: " + count.get());
		}, 10, 10, TimeUnit.SECONDS);
	}

	private Producer<Long, Person> producer() {

		Properties kafkaProps = new Properties();

		kp.getKafkaProducer().forEach((k, v) -> {
			// log.debug("k: " + k + ", v: " + v);
			kafkaProps.put(k, v);
		});

		return new KafkaProducer<>(kafkaProps);
	}

	public void main() {
		log.debug("main service");

		produce();
	}
}
