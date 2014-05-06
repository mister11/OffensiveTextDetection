package hr.fer.zemris.otd.logreg;

import hr.fer.zemris.otd.crossValidation.CrossValidation;
import hr.fer.zemris.otd.dataPreprocessing.Post;
import hr.fer.zemris.otd.dataPreprocessing.PredataCreator;
import hr.fer.zemris.otd.logreg.utils.LogRegUtils;
import hr.fer.zemris.otd.utils.Pair;
import hr.fer.zemris.otd.utils.Serialize;
import hr.fer.zemris.otd.vectors.EqualDatasetSplitter;
import hr.fer.zemris.otd.vectors.IDatasetSplitter;
import hr.fer.zemris.otd.vectors.PostVector;
import hr.fer.zemris.otd.vectors.VectorCreator;

import java.io.IOException;
import java.util.List;

import org.apache.commons.math3.linear.RealMatrix;

public class Test {

	public static void main(String[] args) throws IOException,
			ClassNotFoundException {
		String saveOccurVector = "C:/Users/Big Sven/Desktop/experiment/lemma/occur.txt";
		String saveTfIdfVector = "C:/Users/Big Sven/Desktop/experiment/lemma/tfidf.txt";
		String occurTrainVectors = "C:/Users/Big Sven/Desktop/experiment/lemma/occurTrain.txt";
		String occurTestVectors = "C:/Users/Big Sven/Desktop/experiment/lemma/occurTest.txt";
		String outputPath = "C:/Users/Big Sven/Desktop/experiment/lemma/my_word_list.txt";

		PredataCreator creator = new PredataCreator();

		creator.createPostsList("C:/Users/Big Sven/Desktop/experiment/lemma/novo_razmaci_bezPraznihLinija.txt");
		List<Post> allPosts = creator.getPosts();

		IDatasetSplitter splitter = new EqualDatasetSplitter();
		Pair<List<Post>, List<Post>> dataSets = splitter.createDatasets(
				allPosts, 0.8);
		creator.createMap(dataSets.x); // creator.createMap(outputPath);

		VectorCreator numericTrainSet = new VectorCreator(creator, dataSets.x);
		VectorCreator numericTestSet = new VectorCreator(creator, dataSets.y);

		List<PostVector> trainSet = numericTrainSet.createOccurrenceVectors();
		List<PostVector> testSet = numericTestSet.createOccurrenceVectors();

		// normalization - not copied
		numericTrainSet.nNormalizeVectors(trainSet);
		numericTrainSet.nNormalizeVectors(testSet);

		Serialize.object(trainSet, "trainSet.ser");
		Serialize.object(testSet, "testSet.ser");

		double bestLambda = 0.0;
		double bestPrecision = 0.0;

		CrossValidation cv = new CrossValidation(5);
		List<List<PostVector>> dividedTrainSet = cv.divideSet(trainSet);
		int k = dividedTrainSet.size();
		for (double lambda = 0; lambda <= 2; lambda += 0.1) {
			double avgAccuracy = 0.0;
			for (int i = 0; i < k; i++) {
				List<PostVector> vectForCalc = cv.getOthers(dividedTrainSet, i);
				IFunction function = new SigmoidFunction();
				RealMatrix data = LogRegUtils.getData(vectForCalc);
				RealMatrix theta = LogRegUtils.getTheta(creator.getWordMap()
						.size());
				RealMatrix labels = LogRegUtils.getLabels(vectForCalc);
				LogisticRegression logReg = new LogisticRegression(function);
				RealMatrix finalTheta = null; // = deserTheta(num);

				if (finalTheta == null) {
					System.out.println("Running gradDesc " + (i + 1)
							+ "-th time for lambda = " + lambda);
					finalTheta = logReg.runGradientDescent(theta, data, labels,
							lambda, 600, true);
					// serTheta(finalTheta, num);
				}
				List<PostVector> cvTestSet = dividedTrainSet.get(i);
				data = LogRegUtils.getData(cvTestSet);
				labels = LogRegUtils.getLabels(cvTestSet);
				System.out.println("Prediction for lambda = " + lambda);
				avgAccuracy += logReg.predictAvg(finalTheta, data, labels);
			}

			if (avgAccuracy / k > bestPrecision) {
				bestPrecision = avgAccuracy / k;
				bestLambda = lambda;
			}
			System.out.println("Best precision so far (lambda = " + lambda
					+ ": " + bestPrecision);
			System.out.println("Best lambda so far: " + bestLambda);
		}
		System.out.println("\nEND");
		System.out.println("Best precision: " + bestPrecision);
		System.out.println("Best lambda: " + bestLambda);
	}

}