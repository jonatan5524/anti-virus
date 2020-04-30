package antiVirus.logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;

public class LoggerFormatter extends Formatter {

	@Value("${logger.dateFormat}")
	private String timeFormat;
	private DateFormat dateFormat;
	@Value("${logger.initStringCapacity}")
	private int initStringCapacity;
	
	@PostConstruct
    private void setDateFormat() {
    	dateFormat = new SimpleDateFormat(timeFormat);
    }

	public String format(LogRecord record) {
		StringBuilder builder = new StringBuilder(initStringCapacity);
		builder.append(dateFormat.format(new Date(record.getMillis()))).append(" - ");
		builder.append("[").append(record.getLevel()).append("] - ");
		builder.append(formatMessage(record));
		builder.append("\n");
		return builder.toString();
	}

	public String getHead(Handler h) {
		return super.getHead(h);
	}

	public String getTail(Handler h) {
		return super.getTail(h);
	}
}