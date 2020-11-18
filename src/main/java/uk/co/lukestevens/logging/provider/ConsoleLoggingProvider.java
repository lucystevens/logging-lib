package uk.co.lukestevens.logging.provider;

import uk.co.lukestevens.logging.Logger;
import uk.co.lukestevens.logging.LoggerLevel;
import uk.co.lukestevens.logging.LoggingProvider;
import uk.co.lukestevens.logging.loggers.ConsoleLogger;

public class ConsoleLoggingProvider implements LoggingProvider {
	
	@Override
	public Logger getLogger(String name) {
		return new ConsoleLogger(name, LoggerLevel.INFO);
	}

}
