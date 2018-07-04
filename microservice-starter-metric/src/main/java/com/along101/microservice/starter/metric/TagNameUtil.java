package com.along101.microservice.starter.metric;


import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Scanner;
import java.util.TreeSet;

public class TagNameUtil {
    private static final char QP_SEP_A = '&';
    private static final char QP_SEP_S = ';';
    private static final String NAME_VALUE_SEPARATOR = "=";
    private static final char[] QP_SEPS = new char[]{QP_SEP_A, QP_SEP_S};
    private static final String QP_SEP_PATTERN = "[" + new String(QP_SEPS) + "]";

    public static TagName parse(final String query) {
        String name = StringUtils.substringBefore(query, "?");
        String tags = StringUtils.substringAfter(query, "?");
        TagName tagName = TagName.name(name);
        if (tags != null && tags.length() > 0) {
            final Scanner scanner = new Scanner(tags);
            scanner.useDelimiter(QP_SEP_PATTERN);
            while (scanner.hasNext()) {
                final String token = scanner.next();
                final int i = token.indexOf(NAME_VALUE_SEPARATOR);
                if (i != -1) {
                    String tagKey = token.substring(0, i).trim();
                    String tagValue = token.substring(i + 1).trim();
                    tagName.addTag(tagKey, tagValue);
                }
            }
        }
        return tagName;
    }


    public static String format(TagName tagName) {
        StringBuilder nameFormat = new StringBuilder(tagName.getName());
        if (tagName.getTags().size() > 0) {
            Map<String, String> tags = tagName.getTags();
            TreeSet<String> keys = new TreeSet<>(tags.keySet());
            StringBuilder tagFormat = new StringBuilder();
            for (String key : keys) {
                final String name = key;
                final String value = tags.get(key);
                if (StringUtils.isBlank(name) || StringUtils.isBlank(value)) {
                    continue;
                }
                if (tagFormat.length() > 0) {
                    tagFormat.append(QP_SEP_A);
                }
                tagFormat.append(name).append(NAME_VALUE_SEPARATOR).append(value);
            }
            if (tagFormat.length() > 0) {
                nameFormat.append("?").append(tagFormat);
            }
        }
        return nameFormat.toString();
    }

}
