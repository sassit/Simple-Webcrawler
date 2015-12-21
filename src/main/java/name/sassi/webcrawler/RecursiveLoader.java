package name.sassi.webcrawler;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.List;
import java.util.concurrent.RecursiveAction;

import static java.net.URLEncoder.encode;
import static org.apache.commons.io.FileUtils.copyURLToFile;
import static org.apache.commons.lang3.StringUtils.substringAfterLast;

/**
 * Created by tsassi on 20/12/2015.
 */
public class RecursiveLoader extends RecursiveAction {
    private final List<String> hrefs;
    private final CrawlingContext context;

    public RecursiveLoader(List<String> hrefs, CrawlingContext context) {
        this.hrefs = hrefs;
        this.context = context;
    }

    @Override
    protected void compute() {
        if (hrefs.size() > context.getThreshhold()) {
            int midIndex = hrefs.size() / 2;
            System.out.printf("Splitting list: %s\n", hrefs);
            RecursiveLoader left = new RecursiveLoader(hrefs.subList(0, midIndex), context);
            RecursiveLoader right = new RecursiveLoader(hrefs.subList(midIndex, hrefs.size()), context);
            invokeAll(left, right);
        } else {
            System.out.printf("Downloading list: %s\n", hrefs);
            download();
        }
    }

    private void download() {
        hrefs.stream()
                .forEach(href -> {
                    try {
                        URL url = new URL(href);
                        String encoded = encode(href, "UTF-8");
                        File file = defineOutputFile(url);
                        if (!file.exists()) {
                            copyURLToFile(new URL(encoded), file);
                            context.downloaded();
                            System.out.printf("Downloaded file #%d: %s to %s\n", context.getDownloaded(),
                                    href, file.getAbsolutePath());
                        } else {
                            // For paused or faulty downloads.
                            System.out.printf("File %s already resident.\n", file.getAbsolutePath());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    private File defineOutputFile(URL url) {
        String filename = substringAfterLast(url.getFile(), "/");
        return new File(context.getDestination() + filename);
    }
}
