package hr.fer.zemris.otd.crawler.logreg;

import org.apache.commons.math3.linear.RealMatrix;

public interface IFunction {

	public RealMatrix calculateOnMatrix(RealMatrix matrix);

	public double calculateOnValue(double value);

}
