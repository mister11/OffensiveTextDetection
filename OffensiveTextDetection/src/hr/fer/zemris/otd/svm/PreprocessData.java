package hr.fer.zemris.otd.svm;

import hr.fer.zemris.otd.utils.Deserialize;
import hr.fer.zemris.otd.vectors.PostVector;
import libsvm.svm_node;

import java.util.ArrayList;
import java.util.List;

public class PreprocessData {

	private List<PostVector> vectors;

	public PreprocessData(String fileForDeser) {
		vectors = Deserialize.listPostVectors(fileForDeser);
	}

	public PreprocessData(List<PostVector> vectors) {
		this.vectors = new ArrayList<>(vectors);
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
			labels[i++] = Double.valueOf(String.valueOf(vLabels[1]));
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
