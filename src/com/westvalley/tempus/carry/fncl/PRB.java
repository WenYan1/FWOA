package com.westvalley.tempus.carry.fncl;

import java.util.List;

import com.westvalley.tempus.carry.fncl.PR.PayLine;

import crivia.c.a.KeyLink;
import crivia.c.carry.RelationKey;
import crivia.c.carry.ValueField;
import crivia.ecp.ECPExecuter;
import crivia.ecp.a.ECC;
import crivia.ecp.carry.ec.hrm.HR;
import crivia.ecp.carry.html.form.Select;

/**
 * 付款批次
 */
public class PRB extends ECC {
	
	public static void main(String[] args) {
		refreshDBIandFields(PRB.class);
//		EnumCreater.p("CreateStatus");
//		System.out.println("drop table "+PRB.table()+";");
		printTableCreater4Oracle(PRB.class);
	}
	
	
	
	/**付款方式，目前有<b>线上</b>和<b>线下</b>两种方式*/
	public enum PayLine {
		OnLine("资金管理平台"),UnderLine("线下")
		;
		PayLine(String t){this.t = t;}
		public final String t;
		public static PayLine k2e(String k){
			try {
				return valueOf(k);
			} catch (Exception e) {
				return OnLine;
			}
		}
		public static String t2k(String t){
			for (PayLine a : PayLine.values()) if(a.t.equals(t))return a.toString();
			return k2e(t).toString();
		} 

		public static Select select(ECPExecuter p,Object k){
			Select select = new Select(p,""+k);
			selectOption(select);
			return select;
		}
		public static Select select(String k,Object v){
			Select select = new Select(k, ""+v, null);
			selectOption(select);
			return select;
		}
		private static void selectOption(Select select){
			for (PayLine c : PayLine.values()){
				select.addOption(""+c, c.t);
			}
		}
		
		public static String caseSQL(String f){
			StringBuilder s = new StringBuilder("(case ").append(f);
			for (PayLine a : PayLine.values()){
				s
					.append(" when '").append(a)
					.append("' then '").append(a.t).append("'");
			}
			return s.append(" else '' end)").toString();
		}
	}

	/**凭证生成状态*/
//		Ready("准备中")
//		,Success("成功")
//		,Fail("失败")
//		,Edited("已修改")

	
	


	@Override
	public String tableName() {
		return "WV_FNCL_PRB";
	}

	@Override
	public void keyLinks(List<KeyLink> keyLinks) {
		keyLinks.add(ValueField.c2d("payKey").setTipText("资金管理平台业务参考号"));
		keyLinks.add(ValueField.c2d("payLine").setTipText("支付方式"));
		keyLinks.add(ValueField.c2d("payDay").setTipText("支付日期"));
		keyLinks.add(ValueField.c2d("createDay").setTipText("生成付款单日期"));
		keyLinks.add(ValueField.c2d("createTime").setTipText("生成付款单时间"));
		keyLinks.add(ValueField.c2d("payTime").setTipText("支付时间"));
//		keyLinks.add(ValueField.c2d("payStatus").setTipText("支付状态"));
		keyLinks.add(ValueField.c2d("payOperator").setTipText("支付操作人"));
		keyLinks.add(ValueField.c2d("isManual").setTipText("是否手动做账"));
		keyLinks.add(RelationKey.c2d("payOperatorName","payOperator"
				,HR.table(),HR.Keys.lastName,HR.pk()).setTipText("支付操作人-姓名"));
		keyLinks.add(ValueField.c2d("vcr2Status").setTipText("凭证2-执行状态"));
		keyLinks.add(ValueField.c2d("moneys",double.class).setTipText("总金额"));
		keyLinks.add(ValueField.c2d("prbGLDay").setTipText("相关oracle凭证数据-GL日期"));
		keyLinks.add(ValueField.c2d("prbEACompany").setTipText("相关oracle凭证数据-费用报销公司"));
		keyLinks.add(RelationKey.c2d("prbEACompanyName","prbEACompany"
				,EACpy.table()
				,EACpy.Keys.name
				,""+EACpy.Keys.code
		).setTipText("报销公司"));
		keyLinks.add(ValueField.c2d("prbrecBankName").setTipText("收款银行名称"));
		keyLinks.add(ValueField.c2d("prbrecBankAccountNumber").setTipText("收款银行账号"));
		keyLinks.add(ValueField.c2d("prbrecBankAccountName").setTipText("收款银行账户名"));
		keyLinks.add(ValueField.c2d("prbPayMentAccount").setTipText("相关oracle凭证数据-付款银行账号"));
		keyLinks.add(RelationKey.c2d("prbPayMentAccountName","prbpaymentaccount"
				,EAPaybankAccount.table()
				,EAPaybankAccount.Keys.paybankacount
				,""+EAPaybankAccount.Keys.paybankid
		).setTipText("相关oracle凭证数据-付款银行账号"));
		keyLinks.add(ValueField.c2d("creator").setTipText("申请人"));
		keyLinks.add(RelationKey.c2d("creatorName","creator"
				,HR.table(),HR.Keys.lastName,HR.pk())
			.setRelationPKFormat("to_char(?)")
			.setTipText("申请人-姓名"));
		keyLinks.add(ValueField.c2d("application").setTipText("用途"));
	}


	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//XXX DBI Begin.
	public enum Keys{
		/** <b>资金管理平台业务参考号</b> */payKey("payKey","payKey")
		,/** <b>支付方式</b> */payLine("payLine","payLine")
		,/** <b>支付日期</b> */payDay("payDay","payDay")
		,/** <b>生成付款单日期</b> */createDay("createDay","createDay")
		,/** <b>生成付款单时间</b> */createTime("createTime","createTime")
		,/** <b>支付时间</b> */payTime("payTime","payTime")
		,/** <b>支付操作人</b> */payOperator("payOperator","payOperator")
		,/** <b>是否手动做账</b> */isManual("isManual","isManual")
		,/** <b>支付操作人-姓名</b> */payOperatorName("payOperatorName","lastName")
		,/** <b>凭证2-执行状态</b> */vcr2Status("vcr2Status","vcr2Status")
		,/** <b>总金额</b> */moneys("moneys","moneys")
		,/** <b>相关oracle凭证数据-GL日期</b> */prbGLDay("prbGLDay","prbGLDay")
		,/** <b>相关oracle凭证数据-费用报销公司</b> */prbEACompany("prbEACompany","prbEACompany")
		,/** <b>报销公司</b> */prbEACompanyName("prbEACompanyName","name")
		,/** <b>收款银行名称</b> */prbrecBankName("prbrecBankName","prbrecBankName")
		,/** <b>收款银行账号</b> */prbrecBankAccountNumber("prbrecBankAccountNumber","prbrecBankAccountNumber")
		,/** <b>收款银行账户名</b> */prbrecBankAccountName("prbrecBankAccountName","prbrecBankAccountName")
		,/** <b>相关oracle凭证数据-付款银行账号</b> */prbPayMentAccount("prbPayMentAccount","prbPayMentAccount")
		,/** <b>相关oracle凭证数据-付款银行账号</b> */prbPayMentAccountName("prbPayMentAccountName","paybankacount")
		,/** <b>申请人</b> */creator("creator","creator")
		,/** <b>申请人-姓名</b> */creatorName("creatorName","lastName")
		,/** <b>用途</b> */application("application","application")
		;
		private String _dbKey;
		private String _key;
		Keys(String key,String dbKey){_key=key;_dbKey=dbKey;}
		@Override
		public String toString(){return _dbKey;}
		/**  <b>Key</b> in <b>Carry</b>. */
		public String key(){return _key;}
	}
	private static com.westvalley.tempus.carry.fncl.PRB dbi;
	public synchronized static void initDBI(){if(null!=dbi)return;dbi=new com.westvalley.tempus.carry.fncl.PRB();}
	/**  <b>DB Table</b> of <b>Carry</b>. */
	public static String table(){initDBI();return dbi.tableName();}
	/**  <b>Primary Key</b> of <b>Carry</b>. */
	public static String pk(){initDBI();return dbi.pk4db();}
	/**  <b>Relation SQL</b> of <b>Relation Key</b>. */
	public static String rSQL(com.westvalley.tempus.carry.fncl.PRB.Keys k,String t){initDBI();return dbi.relationSQL(k.key(),t);}
	//XXX DBI End.



	

	

	

	

	

	

	

	

	


	

	

	

	

	

	// XXX Fields Begin.



	/** ...(Miss Tip Text.) */
	private java.lang.String id;
	/** Getter for ...(Miss Tip Text.) */
	public java.lang.String getId(){
		return id;
	}
	/** Setter for ...(Miss Tip Text.) */
	public com.westvalley.tempus.carry.fncl.PRB setId(java.lang.String id){
		this.id = id;
		return this;
	}


	/** <b>资金管理平台业务参考号</b> */
	private java.lang.String payKey;
	/** Getter for <b>资金管理平台业务参考号</b> */
	public java.lang.String getPayKey(){
		return payKey;
	}
	/** Setter for <b>资金管理平台业务参考号</b> */
	public com.westvalley.tempus.carry.fncl.PRB setPayKey(java.lang.String payKey){
		this.payKey = payKey;
		return this;
	}


	/** <b>支付方式</b> */
	private java.lang.String payLine;
	/** Getter for <b>支付方式</b> */
	public java.lang.String getPayLine(){
		return payLine;
	}
	/** Setter for <b>支付方式</b> */
	public com.westvalley.tempus.carry.fncl.PRB setPayLine(java.lang.String payLine){
		this.payLine = payLine;
		return this;
	}


	/** <b>支付日期</b> */
	private java.lang.String payDay;
	/** Getter for <b>支付日期</b> */
	public java.lang.String getPayDay(){
		return payDay;
	}
	/** Setter for <b>支付日期</b> */
	public com.westvalley.tempus.carry.fncl.PRB setPayDay(java.lang.String payDay){
		this.payDay = payDay;
		return this;
	}


	/** <b>生成付款单日期</b> */
	private java.lang.String createDay;
	/** Getter for <b>生成付款单日期</b> */
	public java.lang.String getCreateDay(){
		return createDay;
	}
	/** Setter for <b>生成付款单日期</b> */
	public com.westvalley.tempus.carry.fncl.PRB setCreateDay(java.lang.String createDay){
		this.createDay = createDay;
		return this;
	}


	/** <b>生成付款单时间</b> */
	private java.lang.String createTime;
	/** Getter for <b>生成付款单时间</b> */
	public java.lang.String getCreateTime(){
		return createTime;
	}
	/** Setter for <b>生成付款单时间</b> */
	public com.westvalley.tempus.carry.fncl.PRB setCreateTime(java.lang.String createTime){
		this.createTime = createTime;
		return this;
	}


	/** <b>支付时间</b> */
	private java.lang.String payTime;
	/** Getter for <b>支付时间</b> */
	public java.lang.String getPayTime(){
		return payTime;
	}
	/** Setter for <b>支付时间</b> */
	public com.westvalley.tempus.carry.fncl.PRB setPayTime(java.lang.String payTime){
		this.payTime = payTime;
		return this;
	}


	/** <b>支付操作人</b> */
	private java.lang.String payOperator;
	/** Getter for <b>支付操作人</b> */
	public java.lang.String getPayOperator(){
		return payOperator;
	}
	/** Setter for <b>支付操作人</b> */
	public com.westvalley.tempus.carry.fncl.PRB setPayOperator(java.lang.String payOperator){
		this.payOperator = payOperator;
		return this;
	}


	/** <b>是否手动做账</b> */
	private java.lang.String isManual;
	/** Getter for <b>是否手动做账</b> */
	public java.lang.String getIsManual(){
		return isManual;
	}
	/** Setter for <b>是否手动做账</b> */
	public com.westvalley.tempus.carry.fncl.PRB setIsManual(java.lang.String isManual){
		this.isManual = isManual;
		return this;
	}


	/** <b>支付操作人-姓名</b> */
	private java.lang.String payOperatorName;
	/** Getter for <b>支付操作人-姓名</b> */
	public java.lang.String getPayOperatorName(){
		return payOperatorName;
	}
	/** Setter for <b>支付操作人-姓名</b> */
	public com.westvalley.tempus.carry.fncl.PRB setPayOperatorName(java.lang.String payOperatorName){
		this.payOperatorName = payOperatorName;
		return this;
	}


	/** <b>凭证2-执行状态</b> */
	private java.lang.String vcr2Status;
	/** Getter for <b>凭证2-执行状态</b> */
	public java.lang.String getVcr2Status(){
		return vcr2Status;
	}
	/** Setter for <b>凭证2-执行状态</b> */
	public com.westvalley.tempus.carry.fncl.PRB setVcr2Status(java.lang.String vcr2Status){
		this.vcr2Status = vcr2Status;
		return this;
	}


	/** <b>总金额</b> */
	private double moneys;
	/** Getter for <b>总金额</b> */
	public double getMoneys(){
		return moneys;
	}
	/** Setter for <b>总金额</b> */
	public com.westvalley.tempus.carry.fncl.PRB setMoneys(double moneys){
		this.moneys = moneys;
		return this;
	}


	/** <b>相关oracle凭证数据-GL日期</b> */
	private java.lang.String prbGLDay;
	/** Getter for <b>相关oracle凭证数据-GL日期</b> */
	public java.lang.String getPrbGLDay(){
		return prbGLDay;
	}
	/** Setter for <b>相关oracle凭证数据-GL日期</b> */
	public com.westvalley.tempus.carry.fncl.PRB setPrbGLDay(java.lang.String prbGLDay){
		this.prbGLDay = prbGLDay;
		return this;
	}


	/** <b>相关oracle凭证数据-费用报销公司</b> */
	private java.lang.String prbEACompany;
	/** Getter for <b>相关oracle凭证数据-费用报销公司</b> */
	public java.lang.String getPrbEACompany(){
		return prbEACompany;
	}
	/** Setter for <b>相关oracle凭证数据-费用报销公司</b> */
	public com.westvalley.tempus.carry.fncl.PRB setPrbEACompany(java.lang.String prbEACompany){
		this.prbEACompany = prbEACompany;
		return this;
	}


	/** <b>报销公司</b> */
	private java.lang.String prbEACompanyName;
	/** Getter for <b>报销公司</b> */
	public java.lang.String getPrbEACompanyName(){
		return prbEACompanyName;
	}
	/** Setter for <b>报销公司</b> */
	public com.westvalley.tempus.carry.fncl.PRB setPrbEACompanyName(java.lang.String prbEACompanyName){
		this.prbEACompanyName = prbEACompanyName;
		return this;
	}


	/** <b>收款银行名称</b> */
	private java.lang.String prbrecBankName;
	/** Getter for <b>收款银行名称</b> */
	public java.lang.String getPrbrecBankName(){
		return prbrecBankName;
	}
	/** Setter for <b>收款银行名称</b> */
	public com.westvalley.tempus.carry.fncl.PRB setPrbrecBankName(java.lang.String prbrecBankName){
		this.prbrecBankName = prbrecBankName;
		return this;
	}


	/** <b>收款银行账号</b> */
	private java.lang.String prbrecBankAccountNumber;
	/** Getter for <b>收款银行账号</b> */
	public java.lang.String getPrbrecBankAccountNumber(){
		return prbrecBankAccountNumber;
	}
	/** Setter for <b>收款银行账号</b> */
	public com.westvalley.tempus.carry.fncl.PRB setPrbrecBankAccountNumber(java.lang.String prbrecBankAccountNumber){
		this.prbrecBankAccountNumber = prbrecBankAccountNumber;
		return this;
	}


	/** <b>收款银行账户名</b> */
	private java.lang.String prbrecBankAccountName;
	/** Getter for <b>收款银行账户名</b> */
	public java.lang.String getPrbrecBankAccountName(){
		return prbrecBankAccountName;
	}
	/** Setter for <b>收款银行账户名</b> */
	public com.westvalley.tempus.carry.fncl.PRB setPrbrecBankAccountName(java.lang.String prbrecBankAccountName){
		this.prbrecBankAccountName = prbrecBankAccountName;
		return this;
	}


	/** <b>相关oracle凭证数据-付款银行账号</b> */
	private java.lang.String prbPayMentAccount;
	/** Getter for <b>相关oracle凭证数据-付款银行账号</b> */
	public java.lang.String getPrbPayMentAccount(){
		return prbPayMentAccount;
	}
	/** Setter for <b>相关oracle凭证数据-付款银行账号</b> */
	public com.westvalley.tempus.carry.fncl.PRB setPrbPayMentAccount(java.lang.String prbPayMentAccount){
		this.prbPayMentAccount = prbPayMentAccount;
		return this;
	}


	/** <b>相关oracle凭证数据-付款银行账号</b> */
	private java.lang.String prbPayMentAccountName;
	/** Getter for <b>相关oracle凭证数据-付款银行账号</b> */
	public java.lang.String getPrbPayMentAccountName(){
		return prbPayMentAccountName;
	}
	/** Setter for <b>相关oracle凭证数据-付款银行账号</b> */
	public com.westvalley.tempus.carry.fncl.PRB setPrbPayMentAccountName(java.lang.String prbPayMentAccountName){
		this.prbPayMentAccountName = prbPayMentAccountName;
		return this;
	}


	/** <b>申请人</b> */
	private java.lang.String creator;
	/** Getter for <b>申请人</b> */
	public java.lang.String getCreator(){
		return creator;
	}
	/** Setter for <b>申请人</b> */
	public com.westvalley.tempus.carry.fncl.PRB setCreator(java.lang.String creator){
		this.creator = creator;
		return this;
	}


	/** <b>申请人-姓名</b> */
	private java.lang.String creatorName;
	/** Getter for <b>申请人-姓名</b> */
	public java.lang.String getCreatorName(){
		return creatorName;
	}
	/** Setter for <b>申请人-姓名</b> */
	public com.westvalley.tempus.carry.fncl.PRB setCreatorName(java.lang.String creatorName){
		this.creatorName = creatorName;
		return this;
	}


	/** <b>用途</b> */
	private java.lang.String application;
	/** Getter for <b>用途</b> */
	public java.lang.String getApplication(){
		return application;
	}
	/** Setter for <b>用途</b> */
	public com.westvalley.tempus.carry.fncl.PRB setApplication(java.lang.String application){
		this.application = application;
		return this;
	}


	// XXX Fields End.

}