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
import AntiVirus.Scanner.FileFolderHandler.FileFolderScanner;
import AntiVirus.Scanner.FileFolderHandler.ScanningAlgorithem.ScanningAlgorithemTemplate;
import AntiVirus.Scanner.FileFolderHandler.ScanningAlgorithem.ScanningBFS;
import AntiVirus.Scanner.FileFolderHandler.ScanningAlgorithem.ScanningDFS;
import AntiVirus.Analyzer.YaraAnalyzer.YaraAnalyzer;
import AntiVirus.Scanner.ScannerScheduler;

@Configuration
@EnableAutoConfiguration
@ComponentScan("AntiVirus.Scanner")
@EntityScan("AntiVirus.entities")
@EnableJpaRepositories("AntiVirus.repositories")
@EnableScheduling
public class AppConfig {
	
	// Default Method: BFS, can be changed using Set method
	@Bean
	public ScanningAlgorithemTemplate scanningAlgorithemTemplate() {
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
		executor.initialize();

		return executor;
	}
}
