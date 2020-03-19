package uk.co.lukestevens.logging.loggers;

import java.io.PrintStream;

import uk.co.lukestevens.logging.LoggerLevel;
import uk.co.lukestevens.logging.models.Log;

/**
 * A logger implementation that simply logs to the console
 * 
 * @author luke.stevens
 */
public class ConsoleLogger extends AbstractLogger {
	
	private static PrintStream out = System.out;
	private static PrintStream err = System.err;
	
	/**
	 * Create a new ConsoleLogger
	 * @param name The name of the logger
	 * @param minLevel The minimum level this logger should log for
	 */
	public ConsoleLogger(String name, LoggerLevel minLevel) {
		super(name, minLevel);
	}


	@Override
	protected void log(Log log) {
		PrintStream out = this.getStream(log.getSeverity());
		out.println(this.getLogOutput(log));
	}
	
	/**
	 * @param severity The severity to log
	 * @return The PrintStream to log to
	 */
	protected PrintStream getStream(LoggerLevel severity) {
		return severity.value() > 2? err : out;
	}

}
