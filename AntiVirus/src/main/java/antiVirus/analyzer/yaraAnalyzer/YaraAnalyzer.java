package antiVirus.analyzer.yaraAnalyzer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import antiVirus.analyzer.FileAnalyzer;
import antiVirus.entities.FileDB;
import antiVirus.exceptions.AntiVirusException;
import antiVirus.exceptions.AntiVirusScanningException;
import antiVirus.exceptions.AntiVirusYaraException;
import antiVirus.utils.Utils;
import lombok.ToString;

@Service
@ToString
public class YaraAnalyzer implements FileAnalyzer {

	private Collection<Yara> yaraRules;

	@Value("classpath:yaraAnalyze.py")
	private Resource resourcePythonScript;

	private String pythonScriptPath;

	@Value("${yara.pythonCommand}")
	private String[] pythonCommandOptions;

	@Value("${yara.blacklist}")
	private String[] yaraBlacklist;

	@Value("classpath:YaraRules/*/*.yar")
	private Resource[] resourcesRules;

	@Value("${yara.maxYaraFoundHit}")
	private int maxYaraFoundHit;

	private String pythonCommand;

	public YaraAnalyzer() {
		yaraRules = new ArrayList<Yara>();

	}

	@PostConstruct
	private void initPath() throws AntiVirusException {
		pythonCommand = Utils.isPythonInstalled(pythonCommandOptions);

		try {
			File pythonTempFile = Utils.creatingTempFile(resourcePythonScript);

			pythonScriptPath = pythonTempFile.getPath();
		} catch (IOException e) {
			throw new AntiVirusYaraException("error loading python yara script from resources", e);
		}

	}

	@Override
	public boolean scanFile(FileDB file, Logger logger) throws AntiVirusYaraException {
		logger.info("analyzing file - yara: " + file.getPath());
		int yaraRuleFound = 0;

		for (Yara yara : yaraRules) {
			if (executeScript(yara, file.getPath())) {
				yaraRuleFound++;

				if (isYaraRuleInBlackList(yara)) {
					logger.info("yara found from blacklist: " + yara.getName());
					return true;
				}

			}
			if (yaraRuleFound >= maxYaraFoundHit) {
				logger.info("third yara found");
				return true;
			}

		}

		return false;
	}

	public boolean isYaraRuleInBlackList(Yara yara) {
		return Arrays.stream(yaraBlacklist).parallel().anyMatch(yara.getResPath()::contains);
	}

	private boolean executeScript(Yara yara, String filePath) throws AntiVirusYaraException {
		String command = String.format("%s \"%s\" \"%s\" \"%s\"", pythonCommand, pythonScriptPath, yara.getTempPath(),
				filePath);

		// 1 - error, 0 - output
		String retVals[] = Utils.executeScript(command);

		if (!retVals[1].isEmpty() && retVals[1].contains("YaraSyntaxError ")) {
			throw new AntiVirusYaraException("exception from yara python script: " + retVals[1]);
		}

		if (!retVals[0].isEmpty()) {
			return true;
		}

		return false;
	}

	@PostConstruct
	private void initYaraRules() {

		for (Resource res : resourcesRules) {
			try {
				addNewYara(res);
			} catch (AntiVirusScanningException e) {
				e.printStackTrace();
			}
		}
	}

	private void addNewYara(Resource res) throws AntiVirusScanningException {

		try {
			File yaraTempFile = Utils.creatingTempFile(res);

			yaraRules.add(new Yara(yaraTempFile.getName(), yaraTempFile.getPath(), res.getURI().toString()));
		} catch (IOException e) {
			throw new AntiVirusScanningException("execption reading yara rule: " + res.getFilename(), e);
		}
	}

}
