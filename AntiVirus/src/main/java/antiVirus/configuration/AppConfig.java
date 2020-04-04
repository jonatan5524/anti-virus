package antiVirus.configuration;

import org.springframework.boot.ApplicationArguments;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import antiVirus.AntiVirusApplication;
import antiVirus.analyzer.hashAnalyzer.VirusTotalAnalyzer;
import antiVirus.analyzer.yaraAnalyzer.YaraAnalyzer;
import antiVirus.scanner.ScannerScheduler;
import antiVirus.scanner.fileFolderHandler.FileFolderScanner;
import antiVirus.scanner.fileFolderHandler.scanningAlgorithem.ScanningAlgorithemTemplate;
import antiVirus.scanner.fileFolderHandler.scanningAlgorithem.ScanningBFS;

@Configuration
public class AppConfig {

	// Default Method: BFS, can be changed using Set method
	@Bean
	public ScanningAlgorithemTemplate scanningAlgorithemTemplate() {
		return new ScanningBFS();
	}

	@Bean
	public YaraAnalyzer yaraAnalyzer() {
		return new YaraAnalyzer();
	}

	@Bean
	public VirusTotalAnalyzer virusTotalAnalyzer() {
		return new VirusTotalAnalyzer();
	}

	@Bean
	public ScannerScheduler scannerScheduler() {
		return new ScannerScheduler();
	}

	@Bean
	public FileFolderScanner fileFolderScanner() {
		String temp = "";
		if (AntiVirusApplication.arguments.length > 0) {
			for (int i = 0; i < AntiVirusApplication.arguments.length; i++) {
				if (AntiVirusApplication.arguments[i].equals("-d"))
				{
					temp = AntiVirusApplication.arguments[i + 1];
					break;
				}

			}
		}
		return new FileFolderScanner(temp);
	}

	@Bean
	public TaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(100);
		executor.setMaxPoolSize(100);
		executor.initialize();

		return executor;
	}
}
