package com.westvalley.tempus.carry.fncl;

import java.util.List;

import crivia.c.a.KeyLink;
import crivia.c.carry.ValueField;
import crivia.ecp.a.ECC;
/**费用类别*/
public class TbIMSError extends ECC{
	
	public static void main(String[] args) {
		refreshDBIandFields(TbIMSError.class);
		printTableCreater4Oracle(TbIMSError.class);
		
	}
	
	

	@Override
	public String tableName() {
		return "TB_IMS_ERROR";
	}

	@Override
	public void keyLinks(List<KeyLink> keyLinks) {
		keyLinks.add(ValueField.c2d("MSG").setTipText("错误信息"));
		keyLinks.add(ValueField.c2d("STATUS").setTipText("状态"));
		keyLinks.add(ValueField.c2d("REQUEST_ID").setTipText("请求id"));
		keyLinks.add(ValueField.c2d("CREATED_DATE").setTipText("创建时间"));	
	}
	







	//XXX DBI Begin.
	public enum Keys{
		/** <b>错误信息</b> */MSG("MSG","MSG")
		,/** <b>状态</b> */STATUS("STATUS","STATUS")
		,/** <b>请求id</b> */REQUEST_ID("REQUEST_ID","REQUEST_ID")
		,/** <b>创建时间</b> */CREATED_DATE("CREATED_DATE","CREATED_DATE")
		;
		private String _dbKey;
		private String _key;
		Keys(String key,String dbKey){_key=key;_dbKey=dbKey;}
		@Override
		public String toString(){return _dbKey;}
		/**  <b>Key</b> in <b>Carry</b>. */
		public String key(){return _key;}
	}
	private static com.westvalley.tempus.carry.fncl.TbIMSError dbi;
	public synchronized static void initDBI(){if(null!=dbi)return;dbi=new com.westvalley.tempus.carry.fncl.TbIMSError();}
	/**  <b>DB Table</b> of <b>Carry</b>. */
	public static String table(){initDBI();return dbi.tableName();}
	/**  <b>Primary Key</b> of <b>Carry</b>. */
	public static String pk(){initDBI();return dbi.pk4db();}
	/**  <b>Relation SQL</b> of <b>Relation Key</b>. */
	public static String rSQL(com.westvalley.tempus.carry.fncl.TbIMSError.Keys k,String t){initDBI();return dbi.relationSQL(k.key(),t);}
	//XXX DBI End.



	// XXX Fields Begin.



	/** ...(Miss Tip Text.) */
	private java.lang.String id;
	/** Getter for ...(Miss Tip Text.) */
	public java.lang.String getId(){
		return id;
	}
	/** Setter for ...(Miss Tip Text.) */
	public com.westvalley.tempus.carry.fncl.TbIMSError setId(java.lang.String id){
		this.id = id;
		return this;
	}


	/** <b>错误信息</b> */
	private java.lang.String MSG;
	/** Getter for <b>错误信息</b> */
	public java.lang.String getMSG(){
		return MSG;
	}
	/** Setter for <b>错误信息</b> */
	public com.westvalley.tempus.carry.fncl.TbIMSError setMSG(java.lang.String MSG){
		this.MSG = MSG;
		return this;
	}


	/** <b>状态</b> */
	private java.lang.String STATUS;
	/** Getter for <b>状态</b> */
	public java.lang.String getSTATUS(){
		return STATUS;
	}
	/** Setter for <b>状态</b> */
	public com.westvalley.tempus.carry.fncl.TbIMSError setSTATUS(java.lang.String STATUS){
		this.STATUS = STATUS;
		return this;
	}


	/** <b>请求id</b> */
	private java.lang.String REQUEST_ID;
	/** Getter for <b>请求id</b> */
	public java.lang.String getREQUEST_ID(){
		return REQUEST_ID;
	}
	/** Setter for <b>请求id</b> */
	public com.westvalley.tempus.carry.fncl.TbIMSError setREQUEST_ID(java.lang.String REQUEST_ID){
		this.REQUEST_ID = REQUEST_ID;
		return this;
	}


	/** <b>创建时间</b> */
	private java.lang.String CREATED_DATE;
	/** Getter for <b>创建时间</b> */
	public java.lang.String getCREATED_DATE(){
		return CREATED_DATE;
	}
	/** Setter for <b>创建时间</b> */
	public com.westvalley.tempus.carry.fncl.TbIMSError setCREATED_DATE(java.lang.String CREATED_DATE){
		this.CREATED_DATE = CREATED_DATE;
		return this;
	}


	// XXX Fields End.

}