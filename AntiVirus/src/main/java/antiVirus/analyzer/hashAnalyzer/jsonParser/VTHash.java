package antiVirus.analyzer.hashAnalyzer.jsonParser;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class VTHash {
	@SerializedName("response_code")
	@Expose
	public Integer responseCode;
	@SerializedName("permalink")
	@Expose
	public String permalink;
	@SerializedName("total")
	@Expose
	public Integer total;
	@SerializedName("positives")
	@Expose
	public Integer positives;
}
