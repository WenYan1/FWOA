package com.westvalley.tempus.sie;


import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Map;

import com.westvalley.tempus.workflow.PRSave.Keys;

import crivia.eca.Cat;
import crivia.eca.am2.common.AM2;
import crivia.txt.common.Logger;
import weaver.general.Util;
import weaver.interfaces.workflow.action.BaseAction;
import weaver.soa.workflow.request.Property;
import weaver.soa.workflow.request.RequestInfo;

//价值链无事前报销审批回写
public class AcceptReimburse extends BaseAction {
	//判断是否是提交或者退回 is  然后提示ALT+斜杠
	
	
					
	public String  execute(RequestInfo request){
	
			Property[]  properties = request.getMainTableInfo().getProperty();// 获取表单主字段信息
			String requestId = request.getRequestid();
			String tb_Total_Amount="";
			String tb_ims_status="";
			for (int i = 0; i <properties.length; i++) {
				Logger.log("properties[i].getName() > "+properties[i].getName());
				Logger.log("properties[i].getName() > "+properties[i].getValue());
				if("tb_Total_Amount".equalsIgnoreCase(properties[i].getName())) {
					tb_Total_Amount=properties[i].getValue();
				}
				if("tb_ims_status".equalsIgnoreCase(properties[i].getName())) {
					tb_ims_status=properties[i].getValue();
				}
			}
			
		
		return SUCCESS;
	}
	
}
