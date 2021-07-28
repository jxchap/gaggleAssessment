package com.gaggle.candidateassignment.service;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.gaggle.candidateassignment.LambdaRequestHandler;
import com.gaggle.candidateassignment.domain.Person;

@Service
public class DynamicDatabaseService {

	private static JdbcTemplate createJDBCTemplate() {

		// environment variables from AWS lambda
		String dbUsername = System.getenv("dbUsername");
		String dbPassword = System.getenv("dbPassword");
		String dbUrl = System.getenv("dbUrl");
		String dbDriverClassName = System.getenv("dbDriverClassName");

		// haven't figured out how to create a mock context object, providing default
		// values for in-memory db
		if (dbUsername == null || dbPassword == null || dbUrl == null || dbDriverClassName == null) {
			Properties prop = new Properties();

			try {
				prop.load(LambdaRequestHandler.class.getClassLoader().getResourceAsStream("application.properties"));
			} catch (IOException ex) {
				ex.printStackTrace();
			}

			dbUsername = prop.getProperty("spring.datasource.username");
			dbPassword = prop.getProperty("spring.datasource.password");
			dbUrl = prop.getProperty("spring.datasource.url");
			dbDriverClassName = prop.getProperty("spring.datasource.driverClassName");
		}

		DataSource dsCustom = DataSourceBuilder.create().username(dbUsername).password(dbPassword).url(dbUrl)
				.driverClassName(dbDriverClassName).build();

		return new JdbcTemplate(dsCustom);
	}

	public List<Person> getListOfPersons(String personName) {

		JdbcTemplate jdbcTemplate = createJDBCTemplate();

		String sql = "SELECT * from PERSON where firstname like ? OR lastname like ?";

		Stream<Person> personStream = jdbcTemplate.queryForStream(sql, new PersonMapper(), personName, personName);

		return personStream.collect(Collectors.toList());

	}

	public Person getOnePerson(Integer personId) {

		JdbcTemplate jdbcTemplate = createJDBCTemplate();

		String sql = "SELECT * from PERSON where id = ?";

		return jdbcTemplate.queryForObject(sql, new PersonMapper(), personId);

	}

	private final class PersonMapper implements RowMapper<Person> {
		public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
			Person person = new Person();
			person.setId(rs.getInt("id"));
			person.setFirstname(rs.getString("firstname"));
			person.setLastname(rs.getString("lastname"));
			return person;

		}
	}

}
