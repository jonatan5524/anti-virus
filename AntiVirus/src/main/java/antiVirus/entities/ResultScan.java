package antiVirus.entities;

import java.io.IOException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import antiVirus.analyzer.FileAnalyzer;
import antiVirus.exceptions.AntiVirusException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "result_scan")
@ToString(exclude = { "filedb" })
@NoArgsConstructor
public class ResultScan {

	@Id
	@Column(name = "id")
	private Long id;

	@Setter
	@OneToOne
    @MapsId
	private FileDB filedb;

	@Getter
	@Setter
	private boolean result;

	private String resultAnalyzerJSON;

	@Getter
	@Setter
	@Transient
	private Map<FileAnalyzer, Boolean> resultAnalyzer;

	public void serializeResultAnalyzer() throws AntiVirusException  {
	    try {
			this.resultAnalyzerJSON = new ObjectMapper().writeValueAsString(resultAnalyzer);
		} catch (JsonProcessingException e) {
			throw new AntiVirusException("exception in serializing resultAnalyzer: "+resultAnalyzer,e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void deserializeResultAnalyzer() throws AntiVirusException  {
		try {
		if(resultAnalyzer == null)
			this.resultAnalyzer = new HashMap<FileAnalyzer, Boolean>();
	    this.resultAnalyzer = new ObjectMapper().readValue(resultAnalyzerJSON, HashMap.class);
		}
		catch(IOException e)
		{
			throw new AntiVirusException("exception in deserializing resultAnalyzer: "+resultAnalyzerJSON,e);
		}
	}
	
}
