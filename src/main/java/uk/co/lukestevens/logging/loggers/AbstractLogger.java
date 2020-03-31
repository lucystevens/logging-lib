package uk.co.lukestevens.logging.loggers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import uk.co.lukestevens.logging.Logger;
import uk.co.lukestevens.logging.LoggerLevel;
import uk.co.lukestevens.logging.models.Log;
import uk.co.lukestevens.utils.Dates;

/**
 * An Abstract Logger that defines most of the
 * simple operations needed for the logger, leaving
 * only the final implementation to be overridden
 * 
 * @author luke.stevens
 */
public abstract class AbstractLogger implements Logger {

	final DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	final String name;
	final LoggerLevel minLevel;
	
	/**
	 * Constructs this AbstractLogger
	 * @param name The name of the logger
	 * @param minLevel The minimum level this logger should log for
	 */
	protected AbstractLogger(String name, LoggerLevel minLevel) {
		this.name = name;
		this.minLevel = minLevel;
	}

	@Override
	public void info(String message) {
		this.log(message, LoggerLevel.INFO);
	}

	@Override
	public void info(Exception e) {
		this.log(e, LoggerLevel.INFO);
	}

	@Override
	public void debug(String message) {
		this.log(message, LoggerLevel.DEBUG);
	}

	@Override
	public void debug(Exception e) {
		this.log(e, LoggerLevel.DEBUG);
	}

	@Override
	public void warning(String message) {
		this.log(message, LoggerLevel.WARNING);
	}

	@Override
	public void warning(Exception e) {
		this.log(e, LoggerLevel.WARNING);
	}

	@Override
	public void error(String message) {
		this.log(message, LoggerLevel.ERROR);
	}

	@Override
	public void error(Exception e) {
		this.log(e, LoggerLevel.ERROR);
	}

	@Override
	public void log(Exception e, LoggerLevel level) {
		this.log(e.getClass().getSimpleName() + ": " + e.getMessage(), level);
	}
	
	@Override
	public void log(String message, LoggerLevel level) {
		if(level.value() >= minLevel.value()) {
			Log log = new Log();
			log.setName(name);
			log.setMessage(message);
			log.setSeverity(level);
			log.setTimestamp(Dates.now());
			
			this.log(log);
		}
	}
	
	/**
	 * Logs a log object. The level should have already been checked by this point
	 * @param log The log to log
	 */
	protected abstract void log(Log log);
	
	/**
	 * Gets the output to be written to the log
	 * @param log The log to be logged
	 * @return A String to be logged, containing the level, message
	 * logger name, and timestamp
	 */
	String getLogOutput(Log log) {
		String format = "[%s]:\t%s\t%s\t%s";
		return String.format(format, log.getSeverity().toString(), df.format(log.getTimestamp()), log.getName(), log.getMessage());
	}

}
