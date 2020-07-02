package poc.kafka.properties;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "kafka")
public class KafkaProperties {
	private Map<String, String> kafkaConsumer;
	private Map<String, String> kafkaProducer;
	private Map<String, String> metaData;
}
