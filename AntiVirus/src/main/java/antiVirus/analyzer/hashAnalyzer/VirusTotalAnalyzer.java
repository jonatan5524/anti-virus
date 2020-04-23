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
import antiVirus.analyzer.hashAnalyzer.jsonParser.VTHash;

@Service
public class VirusTotalAnalyzer extends HashAnalyzer {

	private Gson gson;

	@Autowired
	public VirusTotalAnalyzer(@Value("{virus-total.VT_URL}") String URL,
			@Value("${virus-total.API_KEY}") String API_KEY) {
		this.gson = new Gson();
		this.URL = URL;
		this.API_KEY = API_KEY;
	}

	@PostConstruct
	protected void setURI() {
		super.setURI();
		this.URI = "&resource=";
	}

	@Override
	public boolean parseResponse(Get response, Logger logger, FileDB file) {
		logger.info("analyzing file - totalVirus: " + file.getPath());
		if (response.responseCode() == 200) {
			String responseText = response.text();

			VTHash json = gson.fromJson(responseText, VTHash.class);
			if (json.responseCode == 1 && json.positives > 0) {
				logger.info("found VirusTotal: " + file.getPath());
				return true;
			}
		} else {
			logger.warning("virusTotal returned: " + response.text());
		}
		return false;
	}

}
