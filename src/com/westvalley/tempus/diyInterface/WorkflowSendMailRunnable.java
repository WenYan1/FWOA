package com.westvalley.tempus.diyInterface;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

public class WorkflowSendMailRunnable implements Runnable {
	
	private String mailFrom = ""; //邮件服务器
	private String mailAddress = ""; //邮件地址
	private String mailSubject="";   //邮件标题
	private String mailSetHtmlMsg = "";//邮件内容
	private String userName = ""; //用户

	
	public WorkflowSendMailRunnable(String mailFrom,String mailAddress,String mailSubject,String mailSetHtmlMsg, String userName) {
		this.mailFrom = mailFrom;
		this.mailAddress = mailAddress;
		this.mailSubject = mailSubject;
		this.mailSetHtmlMsg = mailSetHtmlMsg;
		this.userName = userName;
	
	}
	@Override
	public void run() {
		try {
			HtmlEmail email = new HtmlEmail();
			email.setCharset("UTF-8");
			email.setHostName("email.tempus.cn");
			email.setAuthentication("TempusHoldITAlert@tempus.cn", "tempus.oa");
			email.setFrom(mailFrom);
			email.addTo( mailAddress,userName);
			email.setSubject(mailSubject);
			email.setHtmlMsg(mailSetHtmlMsg);
			email.send();
		} catch (EmailException e) {
		}
		
	}

}
