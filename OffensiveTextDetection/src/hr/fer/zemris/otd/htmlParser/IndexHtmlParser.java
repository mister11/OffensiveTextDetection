package hr.fer.zemris.otd.htmlParser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Queue;

public class IndexHtmlParser implements IHtmlParser {

	@Override
	public boolean write(String html, FileWriter w) throws IOException {
		Document doc = Jsoup.parse(html);
		Elements posts = doc.select("div.postText");
		boolean flag = (posts.size() > 0) ? true : false;
		for (Element e : posts) {
			Elements real = e.select("p");
			for (Element a : real) {
				String text = a.text().trim();
				if (a.parentNode().nodeName() != "blockquote"
						&& text.length() > 1) {
					w.write(text + System.lineSeparator());
				}
			}
			w.write(System.lineSeparator() + DELIM + System.lineSeparator()
					+ System.lineSeparator());
		}
		w.close();
		return flag;
	}

	@Override
	public void extractLinks(String html, Queue<String> links,
							 HashSet<String> visited) {
		Document doc = Jsoup.parse(html);
		Elements topics = doc.select("dt.topictitle");
		for (Element e : topics) {
			Elements titles = e.select("a.topicitem");
			for (Element a : titles) {
				String title = a.attr("abs:href");
				int pos = title.lastIndexOf("#");
				if (pos != -1) {
					title = title.substring(0, pos);
				}
				if (title.length() > 1 && !visited.contains(title)) {
					visited.add(title);
					links.add(title);
				}
			}
		}
		Elements pages = doc.select("div.leftinner.sep");
		for (Element e : pages) {
			Elements page = e.select("a");
			for (Element a : page) {
				String link = a.attr("abs:href");
				int pos = link.lastIndexOf("#");
				if (pos != -1) {
					link = link.substring(0, pos);
				}
				if (link.length() > 1 && !visited.contains(link)) {
					visited.add(link);
					links.add(link);
				}
			}
		}
	}

}
