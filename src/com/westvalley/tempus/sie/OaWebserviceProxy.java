package com.westvalley.tempus.sie;

public class OaWebserviceProxy implements com.westvalley.tempus.sie.OaWebservice {
  private String _endpoint = null;
  private com.westvalley.tempus.sie.OaWebservice oaWebservice = null;
  
  public OaWebserviceProxy() {
    _initOaWebserviceProxy();
  }
  
  public OaWebserviceProxy(String endpoint) {
    _endpoint = endpoint;
    _initOaWebserviceProxy();
  }
  
  private void _initOaWebserviceProxy() {
    try {
      oaWebservice = (new com.westvalley.tempus.sie.OaServerServiceLocator()).getOaWebservicePort();
      if (oaWebservice != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)oaWebservice)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)oaWebservice)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (oaWebservice != null)
      ((javax.xml.rpc.Stub)oaWebservice)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.westvalley.tempus.sie.OaWebservice getOaWebservice() {
    if (oaWebservice == null)
      _initOaWebserviceProxy();
    return oaWebservice;
  }
  
  public java.lang.String rejectExpenseReimburse(java.lang.String requestId) throws java.rmi.RemoteException{
    if (oaWebservice == null)
      _initOaWebserviceProxy();
    return oaWebservice.rejectExpenseReimburse(requestId);
  }
  
  public java.lang.String acceptExpenseReimburse(java.lang.String requestId, java.math.BigDecimal actualCost) throws java.rmi.RemoteException{
    if (oaWebservice == null)
      _initOaWebserviceProxy();
    return oaWebservice.acceptExpenseReimburse(requestId, actualCost);
  }
  
  public java.lang.String rejectContractApplication(java.lang.String requestId) throws java.rmi.RemoteException{
    if (oaWebservice == null)
      _initOaWebserviceProxy();
    return oaWebservice.rejectContractApplication(requestId);
  }
  
  public java.lang.String acceptContractPayment(java.lang.String requestId) throws java.rmi.RemoteException{
    if (oaWebservice == null)
      _initOaWebserviceProxy();
    return oaWebservice.acceptContractPayment(requestId);
  }
  
  public java.lang.String acceptContractApplication(java.lang.String requestId, java.lang.Integer contractId, java.lang.String contractNo) throws java.rmi.RemoteException{
    if (oaWebservice == null)
      _initOaWebserviceProxy();
    return oaWebservice.acceptContractApplication(requestId, contractId, contractNo);
  }
  
  public java.lang.String rejectContractPayment(java.lang.String requestId) throws java.rmi.RemoteException{
    if (oaWebservice == null)
      _initOaWebserviceProxy();
    return oaWebservice.rejectContractPayment(requestId);
  }
  
  
}