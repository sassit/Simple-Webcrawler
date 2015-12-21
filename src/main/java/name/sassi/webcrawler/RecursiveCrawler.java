package name.sassi.webcrawler;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.RecursiveAction;

import static com.google.common.collect.Sets.newHashSet;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.partitioningBy;
import static java.util.stream.Collectors.toList;
import static name.sassi.webcrawler.Predicates.*;
import static org.jsoup.Jsoup.connect;

/**
 * Created by tsassi on 20/12/2015.
 */
public class RecursiveCrawler extends RecursiveAction {
    private final Set<String> hrefs;
    private final CrawlingContext context;

    public RecursiveCrawler(Set<String> hrefs, CrawlingContext context) {
        this.hrefs = hrefs;
        this.context = context;
    }

    @Override
    protected void compute() {
        Set<String> elements = getElements(hrefs);
        if (!elements.isEmpty()) {
            Set<String> nonVisited = context.nonVisited(elements);
            Map<Boolean, List<String>> partitioned = nonVisited.stream()
                    .filter(eitherContentOrLink(context))
                    .collect(partitioningBy(isContent(context)));
            List<String> content = partitioned.get(TRUE);
            List<String> links = partitioned.get(FALSE);
            System.out.printf("Found content: %s\n", content);
            System.out.printf("Found these links: %s\n", links);
            RecursiveLoader recursiveLoader = new RecursiveLoader(content, context);
            RecursiveCrawler leftCrawler = new RecursiveCrawler(newHashSet(links.subList(0, links.size() / 2)), context);
            RecursiveCrawler rightCrawler = new RecursiveCrawler(newHashSet(links.subList(links.size() / 2, links.size())), context);
            invokeAll(recursiveLoader, leftCrawler, rightCrawler);
        }
    }

    private Set<String> getElements(Set<String> links) {
        Set<String> newLinks = newHashSet();
        try {
            for (String link : links) {
                Connection connect = connect(link)
                        .timeout(10000)
                        .followRedirects(false);
                Document doc = connect.get();
                newLinks.addAll(doc.select("a[href]").stream()
                        .map(element -> element.attr("abs:href"))
                        .filter(isSameDomain(context))
                        .collect(toList()));
                context.visited(link);
            }
            System.out.printf("Visited %d hrefs.\n", context.getVisitedLinks());
            return newLinks;
        } catch (IOException e) {
            e.printStackTrace();
            return emptySet();
        }
    }
}
