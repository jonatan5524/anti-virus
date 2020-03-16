package AntiVirus.entities;

import java.util.Dictionary;

import AntiVirus.Analyzer.FileAnalyzer;
import lombok.Getter;

public class ResultScan {

	@Getter
	private boolean result;
	@Getter
	private Dictionary<FileAnalyzer, Boolean> resultAnalyzer;

}
