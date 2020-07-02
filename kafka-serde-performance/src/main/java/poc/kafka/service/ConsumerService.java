package poc.kafka.service;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import poc.kafka.domain.Person;
import poc.kafka.properties.KafkaProperties;

@Service
@Slf4j
public class ConsumerService {

	@Autowired
	private KafkaProperties kp;
	private AtomicInteger count = new AtomicInteger(0);

	private void consume() {
		log.debug("consume service");

		String topic = kp.getMetaData().get("topic");

		Consumer<Long, Person> consumer = consumer();

		TopicPartition tp = new TopicPartition(topic, 0);
		consumer.assign(Arrays.asList(tp));
		consumer.seekToBeginning(Arrays.asList(tp));
		// consumer.assignment();

		benchmark();

		while (true) {
			ConsumerRecords<Long, Person> records = consumer.poll(Duration.ofMillis(10));
			for (ConsumerRecord<Long, Person> consumerRecord : records) {
				count.getAndIncrement();
			}
			// Thread.sleep(1000);
		}
	}

	private void benchmark() {
		ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();

		timer.scheduleAtFixedRate(() -> {
			log.debug("count: " + count.get());
		}, 10, 10, TimeUnit.SECONDS);
	}

	private Consumer<Long, Person> consumer() {
		Properties kafkaProps = new Properties();
		kp.getKafkaConsumer().forEach((k, v) -> {
			// log.debug("k: " + k + ", v: " + v);
			kafkaProps.put(k, v);
		});

		return new KafkaConsumer<>(kafkaProps);
	}

	public void main() {
		log.debug("main service");

		consume();
	}
}
