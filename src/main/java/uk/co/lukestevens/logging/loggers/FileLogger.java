package uk.co.lukestevens.logging.loggers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import uk.co.lukestevens.logging.LoggerLevel;
import uk.co.lukestevens.logging.models.Log;

/**
 * A logging implementation that logs to a file
 * 
 * @author luke.stevens
 */
public class FileLogger extends AbstractLogger {
	
	final String out;

	/**
	 * Constructs a new FileLogger
	 * @param name The name of the logger
	 * @param minLevel The minimum level this logger should log for
	 * @param out The file to log to
	 */
	public FileLogger(String name, LoggerLevel minLevel, String out) {
		super(name, minLevel);
		this.out = out;
	}

	@Override
	public void log(Log log) {
			try {
				Files.write(
						Paths.get(out),
						(this.getLogOutput(log) + System.lineSeparator()).getBytes(),
						StandardOpenOption.APPEND, StandardOpenOption.CREATE
				);
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

}
