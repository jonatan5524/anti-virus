package AntiVirus.Analyzer.HashAnalyzer;

import javax.annotation.PostConstruct;

import org.javalite.http.Get;
import org.javalite.http.Http;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import AntiVirus.Analyzer.HashAnalyzer.JSONParser.VTHashJSON;
import AntiVirus.entities.FileDB;

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
	private void setURI()
	{
		this.VT_URI = VT_URL+"?apikey=" + API_KEY + "&resource=";
	}

	@Override
	public boolean scanFile(FileDB file) {
		if (!file.getHash().isBlank()) {
			System.out.println(file);
			Get response = Http.get(VT_URI + file.getHash());
			if (response.responseCode() == 200) {
				String responseText = response.text();

				VTHashJSON json = gson.fromJson(responseText, VTHashJSON.class);
				System.out.println(json);
				if (json.responseCode == 1 && json.positives > 0) {
					System.out.println(responseText);
					return true;
				}
			}
		}
		return false;
	}

}
