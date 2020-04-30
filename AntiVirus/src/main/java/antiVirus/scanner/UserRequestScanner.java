package antiVirus.scanner;

import java.util.Collection;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import antiVirus.analyzer.Analyzer;
import antiVirus.analyzer.FileAnalyzer;
import antiVirus.exceptions.AntiVirusException;
import antiVirus.exceptions.AntiVirusScanningException;
import antiVirus.exceptions.AntiVirusUserException;
import antiVirus.utils.Utils;

@Component
public class UserRequestScanner extends ScannerAnalyzerInitializer {

	private String initDirectoryPath;
	@Value("${Scanning.notSetInitDirectoryPath}")
	private String notSetInitDirectoryPath;
	@Autowired
	@Qualifier("userAnalyzeTypeList")
	// ranked from worst to best by time and performance
	private Collection<FileAnalyzer> analyzeTypeList;
	
	public UserRequestScanner() {
		logger = Logger.getLogger(UserRequestScanner.class.getName());

	}

	@PostConstruct
	private void onStartUp() {
		analyzer = applicationContext.getBean(Analyzer.class, fileFolderScanner, analyzeTypeList);
		analyzer.setLogger(logger);
		initDirectoryPath = notSetInitDirectoryPath;
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

	public void startRequestedScan() throws AntiVirusException {
		if (initDirectoryPath.equals(notSetInitDirectoryPath))
			throw new AntiVirusUserException("init directory path is not set for user scan");
		Utils.emptyFile(loggerPath);
		logger.info("scan started at init scanning Directory: " + initDirectoryPath);
		super.startScan();
	}

}
