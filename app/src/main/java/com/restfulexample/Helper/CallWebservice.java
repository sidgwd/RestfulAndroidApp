/**
 * Copyright (C) 2013 Viraat Technology Labs Pvt. Ltd.
 * http://www.viraat.info
 * This class connects to the webservice
 * and returns result as json string
 */
package com.restfulexample.Helper;

import com.restfulexample.Model.UserDetail;
import com.restfulexample.Utill.AppConfig;

import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;


/**
 * @author siddhesh gawde trainer at suven consultant
 * @since 2016
 */
public class CallWebservice {
    /**
     * @return
     */
    public static ArrayList<UserDetail> viewUserList() {
        ArrayList<UserDetail> lstDetails = null;
        try {
            //finalize the url
            String url = AppConfig.WEB_URL + AppConfig.VIEW_ALL_USERS;
            //initialize response object
            HttpResponse response = null;
            //initialize stream object
            InputStream is = null;
            try {
                //initialize client to connect with the url with respective method
                HttpClient httpClient = new DefaultHttpClient();
                //initialize the request method
                HttpGet httpGet = new HttpGet(url);
                //get the response
                response = httpClient.execute(httpGet);
                //get entities from response
                HttpEntity entity = response.getEntity();
                //get the data in streams
                is = entity.getContent();
                //parse the data
                lstDetails = XMLMsgParser.parseUserData(is);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                is.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lstDetails;
    }

    /**
     * @param userDetail
     * @return
     */
    public static ArrayList<UserDetail> createUser(UserDetail userDetail) {
        ArrayList<UserDetail> lstDetails = null;
        try {
            //finalize the url
            String url = AppConfig.WEB_URL + AppConfig.CREATE_USERS;
            //initialize response object
            HttpResponse response = null;
            //initialize stream object
            InputStream is = null;
            try {
                //initialize client to connect with the url with respective method
                HttpClient httpClient = new DefaultHttpClient();
                //initialize the request method
                HttpPut httpPut = new HttpPut(url);
                //get the response
                httpPut.addHeader("Content-Type", "application/xml");
                //set the form entity
                httpPut.setEntity(new StringEntity(XMLSerializer.createUserXML(userDetail), HTTP.UTF_8));
                //get the response
                response = httpClient.execute(httpPut);
                //get entities from response
                HttpEntity entity = response.getEntity();
                //get the data in streams
                is = entity.getContent();
                //parse the data
                lstDetails = XMLMsgParser.parseUserData(is);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                is.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lstDetails;
    }

    /**
     * @param userDetail
     * @return
     */
    public static ArrayList<UserDetail> updateUser(UserDetail userDetail) {
        ArrayList<UserDetail> lstDetails = null;

        try {
            //finalize the url
            String url = AppConfig.WEB_URL + AppConfig.UPDATE_USERS;
            //initialize response object
            HttpResponse response = null;
            InputStream is = null;
            //initialize stream object
            String line = null;
            try {
                //initialize client to connect with the url with respective method
                HttpClient httpClient = new DefaultHttpClient();
                //initialize the request method
                HttpPost httpPost = new HttpPost(url);
                //set supported header
                httpPost.addHeader("Content-Type", "application/xml");
                //get xml data
                String xml = XMLSerializer.createUserXML(userDetail);
                //set the form entity
                httpPost.setEntity(new StringEntity(xml, HTTP.UTF_8));
                //get the response
                response = httpClient.execute(httpPost);
                //get entities from response
                HttpEntity entity = response.getEntity();
                //get the data in streams
                is = entity.getContent();
                //parse the data
                lstDetails = XMLMsgParser.parseUserData(is);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                is.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lstDetails;
    }

    /**
     * @param userDetail
     * @return
     */
    public static ArrayList<UserDetail> deleteUser(UserDetail userDetail) {
        ArrayList<UserDetail> lstDetails = null;
        try {
            //finalize the url
            String url = AppConfig.WEB_URL + AppConfig.DELETE_USERS;
            //initialize response object
            HttpResponse response = null;
            //initialize stream object
            InputStream is = null;
            try {
                //initialize client to connect with the url with respective method
                HttpClient httpClient = new DefaultHttpClient();
                //initialize the request method
                HttpDeleteWithBody httpDeleteWithBody = new HttpDeleteWithBody(url);
                //set supported header
                httpDeleteWithBody.addHeader("Content-Type", "application/xml");
                String xml = XMLSerializer.createUserXML(userDetail);
                //set the form entity
                httpDeleteWithBody.setEntity(new StringEntity(xml));
                //get entities from response
                response = httpClient.execute(httpDeleteWithBody);
                //get the form entites
                HttpEntity entity = response.getEntity();
                //get the data in streams
                is = entity.getContent();
                //parse the data
                lstDetails = XMLMsgParser.parseUserData(is);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                is.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lstDetails;
    }

}
