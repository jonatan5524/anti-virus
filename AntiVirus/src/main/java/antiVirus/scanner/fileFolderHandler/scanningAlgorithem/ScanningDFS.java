package antiVirus.scanner.fileFolderHandler.scanningAlgorithem;

import java.util.LinkedList;
import java.util.Queue;

public class ScanningDFS<T> implements ScanningAlgorithm<T> {

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
