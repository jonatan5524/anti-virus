package antiVirus.analyzer.yaraAnalyzer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
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
import lombok.ToString;

@Service
@ToString
public class YaraAnalyzer implements FileAnalyzer {

	private Collection<Yara> yaraRules;

	@Value("classpath:yaraAnalyze.py")
	private Resource resourcePythonScript;

	private String pythonScriptPath;

	@Value("${yara.pythonCommand}")
	private String pythonCommand;

	@Value("${yara.blacklist}")
	private String[] yaraBlacklist;

	@Value("classpath:YaraRules/*/*.yar")
	private Resource[] resourcesRules;

	@Value("${yara.maxYaraFoundHit}")
	private int maxYaraFoundHit;
	
	public YaraAnalyzer() {
		yaraRules = new ArrayList<Yara>();

	}

	@PostConstruct
	private void initPath() throws AntiVirusException {
		checkIfPythonInstalled();
		
		try {
			File pythonTempFile = creatingTempFile(resourcePythonScript);

			pythonScriptPath = pythonTempFile.getPath();
		} catch (IOException e) {
			throw new AntiVirusYaraException("error loading python yara script from resources", e);
		}

	}
	
	public void checkIfPythonInstalled() throws AntiVirusException {
		if(!System.getenv("PATH").contains("Python"))
			throw new AntiVirusException("python is not installed!");
	}

	@Override
	public boolean scanFile(FileDB file,Logger logger) throws AntiVirusYaraException {
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

	private boolean executeScript(Yara yara, String path) throws AntiVirusYaraException {

		Process p;
		String command = pythonCommand + " \"" + pythonScriptPath + "\" \"" + yara.getTempPath() + "\" \"" + path
				+ "\"";
		try {
			p = Runtime.getRuntime().exec(command);

		} catch (IOException e) {
			throw new AntiVirusYaraException(
					"error excuting python script one of these parameters incorrect: " + command, e);
		}

		BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

		BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

		return readFromProcess(stdInput, stdError);
	}

	private boolean readFromProcess(BufferedReader stdInput, BufferedReader stdError) throws AntiVirusYaraException {
		String output, outputTot = "";
		String error, errorTot = "";
		try {
			while ((output = stdInput.readLine()) != null) {
				outputTot += output;
			}

			while ((error = stdError.readLine()) != null) {
				errorTot += error;
			}
		} catch (IOException e) {

			throw new AntiVirusYaraException("exception reading from executable script", e);
		}

		if (!errorTot.isEmpty() && errorTot.contains("YaraSyntaxError ")) {
			throw new AntiVirusYaraException("exception from yara python script: " + errorTot);
		}

		if (!outputTot.isEmpty()) {
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

	private File creatingTempFile(Resource res) throws IOException {

		File tempFile = File.createTempFile(res.getFilename(), "");
		BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
		BufferedReader reader = new BufferedReader(new InputStreamReader(res.getInputStream()));

		String line;
		while ((line = reader.readLine()) != null) {
			writer.write(line);
			writer.newLine();
		}

		reader.close();
		writer.close();

		return tempFile;
	}

	private void addNewYara(Resource res) throws AntiVirusScanningException {

		try {
			File yaraTempFile = creatingTempFile(res);

			yaraRules.add(new Yara(yaraTempFile.getName(), yaraTempFile.getPath(), res.getURI().toString()));
		} catch (IOException e) {
			throw new AntiVirusScanningException("execption reading yara rule: " + res.getFilename(), e);
		}
	}

}
