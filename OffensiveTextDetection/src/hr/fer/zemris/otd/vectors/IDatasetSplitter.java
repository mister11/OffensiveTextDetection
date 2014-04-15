package hr.fer.zemris.otd.vectors;

import hr.fer.zemris.otd.utils.Pair;

import java.util.List;

public interface IDatasetSplitter {

	Pair<List<PostVector>, List<PostVector>> createDatasets(
			List<PostVector> vectors, double percentage);

}
