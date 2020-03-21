package antiVirus.analyzer;

import antiVirus.entities.FileDB;
import antiVirus.exceptions.AntiVirusException;

public interface FileAnalyzer {

	public boolean scanFile(FileDB file) throws AntiVirusException;
}
