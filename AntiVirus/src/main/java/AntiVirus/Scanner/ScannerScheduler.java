package AntiVirus.Scanner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import AntiVirus.Analyzer.FileAnalyzer;
import AntiVirus.Analyzer.VirusTotalAnalyzer;
import AntiVirus.Scanner.FileFolderHandler.FileFolderScanner;
import AntiVirus.Scanner.FileFolderHandler.ScanningAlgorithem.ScanningBFS;
import AntiVirus.entities.FileDB;
import AntiVirus.entities.FolderDB;
import AntiVirus.entities.ResultScan;

@Service
public class ScannerScheduler {

	@Autowired
	private TaskExecutor taskExecutor;
	@Autowired
	private FileFolderScanner scanner;

	private Collection<FileAnalyzer> analyzeType;

	public ScannerScheduler() {
		analyzeType = new ArrayList<FileAnalyzer>();
		analyzeType.add(new VirusTotalAnalyzer());
	}

	@PostConstruct
	private void onStartUp() {
		scan();
	}

	@Scheduled(cron = "${scanner.scheduler.cron}")
	private void scan() {
		if (!scanner.isScanning()) {
			scanner.setScanningMethod(new ScanningBFS<FolderDB>());
			taskExecutor.execute(scanner);

			try {
				analyzeFiles();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	private void analyzeFiles() throws InterruptedException {
		boolean result, totalRes = true;
		List<FileDB> list = scanner.getFileRepo().findAll();
		FileDB temp;
		while (scanner.isScanning() && list.size() == 0) {
			Thread.sleep(5);
			list = scanner.getFileRepo().findAll();
		}

		for (int i = 0; scanner.isScanning() || i < list.size(); i++) {
			while (scanner.isScanning() && list.size() == i) {
				Thread.sleep(5);
				list = scanner.getFileRepo().findAll();
			}

			temp = list.get(i);
			totalRes = true;
			temp.setResultScan(new ResultScan());
			//System.out.println(temp);
			for (FileAnalyzer type : analyzeType) {
				result = type.scanFile(temp);
				totalRes = totalRes && result;
				temp.getResultScan().getResultAnalyzer().put(type, result);
			}
			temp.getResultScan().setResult(totalRes);
		//	System.out.println(temp);
		}
	}

	private void forceStop() {

	}

}
