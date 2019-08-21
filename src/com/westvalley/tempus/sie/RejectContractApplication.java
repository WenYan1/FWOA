package com.westvalley.tempus.sie;


import java.rmi.RemoteException;
import java.util.Map;

import org.json.JSONObject;

import crivia.db.common.SQL;
import crivia.db.i.CMD;
import crivia.eca.Cat;
import crivia.ecp.common.EcologyDB;
import crivia.txt.common.Logger;
import crivia.txt.sc.RK;
import weaver.interfaces.workflow.action.BaseAction;
import weaver.soa.workflow.request.Property;
import weaver.soa.workflow.request.RequestInfo;
import weaver.general.BaseBean;

//合同申请驳回
public class RejectContractApplication extends BaseAction {
	//判断是否是提交或者退回 is  然后提示ALT+斜杠
	
	
					
	
	public String  execute(RequestInfo request) {
		
		try {
			CMD defaultCMD = EcologyDB.db();
			String tablename = request.getRequestManager().getBillTableName();// 表单主表名称
			Property[]  properties = request.getMainTableInfo().getProperty();// 获取表单主字段信息
			String requestId = request.getRequestid();
			//String requestid=mainValue(requestID);
			Logger.log("requestid >b "+requestId);
			String tb_ims_status="";
			for (int i = 0; i <properties.length; i++) {
				if("tb_ims_status".equalsIgnoreCase(properties[i].getName())) {
					tb_ims_status=properties[i].getValue();
				}
			}
			if("Y".equals(tb_ims_status)) {
				SQL.edit(SQL.sql.array("update "+tablename+"  set tb_ims_status='N' where requestID = ?",requestId),defaultCMD);
				OaWebserviceProxy proxy = new OaWebserviceProxy(new weaver.general.BaseBean().getPropValue("IMSURL","IMS_URL"));//读取/home/weaver/ecology/WEB-INF/prop配置文件
				String a =new weaver.general.BaseBean().getPropValue("IMSURL","IMS_URL");
				System.out.println(proxy);
				Logger.log("a > "+a);
				String result = proxy.rejectContractApplication(requestId);
				System.out.println(result);
				Logger.log("result > "+result);
				JSONObject jo=new JSONObject(result);
				if("E".equals(jo.getString("STATUS"))) {
					Logger.log("合同申请驳回失败 > "+requestId);
					SQL.edit(SQL.sql.array("insert into TB_IMS_ERROR values (?,?,?,?,sysdate)"
							, RK.rk() , jo.getString("MSG"),jo.getString("STATUS"),requestId),EcologyDB.db());
					request.getRequestManager().setMessageid("126221");//126221
					request.getRequestManager().setMessagecontent(jo.getString("赛意接口返回错误"+"MSG")+"请联系管理员");	
					return "faild";
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}


	

}
