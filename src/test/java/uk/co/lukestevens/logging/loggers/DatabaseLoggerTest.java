package uk.co.lukestevens.logging.loggers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uk.co.lukestevens.db.Database;
import uk.co.lukestevens.db.DatabaseResult;
import uk.co.lukestevens.logging.LoggerLevel;
import uk.co.lukestevens.logging.models.Log;
import uk.co.lukestevens.testing.db.TestDatabase;

public class DatabaseLoggerTest {
	
	DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	TestDatabase db;
	
	@BeforeEach
	public void setup() throws IOException, SQLException {
		this.db = new TestDatabase();
		this.db.executeFile("setup");
	}
	
	@Test
	public void testLog() throws IOException, SQLException, ParseException {
		DatabaseLogger logger = new DatabaseLogger(db, "logging-lib-test", "0.0.1-test", null, LoggerLevel.DEBUG);
		
		Log log = new Log();
		log.setMessage("log-message");
		log.setName("DatabaseLoggerTest");
		log.setSeverity(LoggerLevel.ERROR);
		log.setTimestamp(df.parse("2021-02-22 19:21:00"));
		
		logger.log(log);
		
		try(DatabaseResult dbr = db.query("SELECT * from core.logs")){
			ResultSet rs = dbr.getResultSet();
			assertTrue(rs.next());
			
			assertEquals(1, rs.getInt("id"));
			assertEquals("logging-lib-test", rs.getString("application_name"));
			assertEquals("0.0.1-test", rs.getString("application_version"));
			assertEquals("DatabaseLoggerTest", rs.getString("logger_name"));
			assertEquals("log-message", rs.getString("message"));
			assertEquals("ERROR", rs.getString("severity"));
			assertEquals(df.parse("2021-02-22 19:21:00"), rs.getTimestamp("timestamp"));
			
			assertFalse(rs.next());

		}
	}
	
	@Test
	public void testLogWhenThrowsSQLException() throws SQLException, ParseException {
		Date date = df.parse("2021-02-22 19:24:00");
		SQLException exception = mock(SQLException.class);
		Database mockDb = mock(Database.class);
		when(mockDb.update(
				"INSERT INTO core.logs(application_name, application_version, logger_name, message, severity, timestamp) VALUES (?,?,?,?,?,?)",
				"logging-lib-test",
				"0.0.1-test",
				"DatabaseLoggerTest",
				"log-message",
				"ERROR",
				date
				)).thenThrow(exception);
		
		DatabaseLogger logger = new DatabaseLogger(mockDb, "logging-lib-test", "0.0.1-test", null, LoggerLevel.DEBUG);
		
		Log log = new Log();
		log.setMessage("log-message");
		log.setName("DatabaseLoggerTest");
		log.setSeverity(LoggerLevel.ERROR);
		log.setTimestamp(date);
		
		logger.log(log);
		
		verify(exception).printStackTrace();
		
	}


}
