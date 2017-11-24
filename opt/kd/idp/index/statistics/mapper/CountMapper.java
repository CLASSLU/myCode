package kd.idp.index.statistics.mapper;


import java.sql.ResultSet;
import java.sql.SQLException;


import org.springframework.jdbc.core.RowMapper;

public class CountMapper implements RowMapper<Integer>{

	public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
		Integer c=rs.getInt(1);
		
		
		return c;
}
}
