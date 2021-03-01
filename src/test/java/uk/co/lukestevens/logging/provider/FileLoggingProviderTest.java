package uk.co.lukestevens.logging.provider;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import uk.co.lukestevens.logging.Logger;
import uk.co.lukestevens.logging.LoggerLevel;
import uk.co.lukestevens.logging.LoggingProvider;
import uk.co.lukestevens.logging.loggers.FileLogger;

public class FileLoggingProviderTest {
	
	@Test
	public void testGetLoggerFromClass() {
		LoggingProvider provider = new FileLoggingProvider("file.log", () -> LoggerLevel.INFO);
		Logger logger = provider.getLogger(FileLoggingProviderTest.class);
		assertTrue(logger instanceof FileLogger);
	}
	
	@Test
	public void testGetLoggerFromString() {
		LoggingProvider provider = new FileLoggingProvider("file.log", LoggerLevel.INFO);
		Logger logger = provider.getLogger("String");
		assertTrue(logger instanceof FileLogger);
	}

}
