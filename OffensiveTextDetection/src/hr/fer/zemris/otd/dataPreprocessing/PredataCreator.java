package hr.fer.zemris.otd.dataPreprocessing;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class in which posts list and word map are created.
 * 
 * It's a general class for making crucial pieces of data used in this project.
 * 
 * @author Sven Vidak
 *
 */
public class PredataCreator {

	static int id = 0;

	private Map<String, Integer> wordMap;
	private List<Post> posts;

	public PredataCreator() {
		wordMap = new HashMap<String, Integer>();
		posts = new ArrayList<>();
	}

	/**
	 * Method creates word map in format: word - id
	 * 
	 * @param path
	 *            Path to file which contains list of all words. Method which
	 *            provides such file is
	 *            {@link WordExctractor#getAllWords(String, String)}
	 * @throws IOException
	 *             If there is something wrong with given path.
	 */
	public void createMap(String path) throws IOException {
		File f = new File(path);
		List<String> lines = Files.readAllLines(f.toPath(),
				StandardCharsets.UTF_8);
		for (String l : lines) {
			String word = l.trim().toLowerCase();
			if (this.wordMap.containsKey(word)) {
				continue;
			}
			this.wordMap.put(word, id++);
		}
	}

	// newly created method which works as it should (method above does not)
	public void createMap(List<Post> posts) {
		for (Post p : posts) {
			for (String w : p.getPostText().split("\\p{Z}")) {
				String word = w.trim().toLowerCase();
				if (!isNotRubbish(word) || this.wordMap.containsKey(word)) {
					continue;
				}
				this.wordMap.put(word, id++);
			}
		}
	}

	/**
	 * Method creates list of all posts in given dataset.
	 * 
	 * @param dataset
	 *            Path to dataset in specified format.
	 * @throws IOException
	 *             If there is something wrong with given path.
	 */
	public void createPostsList(String dataset) throws IOException {
		File f = new File(dataset);
		List<String> lines = Files.readAllLines(f.toPath(),
				StandardCharsets.UTF_8);
		int size = lines.size();
		boolean isPost = false;
		Post p = null;
		StringBuilder sb = null;
		for (int i = 0; i < size;) {
			String line = lines.get(i).trim();
			if (line.startsWith("#####")) {
				if (isPost) {
					p.setPostText(sb.toString());
					posts.add(p);
					p = null;
					sb = null;
					isPost = !isPost;
				} else {
					if ((i + 1) >= size) {
						break;
					}
					char[] labels = readLabels(lines.get(++i).trim());
					p = new Post(labels.length);
					p.setLabels(labels);
					sb = new StringBuilder();
					i++;
					isPost = !isPost;
				}
			} else {
				sb.append(line + "\n ");
				i++;
			}
		}
	}

	// method reads post labels
	private char[] readLabels(String labelLine) {
		char[] labels = new char[labelLine.split("\\p{Z}").length];
		int i = 0;
		for (Character c : labelLine.toCharArray()) {
			if (c == '1' || c == '0' || c == 'x') {
				labels[i++] = c;
			}
		}
		return labels;
	}

	private static boolean isNotRubbish(String w) {
		Pattern p = Pattern.compile("@?\\p{L}+(\\d+)?|\\d+");
		Matcher m = p.matcher(w);
		return m.matches();
	}

	// standard getters

	public Map<String, Integer> getWordMap() {
		return wordMap;
	}

	public List<Post> getPosts() {
		return posts;
	}
}
