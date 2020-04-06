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
import antiVirus.scanner.fileFolderHandler.scanningAlgorithem.ScanningAlgorithemTemplate;
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
	private ScanningAlgorithemTemplate<FolderDB> scanningMethod;
	@Getter
	private boolean scanning = false;
	@Value("${file.hashAlgorithm}")
	private String hashAlgorithm;

	private String initScanningDir;

	public FileFolderScanner(String initScanningDir) {
		this.initScanningDir = initScanningDir;
		System.out.println("initScanningDir: " + initScanningDir);
	}

	private void scanFolder(FolderDB dir) throws AntiVirusException {
		FolderDB folderTemp = null;
		FileDB fileTemp = null;
		ResultScan resultScanTemp = null;
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance(hashAlgorithm);
		} catch (NoSuchAlgorithmException e) {
			throw new AntiVirusException("Invalid Hashing Algorithem: " + hashAlgorithm, e);
		}
		scanningMethod.init();

		scanningMethod.add(dir);

		while (!scanningMethod.isEmpty()) {

			dir = scanningMethod.remove();
			File[] files = new File(dir.getPath()).listFiles();
			if (files != null) {
				scanFilesInDir(files, folderTemp, fileTemp, resultScanTemp, md);
			}

		}

	}

	private void scanFilesInDir(File[] files, FolderDB folderTemp, FileDB fileTemp, ResultScan resultScanTemp,
			MessageDigest md) throws AntiVirusException {

		for (File file : files) {

			if (file.isDirectory()) {
				if ((Utils.isUnix() && file.getPath() != "\\proc" && file.getPath() != "\\sys") || !Utils.isUnix()) {
					handleFolder(file, folderTemp);
				}
			} else {
				if (!fileRepo.existsByPath(file.getPath())) {
					handleFile(file, resultScanTemp, fileTemp, md);
				}
			}
		}
	}

	private void handleFile(File file, ResultScan resultScanTemp, FileDB fileTemp, MessageDigest md)
			throws AntiVirusException {

		resultScanTemp = new ResultScan();
		resultScanTemp.setResultAnalyzer(new HashMap<FileAnalyzer, Boolean>());

		resultScanTemp.serializeResultAnalyzer();

		fileTemp = new FileDB(Utils.getFileChecksum(md, file), file.getName(), file.getPath());
		resultScanTemp.setFiledb(fileTemp);

		fileTemp.setResultScan(resultScanTemp);

		fileRepo.save(fileTemp);
	}

	private void handleFolder(File file, FolderDB folderTemp) {
		if (!folderRepo.existsByPath(file.getPath())) {
			// System.out.println(file.getPath());
			folderTemp = new FolderDB(file.getName(), file.getPath());
			folderRepo.save(folderTemp);
		} else {
			folderTemp = folderRepo.findByPath(file.getPath());
		}

		scanningMethod.add(folderTemp);
	}

	private FolderDB[] getAllHardDrives() {
		File[] paths;

		paths = File.listRoots();

		System.out.println("hardDrives:");

		FolderDB[] hardDrives = new FolderDB[paths.length];
		for (int i = 0; i < paths.length; i++) {
			System.out.println(paths[i]);
			hardDrives[i] = new FolderDB((paths[i].getName() == "") ? paths[i].getPath() : paths[i].getName(),
					paths[i].getPath());
		}
		return hardDrives;
	}

	private void startScanning() {
		scanning = true;

		if (initScanningDir == "") {
			scanAll();
		} else {
			File temp = new File(initScanningDir);
			FolderDB dir = new FolderDB(temp.getName(), temp.getPath());
			try {
				scanFolder(dir);
			} catch (AntiVirusException e) {

				e.printStackTrace();
			}
		}

		scanning = false;
	}

	private void scanAll() {
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
