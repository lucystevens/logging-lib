package uk.co.lukestevens.logging.provider;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import uk.co.lukestevens.config.ApplicationProperties;
import uk.co.lukestevens.db.Database;
import uk.co.lukestevens.logging.Logger;
import uk.co.lukestevens.logging.LoggerLevel;
import uk.co.lukestevens.logging.LoggingProvider;
import uk.co.lukestevens.logging.loggers.DatabaseLogger;

public class DatabaseLoggingProviderTest {
	
	@Test
	public void testGetLoggerFromClass() {
		Database db = mock(Database.class);
		ApplicationProperties appProperties = mock(ApplicationProperties.class);
		LoggingProvider provider = new DatabaseLoggingProvider(db, appProperties, () -> LoggerLevel.INFO);
				
		Logger logger = provider.getLogger(DatabaseLoggingProviderTest.class);
		assertTrue(logger instanceof DatabaseLogger);
		
		verify(appProperties).getApplicationName();
		verify(appProperties).getApplicationVersion();
	}
	
	@Test
	public void testGetLoggerFromString() {
		Database db = mock(Database.class);
		ApplicationProperties appProperties = mock(ApplicationProperties.class);
		LoggingProvider provider = new DatabaseLoggingProvider(db, appProperties, LoggerLevel.INFO);
				
		Logger logger = provider.getLogger("String");
		assertTrue(logger instanceof DatabaseLogger);
		
		verify(appProperties).getApplicationName();
		verify(appProperties).getApplicationVersion();
	}

}
