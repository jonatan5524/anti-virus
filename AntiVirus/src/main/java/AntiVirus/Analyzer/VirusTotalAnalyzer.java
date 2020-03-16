package AntiVirus.Analyzer;

import org.javalite.http.Get;
import org.javalite.http.Http;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import AntiVirus.entities.FileDB;

public class VirusTotalAnalyzer implements HashAnalyzer {

	private static final String API_KEY = "97b1d615779a154221aec4c0a12bda91601b7bcb3c11568ec8010055d4dffd7b";
	private static final String URI = "https://www.virustotal.com/vtapi/v2/file/report?apikey=" + API_KEY
			+ "&resource=";
	private Gson gson = new Gson();

	@Override
	public boolean scanFile(FileDB file) {
		if (!file.getHash().isBlank()) {
			System.out.println(file);
			Get response = Http.get(URI + file.getHash());
			String responseText = response.text();

			JsonObject json = gson.fromJson(responseText, JsonObject.class);
			if (json.get("response_code").getAsInt() == 1 && json.get("positives").getAsInt() > 0) {
				System.out.println(responseText);
				return true;
			}
		}
		return false;
	}

}
