package com.sscf.investment.sdk.utils;

import android.content.Context;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by davidwei on 2016/10/08
 */
public final class PlistUtils {

    private static final java.lang.String TAG_PLIST = "plist";
    private static final java.lang.String TAG_DICT = "dict";
    private static final java.lang.String TAG_PLIST_ARRAY = "array";
    private static final java.lang.String TAG_KEY = "key";
    private static final java.lang.String TAG_INTEGER = "integer";
    private static final java.lang.String TAG_STRING = "string";

    public static ArrayList parsePlist(final Context context, final String assetsXmlPath) {
        //获得XmlPullParser解析器
        final XmlPullParser xmlParser = Xml.newPullParser();
        InputStream in = null;
        String tag = null;
        ArrayList list = null;
        try {
            in = context.getResources().getAssets().open(assetsXmlPath);
            xmlParser.setInput(in, "utf-8");
            int eventType;
            do {
                eventType = xmlParser.next();
                tag = xmlParser.getName();
                if (TAG_PLIST_ARRAY.equals(tag)) {
                    list = parseArray(xmlParser);
                }
            } while (eventType != XmlPullParser.END_DOCUMENT);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            FileUtil.closeStream(in);
        }
        return list;
    }

    private static ArrayList parseArray(final XmlPullParser xmlParser) throws Exception {
        String tag = null;
        int eventType;
        final ArrayList list = new ArrayList();
        do {
            eventType = xmlParser.next();
            tag = xmlParser.getName();
            if (TAG_DICT.equals(tag)) {
                list.add(parseDict(xmlParser));
            } else if (TAG_STRING.equals(tag)) {
                list.add(parseString(xmlParser));
            }
        } while (eventType != XmlPullParser.END_TAG && !TAG_PLIST_ARRAY.equals(tag));
        return list;
    }

    private static Map<String, Object> parseDict(final XmlPullParser xmlParser) throws Exception {
        String tag = null;
        int eventType;
        String key = null;
        String strValue = null;
        final Map<String, Object> map = new HashMap<>();
        do {
            eventType = xmlParser.next();
            tag = xmlParser.getName();
            if (TAG_KEY.equals(tag)) {
                key = parseKey(xmlParser);
            } else if (TAG_STRING.equals(tag)) {
                map.put(key, parseString(xmlParser));
            } else if (TAG_PLIST_ARRAY.equals(tag)) {
                map.put(key, parseArray(xmlParser));
            }
        } while (eventType != XmlPullParser.END_TAG && !TAG_DICT.equals(tag));
        return map;
    }

    private static Object parseDict(final XmlPullParser xmlParser, final Class cls) throws Exception {
        String tag = null;
        int eventType;
        String key = null;
        String strValue = null;
        final Object o = cls.newInstance();
        do {
            eventType = xmlParser.next();
            tag = xmlParser.getName();
            if (TAG_KEY.equals(tag)) {
                key = parseKey(xmlParser);
            } else if (TAG_STRING.equals(tag)) {
                strValue = parseString(xmlParser);
                ReflectUtils.setFieldValue(o, key, strValue);
            } else if (TAG_PLIST_ARRAY.equals(tag)) {
                ReflectUtils.setFieldValue(o, key, parseArray(xmlParser));
            }
        } while (eventType != XmlPullParser.END_TAG && !TAG_DICT.equals(tag));
        return o;
    }

    private static String parseKey(final XmlPullParser xmlParser) throws Exception {
        return parseItem(xmlParser);
    }

    private static String parseString(final XmlPullParser xmlParser) throws Exception {
        return parseItem(xmlParser);
    }

    private static String parseItem(final XmlPullParser xmlParser) throws Exception {
        int eventType;
        String value = null;
        do {
            eventType = xmlParser.next();
            if (eventType == XmlPullParser.TEXT) {
                value = xmlParser.getText();
                if (value != null) {
                    value = value.trim();
                }
            }
        } while (eventType != XmlPullParser.END_TAG);
        return value;
    }
}
