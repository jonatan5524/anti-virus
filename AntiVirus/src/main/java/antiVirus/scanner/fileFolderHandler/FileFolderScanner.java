package antiVirus.scanner.fileFolderHandler;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import antiVirus.analyzer.FileAnalyzer;
import antiVirus.entities.FileDB;
import antiVirus.entities.ResultScan;
import antiVirus.exceptions.AntiVirusException;
import antiVirus.exceptions.AntiVirusScanningException;
import antiVirus.repositories.FileRepo;
import antiVirus.scanner.fileFolderHandler.scanningAlgorithem.ScanningAlgorithm;
import antiVirus.utils.Utils;
import lombok.Getter;
import lombok.Setter;

public class FileFolderScanner implements Runnable {

	@Getter
	@Autowired
	private FileRepo fileRepo;

	@Autowired
	@Qualifier("ScanningAlgorithmBFS")
	private ScanningAlgorithm<File> scanningMethod;
	@Getter
	private boolean isFileFolderScannerActive;
	@Value("${file.hashAlgorithm}")
	private String hashAlgorithm;

	private MessageDigest messageDigest;

	@Getter
	private String initScanningDirectory;

	public FileFolderScanner() {
		isFileFolderScannerActive = false;
		this.initScanningDirectory = "";
	}

	@PostConstruct
	private void initMessageDigest() throws AntiVirusException {
		try {
			messageDigest = MessageDigest.getInstance(hashAlgorithm);
		} catch (NoSuchAlgorithmException e) {
			throw new AntiVirusException("Invalid Hashing Algorithem: " + hashAlgorithm, e);
		}
	}

	private void scanFolder(File initDir) throws AntiVirusException {
		File parentDirectoryDB;
		scanningMethod.init();
		scanningMethod.add(initDir);

		while (!scanningMethod.isEmpty()) {

			parentDirectoryDB = scanningMethod.remove();

			Optional<File[]> files = Optional.ofNullable(parentDirectoryDB.listFiles());

			if (files.isPresent()) {
				scanFilesInDir(files.get());
			}
		}
	}

	private void scanFilesInDir(File[] files) throws AntiVirusException {

		for (File file : files) {

			if (file.isDirectory()) {
				if (needToEnterFolder(file.getPath())) {
					scanningMethod.add(file);
				}
			} else {
				if (!fileRepo.existsByPath(file.getPath())) {
					insertFileToDB(file);
				} else {
					changeFileHash(file);
				}
			}
		}
	}

	private void changeFileHash(File file) {

		FileDB fileDBTemp = fileRepo.findByPath(file.getPath());
		String newHash = Utils.getFileChecksum(messageDigest, file);
		if (!newHash.contentEquals(fileDBTemp.getHash())) {
			fileDBTemp.setHash(newHash);
			fileDBTemp.getResultScan().setResult(null);
		}
	}

	private boolean needToEnterFolder(String filePath) {
		return (Utils.isUnix() && filePath != "\\proc" && filePath != "\\sys") || !Utils.isUnix();
	}

	private void insertFileToDB(File file) throws AntiVirusException {

		ResultScan resultScanTemp = new ResultScan();
		resultScanTemp.setResult(null);
		resultScanTemp.setResultAnalyzer(new HashMap<FileAnalyzer, Boolean>());

		resultScanTemp.serializeResultAnalyzer();

		FileDB fileTemp = new FileDB(Utils.getFileChecksum(messageDigest, file), file.getName(), file.getPath());
		resultScanTemp.setFiledb(fileTemp);

		fileTemp.setResultScan(resultScanTemp);

		fileRepo.save(fileTemp);
	}

	private File[] getAllHardDrives() {
		return File.listRoots();
	}

	private void startScanning() {
		isFileFolderScannerActive = true;

		if (initScanningDirectory == "") {
			scanAllFileSystem();
		} else {
			System.out.println("started scan from: " + initScanningDirectory);
			System.out.println("scanning method: " + scanningMethod.getClass());
			File initDirectoryFile = new File(initScanningDirectory);
			try {
				scanFolder(initDirectoryFile);
			} catch (AntiVirusException e) {
				e.printStackTrace();
			}
		}

		isFileFolderScannerActive = false;
	}

	private void scanAllFileSystem() {
		File[] hardDrives = getAllHardDrives();
		System.out.println("scanning all drivers");
		System.out.println("scanning method: " + scanningMethod.getClass());
		for (File dir : hardDrives) {

			System.out.println("starting scan in hardrive: " + dir.getPath());
			try {
				scanFolder(dir);
			} catch (AntiVirusException e) {
				e.printStackTrace();
			}
			System.out.println("scan ended on hardrive: " + dir.getPath());

		}
	}

	@Override
	public void run() {
		startScanning();
	}

	public void setInitScanningDirectory(String initScanningDirectory) throws AntiVirusScanningException {

		if (!(new File(initScanningDirectory).exists())) {
			throw new AntiVirusScanningException("the init Path is invalid! ");
		}

		this.initScanningDirectory = initScanningDirectory;
	}

}
