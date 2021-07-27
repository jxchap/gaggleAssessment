package com.gaggle.candidateassignment;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.gaggle.candidateassignment.domain.Gaggle;

@Component
public class LambdaRequestHandler implements RequestHandler<Map<String, String>, Map<String, Object>> {

	@Override
	public Map<String, Object> handleRequest(Map<String, String> input, Context context) {

		// environment variables from AWS lambda
		String dbUsername = System.getenv("dbUsername");
		String dbPassword = System.getenv("dbPassword");
		String dbUrl = System.getenv("dbUrl");
		String dbDriverClassName = System.getenv("dbDriverClassName");
		
		// variables I had to add manually because I could not figure out how to pass them in
		// to the context object during a unit test
//		String dbUsername = "root";
//		String dbPassword = "password";
//		String dbUrl = "jdbc:mysql://localhost:3306/gaggle";
//		String dbDriverClassName = "com.mysql.cj.jdbc.Driver";

		// respond according to input from lambda caller
		Map<String, Object> returnMap = new HashMap<>();
		if (input.get("searchByName") != null) {

			String temp = null;

			try {
				temp = "%" + input.get("searchByName") + "%";
			} catch (Exception e) {
				returnMap.put("result", "This function takes only a String value.");
				return returnMap;
			}

			DataSource dsCustom = DataSourceBuilder.create().username(dbUsername).password(dbPassword).url(dbUrl)
					.driverClassName(dbDriverClassName).build();

			JdbcTemplate jdcbTemplate = new JdbcTemplate(dsCustom);
			
			String sql = "SELECT * from GAGGLE where firstname like ? OR lastname like ?";

			Stream<Gaggle> gaggleStream = jdcbTemplate.queryForStream(sql, new GaggleMapper(), temp, temp);
			
			List<Gaggle> gaggles = gaggleStream.collect(Collectors.toList());
			
			returnMap.put("result", gaggles);
			return returnMap;

		} else if (input.get("searchById") != null) {

			Integer temp = null;

			try {
				temp = Integer.parseInt(input.get("searchById"));
			} catch (Exception e) {
				returnMap.put("result", "This function takes only an integer value.");
				return returnMap;
			}

			DataSource dsCustom = DataSourceBuilder.create().username(dbUsername).password(dbPassword).url(dbUrl)
					.driverClassName(dbDriverClassName).build();

			JdbcTemplate jdcbTemplate = new JdbcTemplate(dsCustom);
			
			String sql = "SELECT * from GAGGLE where id = ?";

			Gaggle gaggle = jdcbTemplate.queryForObject(sql, new GaggleMapper(), temp);

			returnMap.put("result", gaggle);
			return returnMap;

		} else {

			returnMap.put("result",
					"There are two functions available: searchByName and searchById. To use either, create "
							+ "a simple JSON object using searchByName or searchById as a key, and either a String or integer for the value.");

			return returnMap;

		}

	}

	private static final class GaggleMapper implements RowMapper<Gaggle> {
		public Gaggle mapRow(ResultSet rs, int rowNum) throws SQLException {
			Gaggle gaggle = new Gaggle();
			gaggle.setId(rs.getInt("id"));
			gaggle.setFirstName(rs.getString("firstName"));
			gaggle.setLastName(rs.getString("lastName"));
			return gaggle;

		}
	}

}