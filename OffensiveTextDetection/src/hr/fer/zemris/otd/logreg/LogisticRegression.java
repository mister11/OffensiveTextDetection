package hr.fer.zemris.otd.logreg;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

public class LogisticRegression {

	private IFunction function;

	public LogisticRegression(IFunction function) {
		this.function = function;
	}

	public RealMatrix runGradientDescent(RealMatrix theta, RealMatrix data,
			RealMatrix labels, double lambda, int iters, boolean cost) {
		// theta = (n+1) x 1
		// data = m x (n + 1)
		// labels = m x 1
		for (int x = 0; x < iters; x++) {
			RealMatrix H = data.multiply(theta); // m x 1
			if (cost && (x % 50 == 0)) {
				System.out.println("Iter:" + (x + 1) + "\t cost: "
						+ getCostFunction(labels, H, lambda, theta));
			}
			RealMatrix diffUnderSum = function.calculateOnMatrix(H).subtract(
					labels); // m
			// x
			// 1
			RealMatrix underSum = diffUnderSum.transpose().multiply(data)
					.transpose(); // (n+1) x 1
			RealMatrix grad = underSum.scalarMultiply(1.0 / data
					.getRowDimension()); // (n + 1) x 1
			int rowSize = grad.getRowDimension();
			int colSize = grad.getColumnDimension();
			for (int i = 1; i < rowSize; i++) {
				for (int j = 0; j < colSize; j++) {
					double val = grad.getEntry(i, j)
							+ (lambda / data.getRowDimension())
							* theta.getEntry(i, j);
					grad.setEntry(i, j, val);
				}
			}
			theta = theta.subtract(grad);
		}
		return theta;

	}

	public double getCostFunction(RealMatrix labels, RealMatrix h,
			double lambda, RealMatrix theta) {
		double sumThetaSquared = recalculateTheta(theta);
		RealMatrix negatedLabels = new Array2DRowRealMatrix(
				reverseLabels(labels));
		RealMatrix logSigmoidH = getLogOfMatrix(function.calculateOnMatrix(h));
		RealMatrix negatedLogSigmoidH = getLogOfMatrix(recalculateH(function
				.calculateOnMatrix(h)));
		double multOne = labels.transpose().multiply(logSigmoidH).getData()[0][0];
		double multTwo = negatedLabels.transpose().multiply(negatedLogSigmoidH)
				.getData()[0][0];
		return (-1.0 / h.getRowDimension()) * (multOne + multTwo)
				+ (lambda / (2 * h.getRowDimension())) * sumThetaSquared;
	}

	public double predictAvg(RealMatrix theta, RealMatrix data,
			RealMatrix labels) {
		int size = data.getRowDimension();
		int cnt = 0;
		for (int i = 0; i < size; i++) {
			double value = function.calculateOnMatrix(
					data.getRowMatrix(i).multiply(theta)).getData()[0][0];
			if (value >= 0.5) {
				int label = (int) labels.getEntry(i, 0);
				if (label == 1) {
					cnt++;
				}
				System.out.println("Prediction: " + 1 + "\tReal label: "
						+ label);
			} else {
				int label = (int) labels.getEntry(i, 0);
				if (label == 0) {
					cnt++;
				}
				System.out.println("Prediction: " + 0 + "\tReal label: "
						+ label);
			}
		}
		return 1.0 * cnt / size;
	}

	private RealMatrix recalculateH(RealMatrix matrix) {
		int rowSize = matrix.getRowDimension();
		int colSize = matrix.getColumnDimension();
		for (int i = 1; i < rowSize; i++) {
			for (int j = 0; j < colSize; j++) {
				double val = 1 - matrix.getEntry(i, j);
				matrix.setEntry(i, j, val);
			}
		}
		return matrix;
	}

	private RealMatrix getLogOfMatrix(RealMatrix h) {
		int rowSize = h.getRowDimension();
		int colSize = h.getColumnDimension();
		for (int i = 1; i < rowSize; i++) {
			for (int j = 0; j < colSize; j++) {
				double val = Math.log(h.getEntry(i, j));
				h.setEntry(i, j, val);
			}
		}
		return h;
	}

	private double recalculateTheta(RealMatrix theta) {
		int rowSize = theta.getRowDimension();
		int colSize = theta.getColumnDimension();
		double sum = 0.0;
		for (int i = 1; i < rowSize; i++) {
			for (int j = 0; j < colSize; j++) {
				sum += theta.getEntry(i, j) * theta.getEntry(i, j);

			}
		}
		return sum;
	}

	private double[][] reverseLabels(RealMatrix labels) {
		int rowSize;
		int colSize;
		rowSize = labels.getRowDimension();
		colSize = labels.getColumnDimension();
		double[][] labelComplements = new double[rowSize][colSize];
		for (int i = 1; i < rowSize; i++) {
			for (int j = 0; j < colSize; j++) {
				labelComplements[i][j] = 1 - labels.getEntry(i, j);
			}
		}
		return labelComplements;
	}
}
