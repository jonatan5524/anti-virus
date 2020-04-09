package antiVirus.analyzer.hashAnalyzer;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import org.javalite.http.Get;
import org.javalite.http.Http;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import antiVirus.entities.FileDB;
import antiVirus.analyzer.hashAnalyzer.jsonParser.VTHash;

@Service
public class VirusTotalAnalyzer implements HashAnalyzer {

	@Value("${virus-total.API_KEY}")
	private String API_KEY;
	@Value("${virus-total.VT_URL}")
	private String VT_URL;
	private String VT_URI;
	private Gson gson;

	public VirusTotalAnalyzer() {
		this.gson = new Gson();
	}

	@PostConstruct
	private void setURI() {
		this.VT_URI = VT_URL + "?apikey=" + API_KEY + "&resource=";
	}

	@Override
	public boolean scanFile(FileDB file,Logger logger) {
		if (!file.getHash().isEmpty()) {
			logger.info("analyzing file - totalVirus: " + file.getPath());
			Get response = Http.get(VT_URI + file.getHash());

			if (response.responseCode() == 200) {
				String responseText = response.text();

				VTHash json = gson.fromJson(responseText, VTHash.class);
				if (json.responseCode == 1 && json.positives > 0) {
					logger.info("found VirusTotal: " + file.getPath());
					return true;
				}
			} else {
				logger.info("virusTotal returned: " + response.responseCode());
			}
		}
		return false;
	}

}
