package com.westvalley.tempus.carry.fncl;

import java.util.List;

import crivia.c.a.KeyLink;
import crivia.c.carry.ValueField;
import crivia.ecp.a.ECC;
/*关联单位*/
public class EAAssociatedUnit extends ECC{
	
	public static void main(String[] args) {
		//刷新字段信息
		refreshDBIandFields(EAAssociatedUnit.class);
		//打印建表语句
		printTableCreater4Oracle(EAAssociatedUnit.class);
		
	}

	@Override
	public String tableName() {
		
		return "WV_FNCL_EA_Associated_Unit";
	}

	@Override
	public void keyLinks(List<KeyLink> keyLinks) {
		keyLinks.add(ValueField.c2d("code").setTipText("关联单位编码"));
		keyLinks.add(ValueField.c2d("name").setTipText("关联单位名称"));
		
	}

	

	


	

	
	
	
	//XXX DBI Begin.
	public enum Keys{
		/** <b>关联单位编码</b> */code("code","code")
		,/** <b>关联单位名称</b> */name("name","name")
		;
		private String _dbKey;
		private String _key;
		Keys(String key,String dbKey){_key=key;_dbKey=dbKey;}
		@Override
		public String toString(){return _dbKey;}
		/**  <b>Key</b> in <b>Carry</b>. */
		public String key(){return _key;}
	}
	private static com.westvalley.tempus.carry.fncl.EAAssociatedUnit dbi;
	public synchronized static void initDBI(){if(null!=dbi)return;dbi=new com.westvalley.tempus.carry.fncl.EAAssociatedUnit();}
	/**  <b>DB Table</b> of <b>Carry</b>. */
	public static String table(){initDBI();return dbi.tableName();}
	/**  <b>Primary Key</b> of <b>Carry</b>. */
	public static String pk(){initDBI();return dbi.pk4db();}
	/**  <b>Relation SQL</b> of <b>Relation Key</b>. */
	public static String rSQL(com.westvalley.tempus.carry.fncl.EAAssociatedUnit.Keys k,String t){initDBI();return dbi.relationSQL(k.key(),t);}
	//XXX DBI End.



	

	

	

	// XXX Fields Begin.



	/** ...(Miss Tip Text.) */
	private java.lang.String id;
	/** Getter for ...(Miss Tip Text.) */
	public java.lang.String getId(){
		return id;
	}
	/** Setter for ...(Miss Tip Text.) */
	public com.westvalley.tempus.carry.fncl.EAAssociatedUnit setId(java.lang.String id){
		this.id = id;
		return this;
	}


	/** <b>关联单位编码</b> */
	private java.lang.String code;
	/** Getter for <b>关联单位编码</b> */
	public java.lang.String getCode(){
		return code;
	}
	/** Setter for <b>关联单位编码</b> */
	public com.westvalley.tempus.carry.fncl.EAAssociatedUnit setCode(java.lang.String code){
		this.code = code;
		return this;
	}


	/** <b>关联单位名称</b> */
	private java.lang.String name;
	/** Getter for <b>关联单位名称</b> */
	public java.lang.String getName(){
		return name;
	}
	/** Setter for <b>关联单位名称</b> */
	public com.westvalley.tempus.carry.fncl.EAAssociatedUnit setName(java.lang.String name){
		this.name = name;
		return this;
	}


	// XXX Fields End.

}