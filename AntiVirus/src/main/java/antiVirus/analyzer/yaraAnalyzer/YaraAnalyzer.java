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

	public YaraAnalyzer() {
		yaraRules = new ArrayList<Yara>();
		algorithemTemplate = new ScanningBFS<File>();

	}

	@PostConstruct
	private void initPath() {
		scriptPath = System.getProperty("user.dir") + scriptPath;
	}

	@Override
	public boolean scanFile(FileDB file) {
		System.out.println("analyzing file: " + file.getPath());
		int count = 0;
		for (Yara yara : yaraRules) {
			if (executeScript(yara, file.getPath())) {
				System.out.println("yara found: " + yara.getName());
				count++;
			}
			if (count >= 3) {
				// Scanner sc = new Scanner(System.in);
				// String name = sc.nextLine();
				return true;
			}

		}

		return false;
	}

	private boolean executeScript(Yara yara, String path) {
		try {
			Process p = Runtime.getRuntime()
					.exec("python \"" + scriptPath + "\" \"" + yara.getPath() + "\" \"" + path + "\"");

			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

			BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

			String output, outputTot = "";
			while ((output = stdInput.readLine()) != null) {
				outputTot += output;
			}
			String error, errorTot = "";
			while ((error = stdError.readLine()) != null) {
				errorTot += error;
			}

			if (!errorTot.isBlank() && !errorTot.isEmpty() && errorTot.contains("YaraSyntaxError ")) {
				System.out.println(errorTot);
			}

			if (!outputTot.isBlank() && !outputTot.isEmpty()) {
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
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
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	private void addNewYara(File file) throws IOException {
		String text = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
		yaraRules.add(new Yara(file.getName(), file.getPath(), text));
	}

}
