package antiVirus.analyzer;

import java.util.Collection;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Value;

import antiVirus.analyzer.hashAnalyzer.VirusTotalAnalyzer;

import antiVirus.entities.FileDB;
import antiVirus.entities.resultScanStatus;
import antiVirus.exceptions.AntiVirusAnalyzeException;
import antiVirus.exceptions.AntiVirusException;
import antiVirus.repositories.FileRepo;
import antiVirus.scanner.fileFolderHandler.FileFolderScanner;
import antiVirus.utils.Utils;

public class Analyzer {

	private FileFolderScanner fileFolderScanner;

	// ranked from worst to best by time and performance
	private Collection<FileAnalyzer> analyzeType;

	@Value("${analyzer.waitForDbRepoSleep}")
	private int waitForDbRepoSleepDuration;

	private String initScanningDirectory;

	public Analyzer(FileFolderScanner fileFolderScanner, Collection<FileAnalyzer> analyzeType) {
		this.fileFolderScanner = fileFolderScanner;
		this.analyzeType = analyzeType;
		this.initScanningDirectory = fileFolderScanner.getInitScanningDirectory();
	}

	public void analyzeFiles() throws AntiVirusAnalyzeException {
		if (analyzeType == null)
			throw new AntiVirusAnalyzeException("analyzeType is not set before starting the analyze!");

		FileDB tempFileDB = null;

		try {
			System.out.println("init: " + initScanningDirectory);
			List<FileDB> DBlist = getDBListFromRepo();

			DBlist = waitForDbRepo(DBlist, 0);

			for (int i = 0; fileFolderScanner.isFileFolderScannerActive() || i < DBlist.size(); i++) {

				tempFileDB = DBlist.get(i);
				if (tempFileDB.getResultScan().getResult() == null) {
					analyzeFileUpdateResultScan(tempFileDB);
				}
				DBlist = waitForDbRepo(DBlist, i + 1);
			}

		} catch (AntiVirusException | InterruptedException e) {
			throw new AntiVirusAnalyzeException("exception during analyze file: " + tempFileDB, e);
		}
	}

	private void analyzeFileUpdateResultScan(FileDB tempFileDB) throws AntiVirusException {
		int analyzeCounter = 0;

		tempFileDB.getResultScan().deserializeResultAnalyzer();

		analyzeCounter = analyzeSingleFile(tempFileDB);

		tempFileDB.getResultScan().serializeResultAnalyzer();
		tempFileDB.getResultScan().setResult(resultScanStatus.values()[analyzeCounter]);

		fileFolderScanner.getFileRepo().save(tempFileDB);
		
		if (analyzeCounter == 2) {
			System.out.println("virus found!! " + tempFileDB.getPath());
			new Scanner(System.in).nextLine();
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
		DBlist = getDBListFromRepo();

		while (needToWaitForList(index)) {
			Thread.sleep(waitForDbRepoSleepDuration);

			DBlist = getDBListFromRepo();
		}
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
