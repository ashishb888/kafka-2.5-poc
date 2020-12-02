package poc.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import poc.kafka.service.KafkaStreamService;

@SpringBootApplication
public class ServiceTwoApplication {

	@Autowired
	private KafkaStreamService kss;

	public static void main(String[] args) {
		SpringApplication.run(ServiceTwoApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ac) {
		return args -> {
			kss.processMessages();
		};
	}

}
