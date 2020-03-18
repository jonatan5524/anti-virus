package AntiVirus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import AntiVirus.Configuration.AppConfig;
import AntiVirus.Scanner.ScannerScheduler;

@SpringBootApplication
public class AntiVirusApplication {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(AppConfig.class);
		ScannerScheduler scheduler = ctx.getBean(ScannerScheduler.class);

	}
}
