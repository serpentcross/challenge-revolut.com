package com.revolut.challenge.utils;

import java.util.Collection;

public class InfoPrinter {

    public static String printInfo(Collection collection, String emptyMessage) {

        StringBuilder stringBuilder = new StringBuilder("");

        if (collection.size() > 0) {
            for (Object singleObject : collection) {
                stringBuilder.append(singleObject.toString());
            }
        } else {
            stringBuilder.append(emptyMessage);
        }

        return stringBuilder.toString();
    }

}
