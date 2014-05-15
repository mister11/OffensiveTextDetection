package hr.fer.zemris.otd.crawler;

public class IndexCrawler extends AbstractCrawler {

	public IndexCrawler(String initLink, String filePath, String filePrefix,
						String threadPattern, String postPattern) {
		super(initLink, filePath, filePrefix, threadPattern, postPattern);
	}

	// URL example:
	// http://www.index.hr/indexforum/postovi/127544/pobjeda-demokratskog-dinama/1
	@Override
	protected String nextPageUrl(String url) {
		int slash = url.lastIndexOf("/");
		int page = Integer.parseInt(url.substring(slash + 1));
		page++;
		return url.substring(0, slash + 1) + String.valueOf(page);
	}

}
