package antiVirus.configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import antiVirus.analyzer.Analyzer;
import antiVirus.analyzer.FileAnalyzer;
import antiVirus.analyzer.hashAnalyzer.MalShareAnalyzer;
import antiVirus.analyzer.hashAnalyzer.VirusTotalAnalyzer;
import antiVirus.analyzer.yaraAnalyzer.Yara;
import antiVirus.analyzer.yaraAnalyzer.YaraAnalyzer;
import antiVirus.scanner.ScannerScheduler;
import antiVirus.scanner.UserRequestScanner;
import antiVirus.scanner.fileFolderHandler.FileFolderScanner;
import antiVirus.scanner.fileFolderHandler.scanningAlgorithem.ScanningAlgorithm;
import antiVirus.scanner.fileFolderHandler.scanningAlgorithem.ScanningBFS;
import antiVirus.logger.LoggerFormatter;
import antiVirus.logger.LoggerManager;

@Configuration
public class AppConfig {

	@Value("${config.corePoolSize}")
	private int corePoolSize;
	@Value("${config.maxPoolSize}")
	private int maxPoolSize;

	@Bean("ScanningAlgorithmBFS")
	@Scope("prototype")
	public ScanningAlgorithm<File> scanningAlgorithemTemplateBFS() {
		return new ScanningBFS<File>();
	}

	@Bean("ScanningAlgorithmDFS")
	@Scope("prototype")
	public ScanningAlgorithm<File> scanningAlgorithemTemplateDFS() {
		return new ScanningBFS<File>();
	}

	public MalShareAnalyzer malShareAnalyzer() {
		return new MalShareAnalyzer();
	}

	public VirusTotalAnalyzer virusTotalAnalyzer() {
		return new VirusTotalAnalyzer();
	}

	@Bean
	@Scope("prototype")
	public Analyzer analyzer(FileFolderScanner fileFolderScanner, Collection<FileAnalyzer> analyzeType) {
		return new Analyzer(fileFolderScanner, analyzeType);
	}

	@Bean
	public UserRequestScanner userRequestScanner() {
		return new UserRequestScanner();
	}

	@Bean
	@Scope("prototype")
	public YaraAnalyzer yaraAnalyzer() {
		return new YaraAnalyzer();
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
	public LoggerFormatter loggerFormatter() {
		return new LoggerFormatter();
	}
	
	@Bean
	public LoggerManager loggerManager() {
		return new LoggerManager();
	}
	
	
	@Bean
	public TaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(corePoolSize);
		executor.setMaxPoolSize(maxPoolSize);
		executor.initialize();

		return executor;
	}

	@Bean("userAnalyzeTypeList")
	public Collection<FileAnalyzer> userAnalyzeTypeList(YaraAnalyzer yara, MalShareAnalyzer malShare,
			VirusTotalAnalyzer virusTotal) {
		Collection<FileAnalyzer> list = new ArrayList<FileAnalyzer>();

		list.add(yara);
		list.add(malShare);
		list.add(virusTotal);

		return list;
	}

	@Bean("scannerSchedulerAnalyzeTypeList")
	public Collection<FileAnalyzer> scannerSchedulerAnalyzeTypeList(YaraAnalyzer yara, MalShareAnalyzer malShare,
			VirusTotalAnalyzer virusTotal) {
		Collection<FileAnalyzer> list = new ArrayList<FileAnalyzer>();

		list.add(yara);
		list.add(malShare);
		list.add(virusTotal);

		return list;
	}
	

}
