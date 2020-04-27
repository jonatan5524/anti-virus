package antiVirus.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.security.MessageDigest;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;

import antiVirus.exceptions.AntiVirusException;

public class Utils {

	private static final int RADIX = 16;
	private static String OS = System.getProperty("os.name").toLowerCase();

	private static final int READ_BYTE_CHUNK_READ = 1024 * 1024;

	public static String getFileChecksum(MessageDigest digest, File file) {
		try {

			if (file.length() == 0) {
				return "";
			}

			byte[] bytes = readByteFromFileHash(digest, file);

			// This bytes[] has bytes in decimal format;
			// Convert it to hexadecimal format
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, RADIX).substring(1));
			}

			return sb.toString();
		} catch (IOException e) {
			return "";
		}
	}

	private static byte[] readByteFromFileHash(MessageDigest digest, File file) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		byte[] byteArray;
		if (file.length() > READ_BYTE_CHUNK_READ) {
			byteArray = new byte[READ_BYTE_CHUNK_READ];
		} else {
			byteArray = new byte[(int) file.length()];
		}

		int bytesCount = 0;
		while ((bytesCount = fis.read(byteArray)) != -1) {
			digest.update(byteArray, 0, bytesCount);
		}

		fis.close();

		return digest.digest();

	}

	public static byte[] readByteArrayFromFile(String path) throws AntiVirusException {
		try {
			InputStream in = new FileInputStream(new File(path));
			return IOUtils.toByteArray(in);
		} catch (IOException e) {
			throw new AntiVirusException("error reading log file " + path, e);
		}
	}

	public static String convertPathToValidMySQLSearch(String path) {
		return path.replace("\\", "%");
	}

	public static boolean isWindows() {

		return (OS.indexOf("win") >= 0);

	}

	public static boolean isUnix() {

		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0);

	}

	public static void isPythonInstalled() throws AntiVirusException {
		try {
			testCommand("python --version");
		} catch (IOException e) {
			try {
				testCommand("python3 --version");
			} catch (IOException e1) {
				throw new AntiVirusException("python 3 is not installed!");
			}
			
		}
	}
	
	private static void testCommand(String command) throws IOException {
		Runtime rt = Runtime.getRuntime();
		BufferedReader stdInput;
		Process proc;
		proc = rt.exec(command);
		stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		String output = readFromProcess(stdInput);
		if(!output.startsWith("Python 3"))
			throw new IOException();
	}
	
	public static String readFromProcess(BufferedReader bufferedReader) throws IOException {
		String output = "",outputTot="";
		while ((output = bufferedReader.readLine()) != null) {
			outputTot += output;
		}
		
		return outputTot;
	}

	public static void emptyFile(String path) throws AntiVirusException {
		PrintWriter writer;
		try {
			writer = new PrintWriter(new File(path));
			writer.print("");
			writer.close();
		} catch (FileNotFoundException e) {
			throw new AntiVirusException("unable to delete log file: " + path, e);
		}
	}

}
