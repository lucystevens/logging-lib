package uk.co.lukestevens.logging.provider;

import java.util.function.Supplier;

import javax.inject.Inject;

import uk.co.lukestevens.logging.Logger;
import uk.co.lukestevens.logging.LoggerLevel;
import uk.co.lukestevens.logging.LoggingProvider;
import uk.co.lukestevens.logging.loggers.FileLogger;

/**
 * A LoggingProvider that gets a FileLogger
 * 
 * @author Luke Stevens
 */
public class FileLoggingProvider implements LoggingProvider {
	
	final String logFile;
	final Supplier<LoggerLevel> loggerLevel;
	
	public FileLoggingProvider(String logFile, LoggerLevel loggerLevel) {
		this(logFile, () -> loggerLevel);
	}
	
	@Inject
	public FileLoggingProvider(String logFile, Supplier<LoggerLevel> loggerLevel) {
		this.logFile = logFile;
		this.loggerLevel = loggerLevel;
	}

	@Override
	public Logger getLogger(String name) {
		return new FileLogger(name, loggerLevel, logFile);
	}

}
