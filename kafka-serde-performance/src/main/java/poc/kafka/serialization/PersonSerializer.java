package poc.kafka.serialization;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import poc.kafka.domain.Person;

public class PersonSerializer implements Serializer<Person> {

	@Override
	public byte[] serialize(String topic, Person data) {
		if (data == null)
			return null;

		try (ByteArrayOutputStream bos = new ByteArrayOutputStream(); ObjectOutput out = new ObjectOutputStream(bos)) {
			data.writeExternal(out);
			return bos.toByteArray();
		} catch (Exception e) {
			throw new SerializationException("Error while serializing object", e);
		}
	}
}
