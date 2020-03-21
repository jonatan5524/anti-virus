package antiVirus.exceptions;

public class AntiVirusException extends Exception {

	public AntiVirusException(String massage) {
		super(massage);
	}
	
	public AntiVirusException(String massage,Throwable err)
	{
		super(massage,err);
	}

}
