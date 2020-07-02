package poc.kafka.serialization;

import java.io.ByteArrayInputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import poc.kafka.domain.Person;

public class PersonDeserializer implements Deserializer<Person> {

	@Override
	public Person deserialize(String topic, byte[] data) {
		try (ByteArrayInputStream bis = new ByteArrayInputStream(data); ObjectInput in = new ObjectInputStream(bis)) {
			Person p = new Person();
			p.readExternal(in);
			return p;
		} catch (Exception e) {
			throw new SerializationException("Error while deserializing object", e);
		}
	}
}
