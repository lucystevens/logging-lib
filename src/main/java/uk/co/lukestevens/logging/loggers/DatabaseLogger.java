package uk.co.lukestevens.logging.loggers;

import java.io.IOException;
import java.sql.SQLException;

import uk.co.lukestevens.logging.LoggerLevel;
import uk.co.lukestevens.logging.models.Log;
import uk.co.lukestevens.jdbc.Database;
import uk.co.lukestevens.jdbc.result.DatabaseKey;

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
	 * @param dao The dao to use to write Logs
	 * @param app The application
	 * @param version The application version
	 * @param name The name of the logger
	 * @param minLevel The minimum level this logger should log for
	 */
	public DatabaseLogger(Database db, String app, String version, String name, LoggerLevel minLevel) {
		super(name, minLevel);
		this.db = db;
		this.app = app;
		this.version = version;
	}

	@Override
	public void log(Log log) {
		try(DatabaseKey key = db.update(SQL, app, version, log.getName(), log.getMessage(), log.getSeverity().name(), log.getTimestamp())) {
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
	}


}
