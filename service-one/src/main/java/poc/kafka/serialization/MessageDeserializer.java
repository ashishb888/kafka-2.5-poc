package poc.kafka.serialization;

import java.io.ByteArrayInputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import poc.kafka.domain.Message;

public class MessageDeserializer implements Deserializer<Message> {

	@Override
	public Message deserialize(String topic, byte[] data) {
		try (ByteArrayInputStream bis = new ByteArrayInputStream(data); ObjectInput in = new ObjectInputStream(bis)) {
			Message m = new Message();
			m.readExternal(in);
			return m;
		} catch (Exception e) {
			throw new SerializationException("Error while deserializing object", e);
		}
	}
}
