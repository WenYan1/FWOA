package com.westvalley.tempus.carry.fncl;

import java.util.List;

import crivia.c.a.KeyLink;
import crivia.c.carry.Collection;
import crivia.c.carry.SubCarry;
import crivia.c.carry.ValueField;
import crivia.ecp.a.ECC;

public class BankArea extends ECC {
	
	public static void main(String[] args) {
		refreshDBIandFields(BankArea.class);
		System.out.println(new BankArea().tableName());
	}

	@Override
	public String tableName() {
		return "("
+"select s.CITY_CODE id"
+",(case"
+" when s.CITY_CODE=s.PROVINCE_CODE then concat(s.PROVINCE_NAME,s.PARENT_NAME)"
+" when s.CITY_CODE=s.PARENT_CODE then s.PARENT_NAME"
+" else s.CITY_NAME end) n"
+",(case"
+" when s.CITY_CODE=s.PROVINCE_CODE then -1"
+" when s.CITY_CODE=s.PARENT_CODE then s.PROVINCE_CODE "
+" else s.PARENT_CODE end) p"
+" from cd_city s )";
	}

	@Override
	public void keyLinks(List<KeyLink> keyLinks) {
		keyLinks.add(ValueField.c2d("name","n").setTipText("名称"));
		keyLinks.add(SubCarry.c2d("parent",BankArea.class,"p").setTipText("上级区域"));
		
		keyLinks.add(Collection.targetOwn("subs", BankArea.class, "p").setTipText("子区域"));
	}


	
	
	//XXX DBI Begin.
	public enum Keys{
		/** <b>名称</b> */name("name","n")
		,/** <b>上级区域</b> */parent("parent","p")
		,/** <b>子区域</b> */subs("subs","p/id")
		;
		private String _dbKey;
		private String _key;
		Keys(String key,String dbKey){_key=key;_dbKey=dbKey;}
		@Override
		public String toString(){return _dbKey;}
		/**  <b>Key</b> in <b>Carry</b>. */
		public String key(){return _key;}
	}
	private static com.westvalley.tempus.carry.fncl.BankArea dbi;
	public synchronized static void initDBI(){if(null!=dbi)return;dbi=new com.westvalley.tempus.carry.fncl.BankArea();}
	/**  <b>DB Table</b> of <b>Carry</b>. */
	public static String table(){initDBI();return dbi.tableName();}
	/**  <b>Primary Key</b> of <b>Carry</b>. */
	public static String pk(){initDBI();return dbi.pk4db();}
	/**  <b>Relation SQL</b> of <b>Relation Key</b>. */
	public static String rSQL(com.westvalley.tempus.carry.fncl.BankArea.Keys k,String t){initDBI();return dbi.relationSQL(k.key(),t);}
	//XXX DBI End.



	

	

	// XXX Fields Begin.



	/** ...(Miss Tip Text.) */
	private java.lang.String id;
	/** Getter for ...(Miss Tip Text.) */
	public java.lang.String getId(){
		return id;
	}
	/** Setter for ...(Miss Tip Text.) */
	public com.westvalley.tempus.carry.fncl.BankArea setId(java.lang.String id){
		this.id = id;
		return this;
	}


	/** <b>名称</b> */
	private java.lang.String name;
	/** Getter for <b>名称</b> */
	public java.lang.String getName(){
		return name;
	}
	/** Setter for <b>名称</b> */
	public com.westvalley.tempus.carry.fncl.BankArea setName(java.lang.String name){
		this.name = name;
		return this;
	}


	/** <b>上级区域</b> */
	private com.westvalley.tempus.carry.fncl.BankArea parent;
	/** Getter for <b>上级区域</b> */
	public com.westvalley.tempus.carry.fncl.BankArea getParent(){
		if (null == parent){
			parent = new com.westvalley.tempus.carry.fncl.BankArea();
		}
		return parent;
	}
	/** Setter for <b>上级区域</b> */
	public com.westvalley.tempus.carry.fncl.BankArea setParent(com.westvalley.tempus.carry.fncl.BankArea parent){
		this.parent = parent;
		return this;
	}


	/** <b>子区域</b> */
	private java.util.List<com.westvalley.tempus.carry.fncl.BankArea> subs;
	/** Getter for <b>子区域</b> */
	public java.util.List<com.westvalley.tempus.carry.fncl.BankArea> getSubs(){
		return subs;
	}
	/** Setter for <b>子区域</b> */
	public com.westvalley.tempus.carry.fncl.BankArea setSubs(java.util.List<com.westvalley.tempus.carry.fncl.BankArea> subs){
		this.subs = subs;
		return this;
	}


	// XXX Fields End.

}