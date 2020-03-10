package AntiVirus;

import java.io.File;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import FileFolderHandler.FileFolderScanner;
import FileFolderHandler.entities.FolderDB;

@SpringBootApplication
@ComponentScan("FileFolderHandler")
@EntityScan("FileFolderHandler.entities")
@EnableJpaRepositories("FileFolderHandler.repositories")
public class Main {

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);

		FileFolderScanner scanner = new AppConfig().fileFolderScanner();
		scanner.scanFolder(new FolderDB(null, "E", "E:\\", new File("E:\\")));
		scanner.debug();
		/*
		 * @SuppressWarnings("resource") ApplicationContext ctx = new
		 * ClassPathXmlApplicationContext("beans.xml"); FileFolderScanner scanner =
		 * ctx.getBean(FileFolderScanner.class); scanner.scanFolder(new FolderDB(null,
		 * "E", "E:\\", new File("E:\\"))); scanner.debug();
		 */
	}
}
