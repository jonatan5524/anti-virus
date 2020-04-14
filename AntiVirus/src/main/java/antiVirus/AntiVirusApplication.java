package antiVirus;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;

import antiVirus.exceptions.AntiVirusAnalyzeException;
import antiVirus.scanner.ScannerScheduler;
import antiVirus.scanner.UserRequestScanner;



@SpringBootApplication
@ComponentScan
@EntityScan
@Controller
@EnableJpaRepositories
@EnableScheduling
public class AntiVirusApplication {
	
	public static void main(String[] args) {

		ApplicationContext ctx = SpringApplication.run(AntiVirusApplication.class, args);
		ScannerScheduler scheduler = ctx.getBean(ScannerScheduler.class);
		
		System.out.println("web graphic interface available at http://localhost:4060/");
	}	
	

}
