package hr.fer.zemris.otd.vectors;

import hr.fer.zemris.otd.utils.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SimpleDatasetSplitter implements IDatasetSplitter {

	@Override
	public Pair<List<PostVector>, List<PostVector>> createDatasets(
			List<PostVector> vectors, double percentage) {
		Collections.shuffle(vectors);
		int trainSetSize = (int) (vectors.size() * percentage);
		List<PostVector> trainSet = new ArrayList<>();
		List<PostVector> testSet = new ArrayList<>();
		createSets(trainSet, testSet, vectors, trainSetSize);
		return new Pair<List<PostVector>, List<PostVector>>(trainSet, testSet);
	}

	private void createSets(List<PostVector> trainSet,
			List<PostVector> testSet, List<PostVector> vectors, int trainSetSize) {
		int size = vectors.size();
		for (int i = 0; i < size; i++) {
			if (i >= trainSetSize) {
				testSet.add(vectors.get(i));
			} else {
				trainSet.add(vectors.get(i));
			}
		}
	}
}
