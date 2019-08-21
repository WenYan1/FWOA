package com.westvalley.tempus.sie;


import java.rmi.RemoteException;
import java.util.Map;

import com.westvalley.tempus.diyInterface.FileType.t0;

import crivia.db.common.SQL;
import crivia.db.i.CMD;
import crivia.eca.Cat;
import crivia.ecp.common.EcologyDB;
import crivia.txt.common.Logger;
import weaver.interfaces.workflow.action.BaseAction;
import weaver.soa.workflow.request.Property;
import weaver.soa.workflow.request.RequestInfo;

//ims的流程驳回不能再OA中再次提起
public class ReturnErrorAction extends BaseAction {
	//判断是否是提交或者退回 is  然后提示ALT+斜杠
	
	
					
	
	public String  execute(RequestInfo request) {
		
		
			CMD defaultCMD = EcologyDB.db();
			String tablename = request.getRequestManager().getBillTableName();// 表单主表名称
			Property[]  properties = request.getMainTableInfo().getProperty();// 获取表单主字段信息
			String requestId = request.getRequestid();
			
			//String requestid=mainValue(requestID);
			Logger.log("requestid > "+requestId);
			String tb_ims_status = "";
			String tb_ims_return_status = "";
			for (int i = 0; i <properties.length; i++) {
				if("tb_ims_status".equalsIgnoreCase(properties[i].getName())) {
					tb_ims_status=properties[i].getValue();
				}
				
			}
				if("N".equals(tb_ims_status)) {//N代表是从业务系统中提交被驳回的单据，如果是从OA再次提交就不会变，因为业务系统会再次传Y覆盖N
					Logger.log("请在业务系统中进行再次提交 > "+requestId);
					request.getRequestManager().setMessageid("126221");//126221
					request.getRequestManager().setMessagecontent("请在业务系统中进行再次提交");		
			}
			
		return SUCCESS;
	}


	

}
