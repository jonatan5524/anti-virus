package antiVirus.scanner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import antiVirus.analyzer.FileAnalyzer;
import antiVirus.analyzer.hashAnalyzer.VirusTotalAnalyzer;
import antiVirus.analyzer.yaraAnalyzer.YaraAnalyzer;
import antiVirus.entities.FileDB;
import antiVirus.entities.FolderDB;
import antiVirus.entities.ResultScan;
import antiVirus.exceptions.AntiVirusAnalyzeException;
import antiVirus.exceptions.AntiVirusException;
import antiVirus.exceptions.AntiVirusScanningException;
import antiVirus.scanner.fileFolderHandler.FileFolderScanner;
import antiVirus.scanner.fileFolderHandler.scanningAlgorithem.ScanningBFS;

@Component
public class ScannerScheduler {

	@Autowired
	private TaskExecutor taskExecutor;
	@Autowired
	private FileFolderScanner scanner;

	private Collection<FileAnalyzer> analyzeType;
	@Autowired
	private VirusTotalAnalyzer virusTotalAnalyzer;
	@Autowired
	private YaraAnalyzer yaraAnalyzer;

	private boolean scan;

	@Value("${scannerScheduler.waitForList}")
	private int waitForList;

	public ScannerScheduler() {
		analyzeType = new ArrayList<FileAnalyzer>();
		scan = true;
	}

	@PostConstruct
	private void onStartUp() {
		// analyzeType.add(virusTotalAnalyzer);
		analyzeType.add(yaraAnalyzer);
		analyzeType.add(virusTotalAnalyzer);
		scan();
	}

	@Scheduled(cron = "${scanner.scheduler.cron}")
	private void scan() {
		if (scan) {
			if (!scanner.isScanning()) {
				scanner.setScanningMethod(new ScanningBFS<FolderDB>());
				taskExecutor.execute(scanner);

				try {
					analyzeFiles();
				} catch (AntiVirusException | InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}

	private void analyzeFiles() throws InterruptedException, AntiVirusAnalyzeException {
		FileDB temp = null;
		boolean result;
		try {
			List<FileDB> list = scanner.getFileRepo().findAll();

			list = waitForList(list, 0);

			for (int i = 0; scanner.isScanning() || i < list.size(); i++) {

				list = waitForList(list, i);

				temp = list.get(i);

				temp.getResultScan().deserializeResultAnalyzer();
				result = virusTotalAnalyzer.scanFile(temp);
				temp.getResultScan().getResultAnalyzer().put(virusTotalAnalyzer, result);
				
				if (result) {
					result = yaraAnalyzer.scanFile(temp);
					temp.getResultScan().getResultAnalyzer().put(yaraAnalyzer, result);
				}
				temp.getResultScan().setResult(result);
				temp.getResultScan().serializeResultAnalyzer();

				scanner.getFileRepo().save(temp);
				if (result) {
					System.out.println("virus detected! " + temp);
					Scanner scanner = new Scanner(System.in);
					scanner.nextLine();
				}
			}

		} catch (AntiVirusException e) {
			throw new AntiVirusAnalyzeException("exception during analyze file: "+temp,e);
		}
	}

	private List<FileDB> waitForList(List<FileDB> list, int index) throws InterruptedException {
		while (scanner.isScanning() && list.size() == index) {
			Thread.sleep(waitForList);
			list = scanner.getFileRepo().findAll();
		}
		return list;
	}

	public void stopSchedule() {
		scan = false;
	}

	public void resumeSchedule() {
		scan = true;
	}
}
