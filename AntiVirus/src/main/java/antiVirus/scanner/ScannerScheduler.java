package antiVirus.scanner;

import java.util.Collection;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import antiVirus.analyzer.Analyzer;
import antiVirus.analyzer.FileAnalyzer;
import antiVirus.exceptions.AntiVirusException;
import antiVirus.utils.Utils;


@Component
public class ScannerScheduler extends ScannerAnalyzerInitializer{
	
	@Autowired
	@Qualifier("scannerSchedulerAnalyzeTypeList")
	// ranked from worst to best by time and performance
	private Collection<FileAnalyzer> analyzeTypeList;
	
	public ScannerScheduler() {
		logger = Logger.getLogger(ScannerScheduler.class.getName());
	}
	
	@PostConstruct
	private void onStartUp() {
		analyzer = applicationContext.getBean(Analyzer.class, fileFolderScanner, analyzeTypeList);
		analyzer.setLogger(logger);
	}

	@Scheduled(cron = "${scanner.scheduler.cron}")
	private void scheludeScan() throws AntiVirusException {
		
		if (!fileFolderScanner.isFileFolderScannerActive()) {
			Utils.emptyFile(loggerPath);
			logger.info("scedule scan started!");
			super.startScan();
		}

	}

}
