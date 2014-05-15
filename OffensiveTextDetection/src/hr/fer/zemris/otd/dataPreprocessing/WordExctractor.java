package hr.fer.zemris.otd.dataPreprocessing;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class which gets text file with everything separated by space and makes file
 * in which are all words in format: one word - one line.
 *
 * @author Sven Vidak
 */
public class WordExctractor {

	/**
	 * Method that takes path to input file in format described above and
	 * outputs file at location given as ouput path.
	 *
	 * @param inputPath  Input file in described format.
	 * @param outputPath Output file in format: one word - one line
	 * @throws IOException If there is some error during processing files (writing or
	 *                     reading)
	 */
	public static void getAllWords(String inputPath, String outputPath)
			throws IOException {
		File f = new File(inputPath);
		FileWriter fw = new FileWriter(outputPath);
		List<String> lines = Files.readAllLines(f.toPath(),
				StandardCharsets.UTF_8);
		for (String l : lines) {
			for (String w : l.split("\\p{Z}")) {
				if (isNotRubbish(w.trim())) {
					fw.write(w.trim() + System.lineSeparator());
				}
			}
		}
		fw.close();
	}

	/**
	 * Checks whether given string is valid string or not. Valid string is
	 * everything except punctuation or some crazy looking strings.
	 *
	 * @param w String from file.
	 * @return <code>true</code> if string is valid word, <code>false</code>
	 * otherwise
	 */
	private static boolean isNotRubbish(String w) {
		Pattern p = Pattern.compile("@?\\p{L}+(\\d+)?|\\d+");
		Matcher m = p.matcher(w);
		return m.matches();
	}

}
