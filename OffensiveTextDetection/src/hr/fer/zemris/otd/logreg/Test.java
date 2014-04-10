package hr.fer.zemris.otd.logreg;

import hr.fer.zemris.otd.dataPreprocessing.PredataCreator;
import hr.fer.zemris.otd.logreg.utils.LogRegUtils;
import hr.fer.zemris.otd.utils.Pair;
import hr.fer.zemris.otd.vectors.PostVector;
import hr.fer.zemris.otd.vectors.VectorCreator;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

		creator.createMap(outputPath);
		creator.createPostsList("C:/Users/Big Sven/Desktop/experiment/lemma/novo_razmaci_bezPraznihLinija.txt");

		VectorCreator vc = new VectorCreator(creator);

		Pair<List<PostVector>, List<PostVector>> sets = deserialize();
		List<PostVector> trainSet;
		List<PostVector> testSet;
		if (sets == null) {
			trainSet = vc.getVectorsFromFile(occurTrainVectors);
			testSet = vc.getVectorsFromFile(occurTestVectors);
		} else {
			trainSet = sets.x;
			testSet = sets.y;
		}

		if (trainSet == null || testSet == null) {
			List<PostVector> vectors = vc.getVectorsFromFile(saveOccurVector);
			sets = vc.getSets(vectors, 0.8);
			trainSet = sets.x;
			testSet = sets.y;
			serialize(trainSet, testSet);
		}
		IFunction function = new SigmoidFunction();
		RealMatrix data = LogRegUtils.getData(trainSet);
		RealMatrix theta = LogRegUtils.getTheta(creator.getWordMap().size());
		RealMatrix labels = LogRegUtils.getLabels(trainSet);
		LogisticRegression logReg = new LogisticRegression(function);
		int cnt = 0;
		for (double[] row : labels.getData()) {
			if (row[0] == 1) {
				cnt++;
			}
		}
		System.out.println(cnt);
		// RealMatrix finalTheta = deserTheta();
		// if (finalTheta == null) {
		// finalTheta = logReg.runGradientDescent(theta, data, labels, 0, 200,
		// true);
		// serTheta(finalTheta);
		// }
		// data = LogRegUtils.getData(testSet);
		cnt = 0;
		labels = LogRegUtils.getLabels(testSet);
		for (double[] row : labels.getData()) {
			if (row[0] == 1) {
				cnt++;
			}
		}
		System.out.println(cnt);
		// logReg.predict(finalTheta, data, labels);

	}

	private static RealMatrix deserTheta() {
		FileInputStream fis1 = null;
		ObjectInputStream ois = null;
		try {
			fis1 = new FileInputStream("serTheta1.ser");
			ois = new ObjectInputStream(fis1);
			RealMatrix theta = (RealMatrix) ois.readObject();
			return theta;
		} catch (IOException | ClassNotFoundException ex) {
			return null;
		} finally {
			if (fis1 != null) {
				try {
					fis1.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static void serTheta(RealMatrix theta) {
		FileOutputStream fos1 = null;
		ObjectOutputStream oos = null;
		try {
			fos1 = new FileOutputStream("serTheta1.ser");
			oos = new ObjectOutputStream(fos1);
			oos.writeObject(theta);
		} catch (IOException ex) {
			System.err.println("Serialization failed!");
			return;
		} finally {
			if (fos1 != null) {
				try {
					fos1.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static Pair<List<PostVector>, List<PostVector>> deserialize()
			throws IOException, ClassNotFoundException {
		FileInputStream fis1 = null;
		FileInputStream fis2 = null;
		ObjectInputStream ois = null;
		try {
			fis1 = new FileInputStream("serTrain.ser");
			fis2 = new FileInputStream("serTest.ser");
			ois = new ObjectInputStream(fis1);
			List<PostVector> trainSet = (List<PostVector>) ois.readObject();
			ois = new ObjectInputStream(fis2);
			List<PostVector> testSet = (List<PostVector>) ois.readObject();
			return new Pair<List<PostVector>, List<PostVector>>(trainSet,
					testSet);
		} catch (IOException | ClassNotFoundException ex) {
			return null;
		} finally {
			if (fis1 != null) {
				fis1.close();
			}
			if (fis2 != null) {
				fis2.close();
			}
			if (ois != null) {
				ois.close();
			}
		}

	}

	private static void serialize(List<PostVector> trainSet,
			List<PostVector> testSet) throws IOException {
		FileOutputStream fos1 = null;
		FileOutputStream fos2 = null;
		ObjectOutputStream oos = null;
		try {
			fos1 = new FileOutputStream("serTrain.ser");
			fos2 = new FileOutputStream("serTest.ser");
			oos = new ObjectOutputStream(fos1);
			oos.writeObject(trainSet);
			oos = new ObjectOutputStream(fos2);
			oos.writeObject(testSet);
		} catch (IOException ex) {
			System.err.println("Serialization failed!");
			return;
		} finally {
			if (fos1 != null) {
				fos1.close();
			}
			if (fos2 != null) {
				fos2.close();
			}
			if (oos != null) {
				oos.close();
			}
		}
	}

}
