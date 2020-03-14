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

import AntiVirus.FileFolderHandler.ScanningAlgorithem.ScanningAlgorithemTemplate;
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
	@Autowired // Default: BFS
	@Setter
	private ScanningAlgorithemTemplate scanningMethod;
	@Getter
	private boolean scanning = false;

	public void debug() {
		System.out.println("debug.....");
		System.out.println("fileRepo count: " + fileRepo.count());
		System.out.println("folderRepo count: " + folderRepo.count());
	}

	private FolderDB[] getAllHardDrives() {
		File[] paths;

		paths = File.listRoots();

		System.out.println("hardDrives:");

		FolderDB[] hardDrives = new FolderDB[paths.length];
		for (int i = 0; i < paths.length; i++) {
			System.out.println(paths[i]);
			hardDrives[i] = new FolderDB((paths[i].getName()=="") ? paths[i].getPath() : paths[i].getName(), paths[i].getPath(), paths[i]);
		}
		return hardDrives;
	}

	@Override
	public void run() {
		scanning=true;
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");  
		LocalDateTime now = LocalDateTime.now();  
		String start=dtf.format(now);
	//	FolderDB[] hardDrives = getAllHardDrives();
		System.out.println("scanning method: "+scanningMethod.getClass());
	/*
		for (FolderDB dir : hardDrives) {
			System.out.println("starting scan in hardrive: " + dir.getPath());
			scanningMethod.scanFolder(dir, fileRepo, folderRepo);
		}
		*/
		FolderDB dir = new FolderDB("E://", "E://", new File("E://"));
		scanningMethod.scanFolder(dir, fileRepo, folderRepo);
		now = LocalDateTime.now();  
		String end=dtf.format(now);
		
		System.out.println("Done:\n start: "+start+"\nend: "+end );
		System.out.println("scanning method: "+scanningMethod.getClass());
		debug();
	}
}
