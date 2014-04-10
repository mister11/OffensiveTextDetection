package hr.fer.zemris.otd.vectors;

import hr.fer.zemris.otd.dataPreprocessing.AbstractPost;

import java.io.Serializable;

public class PostVector extends AbstractPost implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7203963904941484876L;
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
