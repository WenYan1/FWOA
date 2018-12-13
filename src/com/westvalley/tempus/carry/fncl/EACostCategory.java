package com.westvalley.tempus.carry.fncl;

import java.util.List;

import crivia.c.a.KeyLink;
import crivia.c.carry.ValueField;
import crivia.ecp.a.ECC;
/**费用类别*/
public class EACostCategory extends ECC{
	
	public static void main(String[] args) {
		refreshDBIandFields(EACostCategory.class);
		printTableCreater4Oracle(EACostCategory.class);
		
	}
	
	

	@Override
	public String tableName() {
		return "WV_FNCL_EA_CostCategory";
	}

	@Override
	public void keyLinks(List<KeyLink> keyLinks) {
		keyLinks.add(ValueField.c2d("costvalue").setTipText("费用类别编码"));
		keyLinks.add(ValueField.c2d("costname").setTipText("费用类别名称"));
		
	}


	
	//XXX DBI Begin.
	public enum Keys{
		/** <b>费用类别编码</b> */costvalue("costvalue","costvalue")
		,/** <b>费用类别名称</b> */costname("costname","costname")
		;
		private String _dbKey;
		private String _key;
		Keys(String key,String dbKey){_key=key;_dbKey=dbKey;}
		@Override
		public String toString(){return _dbKey;}
		/**  <b>Key</b> in <b>Carry</b>. */
		public String key(){return _key;}
	}
	private static com.westvalley.tempus.carry.fncl.EACostCategory dbi;
	public synchronized static void initDBI(){if(null!=dbi)return;dbi=new com.westvalley.tempus.carry.fncl.EACostCategory();}
	/**  <b>DB Table</b> of <b>Carry</b>. */
	public static String table(){initDBI();return dbi.tableName();}
	/**  <b>Primary Key</b> of <b>Carry</b>. */
	public static String pk(){initDBI();return dbi.pk4db();}
	/**  <b>Relation SQL</b> of <b>Relation Key</b>. */
	public static String rSQL(com.westvalley.tempus.carry.fncl.EACostCategory.Keys k,String t){initDBI();return dbi.relationSQL(k.key(),t);}
	//XXX DBI End.



	

	// XXX Fields Begin.



	/** ...(Miss Tip Text.) */
	private java.lang.String id;
	/** Getter for ...(Miss Tip Text.) */
	public java.lang.String getId(){
		return id;
	}
	/** Setter for ...(Miss Tip Text.) */
	public com.westvalley.tempus.carry.fncl.EACostCategory setId(java.lang.String id){
		this.id = id;
		return this;
	}


	/** <b>费用类别编码</b> */
	private java.lang.String costvalue;
	/** Getter for <b>费用类别编码</b> */
	public java.lang.String getCostvalue(){
		return costvalue;
	}
	/** Setter for <b>费用类别编码</b> */
	public com.westvalley.tempus.carry.fncl.EACostCategory setCostvalue(java.lang.String costvalue){
		this.costvalue = costvalue;
		return this;
	}


	/** <b>费用类别名称</b> */
	private java.lang.String costname;
	/** Getter for <b>费用类别名称</b> */
	public java.lang.String getCostname(){
		return costname;
	}
	/** Setter for <b>费用类别名称</b> */
	public com.westvalley.tempus.carry.fncl.EACostCategory setCostname(java.lang.String costname){
		this.costname = costname;
		return this;
	}


	// XXX Fields End.

}