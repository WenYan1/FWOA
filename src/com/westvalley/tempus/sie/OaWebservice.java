/**
 * OaWebservice.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.westvalley.tempus.sie;

public interface OaWebservice extends java.rmi.Remote {
    public java.lang.String rejectExpenseReimburse(java.lang.String requestId) throws java.rmi.RemoteException;
    public java.lang.String acceptExpenseReimburse(java.lang.String requestId, java.math.BigDecimal actualCost) throws java.rmi.RemoteException;
    public java.lang.String rejectContractApplication(java.lang.String requestId) throws java.rmi.RemoteException;
    public java.lang.String acceptContractPayment(java.lang.String requestId) throws java.rmi.RemoteException;
    public java.lang.String acceptContractApplication(java.lang.String requestId, java.lang.Integer contractId, java.lang.String contractNo) throws java.rmi.RemoteException;
    public java.lang.String rejectContractPayment(java.lang.String requestId) throws java.rmi.RemoteException;
}
