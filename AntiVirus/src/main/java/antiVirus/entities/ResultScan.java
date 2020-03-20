package antiVirus.entities;

import java.util.Dictionary;
import java.util.Hashtable;

import javax.inject.Inject;

import antiVirus.analyzer.FileAnalyzer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class ResultScan {

	@Getter
	@Setter
	private boolean result;

	@Getter
	private Dictionary<FileAnalyzer, Boolean> resultAnalyzer = new Hashtable<FileAnalyzer, Boolean>();
	
}
