package AntiVirus.FileFolderHandler.ScanningAlgorithem;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import AntiVirus.FileFolderHandler.entities.FileDB;
import AntiVirus.FileFolderHandler.entities.FolderDB;
import AntiVirus.FileFolderHandler.repositories.FileRepo;
import AntiVirus.FileFolderHandler.repositories.FolderRepo;

public class ScanningDFS<T> implements ScanningAlgorithemTemplate<T> {

	private Queue<T> queue;
	
	@Override
	public void init() {
		queue = new LinkedList<T>();
	}

	@Override
	public T remove() {
		return queue.remove();
	}

	@Override
	public void add(T t) {
		queue.add(t);
	}

	@Override
	public boolean isEmpty() {
		return queue.isEmpty();
	}

}
