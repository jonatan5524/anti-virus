package antiVirus.analyzer.hashAnalyzer.jsonParser;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class VTHash {
	
	public Data data;
	
	public class Data{
		
		@SerializedName("attributes")
		public Attributes attributes;
		
		public class Attributes{
			
			@SerializedName("total_votes")
			public TotalVotes total_votes;
			
			public class TotalVotes{
				
				@SerializedName("harmless")
				public Integer harmless;

				@SerializedName("malicious")
				public Integer malicious;
			}
		}
	}
	


}
