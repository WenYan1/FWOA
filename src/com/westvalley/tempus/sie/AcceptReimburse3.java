package com.westvalley.tempus.sie;


import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Map;

import org.json.JSONObject;

import com.westvalley.tempus.workflow.PRSave.Keys;

import crivia.db.common.SQL;
import crivia.eca.Cat;
import crivia.eca.am2.common.AM2;
import crivia.ecp.common.EcologyDB;
import crivia.txt.common.Logger;
import crivia.txt.sc.RK;
import weaver.general.Util;
import weaver.interfaces.workflow.action.BaseAction;
import weaver.soa.workflow.request.Property;
import weaver.soa.workflow.request.RequestInfo;

//价值链礼品及业务招待报销审批回写   后面讲tb_amount改为tb_Actual_reimbursement_amount 
public class AcceptReimburse3 extends BaseAction {
	//判断是否是提交或者退回 is  然后提示ALT+斜杠
	
	
					
	public String  execute(RequestInfo request){
		try {
			Property[]  properties = request.getMainTableInfo().getProperty();// 获取表单主字段信息
			String requestId = request.getRequestid();
			String tb_amount="";
			String tb_ims_status="";
			for (int i = 0; i <properties.length; i++) {
				Logger.log("properties[i].getName() > "+properties[i].getName());
				Logger.log("properties[i].getName() > "+properties[i].getValue());
				if("tb_amount".equalsIgnoreCase(properties[i].getName())) {
					tb_amount=properties[i].getValue();
				}
				if("tb_ims_status".equalsIgnoreCase(properties[i].getName())) {
					tb_ims_status=properties[i].getValue();
				}
			}
		if("Y".equals(tb_ims_status)) {
			Logger.log("tb_amount > "+tb_amount);
			BigDecimal b= new BigDecimal(tb_amount);
			OaWebserviceProxy proxy = new OaWebserviceProxy(new weaver.general.BaseBean().getPropValue("IMSURL","IMS_URL"));//读取/home/weaver/ecology/WEB-INF/prop配置文件
			Logger.log("proxy > "+proxy);
			String result = proxy.acceptExpenseReimburse(requestId,b);
			System.out.println(result);
			Logger.log("result > "+result);
			JSONObject jo=new JSONObject(result);
			if("E".equals(jo.getString("STATUS"))) {
				Logger.log("审批回写失败 > "+requestId);
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
			Logger.log("-----------------------");
			return "faild";
		}
		return SUCCESS;
	}
	
}
