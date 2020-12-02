package poc.kafka.domain;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class MessageStatus implements Externalizable {
	private long messageId;
	private String status;

	public MessageStatus() {
	}

	public MessageStatus(long messageId, String status) {
		this.messageId = messageId;
		this.status = status;
	}

	public long getMessageId() {
		return messageId;
	}

	public void setMessageId(long messageId) {
		this.messageId = messageId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		this.messageId = in.readLong();
		this.status = (String) in.readObject();
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeLong(messageId);
		out.writeObject(status);
	}

	@Override
	public String toString() {
		return "MessageStatus [messageId=" + messageId + ", status=" + status + "]";
	}

}
