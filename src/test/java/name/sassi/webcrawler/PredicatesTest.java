package name.sassi.webcrawler;

import org.junit.Test;

import java.net.URL;

import static com.google.common.collect.Sets.newHashSet;
import static name.sassi.webcrawler.Predicates.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by tsassi on 20/12/2015.
 */
public class PredicatesTest {
    private final CrawlingContext mockContext = mock(CrawlingContext.class);

    @Test
    public void shouldBeLinkWithNoResource() throws Exception {
        assertThat(isLink().test("http://www.abc.com/nohtml/"), is(true));
    }

    @Test
    public void shouldBeNoLink() throws Exception {
        assertThat(isLink().test("http://www.abc.com/nohtml/img/some.jpg"), is(false));
    }

    @Test
    public void shouldBeContent() throws Exception {
        when(mockContext.getContent()).thenReturn(newHashSet("doc", "xml"));
        assertThat(isContent(mockContext).test("http://www.abc.com/nohtml/test.doc"), is(true));
    }

    @Test
    public void shouldNotBeContentAsNoResource() throws Exception {
        when(mockContext.getContent()).thenReturn(newHashSet("doc", "xml"));
        assertThat(isContent(mockContext).test("http://www.abc.com/nohtml/"), is(false));
    }

    @Test
    public void shouldNotBeContentAsDomainNameOnly() throws Exception {
        when(mockContext.getContent()).thenReturn(newHashSet("doc", "xml"));
        assertThat(isContent(mockContext).test("http://www.abc.com"), is(false));
    }

    @Test
    public void shouldBeSameDomain() throws Exception {
        when(mockContext.getRoot()).thenReturn(new URL("http://www.sassi.name"));
        assertThat(isSameDomain(mockContext).test("http://files.sassi.name"), is(true));
    }

    @Test
    public void shouldNotBeSameDomainDifferentHost() throws Exception {
        when(mockContext.getRoot()).thenReturn(new URL("http://www.sasso.name"));
        assertThat(isSameDomain(mockContext).test("http://files.sassi.name"), is(false));
    }

    @Test
    public void shouldNotBeSameDomainDifferentSuffix() throws Exception {
        when(mockContext.getRoot()).thenReturn(new URL("http://www.sassi.co.uk"));
        assertThat(isSameDomain(mockContext).test("http://files.sassi.name"), is(false));
    }
}