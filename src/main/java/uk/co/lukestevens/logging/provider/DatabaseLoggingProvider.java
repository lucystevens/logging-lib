package uk.co.lukestevens.logging.provider;

import java.util.function.Supplier;

import javax.inject.Inject;

import uk.co.lukestevens.config.ApplicationProperties;
import uk.co.lukestevens.db.Database;
import uk.co.lukestevens.logging.Logger;
import uk.co.lukestevens.logging.LoggerLevel;
import uk.co.lukestevens.logging.LoggingProvider;
import uk.co.lukestevens.logging.loggers.DatabaseLogger;

/**
 * A LoggingProvider that gets a DatabaseLogger
 * 
 * @author Luke Stevens
 */
public class DatabaseLoggingProvider implements LoggingProvider {
	
	final Database db;
	final ApplicationProperties appProperties;
	final Supplier<LoggerLevel> loggerLevel;
	
	public DatabaseLoggingProvider(Database db, ApplicationProperties appProperties, LoggerLevel loggerLevel) {
		this(db, appProperties, () -> loggerLevel);
	}
	
	@Inject
	public DatabaseLoggingProvider(Database db, ApplicationProperties appProperties, Supplier<LoggerLevel> loggerLevel) {
		this.db = db;
		this.appProperties = appProperties;
		this.loggerLevel = loggerLevel;
	}

	@Override
	public Logger getLogger(String name) {
		return new DatabaseLogger(db, appProperties.getApplicationName(), appProperties.getApplicationVersion(), name, loggerLevel);
	}

}
