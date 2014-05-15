package hr.fer.zemris.otd.naiveBayes;

import java.util.Map;

public class ProbabilityCalculator {

	private DataProcessor data;

	private Map<String, Double> positiveProb;
	private Map<String, Double> negativeProb;

	public ProbabilityCalculator(Map<String, Integer> wordMap,
								 DataProcessor data) {
		this.data = data;
		calculateProbabilities(wordMap);
	}

	public double getPositiveProb(String word) {
		return this.positiveProb.get(word);
	}

	public double getNegativeProb(String word) {
		return this.negativeProb.get(word);
	}

	public double getPosClassProbab() {
		return 1.0 * data.positiveSize() / data.totalSize();
	}

	public double getNegClassProbab() {
		return 1.0 * data.negativeSize() / data.totalSize();
	}

	private void calculateProbabilities(Map<String, Integer> wordMap) {
		int vocabSize = data.getVocabularySize();
		int posCount = data.getPositiveWordsNum();
		int negCount = data.getNegativeWordsNum();
		for (String word : wordMap.keySet()) {
			int[] counts = data.getWordClassOccur(word);
			double pos = 1.0 * (counts[0] + 1) / (posCount + vocabSize);
			double neg = 1.0 * (counts[1] + 1) / (negCount + vocabSize);
			positiveProb.put(word, pos);
			negativeProb.put(word, neg);
		}
	}

}
