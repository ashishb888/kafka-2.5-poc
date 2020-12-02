package poc.kafka.properties;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "kafka")
public class KafkaProperties {
	private Map<String, String> kafkaConsumer;
	private Map<String, String> kafkaProducer;
	private Map<String, String> metaData;

	public Map<String, String> getKafkaConsumer() {
		return kafkaConsumer;
	}

	public void setKafkaConsumer(Map<String, String> kafkaConsumer) {
		this.kafkaConsumer = kafkaConsumer;
	}

	public Map<String, String> getKafkaProducer() {
		return kafkaProducer;
	}

	public void setKafkaProducer(Map<String, String> kafkaProducer) {
		this.kafkaProducer = kafkaProducer;
	}

	public Map<String, String> getMetaData() {
		return metaData;
	}

	public void setMetaData(Map<String, String> metaData) {
		this.metaData = metaData;
	}

}
