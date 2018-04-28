package com.restfulexample.Helper;

import android.util.Log;
import android.util.Xml;

import com.restfulexample.Model.UserDetail;

import org.xmlpull.v1.XmlSerializer;

import java.io.StringWriter;

/**
 * @author siddhesh gawde trainer at suven consultant
 * @since 2016
 */
public class XMLSerializer {

    // Creating xml data for generating user details
    /**
     * @param detail
     * @return
     * @throws Exception
     */
    public static String createUserXML(UserDetail detail)
            throws Exception {
        StringWriter GenerateXml = null;
        try {
            //initialize serializer
            XmlSerializer xmlSerializer = Xml.newSerializer();
            GenerateXml = new StringWriter();

            xmlSerializer.setOutput(GenerateXml);
            //set encoding
            xmlSerializer.startDocument("UTF-8", true);
            //start serializing the data
            xmlSerializer.startTag(null, "user");

            xmlSerializer.startTag(null, "id");
            xmlSerializer.text(String.valueOf(detail.getId()));
            xmlSerializer.endTag(null, "id");

            xmlSerializer.startTag(null, "name");
            xmlSerializer.text(detail.getName().trim());
            xmlSerializer.endTag(null, "name");

            xmlSerializer.startTag(null, "profession");
            xmlSerializer.text(detail.getProfession().trim());
            xmlSerializer.endTag(null, "profession");

            xmlSerializer.endTag(null, "user");
            //end here and flush it
            xmlSerializer.endDocument();
            xmlSerializer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.v("XML DATA", GenerateXml.toString());
        return GenerateXml.toString();

    }
}
