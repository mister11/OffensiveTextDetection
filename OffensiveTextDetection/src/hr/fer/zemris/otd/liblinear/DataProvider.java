package hr.fer.zemris.otd.liblinear;

import de.bwaldvogel.liblinear.FeatureNode;
import hr.fer.zemris.otd.utils.Deserialize;
import hr.fer.zemris.otd.vectors.PostVector;

import java.util.List;

/**
 * Created by Sven Vidak on 15.05.2014..
 */
public class DataProvider {

	private List<PostVector> vectors;

	public DataProvider(String fileToDeser) {
		this.vectors = Deserialize.listPostVectors(fileToDeser);
	}

	public DataProvider(List<PostVector> vectors) {
		this.vectors = vectors;
	}

	public FeatureNode[][] getNodes() {
		FeatureNode[][] nodes = new FeatureNode[this.vectors.size()][this.vectors
				.get(0).getValues().length];
		int row = 0;
		for (PostVector v : vectors) {
			int index = 1;
			int col = 0;
			for (Double d : v.getValues()) {
				FeatureNode node = new FeatureNode(index++, d);
				nodes[row][col++] = node;
			}
			row++;
		}
		return nodes;
	}

	public double[] getLabels(int label) {
		double[] labels = new double[this.vectors.size()];
		int i = 0;
		for (PostVector v : vectors) {
			char[] vLabels = v.getLabels();
			labels[i++] = Double.valueOf(String.valueOf(vLabels[label]));
		}
		return labels;
	}

	public List<PostVector> getVectors() {
		return vectors;
	}

	public int numOfTrainData() {
		if (vectors == null) {
			return 0;
		}
		return this.vectors.size();
	}
}
