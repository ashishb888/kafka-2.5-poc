package poc.kafka;
import java.nio.ByteBuffer;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class ByteBufferUsage {

	public static void main(String[] args) {
		ByteBuffer bb = ByteBuffer.allocate(40);
		int p1 = 0;
		int p2 = 10;
		int len = 10;
		long l = System.currentTimeMillis();
		Date d = new Date(l);
		Timestamp t = new Timestamp(l);

		bb.position(p1);// Setting position manually
		bb.put("Hello".getBytes()); // Putting strings
		// bb.position(p2);
		// bb.put("There".getBytes());
		bb.putInt(20, 10);
		// bb.putLong(24, d.getTime()); // Putting dates
		// bb.putLong(32, t.getTime()); // Putting timestamps

		System.out.println(getStringFromByteBuffer(bb, p1, len)); // Getting strings
		System.out.println(getStringFromByteBuffer(bb, p2, len));
		System.out.println(bb.getInt(20));
		System.out.println(new Date(bb.getLong(24))); // Getting dates
		System.out.println(new Timestamp(bb.getLong(32))); // Getting timestamps

		System.out.println(toDate(bb.getLong(24))); // Formatting date to a custom format
		System.out.println(toTimestamp(bb.getLong(32))); // Formatting timestamp to a custom format

		// System.out.println(new Timestamp(2314885392840523776L));
		// System.out.println(toTimestamp(2314885392840523776L));

	}

	static DateTimeFormatter timestampFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	private static Timestamp toTimestamp(long l) {
		if (l <= 0)
			return null;

		return Timestamp.valueOf(
				timestampFormatter.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(l), ZoneId.systemDefault())));
	}

	private static Date toDate(long l) {
		if (l <= 0)
			return null;

		return Date.valueOf(dateFormatter.format(Instant.ofEpochMilli(l).atZone(ZoneId.systemDefault()).toLocalDate()));
	}

	private static String getStringFromByteBuffer(ByteBuffer byteBuffer, int offset, int length) {
		byte[] bArr = new byte[length];
		byteBuffer.position(offset);
		byteBuffer.get(bArr, 0, length);
		System.out.println("length: " + bArr.length);

		String s = new String(bArr);

		if (s != null && s.trim().isEmpty())
			return null;

		return s == null ? s : s.trim();
	}

}
