package com.westvalley.tempus.carry.fncl;

import java.util.List;

import crivia.c.a.KeyLink;
import crivia.c.carry.RelationKey;
import crivia.c.carry.SubCarry;
import crivia.c.carry.ValueField;
import crivia.ecp.ECPExecuter;
import crivia.ecp.a.ECC;
import crivia.ecp.carry.ec.hrm.HR;
import crivia.ecp.carry.ec.workflow.WFRequestBase;
import crivia.ecp.carry.html.form.Select;
/**
 * Pay Request
 */
public class Receipt extends ECC {
	
	public static void main(String[] args) {
		refreshDBIandFields(Receipt.class);
//		EnumCreater.p("PayLine");
		//System.out.println("drop table "+Receipt.table()+";");
		printTableCreater4Oracle(Receipt.class);
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


	/**付款状态*/
	public enum PayStatus {
		Unpay("未支付",true)
		,Payed("已支付",false)
		,Success("支付成功",false)
		,Fail("支付失败",true)
		,Edited("已修改",true)
		;
		PayStatus(String t,boolean rea){this.t = t;reeditable=rea;}
		public final String t;
		public final boolean reeditable;
		public static PayStatus k2e(String k){
			try {
				return valueOf(k);
			} catch (Exception e) {
				return Unpay;
			}
		}
		public static String t2k(String t){
			for (PayStatus a : PayStatus.values()) if(a.t.equals(t))return a.toString();
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
			for (PayStatus c : PayStatus.values()){
				select.addOption(""+c, c.t);
			}
		}
		
		public static String caseSQL(String f){
			StringBuilder s = new StringBuilder("(case ").append(f);
			for (PayStatus a : PayStatus.values()){
				s
					.append(" when '").append(a)
					.append("' then '").append(a.t).append("'");
			}
			return s.append(" else '' end)").toString();
		}
	}
	
	
	


	@Override
	public String tableName() {
		return "WV_FNCL_Receipt";
	}

	@Override
	public void keyLinks(List<KeyLink> keyLinks) {
		keyLinks.add(ValueField.c2d("requestID").setTipText("关联业务流程"));
		keyLinks.add(ValueField.c2d("money",double.class).setTipText("金额"));
		//keyLinks.add(ValueField.c2d("createDay").setTipText("申请日期"));
		//keyLinks.add(ValueField.c2d("creator").setTipText("申请人"));
		//keyLinks.add(RelationKey.c2d("creatorName","creator"
			//	,HR.table(),HR.Keys.lastName,HR.pk())
			//.setRelationPKFormat("to_char(?)")
		//	.setTipText("申请人-姓名"));
//		keyLinks.add(SubCarry.c2d("vcr",VCR.class).setTipText("相关oracle凭证"));
//		keyLinks.add(RelationKey.c2d("payRecordID","vcr"
//				,VCR.table(),VCR.Keys.pr,VCR.pk()).setTipText("相关oracle凭证-资金关联平台业务参考号"));
		keyLinks.add(ValueField.c2d("payStatus").setTipText("支付状态"));
		//keyLinks.add(SubCarry.c2d("prb",PRB.class).setTipText("相关付款批次"));
		//keyLinks.add(RelationKey.c2d("payKey","prb"
			//	,PRB.table(),PRB.Keys.payKey,PRB.pk()).setTipText("相关付款批次-资金关联平台业务参考号"));
		//keyLinks.add(RelationKey.c2d("payLine","prb"
			//	,PRB.table(),PRB.Keys.payLine,PRB.pk()).setTipText("相关付款批次-支付方式"));
		//keyLinks.add(RelationKey.c2d("payDay","prb"
			//	,PRB.table(),PRB.Keys.payDay,PRB.pk()).setTipText("相关付款批次-支付日期"));
//		keyLinks.add(RelationKey.c2d("payStatus","prb"
//				,PRB.table(),PRB.Keys.payStatus,PRB.pk()).setTipText("相关付款批次-支付状态"));
		//keyLinks.add(RelationKey.c2d("payOperator","prb"
				//,PRB.table(),PRB.Keys.payOperator,PRB.pk()).setTipText("相关付款批次-支付操作人"));
		//keyLinks.add(RelationKey.c2d("payOperatorName","prb"
			//	,HR.table(),HR.Keys.lastName,HR.pk())
			//.setRelationCDFormat("(select ss."+PRB.Keys.payOperator
			//		+" from "+PRB.table()+" ss where ss."+PRB.pk()+"=?)")
			//.setRelationPKFormat("to_char(?)")
			//.setTipText("相关付款批次-支付操作人-姓名"));
//		keyLinks.add(RelationKey.c2d("payLine","vcr"
//				,VCR.table(),VCR.Keys.payLine,VCR.pk()).setTipText("相关oracle凭证-支付方式"));
		keyLinks.add(ValueField.c2d("recBankName").setTipText("收款银行名称"));
		keyLinks.add(ValueField.c2d("recBankAccountNumber").setTipText("收款银行账号"));
		keyLinks.add(ValueField.c2d("recBankAccountName").setTipText("收款银行账户名"));
		keyLinks.add(ValueField.c2d("recBankAccountID").setTipText("收款银行账户ID"));
		keyLinks.add(ValueField.c2d("recSupplyName").setTipText("收款供应商名称"));
		keyLinks.add(ValueField.c2d("recSupplyID").setTipText("收款供应商ID"));
		keyLinks.add(ValueField.c2d("vcrN").setTipText("相关oracle凭证-凭证号"));
		keyLinks.add(ValueField.c2d("vcrStatus").setTipText("相关oracle凭证-凭证生成状态"));
		keyLinks.add(ValueField.c2d("vcrCreateDay").setTipText("相关oracle凭证-凭证生成日期"));
		keyLinks.add(ValueField.c2d("vcrCreator").setTipText("相关oracle凭证-制单人"));
		keyLinks.add(ValueField.c2d("vcrErrorInfo").setTipText("相关oracle凭证-错误信息"));

		keyLinks.add(ValueField.c2d("vdEACompany").setTipText("相关oracle凭证数据-费用报销公司"));
		keyLinks.add(ValueField.c2d("vdGLDay").setTipText("相关oracle凭证数据-GL日期"));
		keyLinks.add(ValueField.c2d("vdCurrency").setTipText("相关oracle凭证数据-币种"));
		keyLinks.add(ValueField.c2d("vdEACompanyName").setTipText("相关oracle凭证数据-费用报销公司名称（合同付款）"));
		keyLinks.add(ValueField.c2d("vdInvoiceDesc").setTipText("相关oracle凭证数据-摘要（合同付款）"));
		keyLinks.add(ValueField.c2d("vdPayMentMethod").setTipText("相关oracle凭证数据-付款方式（对应表单中的付款方式：check支票  win 电汇）"));
		keyLinks.add(ValueField.c2d("vdPayMentAccount").setTipText("相关oracle凭证数据-付款银行账号"));
		keyLinks.add(RelationKey.c2d("vdPayMentAccountName","vdPayMentAccount"
				,EAPaybankAccount.table()
				,EAPaybankAccount.Keys.paybankacount
				,""+EAPaybankAccount.Keys.paybankid
		).setTipText("相关oracle凭证数据-付款银行账号"));
		keyLinks.add(ValueField.c2d("vdRecPsn").setTipText("相关oracle凭证数据-收款人"));
		keyLinks.add(RelationKey.c2d("vdRecPsnName","vdRecPsn"
				,HR.table(),HR.Keys.lastName,HR.pk())
			.setRelationPKFormat("to_char(?)")
			.setTipText("相关oracle凭证数据-收款人"));
	}



	
	
	
	
	
	
	
	



	public PayStatus getPayStatusX(){
		return PayStatus.k2e(payStatus);
	}
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	


	//XXX DBI Begin.
	public enum Keys{
		/** <b>关联业务流程</b> */requestID("requestID","requestID")
		,/** <b>金额</b> */money("money","money")
		,/** <b>支付状态</b> */payStatus("payStatus","payStatus")
		,/** <b>收款银行名称</b> */recBankName("recBankName","recBankName")
		,/** <b>收款银行账号</b> */recBankAccountNumber("recBankAccountNumber","recBankAccountNumber")
		,/** <b>收款银行账户名</b> */recBankAccountName("recBankAccountName","recBankAccountName")
		,/** <b>收款银行账户ID</b> */recBankAccountID("recBankAccountID","recBankAccountID")
		,/** <b>收款供应商名称</b> */recSupplyName("recSupplyName","recSupplyName")
		,/** <b>收款供应商ID</b> */recSupplyID("recSupplyID","recSupplyID")
		,/** <b>相关oracle凭证-凭证号</b> */vcrN("vcrN","vcrN")
		,/** <b>相关oracle凭证-凭证生成状态</b> */vcrStatus("vcrStatus","vcrStatus")
		,/** <b>相关oracle凭证-凭证生成日期</b> */vcrCreateDay("vcrCreateDay","vcrCreateDay")
		,/** <b>相关oracle凭证-制单人</b> */vcrCreator("vcrCreator","vcrCreator")
		,/** <b>相关oracle凭证-错误信息</b> */vcrErrorInfo("vcrErrorInfo","vcrErrorInfo")
		,/** <b>相关oracle凭证数据-费用报销公司</b> */vdEACompany("vdEACompany","vdEACompany")
		,/** <b>相关oracle凭证数据-GL日期</b> */vdGLDay("vdGLDay","vdGLDay")
		,/** <b>相关oracle凭证数据-币种</b> */vdCurrency("vdCurrency","vdCurrency")
		,/** <b>相关oracle凭证数据-费用报销公司名称（合同付款）</b> */vdEACompanyName("vdEACompanyName","vdEACompanyName")
		,/** <b>相关oracle凭证数据-摘要（合同付款）</b> */vdInvoiceDesc("vdInvoiceDesc","vdInvoiceDesc")
		,/** <b>相关oracle凭证数据-付款方式（对应表单中的付款方式：check支票  win 电汇）</b> */vdPayMentMethod("vdPayMentMethod","vdPayMentMethod")
		,/** <b>相关oracle凭证数据-付款银行账号</b> */vdPayMentAccount("vdPayMentAccount","vdPayMentAccount")
		,/** <b>相关oracle凭证数据-付款银行账号</b> */vdPayMentAccountName("vdPayMentAccountName","paybankacount")
		,/** <b>相关oracle凭证数据-收款人</b> */vdRecPsn("vdRecPsn","vdRecPsn")
		,/** <b>相关oracle凭证数据-收款人</b> */vdRecPsnName("vdRecPsnName","lastName")
		;
		private String _dbKey;
		private String _key;
		Keys(String key,String dbKey){_key=key;_dbKey=dbKey;}
		@Override
		public String toString(){return _dbKey;}
		/**  <b>Key</b> in <b>Carry</b>. */
		public String key(){return _key;}
	}
	private static com.westvalley.tempus.carry.fncl.Receipt dbi;
	public synchronized static void initDBI(){if(null!=dbi)return;dbi=new com.westvalley.tempus.carry.fncl.Receipt();}
	/**  <b>DB Table</b> of <b>Carry</b>. */
	public static String table(){initDBI();return dbi.tableName();}
	/**  <b>Primary Key</b> of <b>Carry</b>. */
	public static String pk(){initDBI();return dbi.pk4db();}
	/**  <b>Relation SQL</b> of <b>Relation Key</b>. */
	public static String rSQL(com.westvalley.tempus.carry.fncl.Receipt.Keys k,String t){initDBI();return dbi.relationSQL(k.key(),t);}
	//XXX DBI End.



	// XXX Fields Begin.



	/** ...(Miss Tip Text.) */
	private java.lang.String id;
	/** Getter for ...(Miss Tip Text.) */
	public java.lang.String getId(){
		return id;
	}
	/** Setter for ...(Miss Tip Text.) */
	public com.westvalley.tempus.carry.fncl.Receipt setId(java.lang.String id){
		this.id = id;
		return this;
	}


	/** <b>关联业务流程</b> */
	private java.lang.String requestID;
	/** Getter for <b>关联业务流程</b> */
	public java.lang.String getRequestID(){
		return requestID;
	}
	/** Setter for <b>关联业务流程</b> */
	public com.westvalley.tempus.carry.fncl.Receipt setRequestID(java.lang.String requestID){
		this.requestID = requestID;
		return this;
	}


	/** <b>金额</b> */
	private double money;
	/** Getter for <b>金额</b> */
	public double getMoney(){
		return money;
	}
	/** Setter for <b>金额</b> */
	public com.westvalley.tempus.carry.fncl.Receipt setMoney(double money){
		this.money = money;
		return this;
	}


	/** <b>支付状态</b> */
	private java.lang.String payStatus;
	/** Getter for <b>支付状态</b> */
	public java.lang.String getPayStatus(){
		return payStatus;
	}
	/** Setter for <b>支付状态</b> */
	public com.westvalley.tempus.carry.fncl.Receipt setPayStatus(java.lang.String payStatus){
		this.payStatus = payStatus;
		return this;
	}


	/** <b>收款银行名称</b> */
	private java.lang.String recBankName;
	/** Getter for <b>收款银行名称</b> */
	public java.lang.String getRecBankName(){
		return recBankName;
	}
	/** Setter for <b>收款银行名称</b> */
	public com.westvalley.tempus.carry.fncl.Receipt setRecBankName(java.lang.String recBankName){
		this.recBankName = recBankName;
		return this;
	}


	/** <b>收款银行账号</b> */
	private java.lang.String recBankAccountNumber;
	/** Getter for <b>收款银行账号</b> */
	public java.lang.String getRecBankAccountNumber(){
		return recBankAccountNumber;
	}
	/** Setter for <b>收款银行账号</b> */
	public com.westvalley.tempus.carry.fncl.Receipt setRecBankAccountNumber(java.lang.String recBankAccountNumber){
		this.recBankAccountNumber = recBankAccountNumber;
		return this;
	}


	/** <b>收款银行账户名</b> */
	private java.lang.String recBankAccountName;
	/** Getter for <b>收款银行账户名</b> */
	public java.lang.String getRecBankAccountName(){
		return recBankAccountName;
	}
	/** Setter for <b>收款银行账户名</b> */
	public com.westvalley.tempus.carry.fncl.Receipt setRecBankAccountName(java.lang.String recBankAccountName){
		this.recBankAccountName = recBankAccountName;
		return this;
	}


	/** <b>收款银行账户ID</b> */
	private java.lang.String recBankAccountID;
	/** Getter for <b>收款银行账户ID</b> */
	public java.lang.String getRecBankAccountID(){
		return recBankAccountID;
	}
	/** Setter for <b>收款银行账户ID</b> */
	public com.westvalley.tempus.carry.fncl.Receipt setRecBankAccountID(java.lang.String recBankAccountID){
		this.recBankAccountID = recBankAccountID;
		return this;
	}


	/** <b>收款供应商名称</b> */
	private java.lang.String recSupplyName;
	/** Getter for <b>收款供应商名称</b> */
	public java.lang.String getRecSupplyName(){
		return recSupplyName;
	}
	/** Setter for <b>收款供应商名称</b> */
	public com.westvalley.tempus.carry.fncl.Receipt setRecSupplyName(java.lang.String recSupplyName){
		this.recSupplyName = recSupplyName;
		return this;
	}


	/** <b>收款供应商ID</b> */
	private java.lang.String recSupplyID;
	/** Getter for <b>收款供应商ID</b> */
	public java.lang.String getRecSupplyID(){
		return recSupplyID;
	}
	/** Setter for <b>收款供应商ID</b> */
	public com.westvalley.tempus.carry.fncl.Receipt setRecSupplyID(java.lang.String recSupplyID){
		this.recSupplyID = recSupplyID;
		return this;
	}


	/** <b>相关oracle凭证-凭证号</b> */
	private java.lang.String vcrN;
	/** Getter for <b>相关oracle凭证-凭证号</b> */
	public java.lang.String getVcrN(){
		return vcrN;
	}
	/** Setter for <b>相关oracle凭证-凭证号</b> */
	public com.westvalley.tempus.carry.fncl.Receipt setVcrN(java.lang.String vcrN){
		this.vcrN = vcrN;
		return this;
	}


	/** <b>相关oracle凭证-凭证生成状态</b> */
	private java.lang.String vcrStatus;
	/** Getter for <b>相关oracle凭证-凭证生成状态</b> */
	public java.lang.String getVcrStatus(){
		return vcrStatus;
	}
	/** Setter for <b>相关oracle凭证-凭证生成状态</b> */
	public com.westvalley.tempus.carry.fncl.Receipt setVcrStatus(java.lang.String vcrStatus){
		this.vcrStatus = vcrStatus;
		return this;
	}


	/** <b>相关oracle凭证-凭证生成日期</b> */
	private java.lang.String vcrCreateDay;
	/** Getter for <b>相关oracle凭证-凭证生成日期</b> */
	public java.lang.String getVcrCreateDay(){
		return vcrCreateDay;
	}
	/** Setter for <b>相关oracle凭证-凭证生成日期</b> */
	public com.westvalley.tempus.carry.fncl.Receipt setVcrCreateDay(java.lang.String vcrCreateDay){
		this.vcrCreateDay = vcrCreateDay;
		return this;
	}


	/** <b>相关oracle凭证-制单人</b> */
	private java.lang.String vcrCreator;
	/** Getter for <b>相关oracle凭证-制单人</b> */
	public java.lang.String getVcrCreator(){
		return vcrCreator;
	}
	/** Setter for <b>相关oracle凭证-制单人</b> */
	public com.westvalley.tempus.carry.fncl.Receipt setVcrCreator(java.lang.String vcrCreator){
		this.vcrCreator = vcrCreator;
		return this;
	}


	/** <b>相关oracle凭证-错误信息</b> */
	private java.lang.String vcrErrorInfo;
	/** Getter for <b>相关oracle凭证-错误信息</b> */
	public java.lang.String getVcrErrorInfo(){
		return vcrErrorInfo;
	}
	/** Setter for <b>相关oracle凭证-错误信息</b> */
	public com.westvalley.tempus.carry.fncl.Receipt setVcrErrorInfo(java.lang.String vcrErrorInfo){
		this.vcrErrorInfo = vcrErrorInfo;
		return this;
	}


	/** <b>相关oracle凭证数据-费用报销公司</b> */
	private java.lang.String vdEACompany;
	/** Getter for <b>相关oracle凭证数据-费用报销公司</b> */
	public java.lang.String getVdEACompany(){
		return vdEACompany;
	}
	/** Setter for <b>相关oracle凭证数据-费用报销公司</b> */
	public com.westvalley.tempus.carry.fncl.Receipt setVdEACompany(java.lang.String vdEACompany){
		this.vdEACompany = vdEACompany;
		return this;
	}


	/** <b>相关oracle凭证数据-GL日期</b> */
	private java.lang.String vdGLDay;
	/** Getter for <b>相关oracle凭证数据-GL日期</b> */
	public java.lang.String getVdGLDay(){
		return vdGLDay;
	}
	/** Setter for <b>相关oracle凭证数据-GL日期</b> */
	public com.westvalley.tempus.carry.fncl.Receipt setVdGLDay(java.lang.String vdGLDay){
		this.vdGLDay = vdGLDay;
		return this;
	}


	/** <b>相关oracle凭证数据-币种</b> */
	private java.lang.String vdCurrency;
	/** Getter for <b>相关oracle凭证数据-币种</b> */
	public java.lang.String getVdCurrency(){
		return vdCurrency;
	}
	/** Setter for <b>相关oracle凭证数据-币种</b> */
	public com.westvalley.tempus.carry.fncl.Receipt setVdCurrency(java.lang.String vdCurrency){
		this.vdCurrency = vdCurrency;
		return this;
	}


	/** <b>相关oracle凭证数据-费用报销公司名称（合同付款）</b> */
	private java.lang.String vdEACompanyName;
	/** Getter for <b>相关oracle凭证数据-费用报销公司名称（合同付款）</b> */
	public java.lang.String getVdEACompanyName(){
		return vdEACompanyName;
	}
	/** Setter for <b>相关oracle凭证数据-费用报销公司名称（合同付款）</b> */
	public com.westvalley.tempus.carry.fncl.Receipt setVdEACompanyName(java.lang.String vdEACompanyName){
		this.vdEACompanyName = vdEACompanyName;
		return this;
	}


	/** <b>相关oracle凭证数据-摘要（合同付款）</b> */
	private java.lang.String vdInvoiceDesc;
	/** Getter for <b>相关oracle凭证数据-摘要（合同付款）</b> */
	public java.lang.String getVdInvoiceDesc(){
		return vdInvoiceDesc;
	}
	/** Setter for <b>相关oracle凭证数据-摘要（合同付款）</b> */
	public com.westvalley.tempus.carry.fncl.Receipt setVdInvoiceDesc(java.lang.String vdInvoiceDesc){
		this.vdInvoiceDesc = vdInvoiceDesc;
		return this;
	}


	/** <b>相关oracle凭证数据-付款方式（对应表单中的付款方式：check支票  win 电汇）</b> */
	private java.lang.String vdPayMentMethod;
	/** Getter for <b>相关oracle凭证数据-付款方式（对应表单中的付款方式：check支票  win 电汇）</b> */
	public java.lang.String getVdPayMentMethod(){
		return vdPayMentMethod;
	}
	/** Setter for <b>相关oracle凭证数据-付款方式（对应表单中的付款方式：check支票  win 电汇）</b> */
	public com.westvalley.tempus.carry.fncl.Receipt setVdPayMentMethod(java.lang.String vdPayMentMethod){
		this.vdPayMentMethod = vdPayMentMethod;
		return this;
	}


	/** <b>相关oracle凭证数据-付款银行账号</b> */
	private java.lang.String vdPayMentAccount;
	/** Getter for <b>相关oracle凭证数据-付款银行账号</b> */
	public java.lang.String getVdPayMentAccount(){
		return vdPayMentAccount;
	}
	/** Setter for <b>相关oracle凭证数据-付款银行账号</b> */
	public com.westvalley.tempus.carry.fncl.Receipt setVdPayMentAccount(java.lang.String vdPayMentAccount){
		this.vdPayMentAccount = vdPayMentAccount;
		return this;
	}


	/** <b>相关oracle凭证数据-付款银行账号</b> */
	private java.lang.String vdPayMentAccountName;
	/** Getter for <b>相关oracle凭证数据-付款银行账号</b> */
	public java.lang.String getVdPayMentAccountName(){
		return vdPayMentAccountName;
	}
	/** Setter for <b>相关oracle凭证数据-付款银行账号</b> */
	public com.westvalley.tempus.carry.fncl.Receipt setVdPayMentAccountName(java.lang.String vdPayMentAccountName){
		this.vdPayMentAccountName = vdPayMentAccountName;
		return this;
	}


	/** <b>相关oracle凭证数据-收款人</b> */
	private java.lang.String vdRecPsn;
	/** Getter for <b>相关oracle凭证数据-收款人</b> */
	public java.lang.String getVdRecPsn(){
		return vdRecPsn;
	}
	/** Setter for <b>相关oracle凭证数据-收款人</b> */
	public com.westvalley.tempus.carry.fncl.Receipt setVdRecPsn(java.lang.String vdRecPsn){
		this.vdRecPsn = vdRecPsn;
		return this;
	}


	/** <b>相关oracle凭证数据-收款人</b> */
	private java.lang.String vdRecPsnName;
	/** Getter for <b>相关oracle凭证数据-收款人</b> */
	public java.lang.String getVdRecPsnName(){
		return vdRecPsnName;
	}
	/** Setter for <b>相关oracle凭证数据-收款人</b> */
	public com.westvalley.tempus.carry.fncl.Receipt setVdRecPsnName(java.lang.String vdRecPsnName){
		this.vdRecPsnName = vdRecPsnName;
		return this;
	}


	// XXX Fields End.

}