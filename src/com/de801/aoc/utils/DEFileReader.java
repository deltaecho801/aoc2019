package com.de801.aoc.utils;

import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class DEFileReader {

	public static List<String> getInputsFromFile(String inputUrl) {
		try {
            URI uri = new URI(inputUrl);
            return Files.readAllLines(Paths.get(inputUrl), Charset.defaultCharset());			
		} catch (Exception e) {
			throw new RuntimeException("Failed to read from file: " + e.getMessage());
		}
	}
}
