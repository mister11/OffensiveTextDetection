package hr.fer.zemris.otd.naiveBayes;

import java.util.HashMap;
import java.util.Map;

public class ProbabilityCalculator {

	private DataProcessor data;

	private int vocabSize;
	private int posCount;
	private int negCount;

	private Map<String, Double> positiveProb;
	private Map<String, Double> negativeProb;

	public ProbabilityCalculator(Map<String, Integer> wordMap,
								 DataProcessor data) {
		this.data = data;
		this.positiveProb = new HashMap<>();
		this.negativeProb = new HashMap<>();
		this.vocabSize = data.getVocabularySize();
		this.posCount = data.getPositiveWordsNum();
		this.negCount = data.getNegativeWordsNum();
		calculateProbabilities(wordMap);
	}

	public double getPositiveProb(String word) {
		String w = word.trim().toLowerCase();
		if(!data.isNotRubbish(w)) {
			return 1.0;
		}
		return this.positiveProb.getOrDefault(w, 1.0 / (posCount + vocabSize));
	}

	public double getNegativeProb(String word) {
		String w = word.trim().toLowerCase();
		if (!data.isNotRubbish(w)) {
			return 1.0;
		}
		return this.negativeProb.getOrDefault(w, 1.0 / (posCount + vocabSize));
	}

	public double getPosClassProbab() {
		return 1.0 * data.positiveSize() / data.totalSize();
	}

	public double getNegClassProbab() {
		return 1.0 * data.negativeSize() / data.totalSize();
	}

	private void calculateProbabilities(Map<String, Integer> wordMap) {
		int i = 1;
		for (String word : wordMap.keySet()) {
			System.out.println(i++);
			int[] counts = data.getWordClassOccur(word);
			double pos = 1.0 * (counts[0] + 1) / (posCount + vocabSize);
			double neg = 1.0 * (counts[1] + 1) / (negCount + vocabSize);
			positiveProb.put(word, pos);
			negativeProb.put(word, neg);
		}
	}

}
