package antiVirus.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;

public class Utils {

	private static final int RADIX = 16;

	public static String getFileChecksum(MessageDigest digest, File file) {
		if (file.length() == 0) {
			return "";
		}
		byte[] bytes = readByteFromFile(digest, file);
		// This bytes[] has bytes in decimal format;
		// Convert it to hexadecimal format
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, RADIX).substring(1));
		}
		return sb.toString();
	}

	private static byte[] readByteFromFile(MessageDigest digest, File file) {
		try (FileInputStream fis = new FileInputStream(file)) {
			byte[] byteArray = new byte[(int) file.length()];
			int bytesCount = 0;

			while ((bytesCount = fis.read(byteArray)) != -1) {
				digest.update(byteArray, 0, bytesCount);
			}

			fis.close();

			return digest.digest();

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
