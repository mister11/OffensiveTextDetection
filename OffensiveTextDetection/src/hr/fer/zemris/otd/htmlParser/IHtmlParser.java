package hr.fer.zemris.otd.htmlParser;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Queue;

/**
 * Interface which provides methods for parsing HTML-like {@link String}
 * 
 * @author Sven Vidak
 * 
 */
public interface IHtmlParser {

	/**
	 * {@link String} delimiter used to separate posts.
	 */
	static final String DELIM = "######";

	/**
	 * Method which extracts and writes posts to given file.
	 * 
	 * @param html
	 *            HTML from which posts are extracted
	 * @param w
	 *            {@link FileWriter} object used to write posts to file.
	 * @return <code>true</code> if any posts are written, <code>false</code> if
	 *         not
	 * @throws IOException
	 */
	public boolean write(String html, FileWriter w) throws IOException;

	/**
	 * Method used to extract specific links from given HTML-like {@link String}
	 * 
	 * @param html
	 *            HTML from which links are extracted.
	 * @param links
	 *            {@link Queue} of URLs which have to be processed
	 * @param visited
	 *            {@link HashSet} which contains all URLs which are processed
	 */
	public void extractLinks(String html, Queue<String> links,
			HashSet<String> visited);

}
