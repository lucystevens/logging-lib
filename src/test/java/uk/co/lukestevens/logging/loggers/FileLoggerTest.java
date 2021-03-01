package uk.co.lukestevens.logging.loggers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import uk.co.lukestevens.logging.LoggerLevel;
import uk.co.lukestevens.logging.models.Log;

public class FileLoggerTest {
	
	DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@AfterEach
	public void clearLogFile() throws IOException {
		Path logFile = Paths.get("test.log");
		Files.deleteIfExists(logFile);
	}
	
	@Test
	public void testLogOnce() throws IOException, ParseException {
		FileLogger logger = new FileLogger("test", () -> LoggerLevel.WARNING, "test.log");
		
		Date date = df.parse("2021-02-22 18:46:00");
		
		Log log = new Log();
		log.setMessage("log-message");
		log.setName("FileLoggerTest");
		log.setSeverity(LoggerLevel.ERROR);
		log.setTimestamp(date);
		
		logger.log(log);
		
		Path outFile = Paths.get("test.log");
		Files.exists(outFile);
		
		List<String> lines = Files.readAllLines(outFile);
		assertEquals(1, lines.size());
		assertEquals(lines.get(0), "[ERROR]:	2021-02-22 18:46:00	FileLoggerTest	log-message");
	}
	
	@Test
	public void testLogMultiple() throws IOException, ParseException {
		FileLogger logger = new FileLogger("test", () -> LoggerLevel.WARNING, "test.log");
		
		Log log1 = new Log();
		log1.setMessage("log-message-one");
		log1.setName("FileLoggerTest");
		log1.setSeverity(LoggerLevel.DEBUG);
		log1.setTimestamp(df.parse("2021-02-22 18:46:00"));
		
		logger.log(log1);
		
		Log log2 = new Log();
		log2.setMessage("log-message-two");
		log2.setName("FileLoggerTest");
		log2.setSeverity(LoggerLevel.WARNING);
		log2.setTimestamp(df.parse("2021-02-22 18:50:00"));
		
		logger.log(log2);
		
		Path outFile = Paths.get("test.log");
		Files.exists(outFile);
		
		List<String> lines = Files.readAllLines(outFile);
		assertEquals(2, lines.size());
		assertEquals(lines.get(0), "[DEBUG]:	2021-02-22 18:46:00	FileLoggerTest	log-message-one");
		assertEquals(lines.get(1), "[WARNING]:	2021-02-22 18:50:00	FileLoggerTest	log-message-two");
	}

}
