package AntiVirus.Scanner.FileFolderHandler.ScanningAlgorithem;

public interface ScanningAlgorithemTemplate<T> {

	public void init();

	public T remove();

	public void add(T t);

	public boolean isEmpty();

}
