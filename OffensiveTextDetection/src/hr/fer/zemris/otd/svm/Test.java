package hr.fer.zemris.otd.svm;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;

public class Test {

	public static void main(String[] args) {
		PreprocessData data = new PreprocessData("trainSet.ser");
		svm_problem problem = new svm_problem();
		problem.l = data.numOfTrainData();
		problem.y = data.getLabels();
		problem.x = data.getNodes();
		svm_parameter params = Parameters.getInitParams();
		svm.svm_check_parameter(problem, params);
		System.out.println("Start alg");
		svm_model model = svm.svm_train(problem, params);

		data = new PreprocessData("testSet.ser");
		svm_node[][] testNodes = data.getNodes();
		double[] labels = data.getLabels();
		int i = 0;
		int cnt = 0;
		for (svm_node[] n : testNodes) {
			int l = (int) svm.svm_predict(model, n);
			int ml = (int) labels[i++];
			if (l == ml) {
				cnt++;
			}
		}
		System.out.println(1.0 * cnt / labels.length);
	}

}
