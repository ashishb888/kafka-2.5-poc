package poc.kafka.service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.PostConstruct;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import poc.kafka.domain.MessageStatus;
import poc.kafka.properties.KafkaProperties;

@Service
public class KafkaConsumerService {
	@Autowired
	private KafkaProperties kp;
	String fromTopic;
	private AtomicBoolean consume = new AtomicBoolean(true);
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private void consumeMessages() {
		Thread t = new Thread(() -> {
			Consumer<Long, MessageStatus> consumer = consumer();
			String fromTopic = kp.getMetaData().get("fromTopic");
			consumer.subscribe(Arrays.asList(fromTopic));

			while (consume.get()) {
				ConsumerRecords<Long, MessageStatus> records = consumer.poll(Duration.ofMillis(10));
				List<MessageStatus> list = new ArrayList<>();
				records.forEach(r -> list.add(r.value()));
				batchUpdate(list);
			}

		});

		t.setName("message-consumer");
		t.start();
	}

	private void batchUpdate(Iterable<MessageStatus> list) {
		List<Object[]> batch = new ArrayList<Object[]>();
		for (MessageStatus messageStatus : list) {
			Object[] values = new Object[] { messageStatus.getMessageId(), messageStatus.getStatus() };
			batch.add(values);
		}

		jdbcTemplate.batchUpdate("INSERT INTO MESSAGE_STATUS (ID, STATUS) VALUES (?,?)", batch);
	}

	private Consumer<Long, MessageStatus> consumer() {
		Properties kafkaProps = new Properties();

		kp.getKafkaConsumer().forEach((k, v) -> {
			kafkaProps.put(k, v);
		});

		return new KafkaConsumer<>(kafkaProps);
	}

	@PostConstruct
	private void init() {
		consumeMessages();
	}
}
