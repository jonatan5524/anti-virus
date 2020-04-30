package antiVirus.logger;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import lombok.Getter;

public class LoggerManager {

	@Value("${logger.extension}")
	private String loggerExtension;
	@Autowired
	private LoggerFormatter loggerFormatter;
	
	public String setUpLogger(Logger logger) throws IOException {

		File tempFile = File.createTempFile(logger.getName(), loggerExtension);
		String path = tempFile.getPath();
		
		FileHandler fileHandler = new FileHandler(tempFile.getPath());
		logger.addHandler(fileHandler);
		fileHandler.setFormatter(loggerFormatter);

		return path;
	}

}
