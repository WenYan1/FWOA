/**
 * OaServerService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.westvalley.tempus.sie;

public interface OaServerService extends javax.xml.rpc.Service {
    public java.lang.String getOaWebservicePortAddress();

    public com.westvalley.tempus.sie.OaWebservice getOaWebservicePort() throws javax.xml.rpc.ServiceException;

    public com.westvalley.tempus.sie.OaWebservice getOaWebservicePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
