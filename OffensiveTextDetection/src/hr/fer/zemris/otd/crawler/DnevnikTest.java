package hr.fer.zemris.otd.crawler;

import hr.fer.zemris.otd.htmlParser.DnevnikHtmlParser;
import hr.fer.zemris.otd.htmlParser.IHtmlParser;

public class DnevnikTest {

	public static void main(String[] args) {

		String initLink = "http://forum.dnevnik.hr/dnevnik";
		String filePath = "posts";
		String filePrefix = "dnevnik";
		String threadPattern = "http://forum.dnevnik.hr/dnevnik.*";
		String postPattern = "http://forum.dnevnik.hr/dnevnik/h/.*";

		IHtmlParser parser = new DnevnikHtmlParser();
		AbstractCrawler crawler = new DnevnikCrawler(initLink, filePath,
				filePrefix, threadPattern, postPattern);
		crawler.run(parser);

	}
}
