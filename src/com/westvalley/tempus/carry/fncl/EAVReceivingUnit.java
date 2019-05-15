package com.westvalley.tempus.carry.fncl;

import java.util.List;

import crivia.c.a.KeyLink;
import crivia.c.carry.ValueField;
import crivia.ecp.a.ECC;
/**收款单位*/
public class EAVReceivingUnit extends ECC{
	
	public static void main(String[] args) {
		refreshDBIandFields(EAVReceivingUnit.class);
		printTableCreater4Oracle(EAVReceivingUnit.class);
		
	}

	@Override
	public String tableName() {
		return "WV_FNCL_EA_V_ReceivingUnit";
	}

	@Override
	public void keyLinks(List<KeyLink> keyLinks) {
		keyLinks.add(ValueField.c2d("code").setTipText("收款单位编码"));
		keyLinks.add(ValueField.c2d("name").setTipText("收款单位名称"));
		keyLinks.add(ValueField.c2d("segment1").setTipText("供应商编号"));
		keyLinks.add(ValueField.c2d("company").setTipText("公司名称"));
		keyLinks.add(ValueField.c2d("combinationCode").setTipText("组合编码"));
	}




	



	//XXX DBI Begin.
	public enum Keys{
		/** <b>收款单位编码</b> */code("code","code")
		,/** <b>收款单位名称</b> */name("name","name")
		,/** <b>供应商编号</b> */segment1("segment1","segment1")
		,/** <b>公司名称</b> */company("company","company")
		,/** <b>组合编码</b> */combinationCode("combinationCode","combinationCode")
		;
		private String _dbKey;
		private String _key;
		Keys(String key,String dbKey){_key=key;_dbKey=dbKey;}
		@Override
		public String toString(){return _dbKey;}
		/**  <b>Key</b> in <b>Carry</b>. */
		public String key(){return _key;}
	}
	private static com.westvalley.tempus.carry.fncl.EAVReceivingUnit dbi;
	public synchronized static void initDBI(){if(null!=dbi)return;dbi=new com.westvalley.tempus.carry.fncl.EAVReceivingUnit();}
	/**  <b>DB Table</b> of <b>Carry</b>. */
	public static String table(){initDBI();return dbi.tableName();}
	/**  <b>Primary Key</b> of <b>Carry</b>. */
	public static String pk(){initDBI();return dbi.pk4db();}
	/**  <b>Relation SQL</b> of <b>Relation Key</b>. */
	public static String rSQL(com.westvalley.tempus.carry.fncl.EAVReceivingUnit.Keys k,String t){initDBI();return dbi.relationSQL(k.key(),t);}
	//XXX DBI End.



	// XXX Fields Begin.



	/** ...(Miss Tip Text.) */
	private java.lang.String id;
	/** Getter for ...(Miss Tip Text.) */
	public java.lang.String getId(){
		return id;
	}
	/** Setter for ...(Miss Tip Text.) */
	public com.westvalley.tempus.carry.fncl.EAVReceivingUnit setId(java.lang.String id){
		this.id = id;
		return this;
	}


	/** <b>收款单位编码</b> */
	private java.lang.String code;
	/** Getter for <b>收款单位编码</b> */
	public java.lang.String getCode(){
		return code;
	}
	/** Setter for <b>收款单位编码</b> */
	public com.westvalley.tempus.carry.fncl.EAVReceivingUnit setCode(java.lang.String code){
		this.code = code;
		return this;
	}


	/** <b>收款单位名称</b> */
	private java.lang.String name;
	/** Getter for <b>收款单位名称</b> */
	public java.lang.String getName(){
		return name;
	}
	/** Setter for <b>收款单位名称</b> */
	public com.westvalley.tempus.carry.fncl.EAVReceivingUnit setName(java.lang.String name){
		this.name = name;
		return this;
	}


	/** <b>供应商编号</b> */
	private java.lang.String segment1;
	/** Getter for <b>供应商编号</b> */
	public java.lang.String getSegment1(){
		return segment1;
	}
	/** Setter for <b>供应商编号</b> */
	public com.westvalley.tempus.carry.fncl.EAVReceivingUnit setSegment1(java.lang.String segment1){
		this.segment1 = segment1;
		return this;
	}


	/** <b>公司名称</b> */
	private java.lang.String company;
	/** Getter for <b>公司名称</b> */
	public java.lang.String getCompany(){
		return company;
	}
	/** Setter for <b>公司名称</b> */
	public com.westvalley.tempus.carry.fncl.EAVReceivingUnit setCompany(java.lang.String company){
		this.company = company;
		return this;
	}


	/** <b>组合编码</b> */
	private java.lang.String combinationCode;
	/** Getter for <b>组合编码</b> */
	public java.lang.String getCombinationCode(){
		return combinationCode;
	}
	/** Setter for <b>组合编码</b> */
	public com.westvalley.tempus.carry.fncl.EAVReceivingUnit setCombinationCode(java.lang.String combinationCode){
		this.combinationCode = combinationCode;
		return this;
	}


	// XXX Fields End.

}