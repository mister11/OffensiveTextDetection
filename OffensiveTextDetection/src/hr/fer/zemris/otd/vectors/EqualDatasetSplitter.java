package hr.fer.zemris.otd.vectors;

import hr.fer.zemris.otd.utils.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EqualDatasetSplitter implements IDatasetSplitter {

	@Override
	public Pair<List<PostVector>, List<PostVector>> createDatasets(
			List<PostVector> vectors, double percentage) {
		List<PostVector> positive = new ArrayList<>();
		List<PostVector> negative = new ArrayList<>();
		getPosAndNeg(vectors, positive, negative);
		List<PostVector> trainSet = new ArrayList<>();
		List<PostVector> testSet = new ArrayList<>();
		getSets(positive, negative, trainSet, testSet, percentage);
		Collections.shuffle(trainSet);
		Collections.shuffle(testSet);
		return new Pair<List<PostVector>, List<PostVector>>(trainSet, testSet);
	}

	private void getSets(List<PostVector> positive, List<PostVector> negative,
			List<PostVector> trainSet, List<PostVector> testSet,
			double percentage) {
		Collections.shuffle(positive);
		Collections.shuffle(negative);
		int realPosSize = positive.size();
		int realNegSize = negative.size();
		int positiveSize = (int) (realPosSize * percentage + 0.5);
		int negativeSize = (int) (realNegSize * percentage + 0.5);
		int i, j;
		for (i = 0; i < positiveSize; i++) {
			trainSet.add(positive.get(i));
		}
		for (j = 0; j < negativeSize; j++) {
			trainSet.add(negative.get(j));
		}
		for (; i < realPosSize; i++) {
			testSet.add(positive.get(i));
		}
		for (; j < realNegSize; j++) {
			testSet.add(negative.get(j));
		}
	}

	private void getPosAndNeg(List<PostVector> vectors,
			List<PostVector> positive, List<PostVector> negative) {
		for (PostVector v : vectors) {
			if (v.getLabel(0) == '1') {
				positive.add(v);
			} else if (v.getLabel(0) == '0') {
				negative.add(v);
			}
		}
	}

}
