package name.sassi.webcrawler;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by tsassi on 20/12/2015.
 */
public class CrawlingContextImpl implements CrawlingContext {
    private final int threshhold;
    private final AtomicInteger visited, downloaded;
    private final ConcurrentSkipListSet<String> visitedLinks;
    private final Set<String> content;
    private final String destination;
    private final String root;

    public CrawlingContextImpl(int threshhold, String destination, Set<String> content, String root) {
        this.threshhold = threshhold;
        this.destination = destination;
        this.content = content;
        this.root = root;
        this.visitedLinks = new ConcurrentSkipListSet<>();
        this.visited = new AtomicInteger(0);
        this.downloaded = new AtomicInteger(0);
    }

    public int getThreshhold() {
        return threshhold;
    }

    public String getDestination() {
        return destination;
    }

    public Set<String> getContent() {
        return content;
    }

    public URL getRoot() {
        try {
            return new URL(root);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void visited(String visited) {
        this.visitedLinks.add(visited);
        this.visited.getAndIncrement();
    }

    public Set<String> nonVisited(Set<String> found){
        found.removeAll(visitedLinks);
        return found;
    }

    public void downloaded(){
        downloaded.getAndIncrement();
    }

    public int getDownloaded() {
        return downloaded.get();
    }

    public int getVisitedLinks() {
        return visited.get();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CrawlingContextImpl{");
        sb.append("threshhold=").append(threshhold);
        sb.append(", destination=").append(destination);
        sb.append(", content=").append(content);
        sb.append(", root='").append(root).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
