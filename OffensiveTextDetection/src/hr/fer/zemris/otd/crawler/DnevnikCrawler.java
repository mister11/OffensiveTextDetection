package hr.fer.zemris.otd.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DnevnikCrawler extends AbstractCrawler {

	private int currPage;
	private int maxPage;

	public DnevnikCrawler(String initLink, String filePath, String filePrefix,
						  String threadPattern, String postPattern) {
		super(initLink, filePath, filePrefix, threadPattern, postPattern);
		this.currPage = 1;
		this.maxPage = 1;
	}

	// first page URL example:
	// http://forum.dnevnik.hr/dnevnik/h/140817/rusija-osudila-cinizam-washingtona-sto-je-s-bombardiranjem-bivse-jugoslavije
	// other pages URL example:
	// http://forum.dnevnik.hr/dnevnik/h/140817/rusija-osudila-cinizam-washingtona-sto-je-s-bombardiranjem-bivse-jugoslavije?page53=2
	@Override
	protected String nextPageUrl(String url) {
		boolean isFistPage = (url.lastIndexOf("?") == -1) ? true : false;
		if (isFistPage) {
			setMaxPage(url);
		}
		this.currPage++;
		if (this.currPage > this.maxPage) {
			this.currPage = 1;
			this.maxPage = 1;
			return "error";
		}
		if (isFistPage) {
			setMaxPage(url);
			return url + "?page53=" + this.currPage;
		} else {
			return url.substring(0, url.indexOf("=") + 1) + this.currPage;
		}

	}

	private void setMaxPage(String url) {
		String html = getHtml(url);
		Document doc = Jsoup.parse(html);
		Elements pages = doc.select("p.paginator.f_right.mb_0");
		for (Element e : pages) {
			Elements last = e.select("a.btn_pag2.textual.mr_0");
			for (Element l : last) {
				String text = l.text();
				int openBracket = l.text().lastIndexOf("(");
				int closeBracket = l.text().lastIndexOf(")");
				this.maxPage = Integer.parseInt(text.substring(openBracket + 1,
						closeBracket));
			}
		}

	}

}
