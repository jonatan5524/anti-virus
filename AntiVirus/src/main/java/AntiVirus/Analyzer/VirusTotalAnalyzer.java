package AntiVirus.Analyzer;

import AntiVirus.FileFolderHandler.entities.FileDB;

public class VirusTotalAnalyzer implements HashAnalyzer{

	@Override
	public boolean scanFile(FileDB file) {
		return true;
	}
	
}
