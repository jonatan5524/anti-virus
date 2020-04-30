package antiVirus.analyzer;

import java.util.logging.Logger;

import antiVirus.entities.FileDB;
import antiVirus.exceptions.AntiVirusException;

public interface FileAnalyzer {

	public boolean scanFile(FileDB file,Logger logger);
}
