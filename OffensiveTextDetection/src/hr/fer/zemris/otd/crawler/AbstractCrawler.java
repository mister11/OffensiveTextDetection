package hr.fer.zemris.otd.crawler;

import hr.fer.zemris.otd.htmlParser.IHtmlParser;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Pattern;

/**
 * Class which implements functionality specific to all crawlers used.
 *
 * @author Sven Vidak
 */

public abstract class AbstractCrawler {

	/**
	 * Initial value for file distinction.
	 */
	private static int fileSufix = 101;

	private String initLink;
	private String file;
	private Queue<String> links;
	private HashSet<String> visited;
	private Pattern threadPattern;
	private Pattern postPattern;

	/**
	 * Sets up all necessary parameters.
	 *
	 * @param initLink      Link from which crawling will start.
	 * @param filePath      Path where .txt file will be saved.
	 * @param filePrefix    Arbitrary file prefix used for distinction between pages from
	 *                      which data is crawled.
	 * @param threadPattern Pattern used for recognizing pages which contains links to
	 *                      threads.
	 * @param postPattern   Pattern used for recognizing pages which contains posts.
	 */
	public AbstractCrawler(String initLink, String filePath, String filePrefix,
						   String threadPattern, String postPattern) {
		this.initLink = initLink;
		this.threadPattern = Pattern.compile(threadPattern);
		this.postPattern = Pattern.compile(postPattern);
		this.file = filePath + File.separator + filePrefix;
		links = new LinkedList<String>();
		visited = new HashSet<String>();
		visited.add(initLink);
		// there are two links to first page
		visited.add("http://forum.dnevnik.hr/dnevnik?page25=1");
	}

	/**
	 * Method used for running crawler.
	 *
	 * @param parser Some implementation of {@link IHtmlParser}
	 */
	public void run(IHtmlParser parser) {
		String html = getHtml(this.initLink);
		parser.extractLinks(html, links, visited);
		while (!links.isEmpty()) {
			String url = links.poll();
			html = getHtml(url);
			if (postPattern.matcher(url).matches()) {
				try {
					System.out.println("NEW THREAD");
					handlePost(url, html, parser);
				} catch (IOException e) {
					System.err.println("Unable to write post to file.");
					System.exit(-1);
				}
			} else if (threadPattern.matcher(url).matches()) {
				System.out.println("Extracting url:\t" + url);
				parser.extractLinks(html, links, visited);
			}
		}
	}

	// breaks when 0 posts are extracted or when there are no more pages
	private void handlePost(String url, String html, IHtmlParser parser)
			throws IOException {
		while (true) {
			System.out.println("Extracting posts:\t" + url);
			boolean flag = extractPosts(parser, html);
			if (!flag) {
				fileSufix++;
				break;
			}
			url = nextPageUrl(url);
			visited.add(url);
			html = getHtml(url);
			if (html == null) {
				fileSufix++;
				break;
			}
		}
	}

	/**
	 * Method used for getting next page URL given some URL.
	 *
	 * @param url Current URL.
	 * @return URL to next page.
	 */
	protected abstract String nextPageUrl(String url);

	private boolean extractPosts(IHtmlParser parser, String html)
			throws IOException {
		if (html == null) {
			return false;
		}
		String fileName = file + String.valueOf(fileSufix) + ".txt";
		FileWriter w = new FileWriter(fileName, true);
		return parser.write(html, w);
	}

	/**
	 * Method which get web page content given some URL.
	 *
	 * @param link URL
	 * @return Web page content in form of HTML.
	 */
	protected String getHtml(String link) {
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(link);
		HttpResponse response = null;
		BufferedReader rd = null;
		try {
			response = client.execute(request);
			rd = new BufferedReader(new InputStreamReader(response.getEntity()
					.getContent()));
			String line = "";
			StringBuilder sb = new StringBuilder();
			while ((line = rd.readLine()) != null) {
				sb.append(line + System.lineSeparator());
			}
			return sb.toString();
		} catch (IllegalStateException | IOException e) {
			System.err.println("End of thread!");
			return null;
		} finally {
			if (rd != null) {
				try {
					rd.close();
				} catch (IOException e) {
					System.err.println("Cannot close input stream!");
					System.exit(-1);
				}
			}
			HttpClientUtils.closeQuietly(client);
		}

	}
}
