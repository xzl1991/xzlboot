package com.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by xlizy on 2017/4/5.
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

    public static StringBuilder formatMsg(String s, Object aobj[]) {
        return formatMsg(new StringBuilder(s), true, aobj);
    }

    public static StringBuilder formatMsg(CharSequence charsequence, boolean flag, Object aobj[]) {
        int i = aobj.length;
        boolean flag1 = false;
        StringBuilder stringbuilder = new StringBuilder(charsequence);
        if (i > 0) {
            for (int j = 0; j < i; j++) {
                String s = new StringBuilder().append("%").append(j + 1).toString();
                for (int k = stringbuilder.indexOf(s); k >= 0; k = stringbuilder.indexOf(s)) {
                    flag1 = true;
                    stringbuilder.replace(k, k + 2, toString(aobj[j], flag));
                }

            }

            if (aobj[i - 1] instanceof Throwable) {
                StringWriter stringwriter = new StringWriter();
                ((Throwable) aobj[i - 1]).printStackTrace(new PrintWriter(stringwriter));
                stringbuilder.append("\n").append(stringwriter.toString());
            } else if (i == 1 && !flag1) {
                stringbuilder.append(aobj[i - 1].toString());
            }
        }
        return stringbuilder;
    }

    public static String toString(Object obj, boolean flag) {
        StringBuilder stringbuilder = new StringBuilder();
        if (obj == null) {
            stringbuilder.append("NULL");
        } else if (obj instanceof Object[]) {
            for (int i = 0; i < ((Object[]) obj).length; i++) {
                stringbuilder.append(((Object[]) obj)[i]).append(", ");
            }

            if (stringbuilder.length() > 0) {
                stringbuilder.delete(stringbuilder.length() - 2, stringbuilder.length());
            }
        } else {
            stringbuilder.append(obj.toString());
        }
        if (flag && stringbuilder.length() > 0
                && (stringbuilder.charAt(0) != '[' || stringbuilder.charAt(stringbuilder.length() - 1) != ']')
                && (stringbuilder.charAt(0) != '{' || stringbuilder.charAt(stringbuilder.length() - 1) != '}')) {
            stringbuilder.insert(0, "[").append("]");
        }
        return stringbuilder.toString();
    }

}
