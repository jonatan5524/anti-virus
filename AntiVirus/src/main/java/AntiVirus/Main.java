package AntiVirus;

import java.io.File;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import FileFolderHandler.FileFolderScanner;
import FileFolderHandler.entities.FolderDB;

@SpringBootApplication
public class Main {
	
	public static void main(String[] args) {
		SpringApplication.run(Main.class,args);
		
		FileFolderScanner scanner = new FileFolderScanner();
		scanner.scanFolder(new FolderDB(null, "E", "E:\\", new File("E:\\")));
		scanner.debug();
	}
}
