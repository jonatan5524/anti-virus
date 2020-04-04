package antiVirus.exceptions;

public class AntiVirusAnalyzeException extends AntiVirusException {

	public AntiVirusAnalyzeException(String massage) {
		super(massage);
	}

	public AntiVirusAnalyzeException(String massage,Throwable err)
	{
		super(massage,err);
	}

}
