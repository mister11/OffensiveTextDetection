package hr.fer.zemris.otd.svm;

import java.io.FileWriter;
import java.io.IOException;

import libsvm.svm_node;

public class Test {

	public static void main(String[] args) throws IOException {
		double[] c = { 0.01, 0.1, 1, 2, 3, 4, 5, 10, 20, 30, 50, 75, 100, 150,
				200, 300, 400, 500, 600, 750, 850, 1000 };
		PreprocessData data = new PreprocessData("testSet.ser");

		svm_node[][] nodes = data.getNodes();
		double[] labels = data.getLabels();
		FileWriter fw = new FileWriter("testSetCV.txt");

		int sizeX = nodes.length;
		int sizeY = nodes[0].length;
		for (int i = 0; i < sizeX; i++) {
			fw.write(labels[i] + " ");
			for (int j = 0; j < sizeY; j++) {
				svm_node node = nodes[i][j];
				fw.write(node.index + ":" + node.value);
				if (j == sizeY - 1) {
					fw.write("\n");
				} else {
					fw.write(" ");
				}
			}
		}
		fw.close();
		// svm_problem problem = new svm_problem();
		// problem.l = data.numOfTrainData();
		// problem.y = data.getLabels();
		// problem.x = data.getNodes();
		// svm_parameter params = Parameters.getInitParams();
		//
		// for (Double cVal : c) {
		// params.C = cVal;
		// svm.svm_check_parameter(problem, params);
		// System.out.println("Start alg");
		// // svm_model model = svm.svm_train(problem, params);
		// double[] target = new double[problem.y.length];
		//
		// svm.svm_cross_validation(problem, params, 5, target);
		//
		// int size = problem.y.length;
		// int cnt = 0;
		// for (int i = 0; i < size; i++) {
		// int l = (int) target[i];
		// int ml = (int) problem.y[i];
		// if (l == ml) {
		// cnt++;
		// }
		// }
		// System.out.println("For C = " + params.C + " and eps: "
		// + params.eps + " accuracy is: " + (1.0 * cnt / size));
		// }

		// data = new PreprocessData("testSet.ser");
		// svm_node[][] testNodes = data.getNodes();
		// double[] labels = data.getLabels();
		// int i = 0;
		// int cnt = 0;
		// for (svm_node[] n : testNodes) {
		// int l = (int) svm.svm_predict(model, n);
		// int ml = (int) labels[i++];
		// if (l == ml) {
		// cnt++;
		// }
		// }
		// System.out.println(1.0 * cnt / labels.length);
	}

}
