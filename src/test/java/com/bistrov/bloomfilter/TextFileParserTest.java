package com.bistrov.bloomfilter;

import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TextFileParserTest {

    private URL url = this.getClass().getResource("/wordlist.txt");
    private File file = new File(url.getFile());

    @Test
    public void testParseFile() {
        TextFileParser parser = new TextFileParser();
        List<String> words = parser.parse(file);

        assertThat(words.contains("zoozoo"), is(true));
    }

}
