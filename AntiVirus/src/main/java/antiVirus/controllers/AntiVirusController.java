package antiVirus.controllers;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import antiVirus.exceptions.AntiVirusAnalyzeException;
import antiVirus.exceptions.AntiVirusException;
import antiVirus.exceptions.AntiVirusUserException;
import antiVirus.scanner.ScannerScheduler;
import antiVirus.scanner.UserRequestScanner;
import antiVirus.utils.Utils;
import antlr.collections.List;

@CrossOrigin
@RestController
public class AntiVirusController {

	@Autowired
	private UserRequestScanner userRequestScanner;
	@Autowired
	private ScannerScheduler scannerScheduler;

	@GetMapping("/userScan/initDirectoryPathScan")
	public void setInitDirectoryPathScan(@RequestParam String path) throws AntiVirusUserException {
		userRequestScanner.setInitDirectoryPath(path);
	}

	@GetMapping("/userScan/start")
	public void startUserScan() throws AntiVirusAnalyzeException, AntiVirusUserException {
		userRequestScanner.startScan();
	}
	
	@GetMapping("/userScan/virusFoundList")
	public Collection<String> getUserScanVirusFoundList() {
		return userRequestScanner.getAnalyzer().getVirusFoundList();
	}
	
	@GetMapping("/userScan/virusSuspiciousList")
	public Collection<String> getUserScanVirusSuspiciousList() {
		return userRequestScanner.getAnalyzer().getVirusSuspiciousList();
	}
	
	@GetMapping("/scheduleScan/virusFoundList")
	public Collection<String> getScheduleScanVirusFoundList() {
		return scannerScheduler.getAnalyzer().getVirusFoundList();
	}
	
	@GetMapping("/scheduleScan/virusSuspiciousList")
	public Collection<String> getScheduleScanVirusSuspiciousList() {
		return scannerScheduler.getAnalyzer().getVirusSuspiciousList();
	}

	@GetMapping("/userScan/status")
	public boolean getUserScanStatus() {
		return userRequestScanner.isActiveScanning();
	}
	
	@GetMapping("/scheduleScan/status")
	public boolean getScheduleScanStatus() {
		return scannerScheduler.isActiveScanning();
	}
	
	@GetMapping(value = "/userScan/log", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public @ResponseBody byte[] getLogUserScan() throws AntiVirusException {
		String path = userRequestScanner.getLoggerPath();
		return Utils.readByteArrayFromFile(path);
	}
	
	@GetMapping(value = "/scheduleScan/log", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public @ResponseBody byte[] getLogScheduleScan() throws AntiVirusException {
		String path = scannerScheduler.getLoggerPath();
		return Utils.readByteArrayFromFile(path);
	}

	@ExceptionHandler(AntiVirusUserException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public String userException(AntiVirusUserException ex) {
		return ex.getMessage();
	}

	@ExceptionHandler(AntiVirusException.class)
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	public String userException(AntiVirusException ex) {
		return ex.getMessage();
	}
}
