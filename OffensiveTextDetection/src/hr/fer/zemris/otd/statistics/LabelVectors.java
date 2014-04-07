package hr.fer.zemris.otd.statistics;

import java.util.ArrayList;
import java.util.List;

public class LabelVectors {

	List<String> offensive;
	List<String> rude;
	List<String> sarcasm;
	List<String> target;

	public LabelVectors() {
		this.offensive = new ArrayList<String>();
		this.rude = new ArrayList<String>();
		this.sarcasm = new ArrayList<String>();
		this.target = new ArrayList<String>();
	}

	public double percentageOfOffensive(String type) {
		int off = 0;
		int size = offensive.size();
		for (int i = 0; i < size; i++) {
			if (offensive.get(i).equals(type)) {
				off++;
			}
		}
		return (double) off / size;
	}

	public double percentageOfTarget(String type) {
		int off = 0;
		int size = target.size();
		for (int i = 0; i < size; i++) {
			if (target.get(i).equals(type)) {
				off++;
			}
		}
		return (double) off / size;
	}

	public double percentageOfRude(String type) {
		int off = 0;
		int size = rude.size();
		for (int i = 0; i < size; i++) {
			if (rude.get(i).equals(type)) {
				off++;
			}
		}
		return (double) off / size;
	}

	public double percentageOfSarcasm(String type) {
		int off = 0;
		int size = sarcasm.size();
		for (int i = 0; i < size; i++) {
			if (sarcasm.get(i).equals(type)) {
				off++;
			}
		}
		return (double) off / size;
	}
}
