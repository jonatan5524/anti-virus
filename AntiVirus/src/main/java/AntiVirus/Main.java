package AntiVirus;

import java.io.File;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import AntiVirus.FileFolderHandler.FileFolderScanner;
import AntiVirus.FileFolderHandler.entities.FolderDB;

@SpringBootApplication
@ComponentScan("AntiVirus.FileFolderHandler")
@EntityScan("AntiVirus.FileFolderHandler")
@EnableJpaRepositories("AntiVirus.FileFolderHandler")
public class Main {
	
	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(Main.class, args);
		
		FileFolderScanner scanner = new AppConfig().fileFolderScanner();
		AutowireCapableBeanFactory factory = ctx.getAutowireCapableBeanFactory();
		factory.autowireBean( scanner );
		long start_time = System.nanoTime();
		scanner.scanFolder(new FolderDB( "E", "E:\\", new File("E:\\")));
		long end_time = System.nanoTime();
		double difference = (end_time - start_time) / 1e6;
		scanner.debug();
		System.out.println("runtime: "+difference);
		
	}
}
