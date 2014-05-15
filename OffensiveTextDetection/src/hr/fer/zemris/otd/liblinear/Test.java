package hr.fer.zemris.otd.liblinear;


import de.bwaldvogel.liblinear.Linear;
import de.bwaldvogel.liblinear.Parameter;
import de.bwaldvogel.liblinear.Problem;
import hr.fer.zemris.otd.dataPreprocessing.Post;
import hr.fer.zemris.otd.dataPreprocessing.PredataCreator;
import hr.fer.zemris.otd.stemming.DataManager;
import hr.fer.zemris.otd.utils.Pair;
import hr.fer.zemris.otd.vectors.EqualDatasetSplitter;
import hr.fer.zemris.otd.vectors.IDatasetSplitter;
import hr.fer.zemris.otd.vectors.PostVector;
import hr.fer.zemris.otd.vectors.VectorCreator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sven Vidak on 15.05.2014..
 */

public class Test {

	private static String directory = "D:/Documents/SVEN/FER/ZR/stemmer/";
	private static String postFile = "allPosts.txt";
	private static String stemmedPosts = "stemmedPosts.txt";

	public static void main(String[] args) throws IOException {
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
		DataProvider trainSet = new DataProvider(trainVecs);

		VectorCreator numericTestSet = new VectorCreator(
				stemmer.getStemMapping(), stemmer.getRealMap(), dataSets.y);
		testVecs = numericTestSet.createOccurrenceVectors();
		numericTestSet.nNormalizeVectors(testVecs);
		DataProvider testSet = new DataProvider(testVecs);

		Problem problem = new Problem();
		problem.l = trainSet.numOfTrainData();
		problem.y = trainSet.getLabels();
		problem.x = trainSet.getNodes();
		problem.n = trainSet.getVectors().get(0).getValues().length;
		Parameter params = Parameters.getInitParams();

		for (int x = -15; x <= 15; x++) {
			params.setC(Math.pow(2, x));
			System.out.println("Start alg");
			// svm_model model = svm.svm_train(problem, params);
			double[] target = new double[problem.y.length];

			Linear.crossValidation(problem, params, 5, target);


			int size = problem.y.length;
			int cnt = 0;
			for (int i = 0; i < size; i++) {
				int l = (int) target[i];
				int ml = (int) problem.y[i];
				if (l == ml) {
					cnt++;
				}
			}
			System.out.println("For C = " + params.getC() + " and eps: "
					+ params.getEps() + " accuracy is: " + (1.0 * cnt / size));
		}

	}
}
