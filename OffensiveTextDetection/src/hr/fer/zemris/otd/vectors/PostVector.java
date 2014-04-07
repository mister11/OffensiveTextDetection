package hr.fer.zemris.otd.vectors;

import hr.fer.zemris.otd.dataPreprocessing.AbstractPost;

public class PostVector extends AbstractPost {

	private double[] values;

	public PostVector(int numberOfLabels, int numberOfWords) {
		super(numberOfLabels);
		this.values = new double[numberOfWords];
	}

	public double[] getValues() {
		return values;
	}

	public double getValue(int position) {
		return this.values[position];
	}

	public void setValues(double[] values) {
		int size = values.length;
		for (int i = 0; i < size; i++) {
			this.values[i] = values[i];
		}
	}

	public void setValue(int position, double value) {
		this.values[position] = value;
	}

}
