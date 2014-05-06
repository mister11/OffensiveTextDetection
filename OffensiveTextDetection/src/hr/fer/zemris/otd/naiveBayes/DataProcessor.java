package hr.fer.zemris.otd.naiveBayes;

import hr.fer.zemris.otd.dataPreprocessing.Post;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DataProcessor {

	private List<Post> vectors;
	private List<Post> positiveSet;
	private List<Post> negativeSet;

	private Set<String> words;

	public DataProcessor(List<Post> vectors) {
		this.vectors = vectors;
		this.words = new HashSet<>();
		setPosAndNeg();
	}

	public int getVocabularySize() {
		if (words.isEmpty()) {
			for (Post p : vectors) {
				addToWords(p.getPostText());
			}
		}
		return words.size();
	}

	public int getPositiveWordsNum() {
		int cnt = 0;
		for (Post p : positiveSet) {
			cnt += p.getPostText().split("\\p{Z}").length;
		}
		return cnt;
	}

	public int getNegativeWordsNum() {
		int cnt = 0;
		for (Post p : negativeSet) {
			cnt += p.getPostText().split("\\p{Z}").length;
		}
		return cnt;
	}

	// returns value for both, positive and negative class
	public int[] getWordClassOccur(String word) {
		int[] counts = new int[2];
		counts[0] = positiveClass(word);
		counts[1] = negativeClass(word);
		return counts;
	}

	private int negativeClass(String word) {
		int cnt = 0;
		for (Post p : negativeSet) {
			for (String w : p.getPostText().split("\\p{Z}")) {
				if (word.equals(w)) {
					cnt++;
				}
			}
		}
		return cnt;
	}

	private int positiveClass(String word) {
		int cnt = 0;
		for (Post p : positiveSet) {
			for (String w : p.getPostText().split("\\p{Z}")) {
				if (word.equals(w)) {
					cnt++;
				}
			}
		}
		return cnt;
	}

	private void addToWords(String postText) {
		for (String word : postText.split("\\p{Z}")) {
			if (!words.contains(word)) {
				words.add(word);
			}
		}
	}

	private void setPosAndNeg() {
		for (Post p : vectors) {
			char[] labels = p.getLabels();
			if (labels[0] == '0') {
				negativeSet.add(p);
			} else if (labels[0] == '1') {
				positiveSet.add(p);
			}
		}
	}

	public int totalSize() {
		return vectors.size();
	}

	public int positiveSize() {
		return positiveSet.size();
	}

	public int negativeSize() {
		return negativeSet.size();
	}

}
