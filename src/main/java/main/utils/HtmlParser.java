package main.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

@Service
public final class HtmlParser {
    private HtmlParser() {
    }
    public static String html2text(final String html) {
        if (html == null || html.isBlank()) {
            return null;
        }
        Document document = Jsoup.parse(html);
        return document.wholeText();
    }
}
