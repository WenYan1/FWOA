package com.westvalley.tempus.carry.fncl;

import java.util.List;

import crivia.c.a.KeyLink;
import crivia.c.carry.ValueField;
import crivia.ecp.a.ECC;

public class EAProjectSegment extends ECC {
	
	public static void main(String[] args) {
		
		refreshDBIandFields(EAProjectSegment.class);
		printTableCreater4Oracle(EAProjectSegment.class);
	}

	@Override
	public String tableName() {
		
		return "WV_FNCL_EA_ProjectSegment";
	}

	@Override
	public void keyLinks(List<KeyLink> keyLinks) {
		keyLinks.add(ValueField.c2d("pscode").setTipText("项目段编码"));
		keyLinks.add(ValueField.c2d("psname").setTipText("项目段名称"));
		keyLinks.add(ValueField.c2d("cpcode").setTipText("公司编码"));
	}


	
	//XXX DBI Begin.
	public enum Keys{
		/** <b>项目段编码</b> */pscode("pscode","pscode")
		,/** <b>项目段名称</b> */psname("psname","psname")
		,/** <b>公司编码</b> */cpcode("cpcode","cpcode")
		;
		private String _dbKey;
		private String _key;
		Keys(String key,String dbKey){_key=key;_dbKey=dbKey;}
		@Override
		public String toString(){return _dbKey;}
		/**  <b>Key</b> in <b>Carry</b>. */
		public String key(){return _key;}
	}
	private static com.westvalley.tempus.carry.fncl.EAProjectSegment dbi;
	public synchronized static void initDBI(){if(null!=dbi)return;dbi=new com.westvalley.tempus.carry.fncl.EAProjectSegment();}
	/**  <b>DB Table</b> of <b>Carry</b>. */
	public static String table(){initDBI();return dbi.tableName();}
	/**  <b>Primary Key</b> of <b>Carry</b>. */
	public static String pk(){initDBI();return dbi.pk4db();}
	/**  <b>Relation SQL</b> of <b>Relation Key</b>. */
	public static String rSQL(com.westvalley.tempus.carry.fncl.EAProjectSegment.Keys k,String t){initDBI();return dbi.relationSQL(k.key(),t);}
	//XXX DBI End.



	

	// XXX Fields Begin.



	/** ...(Miss Tip Text.) */
	private java.lang.String id;
	/** Getter for ...(Miss Tip Text.) */
	public java.lang.String getId(){
		return id;
	}
	/** Setter for ...(Miss Tip Text.) */
	public com.westvalley.tempus.carry.fncl.EAProjectSegment setId(java.lang.String id){
		this.id = id;
		return this;
	}


	/** <b>项目段编码</b> */
	private java.lang.String pscode;
	/** Getter for <b>项目段编码</b> */
	public java.lang.String getPscode(){
		return pscode;
	}
	/** Setter for <b>项目段编码</b> */
	public com.westvalley.tempus.carry.fncl.EAProjectSegment setPscode(java.lang.String pscode){
		this.pscode = pscode;
		return this;
	}


	/** <b>项目段名称</b> */
	private java.lang.String psname;
	/** Getter for <b>项目段名称</b> */
	public java.lang.String getPsname(){
		return psname;
	}
	/** Setter for <b>项目段名称</b> */
	public com.westvalley.tempus.carry.fncl.EAProjectSegment setPsname(java.lang.String psname){
		this.psname = psname;
		return this;
	}


	/** <b>公司编码</b> */
	private java.lang.String cpcode;
	/** Getter for <b>公司编码</b> */
	public java.lang.String getCpcode(){
		return cpcode;
	}
	/** Setter for <b>公司编码</b> */
	public com.westvalley.tempus.carry.fncl.EAProjectSegment setCpcode(java.lang.String cpcode){
		this.cpcode = cpcode;
		return this;
	}


	// XXX Fields End.

}