package com.westvalley.tempus.carry.fncl;

import java.util.List;

import crivia.c.a.KeyLink;
import crivia.c.carry.ValueField;
import crivia.ecp.a.ECC;
/**开户行*/
public class EAVStartBank extends ECC{
	
	public static void main(String[] args) {
		refreshDBIandFields(EAVStartBank.class);
		printTableCreater4Oracle(EAVStartBank.class);
		
	}
	
	 

	@Override
	public String tableName() {
		return "WV_FNCL_EA_START_BANK";
	}

	@Override
	public void keyLinks(List<KeyLink> keyLinks) {
		keyLinks.add(ValueField.c2d("bank_id").setTipText("开户行银行id"));
		keyLinks.add(ValueField.c2d("house_bank_code").setTipText("开户行银行编码"));
		keyLinks.add(ValueField.c2d("house_bank_name").setTipText("开户行银行名称"));
		keyLinks.add(ValueField.c2d("city_code").setTipText("城市编码"));
		
	}
	
	



	//XXX DBI Begin.
	public enum Keys{
		/** <b>开户行银行id</b> */bank_id("bank_id","bank_id")
		,/** <b>开户行银行编码</b> */house_bank_code("house_bank_code","house_bank_code")
		,/** <b>开户行银行名称</b> */house_bank_name("house_bank_name","house_bank_name")
		,/** <b>城市编码</b> */city_code("city_code","city_code")
		;
		private String _dbKey;
		private String _key;
		Keys(String key,String dbKey){_key=key;_dbKey=dbKey;}
		@Override
		public String toString(){return _dbKey;}
		/**  <b>Key</b> in <b>Carry</b>. */
		public String key(){return _key;}
	}
	private static com.westvalley.tempus.carry.fncl.EAVStartBank dbi;
	public synchronized static void initDBI(){if(null!=dbi)return;dbi=new com.westvalley.tempus.carry.fncl.EAVStartBank();}
	/**  <b>DB Table</b> of <b>Carry</b>. */
	public static String table(){initDBI();return dbi.tableName();}
	/**  <b>Primary Key</b> of <b>Carry</b>. */
	public static String pk(){initDBI();return dbi.pk4db();}
	/**  <b>Relation SQL</b> of <b>Relation Key</b>. */
	public static String rSQL(com.westvalley.tempus.carry.fncl.EAVStartBank.Keys k,String t){initDBI();return dbi.relationSQL(k.key(),t);}
	//XXX DBI End.



	// XXX Fields Begin.



	/** ...(Miss Tip Text.) */
	private java.lang.String id;
	/** Getter for ...(Miss Tip Text.) */
	public java.lang.String getId(){
		return id;
	}
	/** Setter for ...(Miss Tip Text.) */
	public com.westvalley.tempus.carry.fncl.EAVStartBank setId(java.lang.String id){
		this.id = id;
		return this;
	}


	/** <b>开户行银行id</b> */
	private java.lang.String bank_id;
	/** Getter for <b>开户行银行id</b> */
	public java.lang.String getBank_id(){
		return bank_id;
	}
	/** Setter for <b>开户行银行id</b> */
	public com.westvalley.tempus.carry.fncl.EAVStartBank setBank_id(java.lang.String bank_id){
		this.bank_id = bank_id;
		return this;
	}


	/** <b>开户行银行编码</b> */
	private java.lang.String house_bank_code;
	/** Getter for <b>开户行银行编码</b> */
	public java.lang.String getHouse_bank_code(){
		return house_bank_code;
	}
	/** Setter for <b>开户行银行编码</b> */
	public com.westvalley.tempus.carry.fncl.EAVStartBank setHouse_bank_code(java.lang.String house_bank_code){
		this.house_bank_code = house_bank_code;
		return this;
	}


	/** <b>开户行银行名称</b> */
	private java.lang.String house_bank_name;
	/** Getter for <b>开户行银行名称</b> */
	public java.lang.String getHouse_bank_name(){
		return house_bank_name;
	}
	/** Setter for <b>开户行银行名称</b> */
	public com.westvalley.tempus.carry.fncl.EAVStartBank setHouse_bank_name(java.lang.String house_bank_name){
		this.house_bank_name = house_bank_name;
		return this;
	}


	/** <b>城市编码</b> */
	private java.lang.String city_code;
	/** Getter for <b>城市编码</b> */
	public java.lang.String getCity_code(){
		return city_code;
	}
	/** Setter for <b>城市编码</b> */
	public com.westvalley.tempus.carry.fncl.EAVStartBank setCity_code(java.lang.String city_code){
		this.city_code = city_code;
		return this;
	}


	// XXX Fields End.

}