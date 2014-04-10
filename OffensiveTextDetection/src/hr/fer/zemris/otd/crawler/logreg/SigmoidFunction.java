package hr.fer.zemris.otd.crawler.logreg;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

public class SigmoidFunction implements IFunction {

	@Override
	public RealMatrix calculateOnMatrix(RealMatrix matrix) {
		return sigmoid(matrix.getData());
	}

	private RealMatrix sigmoid(double[][] array) {
		int rows = array.length;
		int cols = array[0].length;
		double[][] matrix = new double[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				matrix[i][j] = 1.0 / (1.0 + Math.exp((-1.0) * array[i][j]));
			}
		}
		return new Array2DRowRealMatrix(matrix);
	}

	@Override
	public double calculateOnValue(double value) {
		return 1.0 / (1.0 + Math.exp((-1.0) * value));
	}

}
