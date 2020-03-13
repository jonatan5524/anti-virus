package AntiVirus.FileFolderHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import javax.annotation.PostConstruct;
import javax.swing.filechooser.FileSystemView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Component;

import AntiVirus.FileFolderHandler.entities.FileDB;
import AntiVirus.FileFolderHandler.entities.FolderDB;
import AntiVirus.FileFolderHandler.repositories.FileRepo;
import AntiVirus.FileFolderHandler.repositories.FolderRepo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Component
@NoArgsConstructor
public class FileFolderScanner implements Runnable {

	@Autowired
	private FileRepo fileRepo;
	@Autowired
	private FolderRepo folderRepo;
	@Getter
	private boolean scanning = false;

	private void scanFolder(FolderDB dir) {
		try {
			File[] files = dir.getIOFolder().listFiles();
			if (files != null) {
				for (File file : files) {
					if (file.isDirectory()) {
						FolderDB folderTemp;
						if (!folderRepo.existsByPath(file.getPath())) {
							System.out.println(file.getPath());
							folderTemp = new FolderDB(file.getName(), file.getPath(), file);
							folderRepo.save(folderTemp);
						} else
							folderTemp = folderRepo.findByPath(file.getPath());
						scanFolder(folderTemp);
					} else {

						MessageDigest md = MessageDigest.getInstance("MD5");
						if (!fileRepo.existsByPath(file.getPath())) {
							System.out.println(file.getPath());
							fileRepo.save(new FileDB(checksum(file, md), file.getName(), file.getPath(), file));
						}
					}
				}
			}
		} catch (NoSuchAlgorithmException | NullPointerException e) {
			e.printStackTrace();
		}
	}

	public void debug() {
		System.out.println("debug.....");
		System.out.println("fileRepo count: " + fileRepo.count());
		System.out.println("folderRepo count: " + folderRepo.count());
	}

	private FolderDB[] getAllHardDrives() {
		File[] paths;
		// FileSystemView fsv = FileSystemView.getFileSystemView();
		paths = File.listRoots();

		System.out.println("hardDrives:");

		FolderDB[] hardDrives = new FolderDB[paths.length];
		for (int i = 0; i < paths.length; i++) {
			System.out.println(paths[i]);
			hardDrives[i] = new FolderDB(paths[i].getName(), paths[i].getPath(), paths[i]);
		}
		return hardDrives;
	}

	private String checksum(File input, MessageDigest md) {
		try (InputStream in = new FileInputStream(input)) {
			byte[] block = new byte[4096];
			int length;
			while ((length = in.read(block)) > 0) {
				md.update(block, 0, length);
			}
			return md.digest().toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void run() {
		scanning=true;
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");  
		LocalDateTime now = LocalDateTime.now();  
		String start=dtf.format(now);
		FolderDB[] hardDrives = getAllHardDrives();

		for (FolderDB dir : hardDrives) {
			System.out.println("starting scan in hardrive: " + dir.getPath());
			scanFolder(dir);
		}
		now = LocalDateTime.now();  
		String end=dtf.format(now);
		
		System.out.println("Done:\n start: "+start+"\nend: "+end );
	}
}
