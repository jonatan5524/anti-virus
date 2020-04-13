package antiVirus.scanner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.TaskExecutor;

import antiVirus.analyzer.Analyzer;
import antiVirus.analyzer.FileAnalyzer;
import antiVirus.entities.FolderDB;
import antiVirus.exceptions.AntiVirusException;
import antiVirus.logger.loggerManager;
import antiVirus.scanner.fileFolderHandler.FileFolderScanner;
import antiVirus.scanner.fileFolderHandler.scanningAlgorithem.ScanningBFS;
import lombok.Getter;

public abstract class ScannerAnalyzerInitializer {
	
	@Getter
	protected Logger logger;
	
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
	
	// ranked from worst to best by time and performance
	protected Collection<FileAnalyzer> analyzeType;
	
	public ScannerAnalyzerInitializer() {
		analyzeType = new ArrayList<FileAnalyzer>();
		logger = Logger.getLogger(ScannerScheduler.class.getName());
		isActiveScanning = false;
		try {
			loggerPath = loggerManager.setUpLogger(logger);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void startScan()
	{
		isActiveScanning = true;
		fileFolderScanner.setScanningMethod(new ScanningBFS<FolderDB>());
		taskExecutor.execute(fileFolderScanner);

		try {
			Thread.sleep(50);
			analyzer.analyzeFiles();
		} catch (AntiVirusException | InterruptedException e) {
			e.printStackTrace();
		}
		isActiveScanning = false;
	}
}
