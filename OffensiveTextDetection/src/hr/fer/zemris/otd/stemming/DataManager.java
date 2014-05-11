package hr.fer.zemris.otd.stemming;

import hr.fer.zemris.otd.dataPreprocessing.Post;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * First use {@link #writePlainPosts(List, String)} to write posts text in file.
 * Then use {@link #stemPosts(String, String)} to stem words from posts.
 * 
 * @author Sven Vidak
 *
 */

public class DataManager {

	private static int id = 0;

	private Map<String, String> stemMapping;
	private Map<String, Integer> realMap;

	public DataManager() {
		this.stemMapping = new HashMap<>();
		this.realMap = new HashMap<>();
	}

	public void writePlainPosts(List<Post> posts, String filename)
			throws IOException {
		FileWriter fw = new FileWriter(filename);
		for (Post p : posts) {
			fw.write(p.getPostText());
		}
		fw.close();
	}

	public void stemPosts(String directory, String scriptName,
			String inputFile, String outputFile) throws IOException {
		ProcessBuilder process = new ProcessBuilder("python", scriptName,
				inputFile, outputFile);
		process.directory(new File(directory));
		process.start();
	}

	public void createMap(String path) throws IOException {
		File f = new File(path);
		List<String> lines = Files.readAllLines(f.toPath(),
				StandardCharsets.UTF_8);
		for (String l : lines) {
			String[] parts = l.trim().toLowerCase().split("\\s+");
			if (!stemMapping.containsKey(parts[0])) {
				stemMapping.put(parts[0].trim(), parts[1].trim());
			}
			if (!realMap.containsKey(parts[1].trim())) {
				realMap.put(parts[1].trim(), id++);
			}
		}
	}

	public Map<String, String> getStemMapping() {
		return stemMapping;
	}

	public Map<String, Integer> getRealMap() {
		return realMap;
	}
}
