package hr.fer.zemris.otd.statistics;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CohensKappa {

	public int countPosts(String path) {
		File folder = new File(path);
		File[] files = folder.listFiles();
		int numOfPosts = 0;
		for (File f : files) {
			try {
				numOfPosts += countPosts(f);
			} catch (IOException e) {
				System.err.println("Error during posts count!");
				System.exit(-1);
			}
		}
		System.out.println("Total number of posts: " + numOfPosts);
		return numOfPosts;
	}

	public void makeCharVectors(String path, LabelVectors vectors) {
		File folder = new File(path);
		File[] files = folder.listFiles();
		for (File f : files) {
			try {
				makeVector(f, vectors);
			} catch (IOException e) {
				System.err.println("Error during posts count!");
				System.exit(-1);
			}
		}
		System.out.println("Vectors made!");
	}

	public int[] makeStatsTableNoX(List<String> first, List<String> second) {
		int[] stats = new int[4];
		int size = Math.min(first.size(), second.size());
		for (int i = 0; i < size; i++) {
			if (first.get(i).equals("1") && second.get(i).equals("1")) {
				stats[0]++;
			} else if (first.get(i).equals("1") && second.get(i).equals("0")) {
				stats[1]++;
			} else if (first.get(i).equals("0") && second.get(i).equals("1")) {
				stats[2]++;
			} else if (first.get(i).equals("0") && second.get(i).equals("0")) {
				stats[3]++;
			}
		}
		return stats;
	}

	public int[] makeStatsTable(List<String> first, List<String> second) {
		int[] stats = new int[9];
		int size = Math.min(first.size(), second.size());
		for (int i = 0; i < size; i++) {
			if (first.get(i).equals("1") && second.get(i).equals("1")) {
				stats[0]++;
			} else if (first.get(i).equals("1") && second.get(i).equals("0")) {
				stats[1]++;
			} else if (first.get(i).equals("0") && second.get(i).equals("1")) {
				stats[2]++;
			} else if (first.get(i).equals("0") && second.get(i).equals("0")) {
				stats[3]++;
			} else if (first.get(i).equals("1") && second.get(i).equals("x")) {
				stats[4]++;
			} else if (first.get(i).equals("0") && second.get(i).equals("x")) {
				stats[5]++;
			} else if (first.get(i).equals("x") && second.get(i).equals("1")) {
				stats[6]++;
			} else if (first.get(i).equals("x") && second.get(i).equals("0")) {
				stats[7]++;
			} else if (first.get(i).equals("x") && second.get(i).equals("x")) {
				stats[8]++;
			}
		}
		return stats;
	}

	public double getScore(int[] stats) {
		if (stats.length == 4) {
			int diagonal = stats[0] + stats[3];
			int col1 = stats[0] + stats[2];
			int col2 = stats[1] + stats[3];
			int row1 = stats[0] + stats[1];
			int row2 = stats[2] + stats[3];
			int total = 0;
			for (int i = 0; i < stats.length; i++) {
				total += stats[i];
			}
			double ef = (1.0 * row1 * col1) / total + (1.0 * row2 * col2)
					/ total;
			return (diagonal - ef) / (total - ef);
		} else if (stats.length == 9) {
			int diagonal = stats[0] + stats[3] + stats[8];
			int col1 = stats[0] + stats[2] + stats[6];
			int col2 = stats[1] + stats[3] + stats[7];
			int col3 = stats[4] + stats[5] + stats[8];
			int row1 = stats[0] + stats[1] + stats[4];
			int row2 = stats[2] + stats[3] + stats[5];
			int row3 = stats[6] + stats[7] + stats[8];
			int total = 0;
			for (int i = 0; i < stats.length; i++) {
				total += stats[i];
			}
			double ef = (1.0 * row1 * col1) / total + (1.0 * row2 * col2)
					/ total + (1.0 * row3 * col3) / total;
			return (diagonal - ef) / (total - ef);
		} else {
			return -1;
		}

	}

	public void makeVector(File f, LabelVectors vectors) throws IOException {
		List<String> lines = Files.readAllLines(f.toPath(),
				StandardCharsets.UTF_8);
		for (String l : lines) {
			l = l.trim();
			if (l.isEmpty()) {
				continue;
			}
			if (isTagging(l)) {
				String[] labels = l.split("\\s+");
				vectors.offensive.add(labels[0]);
				vectors.rude.add(labels[1]);
				vectors.sarcasm.add(labels[2]);
				vectors.target.add(labels[3]);
			}
		}
		System.out.println("Done processing: " + f.getName());
	}

	public int countPosts(File f) throws IOException {
		List<String> lines = Files.readAllLines(f.toPath(),
				StandardCharsets.UTF_8);
		int cnt = 0;
		for (String l : lines) {
			l = l.trim();
			if (l.isEmpty()) {
				continue;
			}
			if (isTagging(l)) {
				cnt++;
			}
		}
		return cnt;
	}

	private boolean isTagging(String l) {
		Pattern p = Pattern.compile("[0|1|x ]+");
		Matcher m = p.matcher(l);
		// return ((Character.isDigit(l.charAt(0)) || l.charAt(0) == 'x') &&
		// (Character
		// .isDigit(l.charAt(2)) || l.charAt(2) == 'x'));
		return m.matches();
	}
}
