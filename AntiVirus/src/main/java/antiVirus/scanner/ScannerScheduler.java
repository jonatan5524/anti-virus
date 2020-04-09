package antiVirus.scanner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import antiVirus.analyzer.Analyzer;
import antiVirus.analyzer.FileAnalyzer;
import antiVirus.analyzer.hashAnalyzer.MalShareAnalyzer;
import antiVirus.analyzer.hashAnalyzer.VirusTotalAnalyzer;
import antiVirus.analyzer.yaraAnalyzer.YaraAnalyzer;
import antiVirus.entities.FileDB;
import antiVirus.entities.FolderDB;
import antiVirus.exceptions.AntiVirusAnalyzeException;
import antiVirus.exceptions.AntiVirusException;
import antiVirus.logger.loggerManager;
import antiVirus.scanner.fileFolderHandler.FileFolderScanner;
import antiVirus.scanner.fileFolderHandler.scanningAlgorithem.ScanningBFS;
import lombok.Getter;

@Component
public class ScannerScheduler {

	@Getter
	private Logger logger;
	
	@Getter
	private String loggerPath;
	
	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private FileFolderScanner fileFolderScanner;

	@Autowired
	private TaskExecutor taskExecutor;

	@Autowired
	@Getter
	private Analyzer analyzer;

	// ranked from worst to best by time and performance
	private Collection<FileAnalyzer> analyzeType;

	public ScannerScheduler() {
		analyzeType = new ArrayList<FileAnalyzer>();
		logger = Logger.getLogger(ScannerScheduler.class.getName());
		try {
			loggerPath = loggerManager.setUpLogger(logger);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@PostConstruct
	private void onStartUp() {
		analyzeType.add(applicationContext.getBean(YaraAnalyzer.class));
		logger.info("analyzeType: Yara analyzer");
		analyzeType.add(applicationContext.getBean(MalShareAnalyzer.class));
		logger.info("analyzeType: MalShare analyzer");
		analyzeType.add(applicationContext.getBean(VirusTotalAnalyzer.class));
		logger.info("analyzeType: VirusTotal analyzer");
		analyzer = applicationContext.getBean(Analyzer.class, fileFolderScanner, analyzeType);
		analyzer.setLogger(logger);
	}

	@Scheduled(cron = "${scanner.scheduler.cron}")
	private void scheludeScan() {
		if (!fileFolderScanner.isFileFolderScannerActive()) {
			logger.info("scedule scan started!");
			fileFolderScanner.setScanningMethod(new ScanningBFS<FolderDB>());
			taskExecutor.execute(fileFolderScanner);

			try {
				Thread.sleep(50);
				analyzer.analyzeFiles();
			} catch (AntiVirusException | InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}
