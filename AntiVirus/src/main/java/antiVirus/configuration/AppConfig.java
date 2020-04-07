package antiVirus.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import antiVirus.AntiVirusApplication;
import antiVirus.analyzer.hashAnalyzer.VirusTotalAnalyzer;
import antiVirus.analyzer.yaraAnalyzer.YaraAnalyzer;
import antiVirus.exceptions.AntiVirusException;
import antiVirus.scanner.ScannerScheduler;
import antiVirus.scanner.fileFolderHandler.FileFolderScanner;
import antiVirus.scanner.fileFolderHandler.scanningAlgorithem.ScanningAlgorithm;
import antiVirus.scanner.fileFolderHandler.scanningAlgorithem.ScanningBFS;

@Configuration
public class AppConfig {

	// Default Method: BFS, can be changed using Set method
	@Bean
	public ScanningAlgorithm scanningAlgorithemTemplate() {
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

		try {
			return new FileFolderScanner(handleArgs("-d"));
		} catch (AntiVirusException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private String handleArgs(String option) {
		String temp = "";
		if (AntiVirusApplication.arguments.length > 0) {
			for (int i = 0; i < AntiVirusApplication.arguments.length; i++) {
				if (AntiVirusApplication.arguments[i].equals(option)) {
					temp = AntiVirusApplication.arguments[i + 1];
					break;
				}

			}
		}
		return temp;
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
