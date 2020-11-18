package uk.co.lukestevens.logging.provider;

import javax.inject.Inject;

import uk.co.lukestevens.config.ApplicationProperties;
import uk.co.lukestevens.jdbc.Database;
import uk.co.lukestevens.logging.Logger;
import uk.co.lukestevens.logging.LoggerLevel;
import uk.co.lukestevens.logging.LoggingProvider;
import uk.co.lukestevens.logging.loggers.DatabaseLogger;

public class DatabaseLoggingProvider implements LoggingProvider {
	
	final Database db;
	final ApplicationProperties appProperties;
	
	@Inject
	public DatabaseLoggingProvider(Database db, ApplicationProperties appProperties) {
		this.db = db;
		this.appProperties = appProperties;
	}

	@Override
	public Logger getLogger(String name) {
		return new DatabaseLogger(db, appProperties.getApplicationName(), appProperties.getApplicationVersion(), name, LoggerLevel.INFO);
	}

}
