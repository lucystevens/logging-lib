package uk.co.lukestevens.logging.models;

import java.util.Date;

import uk.co.lukestevens.logging.LoggerLevel;

/**
 * A simple object class to represent a Log to
 * be written to the database
 * 
 * @author luke.stevens
 */
public class Log{
	
	String name;
	String message;
	LoggerLevel severity;
	Date timestamp;
	
	/**
	 * @return The logger name, usually the class the log was created in
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name of the logger that created this log
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return The message to log
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * Sets the message to log
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	/**
	 * @return The severity level of this log
	 */
	public LoggerLevel getSeverity() {
		return severity;
	}
	
	/**
	 * Sets the severity level of this log
	 * @param severity
	 */
	public void setSeverity(LoggerLevel severity) {
		this.severity = severity;
	}
	
	/**
	 * @return The time at which this was logged
	 */
	public Date getTimestamp() {
		return timestamp;
	}
	
	/**
	 * Sets the time at which this was logged
	 * @param timestamp
	 */
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	
	

}
