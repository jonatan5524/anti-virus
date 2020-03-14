package AntiVirus;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import AntiVirus.FileFolderHandler.FileFolderScanner;
import AntiVirus.FileFolderHandler.ScanningAlgorithem.ScanningAlgorithemTemplate;
import AntiVirus.FileFolderHandler.ScanningAlgorithem.ScanningBFS;
import AntiVirus.FileFolderHandler.ScanningAlgorithem.ScanningDFS;
import AntiVirus.Scanner.ScannerScheduler;

@Configuration
@EnableAutoConfiguration
@ComponentScan({ "AntiVirus.FileFolderHandler", "AntiVirus.Scanner" })
@EntityScan("AntiVirus.FileFolderHandler.entities")
@EnableJpaRepositories("AntiVirus.FileFolderHandler.repositories")
@EnableScheduling
public class AppConfig {

	// Default Method: BFS, can be changed using Set method
	@Bean
	public ScanningAlgorithemTemplate scanningAlgorithemTemplate()
	{
		return new ScanningBFS();
	}
	
	@Bean
	public ScannerScheduler scannerScheduler() {
		return new ScannerScheduler();
	}

	@Bean
	public FileFolderScanner fileFolderScanner() {
		return new FileFolderScanner();
	}

	@Bean
	public TaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(5);
		executor.setMaxPoolSize(5);
		executor.setQueueCapacity(10);
		executor.initialize();

		return executor;
	}
}
