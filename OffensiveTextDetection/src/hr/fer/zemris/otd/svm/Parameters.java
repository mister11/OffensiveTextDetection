package hr.fer.zemris.otd.svm;

import libsvm.svm_parameter;

public class Parameters {

	public static svm_parameter getInitParams() {
		svm_parameter params = new svm_parameter();
		params.svm_type = svm_parameter.C_SVC;
		params.kernel_type = svm_parameter.LINEAR;
		params.eps = 1e-4;
		params.C = 0.015;
		// params.nr_weight = 0;
		return params;
	}

}
