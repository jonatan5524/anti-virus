package antiVirus.scanner;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import antiVirus.analyzer.Analyzer;
import antiVirus.analyzer.FileAnalyzer;
import antiVirus.analyzer.hashAnalyzer.MalShareAnalyzer;
import antiVirus.analyzer.hashAnalyzer.VirusTotalAnalyzer;
import antiVirus.analyzer.yaraAnalyzer.YaraAnalyzer;
import antiVirus.entities.FolderDB;
import antiVirus.exceptions.AntiVirusException;
import antiVirus.exceptions.AntiVirusScanningException;
import antiVirus.scanner.fileFolderHandler.FileFolderScanner;
import antiVirus.scanner.fileFolderHandler.scanningAlgorithem.ScanningBFS;

@Component
public class UserRequestScanner {

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private FileFolderScanner fileFolderScanner;

	@Autowired
	private TaskExecutor taskExecutor;

	private Analyzer analyzer;

	// ranked from worst to best by time and performance
	private Collection<FileAnalyzer> analyzeType;

	private String initDirectoryPath;

	public UserRequestScanner(String initDirectoryPath) {
		analyzeType = new ArrayList<FileAnalyzer>();
		this.initDirectoryPath = initDirectoryPath;

	}

	private void fileFolderInitDirectory(String initDirectory) {
		try {
			fileFolderScanner.setInitScanningDirectory(initDirectory);
		} catch (AntiVirusScanningException e) {
			e.printStackTrace();
		}
	}

	@PostConstruct
	private void onStartUp() {
		fileFolderInitDirectory(initDirectoryPath);
		analyzeType.add(applicationContext.getBean(YaraAnalyzer.class));
		analyzeType.add(applicationContext.getBean(MalShareAnalyzer.class));
		analyzeType.add(applicationContext.getBean(VirusTotalAnalyzer.class));
		analyzer = applicationContext.getBean(Analyzer.class, fileFolderScanner, analyzeType);
	}

	public void startScan() {
		stopSchedule();
		
		fileFolderScanner.setScanningMethod(new ScanningBFS<FolderDB>());
		taskExecutor.execute(fileFolderScanner);
		
		try {
			Thread.sleep(5);
			analyzer.analyzeFiles();
		} catch (AntiVirusException | InterruptedException e) {
			e.printStackTrace();
		}
		
		resumeSchedule();

	}
	
	public void stopSchedule()
	{
		applicationContext.getBean(ScannerScheduler.class).stopSchedule();
	}
	
	public void resumeSchedule()
	{
		applicationContext.getBean(ScannerScheduler.class).resumeSchedule();
	}
}
