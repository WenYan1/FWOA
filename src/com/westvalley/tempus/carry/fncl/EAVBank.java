package com.westvalley.tempus.carry.fncl;

import java.util.List;

import crivia.c.a.KeyLink;
import crivia.c.carry.ValueField;
import crivia.ecp.a.ECC;
/**费用类别*/
public class EAVBank extends ECC{
	
	public static void main(String[] args) {
		refreshDBIandFields(EAVBank.class);
		printTableCreater4Oracle(EAVBank.class);
		
	}
	
	

	@Override
	public String tableName() {
		return "WV_FNCL_EA_V_BANK";
	}

	@Override
	public void keyLinks(List<KeyLink> keyLinks) {
		keyLinks.add(ValueField.c2d("bank_code").setTipText("银行编码"));
		keyLinks.add(ValueField.c2d("bank_name").setTipText("银行名称"));
		
	}
	
	


	
	//XXX DBI Begin.
	public enum Keys{
		/** <b>银行编码</b> */bank_code("bank_code","bank_code")
		,/** <b>银行名称</b> */bank_name("bank_name","bank_name")
		;
		private String _dbKey;
		private String _key;
		Keys(String key,String dbKey){_key=key;_dbKey=dbKey;}
		@Override
		public String toString(){return _dbKey;}
		/**  <b>Key</b> in <b>Carry</b>. */
		public String key(){return _key;}
	}
	private static com.westvalley.tempus.carry.fncl.EAVBank dbi;
	public synchronized static void initDBI(){if(null!=dbi)return;dbi=new com.westvalley.tempus.carry.fncl.EAVBank();}
	/**  <b>DB Table</b> of <b>Carry</b>. */
	public static String table(){initDBI();return dbi.tableName();}
	/**  <b>Primary Key</b> of <b>Carry</b>. */
	public static String pk(){initDBI();return dbi.pk4db();}
	/**  <b>Relation SQL</b> of <b>Relation Key</b>. */
	public static String rSQL(com.westvalley.tempus.carry.fncl.EAVBank.Keys k,String t){initDBI();return dbi.relationSQL(k.key(),t);}
	//XXX DBI End.



	

	// XXX Fields Begin.



	/** ...(Miss Tip Text.) */
	private java.lang.String id;
	/** Getter for ...(Miss Tip Text.) */
	public java.lang.String getId(){
		return id;
	}
	/** Setter for ...(Miss Tip Text.) */
	public com.westvalley.tempus.carry.fncl.EAVBank setId(java.lang.String id){
		this.id = id;
		return this;
	}


	/** <b>银行编码</b> */
	private java.lang.String bank_code;
	/** Getter for <b>银行编码</b> */
	public java.lang.String getBank_code(){
		return bank_code;
	}
	/** Setter for <b>银行编码</b> */
	public com.westvalley.tempus.carry.fncl.EAVBank setBank_code(java.lang.String bank_code){
		this.bank_code = bank_code;
		return this;
	}


	/** <b>银行名称</b> */
	private java.lang.String bank_name;
	/** Getter for <b>银行名称</b> */
	public java.lang.String getBank_name(){
		return bank_name;
	}
	/** Setter for <b>银行名称</b> */
	public com.westvalley.tempus.carry.fncl.EAVBank setBank_name(java.lang.String bank_name){
		this.bank_name = bank_name;
		return this;
	}


	// XXX Fields End.

}