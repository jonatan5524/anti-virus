package antiVirus.analyzer.hashAnalyzer;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import org.javalite.http.Get;
import org.javalite.http.Http;
import org.springframework.beans.factory.annotation.Value;

import antiVirus.analyzer.FileAnalyzer;
import antiVirus.entities.FileDB;

public abstract class HashAnalyzer implements FileAnalyzer {
	
	protected String API_KEY;
	protected String URL;
	protected String URI;
	
	
	protected void setURI() {
		this.URI = URL + "?api_key=" + API_KEY;
	}

	
	public abstract boolean parseResponse(Get response,Logger logger,FileDB file);
}
