package AntiVirus.Analyzer;

import AntiVirus.entities.FileDB;

public interface FileAnalyzer {

	public boolean scanFile(FileDB file);
}
