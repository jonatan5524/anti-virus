package antiVirus.utils;

import java.io.IOException;
import java.util.Dictionary;
import java.util.HashMap;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import antiVirus.analyzer.FileAnalyzer;

@Converter
public class HashMapConverter implements AttributeConverter<HashMap<FileAnalyzer, Boolean>, String> {

	@Autowired
	private ObjectMapper objectMapper;
	
	@Override
	public String convertToDatabaseColumn(HashMap<FileAnalyzer, Boolean> resultAnalyzer) {
		//ObjectMapper objectMapper = new ObjectMapper();
		String resultAnalyzerJson = null;
		try {
			resultAnalyzerJson = objectMapper.writeValueAsString(resultAnalyzer);

		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		System.out.println(resultAnalyzerJson);
		return resultAnalyzerJson;
	}

	@Override
	public HashMap<FileAnalyzer, Boolean> convertToEntityAttribute(String dbData) {

		HashMap<FileAnalyzer, Boolean> resultAnalyzer = null;
		//ObjectMapper objectMapper = new ObjectMapper();
		try {
			resultAnalyzer = objectMapper.readValue(dbData, HashMap.class);
		} catch (final IOException e) {
			e.printStackTrace();
		}

		return resultAnalyzer;
	}

}
