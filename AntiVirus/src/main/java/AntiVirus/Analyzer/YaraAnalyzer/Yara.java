package AntiVirus.Analyzer.YaraAnalyzer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString(exclude = {"rule","path"})
@Getter
@AllArgsConstructor
public class Yara {
	
	private String name;
	private String path;
	private String rule;
}
