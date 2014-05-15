package hr.fer.zemris.otd.svm;

import hr.fer.zemris.otd.dataPreprocessing.Post;
import hr.fer.zemris.otd.dataPreprocessing.PredataCreator;
import hr.fer.zemris.otd.stemming.DataManager;
import hr.fer.zemris.otd.utils.Pair;
import hr.fer.zemris.otd.vectors.EqualDatasetSplitter;
import hr.fer.zemris.otd.vectors.IDatasetSplitter;
import hr.fer.zemris.otd.vectors.PostVector;
import hr.fer.zemris.otd.vectors.VectorCreator;
import libsvm.svm;
import libsvm.svm_parameter;
import libsvm.svm_problem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Test {

	private static String directory = "D:/Documents/SVEN/FER/ZR/stemmer/";
	private static String postFile = "allPosts.txt";
	private static String stemmedPosts = "stemmedPosts.txt";

	public static void main(String[] args) throws IOException {
		// PreprocessData trainSet = new PreprocessData("trainSet.ser");
		// PreprocessData testSet = new PreprocessData("testSet.ser");
		// int trainSize = trainSet.numOfTrainData();
		// int testSize = testSet.numOfTrainData();
		//
		// if (trainSize == 0 || testSize == 0) {

		List<PostVector> trainVecs = new ArrayList<>();
		List<PostVector> testVecs = new ArrayList<>();
		PredataCreator creator = new PredataCreator();
		creator.createPostsList("C:/Users/Big Sven/Desktop/experiment/lemma/novo_razmaci_bezPraznihLinija.txt");
		List<Post> allPosts = creator.getPosts();

		IDatasetSplitter splitter = new EqualDatasetSplitter();
		Pair<List<Post>, List<Post>> dataSets = splitter.createDatasets(
				allPosts, 0.8);
		DataManager stemmer = new DataManager();
		stemmer.writePlainPosts(dataSets.x, directory + postFile);
		stemmer.stemPosts(directory, "Croatian_stemmer.py", postFile,
				stemmedPosts);
		stemmer.createMap(directory + stemmedPosts);
		// creator.createMapWithMinCount(dataSets.x, 0); //
		// creator.createMap(outputPath);

		VectorCreator numericTrainSet = new VectorCreator(
				stemmer.getStemMapping(), stemmer.getRealMap(), dataSets.x);
		trainVecs = numericTrainSet.createOccurrenceVectors();
		numericTrainSet.nNormalizeVectors(trainVecs);
		PreprocessData trainSet;
		trainSet = new PreprocessData(trainVecs);

		VectorCreator numericTestSet = new VectorCreator(
				stemmer.getStemMapping(), stemmer.getRealMap(), dataSets.y);
		testVecs = numericTestSet.createOccurrenceVectors();
		numericTestSet.nNormalizeVectors(testVecs);
		PreprocessData testSet;
		testSet = new PreprocessData(testVecs);
		// }

		// svm_node[][] nodes = trainSet.getNodes();
		// double[] labels = trainSet.getLabels();
		// FileWriter fw = new FileWriter("trainSetCV.txt");
		//
		// int sizeX = nodes.length;
		// int sizeY = nodes[0].length;
		// for (int i = 0; i < sizeX; i++) {
		// fw.write(labels[i] + " ");
		// for (int j = 0; j < sizeY; j++) {
		// svm_node node = nodes[i][j];
		// fw.write(node.index + ":" + node.value);
		// if (j == sizeY - 1) {
		// fw.write(System.lineSeparator());
		// } else {
		// fw.write(" ");
		// }
		// }
		// }
		// fw.close();
		svm_problem problem = new svm_problem();
		problem.l = trainSet.numOfTrainData();
		problem.y = trainSet.getLabels();
		problem.x = trainSet.getNodes();
		svm_parameter params = Parameters.getInitParams();

		for (int x = -5; x <= 15; x++) {
			params.C = Math.pow(2, x);
			svm.svm_check_parameter(problem, params);
			System.out.println("Start alg");
			// svm_model model = svm.svm_train(problem, params);
			double[] target = new double[problem.y.length];

			svm.svm_cross_validation(problem, params, 5, target);

			int size = problem.y.length;
			int cnt = 0;
			for (int i = 0; i < size; i++) {
				int l = (int) target[i];
				int ml = (int) problem.y[i];
				if (l == ml) {
					cnt++;
				}
			}
			System.out.println("For C = " + params.C + " and eps: "
					+ params.eps + " accuracy is: " + (1.0 * cnt / size));
		}

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
