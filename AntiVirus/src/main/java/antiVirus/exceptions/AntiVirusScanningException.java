package antiVirus.exceptions;

public class AntiVirusScanningException extends AntiVirusException{

	public AntiVirusScanningException(String massage) {
		super(massage);
	}

	public AntiVirusScanningException(String massage,Throwable err)
	{
		super(massage,err);
	}

	
}
