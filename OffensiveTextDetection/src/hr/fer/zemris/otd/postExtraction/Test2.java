package hr.fer.zemris.otd.postExtraction;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;

public class Test2 {

	public static void main(String[] args) throws IOException {
		PostExtractor p = new PostExtractor();
		File[] files = p.getFiles("posts/");
		Random r = new Random();
		int size = files.length;
		// File f = files[r.nextInt(size)];
		// File f = new File("posts/index159.txt");
		FileWriter fw = new FileWriter("posts/dataset1.txt", true);
		HashSet<String> seen = new HashSet<String>();
		// int upperLimit = p.countPosts(f);
		// System.out.println(p.getRandomPost(f, r, upperLimit));
		while (seen.size() < 1000) {
			File f = files[r.nextInt(size)];
			int upperLimit = p.countPosts(f);
			String post = p.getRandomPost(f, r, upperLimit);
			if (!seen.contains(post)) {
				seen.add(post);
				fw.write(post + "\n\n######\n\n");
			}
		}
		fw.close();

		File f = new File("posts/dataset1.txt");
		System.out.println(p.countPosts(f));
	}

}
