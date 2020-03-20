package antiVirus.analyzer;

import antiVirus.entities.FileDB;

public interface FileAnalyzer {

	public boolean scanFile(FileDB file);
}
