package antiVirus.analyzer.yaraAnalyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import antiVirus.analyzer.FileAnalyzer;
import antiVirus.entities.FileDB;
import antiVirus.exceptions.AntiVirusScanningException;
import antiVirus.exceptions.AntiVirusYaraException;
import antiVirus.scanner.fileFolderHandler.scanningAlgorithem.ScanningAlgorithemTemplate;
import antiVirus.scanner.fileFolderHandler.scanningAlgorithem.ScanningBFS;
import lombok.ToString;

@Service
@ToString
public class YaraAnalyzer implements FileAnalyzer {

	private Collection<Yara> yaraRules;
	private ScanningAlgorithemTemplate<File> algorithemTemplate;
	@Value("${yara.scriptPath}")
	private String scriptPath;
	@Value("${yara.pythonCommand}")
	private String pythonCommand;

	public YaraAnalyzer() {
		yaraRules = new ArrayList<Yara>();
		algorithemTemplate = new ScanningBFS<File>();

	}

	@PostConstruct
	private void initPath() {
		scriptPath = System.getProperty("user.dir") + scriptPath;
		try {
			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec(pythonCommand);
		} catch (IOException e) {
			pythonCommand = "py";
		}

	}

	@Override
	public boolean scanFile(FileDB file) throws AntiVirusYaraException {
		System.out.println("analyzing file: " + file.getPath());
		int count = 0;
		for (Yara yara : yaraRules) {
			if (executeScript(yara, file.getPath())) {
				System.out.println("yara found: " + yara.getName());
				count++;
			}
			if (count >= 3) {
				 Scanner sc = new Scanner(System.in);
				 String name = sc.nextLine();
				return true;
			}

		}

		return false;
	}

	private boolean executeScript(Yara yara, String path) throws AntiVirusYaraException {

		Process p;
		try {
			p = Runtime.getRuntime()
					.exec(pythonCommand + " \"" + scriptPath + "\" \"" + yara.getPath() + "\" \"" + path + "\"");
		} catch (IOException e) {
			throw new AntiVirusYaraException("error excuting python script one of these parameters incorrect: "
					+ pythonCommand + " \"" + scriptPath + "\" \"" + yara.getPath() + "\" \"" + path + "\"", e);
		}

		BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

		BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

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
			// TODO Auto-generated catch block
			throw new AntiVirusYaraException("exception reading from executable script", e);
		}

		if (!errorTot.isBlank() && !errorTot.isEmpty() && errorTot.contains("YaraSyntaxError ")) {
			System.out.println(errorTot);
		}

		if (!outputTot.isBlank() && !outputTot.isEmpty()) {
			return true;
		}

		return false;
	}

	@PostConstruct
	private void initYaraRules() {
		URL folderURL = YaraAnalyzer.class.getClassLoader().getResource("YaraRules");
		System.out.println("path " + folderURL);

		// go over the YARARules Folder
		File dir = new File(folderURL.getPath());
		algorithemTemplate.init();
		algorithemTemplate.add(dir);

		System.out.println("yara rules:");
		while (!algorithemTemplate.isEmpty()) {
			dir = algorithemTemplate.remove();
			System.out.println("current dir: " + dir.getPath());
			for (File file : dir.listFiles()) {
				if (file.isDirectory()) {
					algorithemTemplate.add(file);
				} else {
					try {
						System.out.println(file.getName());

						addNewYara(file);
					} catch (AntiVirusScanningException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	private void addNewYara(File file) throws AntiVirusScanningException {

		try {
			String text = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
			yaraRules.add(new Yara(file.getName(), file.getPath(), text));
		} catch (IOException e) {
			throw new AntiVirusScanningException("execption reading yara rule: " + file.getName(), e);
		}
	}

}
