package solutions.alterego.android.unisannio.ingegneria;

import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import solutions.alterego.android.unisannio.interfaces.IParser;
import solutions.alterego.android.unisannio.models.Article;
import solutions.alterego.android.unisannio.utils.DateUtils;

public class IngegneriaAvvisiStudentiParser implements IParser {

    @Override
    public List parse(Document document) {
        DateUtils dateUtils = null;

        Elements elements = document.select("#maincontent-block > #item");

        List<Article> list = new ArrayList<>();

        for (Element element : elements) {
            Element header = element.select("#header > h2 > a").first();
            String title = header.text();
            String url = header.attr("href");

            Elements paragraphs = element.select("p");
            String author = "";
            for (Element paragraph : paragraphs) {
                if (paragraph.text().contains("Autore")) {
                    author = paragraph.text().replace("Autore: ", "");
                }
            }
            if ("".equals(author)) {
                author = "Presidio didattico";
            }

            String body = element.select(".avvtext").first().text();

            String date = dateUtils.extractingData(element.select("#publishinfo").first().text());

            DateTimeFormatter dtf = DateTimeFormat.forPattern("dd/MM/yyyy");
            DateTime jodatime = dtf.parseDateTime(date);

            list.add(new Article(title, url, body, jodatime, author));
        }
        return list;
    }
}
