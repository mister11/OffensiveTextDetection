package hr.fer.zemris.otd.logreg;

import hr.fer.zemris.otd.crossValidation.CrossValidation;
import hr.fer.zemris.otd.dataPreprocessing.Post;
import hr.fer.zemris.otd.dataPreprocessing.PredataCreator;
import hr.fer.zemris.otd.logreg.utils.LogRegUtils;
import hr.fer.zemris.otd.stemming.DataManager;
import hr.fer.zemris.otd.utils.Pair;
import hr.fer.zemris.otd.vectors.EqualDatasetSplitter;
import hr.fer.zemris.otd.vectors.IDatasetSplitter;
import hr.fer.zemris.otd.vectors.PostVector;
import hr.fer.zemris.otd.vectors.VectorCreator;
import org.apache.commons.math3.linear.RealMatrix;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Test {

	private static String directory = "D:/Documents/SVEN/FER/ZR/stemmer/";
	private static String postFile = "allPosts.txt";
	private static String stemmedPosts = "stemmedPosts.txt";

	public static void main(String[] args) throws IOException,
			ClassNotFoundException {
		String saveOccurVector = "C:/Users/Big Sven/Desktop/experiment/lemma/occur.txt";
		String saveTfIdfVector = "C:/Users/Big Sven/Desktop/experiment/lemma/tfidf.txt";
		String occurTrainVectors = "C:/Users/Big Sven/Desktop/experiment/lemma/occurTrain.txt";
		String occurTestVectors = "C:/Users/Big Sven/Desktop/experiment/lemma/occurTest.txt";
		String outputPath = "C:/Users/Big Sven/Desktop/experiment/lemma/my_word_list.txt";

		int mapSize = 0;

		List<PostVector> trainSet = new ArrayList<>();
		List<PostVector> testSet = new ArrayList<>();

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
		// creator.createMapWithMinCount(dataSets.x, 1); //
		// creator.createMap(outputPath);
		mapSize = stemmer.getRealMap().size();

		VectorCreator numericTrainSet = new VectorCreator(
				stemmer.getStemMapping(), stemmer.getRealMap(), dataSets.x);
		trainSet = numericTrainSet.createOccurrenceVectors();
		numericTrainSet.nNormalizeVectors(trainSet);

		VectorCreator numericTestSet = new VectorCreator(
				stemmer.getStemMapping(), stemmer.getRealMap(), dataSets.y);
		testSet = numericTestSet.createOccurrenceVectors();
		numericTestSet.nNormalizeVectors(testSet);

		if (mapSize == 0) {
			mapSize = trainSet.get(0).getValues().length;
		}
		double bestLambda = 0.0;
		double bestPrecision = 0.0;

		CrossValidation cv = new CrossValidation(5);
		List<List<PostVector>> dividedTrainSet = cv.divideSet(trainSet);
		int k = dividedTrainSet.size();
		for (double lambda = -5; lambda <= 15; lambda++) {
			double avgAccuracy = 0.0;
			for (int i = 0; i < k; i++) {
				List<PostVector> vectForCalc = cv.getOthers(dividedTrainSet, i);
				IFunction function = new SigmoidFunction();
				RealMatrix data = LogRegUtils.getData(vectForCalc);
				RealMatrix theta = LogRegUtils.getTheta(mapSize);
				RealMatrix labels = LogRegUtils.getLabels(vectForCalc);
				LogisticRegression logReg = new LogisticRegression(function);
				RealMatrix finalTheta = null; // = deserTheta(num);

				if (finalTheta == null) {
					System.out.println("Running gradDesc " + (i + 1)
							+ "-th time for lambda = " + Math.pow(2, lambda));
					finalTheta = logReg.runGradientDescent(theta, data, labels,
							Math.pow(2, lambda), 1000, true);
					// serTheta(finalTheta, num);
				}
				List<PostVector> cvTestSet = dividedTrainSet.get(i);
				data = LogRegUtils.getData(cvTestSet);
				labels = LogRegUtils.getLabels(cvTestSet);
				System.out.println("Prediction for lambda = "
						+ Math.pow(2, lambda));
				avgAccuracy += logReg.predictAvg(finalTheta, data, labels);
			}

			if (avgAccuracy / k > bestPrecision) {
				bestPrecision = avgAccuracy / k;
				bestLambda = Math.pow(2, lambda);
			}
			System.out.println("Current accuracy: " + (avgAccuracy / k));
			System.out.println("Best accuracy so far (lambda = "
					+ Math.pow(2, lambda) + ": " + bestPrecision);
			System.out.println("Best lambda so far: " + bestLambda);
		}
		System.out.println("\nEND");
		System.out.println("Best precision: " + bestPrecision);
		System.out.println("Best lambda: " + bestLambda);
	}

}