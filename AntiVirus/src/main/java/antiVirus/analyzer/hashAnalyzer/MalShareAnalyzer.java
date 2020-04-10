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
public class MalShareAnalyzer implements HashAnalyzer {

	@Value("${MalShare.API_KEY}")
	private String API_KEY;
	@Value("${MalShare.URL}")
	private String MS_URL;
	private String MS_URI;

	@PostConstruct
	private void setURI() {
		this.MS_URI = MS_URL + "?api_key=" + API_KEY + "&action=details&hash=";
	}

	@Override
	public boolean scanFile(FileDB file,Logger logger) {
		if (!file.getHash().isEmpty()) {
			logger.info("analyzing file - MalShareAnalyzer: " + file.getPath());
			Get response = Http.get(MS_URI + file.getHash());

			if (response.responseCode() == 200) {
				logger.info("found MalShareAnalyzer: " + file.getPath());
				return true;

			}else if(response.responseCode() != 404) {
				logger.warning("malShare returned: " + response.responseCode());
			}
		}
		return false;
	}

}
