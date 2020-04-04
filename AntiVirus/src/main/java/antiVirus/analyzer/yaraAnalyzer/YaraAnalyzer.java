package antiVirus.analyzer.yaraAnalyzer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Scanner;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
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

	@Value("classpath:yaraAnalyze.py")
	private Resource resScript;
	private String scriptPath;
	@Value("${yara.pythonCommand}")
	private String pythonCommand;
	@Value("${yara.blacklist}")
	private String[] yaraBlacklist;

	@Value("classpath:YaraRules/*/*.yar")
	private Resource[] resourcesRules;

	public YaraAnalyzer() {
		yaraRules = new ArrayList<Yara>();
		algorithemTemplate = new ScanningBFS<File>();

	}

	@PostConstruct
	private void initPath() throws AntiVirusYaraException {
		try {
			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec(pythonCommand);
		} catch (IOException e) {
			pythonCommand = "py";
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(resScript.getInputStream()));
			File pythonTempFile = File.createTempFile(resScript.getFilename(), "");
			BufferedWriter writer = new BufferedWriter(new FileWriter(pythonTempFile));
			readingWritingTemp(reader, writer);
			reader.close();
			writer.close();

			scriptPath = pythonTempFile.getPath();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new AntiVirusYaraException("error loading python yara script from resources", e);
		}

	}

	@Override
	public boolean scanFile(FileDB file) throws AntiVirusYaraException {
		System.out.println("analyzing file - yara: " + file.getPath());
		int count = 0;
		for (Yara yara : yaraRules) {
			if (executeScript(yara, file.getPath())) {
				count++;
			//	System.out.print(yara.getName()+", ");
				if (Arrays.stream(yaraBlacklist).parallel().anyMatch(yara.getResPath()::contains)) {
					System.out.println("yara found from blacklist: " + yara.getName());
					return true;
				} else
				{
					System.out.println("yara found: " + yara.getName());
			
				}
			}
			if (count >= 3) {
				System.out.println("third yara found");
				// Scanner sc = new Scanner(System.in);
				// String name = sc.nextLine();
				return true;
			}

		}

		return false;
	}

	private boolean executeScript(Yara yara, String path) throws AntiVirusYaraException {

		Process p;
		try {
			p = Runtime.getRuntime()
					.exec(pythonCommand + " \"" + scriptPath + "\" \"" + yara.getTempPath() + "\" \"" + path + "\"");
			// System.out.println("command: "+pythonCommand + " \"" + scriptPath + "\" \"" +
			// yara.getTempPath() + "\" \"" + path + "\"");
		} catch (IOException e) {
			throw new AntiVirusYaraException("error excuting python script one of these parameters incorrect: "
					+ pythonCommand + " \"" + scriptPath + "\" \"" + yara.getTempPath() + "\" \"" + path + "\"", e);
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

		if (errorTot.length() != 0 && errorTot != " " && errorTot.contains("YaraSyntaxError ")) {
			System.out.println(errorTot);
		}

		if (outputTot.length() != 0 && outputTot != " ") {
			return true;
		}

		return false;
	}

	@PostConstruct
	private void initYaraRules() {
		URL folderURL = YaraAnalyzer.class.getClassLoader().getResource("YaraRules");
		System.out.println("path " + folderURL);

		System.out.println("yara rules:");
		for (Resource res : resourcesRules) {
			try {
				System.out.println(res);

				addNewYara(res);
			} catch (AntiVirusScanningException e) {
				e.printStackTrace();
			}
		}
	}

	private void readingWritingTemp(BufferedReader br, BufferedWriter bw) throws IOException {

		String line;
		while ((line = br.readLine()) != null) {
			bw.write(line);
			bw.newLine();
		}

	}

	private void addNewYara(Resource res) throws AntiVirusScanningException {

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(res.getInputStream()));

			File yaraTempFile = File.createTempFile(res.getFilename(), "");
			BufferedWriter writer = new BufferedWriter(new FileWriter(yaraTempFile));
			readingWritingTemp(reader, writer);
			reader.close();
			writer.close();

			yaraRules.add(new Yara(yaraTempFile.getName(), yaraTempFile.getPath(), res.getURI().toString()));
		} catch (IOException e) {
			throw new AntiVirusScanningException("execption reading yara rule: " + res.getFilename(), e);
		}
	}

}
