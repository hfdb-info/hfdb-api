package info.hfdb.hfdbapi;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * Note: With the new command line arguments feature in the webscraper
 * working, it doesn't want to work copy/pasting it to this project
 * without excluding the DataSourceAutoConfigurationClass. Why the exclusion is
 * required here and not in the web scraper is beyond me - Justin Blalock
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class HfdbApiApplication {

	// Used for interacting with the database
	private static JdbcTemplate jdbcTemplate;
	private static Connection connection;

	public static void main(String[] args) {

		if (!parametersCorrect(args))
			return;

		DataSource datasource = setupCredentials(args[0], args[1], args[2], args[3]);
		jdbcTemplate = new JdbcTemplate(datasource);

		// Check to see if can interact with postgres
		try {
			jdbcTemplate.execute("select 'test' as test;");
		} catch (DataAccessException e) {
			System.out.println(
					"Something went wrong with the test sql query. Are your credentials correct? Does the hfdb-database0 database exist?");
			System.out.println("Printing stack trace:");
			e.printStackTrace();
			return;
		}

		try {
			connection = datasource.getConnection();
		} catch (SQLException e) {
			System.out.println("Something went wrong with setting up the connection variable.");
			e.printStackTrace();
			return;
		}

		SpringApplication.run(HfdbApiApplication.class, args);
	}

	/**
	 * Checks to see if the parameters provided through the command line are valid
	 *
	 * @param args The String array containing the command line parameters
	 * @return A boolean whether the parameters are valid or not
	 */
	private static boolean parametersCorrect(String[] args) {

		final String msg = "Please provide the following parameters as such: IPAddress port username password";
		boolean credsReady = true;
		if (args == null || args.length != 4) {
			System.out.println(msg);
			credsReady = false;
		}

		if (!credsReady)
			return false;

		// This is literally useless. Its so the linter will shut up about args being
		// null. If it's actually null, it won't get this far.
		if (args == null)
			args = new String[0];

		// IP Address check
		String[] octets = args[0].split("\\.");

		// Another useless piece of code to make the linter shut up except it applies to
		// octets this time
		if (octets == null)
			octets = new String[0];

		// IP Address check (cont'd)
		boolean ipPass = ((octets != null) && (octets.length == 4));
		for (int i = 0; i < 4 && ipPass; i++) {
			ipPass &= octets[i].matches("^(2[0-4][0-9]|25[0-5]|1[0-9][0-9]|[1-9]?[0-9])$");
		}
		if (!ipPass) {
			System.out.println("The IP address entered is not valid");
			credsReady = false;
		}

		// Port check
		boolean validPort = true;
		validPort &= args[1].matches("^[1-9][0-9]{0,4}$");
		if (validPort) {
			int port = Integer.parseInt(args[1]);
			validPort &= (port > 1 && port < 65536);
		}

		if (!validPort) {
			System.out.println("The port entered is not valid");
			credsReady = false;
		}

		if (!credsReady) {
			System.out.println(msg);
			return false;
		}

		return true;
	}

	/**
	 * Sets up a datasource object for the creation of a JdbcTemplate object using
	 * the provided IP, port, username, and password
	 *
	 * @param ipAddr   IP address of the postgresql instance
	 * @param port     port number of the postgres instance
	 * @param username username that the webscraper is to assume
	 * @param password password of the user
	 * @return A datasource object for the creation of a JdbcTemplate object
	 */
	private static DataSource setupCredentials(String ipAddr, String port, String username, String password) {
		DriverManagerDataSource datasource = new DriverManagerDataSource();
		datasource.setDriverClassName("org.postgresql.Driver");
		datasource.setUrl("jdbc:postgresql://" + ipAddr + ":" + port + "/hfdb-database0");
		datasource.setUsername(username);
		datasource.setPassword(password);
		return datasource;
	}

	public static Connection getConnection() {
		return connection;
	}

	public static JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

}
