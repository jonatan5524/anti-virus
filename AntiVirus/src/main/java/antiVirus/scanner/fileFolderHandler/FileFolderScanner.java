package antiVirus.scanner.fileFolderHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;

import antiVirus.analyzer.FileAnalyzer;
import antiVirus.entities.FileDB;
import antiVirus.entities.FolderDB;
import antiVirus.entities.ResultScan;
import antiVirus.exceptions.AntiVirusException;
import antiVirus.repositories.FileRepo;
import antiVirus.repositories.FolderRepo;
import antiVirus.scanner.fileFolderHandler.scanningAlgorithem.ScanningAlgorithm;
import antiVirus.utils.Utils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Component
@NoArgsConstructor
public class FileFolderScanner implements Runnable {

	@Getter
	@Autowired
	private FileRepo fileRepo;
	@Autowired
	private FolderRepo folderRepo;
	@Autowired // Default: BFS
	@Setter
	private ScanningAlgorithm<FolderDB> scanningMethod;
	@Getter
	private boolean isFileFolderScannerActive;
	@Value("${file.hashAlgorithm}")
	private String hashAlgorithm;

	private MessageDigest messageDigest;

	private String initScanningDirectory;

	public FileFolderScanner(String initScanningDirectory) throws AntiVirusException {
		isFileFolderScannerActive = false;
		this.initScanningDirectory = initScanningDirectory;
		System.out.println("initScanningDir: " + initScanningDirectory);

	}

	@PostConstruct
	public void initMessageDigest() throws AntiVirusException {
		try {
			messageDigest = MessageDigest.getInstance(hashAlgorithm);
		} catch (NoSuchAlgorithmException e) {
			throw new AntiVirusException("Invalid Hashing Algorithem: " + hashAlgorithm, e);
		}
	}

	private void scanFolder(FolderDB initDirDB) throws AntiVirusException {
		FolderDB parentDirectoryDB;
		scanningMethod.init();
		scanningMethod.add(initDirDB);

		while (!scanningMethod.isEmpty()) {

			parentDirectoryDB = scanningMethod.remove();
			File[] files = new File(parentDirectoryDB.getPath()).listFiles();
			if (files != null) {
				scanFilesInDir(files);
			}

		}

	}

	private void scanFilesInDir(File[] files) throws AntiVirusException {

		for (File file : files) {

			if (file.isDirectory()) {
				if (needToAddFolderToDB(file.getPath())) {
					insertFolderToDB(file);
				}
			} else {
				if (!fileRepo.existsByPath(file.getPath())) {
					insertFileToDB(file);
				}
			}
		}
	}

	private boolean needToAddFolderToDB(String filePath) {
		return (Utils.isUnix() && filePath != "\\proc" && filePath != "\\sys") || !Utils.isUnix();
	}

	private void insertFileToDB(File file) throws AntiVirusException {

		ResultScan resultScanTemp = new ResultScan();
		resultScanTemp.setResultAnalyzer(new HashMap<FileAnalyzer, Boolean>());

		resultScanTemp.serializeResultAnalyzer();

		FileDB fileTemp = new FileDB(Utils.getFileChecksum(messageDigest, file), file.getName(), file.getPath());
		resultScanTemp.setFiledb(fileTemp);

		fileTemp.setResultScan(resultScanTemp);

		fileRepo.save(fileTemp);
	}

	private void insertFolderToDB(File file) {
		FolderDB folderTemp;
		String filePath = file.getPath();

		if (!folderRepo.existsByPath(filePath)) {
			folderTemp = new FolderDB(file.getName(), filePath);
			folderRepo.save(folderTemp);
		} else {
			folderTemp = folderRepo.findByPath(filePath);
		}

		scanningMethod.add(folderTemp);
	}

	private FolderDB[] getAllHardDrives() {
		File[] paths = File.listRoots();

		System.out.println("hardDrives:");

		FolderDB[] hardDrives = new FolderDB[paths.length];
		for (int i = 0; i < paths.length; i++) {
			System.out.println(paths[i]);
			hardDrives[i] = new FolderDB(paths[i].getPath(), paths[i].getPath());
		}
		return hardDrives;
	}

	private void startScanning() {
		isFileFolderScannerActive = true;

		if (initScanningDirectory == "") {
			scanAllFileSystem();
		} else {
			File initDirectoryFile = new File(initScanningDirectory);
			FolderDB initDirectoryDB = new FolderDB(initDirectoryFile.getName(), initDirectoryFile.getPath());
			try {
				scanFolder(initDirectoryDB);
			} catch (AntiVirusException e) {
				e.printStackTrace();
			}
		}

		isFileFolderScannerActive = false;
	}

	private void scanAllFileSystem() {
		FolderDB[] hardDrives = getAllHardDrives();
		System.out.println("scanning method: " + scanningMethod.getClass());
		for (FolderDB dir : hardDrives) {

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

}
