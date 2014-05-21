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
		List<Post> trainPosts = Deserialize.listPosts("trainSet.ser");
		List<Post> testPosts = Deserialize.listPosts("testSet.ser");

		DataProcessor dp = new DataProcessor(trainPosts);
		PredataCreator creator = new PredataCreator();
		creator.createMapWithMinCount(trainPosts, 0);
		ProbabilityCalculator pc = new ProbabilityCalculator(creator.getWordMap(), dp);
		for(Post post : testPosts) {
			double pos = Math.log(pc.getPosClassProbab());
			double neg = Math.log(pc.getNegClassProbab());
			for(String word : post.getPostText().split("\\p{Z}")) {
				String w = word.trim().toLowerCase();
				pos += Math.log(pc.getPositiveProb(w));
				neg += Math.log(pc.getNegativeProb(w));
			}
			System.out.println("Real label: " + post.getLabel(0) + "\tCalc. label: " + ((pos > neg) ? "1" : "0"));
		}

	}
}
