package hr.fer.zemris.otd.temp;

import hr.fer.zemris.otd.dataPreprocessing.Post;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Sven Vidak on 27.05.2014. 00:09.
 */
public class ArtPostsCreator {

	public List<Post> getArtificalPosts(String filename) throws IOException {
		List<String> lines = Files.readAllLines(new File(filename).toPath(), StandardCharsets.UTF_8);
		Random r = new Random();
		List<Post> posts = new ArrayList<>();
		getGood(lines, posts, r);
		getBad(lines, posts, r);
		return posts;
	}

	private void getBad(List<String> lines, List<Post> posts, Random r) {
		int size = (int) (lines.size() * 0.2 + 0.5);
		for (int i = 0; i < size; i++) {
			Post p = new Post(2);
			p.setLabel(0, '1');
			p.setLabel(1, '1');
			StringBuilder sb = new StringBuilder();
			for (int j = 0; j < 7; j++) {
				int index = 40 + r.nextInt(10);
				sb.append(lines.get(index) + " ");
			}
			p.setPostText(sb.toString().trim());
			posts.add(p);
		}
	}

	private void getGood(List<String> lines, List<Post> posts, Random r) {
		int size = (int) (lines.size() * 0.8 + 0.5);
		for(int i=0; i < size; i++) {
			Post p = new Post(2);
			p.setLabel(0, '0');
			p.setLabel(1, '0');
			StringBuilder sb = new StringBuilder();
			for(int j=0; j < 7; j++) {
				int index = r.nextInt(40);
				sb.append(lines.get(index) + " ");
			}
			p.setPostText(sb.toString().trim());
			posts.add(p);
		}

	}

	public static void main(String[] args) throws IOException {
		ArtPostsCreator creator = new ArtPostsCreator();
		creator.getArtificalPosts("C:/Users/Big Sven/Desktop/experiment/lemma/art_wordMap.txt");
	}
}
