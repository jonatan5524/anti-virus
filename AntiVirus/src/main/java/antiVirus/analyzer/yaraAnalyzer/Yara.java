package antiVirus.analyzer.yaraAnalyzer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString(exclude = {"tempPath"})
@Getter
@AllArgsConstructor
public class Yara {
	
	private String name;
	private String tempPath;
	private String resPath;
	//private String rule;
}
