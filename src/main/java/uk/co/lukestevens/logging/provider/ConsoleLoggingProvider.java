package uk.co.lukestevens.logging.provider;

import java.util.function.Supplier;

import javax.inject.Inject;

import uk.co.lukestevens.logging.Logger;
import uk.co.lukestevens.logging.LoggerLevel;
import uk.co.lukestevens.logging.LoggingProvider;
import uk.co.lukestevens.logging.loggers.ConsoleLogger;

/**
 * A LoggingProvider that gets a ConsoleLogger
 * 
 * @author Luke Stevens
 */
public class ConsoleLoggingProvider implements LoggingProvider {
	
	final Supplier<LoggerLevel> loggerLevel;

	public ConsoleLoggingProvider(LoggerLevel loggerLevel) {
		this(() -> loggerLevel);
	}
	
	@Inject
	public ConsoleLoggingProvider(Supplier<LoggerLevel> loggerLevel) {
		this.loggerLevel = loggerLevel;
	}

	@Override
	public Logger getLogger(String name) {
		return new ConsoleLogger(name, loggerLevel);
	}

}
