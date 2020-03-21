package antiVirus.exceptions;

public class AntiVirusYaraException extends AntiVirusException{
	
	public AntiVirusYaraException(String massage) {
		super(massage);
	}
	
	public AntiVirusYaraException(String massage,Throwable err)
	{
		super(massage,err);
	}

}
