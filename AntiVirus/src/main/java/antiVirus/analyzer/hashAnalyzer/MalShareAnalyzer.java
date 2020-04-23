package antiVirus.analyzer.hashAnalyzer;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import org.javalite.http.Get;
import org.javalite.http.Http;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import antiVirus.entities.FileDB;


@Service
public class MalShareAnalyzer extends HashAnalyzer {

	public MalShareAnalyzer(@Value("${MalShare.API_KEY}") String API_KEY,@Value("${MalShare.URL}") String URL) {
		this.API_KEY=API_KEY;
		this.URL=URL;
	}
	
	@PostConstruct
	protected void setURI() {
		super.setURI();
		this.URI += "&action=details&hash=";
	}

	@Override
	public boolean parseResponse(Get response,Logger logger,FileDB file) {
		logger.info("analyzing file - MalShareAnalyzer: " + file.getPath());
		if (response.responseCode() == 200) {
			logger.info("found MalShareAnalyzer: " + file.getPath());
			return true;

		}else {
			logger.warning("malShare returned: " + response.responseCode());
		}
		return false;
	}


}
