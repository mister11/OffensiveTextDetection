package hr.fer.zemris.otd.naiveBayes;

import hr.fer.zemris.otd.dataPreprocessing.Post;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataProcessor {

	private List<Post> vectors;
	private List<Post> positiveSet;
	private List<Post> negativeSet;

	private Set<String> words;

	public DataProcessor(List<Post> vectors) {
		this.vectors = vectors;
		this.positiveSet = new ArrayList<>();
		this.negativeSet = new ArrayList<>();
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
			for (String word : p.getPostText().split("\\p{Z}")) {
				String w = word.trim().toLowerCase();
				if (!isNotRubbish(w)) {
					continue;
				}
				cnt++;
			}
		}
		return cnt;
	}

	public int getNegativeWordsNum() {
		int cnt = 0;
		for (Post p : negativeSet) {
			for(String word : p.getPostText().split("\\p{Z}")) {
				String w = word.trim().toLowerCase();
				if(!isNotRubbish(w)) {
					continue;
				}
				cnt++;
			}
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
				if (!isNotRubbish(w)) {
					continue;
				}
				String ww = w.trim().toLowerCase();
				if (word.equals(ww)) {
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
				if(!isNotRubbish(w)) {
					continue;
				}
				String ww = w.trim().toLowerCase();
				if (word.equals(ww)) {
					cnt++;
				}
			}
		}
		return cnt;
	}

	private void addToWords(String postText) {
		for (String word : postText.split("\\p{Z}")) {
			String w = word.trim().toLowerCase();
			if (!words.contains(w) && isNotRubbish(w)) {
				words.add(w);
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

	public boolean isNotRubbish(String w) {
		//Pattern p = Pattern.compile("@?\\p{L}+(\\d+)?|\\d+");
		Pattern p = Pattern.compile("@?\\p{L}+.*");
		Matcher m = p.matcher(w);
		return m.matches();
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
