package hr.fer.zemris.otd.vectors;

import hr.fer.zemris.otd.dataPreprocessing.Post;
import hr.fer.zemris.otd.utils.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SimpleDatasetSplitter implements IDatasetSplitter {

	@Override
	public Pair<List<Post>, List<Post>> createDatasets(List<Post> vectors,
													   double percentage) {
		Collections.shuffle(vectors);
		int trainSetSize = (int) (vectors.size() * percentage);
		List<Post> trainSet = new ArrayList<>();
		List<Post> testSet = new ArrayList<>();
		createSets(trainSet, testSet, vectors, trainSetSize);
		return new Pair<List<Post>, List<Post>>(trainSet, testSet);
	}

	private void createSets(List<Post> trainSet, List<Post> testSet,
							List<Post> vectors, int trainSetSize) {
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
