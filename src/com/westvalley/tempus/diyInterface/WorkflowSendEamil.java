package com.westvalley.tempus.diyInterface;



import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import crivia.db.common.SQL;
import crivia.ecp.carry.ec.hrm.HR;
import crivia.ecp.common.EcologyDB;
import crivia.txt.common.Logger;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.Property;
import weaver.soa.workflow.request.RequestInfo;

public class WorkflowSendEamil implements Action {

	@Override
	public String execute(RequestInfo request) {
		try {
		String requestId = request.getRequestid();
		//String tablename = request.getRequestManager().getBillTableName();// 表单主表名称
		Property[]  properties = request.getMainTableInfo().getProperty();// 获取表单主字段信息
		Logger.log("邮件开始 > "+requestId);	
		String URL=new weaver.general.BaseBean().getPropValue("IMSURL","URL");
		Logger.log("URL > "+URL);	
		String tb_applicant = "";
		String requestname = request.getRequestManager().getRequestname();// 请求标题
		for (int i = 0; i <properties.length; i++) {
			Logger.log("properties[i].getName() > "+properties[i].getName());
			Logger.log("properties[i].getName() > "+properties[i].getValue());
			if("tb_applicant".equalsIgnoreCase(properties[i].getName())) {
				tb_applicant=properties[i].getValue();
			}
		}
		HtmlEmail email = new HtmlEmail();
		email.setCharset("UTF-8");
		email.setHostName("email.tempus.cn");
		email.setAuthentication("TempusHoldITAlert@tempus.cn", "tempus.oa");
		Logger.log("邮件 > "+email);	
		HR user = SQL.one(EcologyDB.db(), HR.class, true
				,SQL.cd.equals(HR.pk(),tb_applicant));
		Logger.log("用户信息 > "+user);
		String mailFrom = "TempusHoldITAlert@tempus.cn";
		String mailAddress = user.getEmail().toString();
		String mailSubject = "流程办结提醒";   //邮件标题
	    String mailSetHtmlMsg = "<html><div>Dear "+user.getLastName()+
				",</div><div style='text-indent:2em;'>您的流程：<span style='font-weight: bold;'>"
				+requestname+"</span>已办结</div><a href='"+URL+"/workflow/request/ManageRequestNoForm.jsp?viewdoc=&fromFlowDoc=&f_weaver_belongto_userid="+user.getId()+"&f_weaver_belongto_usertype=0&uselessFlag=&requestid="+requestId+"&isrequest=0&isovertime=0&isaffirmance=&reEdit=1&seeflowdoc=0&isworkflowdoc=0&isfromtab=false&isSubmitDirect='>查看表单</a></html>";//邮件内容
		String userName = user.getLastName(); //用户
		email.setFrom(mailFrom);
		email.addTo( mailAddress,userName);
		email.setSubject(mailSubject);
		email.setHtmlMsg(mailSetHtmlMsg);
		Logger.log("邮件发送 > "+email);	
		email.send();
		} catch (EmailException e) {
		}
		//SendRemindMail(mailFrom,mailAddress,mailSubject,mailSetHtmlMsg,userName);
		return null;
	}
	//  private void SendRemindMail(String mailFrom,String mailAddress,String mailSubject,String mailSetHtmlMsg, String userName){
	  //  	new Thread(new WorkflowSendMailRunnable(mailFrom,mailAddress,mailSubject,mailSetHtmlMsg,userName)).start();
	//    }

}
