package hr.fer.zemris.otd.dataPreprocessing;

import hr.fer.zemris.otd.vectors.PostVector;
import hr.fer.zemris.otd.vectors.VectorCreator;

import java.io.IOException;
import java.util.List;

public class Test {

	public static void main(String[] args) throws IOException {
		String path = "C:/Users/Big Sven/Desktop/experiment/lemma/novo_clean_output.txt";
		String inputPath = "C:/Users/Big Sven/Desktop/experiment/lemma/novo_ulaz_u_cst.txt";
		String outputPath = "C:/Users/Big Sven/Desktop/experiment/lemma/my_word_list.txt";

		PredataCreator creator = new PredataCreator();

		creator.createMap(outputPath);
		creator.createPostsList("C:/Users/Big Sven/Desktop/experiment/lemma/novo_razmaci_bezPraznihLinija.txt");

		VectorCreator vc = new VectorCreator(creator);

		String saveOccurVector = "C:/Users/Big Sven/Desktop/experiment/lemma/occur.txt";
		String saveTfIdfVector = "C:/Users/Big Sven/Desktop/experiment/lemma/tfidf.txt";

		System.out.println(creator.getPosts().size());
		List<PostVector> occurVectors = vc.createOccurrenceVectors();
		List<PostVector> occur = vc.nNormalizeVectors(occurVectors);
		vc.saveVectorsInFile(occur, saveOccurVector);
		System.out.println();
		List<PostVector> tfIdfVectors = vc.createTfIdfVectors();
		vc.normalizeVectors(tfIdfVectors);
		vc.saveVectorsInFile(tfIdfVectors, saveTfIdfVector);
		System.out.println();

		// List<PostVector> occur = vc.getVectorsFromFile(saveOccurVector);
		// List<PostVector> tfidf = vc.getVectorsFromFile(saveTfIdfVector);
		// System.out.println();

	}

}
