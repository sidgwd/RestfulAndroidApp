package com.restfulexample.Helper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

import com.restfulexample.Model.UserDetail;

/**
 * @author siddhesh gawde trainer at suven consultant
 * @since 2016
 */
public class XMLMsgParser {

    private static final String ns = null;

    /**
     * @param in
     * @return
     * @throws XmlPullParserException
     * @throws IOException
     */
    public static ArrayList<UserDetail> parseUserData(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    /**
     * @param parser
     * @return
     * @throws XmlPullParserException
     * @throws IOException
     */
    private static ArrayList<UserDetail> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        ArrayList<UserDetail> lstUser = new ArrayList<UserDetail>();

        parser.require(XmlPullParser.START_TAG, ns, "userDetails");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("user")) {
                lstUser.add(readEntry(parser));
            } else {
                skip(parser);
            }
        }
        return lstUser;
    }

    /**
     * @param parser
     * @return
     * @throws XmlPullParserException
     * @throws IOException
     */
    private static UserDetail readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "user");

        UserDetail userDetail = new UserDetail();
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("id")) {
                userDetail.setId(Integer.parseInt(readTags(parser, "id")));
            } else if (name.equals("name")) {
                userDetail.setName(readTags(parser, "name"));
            } else if (name.equals("profession")) {
                userDetail.setProfession(readTags(parser, "profession"));
            } else {
                skip(parser);
            }
        }
        return userDetail;
    }


    /**
     * @param parser
     * @param tagName
     * @return
     * @throws IOException
     * @throws XmlPullParserException
     */
    private static String readTags(XmlPullParser parser, String tagName)
            throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, tagName);
        String res = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, tagName);
        return res;
    }

    /**
     * @param parser
     * @return
     * @throws IOException
     * @throws XmlPullParserException
     */
    private static String readText(XmlPullParser parser) throws IOException,
            XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.next();

        }
        return result;
    }

    /**
     * @param parser
     * @throws XmlPullParserException
     * @throws IOException
     */
    private static void skip(XmlPullParser parser)
            throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
