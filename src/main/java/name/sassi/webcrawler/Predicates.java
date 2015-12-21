package name.sassi.webcrawler;

import com.google.common.net.InternetDomainName;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.Predicate;

import static com.google.common.net.InternetDomainName.from;
import static org.apache.commons.lang3.StringUtils.substringAfterLast;

/**
 * Created by tsassi on 20/12/2015.
 */
public class Predicates {
    static Predicate<String> isContent(CrawlingContext context) {
        return href -> {
            String lastPart = substringAfterLast(href, "/");
            String suffix = substringAfterLast(lastPart, ".");
            return context.getContent().contains(suffix);
        };
    }

    static Predicate<String> isLink(CrawlingContext context) {
        return extension -> context.getLinks().contains(extension);
    }

    static Predicate<String> isLink() {
        return extension -> extension.endsWith("/");
    }

    static Predicate<String> isSameDomain(CrawlingContext context) {
        return node -> {
            try {
                InternetDomainName domainName = from(context.getRoot().getHost()).topPrivateDomain();
                InternetDomainName nodeName = from(new URL(node).getHost()).topPrivateDomain();
                return domainName.equals(nodeName);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return false;
        };
    }

    static Predicate<String> eitherContentOrLink(CrawlingContext context) {
        return isLink(context).or(isLink()).or(isContent(context));
    }
}
