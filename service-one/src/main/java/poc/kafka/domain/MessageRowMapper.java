package poc.kafka.domain;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class MessageRowMapper implements RowMapper<Message> {

	@Override
	public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
		return new Message(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getString(4));
	}

}
