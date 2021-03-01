package uk.co.lukestevens.logging.loggers;

import java.sql.SQLException;
import java.util.function.Supplier;

import uk.co.lukestevens.logging.LoggerLevel;
import uk.co.lukestevens.logging.models.Log;
import uk.co.lukestevens.db.Database;

/**
 * A logger implementation that logs messages
 * to a database
 * 
 * @author luke.stevens
 */
public class DatabaseLogger extends AbstractLogger {
	
	private String SQL = "INSERT INTO core.logs(application_name, application_version, logger_name, message, severity, timestamp) VALUES (?,?,?,?,?,?)";
	
	private final Database db;
	private final String app;
	private final String version;

	/**
	 * Creates a new Database Logger
	 * @param db The database to use to write Logs
	 * @param app The application name
	 * @param version The application version
	 * @param name The name of the logger
	 * @param minLevel The minimum level this logger should log for
	 */
	public DatabaseLogger(Database db, String app, String version, String name, Supplier<LoggerLevel> minLevel) {
		super(name, minLevel);
		this.db = db;
		this.app = app;
		this.version = version;
	}

	@Override
	public void log(Log log) {
		try {
			db.update(
					SQL,
					app,
					version,
					log.getName(),
					log.getMessage(),
					log.getSeverity().name(),
					log.getTimestamp()
				);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


}
