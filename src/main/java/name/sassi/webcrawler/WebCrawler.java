package name.sassi.webcrawler;

import java.io.IOException;
import java.util.concurrent.ForkJoinPool;

import static com.google.common.collect.Sets.newHashSet;

/**
 * Created by tsassi on 20/12/2015.
 */
public class WebCrawler {
    public static void main(String[] args) throws IOException, InterruptedException {
        ForkJoinPool pool = new ForkJoinPool(4);
        String root = "http://www.allpdfs.com";
        CrawlingContext context = new CrawlingContextImpl(10, "/tmp/downloads/", newHashSet("pdf"), root);
        RecursiveCrawler task = new RecursiveCrawler(newHashSet(root), context);
        pool.execute(task);
        pool.shutdown();
        if(task.isCompletedNormally()) {
            System.out.printf("Process completed normally\n");
        }
    }
}
