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
import antiVirus.analyzer.hashAnalyzer.jsonParser.VTHash.Data.Attributes;
import antiVirus.analyzer.hashAnalyzer.jsonParser.VTHash.Data.Attributes.TotalVotes;

@Service
public class VirusTotalAnalyzer extends HashAnalyzer {

	private Gson gson;

	@Autowired
	public VirusTotalAnalyzer(@Value("${virus-total.VT_URL}") String URL,
			@Value("${virus-total.API_KEY}") String API_KEY) {
		this.gson = new Gson();
		this.URL = URL;
		this.API_KEY = API_KEY;
	}
	
	public boolean scanFile(FileDB file,Logger logger) {
		if (!file.getHash().isEmpty()) {
			logger.info("analyzing file - totalVirus: " + file.getPath());

			Get response = Http.get(URL + file.getHash()).header("X-Apikey", API_KEY);

			return parseResponse(response,logger,file);
		}
		return false;
	}

	@Override
	public boolean parseResponse(Get response, Logger logger, FileDB file) {
		
		if (response.responseCode() == 200) {
			String responseText = response.text();

			VTHash json = gson.fromJson(responseText, VTHash.class);
			TotalVotes totalVotes = json.data.attributes.total_votes;
			if (totalVotes.malicious > totalVotes.harmless ) {
				logger.info("found VirusTotal: " + file.getPath());
				return true;
			}
		} else {
			logger.warning("virusTotal returned: " + response.responseCode());
		}
		return false;
	}

}
