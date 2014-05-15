package hr.fer.zemris.otd.htmlParser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Queue;

public class DnevnikHtmlParser implements IHtmlParser {

	@Override
	public boolean write(String html, FileWriter w) throws IOException {
		Document doc = Jsoup.parse(html);
		Elements posts = doc.select("div.post.pv_24");
		boolean flag = (posts.size() > 0) ? true : false;
		for (Element e : posts) {
			Elements real = e.select("p");
			if (real.size() == 1) {
				Elements hidden = e.select("div.hidden_post");
				for (Element h : hidden) {
					String post = getPost(h).trim();
					if (post.length() > 1) {
						w.write(post + "\n");
					}
				}
			} else {
				for (Element a : real) {
					if (!a.className().isEmpty()) {
						continue;
					}
					String text = a.text().trim();
					if (text.length() > 1) {
						w.write(text + System.lineSeparator());
					}
				}
			}
			w.write(System.lineSeparator() + DELIM + System.lineSeparator()
					+ System.lineSeparator());
		}
		w.close();
		return flag;
	}

	private String getPost(Element h) {
		Element postElement = h.select("a").first();
		if (postElement == null) {
			return "";
		}
		String post = postElement.attr("data-post");
		String cleanPost = post.replaceAll("\\<.*?>", "");
		return cleanPost;
	}

	@Override
	public void extractLinks(String html, Queue<String> links,
							 HashSet<String> visited) {
		Document doc = Jsoup.parse(html, "http://forum.dnevnik.hr/");
		Elements topics = doc.select("div.title");
		for (Element e : topics) {
			Elements titles = e.select("a.thread_title");
			for (Element a : titles) {
				String title = a.attr("abs:href");
				if (title.length() > 1 && !visited.contains(title)) {
					links.add(title);
					visited.add(title);
				}
			}
		}
		Elements pages = doc.select("p.paginator.f_right.mb_0");
		for (Element e : pages) {
			Elements page = e.select("a.btn_pag2");
			for (Element a : page) {
				String link = a.attr("abs:href");
				if (link.length() > 1 && !visited.contains(link)) {
					links.add(link);
					visited.add(link);
				}
			}
		}
	}

}
