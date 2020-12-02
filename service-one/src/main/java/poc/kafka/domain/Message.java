package poc.kafka.domain;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class Message implements Externalizable {
	private long id;
	private String mFrom;
	private String to;
	private String body;

	public Message() {
	}

	public Message(String mFrom, String to, String body) {
		this.mFrom = mFrom;
		this.to = to;
		this.body = body;
	}

	public Message(long id, String mFrom, String to, String body) {
		this.id = id;
		this.mFrom = mFrom;
		this.to = to;
		this.body = body;
	}

	public String getmFrom() {
		return mFrom;
	}

	public void setmFrom(String mFrom) {
		this.mFrom = mFrom;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public long getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Message [id=" + id + ", mFrom=" + mFrom + ", to=" + to + ", body=" + body + "]";
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		this.id = in.readLong();
		this.mFrom = (String) in.readObject();
		this.to = (String) in.readObject();
		this.body = (String) in.readObject();
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeLong(id);
		out.writeObject(mFrom);
		out.writeObject(to);
		out.writeObject(body);
	}

}
