package AntiVirus.Analyzer.YaraAnalyzer;

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

import AntiVirus.Analyzer.FileAnalyzer;
import AntiVirus.Scanner.FileFolderHandler.ScanningAlgorithem.ScanningAlgorithemTemplate;
import AntiVirus.Scanner.FileFolderHandler.ScanningAlgorithem.ScanningBFS;
import AntiVirus.entities.FileDB;
import lombok.ToString;

@ToString
public class YaraAnalyzer implements FileAnalyzer {

	private Collection<Yara> yaraRules;
	private ScanningAlgorithemTemplate<File> algorithemTemplate;
	private final static String scriptPath = System.getProperty("user.dir") + "\\src\\main\\resources\\yaraAnalyze.py";

	public YaraAnalyzer() {
		yaraRules = new ArrayList<Yara>();
		algorithemTemplate = new ScanningBFS<File>();
		initYara();
	}

	@Override
	public boolean scanFile(FileDB file) {
		System.out.println("analyzing file: " + file.getPath());
		for (Yara yara : yaraRules) {
			if (executeScript(yara, file.getPath())) {
				System.out.println("found!!!! " + yara.name);
				Scanner sc = new Scanner(System.in);
				String name = sc.nextLine();
				return true;
			} else
				System.out.println("tried: " + yara.name);
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

			if (!errorTot.isBlank() && !errorTot.isEmpty())
				System.out.println(errorTot);

			if (!outputTot.isBlank() && !outputTot.isEmpty()) {
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	private void initYara() {
		URL folderURL = YaraAnalyzer.class.getClassLoader().getResource("YaraRules");
		System.out.println("path " + folderURL);
		File dir = new File(folderURL.getPath());
		algorithemTemplate.init();
		algorithemTemplate.add(dir);
		String text;
		System.out.println("yara rules:");
		while (!algorithemTemplate.isEmpty()) {
			dir = algorithemTemplate.remove();
			System.out.println("current dir: " + dir.getPath());
			for (File file : dir.listFiles()) {
				if (file.isDirectory())
					algorithemTemplate.add(file);
				else {
					try {
						System.out.println(file.getName());
						text = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
						yaraRules.add(new Yara(file.getName(), file.getPath(), text));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

}
