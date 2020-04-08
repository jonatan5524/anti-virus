package antiVirus.scanner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

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
import antiVirus.scanner.fileFolderHandler.FileFolderScanner;
import antiVirus.scanner.fileFolderHandler.scanningAlgorithem.ScanningBFS;

@Component
public class ScannerScheduler {

	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
	private FileFolderScanner fileFolderScanner;
	
	@Autowired
	private TaskExecutor taskExecutor;
	
	@Autowired
	private Analyzer analyzer;
	
	// ranked from worst to best by time and performance
	private Collection<FileAnalyzer> analyzeType;

	private boolean needToRunScheduleScan;
	
	public ScannerScheduler() {
		analyzeType = new ArrayList<FileAnalyzer>();
		needToRunScheduleScan = true;
	}

	@PostConstruct
	private void onStartUp() {
		analyzeType.add(applicationContext.getBean(YaraAnalyzer.class));
		analyzeType.add(applicationContext.getBean(MalShareAnalyzer.class));
		analyzeType.add(applicationContext.getBean(VirusTotalAnalyzer.class));
		analyzer = applicationContext.getBean(Analyzer.class, fileFolderScanner, analyzeType);
	}

	@Scheduled(cron = "${scanner.scheduler.cron}")
	private void scheludeScan() {

		if (needToRunScheduleScan) {
			if (!fileFolderScanner.isFileFolderScannerActive()) {
				fileFolderScanner.setScanningMethod(new ScanningBFS<FolderDB>());
				taskExecutor.execute(fileFolderScanner);

				try {
					Thread.sleep(5);
					analyzer.analyzeFiles();
				} catch (AntiVirusException | InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void stopSchedule() {
		needToRunScheduleScan = false;
		System.out.println("schedule Scanner stoped!");
	}

	public void resumeSchedule() {
		needToRunScheduleScan = true;
		System.out.println("schedule Scanner resumed!");
	}
}
