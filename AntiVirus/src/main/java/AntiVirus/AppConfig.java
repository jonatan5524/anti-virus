package AntiVirus;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import AntiVirus.FileFolderHandler.FileFolderScanner;

@Configuration
public class AppConfig {
	
	@Bean
	public FileFolderScanner fileFolderScanner()
	{
		return new FileFolderScanner();
	}
}
