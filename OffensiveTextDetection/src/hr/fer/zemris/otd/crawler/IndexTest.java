package hr.fer.zemris.otd.crawler;

import hr.fer.zemris.otd.htmlParser.IHtmlParser;
import hr.fer.zemris.otd.htmlParser.IndexHtmlParser;

public class IndexTest {

	public static void main(String[] args) {

		String initLink = "http://www.index.hr/indexforum/teme/3/Index-hr-Komentari-clanaka";
		String filePath = "posts";
		String filePrefix = "index";
		String threadPattern = "http://www.index.hr/indexforum/teme/3/Index-hr-Komentari-clanaka/.*";
		String postPattern = "http://www.index.hr/indexforum/postovi/.*";

		IHtmlParser parser = new IndexHtmlParser();
		AbstractCrawler crawler = new IndexCrawler(initLink, filePath,
				filePrefix, threadPattern, postPattern);
		crawler.run(parser);

	}
}
