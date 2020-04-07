package antiVirus.scanner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
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
	private TaskExecutor taskExecutor;
	@Autowired
	private FileFolderScanner fileFolderScanner;

	// ranked from worst to best by time and performance
	private Collection<FileAnalyzer> analyzeType;
	// best analysis but limited to 4 requests per minute and 1k per day
	@Autowired
	private VirusTotalAnalyzer virusTotalAnalyzer;
	// least good analysis but always available
	@Autowired
	private YaraAnalyzer yaraAnalyzer;
	// good analysis but limited to 2000 requests per day
	@Autowired
	private MalShareAnalyzer malShareAnalyzer;

	private boolean needToRunScheduleScan;

	@Value("${scannerScheduler.waitForDbRepoSleep}")
	private int waitForDbRepoSleepDuration;

	public ScannerScheduler() {
		analyzeType = new ArrayList<FileAnalyzer>();
		needToRunScheduleScan = true;
	}

	@PostConstruct
	private void onStartUp() {
		analyzeType.add(yaraAnalyzer);
		analyzeType.add(malShareAnalyzer);
		analyzeType.add(virusTotalAnalyzer);
	}

	@Scheduled(cron = "${scanner.scheduler.cron}")
	private void scheludeScan() {
		if (needToRunScheduleScan) {
			if (!fileFolderScanner.isFileFolderScannerActive()) {
				fileFolderScanner.setScanningMethod(new ScanningBFS<FolderDB>());
				taskExecutor.execute(fileFolderScanner);

				try {
					analyzeFiles();
				} catch (AntiVirusException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void analyzeFiles() throws AntiVirusAnalyzeException {
		FileDB tempFileDB = null;
		int analyzeCounter = 0;

		try {
			List<FileDB> DBlist = fileFolderScanner.getFileRepo().findAll();

			DBlist = waitForDbRepo(DBlist, 0);

			for (int i = 0; fileFolderScanner.isFileFolderScannerActive() || i < DBlist.size(); i++) {

				DBlist = waitForDbRepo(DBlist, i);

				tempFileDB = DBlist.get(i);

				tempFileDB.getResultScan().deserializeResultAnalyzer();

				analyzeCounter = analyzeSingleFile(tempFileDB);

				tempFileDB.getResultScan().serializeResultAnalyzer();
				tempFileDB.getResultScan().setResult(analyzeCounter);
				fileFolderScanner.getFileRepo().save(tempFileDB);

				if (analyzeCounter == 2) {
					System.out.println("virus found!! " + tempFileDB.getPath());
					new Scanner(System.in).nextLine();
				}
			}

		} catch (AntiVirusException | InterruptedException e) {
			throw new AntiVirusAnalyzeException("exception during analyze file: " + tempFileDB, e);
		}
	}

	private int analyzeSingleFile(FileDB tempFileDB) throws AntiVirusException {
		int analyzeCounter = 0;
		boolean result;
		for (FileAnalyzer analyzer : analyzeType) {
			if ((analyzer instanceof VirusTotalAnalyzer) && analyzeCounter == 0)
				break;
			result = analyzer.scanFile(tempFileDB);
			tempFileDB.getResultScan().getResultAnalyzer().put(analyzer, result);
			analyzeCounter += result ? 1 : 0;
			if (analyzeCounter == 2)
				break;
		}

		return analyzeCounter;
	}

	private List<FileDB> waitForDbRepo(List<FileDB> DBlist, int index) throws InterruptedException {
		while (fileFolderScanner.isFileFolderScannerActive() && DBlist.size() == index) {
			Thread.sleep(waitForDbRepoSleepDuration);
			DBlist = fileFolderScanner.getFileRepo().findAll();
		}
		return DBlist;
	}

	public void stopSchedule() {
		needToRunScheduleScan = false;
	}

	public void resumeSchedule() {
		needToRunScheduleScan = true;
	}
}
