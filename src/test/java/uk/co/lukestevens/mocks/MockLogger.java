package uk.co.lukestevens.mocks;


import uk.co.lukestevens.logging.LoggerLevel;
import uk.co.lukestevens.logging.loggers.AbstractLogger;
import uk.co.lukestevens.logging.models.Log;

public class MockLogger extends AbstractLogger {
	
	private Log log;

	public MockLogger(String name, LoggerLevel minLevel) {
		super(name, minLevel);
	}

	@Override
	protected void log(Log log) {
		this.log = log;
	}
	
	public Log getLastLog() {
		 return log;
	}

}
