package antiVirus.analyzer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;

import antiVirus.analyzer.hashAnalyzer.VirusTotalAnalyzer;

import antiVirus.entities.FileDB;
import antiVirus.entities.resultScanStatus;
import antiVirus.exceptions.AntiVirusAnalyzeException;
import antiVirus.exceptions.AntiVirusException;
import antiVirus.repositories.FileRepo;
import antiVirus.scanner.fileFolderHandler.FileFolderScanner;
import antiVirus.utils.Utils;
import lombok.Getter;
import lombok.Setter;

public class Analyzer {

	@Setter
	@Getter
	private Logger logger;

	private FileFolderScanner fileFolderScanner;
	
	@Value("${analyzer.suspiciousVirusCountLimit}")
	private int suspiciousVirusCountLimit;
	@Value("${analyzer.virusCountLimit}")
	private int virusCountLimit;

	@Getter
	private Collection<String> virusFoundList;
	@Getter
	private Collection<String> virusSuspiciousList;

	// ranked from worst to best by time and performance
	private Collection<FileAnalyzer> analyzeTypeList;

	@Value("${analyzer.waitForDbRepoSleep}")
	private int waitForDbRepoSleepDuration;

	private String initScanningDirectory;

	public Analyzer(FileFolderScanner fileFolderScanner, Collection<FileAnalyzer> analyzeType) {
		this.fileFolderScanner = fileFolderScanner;
		this.analyzeTypeList = analyzeType;
		virusFoundList = new ArrayList<String>();
		virusSuspiciousList = new ArrayList<String>();
	}

	private void emptyVirusesLists() {
		virusFoundList.clear();
		virusSuspiciousList.clear();
	}

	public void startAnalyzingFiles() throws AntiVirusAnalyzeException {
		this.initScanningDirectory = fileFolderScanner.getInitScanningDirectory();
		emptyVirusesLists();
		FileDB tempFileDB = null;

		try {
			List<FileDB> DBlist = getDBListFromRepo();
			
			DBlist = waitForDbRepo(DBlist, 0);
			
			for (int i = 0; fileFolderScanner.isFileFolderScannerActive() || i < DBlist.size(); i++) {

				tempFileDB = DBlist.get(i);
				
				runOnDbRepositoryFileAndAnalyze(tempFileDB);
					
				DBlist = waitForDbRepo(DBlist, i + 1);
			}

		} catch (AntiVirusException | InterruptedException e) {
			throw new AntiVirusAnalyzeException("exception during analyze file: " + tempFileDB, e);
		}
	}

	private void runOnDbRepositoryFileAndAnalyze(FileDB tempFileDB) throws AntiVirusException {
		logger.info("analyzing file: " + tempFileDB.getPath());
		Optional<resultScanStatus> resultScanStatusOptinal = Optional.ofNullable(tempFileDB.getResultScan().getResult());
		if (!resultScanStatusOptinal.isPresent()) {
			analyzeFileUpdateResultScan(tempFileDB);
		}
		else if(resultScanStatusOptinal.get() == resultScanStatus.VIRUS)
		{
			virusFoundList.add(tempFileDB.getPath());
		}
		else if (resultScanStatusOptinal.get() == resultScanStatus.SUSPICIOUS_VIRUS) {
			virusSuspiciousList.add(tempFileDB.getPath());
		}
	}
	
	private void analyzeFileUpdateResultScan(FileDB tempFileDB) throws AntiVirusException {
		int analyzeCounter = 0;

		tempFileDB.getResultScan().deserializeResultAnalyzer();

		analyzeCounter = analyzeSingleFile(tempFileDB);

		tempFileDB.getResultScan().serializeResultAnalyzer();
		tempFileDB.getResultScan().setResult(resultScanStatus.values()[analyzeCounter]);

		fileFolderScanner.getFileRepo().save(tempFileDB);
		if (analyzeCounter >= virusCountLimit) {
			virusFoundList.add(tempFileDB.getPath());
			logger.info("virus found!! " + tempFileDB.getPath());
		} else if (analyzeCounter == suspiciousVirusCountLimit) {
			virusSuspiciousList.add(tempFileDB.getPath());
		}
	}

	private int analyzeSingleFile(FileDB tempFileDB) throws AntiVirusException {
		int analyzeCounter = 0;
		boolean result;
		
		for (FileAnalyzer analyzer : analyzeTypeList) {
			if ((analyzer instanceof VirusTotalAnalyzer) && analyzeCounter == 0)
				return analyzeCounter;
			result = analyzer.scanFile(tempFileDB, logger);
			tempFileDB.getResultScan().getResultAnalyzer().put(analyzer, result);

			analyzeCounter += result ? 1 : 0;

			if (analyzeCounter == virusCountLimit)
				return analyzeCounter;
		}

		return analyzeCounter;
	}

	private List<FileDB> waitForDbRepo(List<FileDB> DBlist, int index) throws InterruptedException {
		DBlist = getDBListFromRepo();

		while (needToWaitForList(index)) {
			Thread.sleep(waitForDbRepoSleepDuration);
		}

		DBlist = getDBListFromRepo();
		return DBlist;
	}

	private boolean needToWaitForList(int index) {
		FileRepo fileRepoTemp = fileFolderScanner.getFileRepo();
		long count;
		if (initScanningDirectory.isEmpty()) {
			count = fileRepoTemp.count();
		} else {
			count = fileRepoTemp.countByPathStartingWith(Utils.convertPathToValidMySQLSearch(initScanningDirectory));
		}

		return fileFolderScanner.isFileFolderScannerActive() && index == count;
	}

	private List<FileDB> getDBListFromRepo() {

		if (initScanningDirectory.isEmpty()) {
			return fileFolderScanner.getFileRepo().findAll();
		} else {
			return fileFolderScanner.getFileRepo()
					.findByPathStartingWith(Utils.convertPathToValidMySQLSearch(initScanningDirectory));
		}

	}
}
