package hr.fer.zemris.otd.crossValidation;

import hr.fer.zemris.otd.vectors.PostVector;

import java.util.ArrayList;
import java.util.List;

public class CrossValidation {

	private int folds;

	public CrossValidation(int folds) {
		this.folds = folds;
	}

	public List<List<PostVector>> divideSet(List<PostVector> trainSet) {
		List<List<PostVector>> dividedSet = new ArrayList<>();
		int size = trainSet.size();
		for (int i = 1; i <= folds; i++) {
			int start = (int) (1.0 * (i - 1) * size / folds + 0.5);
			int end = (int) (1.0 * i * size / folds + 0.5);
			List<PostVector> vector = new ArrayList<>();
			for (int j = start; j < end; j++) {
				vector.add(trainSet.get(j));
			}
			dividedSet.add(vector);
		}
		return dividedSet;
	}

	public List<PostVector> getOthers(List<List<PostVector>> dividedTrainSet,
									  int index) {
		if (index < 0 || index > folds) {
			System.err
					.println("Index of a cross validation set is bigger than number of folds");
			System.exit(-1);
		}
		List<PostVector> vectors = new ArrayList<>();
		int size = dividedTrainSet.size();
		for (int i = 0; i < size; i++) {
			if (i != index) {
				vectors.addAll(dividedTrainSet.get(i));
			}
		}
		return vectors;
	}
}
