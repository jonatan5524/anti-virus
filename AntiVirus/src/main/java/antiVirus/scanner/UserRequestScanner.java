package antiVirus.scanner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import antiVirus.exceptions.AntiVirusAnalyzeException;
import antiVirus.exceptions.AntiVirusException;
import antiVirus.exceptions.AntiVirusScanningException;
import antiVirus.exceptions.AntiVirusUserException;
import antiVirus.logger.loggerManager;
import antiVirus.scanner.fileFolderHandler.FileFolderScanner;
import antiVirus.scanner.fileFolderHandler.scanningAlgorithem.ScanningBFS;
import lombok.Getter;

@Component
public class UserRequestScanner extends ScannerAnalyzerInitializer {

	private String initDirectoryPath;
	@Autowired
	@Qualifier("userAnalyzeTypeList")
	// ranked from worst to best by time and performance
	private Collection<FileAnalyzer> analyzeTypeList;
	
	public UserRequestScanner() {
		initDirectoryPath = "";
		logger = Logger.getLogger(UserRequestScanner.class.getName());

	}

	@PostConstruct
	private void onStartUp() {
		analyzer = applicationContext.getBean(Analyzer.class, fileFolderScanner, analyzeTypeList);
		analyzer.setLogger(logger);
	}

	public void setInitDirectoryPath(String initDirectoryPath) throws AntiVirusUserException {
		this.initDirectoryPath = initDirectoryPath;
		try {
			setUpfileFolderInitDirectory();
		} catch (AntiVirusScanningException e) {
			throw new AntiVirusUserException("invalid init directory path: " + initDirectoryPath);
		}

	}

	private void setUpfileFolderInitDirectory() throws AntiVirusScanningException {
		fileFolderScanner.setInitScanningDirectory(initDirectoryPath);
	}

	public void startRequestedScan() throws AntiVirusAnalyzeException, AntiVirusUserException {
		if (initDirectoryPath == "")
			throw new AntiVirusUserException("init directory path is not set for user scan");
		logger.info("scan started at init scanning Directory: " + initDirectoryPath);
		super.startScan();
	}

}
