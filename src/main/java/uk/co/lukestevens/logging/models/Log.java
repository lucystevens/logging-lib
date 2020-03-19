package uk.co.lukestevens.logging.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import uk.co.lukestevens.logging.LoggerLevel;
import uk.co.lukestevens.models.IEntity;

/**
 * A simple object class to represent a Log to
 * be written to the database
 * 
 * @author luke.stevens
 */
@Entity
@Table(name = "logs", schema= "core")
public class Log extends IEntity{
	
	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private Integer id;
	
	@Column(name = "application_name")
	private String applicationName;
	
	@Column(name = "application_version")
	private String applicationVersion;
	
	@Column(name = "logger_name")
	private String name;
	
	@Column(name = "message")
	private String message;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "severity")
	private LoggerLevel severity;
	
	@Column(name = "timestamp")
	private Date timestamp;
	
	/**
	 * @return The log id
	 */
	@Override
	public Integer getId() {
		return id;
	}
	
	@Override
	public String toString() {
		return super.toString() + this.name;
	}
	
	/**
	 * @return The unique name for the application that created this log
	 */
	public String getApplicationName() {
		return applicationName;
	}
	
	/**
	 * Sets the unique name for the application that created this log
	 * @param applicationName The unique application name
	 */
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	
	/**
	 * @return The version of the application that created this log
	 */
	public String getApplicationVersion() {
		return applicationVersion;
	}
	
	/**
	 * Sets the version of the application that created this log
	 * @param applicationVersion
	 */
	public void setApplicationVersion(String applicationVersion) {
		this.applicationVersion = applicationVersion;
	}
	
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
