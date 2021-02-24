package uk.co.lukestevens.logging.loggers;

import static org.mockito.Mockito.*;

import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.junit.jupiter.api.Test;


import uk.co.lukestevens.logging.LoggerLevel;
import uk.co.lukestevens.logging.models.Log;

public class ConsoleLoggerTest {
	
	DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Test
	public void testLogToStdOut() throws Exception {
		ConsoleLogger.out = mock(PrintStream.class);
		ConsoleLogger.err = mock(PrintStream.class);
		
		ConsoleLogger logger = new ConsoleLogger("test", LoggerLevel.INFO);
		
		Log log = new Log();
		log.setApplicationName("logging-lib-test");
		log.setApplicationVersion("0.0.1-test");
		log.setMessage("log-message");
		log.setName("ConsoleLoggerTest");
		log.setSeverity(LoggerLevel.INFO);
		log.setTimestamp(df.parse("2021-02-22 19:02:00"));
		
		logger.log(log);
		
		verify(ConsoleLogger.out).println("[INFO]:	2021-02-22 19:02:00	ConsoleLoggerTest	log-message");
		verify(ConsoleLogger.err, never()).println(anyString());
	}
	
	@Test
	public void testLogToStdErr() throws Exception {
		ConsoleLogger.out = mock(PrintStream.class);
		ConsoleLogger.err = mock(PrintStream.class);
		
		ConsoleLogger logger = new ConsoleLogger("test", LoggerLevel.INFO);
		
		Log log = new Log();
		log.setApplicationName("logging-lib-test");
		log.setApplicationVersion("0.0.1-test");
		log.setMessage("log-message");
		log.setName("ConsoleLoggerTest");
		log.setSeverity(LoggerLevel.ERROR);
		log.setTimestamp(df.parse("2021-02-22 19:02:00"));
		
		logger.log(log);
		
		verify(ConsoleLogger.err).println("[ERROR]:	2021-02-22 19:02:00	ConsoleLoggerTest	log-message");
		verify(ConsoleLogger.out, never()).println(anyString());
	}

}
