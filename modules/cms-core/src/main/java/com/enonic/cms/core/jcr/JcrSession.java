package com.enonic.cms.core.jcr;

import java.util.Calendar;
import java.util.Date;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.Session;

public interface JcrSession
{
    Session getRealSession();

    JcrRepository getRepository();

    void login();

    void logout();

    void save();

    Node getRootNode();

    Node getNode(String absPath);

    Node getOrCreateNode(String absPath);

    boolean nodeExists(String absPath);

    void removeItem(String absPath);

    boolean propertyExists(String absPath);

    Property getProperty(String absPath);


    String getPropertyString(String absPath);

    boolean getPropertyBoolean(String absPath);

    long getPropertyLong(String absPath);

    double getPropertyDouble(String absPath);

    Date getPropertyDate(String absPath);

    Calendar getPropertyCalendar(String absPath);


    void setPropertyString(String absPath, String value);

    void setPropertyBoolean(String absPath, String value);

    void setPropertyLong(String absPath, long value);

    void setPropertyDouble(String absPath, double value);

    void setPropertyDate(String absPath, Date value);

    void setPropertyCalendar(String absPath, Calendar value);

}
