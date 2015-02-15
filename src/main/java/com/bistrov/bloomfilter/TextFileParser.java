package com.bistrov.bloomfilter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TextFileParser {

    public List<String> parse(File file) {
        List<String> words = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String nextLine;
            while((nextLine = br.readLine()) != null) {
                words.add(nextLine);
            }
        } catch (FileNotFoundException  e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return words;
    }

}
