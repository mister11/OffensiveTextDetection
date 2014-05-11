package hr.fer.zemris.otd.vectors;

import hr.fer.zemris.otd.dataPreprocessing.Post;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VectorCreator {

	private Map<String, String> stemMapping;
	private Map<String, Integer> realMap;
	private List<Post> posts;

	/**
	 * Before creation of an object of this class, you should call any method
	 * which build word map
	 */
	public VectorCreator(Map<String, String> stemMapping,
			Map<String, Integer> realMap, List<Post> posts) {
		if ((stemMapping != null && stemMapping.isEmpty()) || realMap.isEmpty()) {
			System.err
					.println("You should create map before creation of this object. Please read documetation for this constructor.");
			System.exit(-1);
		}
		this.stemMapping = stemMapping;
		this.realMap = realMap;
		this.posts = posts;
	}

	public List<PostVector> createOccurrenceVectors() throws IOException {
		List<PostVector> occurrenceVectors = new ArrayList<>();
		int cnt = 0;
		for (Post p : posts) {
			System.out.println("occur " + (++cnt));
			occurrenceVectors.add(createOccurrenceVector(p));
		}
		return occurrenceVectors;
	}

	public List<PostVector> createTfIdfVectors() throws IOException {
		List<PostVector> tfIdfVectors = new ArrayList<>();
		int cnt = 0;
		if (stemMapping == null) {
			for (Post p : posts) {
				System.out.println("tfidf " + (++cnt));
				tfIdfVectors.add(createTfIdfVector(p));
			}
		} else {
			for (Post p : posts) {
				System.out.println("tfidf " + (++cnt));
				tfIdfVectors.add(createTfIdfVectorStemmed(p));
			}
		}
		return tfIdfVectors;
	}

	public void normalizeVectors(List<PostVector> vectors) {
		double[] maxValues = getMaxValues(vectors);
		int listSize = vectors.size();
		int vectorSize = maxValues.length;
		for (int i = 0; i < listSize; i++) {
			PostVector v = vectors.get(i);
			for (int j = 0; j < vectorSize; j++) {
				double oldValue = v.getValue(j);
				v.setValue(j, oldValue / maxValues[j]);
			}
		}
	}

	public List<PostVector> nNormalizeVectors(List<PostVector> vectors) {
		List<PostVector> postVectors = new ArrayList<>();
		double[] maxValues = getMaxValues(vectors);
		int listSize = vectors.size();
		int vectorSize = maxValues.length;
		for (int i = 0; i < listSize; i++) {
			PostVector v = vectors.get(i);
			PostVector newVector = new PostVector(v.getLabels().length,
					realMap.size());
			newVector.setLabels(v.getLabels());
			for (int j = 0; j < vectorSize; j++) {
				double oldValue = v.getValue(j);
				newVector.setValue(j, oldValue / maxValues[j]);
			}
			postVectors.add(newVector);
		}
		return postVectors;
	}

	public void saveVectorsInFile(List<PostVector> vectors, String path)
			throws IOException {
		if (new File(path).exists()) {
			System.err
					.println("File already exists. If it is file with vectors inside, you can use method to read it.");
			return;
		}
		FileWriter fw = new FileWriter(path);
		for (PostVector v : vectors) {
			writeLabels(fw, v.getLabels());
			writeValues(fw, v.getValues());
		}
		fw.close();
	}

	public List<PostVector> getVectorsFromFile(String path) throws IOException {
		if (!new File(path).exists()) {
			return null;
		}
		List<String> lines = Files.readAllLines(new File(path).toPath(),
				StandardCharsets.UTF_8);
		List<PostVector> vectors = new ArrayList<>();
		int i = 0;
		int size = lines.size();
		for (; i < size;) {
			String[] labels = lines.get(i++).split(",");
			String[] values = lines.get(i++).split(",");
			// TODO - if ever is gonna be used, watch out for two types map
			PostVector v = new PostVector(labels.length, realMap.size());
			setLabels(v, labels);
			setValues(v, values);
			vectors.add(v);
		}
		return vectors;
	}

	private void setValues(PostVector v, String[] values) {
		int size = values.length;
		for (int i = 0; i < size; i++) {
			String val = values[i].trim();
			if (val.isEmpty()) {
				continue;
			}
			double value = Double.parseDouble(val);
			v.setValue(i, value);
		}
	}

	private void setLabels(PostVector v, String[] labels) {
		int size = labels.length;
		for (int i = 0; i < size; i++) {
			String val = labels[i].trim();
			if (val.isEmpty()) {
				continue;
			}
			char l = val.charAt(0);
			v.setLabel(i, l);
		}
	}

	private double[] getMaxValues(List<PostVector> vectors) {
		double[] maxValues = new double[vectors.get(0).getValues().length];
		int listSize = vectors.size();
		int vectorSize = maxValues.length;
		for (int j = 0; j < vectorSize; j++) {
			double maxValue = 0;
			for (int i = 0; i < listSize; i++) {
				maxValue = Math.max(maxValue, vectors.get(i).getValue(j));
			}
			maxValues[j] = maxValue;
		}
		return maxValues;
	}

	private void writeValues(FileWriter fw, double[] values) throws IOException {
		for (Double v : values) {
			fw.write(String.valueOf(v) + ",");
		}
		fw.write(System.lineSeparator());
	}

	private void writeLabels(FileWriter fw, char[] labels) throws IOException {
		for (Character c : labels) {
			fw.write(c.toString() + ",");
		}
		fw.write(System.lineSeparator());
	}

	private PostVector createTfIdfVector(Post post) {
		PostVector postVector = new PostVector(post.getLabels().length,
				realMap.size());
		int numberOfPost = posts.size();
		for (String word : realMap.keySet()) {
			double tfidf = 0.0;
			int wordPostOccurrences = getWordPostOccurrences(word, post);
			if (wordPostOccurrences > 0) {
				int postWordOccurrences = getPostWordOccurrences(word);
				double log = Math.log(1.0 * numberOfPost / postWordOccurrences);
				tfidf = 1.0 * wordPostOccurrences * log;
			}
			postVector.setValue(realMap.get(word), tfidf);
		}
		postVector.setLabels(post.getLabels());
		return postVector;
	}

	private PostVector createTfIdfVectorStemmed(Post post) {
		PostVector postVector = new PostVector(post.getLabels().length,
				realMap.size());
		Map<String, Integer> stemWordPost = new HashMap<>();
		Map<String, Integer> stemPostWord = new HashMap<>();
		for (String word : stemMapping.keySet()) {
			int wordPostOccurrences = getWordPostOccurrences(word, post);
			String stemWord = stemMapping.get(word);
			if (stemWordPost.get(stemWord) == null) {
				stemWordPost.put(stemWord, wordPostOccurrences);
			} else {
				stemWordPost.put(stemWord, stemPostWord.get(stemWord)
						+ wordPostOccurrences);
			}
			if (stemWordPost.get(stemWord) > 0) {
				int postWordOccurrences = getPostWordOccurrences(word);
				if (stemPostWord.get(stemWord) == null) {
					stemPostWord.put(stemWord, postWordOccurrences);
				} else {
					stemPostWord.put(stemWord, stemPostWord.get(stemWord)
							+ postWordOccurrences);
				}
			}
		}
		int numberOfPost = posts.size();
		for (String stem : realMap.keySet()) {
			double tfidf = 0.0;
			double log = Math.log(1.0 * numberOfPost / stemPostWord.get(stem));
			tfidf = 1.0 * stemWordPost.get(stem) * log;
			postVector.setValue(realMap.get(stem), tfidf);
		}
		return postVector;
	}

	private int getWordPostOccurrences(String word, Post post) {
		int counter = 0;
		for (String postWord : post.getPostText().split("\\p{Z}")) {
			if (word.equals(postWord.trim().toLowerCase())) {
				counter++;
			}
		}
		return counter;
	}

	private int getPostWordOccurrences(String word) {
		int counter = 0;
		for (Post post : posts) {
			for (String postWord : post.getPostText().split("\\p{Z}")) {
				if (word.equals(postWord.trim().toLowerCase())) {
					counter++;
					break;
				}
			}
		}
		return counter;
	}

	private PostVector createOccurrenceVector(Post post) {
		PostVector postVector = new PostVector(post.getLabels().length,
				realMap.size());

		for (String w : post.getPostText().split("\\p{Z}")) {
			String word = w.trim().toLowerCase();
			// double negation ftw...not (not rubbish) == rubbish
			if (!isNotRubbish(word)) {
				continue; // we are not interested in parts that are not real
							// words
			}
			if (stemMapping == null && !realMap.containsKey(word)) {
				continue;
			}
			if (stemMapping != null
					&& !realMap.containsKey(stemMapping.get(word))) {
				continue;
			}
			int position;
			if (stemMapping == null) {
				position = realMap.get(word);
			} else {
				position = realMap.get(stemMapping.get(word));
			}
			double oldValue = postVector.getValue(position);
			postVector.setValue(position, oldValue + 1);
		}
		postVector.setLabels(post.getLabels());
		return postVector;
	}

	private boolean isNotRubbish(String w) {
		Pattern p = Pattern.compile("@?\\p{L}+(\\d+)?|\\d+");
		Matcher m = p.matcher(w);
		return m.matches();
	}

}
