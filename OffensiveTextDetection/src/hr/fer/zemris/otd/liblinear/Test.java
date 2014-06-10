package hr.fer.zemris.otd.liblinear;


import de.bwaldvogel.liblinear.*;
import hr.fer.zemris.otd.dataPreprocessing.Post;
import hr.fer.zemris.otd.dataPreprocessing.PredataCreator;
import hr.fer.zemris.otd.statistics.PostLength;
import hr.fer.zemris.otd.stemming.DataManager;
import hr.fer.zemris.otd.utils.Deserialize;
import hr.fer.zemris.otd.vectors.PostVector;
import hr.fer.zemris.otd.vectors.VectorCreator;

import java.io.IOException;
import java.util.List;

/**
 * Created by Sven Vidak on 15.05.2014..
 */

public class Test {

	private static String directory = "D:/Documents/SVEN/FER/ZR/stemmer/";
	private static String postFile = "allPosts.txt";
	private static String stemmedPosts = "stemmedPosts.txt";

	private static String postFile1 = "C:/Users/Big Sven/Desktop/experiment/lemma/novo_razmaci_bezPraznihLinija.txt";
	private static String postFile2 = "C:/Users/Big Sven/Desktop/experiment/lemma/real_deal.txt";
	private static String wordList1 = "C:/Users/Big Sven/Desktop/experiment/lemma/my_word_list.txt";
	private static String wordList2 = "C:/Users/Big Sven/Desktop/experiment/lemma/my_words.txt";

	private static int label = 1;

	public static void main(String[] args) throws IOException {

		PredataCreator creator = new PredataCreator();
//		creator.createPostsList(postFile2);
//		ArtPostsCreator artPostsCreator = new ArtPostsCreator();
//		List<Post> allPosts = artPostsCreator.getArtificalPosts("C:/Users/Big Sven/Desktop/experiment/lemma/art_wordMap.txt");
//
//
//		IDatasetSplitter splitter = new EqualDatasetSplitter();
//
//		Pair<List<Post>, List<Post>> dataSets = splitter.createDatasets(
//				allPosts, 0.8, label);
//
//		Serialize.object(dataSets.x, "trainSetArtAdv.ser");
//		Serialize.object(dataSets.y, "testSetArtAdv.ser");


		List<Post> trainPosts = Deserialize.listPosts("data/trainSet.ser");
		List<Post> testPosts = Deserialize.listPosts("data/testSet.ser");


		PostLength.calcAll(trainPosts, testPosts);


		DataManager stemmer = new DataManager();
//		stemmer.writePlainPosts(dataSets.x, directory + postFile);
//		stemmer.stemPosts(directory, "Croatian_stemmer.py", postFile,
//				stemmedPosts);
//		stemmer.createMap(directory + stemmedPosts);
		//creator.createImportantWords("wekaStatsUTF8.txt");
		creator.createMapWithMinCount(trainPosts, 0);

		//creator.createMap(wordList2);


		List<PostVector> trainVecs;
		List<PostVector> testVecs;

		VectorCreator numericTrainSet = new VectorCreator(
				null, creator.getWordMap(), trainPosts);
		trainVecs = numericTrainSet.createOccurrenceVectors();
		numericTrainSet.normalizeVectors(trainVecs);
		DataProvider trainSet = new DataProvider(trainVecs);

		VectorCreator numericTestSet = new VectorCreator(
				null, creator.getWordMap(), testPosts);
		testVecs = numericTestSet.createOccurrenceVectors();
		numericTestSet.normalizeVectors(testVecs);
		DataProvider testSet = new DataProvider(testVecs);


		System.out.println(trainVecs.stream().filter(vec -> vec.getLabel(label) == '0').count());
		System.out.println(testVecs.stream().filter(vec -> vec.getLabel(label) == '0').count());

//		FileWriter fw1 = new FileWriter("trainVecs.txt");
//		FileWriter fw2 = new FileWriter("testVecs.txt");
//		for (PostVector v : trainVecs) {
//			int index = 1;
//			char[] labels = v.getLabels();
//			fw1.append(labels[label] + " ");
//			for (Double val : v.getValues()) {
//				if (Double.compare(val, 0.0) == 0) {
//					continue;
//				}
//				fw1.append(index + ":" + val + " ");
//				index++;
//			}
//			fw1.append(index + ":-1\n");
//		}
//		fw1.close();
//
//		for (PostVector v : testVecs) {
//			int index = 1;
//			char[] labels = v.getLabels();
//			fw2.append(labels[label] + " ");
//			for (Double val : v.getValues()) {
//				if (Double.compare(val, 0.0) == 0) {
//					continue;
//				}
//				fw2.append(index + ":" + val + " ");
//				index++;
//			}
//			fw2.append(index + ":-1\n");
//		}
//		fw2.close();

		Problem problem = new Problem();
		problem.l = trainSet.numOfTrainData();
		problem.y = trainSet.getLabels(label);
		problem.x = trainSet.getNodes();
		problem.n = trainSet.getVectors().get(0).getValues().length;
		Parameter params = Parameters.getInitParams();


		/**
		 * Training...
		 */

		params.setC(Math.pow(2, -6));
		Model model = Linear.train(problem, params);
		int tp = 0;
		int fn = 0;
		int fp = 0;
		int tn = 0;
		int cnt = 0;
		for (PostVector v : testSet.getVectors()) {
			Feature[] vector = getFeature(v);
			int realLabel = Integer.valueOf(String.valueOf(v.getLabel(label)));
			int prediction = (int) Linear.predict(model, vector);
			if (realLabel == 1 && prediction == 1) {
				tp++;
			} else if (realLabel == 1 && prediction == 0) {
				fn++;
			} else if (realLabel == 0 && prediction == 1) {
				fp++;
			} else if (realLabel == 0 && prediction == 0) {
				tn++;
			}
			if(realLabel == prediction) cnt++;
		}
		int size = testVecs.size();
		System.out.println("A: " + (double)cnt / size);
		double P = 1.0 * tp / (tp + fp);
		System.out.println("P: " + P);
		double R = 1.0 * tp / (tp + fn);
		System.out.println("R: " + R);
		System.out.println("F1: " + 2.0 * P * R / (P + R));

		/**
		 * CROSS VALIDATION
		 */

//		System.out.println(creator.getWordMap().size());
//		System.out.println(trainPosts.size() + " " + testPosts.size());
//		for (int x = -15; x <= 15; x++) {
//			params.setC(Math.pow(2, x));
//			System.out.println("Start alg");
//			// svm_model model = svm.svm_train(problem, params);
//			double[] target = new double[problem.y.length];
//
//			Linear.crossValidation(problem, params, 5, target);
//
//
//			int size = problem.y.length;
//			int cnt = 0;
//			for (int i = 0; i < size; i++) {
//				int l = (int) target[i];
//				int ml = (int) problem.y[i];
//				if (l == ml) {
//					cnt++;
//				}
//			}
//			System.out.println("For C = " + params.getC() + " and eps: "
//					+ params.getEps() + " accuracy is: " + (1.0 * cnt / size));
//		}

	}

	private static Feature[] getFeature(PostVector v) {
		Feature[] vecs = new Feature[v.getValues().length];
		int index = 1;
		for (double d : v.getValues()) {
			Feature f = new FeatureNode(index, d);
			vecs[index - 1] = f;
			index++;
		}
		return vecs;
	}
}
