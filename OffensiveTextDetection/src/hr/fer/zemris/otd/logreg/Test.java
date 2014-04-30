package hr.fer.zemris.otd.logreg;

import hr.fer.zemris.otd.dataPreprocessing.PredataCreator;
import hr.fer.zemris.otd.logreg.utils.LogRegUtils;
import hr.fer.zemris.otd.utils.Deserialize;
import hr.fer.zemris.otd.utils.Pair;
import hr.fer.zemris.otd.utils.Serialize;
import hr.fer.zemris.otd.vectors.EqualDatasetSplitter;
import hr.fer.zemris.otd.vectors.IDatasetSplitter;
import hr.fer.zemris.otd.vectors.PostVector;
import hr.fer.zemris.otd.vectors.VectorCreator;

import java.io.IOException;
import java.util.ArrayList;
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
		IDatasetSplitter splitter = new EqualDatasetSplitter();

		creator.createMap(outputPath);
		creator.createPostsList("C:/Users/Big Sven/Desktop/experiment/lemma/novo_razmaci_bezPraznihLinija.txt");

		VectorCreator vc = new VectorCreator(creator);

		Pair<List<PostVector>, List<PostVector>> sets = null;

		System.out.println("deser datasets");
		List<PostVector> trainSet = Deserialize.listPostVectors("trainSet.ser");
		List<PostVector> testSet = Deserialize.listPostVectors("testSet.ser");

		if (trainSet == null || testSet == null) {
			System.out.println("No ser datasets");
			List<PostVector> vectors = Deserialize
					.listPostVectors("serOccurVec.ser");
			if (vectors == null) {
				vectors = vc.getVectorsFromFile(saveOccurVector);
				Serialize.object(vectors, "serOccurVec.ser");
				;
			}
			System.out.println("Creating datasets");
			sets = splitter.createDatasets(vectors, 0.8);
			trainSet = sets.x;
			testSet = sets.y;
			System.out.println("Ser datasets");
			Serialize.object(trainSet, "trainSet.ser");
			Serialize.object(testSet, "testSet.ser");
		}

		double bestLambda = 0.0;
		double bestPrecision = 0.0;

		// cross validation
		List<List<PostVector>> dividedTrainSet = divideSet(trainSet, 5);
		int k = dividedTrainSet.size();
		for (double lambda = 0; lambda <= 2; lambda += 0.1) {
			double avgAccuracy = 0.0;
			for (int i = 0; i < k; i++) {
				List<PostVector> vectForCalc = getOthers(dividedTrainSet, i);
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

	private static List<PostVector> getOthers(
			List<List<PostVector>> dividedTrainSet, int index) {
		List<PostVector> vectors = new ArrayList<>();
		int size = dividedTrainSet.size();
		for (int i = 0; i < size; i++) {
			if (i != index) {
				vectors.addAll(dividedTrainSet.get(i));
			}
		}
		return vectors;
	}

	private static List<List<PostVector>> divideSet(List<PostVector> trainSet,
			int k) {
		List<List<PostVector>> dividedSet = new ArrayList<>();
		int size = trainSet.size();
		for (int i = 1; i <= k; i++) {
			int start = (int) (1.0 * (i - 1) * size / k + 0.5);
			int end = (int) (1.0 * i * size / k + 0.5);
			List<PostVector> vector = new ArrayList<>();
			for (int j = start; j < end; j++) {
				vector.add(trainSet.get(j));
			}
			dividedSet.add(vector);
		}
		return dividedSet;
	}

}