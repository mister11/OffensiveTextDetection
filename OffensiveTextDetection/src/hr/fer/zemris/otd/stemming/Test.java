package hr.fer.zemris.otd.stemming;

import java.io.IOException;

public class Test {

	public static void main(String[] args) throws IOException {
		// PredataCreator creator = new PredataCreator();
		// creator.createPostsList("C:/Users/Big Sven/Desktop/experiment/lemma/novo_razmaci_bezPraznihLinija.txt");
		// List<Post> allPosts = creator.getPosts();
		//
		// IDatasetSplitter splitter = new EqualDatasetSplitter();
		// Pair<List<Post>, List<Post>> dataSets = splitter.createDatasets(
		// allPosts, 0.8);
		DataManager dataManager = new DataManager();
		// dataManager.writePlainPosts(dataSets.x,
		// "D:/Documents/SVEN/FER/ZR/stemmer/posts.txt");

		// dataManager.stemPosts("D:/Documents/SVEN/FER/ZR/stemmer/",
		// "Croatian_stemmer.py", "posts.txt", "stemmedPosts.txt");

		dataManager
				.createMap("D:/Documents/SVEN/FER/ZR/stemmer/stemmedPosts.txt");
		System.out.println();
	}

}
