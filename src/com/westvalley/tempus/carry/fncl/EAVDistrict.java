package com.westvalley.tempus.carry.fncl;

import java.util.List;

import crivia.c.a.KeyLink;
import crivia.c.carry.ValueField;
import crivia.ecp.a.ECC;
/**城市*/
public class EAVDistrict extends ECC{
	
	public static void main(String[] args) {
		refreshDBIandFields(EAVDistrict.class);
		printTableCreater4Oracle(EAVDistrict.class);
		
	}
	
	

	@Override
	public String tableName() {
		return "WV_FNCL_EA_V_DISTRICT";
	}

	@Override
	public void keyLinks(List<KeyLink> keyLinks) {
		keyLinks.add(ValueField.c2d("district_code").setTipText("市区编码"));
		keyLinks.add(ValueField.c2d("district_name").setTipText("市区名称"));
		keyLinks.add(ValueField.c2d("province_code").setTipText("省份编码"));
		keyLinks.add(ValueField.c2d("province_name").setTipText("省份名称"));
		
	}
	

	




	//XXX DBI Begin.
	public enum Keys{
		/** <b>市区编码</b> */district_code("district_code","district_code")
		,/** <b>市区名称</b> */district_name("district_name","district_name")
		,/** <b>省份编码</b> */province_code("province_code","province_code")
		,/** <b>省份名称</b> */province_name("province_name","province_name")
		;
		private String _dbKey;
		private String _key;
		Keys(String key,String dbKey){_key=key;_dbKey=dbKey;}
		@Override
		public String toString(){return _dbKey;}
		/**  <b>Key</b> in <b>Carry</b>. */
		public String key(){return _key;}
	}
	private static com.westvalley.tempus.carry.fncl.EAVDistrict dbi;
	public synchronized static void initDBI(){if(null!=dbi)return;dbi=new com.westvalley.tempus.carry.fncl.EAVDistrict();}
	/**  <b>DB Table</b> of <b>Carry</b>. */
	public static String table(){initDBI();return dbi.tableName();}
	/**  <b>Primary Key</b> of <b>Carry</b>. */
	public static String pk(){initDBI();return dbi.pk4db();}
	/**  <b>Relation SQL</b> of <b>Relation Key</b>. */
	public static String rSQL(com.westvalley.tempus.carry.fncl.EAVDistrict.Keys k,String t){initDBI();return dbi.relationSQL(k.key(),t);}
	//XXX DBI End.



	// XXX Fields Begin.



	/** ...(Miss Tip Text.) */
	private java.lang.String id;
	/** Getter for ...(Miss Tip Text.) */
	public java.lang.String getId(){
		return id;
	}
	/** Setter for ...(Miss Tip Text.) */
	public com.westvalley.tempus.carry.fncl.EAVDistrict setId(java.lang.String id){
		this.id = id;
		return this;
	}


	/** <b>市区编码</b> */
	private java.lang.String district_code;
	/** Getter for <b>市区编码</b> */
	public java.lang.String getDistrict_code(){
		return district_code;
	}
	/** Setter for <b>市区编码</b> */
	public com.westvalley.tempus.carry.fncl.EAVDistrict setDistrict_code(java.lang.String district_code){
		this.district_code = district_code;
		return this;
	}


	/** <b>市区名称</b> */
	private java.lang.String district_name;
	/** Getter for <b>市区名称</b> */
	public java.lang.String getDistrict_name(){
		return district_name;
	}
	/** Setter for <b>市区名称</b> */
	public com.westvalley.tempus.carry.fncl.EAVDistrict setDistrict_name(java.lang.String district_name){
		this.district_name = district_name;
		return this;
	}


	/** <b>省份编码</b> */
	private java.lang.String province_code;
	/** Getter for <b>省份编码</b> */
	public java.lang.String getProvince_code(){
		return province_code;
	}
	/** Setter for <b>省份编码</b> */
	public com.westvalley.tempus.carry.fncl.EAVDistrict setProvince_code(java.lang.String province_code){
		this.province_code = province_code;
		return this;
	}


	/** <b>省份名称</b> */
	private java.lang.String province_name;
	/** Getter for <b>省份名称</b> */
	public java.lang.String getProvince_name(){
		return province_name;
	}
	/** Setter for <b>省份名称</b> */
	public com.westvalley.tempus.carry.fncl.EAVDistrict setProvince_name(java.lang.String province_name){
		this.province_name = province_name;
		return this;
	}


	// XXX Fields End.

}