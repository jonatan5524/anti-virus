package antiVirus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import antiVirus.scanner.ScannerScheduler;
import antiVirus.scanner.UserRequestScanner;



@SpringBootApplication
@ComponentScan
@EntityScan
@EnableJpaRepositories
@EnableScheduling
public class AntiVirusApplication {
	
	public static void main(String[] args) {

		ApplicationContext ctx = SpringApplication.run(AntiVirusApplication.class, args);
		ScannerScheduler scheduler = ctx.getBean(ScannerScheduler.class);
		
		UserRequestScanner user = ctx.getBean(UserRequestScanner.class,"C:\\Users\\jonat\\Desktop\\webserviceStatus\\server-monitor\\src");
		user.startScan();
	}	
}
