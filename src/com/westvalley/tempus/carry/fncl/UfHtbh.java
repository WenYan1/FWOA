package com.westvalley.tempus.carry.fncl;

import java.util.List;

import crivia.c.a.KeyLink;
import crivia.c.carry.ValueField;
import crivia.ecp.a.ECC;

public class UfHtbh extends ECC {
	
	public static void main(String[] args) {
		refreshDBIandFields(UfHtbh.class);
		System.out.println(new UfHtbh().tableName());
	}

	@Override
	public String tableName() {
		return "UF_HTBH";
	}

	@Override
	public void keyLinks(List<KeyLink> keyLinks) {
		keyLinks.add(ValueField.c2d("tb_contract_no").setTipText("合同编号"));
		keyLinks.add(ValueField.c2d("tb_Corporate").setTipText("公司法人"));
		keyLinks.add(ValueField.c2d("tb_department").setTipText("部门"));
	}







	
	//XXX DBI Begin.
	public enum Keys{
		/** <b>合同编号</b> */tb_contract_no("tb_contract_no","tb_contract_no")
		,/** <b>公司法人</b> */tb_Corporate("tb_Corporate","tb_Corporate")
		,/** <b>部门</b> */tb_department("tb_department","tb_department")
		;
		private String _dbKey;
		private String _key;
		Keys(String key,String dbKey){_key=key;_dbKey=dbKey;}
		@Override
		public String toString(){return _dbKey;}
		/**  <b>Key</b> in <b>Carry</b>. */
		public String key(){return _key;}
	}
	private static com.westvalley.tempus.carry.fncl.UfHtbh dbi;
	public synchronized static void initDBI(){if(null!=dbi)return;dbi=new com.westvalley.tempus.carry.fncl.UfHtbh();}
	/**  <b>DB Table</b> of <b>Carry</b>. */
	public static String table(){initDBI();return dbi.tableName();}
	/**  <b>Primary Key</b> of <b>Carry</b>. */
	public static String pk(){initDBI();return dbi.pk4db();}
	/**  <b>Relation SQL</b> of <b>Relation Key</b>. */
	public static String rSQL(com.westvalley.tempus.carry.fncl.UfHtbh.Keys k,String t){initDBI();return dbi.relationSQL(k.key(),t);}
	//XXX DBI End.



	

	// XXX Fields Begin.



	/** ...(Miss Tip Text.) */
	private java.lang.String id;
	/** Getter for ...(Miss Tip Text.) */
	public java.lang.String getId(){
		return id;
	}
	/** Setter for ...(Miss Tip Text.) */
	public com.westvalley.tempus.carry.fncl.UfHtbh setId(java.lang.String id){
		this.id = id;
		return this;
	}


	/** <b>合同编号</b> */
	private java.lang.String tb_contract_no;
	/** Getter for <b>合同编号</b> */
	public java.lang.String getTb_contract_no(){
		return tb_contract_no;
	}
	/** Setter for <b>合同编号</b> */
	public com.westvalley.tempus.carry.fncl.UfHtbh setTb_contract_no(java.lang.String tb_contract_no){
		this.tb_contract_no = tb_contract_no;
		return this;
	}


	/** <b>公司法人</b> */
	private java.lang.String tb_Corporate;
	/** Getter for <b>公司法人</b> */
	public java.lang.String getTb_Corporate(){
		return tb_Corporate;
	}
	/** Setter for <b>公司法人</b> */
	public com.westvalley.tempus.carry.fncl.UfHtbh setTb_Corporate(java.lang.String tb_Corporate){
		this.tb_Corporate = tb_Corporate;
		return this;
	}


	/** <b>部门</b> */
	private java.lang.String tb_department;
	/** Getter for <b>部门</b> */
	public java.lang.String getTb_department(){
		return tb_department;
	}
	/** Setter for <b>部门</b> */
	public com.westvalley.tempus.carry.fncl.UfHtbh setTb_department(java.lang.String tb_department){
		this.tb_department = tb_department;
		return this;
	}


	// XXX Fields End.

}