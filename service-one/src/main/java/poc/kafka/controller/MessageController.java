package poc.kafka.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import poc.kafka.domain.Message;
import poc.kafka.domain.Response;
import poc.kafka.service.MessageService;

@RequestMapping("/messages")
@RestController
public class MessageController {

	@Autowired
	private MessageService ms;

	@PostMapping
	public Response saveAll(@RequestBody List<Message> messages) {
		try {
			ms.saveAll(messages);
		} catch (Exception e) {
			e.printStackTrace();
			return new Response("error",
					Collections.singletonMap("DB_ERR", "Error while inserting messages into database"));
		}

		return new Response("success",
				Collections.singletonMap("DB_SUC", "Successfully inserted messages into database"));
	}

	/*
	 * @GetMapping public Iterable<Message> findAll() { return mr.findAll(); }
	 */
}
