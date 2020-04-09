package antiVirus.logger;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;


import lombok.Getter;

public class loggerManager {

	public static String setUpLogger(Logger logger) throws IOException {

		File tempFile = File.createTempFile(logger.getName(), ".log");
		String path = tempFile.getPath();
		
		FileHandler fileHandler = new FileHandler(tempFile.getPath());
		logger.addHandler(fileHandler);
		fileHandler.setFormatter(new loggerFormatter());

		return path;
	}

}
