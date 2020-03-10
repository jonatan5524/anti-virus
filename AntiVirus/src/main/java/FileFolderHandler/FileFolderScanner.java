package FileFolderHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sun.istack.NotNull;

import FileFolderHandler.entities.FileDB;
import FileFolderHandler.entities.FolderDB;
import FileFolderHandler.repositories.FileRepo;
import FileFolderHandler.repositories.FolderRepo;

@Component
public class FileFolderScanner {
	
	@Autowired
	private FileRepo fileRepo;
	@Autowired
	private FolderRepo folderRepo;

	public void scanFolder(FolderDB dir) {
		try {
			
			if (!folderRepo.existsById(dir.getPath()))
				folderRepo.save(dir);
			File[] files = dir.getIOFolder().listFiles();
			for (File file : files) {
				if (file.isDirectory()) {
					FolderDB folderTemp = new FolderDB(dir, file.getName(), file.getPath(), file);
					if (!folderRepo.existsById(file.getPath()))
						folderRepo.save(folderTemp);
					scanFolder(folderTemp);
				} else {
					MessageDigest md = MessageDigest.getInstance("MD5");
					FileDB fileDb = new FileDB(checksum(file, md), dir, file.getName(), file.getPath(), file);
					if (!fileRepo.existsById(file.getPath()))
						fileRepo.save(fileDb);
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
