package ru.kapahgaiii.webscraper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Engine {
    private String preparedText;
    private long downloadTime, processTime;

    Engine() {
        downloadTime = 0;
        processTime = 0;
        preparedText = null;
    }

    public double getDownloadTime() {
        return (downloadTime/10000000)/100.0;
    }

    public double getProcessTime() {
        return (processTime/10000000)/100.0;
    }

    //we use this method to load page contents into engine
    public void loadData(String url) {
        preparedText = prepareText(getContentOfHTTPPage(url));
    }

    //three methods below returns sth we need
    //they works with private field preparedText
    public int getCharactersCount() {
        Stopwatch stopwatch = new Stopwatch();
        stopwatch.start();

        String text = preparedText;

        // Delete all tags
        Pattern pattern = Pattern.compile("<([^>]*>)");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            text = text.replace(matcher.group(), "");
        }

        // Delete tabs
        text = text.replace("\t", "");

        // Delete double spaces
        while (text.contains("  ")) {
            text = text.replace("  ", " ");
        }

        text = text.trim();

        processTime += stopwatch.stop();

        return text.length();
    }

    public Map<String, Integer> getWordsCount(String[] words) {
        Stopwatch stopwatch = new Stopwatch();
        stopwatch.start();

        String text = preparedText;
        Map<String, Integer> result = new HashMap<String, Integer>();

        // Delete all tags
        Pattern pattern = Pattern.compile("<([^>]*>)");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            text = text.replace(matcher.group(), "");
        }

        for (String word : words) {
            pattern = Pattern.compile(word, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
            matcher = pattern.matcher(text);
            int count = 0;
            while (matcher.find()) {
                count++;
            }
            result.put(word, count);
        }

        processTime += stopwatch.stop();

        return result;
    }

    public String[] getSentences(String[] words) {
        Stopwatch stopwatch = new Stopwatch();
        stopwatch.start();

        List<String> result = new LinkedList<String>();

        String text = preparedText;

        // Replace all tags by "."
        Pattern pattern = Pattern.compile("<([^>]*>)");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            text = text.replace(matcher.group(), ".");
        }

        // Replace all chars that can be the end of the string by "."
        pattern = Pattern.compile("!|\\?|\\|");
        matcher = pattern.matcher(text);
        while (matcher.find()) {
            text = text.replace(matcher.group(), ".");
        }

        // Delete tabs
        text = text.replace("\t", " ");

        // Delete double dots and spaces before dots
        pattern = Pattern.compile("(\\.\\.)|( \\.)");
        matcher = pattern.matcher(text);
        while (matcher.find()) {
            text = text.replace(matcher.group(), ".");
        }

        // Delete double spaces
        while (text.contains("  ")) {
            text = text.replace("  ", " ");
        }

        // find words and make sentences
        for (String word : words) {
            pattern = Pattern.compile("\\. ?([^\\.]*" + word + "[^\\.]*)\\.", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
            matcher = pattern.matcher(text);
            while (matcher.find()) {
                result.add(matcher.group(1).trim());
            }
        }

        processTime += stopwatch.stop();

        return result.toArray(new String[result.size()]);
    }


    // Private zone:

    // we use this to prepare html for future parsing
    private String prepareText(String text) {
        Stopwatch stopwatch = new Stopwatch();
        stopwatch.start();

        // Delete <a> tags - they are useless
        Pattern pattern = Pattern.compile("<(/?)a( (([^>]*)>)|>)");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            text = text.replace(matcher.group(), "");
        }

        // Delete head, script, style
        pattern = Pattern.compile("(<head.*</head>)|(<script[^>]*>.*?</script>)|(<style[^>]*>.*?</style>)");
        matcher = pattern.matcher(text);
        while (matcher.find()) {
            text = text.replace(matcher.group(), "");
        }

        // Replace HTML code symbols
        text = Helpers.unescapeHTML(text);

        processTime += stopwatch.stop();

        return text;
    }

    private String getContentOfHTTPPage(String url) {
        Stopwatch stopwatch = new Stopwatch();
        stopwatch.start();
        try {
            String codePage = "UTF-8";
            try {
                StringBuilder sb = new StringBuilder();
                URL pageURL = new URL(url);
                URLConnection uc = pageURL.openConnection();
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(
                                uc.getInputStream(), codePage));
                try {
                    String inputLine;
                    while ((inputLine = br.readLine()) != null) {
                        sb.append(inputLine);
                    }
                } finally {
                    br.close();
                }
                return sb.toString();
            } catch (Exception e) {
                System.out.println("Could not get page content (" + url + ")");
                return null;
            }
        } finally { // because some errors can occur
            downloadTime += stopwatch.stop();
        }
    }
}
