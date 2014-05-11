package hr.fer.zemris.otd.dataPreprocessing;

import java.io.IOException;
import java.util.List;

/**
 * Statistika za ZR - 3. dio
 * 
 * @author Sven Vidak
 *
 */

public class Test {

	public static void main(String[] args) throws IOException {
		PredataCreator creator = new PredataCreator();
		creator.createPostsList("C:/Users/Big Sven/Desktop/experiment/lemma/novo_razmaci_bezPraznihLinija.txt");
		int posOff = 0;
		int negOff = 0;
		int posRude = 0;
		int negRude = 0;
		for (Post p : creator.getPosts()) {
			char[] labels = p.getLabels();
			if (labels[0] == '1') {
				posOff++;
			} else if (labels[0] == '0') {
				negOff++;
			}
			if (labels[1] == '1') {
				posRude++;
			} else if (labels[1] == '0') {
				negRude++;
			}
		}

		System.out.println("Offense positive: " + posOff);
		System.out.println("Offense negative: " + negOff);
		System.out.println("Rude positive: " + posRude);
		System.out.println("Rude negative: " + negRude);

		List<Post> specPost = creator
				.createSpecPostsList("C:/Users/Big Sven/Desktop/experiment/lemma/novo_razmaci_bezPraznihLinija.txt");
		for (Post p : specPost) {
			System.out.println("Mark: " + p.getMark());
			System.out.println(p);
			System.out.println();
		}
	}
}
