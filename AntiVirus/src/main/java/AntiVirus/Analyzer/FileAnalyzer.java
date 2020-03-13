package AntiVirus.Analyzer;

import AntiVirus.FileFolderHandler.entities.FileDB;

public interface FileAnalyzer {
	
	public boolean scanFile(FileDB file);
}
