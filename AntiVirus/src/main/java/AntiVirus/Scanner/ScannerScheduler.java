package AntiVirus.Scanner;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import AntiVirus.Analyzer.FileAnalyzer;
import AntiVirus.Analyzer.VirusTotalAnalyzer;
import AntiVirus.Scanner.FileFolderHandler.FileFolderScanner;
import AntiVirus.Scanner.FileFolderHandler.ScanningAlgorithem.ScanningBFS;

@Service
public class ScannerScheduler {

	@Autowired
	private TaskExecutor taskExecutor;
	@Autowired
	private FileFolderScanner scanner;

	private Collection<FileAnalyzer> scannerType;

	public ScannerScheduler() {
		scannerType = new ArrayList<FileAnalyzer>();
		scannerType.add(new VirusTotalAnalyzer());
	}
	
	@PostConstruct
	private void onStartUp()
	{
		scan();
	}

	@Scheduled(cron = "${scanner.scheduler.cron}")
	private void scan() {
		if (!scanner.isScanning()) {
			scanner.setScanningMethod(new ScanningBFS());
			taskExecutor.execute(scanner);

		}

	}

	private void forceStop() {

	}

}
