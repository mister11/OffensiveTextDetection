package hr.fer.zemris.otd.naiveBayes;

import hr.fer.zemris.otd.dataPreprocessing.Post;
import hr.fer.zemris.otd.dataPreprocessing.PredataCreator;
import hr.fer.zemris.otd.utils.Deserialize;

import java.util.List;

/**
 * Created by Sven Vidak on 21.05.2014..
 */
public class Test {

	public static void main(String[] args) {
		List<Post> trainPosts = Deserialize.listPosts("data/trainSet.ser");
		List<Post> testPosts = Deserialize.listPosts("data/testSet.ser");

		DataProcessor dp = new DataProcessor(trainPosts);
		PredataCreator creator = new PredataCreator();
		creator.createMapWithMinCount(trainPosts, 0);
		ProbabilityCalculator pc = new ProbabilityCalculator(creator.getWordMap(), dp);
		int tp = 0;
		int fn = 0;
		int fp = 0;
		int tn = 0;
		int cnt = 0;
		for(Post post : testPosts) {
			double pos = Math.log(pc.getPosClassProbab());
			double neg = Math.log(pc.getNegClassProbab());
			for(String word : post.getPostText().split("\\p{Z}")) {
				String w = word.trim().toLowerCase();
				pos += Math.log(pc.getPositiveProb(w));
				neg += Math.log(pc.getNegativeProb(w));
			}
			int realLabel = Integer.valueOf(String.valueOf(post.getLabel(1)));
			int prediction = (pos > neg) ? 1 : 0;
			if (realLabel == 1 && prediction == 1) {
				tp++;
			} else if (realLabel == 1 && prediction == 0) {
				fn++;
			} else if (realLabel == 0 && prediction == 1) {
				fp++;
			} else if (realLabel == 0 && prediction == 0) {
				tn++;
			}
			if (realLabel == prediction) cnt++;

			System.out.println("Real label: " + post.getLabel(0) + "\tCalc. label: " + ((pos > neg) ? "1" : "0"));
		}
		int size = testPosts.size();
		System.out.println("A: " + (double) cnt / size);
		double P = 1.0 * tp / (tp + fp);
		System.out.println("P: " + P);
		double R = 1.0 * tp / (tp + fn);
		System.out.println("R: " + R);
		System.out.println("F1: " + 2.0 * P * R / (P + R));

	}
}
