package com.westvalley.tempus.carry.oracle;

import java.util.List;

import crivia.c.a.KeyLink;
import crivia.c.carry.ValueField;
import crivia.ecp.a.ECC;

public class OVM extends ECC {
	
	public static void main(String[] args) {
//		String s=crivia.txt.common.Text.get(OVM.class, "a");
//		for(String ss:s.replaceAll("\r", "").split("\n")){
//			String[]sss=ss.replaceAll("\t", " ").split(" ");
//			if(sss.length<2)continue;
//			System.out.println("keyLinks.add(ValueField.c2d(\""+sss[0]+"\").setTipText(\""+sss[1]+"\"));");
//		}
		refreshDBIandFields(OVM.class);
	}
	
	@Override
	public ValueField primaryKey() {
		return ValueField.c2d("id","fid");
	}

	@Override
	public String tableName() {
		return "tb_fvoa_reimbursement";
	}

	@Override
	public void keyLinks(List<KeyLink> keyLinks) {
		
		keyLinks.add(ValueField.c2d("fprocessinstanceid").setTipText("requestID"));
		keyLinks.add(ValueField.c2d("ftotalamount",double.class).setTipText("总金额"));
		keyLinks.add(ValueField.c2d("fcreator").setTipText("hrmResource表的workCode字段"));
		keyLinks.add(ValueField.c2d("fcostcompany").setTipText("费用报销公司"));
		keyLinks.add(ValueField.c2d("finvoicedate").setTipText("发票日期"));
		keyLinks.add(ValueField.c2d("fpaymentdate").setTipText("GL日期"));
		keyLinks.add(ValueField.c2d("fpayee").setTipText("领款人工号"));
		keyLinks.add(ValueField.c2d("fpaymentaccount").setTipText("付款银行账户"));
		keyLinks.add(ValueField.c2d("fcurrency").setTipText("币种"));
		keyLinks.add(ValueField.c2d("fcompanyname").setTipText("费用报销公司名称（合同付款）"));
		keyLinks.add(ValueField.c2d("finvoicedescription").setTipText("摘要（合同付款）"));
		keyLinks.add(ValueField.c2d("fpaymentmethod").setTipText("付款方式（对应表单中的付款方式：check支票"));

		keyLinks.add(ValueField.c2d("FCOMBINATIONID".toLowerCase()).setTipText("相关oracle凭证-组合ID"));
		keyLinks.add(ValueField.c2d("finvoiceID","fapinvoiceID").setTipText("相关oracle凭证-凭证号"));
		keyLinks.add(ValueField.c2d("fvcrStatus").setTipText("相关oracle凭证-凭证生成状态"));
//		keyLinks.add(ValueField.c2d("fvcrCreateDay").setTipText("相关oracle凭证-凭证生成日期"));
		keyLinks.add(ValueField.c2d("fvcrCreator").setTipText("相关oracle凭证-制单人"));
		keyLinks.add(ValueField.c2d("fvcrErrorInfo").setTipText("相关oracle凭证-错误信息"));
		keyLinks.add(ValueField.c2d("fheaderSummary").setTipText("相关oracle凭证-头摘要"));
	}


	
	
	
	
	
	
	
	//XXX DBI Begin.
	public enum Keys{
		/** <b>requestID</b> */fprocessinstanceid("fprocessinstanceid","fprocessinstanceid")
		,/** <b>总金额</b> */ftotalamount("ftotalamount","ftotalamount")
		,/** <b>hrmResource表的workCode字段</b> */fcreator("fcreator","fcreator")
		,/** <b>费用报销公司</b> */fcostcompany("fcostcompany","fcostcompany")
		,/** <b>发票日期</b> */finvoicedate("finvoicedate","finvoicedate")
		,/** <b>GL日期</b> */fpaymentdate("fpaymentdate","fpaymentdate")
		,/** <b>领款人工号</b> */fpayee("fpayee","fpayee")
		,/** <b>付款银行账户</b> */fpaymentaccount("fpaymentaccount","fpaymentaccount")
		,/** <b>币种</b> */fcurrency("fcurrency","fcurrency")
		,/** <b>费用报销公司名称（合同付款）</b> */fcompanyname("fcompanyname","fcompanyname")
		,/** <b>摘要（合同付款）</b> */finvoicedescription("finvoicedescription","finvoicedescription")
		,/** <b>付款方式（对应表单中的付款方式：check支票</b> */fpaymentmethod("fpaymentmethod","fpaymentmethod")
		,/** <b>相关oracle凭证-组合ID</b> */fcombinationid("fcombinationid","fcombinationid")
		,/** <b>相关oracle凭证-凭证号</b> */finvoiceID("finvoiceID","fapinvoiceID")
		,/** <b>相关oracle凭证-凭证生成状态</b> */fvcrStatus("fvcrStatus","fvcrStatus")
		,/** <b>相关oracle凭证-制单人</b> */fvcrCreator("fvcrCreator","fvcrCreator")
		,/** <b>相关oracle凭证-错误信息</b> */fvcrErrorInfo("fvcrErrorInfo","fvcrErrorInfo")
		,/** <b>相关oracle凭证-头摘要</b> */fheaderSummary("fheaderSummary","fheaderSummary")
		;
		private String _dbKey;
		private String _key;
		Keys(String key,String dbKey){_key=key;_dbKey=dbKey;}
		@Override
		public String toString(){return _dbKey;}
		/**  <b>Key</b> in <b>Carry</b>. */
		public String key(){return _key;}
	}
	private static com.westvalley.tempus.carry.oracle.OVM dbi;
	public synchronized static void initDBI(){if(null!=dbi)return;dbi=new com.westvalley.tempus.carry.oracle.OVM();}
	/**  <b>DB Table</b> of <b>Carry</b>. */
	public static String table(){initDBI();return dbi.tableName();}
	/**  <b>Primary Key</b> of <b>Carry</b>. */
	public static String pk(){initDBI();return dbi.pk4db();}
	/**  <b>Relation SQL</b> of <b>Relation Key</b>. */
	public static String rSQL(com.westvalley.tempus.carry.oracle.OVM.Keys k,String t){initDBI();return dbi.relationSQL(k.key(),t);}
	//XXX DBI End.



	

	

	

	

	

	

	

	// XXX Fields Begin.



	/** ...(Miss Tip Text.) */
	private java.lang.String id;
	/** Getter for ...(Miss Tip Text.) */
	public java.lang.String getId(){
		return id;
	}
	/** Setter for ...(Miss Tip Text.) */
	public com.westvalley.tempus.carry.oracle.OVM setId(java.lang.String id){
		this.id = id;
		return this;
	}


	/** <b>requestID</b> */
	private java.lang.String fprocessinstanceid;
	/** Getter for <b>requestID</b> */
	public java.lang.String getFprocessinstanceid(){
		return fprocessinstanceid;
	}
	/** Setter for <b>requestID</b> */
	public com.westvalley.tempus.carry.oracle.OVM setFprocessinstanceid(java.lang.String fprocessinstanceid){
		this.fprocessinstanceid = fprocessinstanceid;
		return this;
	}


	/** <b>总金额</b> */
	private double ftotalamount;
	/** Getter for <b>总金额</b> */
	public double getFtotalamount(){
		return ftotalamount;
	}
	/** Setter for <b>总金额</b> */
	public com.westvalley.tempus.carry.oracle.OVM setFtotalamount(double ftotalamount){
		this.ftotalamount = ftotalamount;
		return this;
	}


	/** <b>hrmResource表的workCode字段</b> */
	private java.lang.String fcreator;
	/** Getter for <b>hrmResource表的workCode字段</b> */
	public java.lang.String getFcreator(){
		return fcreator;
	}
	/** Setter for <b>hrmResource表的workCode字段</b> */
	public com.westvalley.tempus.carry.oracle.OVM setFcreator(java.lang.String fcreator){
		this.fcreator = fcreator;
		return this;
	}


	/** <b>费用报销公司</b> */
	private java.lang.String fcostcompany;
	/** Getter for <b>费用报销公司</b> */
	public java.lang.String getFcostcompany(){
		return fcostcompany;
	}
	/** Setter for <b>费用报销公司</b> */
	public com.westvalley.tempus.carry.oracle.OVM setFcostcompany(java.lang.String fcostcompany){
		this.fcostcompany = fcostcompany;
		return this;
	}


	/** <b>发票日期</b> */
	private java.lang.String finvoicedate;
	/** Getter for <b>发票日期</b> */
	public java.lang.String getFinvoicedate(){
		return finvoicedate;
	}
	/** Setter for <b>发票日期</b> */
	public com.westvalley.tempus.carry.oracle.OVM setFinvoicedate(java.lang.String finvoicedate){
		this.finvoicedate = finvoicedate;
		return this;
	}


	/** <b>GL日期</b> */
	private java.lang.String fpaymentdate;
	/** Getter for <b>GL日期</b> */
	public java.lang.String getFpaymentdate(){
		return fpaymentdate;
	}
	/** Setter for <b>GL日期</b> */
	public com.westvalley.tempus.carry.oracle.OVM setFpaymentdate(java.lang.String fpaymentdate){
		this.fpaymentdate = fpaymentdate;
		return this;
	}


	/** <b>领款人工号</b> */
	private java.lang.String fpayee;
	/** Getter for <b>领款人工号</b> */
	public java.lang.String getFpayee(){
		return fpayee;
	}
	/** Setter for <b>领款人工号</b> */
	public com.westvalley.tempus.carry.oracle.OVM setFpayee(java.lang.String fpayee){
		this.fpayee = fpayee;
		return this;
	}


	/** <b>付款银行账户</b> */
	private java.lang.String fpaymentaccount;
	/** Getter for <b>付款银行账户</b> */
	public java.lang.String getFpaymentaccount(){
		return fpaymentaccount;
	}
	/** Setter for <b>付款银行账户</b> */
	public com.westvalley.tempus.carry.oracle.OVM setFpaymentaccount(java.lang.String fpaymentaccount){
		this.fpaymentaccount = fpaymentaccount;
		return this;
	}


	/** <b>币种</b> */
	private java.lang.String fcurrency;
	/** Getter for <b>币种</b> */
	public java.lang.String getFcurrency(){
		return fcurrency;
	}
	/** Setter for <b>币种</b> */
	public com.westvalley.tempus.carry.oracle.OVM setFcurrency(java.lang.String fcurrency){
		this.fcurrency = fcurrency;
		return this;
	}


	/** <b>费用报销公司名称（合同付款）</b> */
	private java.lang.String fcompanyname;
	/** Getter for <b>费用报销公司名称（合同付款）</b> */
	public java.lang.String getFcompanyname(){
		return fcompanyname;
	}
	/** Setter for <b>费用报销公司名称（合同付款）</b> */
	public com.westvalley.tempus.carry.oracle.OVM setFcompanyname(java.lang.String fcompanyname){
		this.fcompanyname = fcompanyname;
		return this;
	}


	/** <b>摘要（合同付款）</b> */
	private java.lang.String finvoicedescription;
	/** Getter for <b>摘要（合同付款）</b> */
	public java.lang.String getFinvoicedescription(){
		return finvoicedescription;
	}
	/** Setter for <b>摘要（合同付款）</b> */
	public com.westvalley.tempus.carry.oracle.OVM setFinvoicedescription(java.lang.String finvoicedescription){
		this.finvoicedescription = finvoicedescription;
		return this;
	}


	/** <b>付款方式（对应表单中的付款方式：check支票</b> */
	private java.lang.String fpaymentmethod;
	/** Getter for <b>付款方式（对应表单中的付款方式：check支票</b> */
	public java.lang.String getFpaymentmethod(){
		return fpaymentmethod;
	}
	/** Setter for <b>付款方式（对应表单中的付款方式：check支票</b> */
	public com.westvalley.tempus.carry.oracle.OVM setFpaymentmethod(java.lang.String fpaymentmethod){
		this.fpaymentmethod = fpaymentmethod;
		return this;
	}


	/** <b>相关oracle凭证-组合ID</b> */
	private java.lang.String fcombinationid;
	/** Getter for <b>相关oracle凭证-组合ID</b> */
	public java.lang.String getFcombinationid(){
		return fcombinationid;
	}
	/** Setter for <b>相关oracle凭证-组合ID</b> */
	public com.westvalley.tempus.carry.oracle.OVM setFcombinationid(java.lang.String fcombinationid){
		this.fcombinationid = fcombinationid;
		return this;
	}


	/** <b>相关oracle凭证-凭证号</b> */
	private java.lang.String finvoiceID;
	/** Getter for <b>相关oracle凭证-凭证号</b> */
	public java.lang.String getFinvoiceID(){
		return finvoiceID;
	}
	/** Setter for <b>相关oracle凭证-凭证号</b> */
	public com.westvalley.tempus.carry.oracle.OVM setFinvoiceID(java.lang.String finvoiceID){
		this.finvoiceID = finvoiceID;
		return this;
	}


	/** <b>相关oracle凭证-凭证生成状态</b> */
	private java.lang.String fvcrStatus;
	/** Getter for <b>相关oracle凭证-凭证生成状态</b> */
	public java.lang.String getFvcrStatus(){
		return fvcrStatus;
	}
	/** Setter for <b>相关oracle凭证-凭证生成状态</b> */
	public com.westvalley.tempus.carry.oracle.OVM setFvcrStatus(java.lang.String fvcrStatus){
		this.fvcrStatus = fvcrStatus;
		return this;
	}


	/** <b>相关oracle凭证-制单人</b> */
	private java.lang.String fvcrCreator;
	/** Getter for <b>相关oracle凭证-制单人</b> */
	public java.lang.String getFvcrCreator(){
		return fvcrCreator;
	}
	/** Setter for <b>相关oracle凭证-制单人</b> */
	public com.westvalley.tempus.carry.oracle.OVM setFvcrCreator(java.lang.String fvcrCreator){
		this.fvcrCreator = fvcrCreator;
		return this;
	}


	/** <b>相关oracle凭证-错误信息</b> */
	private java.lang.String fvcrErrorInfo;
	/** Getter for <b>相关oracle凭证-错误信息</b> */
	public java.lang.String getFvcrErrorInfo(){
		return fvcrErrorInfo;
	}
	/** Setter for <b>相关oracle凭证-错误信息</b> */
	public com.westvalley.tempus.carry.oracle.OVM setFvcrErrorInfo(java.lang.String fvcrErrorInfo){
		this.fvcrErrorInfo = fvcrErrorInfo;
		return this;
	}


	/** <b>相关oracle凭证-头摘要</b> */
	private java.lang.String fheaderSummary;
	/** Getter for <b>相关oracle凭证-头摘要</b> */
	public java.lang.String getFheaderSummary(){
		return fheaderSummary;
	}
	/** Setter for <b>相关oracle凭证-头摘要</b> */
	public com.westvalley.tempus.carry.oracle.OVM setFheaderSummary(java.lang.String fheaderSummary){
		this.fheaderSummary = fheaderSummary;
		return this;
	}


	// XXX Fields End.

}