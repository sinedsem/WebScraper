package ru.kapahgaiii.webscraper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helpers {
    //replaces HTML codes by their meanings or *
    public static String unescapeHTML(String s) {
        s = s.replace("&nbsp;", " ");
        s = s.replace("&#160;", " ");
        s = s.replace("&amp;", "&");
        s = s.replace("&#38;", "&");
        s = s.replace("&quot;", "\"");
        s = s.replace("&#34;", "\"");
        s = s.replace("&#39;", "'");

        // if unknown we can replace it by *
        Pattern pattern = Pattern.compile("&([a-z]{2,6})|&(#[0-9]{2,4});");
        Matcher matcher= pattern.matcher(s);
        while (matcher.find()) {
            s = s.replace(matcher.group(), "*");
        }

        return s;
    }

    //checks if the string is a url
    public static boolean isUrl(String str){
        Pattern urlPattern = Pattern.compile("((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)",Pattern.CASE_INSENSITIVE);
        Matcher matcher = urlPattern.matcher(str);
        return matcher.find();
    }
}
