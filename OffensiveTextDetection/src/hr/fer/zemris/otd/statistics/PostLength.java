package hr.fer.zemris.otd.statistics;

import hr.fer.zemris.otd.dataPreprocessing.Post;

import java.util.List;

/**
 * Created by Sven Vidak on 07.06.2014. 22:25.
 */
public class PostLength {

	public static void calcAll(List<Post> train, List<Post> test) {
		int minWords = 500;
		int maxWords = 0;
		double avgWords = 0;
		int minChars = 1000;
		int maxChars = 0;
		double avgChars = 0;

		for(Post p : train) {
			minChars = Math.min(minChars, p.getPostText().length());
			maxChars = Math.max(maxChars, p.getPostText().length());
			avgChars += p.getPostText().length();

			String[] words = p.getPostText().split("\\p{Z}");
			minWords = Math.min(minWords, words.length);
			maxWords = Math.max(maxWords, words.length);
			avgWords += words.length;
		}

		for (Post p : test) {
			int len = p.getPostText().trim().length();
			minChars = Math.min(minChars, len);
			maxChars = Math.max(maxChars, len);
			avgChars += len;

			String[] words = p.getPostText().trim().split("\\p{Z}");
			minWords = Math.min(minWords, words.length);
			maxWords = Math.max(maxWords, words.length);
			avgWords += words.length;
		}
		int size = train.size() + test.size();

		avgChars = avgChars / size;
		avgWords = avgWords / size;

		System.out.println("Min chars: " + minChars);
		System.out.println("Max chars: " + maxChars);
		System.out.println("Avg chars: " + avgChars);
		System.out.println("Min words: " + minWords);
		System.out.println("Max words: " + maxWords);
		System.out.println("Avg words: " + avgWords);


	}
}
