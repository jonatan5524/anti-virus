package antiVirus.configuration;

import java.util.Collection;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import antiVirus.AntiVirusApplication;
import antiVirus.analyzer.Analyzer;
import antiVirus.analyzer.FileAnalyzer;
import antiVirus.analyzer.hashAnalyzer.MalShareAnalyzer;
import antiVirus.analyzer.hashAnalyzer.VirusTotalAnalyzer;
import antiVirus.analyzer.yaraAnalyzer.YaraAnalyzer;
import antiVirus.exceptions.AntiVirusException;
import antiVirus.scanner.ScannerScheduler;
import antiVirus.scanner.UserRequestScanner;
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
	@Scope("prototype")
	public Analyzer analyzer(FileFolderScanner fileFolderScanner,Collection<FileAnalyzer> analyzeType) {
		return new Analyzer(fileFolderScanner,analyzeType);
	}
	
	@Bean
	public UserRequestScanner userRequestScanner() {
		return new UserRequestScanner();
	}
	
	@Bean
	public MalShareAnalyzer malShareAnalyzer() {
		return new MalShareAnalyzer(null, null);
	}

	@Bean
	@Scope("prototype")
	public YaraAnalyzer yaraAnalyzer() {
		return new YaraAnalyzer();
	}

	@Bean
	public VirusTotalAnalyzer virusTotalAnalyzer() {
		return new VirusTotalAnalyzer(null, null);
	}

	@Bean
	public ScannerScheduler scannerScheduler() {
		return new ScannerScheduler();
	}

	@Bean
	@Scope("prototype")
	public FileFolderScanner fileFolderScanner() {
		return new FileFolderScanner();

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
