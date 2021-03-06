package solutions.alterego.android.unisannio.interfaces;

import org.jsoup.nodes.Document;

import java.util.List;

import rx.Observable;

public interface IParser<T> {

    List<T> parse(Document document);
}
