package poc.kafka.properties;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "kafka")
public class KafkaProperties {
	private Map<String, String> kafkaStreams;
	private Map<String, String> metaData;

	public Map<String, String> getKafkaStreams() {
		return kafkaStreams;
	}

	public void setKafkaStreams(Map<String, String> kafkaStreams) {
		this.kafkaStreams = kafkaStreams;
	}

	public Map<String, String> getMetaData() {
		return metaData;
	}

	public void setMetaData(Map<String, String> metaData) {
		this.metaData = metaData;
	}

}
