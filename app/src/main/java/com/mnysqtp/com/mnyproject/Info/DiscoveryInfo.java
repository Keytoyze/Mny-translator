package com.mnysqtp.com.mnyproject.Info;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.support.annotation.XmlRes;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

public class DiscoveryInfo {

    private String Url;
    private String title;
    private String resource;

    private static final String XML_TAG = "item";
    private static final String XML_ATTRIBUTE_NAME_TITLE = "title";
    private static final String XML_ATTRIBUTE_NAME_RESOURCE = "resource";
    private static final String XML_ATTRIBUTE_NAME_URL = "url";

    private DiscoveryInfo(String Url, String title, String resource){
        this.Url = Url;
        this.title = title;
        this.resource = resource;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public static ArrayList<DiscoveryInfo> fromXML(@XmlRes int res, Context context){
        XmlResourceParser xmlParser = context.getResources().getXml(res);
        ArrayList<DiscoveryInfo> result = new ArrayList<>();
        try {
            int event = xmlParser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event){
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if (xmlParser.getName().equals(XML_TAG)) {
                            result.add(new DiscoveryInfo(
                                    xmlParser.getAttributeValue(null, XML_ATTRIBUTE_NAME_URL),
                                    xmlParser.getAttributeValue(null, XML_ATTRIBUTE_NAME_TITLE),
                                    xmlParser.getAttributeValue(null, XML_ATTRIBUTE_NAME_RESOURCE)
                            ));
                        }
                        break;
                    case XmlPullParser.TEXT:
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                    default:
                        break;
                }
                event = xmlParser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
