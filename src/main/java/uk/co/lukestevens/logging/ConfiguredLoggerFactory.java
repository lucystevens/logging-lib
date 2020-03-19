package uk.co.lukestevens.logging;

import uk.co.lukestevens.config.Config;
import uk.co.lukestevens.logging.loggers.ConsoleLogger;
import uk.co.lukestevens.logging.loggers.DatabaseLogger;
import uk.co.lukestevens.logging.loggers.FileLogger;
import uk.co.lukestevens.jdbc.ConfiguredDatabase;
import uk.co.lukestevens.jdbc.Database;

/**
 * A static manager class used for getting the correct logger
 * 
 * @author luke.stevens
 */
public class ConfiguredLoggerFactory implements LoggerFactory {
	
	// A provider class to get a Logger from it's name
	private static interface LoggerProvider {
		Logger get(String name);
	}
	
	private LoggerProvider provider = (name) -> new ConsoleLogger(name, LoggerLevel.INFO);
	
	/**
	 * Create a new Logger factory using the application config.
	 * @param config The application config to load the logger
	 */
	public ConfiguredLoggerFactory(Config config) {
		String loggerType = config.getAsStringOrDefault("logger.type", "console");
		LoggerLevel minLevel = config.getParsedValueOrDefault("logger.level", LoggerLevel::valueOf, LoggerLevel.INFO);
		
		if(loggerType.equalsIgnoreCase("file")) {
			String loggerOut = config.getAsStringOrDefault("logger.file", "output.log");
			provider = (name) -> new FileLogger(name, minLevel, loggerOut);
		}
		else if(loggerType.equalsIgnoreCase("database")) {
			String appName = config.getApplicationName();
			String version = config.getApplicationVersion();
			String dbAlias = config.getAsStringOrDefault("logger.db.alias", "logger");
			Database db = new ConfiguredDatabase(config, dbAlias);
		
			provider = (name) -> new DatabaseLogger(db, appName, version, name, minLevel);
		}
		else {
			provider = (name) -> new ConsoleLogger(name, minLevel);
		}
	}
	
	/**
	 * @param name The logger name
	 * @return A logger for the given name
	 */
	@Override
	public Logger getLogger(String name) {
		return provider.get(name);
	}
	
	/**
	 * @param c The calling class
	 * @return A logger for the given class name
	 */
	@Override
	public Logger getLogger(Class<?> c) {
		return getLogger(c.getSimpleName());
	}

}
