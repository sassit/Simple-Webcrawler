package name.sassi.webcrawler;

import java.net.URL;
import java.util.Set;

import static com.google.common.collect.ImmutableSet.copyOf;

/**
 * Created by tsassi on 20/12/2015.
 */
public interface CrawlingContext {
    default Set<String> getLinks() {
        return copyOf(new String[]{"html", "htm"});
    }

    int getThreshhold();

    String getDestination();

    Set<String> getContent();

    URL getRoot();

    void visited(String visited);

    Set<String> nonVisited(Set<String> found);

    int getVisitedLinks();

    void downloaded();

    int getDownloaded();
}
