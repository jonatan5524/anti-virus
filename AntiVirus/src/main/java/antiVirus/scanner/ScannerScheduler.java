package antiVirus.scanner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

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

	@Value("${scannerScheduler.waitForList}")
	private int waitForList;

	public ScannerScheduler() {
		analyzeType = new ArrayList<FileAnalyzer>();
	}

	@PostConstruct
	private void onStartUp() {
		// analyzeType.add(virusTotalAnalyzer);
		analyzeType.add(yaraAnalyzer);
		scan();
	}

	@Scheduled(cron = "${scanner.scheduler.cron}")
	private void scan() {
		if (!scanner.isScanning()) {
			scanner.setScanningMethod(new ScanningBFS<FolderDB>());
			taskExecutor.execute(scanner);

			try {
				analyzeFiles();
			} catch (InterruptedException | IOException e) {
				e.printStackTrace();
			}
		}

	}

	private void analyzeFiles() throws InterruptedException, IOException {
		boolean result, totalRes = true;
		List<FileDB> list = scanner.getFileRepo().findAll();
		FileDB temp;

		list = waitForList(list, 0);

		for (int i = 0; scanner.isScanning() || i < list.size(); i++) {

			list = waitForList(list, i);

			temp = list.get(i);
			temp.getResultScan().deserializeResultAnalyzer();
			totalRes = true;

			//System.out.println(temp);
			for (FileAnalyzer type : analyzeType) {
				result = type.scanFile(temp);
				totalRes = totalRes && result;
				temp.getResultScan().getResultAnalyzer().put(type, result);
			}
			temp.getResultScan().setResult(totalRes);
			temp.getResultScan().serializeResultAnalyzer();
			
			scanner.getFileRepo().save(temp);
			System.out.println(temp);

			
		}
	}

	private List<FileDB> waitForList(List<FileDB> list, int index) throws InterruptedException {
		while (scanner.isScanning() && list.size() == index) {
			Thread.sleep(waitForList);
			list = scanner.getFileRepo().findAll();
		}
		return list;
	}

}
