package hr.fer.zemris.otd.vectors;

import hr.fer.zemris.otd.dataPreprocessing.Post;
import hr.fer.zemris.otd.dataPreprocessing.PredataCreator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VectorCreator {

	private PredataCreator creator;

	public VectorCreator(PredataCreator creator) {
		this.creator = creator;
	}

	public List<PostVector> createOccurrenceVectors() throws IOException {
		List<PostVector> occurrenceVectors = new ArrayList<>();
		int cnt = 0;
		for (Post p : creator.getPosts()) {
			System.out.println("SVM " + (++cnt));
			occurrenceVectors.add(createOccurrenceVector(p));
		}
		return occurrenceVectors;
	}

	public List<PostVector> createTfIdfVectors() throws IOException {
		List<PostVector> tfIdfVectors = new ArrayList<>();
		int cnt = 0;
		for (Post p : creator.getPosts()) {
			System.out.println("TFIDF " + (++cnt));
			tfIdfVectors.add(createTfIdfVector(p));
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
			PostVector newVector = new PostVector(v.getLabels().length, creator
					.getWordMap().size());
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
			PostVector v = new PostVector(labels.length, creator.getWordMap()
					.size());
			setLabels(v, labels);
			setValues(v, values);
			vectors.add(v);
		}
		return vectors;
	}

	private void setValues(PostVector v, String[] values) {
		int size = values.length;
		for (int i = 0; i < size; i++) {
			double value = Double.parseDouble(values[i]);
			v.setValue(i, value);
		}
	}

	private void setLabels(PostVector v, String[] labels) {
		int size = labels.length;
		for (int i = 0; i < size; i++) {
			char l = labels[i].charAt(0);
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
		fw.write("\n");
	}

	private void writeLabels(FileWriter fw, char[] labels) throws IOException {
		for (Character c : labels) {
			fw.write(c + ",");
		}
		fw.write("\n");
	}

	private PostVector createTfIdfVector(Post post) {
		Map<String, Integer> wordMap = creator.getWordMap();
		PostVector postVector = new PostVector(post.getLabels().length,
				wordMap.size());
		int numberOfPost = creator.getPosts().size();
		for (String word : wordMap.keySet()) {
			double tfidf = 0.0;
			int wordPostOccurrences = getWordPostOccurrences(word, post);
			if (wordPostOccurrences > 0) {
				int postWordOccurrences = getPostWordOccurrences(word);
				double log = Math.log(1.0 * numberOfPost / postWordOccurrences);
				tfidf = 1.0 * wordPostOccurrences * log;
			}
			postVector.setValue(wordMap.get(word), tfidf);
		}
		postVector.setLabels(post.getLabels());
		return postVector;
	}

	private int getWordPostOccurrences(String word, Post post) {
		int counter = 0;
		for (String postWord : post.getPostText().split("\\p{Z}")) {
			if (word.equals(postWord)) {
				counter++;
			}
		}
		return counter;
	}

	private int getPostWordOccurrences(String word) {
		int counter = 0;
		for (Post post : creator.getPosts()) {
			for (String postWord : post.getPostText().split("\\p{Z}")) {
				if (word.equals(postWord)) {
					counter++;
					break;
				}
			}
		}
		return counter;
	}

	private PostVector createOccurrenceVector(Post post) {
		Map<String, Integer> wordMap = creator.getWordMap();
		PostVector postVector = new PostVector(post.getLabels().length,
				wordMap.size());
		for (String w : post.getPostText().split("\\p{Z}")) {
			String word = w.trim().toLowerCase();
			// double negation ftw...not (not rubbish) == rubbish
			if (!isNotRubbish(word)) {
				continue; // we are not interested in parts that are not real
							// words
			}
			if (!wordMap.containsKey(word)) {
				System.err
						.println("So, word you find in post you used to create wordMap is not in that map... You must be kiddin' me!");
				System.err
						.println("As you probably expect, this program will now crash!");
				System.exit(-1);
			}
			int position = wordMap.get(word);
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
