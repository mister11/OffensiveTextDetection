package hr.fer.zemris.otd.svm;

import hr.fer.zemris.otd.utils.Deserialize;
import hr.fer.zemris.otd.vectors.PostVector;

import java.util.List;

import libsvm.svm_node;

public class PreprocessData {

	private List<PostVector> vectors;

	public PreprocessData(String fileForDeser) {
		vectors = Deserialize.listPostVectors(fileForDeser);
	}

	public svm_node[][] getNodes() {
		svm_node[][] nodes = new svm_node[this.vectors.size()][this.vectors
				.get(0).getValues().length];
		int row = 0;
		for (PostVector v : vectors) {
			int index = 1;
			int col = 0;
			for (Double d : v.getValues()) {
				svm_node node = new svm_node();
				node.index = index++;
				node.value = d;
				nodes[row][col++] = node;
			}
			row++;
		}
		return nodes;
	}

	public double[] getLabels() {
		double[] labels = new double[this.vectors.size()];
		int i = 0;
		for (PostVector v : vectors) {
			char[] vLabels = v.getLabels();
			labels[i++] = Double.valueOf(String.valueOf(vLabels[0]));
		}
		return labels;
	}

	public int numOfTrainData() {
		return this.vectors.size();
	}

}
