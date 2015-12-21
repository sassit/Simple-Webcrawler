package name.sassi.webcrawler;

import java.io.IOException;
import java.util.concurrent.ForkJoinPool;

import static com.google.common.collect.Sets.newHashSet;

/**
 * Created by tsassi on 20/12/2015.
 */
public class WebCrawler {
    public static void main(String[] args) throws IOException, InterruptedException {
        ForkJoinPool pool = new ForkJoinPool();
        String root = "http://www.allitebooks.com";
        CrawlingContext context = new CrawlingContextImpl(10, "/tmp/allitebooks/", newHashSet("pdf"), root);
        RecursiveCrawler task = new RecursiveCrawler(newHashSet(root), context);
        pool.invoke(task);
        pool.shutdown();
        if (task.isCompletedNormally()) {
            System.out.println("Process completed normally.");
        }
    }
}
