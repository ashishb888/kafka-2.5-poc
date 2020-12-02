package poc.kafka.service;

import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.PostConstruct;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import poc.kafka.domain.Message;
import poc.kafka.domain.MessageRowMapper;
import poc.kafka.properties.KafkaProperties;

@Service
public class KafkaService {
	private Logger log = LoggerFactory.getLogger(KafkaService.class);
	@Autowired
	private KafkaProperties kp;
	Producer<Long, Message> producer;
	String toTopic;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	private AtomicLong id = new AtomicLong(0);
	private AtomicBoolean produce = new AtomicBoolean(true);

	public void sendMessage(Message message) {
		producer.send(new ProducerRecord<Long, Message>(toTopic, message));
	}

	public void sendMessages(Iterable<Message> messages) {
		messages.forEach(m -> {
			log.debug("m: " + m);
			producer.send(new ProducerRecord<Long, Message>(toTopic, m.getId(), m));
		});
	}

	private void sendMessages() {
		Thread t = new Thread(() -> {
			while (produce.get()) {
				@SuppressWarnings({ "deprecation" })
				List<Message> mLs = jdbcTemplate.query("SELECT ID, MFROM, TO, BODY FROM MESSAGE WHERE ID > ?",
						new Object[] { id.get() }, new MessageRowMapper());
				if (!mLs.isEmpty()) {
					sendMessages(mLs);
					id.getAndSet(mLs.stream().max(Comparator.comparing(Message::getId)).get().getId());
				}

				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}

		});

		t.setName("message-produer");
		t.start();
	}

	private void producer() {
		Properties kafkaProps = new Properties();

		kp.getKafkaProducer().forEach((k, v) -> {
			kafkaProps.put(k, v);
		});

		producer = new KafkaProducer<>(kafkaProps);
	}

	@PostConstruct
	private void init() {
		producer();
		toTopic = kp.getMetaData().get("toTopic");
		sendMessages();
	}
}
