package antiVirus.analyzer.hashAnalyzer;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import org.javalite.http.Get;
import org.javalite.http.Http;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import antiVirus.entities.FileDB;

@Service
public class MalShareAnalyzer extends HashAnalyzer {

	@Autowired
	public MalShareAnalyzer(@Value("${MalShare.API_KEY}") String API_KEY, @Value("${MalShare.URL}") String URL) {
		this.API_KEY = API_KEY;
		this.URL = URL;
	}

	@PostConstruct
	protected void setURI() {
		super.setURI();
		this.URI += "&action=details&hash=";
	}

	public boolean scanFile(FileDB file,Logger logger) {
		if (!file.getHash().isEmpty()) {
			logger.info("analyzing file - MalShareAnalyzer: " + file.getPath());
			Get response = Http.get(URI + file.getHash());

			return parseResponse(response,logger,file);
		}
		return false;
	}
	
	@Override
	public boolean parseResponse(Get response, Logger logger, FileDB file) {
		
		if (response.responseCode() == 200) {
			logger.info("found MalShareAnalyzer: " + file.getPath());
			return true;

		} else if (response.responseCode() != 404) {
			logger.warning("malShare returned: " + response.text());
		}
		return false;
	}

}
