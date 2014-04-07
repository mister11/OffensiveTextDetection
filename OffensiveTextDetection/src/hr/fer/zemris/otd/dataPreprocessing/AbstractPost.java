package hr.fer.zemris.otd.dataPreprocessing;

/**
 * Kinda stupid class which only has a reference to posts labels. If there were
 * no this class, some other classes should have the same code for setting and
 * getting labels.
 * 
 * @author Sven Vidak
 *
 */

public abstract class AbstractPost {

	protected char[] labels;

	public AbstractPost(int numberOfLabels) {
		this.labels = new char[numberOfLabels];
	}

	public void setLabel(int position, char value) {
		this.labels[position] = value;
	}

	public void setLabels(char[] labels) {
		int size = labels.length;
		for (int i = 0; i < size; i++) {
			this.labels[i] = labels[i];
		}
	}

	public char getLabel(int position) {
		return labels[position];
	}

	public char[] getLabels() {
		return labels;
	}

}
