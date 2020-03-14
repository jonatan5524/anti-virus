package AntiVirus.FileFolderHandler.ScanningAlgorithem;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Stack;

import AntiVirus.FileFolderHandler.entities.FileDB;
import AntiVirus.FileFolderHandler.entities.FolderDB;
import AntiVirus.FileFolderHandler.repositories.FileRepo;
import AntiVirus.FileFolderHandler.repositories.FolderRepo;

public class ScanningBFS extends ScanningAlgorithemTemplate{

	@Override
	public void scanFolder(FolderDB dir, FileRepo fileRepo, FolderRepo folderRepo) {
		try {
			FolderDB folderTemp;
			// FileDB fileTemp;
			MessageDigest md = MessageDigest.getInstance("MD5");
			Stack<FolderDB> stack = new Stack<FolderDB>();
			stack.push(dir);

			// queue for BFS
			while (!stack.empty()) {
				
				dir = stack.pop();
				File[] files = dir.getIOFolder().listFiles();
				if (files != null) {
					// go over all the files and sub directory in the current directory
					for (File file : files) {
						// if is a sub directory add to queue and to repository if needed
						if (file.isDirectory()) {
							if (!folderRepo.existsByPath(file.getPath())) {
								System.out.println(file.getPath());
								folderTemp = new FolderDB(file.getName(), file.getPath(), file);
								folderRepo.save(folderTemp);
							} else
								folderTemp = folderRepo.findByPath(file.getPath());
							stack.push(folderTemp);
						// if is a file add to repository if needed
						} else {
							if (!fileRepo.existsByPath(file.getPath())) {
								System.out.println(file.getPath());
								fileRepo.save(new FileDB(checksum(file, md), file.getName(), file.getPath(), file));
							}
						}
					}
				}

			}
		} catch (NoSuchAlgorithmException | NullPointerException e) {
			e.printStackTrace();
		}
	}

	
}
