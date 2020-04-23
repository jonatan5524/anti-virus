package antiVirus.scanner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import antiVirus.analyzer.Analyzer;
import antiVirus.analyzer.FileAnalyzer;
import antiVirus.analyzer.hashAnalyzer.MalShareAnalyzer;
import antiVirus.analyzer.hashAnalyzer.VirusTotalAnalyzer;
import antiVirus.analyzer.yaraAnalyzer.YaraAnalyzer;
import antiVirus.entities.FolderDB;
import antiVirus.exceptions.AntiVirusException;
import antiVirus.logger.loggerManager;
import antiVirus.scanner.fileFolderHandler.scanningAlgorithem.ScanningBFS;


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
	private void scheludeScan() {
		
		if (!fileFolderScanner.isFileFolderScannerActive()) {
			logger.info("scedule scan started!");
			super.startScan();
		}

	}

}
