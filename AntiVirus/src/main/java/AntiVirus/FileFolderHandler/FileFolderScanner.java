package AntiVirus.FileFolderHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import AntiVirus.FileFolderHandler.entities.FileDB;
import AntiVirus.FileFolderHandler.entities.FolderDB;
import AntiVirus.FileFolderHandler.repositories.FileRepo;
import AntiVirus.FileFolderHandler.repositories.FolderRepo;

@Component
public class FileFolderScanner {

	@Autowired
	private FileRepo fileRepo;
	@Autowired
	private FolderRepo folderRepo;

	public void scanFolder(FolderDB dir) {
		try {

			if (!folderRepo.existsByPath(dir.getPath()))
				folderRepo.save(dir);
			File[] files = dir.getIOFolder().listFiles();
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
		} catch (NoSuchAlgorithmException | NullPointerException e) {
			e.printStackTrace();
		}
	}

	public void debug() {
		System.out.println("debug.....");
		System.out.println("fileRepo count: " + fileRepo.count());
		System.out.println("folderRepo count: " + folderRepo.count());
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
}
