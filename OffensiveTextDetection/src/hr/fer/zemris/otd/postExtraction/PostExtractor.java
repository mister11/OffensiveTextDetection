package hr.fer.zemris.otd.postExtraction;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Random;

public class PostExtractor {

	public File[] getFiles(String path) {
		File f = new File(path);
		if (!f.isDirectory()) {
			return null;
		}
		return f.listFiles();
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
			if (l.startsWith("#####")) {
				cnt++;
			}
		}
		return cnt;
	}

	public String getRandomPost(File f, Random r, int upperLimit)
			throws IOException {

		List<String> lines = Files.readAllLines(f.toPath(),
				StandardCharsets.UTF_8);
		while (true) {
			int postNum = r.nextInt(upperLimit);
			System.out.println("Post num: " + postNum + " in file: "
					+ f.getName());
			int size = lines.size();
			for (int i = 0; i < size; i++) {
				String line = lines.get(i).trim();
				if (line.isEmpty() || Character.isDigit(line.charAt(0))
						|| line.charAt(0) == 'x') {
					continue;
				}
				if (line.startsWith("#####")) {
					postNum--;
				} else if (postNum <= 0) {
					String post = getPost(i, lines);
					if (post == null) {
						break;
					} else {
						return post;
					}
				}
			}
		}
	}

	private String getPost(int i, List<String> lines) {
		StringBuilder sb = new StringBuilder();
		while (true) {
			if (i >= lines.size()) {
				return null;
			}
			String line = lines.get(i).trim();
			if (line.isEmpty() || Character.isDigit(line.charAt(0))
					|| line.charAt(0) == 'x') {
				i++;
				continue;
			}
			if (line.startsWith("#####")) {
				if (sb.toString().trim().length() < 10) {
					return null;
				} else {
					return sb.toString();
				}
			}
			sb.append(line.trim() + System.lineSeparator());
			i++;
		}
	}
}
