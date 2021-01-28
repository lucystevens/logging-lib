package uk.co.lukestevens.logging.provider;

import javax.inject.Inject;

import uk.co.lukestevens.logging.Logger;
import uk.co.lukestevens.logging.LoggerLevel;
import uk.co.lukestevens.logging.LoggingProvider;
import uk.co.lukestevens.logging.loggers.ConsoleLogger;

public class ConsoleLoggingProvider implements LoggingProvider {
	
	final LoggerLevel loggerLevel;
	
	@Inject
	public ConsoleLoggingProvider(LoggerLevel loggerLevel) {
		this.loggerLevel = loggerLevel;
	}

	@Override
	public Logger getLogger(String name) {
		return new ConsoleLogger(name, loggerLevel);
	}

}
