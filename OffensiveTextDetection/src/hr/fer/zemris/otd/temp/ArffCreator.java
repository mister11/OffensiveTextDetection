package hr.fer.zemris.otd.temp;

import hr.fer.zemris.otd.dataPreprocessing.Post;
import hr.fer.zemris.otd.dataPreprocessing.PredataCreator;
import hr.fer.zemris.otd.utils.Deserialize;
import hr.fer.zemris.otd.vectors.PostVector;
import hr.fer.zemris.otd.vectors.VectorCreator;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Sven Vidak on 28.05.2014. 22:44.
 */
public class ArffCreator {

	public void createArff(Map<String, Integer> wordMap, List<PostVector> vectors, int label) throws IOException {
		Map<String, Integer> sortedMap = new TreeMap<>(new ValueComparator(wordMap));
		sortedMap.putAll(wordMap);
		FileWriter fw = new FileWriter("weka.arff");
		fw.write("@RELATION	posts\n");
		sortedMap.keySet().forEach(key -> {
			try {
				fw.write("@ATTRIBUTE " + key + " REAL\n");
			} catch (IOException e) {
				System.err.println("Writing error -> .arff creator");
			}
		});
		fw.write("@ATTRIBUTE class {0, 1}\n");
		fw.write("@DATA\n");

		int size = vectors.get(0).getValues().length;
		for (PostVector pv : vectors) {
			fw.write("{");
			StringBuilder sb = new StringBuilder();
			for(int i=0; i < size; i++) {
				double val = pv.getValue(i);
				if(Double.compare(val, 0.0) == 0) {
					continue;
				}
				sb.append(i + " " + val + ", ");
			}
			fw.append(sb.toString() + " " + size + " " + pv.getLabel(label) + "}\n");
		}
		fw.close();
	}

	public static void main(String[] args) throws IOException {
		List<Post> trainPosts = Deserialize.listPosts("trainSet.ser");
		List<Post> testPosts = Deserialize.listPosts("testSet.ser");
		PredataCreator creator = new PredataCreator();
		creator.createMapWithMinCount(trainPosts, 0);
		VectorCreator numericTrainSet = new VectorCreator(
				null, creator.getWordMap(), trainPosts);
		List<PostVector> trainVecs = numericTrainSet.createOccurrenceVectors();
		numericTrainSet.normalizeVectors(trainVecs);
		ArffCreator ac = new ArffCreator();
		ac.createArff(creator.getWordMap(), trainVecs, 1);
	}

}

class ValueComparator implements Comparator<String> {

	private Map<String, Integer> wordMap;

	public ValueComparator(Map<String, Integer> wordMap) {
		this.wordMap = wordMap;
	}

	@Override
	public int compare(String o1, String o2) {
		if(wordMap.get(o1) >= wordMap.get(o2)) {
			return 1;
		} else {
			return -1;
		}
	}
}
