package antiVirus.analyzer.hashAnalyzer;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import org.javalite.http.Get;
import org.javalite.http.Http;
import org.springframework.beans.factory.annotation.Value;

import antiVirus.analyzer.FileAnalyzer;
import antiVirus.entities.FileDB;

public interface HashAnalyzer extends FileAnalyzer {
	
	public boolean parseResponse(Get response,Logger logger,FileDB file);
}
