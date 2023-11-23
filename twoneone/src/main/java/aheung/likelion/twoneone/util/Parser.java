package aheung.likelion.twoneone.util;

import java.util.StringTokenizer;

public class Parser {

    public static String getContentType(String input) {
        StringTokenizer st = new StringTokenizer(input, "/");

        if (st.hasMoreTokens()) {
            return st.nextToken();
        }

        return null;
    }
}
