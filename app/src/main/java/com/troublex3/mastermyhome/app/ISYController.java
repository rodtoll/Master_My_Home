package com.troublex3.mastermyhome.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by rodtoll on 4/7/14.
 */
public class ISYController {

    public static final String NODE_NAME_FOLDER_ROOT = "folder";
    public static final String NODE_NAME_DEVICE_ROOT = "node";
    public static final String NODE_NAME_FOLDER_NAME = "name";
    public static final String NODE_NAME_FOLDER_ADDRESS = "address";
    public static final String KEY_PREF_ISY_ADDRESS = "isy.address";
    public static final String KEY_PREF_ISY_USERNAME = "isy.username";
    public static final String KEY_PREF_ISY_PASSWORD = "isy.password";
    public static final String NODE_NAME_DEVICE_NAME = "name";
    public static final String NODE_NAME_DEVICE_ADDRESS = "address";
    public static final String NODE_NAME_DEVICE_TYPE = "type";
    public static final String NODE_NAME_DEVICE_PARENT = "parent";
    public static final String NODE_NAME_DEVICE_PROPERTIES = "property";
    public static final String NODE_NAME_DEVICE_PROPERTIES_STATUS_ATTRIBUTE = "formatted";
    public static final String ISY_URL_FORMAT = "http://%s/rest/nodes";
    public static final String ISY_URL_COMMAND_FORMAT = "http://%s/rest/nodes/%s/cmd/%s";
    public static final String ISY_URL_AUTHORIZATION_HEADER = "Authorization";
    public static final String ISY_URL_AUTHORIZATION_HEADER_BASIC = "Basic";

    private ISYController(Context context) {
        mAppContext = context;
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mAppContext);

        mIsyAddress = sharedPref.getString(ISYController.KEY_PREF_ISY_ADDRESS, "10.0.1.19");
        mIsyUserName = sharedPref.getString(ISYController.KEY_PREF_ISY_USERNAME, "admin");
        mIsyPassword = sharedPref.getString(ISYController.KEY_PREF_ISY_PASSWORD, "ErgoFlat91");

        mDeviceMap = new HashMap<String, ISYNode>();
    }

    private static ISYController sIsyController;
    private Context mAppContext;
    private String mIsyAddress;
    private String mIsyUserName;
    private String mIsyPassword;

    // Maps from device address to device data
    private HashMap<String,ISYNode> mDeviceMap;
    private ISYNode[] mDeviceList;

    public static ISYController get(Context context) {
        if(sIsyController == null) {
            sIsyController = new ISYController(context);
        }
        return sIsyController;
    }

    public HashMap<String,ISYNode> getNodeMap() {
        return this.mDeviceMap;
    }

    public void refreshNodeMap() throws MalformedURLException, IOException, XmlPullParserException {
        this.getNodeList();
    }

    private void getNodeList() throws MalformedURLException, IOException, XmlPullParserException {

        URL nodeURL = new URL(String.format(ISYController.ISY_URL_FORMAT, mIsyAddress));
        String userPassword = mIsyUserName + ":" + mIsyPassword;
        String basicAuth = ISY_URL_AUTHORIZATION_HEADER_BASIC + " " +
                           new String(Base64.encode(userPassword.getBytes(), 0));

        HttpURLConnection connection = null;

        try {
            connection = (HttpURLConnection) nodeURL.openConnection();
            connection.setRequestProperty(ISYController.ISY_URL_AUTHORIZATION_HEADER, basicAuth);

            InputStream in = connection.getInputStream();
            BufferedReader rd  = new BufferedReader(new InputStreamReader(in));
            XmlPullParser resultParser = Xml.newPullParser();
            resultParser.setInput(rd);

            parseAndBuildNodeList(resultParser);
        } finally {
            if(connection != null) {
                connection.disconnect();
            }
        }
    }


    public void executeCommand(ISYNode node, String command)  throws MalformedURLException, IOException, XmlPullParserException {

        URL nodeURL = new URL(String.format(ISYController.ISY_URL_COMMAND_FORMAT, mIsyAddress, node.mNodeAddress.replace(" ", "%20"), command ));
        String userPassword = mIsyUserName + ":" + mIsyPassword;
        String basicAuth = ISY_URL_AUTHORIZATION_HEADER_BASIC + " " +
                new String(Base64.encode(userPassword.getBytes(), 0));

        HttpURLConnection connection = null;

        try {
            connection = (HttpURLConnection) nodeURL.openConnection();
            connection.setRequestProperty(ISYController.ISY_URL_AUTHORIZATION_HEADER, basicAuth);

            InputStream in = connection.getInputStream();

        } finally {
            if(connection != null) {
                connection.disconnect();
            }
        }
    }

    private void parseDevice(XmlPullParser xmlPullParser) throws IOException, XmlPullParserException {
        ISYDevice newDevice = new ISYDevice();
        newDevice.setNodeType(ISYNode.ISYNodeType.DEVICE);
        int eventType = xmlPullParser.getEventType();
        while (true) {
            if (eventType == XmlPullParser.END_TAG) {
                if (xmlPullParser.getName().equals(ISYController.NODE_NAME_DEVICE_ROOT))
                    break;
            } else if (eventType == XmlPullParser.START_TAG) {
                if (xmlPullParser.getName().equals(ISYController.NODE_NAME_DEVICE_NAME)) {
                    xmlPullParser.next();
                    newDevice.setNodeName(xmlPullParser.getText());
                } else if (xmlPullParser.getName().equals(ISYController.NODE_NAME_DEVICE_ADDRESS)) {
                    xmlPullParser.next();
                    newDevice.setNodeAddress(xmlPullParser.getText());
                } else if (xmlPullParser.getName().equals(ISYController.NODE_NAME_DEVICE_TYPE)) {
                    xmlPullParser.next();
                    newDevice.setDeviceTypeInternal(xmlPullParser.getText());
                } else if (xmlPullParser.getName().equals(ISYController.NODE_NAME_DEVICE_PARENT)) {
                    xmlPullParser.next();
                    ISYNode parent = this.mDeviceMap.get(xmlPullParser.getText());
                    parent.addChildNode(newDevice);
                } else if (xmlPullParser.getName().equals(ISYController.NODE_NAME_DEVICE_PROPERTIES)) {
                    String status = xmlPullParser.getAttributeValue(
                            null,ISYController.NODE_NAME_DEVICE_PROPERTIES_STATUS_ATTRIBUTE);
                    newDevice.setStatus(status);
                }
            }
            xmlPullParser.next();
            eventType = xmlPullParser.getEventType();
        }
        if(this.mDeviceMap.containsKey(newDevice.getNodeAddress())) {
            this.mDeviceMap.get(newDevice.getNodeAddress()).copyFrom(newDevice);
        } else {
            this.mDeviceMap.put(newDevice.getNodeAddress(), newDevice);
        }
    }

    private void parseFolder(XmlPullParser xmlPullParser) throws IOException, XmlPullParserException {
        ISYNode newFolder = new ISYNode();
        newFolder.setNodeType(ISYNode.ISYNodeType.FOLDER);
        int eventType = xmlPullParser.getEventType();
        while(true) {
            if(eventType == XmlPullParser.END_TAG) {
                if(xmlPullParser.getName().equals(ISYController.NODE_NAME_FOLDER_ROOT))
                    break;
            } else if(eventType == XmlPullParser.START_TAG) {
                if(xmlPullParser.getName().equals(ISYController.NODE_NAME_FOLDER_NAME)) {
                    xmlPullParser.next();
                    newFolder.setNodeName(xmlPullParser.getText());
                } else if(xmlPullParser.getName().equals(ISYController.NODE_NAME_FOLDER_ADDRESS)) {
                    xmlPullParser.next();
                    newFolder.setNodeAddress(xmlPullParser.getText());
                }
            }
            xmlPullParser.next();
            eventType = xmlPullParser.getEventType();
        }
        if(this.mDeviceMap.containsKey(newFolder.getNodeAddress())) {
            this.mDeviceMap.get(newFolder.getNodeAddress()).copyFrom(newFolder);
        } else {
            this.mDeviceMap.put(newFolder.getNodeAddress(), newFolder);
        }
    }

    private void parseAndBuildNodeList(XmlPullParser xmlPullParser) throws IOException, XmlPullParserException {
        int eventType = xmlPullParser.getEventType();
        ISYNode currentNode = null;
        while(eventType != XmlPullParser.END_DOCUMENT) {
            if(eventType == XmlPullParser.START_TAG) {
                if(xmlPullParser.getName().equals(ISYController.NODE_NAME_FOLDER_ROOT)) {
                    parseFolder(xmlPullParser);
                } else if(xmlPullParser.getName().equals(ISYController.NODE_NAME_DEVICE_ROOT)) {
                    parseDevice(xmlPullParser);
                }
            }
            xmlPullParser.next();
            eventType = xmlPullParser.getEventType();
        }
    }


}
