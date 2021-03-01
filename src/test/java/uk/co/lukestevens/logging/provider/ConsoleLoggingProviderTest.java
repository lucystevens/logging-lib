package uk.co.lukestevens.logging.provider;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import uk.co.lukestevens.logging.Logger;
import uk.co.lukestevens.logging.LoggerLevel;
import uk.co.lukestevens.logging.LoggingProvider;
import uk.co.lukestevens.logging.loggers.ConsoleLogger;

public class ConsoleLoggingProviderTest {
	
	@Test
	public void testGetLoggerFromClass() {
		LoggingProvider provider = new ConsoleLoggingProvider(() -> LoggerLevel.INFO);
		Logger logger = provider.getLogger(ConsoleLoggingProviderTest.class);
		assertTrue(logger instanceof ConsoleLogger);
	}
	
	@Test
	public void testGetLoggerFromString() {
		LoggingProvider provider = new ConsoleLoggingProvider(LoggerLevel.INFO);
		Logger logger = provider.getLogger("String");
		assertTrue(logger instanceof ConsoleLogger);
	}

}
