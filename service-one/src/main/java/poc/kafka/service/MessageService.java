package poc.kafka.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import poc.kafka.domain.Message;

@Service
public class MessageService {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void saveAll(Iterable<Message> messages) {
		List<Object[]> batch = new ArrayList<Object[]>();
		for (Message message : messages) {
			Object[] values = new Object[] { message.getmFrom(), message.getTo(), message.getBody() };
			batch.add(values);
		}

		jdbcTemplate.batchUpdate("INSERT INTO MESSAGE (MFROM, TO, BODY) VALUES (?,?,?)", batch);
	}
}
