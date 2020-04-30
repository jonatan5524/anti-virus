package antiVirus.scanner;

import java.io.IOException;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.TaskExecutor;

import antiVirus.analyzer.Analyzer;
import antiVirus.exceptions.AntiVirusAnalyzeException;
import antiVirus.exceptions.AntiVirusException;
import antiVirus.logger.LoggerManager;
import antiVirus.scanner.fileFolderHandler.FileFolderScanner;
import lombok.Getter;

public abstract class ScannerAnalyzerInitializer {
	
	@Getter
	protected Logger logger;
	
	@Autowired
	private LoggerManager loggerManager;
	
	@Getter
	protected String loggerPath;
	
	@Autowired
	protected ApplicationContext applicationContext;

	@Autowired
	protected FileFolderScanner fileFolderScanner;

	@Autowired
	protected TaskExecutor taskExecutor;

	@Autowired
	@Getter
	protected Analyzer analyzer;

	@Getter
	protected boolean isActiveScanning;
	
	@Value("${ScannerAnalyzerInitializer.waitBeforeStartAnalyze}")
	private int waitBeforeStartAnalyze;
	
	public ScannerAnalyzerInitializer() {
		isActiveScanning = false;
	}
	
	@PostConstruct
	private void setUpLogFile() {
		try {
			loggerPath = loggerManager.setUpLogger(logger);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void startScan()
	{
		isActiveScanning = true;
		taskExecutor.execute(fileFolderScanner);

		try {
			Thread.sleep(waitBeforeStartAnalyze);
			
			analyzer.startAnalyzingFiles();
		} catch (AntiVirusAnalyzeException | InterruptedException e) {
			e.printStackTrace();
		}
		isActiveScanning = false;
	}
}
