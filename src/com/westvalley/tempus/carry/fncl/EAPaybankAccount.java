package com.westvalley.tempus.carry.fncl;

import java.util.List;

import crivia.c.a.KeyLink;
import crivia.c.carry.ValueField;
import crivia.ecp.a.ECC;

public class EAPaybankAccount extends ECC{
	
	public static void main(String[] args) {
		refreshDBIandFields(EAPaybankAccount.class);
		printTableCreater4Oracle(EAPaybankAccount.class);
		
	}




	@Override
	public String tableName() {
		return "WV_FNCL_EA_PaybankAccount";
	}

	@Override
	public void keyLinks(List<KeyLink> keyLinks) {
		keyLinks.add(ValueField.c2d("cpcode").setTipText("公司编码"));
		keyLinks.add(ValueField.c2d("paybankid").setTipText("银行账户id"));
		keyLinks.add(ValueField.c2d("paybankacount").setTipText("付款银行账户"));
		keyLinks.add(ValueField.c2d("paybanknum").setTipText("线上付款银行账户"));
	}


	

	
	
	//XXX DBI Begin.
	public enum Keys{
		/** <b>公司编码</b> */cpcode("cpcode","cpcode")
		,/** <b>银行账户id</b> */paybankid("paybankid","paybankid")
		,/** <b>付款银行账户</b> */paybankacount("paybankacount","paybankacount")
		,/** <b>线上付款银行账户</b> */paybanknum("paybanknum","paybanknum")
		;
		private String _dbKey;
		private String _key;
		Keys(String key,String dbKey){_key=key;_dbKey=dbKey;}
		@Override
		public String toString(){return _dbKey;}
		/**  <b>Key</b> in <b>Carry</b>. */
		public String key(){return _key;}
	}
	private static com.westvalley.tempus.carry.fncl.EAPaybankAccount dbi;
	public synchronized static void initDBI(){if(null!=dbi)return;dbi=new com.westvalley.tempus.carry.fncl.EAPaybankAccount();}
	/**  <b>DB Table</b> of <b>Carry</b>. */
	public static String table(){initDBI();return dbi.tableName();}
	/**  <b>Primary Key</b> of <b>Carry</b>. */
	public static String pk(){initDBI();return dbi.pk4db();}
	/**  <b>Relation SQL</b> of <b>Relation Key</b>. */
	public static String rSQL(com.westvalley.tempus.carry.fncl.EAPaybankAccount.Keys k,String t){initDBI();return dbi.relationSQL(k.key(),t);}
	//XXX DBI End.



	

	

	// XXX Fields Begin.



	/** ...(Miss Tip Text.) */
	private java.lang.String id;
	/** Getter for ...(Miss Tip Text.) */
	public java.lang.String getId(){
		return id;
	}
	/** Setter for ...(Miss Tip Text.) */
	public com.westvalley.tempus.carry.fncl.EAPaybankAccount setId(java.lang.String id){
		this.id = id;
		return this;
	}


	/** <b>公司编码</b> */
	private java.lang.String cpcode;
	/** Getter for <b>公司编码</b> */
	public java.lang.String getCpcode(){
		return cpcode;
	}
	/** Setter for <b>公司编码</b> */
	public com.westvalley.tempus.carry.fncl.EAPaybankAccount setCpcode(java.lang.String cpcode){
		this.cpcode = cpcode;
		return this;
	}


	/** <b>银行账户id</b> */
	private java.lang.String paybankid;
	/** Getter for <b>银行账户id</b> */
	public java.lang.String getPaybankid(){
		return paybankid;
	}
	/** Setter for <b>银行账户id</b> */
	public com.westvalley.tempus.carry.fncl.EAPaybankAccount setPaybankid(java.lang.String paybankid){
		this.paybankid = paybankid;
		return this;
	}


	/** <b>付款银行账户</b> */
	private java.lang.String paybankacount;
	/** Getter for <b>付款银行账户</b> */
	public java.lang.String getPaybankacount(){
		return paybankacount;
	}
	/** Setter for <b>付款银行账户</b> */
	public com.westvalley.tempus.carry.fncl.EAPaybankAccount setPaybankacount(java.lang.String paybankacount){
		this.paybankacount = paybankacount;
		return this;
	}


	/** <b>线上付款银行账户</b> */
	private java.lang.String paybanknum;
	/** Getter for <b>线上付款银行账户</b> */
	public java.lang.String getPaybanknum(){
		return paybanknum;
	}
	/** Setter for <b>线上付款银行账户</b> */
	public com.westvalley.tempus.carry.fncl.EAPaybankAccount setPaybanknum(java.lang.String paybanknum){
		this.paybanknum = paybanknum;
		return this;
	}


	// XXX Fields End.

}