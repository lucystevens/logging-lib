package uk.co.lukestevens.logging.models;

import java.util.Date;

import uk.co.lukestevens.logging.LoggerLevel;
import uk.co.lukestevens.logging.models.Log;

public class LogBuilder {
	
	public static LogBuilder withId(int id) {
		return new LogBuilder(id);
	}
	
	private final Log log = new Log();
	
	private LogBuilder(int id) {
		this.log.id = id;
	}
	
	public LogBuilder applicationName(String applicationName) {
		this.log.applicationName = applicationName;
		return this;
	}
	
	public LogBuilder applicationVersion(String applicationVersion) {
		this.log.applicationVersion = applicationVersion;
		return this;
	}
	
	public LogBuilder name(String name) {
		this.log.name = name;
		return this;
	}
	
	public LogBuilder message(String message) {
		this.log.message = message;
		return this;
	}
	
	public LogBuilder severity(String severity) {
		this.log.severity = LoggerLevel.valueOf(severity);
		return this;
	}
	
	public LogBuilder timestamp(Date timestamp) {
		this.log.timestamp = timestamp;
		return this;
	}
	
	public Log build() {
		return log;
	}

}
