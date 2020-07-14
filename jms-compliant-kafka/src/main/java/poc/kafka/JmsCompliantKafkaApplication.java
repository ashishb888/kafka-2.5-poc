package poc.kafka;

import java.util.Properties;
import java.util.UUID;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import io.confluent.kafka.jms.JMSClientConfig;
import io.confluent.kafka.jms.KafkaConnectionFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class JmsCompliantKafkaApplication {

	public static void main(String[] args) {
		SpringApplication.run(JmsCompliantKafkaApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ac) {
		return args -> {
			Properties settings = new Properties();
			settings.put(JMSClientConfig.CLIENT_ID_CONFIG, "jms-client");
			settings.put(JMSClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
			settings.put(JMSClientConfig.CONFLUENT_TOPIC_REPLICATION_FACTOR_CONF, 1);

			ConnectionFactory connectionFactory = new KafkaConnectionFactory(settings);
			try {
				Connection connection = connectionFactory.createConnection();
				Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
				Destination testQueue = session.createQueue("jms-test");

				MessageProducer producer = session.createProducer(testQueue);
				for (int i = 0; i < 50; i++) {
					TextMessage message = session.createTextMessage();
					message.setText(UUID.randomUUID().toString());
					producer.send(message);
				}

				MessageConsumer consumer = session.createConsumer(testQueue);
				while (true) {
					TextMessage message = (TextMessage) consumer.receive();
					System.out.println(message.getText());
				}
			} catch (JMSException e) {
				log.error(e.getMessage(), e);
			}
		};
	}
}
