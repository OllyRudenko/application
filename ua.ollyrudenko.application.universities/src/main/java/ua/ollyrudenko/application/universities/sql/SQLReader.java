package ua.ollyrudenko.application.universities.sql;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SQLReader {

	private final static Logger logger = LoggerFactory.getLogger(SQLReader.class);

	public static String takeSQLCommand(String pathToSQLFile) {
		String[] s = null;
		String sql = null;
		try (Stream<String> streamFromFiles = Files.lines(Paths.get(pathToSQLFile), StandardCharsets.UTF_8)) {
			s = streamFromFiles.flatMap(p -> Arrays.asList(p.split("\n")).stream()).toArray(String[]::new);
			sql = Arrays.asList(s).stream().collect(Collectors.joining());
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("file is exist");
		}
		return sql;
	}
}
