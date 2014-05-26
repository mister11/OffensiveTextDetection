package hr.fer.zemris.otd.vectors;

import hr.fer.zemris.otd.dataPreprocessing.Post;
import hr.fer.zemris.otd.utils.Pair;

import java.util.List;

public interface IDatasetSplitter {

	Pair<List<Post>, List<Post>> createDatasets(List<Post> vectors,
												double percentage, int label);

}
