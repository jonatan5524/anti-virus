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
import org.springframework.stereotype.Component;

import antiVirus.analyzer.FileAnalyzer;
import antiVirus.entities.FileDB;
import antiVirus.entities.FolderDB;
import antiVirus.entities.ResultScan;
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

	private void scanFolder(FolderDB dir) throws NoSuchAlgorithmException {
		FolderDB folderTemp;
		FileDB fileTemp;
		ResultScan resultScanTemp;
		MessageDigest md = MessageDigest.getInstance("MD5");
		scanningMethod.init();

		scanningMethod.add(dir);

		while (!scanningMethod.isEmpty()) {

			dir = scanningMethod.remove();
			File[] files = new File(dir.getPath()).listFiles();
			if (files != null) {
				// go over all the files and sub directory in the current directory
				for (File file : files) {
					// if is a sub directory add to queue and to repository if needed
					if (file.isDirectory()) {
						if (!folderRepo.existsByPath(file.getPath())) {
							// System.out.println(file.getPath());
							folderTemp = new FolderDB(file.getName(), file.getPath());
							folderRepo.save(folderTemp);
						} else
							folderTemp = folderRepo.findByPath(file.getPath());
						scanningMethod.add(folderTemp);
						// if is a file add to repository if needed
					} else {
						if (!fileRepo.existsByPath(file.getPath())) {
							System.out.println(file.getPath());
							//resultScanTemp = new ResultScan();
							//resultScanTemp.setResultAnalyzer(new HashMap<FileAnalyzer, Boolean>());
							fileTemp = new FileDB(Utils.getFileChecksum(md, file), file.getName(), file.getPath());
							fileTemp.getResultScan().setResultAnalyzer(new HashMap<FileAnalyzer, Boolean>());
							//resultScanTemp.setFiledb(fileTemp);
							//fileTemp.setResultScan(resultScanTemp);
							
							System.out.println(fileTemp);
							fileRepo.save(fileTemp);
							System.out.println(file.getPath());
						}
					}
				}
			}

		}

	}

	/*
	 * public void debug() { System.out.println("debug.....");
	 * System.out.println("fileRepo count: " + fileRepo.count());
	 * System.out.println("folderRepo count: " + folderRepo.count()); }
	 */
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
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String start = dtf.format(now);
		FolderDB[] hardDrives = getAllHardDrives();
		System.out.println("scanning method: " + scanningMethod.getClass());
		for (FolderDB dir : hardDrives) {
			// if (dir.getPath().contains("E")) {
			System.out.println("starting scan in hardrive: " + dir.getPath());
			try {
				scanFolder(dir);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			System.out.println("scan ended on hardrive: " + dir.getPath());
			// }
		}
		now = LocalDateTime.now();
		String end = dtf.format(now);

		System.out.println("Done:\n start: " + start + "\nend: " + end);
		System.out.println("scanning method: " + scanningMethod.getClass());
		// debug();
		scanning = false;
	}

	@Override
	public void run() {
		startScanning();
	}

}
