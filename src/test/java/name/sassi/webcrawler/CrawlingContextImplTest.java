package name.sassi.webcrawler;

import org.junit.Test;

import static com.google.common.collect.Sets.newHashSet;
import static java.util.Collections.emptySet;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.junit.Assert.assertThat;

/**
 * Created by tsassi on 21/12/2015.
 */
public class CrawlingContextImplTest {
    @Test
    public void shouldNotBeVisited(){
        String abc = "http://www.abc.com";
        CrawlingContextImpl crawlingContext = new CrawlingContextImpl(5, "/tmp", emptySet(), abc);
        crawlingContext.visited("http://www.cnn.com");
        assertThat(crawlingContext.nonVisited(newHashSet(abc)), hasItem(abc));
    }

    @Test
    public void shouldBeVisited(){
        String cnn = "http://www.cnn.com";
        CrawlingContextImpl crawlingContext = new CrawlingContextImpl(5, "/tmp", emptySet(), cnn);
        crawlingContext.visited("http://www.cnn.com");
        assertThat(crawlingContext.nonVisited(newHashSet(cnn)), not(hasItem(cnn)));
    }
}