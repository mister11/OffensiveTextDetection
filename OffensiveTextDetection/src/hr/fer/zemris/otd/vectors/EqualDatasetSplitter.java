package hr.fer.zemris.otd.vectors;

import hr.fer.zemris.otd.dataPreprocessing.Post;
import hr.fer.zemris.otd.utils.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EqualDatasetSplitter implements IDatasetSplitter {

	@Override
	public Pair<List<Post>, List<Post>> createDatasets(List<Post> vectors,
													   double percentage) {
		List<Post> positive = new ArrayList<>();
		List<Post> negative = new ArrayList<>();
		getPosAndNeg(vectors, positive, negative);
		List<Post> trainSet = new ArrayList<>();
		List<Post> testSet = new ArrayList<>();
		getSets(positive, negative, trainSet, testSet, percentage);
		Collections.shuffle(trainSet);
		Collections.shuffle(testSet);
		return new Pair<List<Post>, List<Post>>(trainSet, testSet);
	}

	private void getSets(List<Post> positive, List<Post> negative,
						 List<Post> trainSet, List<Post> testSet, double percentage) {
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

	private void getPosAndNeg(List<Post> vectors, List<Post> positive,
							  List<Post> negative) {
		for (Post v : vectors) {
			if (v.getLabel(0) == '1') {
				positive.add(v);
			} else if (v.getLabel(0) == '0') {
				negative.add(v);
			}
		}
	}

}
