package antiVirus.scanner.fileFolderHandler.scanningAlgorithem;

import java.util.Stack;

public class ScanningDFS<T> implements ScanningAlgorithm<T> {

	private Stack<T> stack;

	@Override
	public void init() {
		stack = new Stack<T>();
	}

	@Override
	public T remove() {
		return stack.pop();
	}

	@Override
	public void add(T t) {
		stack.add(t);
	}

	@Override
	public boolean isEmpty() {
		return stack.isEmpty();
	}

}
