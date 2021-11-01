package main.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

/**
 * The type Html parser.
 */
@Service
public final class HtmlParser {
    /**
     * Instantiates a new Html parser.
     */
    private HtmlParser() {
    }
    /**
     * Html 2 text string.
     *
     * @param html the html
     * @return the string
     */
    public static String html2text(final String html) {
        if (html == null || html.isBlank()) {
            return null;
        }
        Document document = Jsoup.parse(html);
        return document.wholeText();
    }
}
