package com.westvalley.tempus.carry.fncl;

import java.util.List;

import crivia.c.a.KeyLink;
import crivia.c.carry.ValueField;
import crivia.ecp.a.ECC;
/*成本中心*/
public class EACostCenter extends ECC{
	
	public static void main(String[] args) {
		//刷新字段信息
		refreshDBIandFields(EACostCenter.class);
		//打印建表语句
		printTableCreater4Oracle(EACostCenter.class);
		
	}

	@Override
	public String tableName() {
		
		return "WV_FNCL_EA_CostCenter";
	}

	@Override
	public void keyLinks(List<KeyLink> keyLinks) {
		keyLinks.add(ValueField.c2d("code").setTipText("成本中心"));
		keyLinks.add(ValueField.c2d("description").setTipText("描述"));
		keyLinks.add(ValueField.c2d("cpCode").setTipText("公司编码"));
		
	}

	

	


	

	
	
	//XXX DBI Begin.
	public enum Keys{
		/** <b>成本中心</b> */code("code","code")
		,/** <b>描述</b> */description("description","description")
		,/** <b>公司编码</b> */cpCode("cpCode","cpCode")
		;
		private String _dbKey;
		private String _key;
		Keys(String key,String dbKey){_key=key;_dbKey=dbKey;}
		@Override
		public String toString(){return _dbKey;}
		/**  <b>Key</b> in <b>Carry</b>. */
		public String key(){return _key;}
	}
	private static com.westvalley.tempus.carry.fncl.EACostCenter dbi;
	public synchronized static void initDBI(){if(null!=dbi)return;dbi=new com.westvalley.tempus.carry.fncl.EACostCenter();}
	/**  <b>DB Table</b> of <b>Carry</b>. */
	public static String table(){initDBI();return dbi.tableName();}
	/**  <b>Primary Key</b> of <b>Carry</b>. */
	public static String pk(){initDBI();return dbi.pk4db();}
	/**  <b>Relation SQL</b> of <b>Relation Key</b>. */
	public static String rSQL(com.westvalley.tempus.carry.fncl.EACostCenter.Keys k,String t){initDBI();return dbi.relationSQL(k.key(),t);}
	//XXX DBI End.



	

	

	// XXX Fields Begin.



	/** ...(Miss Tip Text.) */
	private java.lang.String id;
	/** Getter for ...(Miss Tip Text.) */
	public java.lang.String getId(){
		return id;
	}
	/** Setter for ...(Miss Tip Text.) */
	public com.westvalley.tempus.carry.fncl.EACostCenter setId(java.lang.String id){
		this.id = id;
		return this;
	}


	/** <b>成本中心</b> */
	private java.lang.String code;
	/** Getter for <b>成本中心</b> */
	public java.lang.String getCode(){
		return code;
	}
	/** Setter for <b>成本中心</b> */
	public com.westvalley.tempus.carry.fncl.EACostCenter setCode(java.lang.String code){
		this.code = code;
		return this;
	}


	/** <b>描述</b> */
	private java.lang.String description;
	/** Getter for <b>描述</b> */
	public java.lang.String getDescription(){
		return description;
	}
	/** Setter for <b>描述</b> */
	public com.westvalley.tempus.carry.fncl.EACostCenter setDescription(java.lang.String description){
		this.description = description;
		return this;
	}


	/** <b>公司编码</b> */
	private java.lang.String cpCode;
	/** Getter for <b>公司编码</b> */
	public java.lang.String getCpCode(){
		return cpCode;
	}
	/** Setter for <b>公司编码</b> */
	public com.westvalley.tempus.carry.fncl.EACostCenter setCpCode(java.lang.String cpCode){
		this.cpCode = cpCode;
		return this;
	}


	// XXX Fields End.

}