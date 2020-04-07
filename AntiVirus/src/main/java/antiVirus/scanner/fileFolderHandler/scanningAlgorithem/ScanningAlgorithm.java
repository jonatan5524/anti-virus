package antiVirus.scanner.fileFolderHandler.scanningAlgorithem;

public interface ScanningAlgorithm<T> {

	public void init();

	public T remove();

	public void add(T t);

	public boolean isEmpty();

}
