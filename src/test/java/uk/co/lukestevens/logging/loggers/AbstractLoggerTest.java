package uk.co.lukestevens.logging.loggers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.jupiter.api.Test;

import uk.co.lukestevens.logging.Logger;
import uk.co.lukestevens.logging.LoggerLevel;
import uk.co.lukestevens.logging.models.Log;
import uk.co.lukestevens.mocks.MockLogger;
import uk.co.lukestevens.testing.mocks.DateMocker;

public class AbstractLoggerTest {
	
	DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	Logger getMockedLogger() {
		Logger logger = mock(AbstractLogger.class, CALLS_REAL_METHODS);
		doNothing().when(logger).log(anyString(), any());
		return logger;
	}
	
	@Test
	public void testLogStringAsError() {
		Logger logger = getMockedLogger();
		logger.error("error message");
		verify(logger).log("error message", LoggerLevel.ERROR);
	}
	
	@Test
	public void testLogExceptionAsError() {
		Logger logger = getMockedLogger();
		logger.error(new IOException("error message"));
		verify(logger).log("IOException: error message", LoggerLevel.ERROR);
	}
	
	@Test
	public void testLogStringAsWarning() {
		Logger logger = getMockedLogger();
		logger.warning("warning message");
		verify(logger).log("warning message", LoggerLevel.WARNING);
	}
	
	@Test
	public void testLogExceptionAsWarning() {
		Logger logger = getMockedLogger();
		logger.warning(new SQLException("warning message"));
		verify(logger).log("SQLException: warning message", LoggerLevel.WARNING);
	}
	
	@Test
	public void testLogStringAsInfo() {
		Logger logger = getMockedLogger();
		logger.info("info message");
		verify(logger).log("info message", LoggerLevel.INFO);
	}
	
	@Test
	public void testLogExceptionAsInfo() {
		Logger logger = getMockedLogger();
		logger.info(new NullPointerException("info message"));
		verify(logger).log("NullPointerException: info message", LoggerLevel.INFO);
	}
	
	@Test
	public void testLogStringAsDebug() {
		Logger logger = getMockedLogger();
		logger.debug("debug message");
		verify(logger).log("debug message", LoggerLevel.DEBUG);
	}
	
	@Test
	public void testLogExceptionAsDebug() {
		Logger logger = getMockedLogger();
		logger.debug(new NullPointerException("debug message"));
		verify(logger).log("NullPointerException: debug message", LoggerLevel.DEBUG);
	}
	
	@Test
	public void testLogWhenLevelIsBelowMinimum() throws ParseException {
		DateMocker.setCurrentDate(df.parse("2021-02-23 18:41:00"));
		MockLogger logger = new MockLogger("AbstractLoggerTest", LoggerLevel.INFO);
		logger.log("debug message", LoggerLevel.DEBUG);
		
		Log log = logger.getLastLog();
		assertNull(log);	
	}
	
	@Test
	public void testLogWhenLevelIsMinimum() throws ParseException {
		DateMocker.setCurrentDate(df.parse("2021-02-23 18:41:00"));
		MockLogger logger = new MockLogger("AbstractLoggerTest", LoggerLevel.INFO);
		logger.log("info message", LoggerLevel.INFO);
		
		Log log = logger.getLastLog();
		assertEquals("AbstractLoggerTest", log.getName());
		assertEquals(LoggerLevel.INFO, log.getSeverity());
		assertEquals("info message", log.getMessage());
		assertEquals(df.parse("2021-02-23 18:41:00"), log.getTimestamp());
	}
	
	@Test
	public void testLogWhenLevelIsAboveMinimum() throws ParseException {
		DateMocker.setCurrentDate(df.parse("2021-02-23 18:41:00"));
		MockLogger logger = new MockLogger("AbstractLoggerTest", LoggerLevel.INFO);
		logger.log("error message", LoggerLevel.ERROR);
		
		Log log = logger.getLastLog();
		assertEquals("AbstractLoggerTest", log.getName());
		assertEquals(LoggerLevel.ERROR, log.getSeverity());
		assertEquals("error message", log.getMessage());
		assertEquals(df.parse("2021-02-23 18:41:00"), log.getTimestamp());
	}
	
	@Test
	public void testLoggerLevels() throws ParseException {
		assertTrue(LoggerLevel.DEBUG.value() < LoggerLevel.INFO.value());
		assertTrue(LoggerLevel.INFO.value() < LoggerLevel.WARNING.value());
		assertTrue(LoggerLevel.WARNING.value() < LoggerLevel.ERROR.value());
	}

}
