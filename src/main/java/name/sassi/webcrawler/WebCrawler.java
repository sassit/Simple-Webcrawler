package name.sassi.webcrawler;

import java.io.IOException;
import java.util.concurrent.ForkJoinPool;

import static com.google.common.collect.Sets.newHashSet;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by tsassi on 20/12/2015.
 */
public class WebCrawler {
    public static void main(String[] args) throws IOException, InterruptedException {
        ForkJoinPool pool = new ForkJoinPool(4);
        String root = "http://www.allitebooks.com";
        CrawlingContext context = new CrawlingContextImpl(10, "/tmp/allitebooks/", newHashSet("pdf"), root);
        RecursiveCrawler task = new RecursiveCrawler(newHashSet(root), context);
        pool.execute(task);
        do {
            System.out.printf("Thread count: %d\n", pool.getRunningThreadCount());
            System.out.printf("Thread steal: %d\n", pool.getStealCount());
            System.out.printf("Parallelism: %d\n", pool.getParallelism());
            try {
                SECONDS.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (!task.isDone());
        pool.shutdown();
        if(task.isCompletedNormally()) {
            System.out.printf("Process completed normally\n");
        }
    }
}
