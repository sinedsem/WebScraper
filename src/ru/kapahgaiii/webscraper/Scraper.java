package ru.kapahgaiii.webscraper;

import java.io.*;
import java.util.*;

public class Scraper {
    public static void main(String[] args) {

        Set<String> urls = getURLsSet(args[0]);
        if (urls == null) {
            return;
        }

        Set<String> words = getWordsSet(args[1]);

        Parameters params = new Parameters(args);

        Engine engine = new Engine();

        int totalCharactersCount = 0;
        int totalWordsCount = 0;
        List<String> totalSentences = new LinkedList<String>();

        for (String url : urls) {
            engine.loadData(url);
            System.out.println("URL: " + url);
            if (params.C) {
                int c = engine.getCharactersCount();
                totalCharactersCount += c;
                System.out.println("Characters count: " + c);
            }
            if (params.W) {
                int c = 0;
                Map<String, Integer> map = engine.getWordsCount(words.toArray(new String[words.size()]));
                for (Map.Entry<String, Integer> pair : map.entrySet()) {
                    c += pair.getValue();
                }
                totalWordsCount += c;
                System.out.println("Words count: " + c);
            }
            if (params.E) {
                String[] sentences = engine.getSentences(words.toArray(new String[words.size()]));
                if (sentences.length > 0) {
                    System.out.println("Sentences: ");
                    for (String sentence : sentences) {
                        totalSentences.add(sentence);
                        System.out.println("  " + sentence); // spaces only for beauty
                    }
                } else {
                    System.out.println("Sentences (words) not found");
                }
            }
            System.out.println();
        }

        System.out.println("TOTAL");
        if (params.C) {
            System.out.println("Characters count: " + totalCharactersCount);
        }
        if (params.W) {
            System.out.println("Words count: " + totalWordsCount);
        }
        if (params.E) {
            if (!totalSentences.isEmpty()) {
                System.out.println("Sentences: ");
                for (String sentence : totalSentences) {
                    System.out.println("  " + sentence); // spaces only for beauty
                }
            } else {
                System.out.println("Sentences (words) not found");
            }
        }


        if (params.V) {
            System.out.println();
            System.out.println("Total time spent");
            System.out.println("for downloading: " + engine.getDownloadTime() + " s");
            System.out.println("for processing: " + engine.getProcessTime() + " s");
        }

    }


    private static Set<String> getURLsSet(String s) {
        Set<String> urls = new HashSet<String>();
        if (Helpers.isUrl(s)) {
            urls.add(s);
        } else {
            BufferedReader reader;
            try {
                File input = new File(s);
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(input)));
            } catch (FileNotFoundException e) {
                System.err.println("Incorrect url/path to file");
                return null;
            }

            try {
                String url;
                while ((url = reader.readLine()) != null) {
                    if (Helpers.isUrl(url)) {
                        urls.add(url);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error while reading input file");
                return null;
            }

            if (urls.isEmpty()) {
                System.err.println("No urls found in file");
                return null;
            }
        }

        return urls;
    }

    private static Set<String> getWordsSet(String s) {
        return new HashSet<String>(Arrays.asList(s.split(",")));
    }
}
