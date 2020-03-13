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
import AntiVirus.Scanner.ScannerScheduler;

@Configuration
@EnableAutoConfiguration
@ComponentScan({ "AntiVirus.FileFolderHandler", "AntiVirus.Scanner" })
@EntityScan("AntiVirus.FileFolderHandler.entities")
@EnableJpaRepositories("AntiVirus.FileFolderHandler.repositories")
@EnableScheduling
public class AppConfig {

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
