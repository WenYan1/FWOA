/**
 * OaServerServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.westvalley.tempus.sie;

public class OaServerServiceLocator extends org.apache.axis.client.Service implements com.westvalley.tempus.sie.OaServerService {

    public OaServerServiceLocator() {
    }


    public OaServerServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public OaServerServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for OaWebservicePort
    private java.lang.String OaWebservicePort_address = "http://172.18.240.103:2211/services/OaWebservice";

    public java.lang.String getOaWebservicePortAddress() {
        return OaWebservicePort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String OaWebservicePortWSDDServiceName = "OaWebservicePort";

    public java.lang.String getOaWebservicePortWSDDServiceName() {
        return OaWebservicePortWSDDServiceName;
    }

    public void setOaWebservicePortWSDDServiceName(java.lang.String name) {
        OaWebservicePortWSDDServiceName = name;
    }

    public com.westvalley.tempus.sie.OaWebservice getOaWebservicePort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(OaWebservicePort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getOaWebservicePort(endpoint);
    }

    public com.westvalley.tempus.sie.OaWebservice getOaWebservicePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.westvalley.tempus.sie.OaServerServiceSoapBindingStub _stub = new com.westvalley.tempus.sie.OaServerServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getOaWebservicePortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setOaWebservicePortEndpointAddress(java.lang.String address) {
        OaWebservicePort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.westvalley.tempus.sie.OaWebservice.class.isAssignableFrom(serviceEndpointInterface)) {
                com.westvalley.tempus.sie.OaServerServiceSoapBindingStub _stub = new com.westvalley.tempus.sie.OaServerServiceSoapBindingStub(new java.net.URL(OaWebservicePort_address), this);
                _stub.setPortName(getOaWebservicePortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("OaWebservicePort".equals(inputPortName)) {
            return getOaWebservicePort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://oa.saaf.sie.com", "OaServerService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://oa.saaf.sie.com", "OaWebservicePort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("OaWebservicePort".equals(portName)) {
            setOaWebservicePortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
