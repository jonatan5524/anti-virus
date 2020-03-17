package AntiVirus.Analyzer.YaraAnalyzer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString(exclude = {"rule","path"})
@Getter
@AllArgsConstructor
public class Yara {
	
	String name;
	String path;
	String rule;
}
