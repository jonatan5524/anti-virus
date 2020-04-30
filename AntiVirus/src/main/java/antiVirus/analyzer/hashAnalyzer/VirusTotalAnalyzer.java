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
public class VirusTotalAnalyzer implements HashAnalyzer {

	private Gson gson;
	@Value("${virus-total.API_KEY}")
	private String API_KEY;
	@Value("${virus-total.VT_URL}")
	private String URL;
	
	public VirusTotalAnalyzer() {
		this.gson = new Gson();
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
