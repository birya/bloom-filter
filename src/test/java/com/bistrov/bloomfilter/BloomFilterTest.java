package com.bistrov.bloomfilter;

import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class BloomFilterTest {

    private URL url = this.getClass().getResource("/wordlist.txt");
    private File file = new File(url.getFile());

    @Test
    public void testNumberOfChunksWithMD5() throws NoSuchAlgorithmException {
        BloomFilter bloomFilterMD5 = new BloomFilter();

        assertThat(bloomFilterMD5.getNumberOfChunks(), is(4));
    }

    @Test
    public void testNumberOfChunksWithSHA256() throws NoSuchAlgorithmException {
        BloomFilter bloomFilterSHA256 = new BloomFilter(MessageDigest.getInstance("SHA-256"));

        assertThat(bloomFilterSHA256.getNumberOfChunks(), is(8));
    }

    @Test
    public void testProbablyContains() {
        BloomFilter bloomFilter = new BloomFilter();
        bloomFilter.add("aaaa".getBytes());

        assertThat(bloomFilter.probablyContains("aaaa".getBytes()), is(true));
    }

    @Test
    public void testDoesNotContain() {
        BloomFilter bloomFilter = new BloomFilter();
        bloomFilter.add("aaaa".getBytes());

        assertThat(bloomFilter.probablyContains("aaa".getBytes()), is(false));
    }

    @Test
    public void testProbablyInFile() {
        List<String> strings = new TextFileParser().parse(file);
        BloomFilter bloomFilter = new BloomFilter();
        for (String string : strings) {
            bloomFilter.add(string.getBytes());
        }

        for (String string : strings) {
            assertThat(bloomFilter.probablyContains(string.getBytes()), is(true));
        }
    }

    @Test
    public void testNotInFile() {
        List<String> strings = new TextFileParser().parse(file);
        BloomFilter bloomFilter = new BloomFilter();
        for (String string : strings) {
            bloomFilter.add(string.getBytes());
        }

        assertThat(bloomFilter.probablyContains("someString".getBytes()), is(false));
    }

}
