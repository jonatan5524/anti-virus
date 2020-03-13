package AntiVirus.Scanner;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.Timer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import AntiVirus.Analyzer.FileAnalyzer;
import AntiVirus.Analyzer.VirusTotalAnalyzer;
import AntiVirus.FileFolderHandler.FileFolderScanner;
import AntiVirus.FileFolderHandler.entities.FolderDB;

@Service
public class ScannerScheduler {

	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	private TaskExecutor taskExecutor;
	@Autowired
	private AutowireCapableBeanFactory factory;
	@Autowired
	private FileFolderScanner scanner;

	private Collection<FileAnalyzer> scannerType;
	private final int DELAY_SEC = 30;

	public ScannerScheduler() {
		scannerType = new ArrayList<FileAnalyzer>();
		scannerType.add(new VirusTotalAnalyzer());
	}

	@Scheduled(fixedDelay = DELAY_SEC * 10000)
	private void scan() {
		if(!scanner.isScanning())
			taskExecutor.execute(scanner);
		
	}

	private void forceStop() {

	}

}
