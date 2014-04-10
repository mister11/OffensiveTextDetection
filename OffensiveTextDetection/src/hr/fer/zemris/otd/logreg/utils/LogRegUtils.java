package hr.fer.zemris.otd.logreg.utils;

import hr.fer.zemris.otd.vectors.PostVector;

import java.util.List;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

public class LogRegUtils {

	public static RealMatrix getData(List<PostVector> vectors) {
		int cols = vectors.get(0).getValues().length;
		int rows = vectors.size();
		double[][] data = new double[rows][cols + 1];
		for (int i = 0; i < rows; i++) {
			data[i][0] = 1.0;
		}
		for (int i = 0; i < rows; i++) {
			PostVector v = vectors.get(i);
			for (int j = 1; j < cols + 1; j++) {
				data[i][j] = v.getValue(j - 1);
			}
		}
		return new Array2DRowRealMatrix(data);
	}

	public static RealMatrix getLabels(List<PostVector> vectors) {
		double[][] labels = new double[vectors.size()][1];
		int i = 0;
		for (PostVector v : vectors) {
			labels[i++][0] = Double.valueOf(String.valueOf(v.getLabel(0)));
		}
		return new Array2DRowRealMatrix(labels);
	}

	public static RealMatrix getTheta(int size) {
		double[][] theta = new double[size + 1][1];
		return new Array2DRowRealMatrix(theta);
	}

}
