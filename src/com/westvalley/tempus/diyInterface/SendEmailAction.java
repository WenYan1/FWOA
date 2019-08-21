package com.westvalley.tempus.diyInterface;


import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;


import crivia.db.common.SQL;
import crivia.db.i.CMD;

import crivia.ecp.carry.ec.hrm.HR;
import crivia.ecp.common.EcologyDB;
import crivia.txt.common.Logger;
import weaver.general.SendMail;
import weaver.interfaces.workflow.action.BaseAction;
import weaver.soa.workflow.request.Property;
import weaver.soa.workflow.request.RequestInfo;

//ims的流程驳回不能再OA中再次提起
public class SendEmailAction extends BaseAction {
	//判断是否是提交或者退回 is  然后提示ALT+斜杠

	public String  execute(RequestInfo request) {
		Logger.log("发送邮件开始 > "+request);
		String requestId = request.getRequestid();
		 SendMail sm = new SendMail();
			Logger.log("邮件 > "+sm);	
			sm.setMailServer("email.tempus.cn");
			sm.setNeedauthsend(true);
            sm.setUsername("TempusHoldITAlert@tempus.cn");
            sm.setPassword("tempus.oa");
			try {
				CMD defaultCMD = EcologyDB.db();
				String tablename = request.getRequestManager().getBillTableName();// 表单主表名称
				Property[]  properties = request.getMainTableInfo().getProperty();// 获取表单主字段信息
				
				String URL=new weaver.general.BaseBean().getPropValue("IMSURL","URL");
				Logger.log("URL > "+URL);	
				String tb_applicant = "";
				String requestname = request.getRequestManager().getRequestname();// 请求标题
				for (int i = 0; i <properties.length; i++) {
					if("tb_applicant".equalsIgnoreCase(properties[i].getName())) {
						tb_applicant=properties[i].getValue();
					}	
				}
				HR user = SQL.one(EcologyDB.db(), HR.class, true
						,SQL.cd.equals(HR.pk(),tb_applicant));
				
				Logger.log("用户信息 > "+user);
				boolean send =false;
				send = sm.sendMiltipartHtml("TempusHoldITAlert@tempus.cn",user.getEmail().toString() , null, null, "流程办结提醒","<html><div>Dear "+user.getLastName()+
						",</div><div style='text-indent:2em;'>您的流程：<span style='font-weight: bold;'>"
						+requestname+"</span>已办结</div><a href='"+URL+"/workflow/request/ManageRequestNoForm.jsp?viewdoc=&fromFlowDoc=&f_weaver_belongto_userid="+user.getId()+"&f_weaver_belongto_usertype=0&uselessFlag=&requestid="+requestId+"&isrequest=0&isovertime=0&isaffirmance=&reEdit=1&seeflowdoc=0&isworkflowdoc=0&isfromtab=false&isSubmitDirect='>查看表单</a></html>" , 3, null,null, "3");
				Logger.log("邮件结束 > "+send);	
			} catch (Exception e) {
				
				Logger.log("邮件发送失败 > "+requestId);
				request.getRequestManager().setMessageid("126221");//126221
				request.getRequestManager().setMessagecontent("请联系管理员");	
				return "faild";
			}
		
				
		return SUCCESS;
	}


	

}
