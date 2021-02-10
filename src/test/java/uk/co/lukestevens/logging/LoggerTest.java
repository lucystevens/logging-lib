package uk.co.lukestevens.logging;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.PrintStream;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uk.co.lukestevens.db.DatabaseResult;
import uk.co.lukestevens.logging.Logger;
import uk.co.lukestevens.logging.LoggerLevel;
import uk.co.lukestevens.logging.loggers.ConsoleLogger;
import uk.co.lukestevens.logging.loggers.DatabaseLogger;
import uk.co.lukestevens.logging.loggers.FileLogger;
import uk.co.lukestevens.logging.models.Log;
import uk.co.lukestevens.logging.models.LogBuilder;
import uk.co.lukestevens.testing.mocks.DateMocker;
import uk.co.lukestevens.mocks.MockLogger;
import uk.co.lukestevens.mocks.WriterOutputStream;
import uk.co.lukestevens.testing.db.TestDatabase;

public class LoggerTest {
	
	static final Date CURRENT_DATE = new Date();
	
	final String errorRegex = "\\[ERROR\\]:\\t[0-9/: ]+\\ttest\\tThis is an error\\s*";
	final String warningRegex = "\\[WARNING\\]:\\t[0-9/: ]+\\ttest\\tThis is a warning\\s*";
	final String debugRegex = "\\[DEBUG\\]:\\t[0-9/: ]+\\ttest\\tThis is for debug\\s*";
	final String infoRegex = "\\[INFO\\]:\\t[0-9/: ]+\\ttest\\tThis is some info\\s*";

	
	@AfterAll
	@BeforeAll
	public static void clearLogFile() throws IOException {
		Path logFile = Paths.get("test.log");
		Files.deleteIfExists(logFile);
	}
	
	TestDatabase db;
	
	@BeforeEach
	public void setup() throws IOException, SQLException {
		DateMocker.setCurrentDate(CURRENT_DATE);
		this.db = new TestDatabase();
		this.db.executeFile("setup");
	}

	@Test
	public void testLogException() {
		MockLogger logger = new MockLogger("test", LoggerLevel.INFO);
		IOException e = new IOException("Could not connect to database");
		logger.error(e);
		
		Log log = logger.getLastLog();
		assertEquals("IOException: Could not connect to database", log.getMessage());
		assertEquals("test", log.getName());
		assertEquals(LoggerLevel.ERROR, log.getSeverity());
		assertEquals(CURRENT_DATE, log.getTimestamp());
	}
	
	@Test
	public void testBelowMinSeverity() {
		MockLogger logger = new MockLogger("test", LoggerLevel.ERROR);
		logger.info("Test message");
		
		Log log = logger.getLastLog();
		assertNull(log);
	}
	
	@Test
	public void testConsoleLogger() throws Exception {
		
		StringWriter outString = new StringWriter();
		StringWriter errString = new StringWriter();
		
		try(
			PrintStream outStream = new PrintStream(new WriterOutputStream(outString));
			PrintStream errStream = new PrintStream(new WriterOutputStream(errString))
			){
			
			// Hack the printstream fields
			Field outField = ConsoleLogger.class.getDeclaredField("out");
			outField.setAccessible(true);
			outField.set(null, outStream);
			
			Field errField = ConsoleLogger.class.getDeclaredField("err");
			errField.setAccessible(true);
			errField.set(null, errStream);
			
			Logger logger = new ConsoleLogger("test", LoggerLevel.INFO);
			
			logger.error("This is an error");
			logger.warning("This is a warning");
			logger.debug("This is for debug");
			logger.info("This is some info");
			
			String err = errString.toString();
			String out = outString.toString();
			
			assertTrue(err.matches(errorRegex + warningRegex));
			assertTrue(out.matches(infoRegex));
		}	
	}
	
	@Test
	public void testFileLogger() throws IOException {
		Logger logger = new FileLogger("test", LoggerLevel.WARNING, "test.log");
		
		logger.error("This is an error");
		logger.warning("This is a warning");
		logger.debug("This is for debug");
		logger.info("This is some info");
		
		Path outFile = Paths.get("test.log");
		Files.exists(outFile);
		
		List<String> lines = Files.readAllLines(outFile);
		assertEquals(2, lines.size());
		assertTrue(lines.get(0).matches(errorRegex));
		assertTrue(lines.get(1).matches(warningRegex));
	}
	
	@Test
	public void testDatabaseLogger() throws IOException, SQLException {
		Logger logger = new DatabaseLogger(db, "logging-lib-test", "0.0.1-TEST", "test", LoggerLevel.DEBUG);
		assertNotNull(logger);
		assertEquals(DatabaseLogger.class, logger.getClass());
		
		logger.error("This is an error");
		logger.warning("This is a warning");
		logger.debug("This is for debug");
		logger.info("This is some info");
		
		List<Log> logs;
		try(DatabaseResult dbr = db.query("SELECT * from core.logs")){
			logs = dbr.parseResultSet(rs -> 
				LogBuilder.withId(rs.getInt("id"))
					.applicationName(rs.getString("application_name"))
					.applicationVersion(rs.getString("application_version"))
					.name(rs.getString("logger_name"))
					.message(rs.getString("message"))
					.severity(rs.getString("severity"))
					.timestamp(rs.getDate("timestamp"))
					.build()
				);
		}
		assertEquals(4, logs.size());
		
		Log error = logs.get(0);
		assertEquals(1, error.getId());
		assertEquals("This is an error", error.getMessage());
		assertEquals("test", error.getName());
		assertEquals(LoggerLevel.ERROR, error.getSeverity());
		assertEquals("logging-lib-test", error.getApplicationName());
		assertEquals("0.0.1-TEST", error.getApplicationVersion());
		
		assertEquals(CURRENT_DATE, error.getTimestamp());
		
		Log warning = logs.get(1);
		assertEquals(2, warning.getId());
		assertEquals("This is a warning", warning.getMessage());
		assertEquals("test", warning.getName());
		assertEquals(LoggerLevel.WARNING, warning.getSeverity());
		assertEquals("logging-lib-test", warning.getApplicationName());
		assertEquals("0.0.1-TEST", error.getApplicationVersion());
		assertEquals(CURRENT_DATE, error.getTimestamp());
		
		Log debug = logs.get(2);
		assertEquals(3, debug.getId());
		assertEquals("This is for debug", debug.getMessage());
		assertEquals("test", debug.getName());
		assertEquals(LoggerLevel.DEBUG, debug.getSeverity());
		assertEquals("logging-lib-test", debug.getApplicationName());
		assertEquals("0.0.1-TEST", debug.getApplicationVersion());
		assertEquals(CURRENT_DATE, error.getTimestamp());
		
		Log info = logs.get(3);
		assertEquals(4, info.getId());
		assertEquals("This is some info", info.getMessage());
		assertEquals("test", info.getName());
		assertEquals(LoggerLevel.INFO, info.getSeverity());
		assertEquals("logging-lib-test", info.getApplicationName());
		assertEquals("0.0.1-TEST", info.getApplicationVersion());
		assertEquals(CURRENT_DATE, info.getTimestamp());
	}

}
