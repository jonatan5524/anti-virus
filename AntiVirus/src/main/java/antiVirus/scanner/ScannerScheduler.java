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
import javax.swing.JOptionPane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import antiVirus.analyzer.FileAnalyzer;
import antiVirus.analyzer.hashAnalyzer.MalShareAnalyzer;
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

	private boolean scan;

	@Value("${scannerScheduler.waitForList}")
	private int waitForList;

	public ScannerScheduler() {
		analyzeType = new ArrayList<FileAnalyzer>();
		scan = true;
	}

	@PostConstruct
	private void onStartUp() {
		analyzeType.add(yaraAnalyzer); 
		analyzeType.add(malShareAnalyzer);
		analyzeType.add(virusTotalAnalyzer);
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
		boolean result = false;
		int count = 0;
		try {
			List<FileDB> list = scanner.getFileRepo().findAll();

			list = waitForList(list, 0);

			for (int i = 0; scanner.isScanning() || i < list.size(); i++) {

				list = waitForList(list, i);

				temp = list.get(i);

				temp.getResultScan().deserializeResultAnalyzer();
				
				count=0;
				for(FileAnalyzer analyzer : analyzeType)
				{
					if((analyzer instanceof VirusTotalAnalyzer) && count == 0)
						break;
					result = analyzer.scanFile(temp);
					temp.getResultScan().getResultAnalyzer().put(analyzer, result);
					count += result ? 1 : 0;
					if(count==2)
						break;
				}

				temp.getResultScan().serializeResultAnalyzer();
				temp.getResultScan().setResult(count);
				scanner.getFileRepo().save(temp);
				if (count == 2) {
					System.out.println("virus found!! "+temp.getPath());
					new Scanner(System.in).nextLine();
				}
			}

		} catch (AntiVirusException e) {
			throw new AntiVirusAnalyzeException("exception during analyze file: " + temp, e);
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
