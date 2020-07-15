package uk.co.lukestevens.logging;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
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
import java.util.Properties;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uk.co.lukestevens.config.Config;
import uk.co.lukestevens.config.ConfigManager;
import uk.co.lukestevens.encryption.EncryptionService;
import uk.co.lukestevens.encryption.IgnoredEncryptionService;
import uk.co.lukestevens.hibernate.Dao;
import uk.co.lukestevens.hibernate.HibernateController;
import uk.co.lukestevens.logging.Logger;
import uk.co.lukestevens.logging.LoggerLevel;
import uk.co.lukestevens.logging.loggers.ConsoleLogger;
import uk.co.lukestevens.logging.loggers.DatabaseLogger;
import uk.co.lukestevens.logging.loggers.FileLogger;
import uk.co.lukestevens.logging.models.Log;
import uk.co.lukestevens.testing.mocks.MockConfigSource;
import uk.co.lukestevens.mocks.MockLogger;
import uk.co.lukestevens.mocks.WriterOutputStream;
import uk.co.lukestevens.testing.db.TestDatabase;

public class LoggerTest {
	
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
	
	static Config config;
	TestDatabase db;
	
	@BeforeAll
	public static void loadConfig() throws IOException {
		EncryptionService encryption = new IgnoredEncryptionService();
		File configFile = new File("src/test/resources/conf/test.conf");
		ConfigManager configManager = new ConfigManager(configFile, encryption);
		config = configManager.getAppConfig();
	}
	
	@BeforeEach
	public void setup() throws IOException, SQLException {
		this.db = new TestDatabase();
		this.db.executeFile("setup");
	}

	
	// Checks that date is less than 10 seconds before now
	public static void assertDateIsNow(Date date) {
		long millisToCheck = date.getTime();
		long millisNow = System.currentTimeMillis();
		assertTrue(millisToCheck > millisNow - 10000, "Date is more than 10 seconds ago");
		assertTrue(millisToCheck <= millisNow, "Date is in the future");
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
		assertDateIsNow(log.getTimestamp());
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
		
		Properties props = new Properties();
		props.setProperty("logger.type", "CONSOLE");
		props.setProperty("logger.level", "DEBUG");
		
		Config config = new MockConfigSource(props);
		
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
		
			LoggerFactory factory = new ConfiguredLoggerFactory(config);
			
			Logger logger = factory.getLogger("test");
			assertNotNull(logger);
			assertEquals(ConsoleLogger.class, logger.getClass());
			
			logger.error("This is an error");
			logger.warning("This is a warning");
			logger.debug("This is for debug");
			logger.info("This is some info");
			
			String err = errString.toString();
			String out = outString.toString();
			
			assertTrue(err.matches(errorRegex + warningRegex));
			assertTrue(out.matches(debugRegex));
		}	
	}
	
	@Test
	public void testFileLogger() throws IOException {
		Properties props = new Properties();
		props.setProperty("logger.type", "FILE");
		props.setProperty("logger.level", "WARNING");
		props.setProperty("logger.file", "test.log");
		Config config = new MockConfigSource(props);
		
		LoggerFactory factory = new ConfiguredLoggerFactory(config);
		
		Logger logger = factory.getLogger("test");
		assertNotNull(logger);
		assertEquals(FileLogger.class, logger.getClass());
		
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
	public void testDatabaseLogger() throws IOException {
		LoggerFactory factory = new ConfiguredLoggerFactory(config);
		
		Logger logger = factory.getLogger("test");
		assertNotNull(logger);
		assertEquals(DatabaseLogger.class, logger.getClass());
		
		logger.error("This is an error");
		logger.warning("This is a warning");
		logger.debug("This is for debug");
		logger.info("This is some info");
		
		HibernateController hibernate = new HibernateController(config);
		Dao<Log> dao = hibernate.getDao(Log.class);
		
		List<Log> logs = dao.list();
		assertEquals(4, logs.size());
		
		Log error = logs.get(0);
		assertEquals(1, error.getId());
		assertEquals("This is an error", error.getMessage());
		assertEquals("test", error.getName());
		assertEquals(LoggerLevel.ERROR, error.getSeverity());
		assertEquals("logging-lib-test", error.getApplicationName());
		assertEquals("0.0.1-TEST", error.getApplicationVersion());
		
		assertDateIsNow(error.getTimestamp());
		
		Log warning = logs.get(1);
		assertEquals(2, warning.getId());
		assertEquals("This is a warning", warning.getMessage());
		assertEquals("test", warning.getName());
		assertEquals(LoggerLevel.WARNING, warning.getSeverity());
		assertEquals("logging-lib-test", warning.getApplicationName());
		assertEquals("0.0.1-TEST", error.getApplicationVersion());
		assertDateIsNow(warning.getTimestamp());
		
		Log debug = logs.get(2);
		assertEquals(3, debug.getId());
		assertEquals("This is for debug", debug.getMessage());
		assertEquals("test", debug.getName());
		assertEquals(LoggerLevel.DEBUG, debug.getSeverity());
		assertEquals("logging-lib-test", debug.getApplicationName());
		assertEquals("0.0.1-TEST", debug.getApplicationVersion());
		assertDateIsNow(debug.getTimestamp());
		
		Log info = logs.get(3);
		assertEquals(4, info.getId());
		assertEquals("This is some info", info.getMessage());
		assertEquals("test", info.getName());
		assertEquals(LoggerLevel.INFO, info.getSeverity());
		assertEquals("logging-lib-test", info.getApplicationName());
		assertEquals("0.0.1-TEST", info.getApplicationVersion());
		assertDateIsNow(info.getTimestamp());
	}

}
