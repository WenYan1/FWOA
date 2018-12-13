package com.westvalley.tempus.carry.fncl;

import java.util.List;

import crivia.c.a.KeyLink;
import crivia.c.carry.RelationKey;
import crivia.c.carry.ValueField;
import crivia.ecp.a.ECC;
import crivia.ecp.carry.ec.hrm.HR;
/**
 * 费用报销公司<br>
 * 管理数据库字段映射等相关信息<br>
 * (实体类)
 */
public class EACpy extends ECC {
	
	public static void main(String[] args) {
		refreshDBIandFields(EACpy.class);
//		System.out.println("drop table "+EACpy.table());
//		printTableCreater4Oracle(EACpy.class);
	}

	//表名
	@Override
	public String tableName() {
		return "WV_FNCL_EA_Company";
	}

	//字段映射
	@Override
	public void keyLinks(List<KeyLink> keyLinks) {
		//Value Field 表示真正存储在数据库中的字段
		keyLinks.add(ValueField.c2d("name").setTipText("名称"));
		keyLinks.add(ValueField.c2d("code").setTipText("编码"));
		keyLinks.add(ValueField.c2d("paymentOperator").setTipText("付款操作人"));
		keyLinks.add(RelationKey.c2d("paymentOperatorName","paymentOperator"
				,HR.table(),HR.Keys.lastName,HR.pk())
			.relationMultiByComma()
			.setTipText("付款操作人名称"));
		keyLinks.add(ValueField.c2d("paymentSearchOperator").setTipText("付款查询操作人"));
		keyLinks.add(RelationKey.c2d("paymentSearchOperatorName","paymentSearchOperator"
				,HR.table(),HR.Keys.lastName,HR.pk())
			.relationMultiByComma()
			.setTipText("付款查询操作人名称"));
	}


	
	
	
	//XXX DBI Begin.
	public enum Keys{
		/** <b>名称</b> */name("name","name")
		,/** <b>编码</b> */code("code","code")
		,/** <b>付款操作人</b> */paymentOperator("paymentOperator","paymentOperator")
		,/** <b>付款操作人名称</b> */paymentOperatorName("paymentOperatorName","lastName")
		,/** <b>付款查询操作人</b> */paymentSearchOperator("paymentSearchOperator","paymentSearchOperator")
		,/** <b>付款查询操作人名称</b> */paymentSearchOperatorName("paymentSearchOperatorName","lastName")
		;
		private String _dbKey;
		private String _key;
		Keys(String key,String dbKey){_key=key;_dbKey=dbKey;}
		@Override
		public String toString(){return _dbKey;}
		/**  <b>Key</b> in <b>Carry</b>. */
		public String key(){return _key;}
	}
	private static com.westvalley.tempus.carry.fncl.EACpy dbi;
	public synchronized static void initDBI(){if(null!=dbi)return;dbi=new com.westvalley.tempus.carry.fncl.EACpy();}
	/**  <b>DB Table</b> of <b>Carry</b>. */
	public static String table(){initDBI();return dbi.tableName();}
	/**  <b>Primary Key</b> of <b>Carry</b>. */
	public static String pk(){initDBI();return dbi.pk4db();}
	/**  <b>Relation SQL</b> of <b>Relation Key</b>. */
	public static String rSQL(com.westvalley.tempus.carry.fncl.EACpy.Keys k,String t){initDBI();return dbi.relationSQL(k.key(),t);}
	//XXX DBI End.



	

	

	

	// XXX Fields Begin.



	/** ...(Miss Tip Text.) */
	private java.lang.String id;
	/** Getter for ...(Miss Tip Text.) */
	public java.lang.String getId(){
		return id;
	}
	/** Setter for ...(Miss Tip Text.) */
	public com.westvalley.tempus.carry.fncl.EACpy setId(java.lang.String id){
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
	public com.westvalley.tempus.carry.fncl.EACpy setName(java.lang.String name){
		this.name = name;
		return this;
	}


	/** <b>编码</b> */
	private java.lang.String code;
	/** Getter for <b>编码</b> */
	public java.lang.String getCode(){
		return code;
	}
	/** Setter for <b>编码</b> */
	public com.westvalley.tempus.carry.fncl.EACpy setCode(java.lang.String code){
		this.code = code;
		return this;
	}


	/** <b>付款操作人</b> */
	private java.lang.String paymentOperator;
	/** Getter for <b>付款操作人</b> */
	public java.lang.String getPaymentOperator(){
		return paymentOperator;
	}
	/** Setter for <b>付款操作人</b> */
	public com.westvalley.tempus.carry.fncl.EACpy setPaymentOperator(java.lang.String paymentOperator){
		this.paymentOperator = paymentOperator;
		return this;
	}


	/** <b>付款操作人名称</b> */
	private java.lang.String paymentOperatorName;
	/** Getter for <b>付款操作人名称</b> */
	public java.lang.String getPaymentOperatorName(){
		return paymentOperatorName;
	}
	/** Setter for <b>付款操作人名称</b> */
	public com.westvalley.tempus.carry.fncl.EACpy setPaymentOperatorName(java.lang.String paymentOperatorName){
		this.paymentOperatorName = paymentOperatorName;
		return this;
	}


	/** <b>付款查询操作人</b> */
	private java.lang.String paymentSearchOperator;
	/** Getter for <b>付款查询操作人</b> */
	public java.lang.String getPaymentSearchOperator(){
		return paymentSearchOperator;
	}
	/** Setter for <b>付款查询操作人</b> */
	public com.westvalley.tempus.carry.fncl.EACpy setPaymentSearchOperator(java.lang.String paymentSearchOperator){
		this.paymentSearchOperator = paymentSearchOperator;
		return this;
	}


	/** <b>付款查询操作人名称</b> */
	private java.lang.String paymentSearchOperatorName;
	/** Getter for <b>付款查询操作人名称</b> */
	public java.lang.String getPaymentSearchOperatorName(){
		return paymentSearchOperatorName;
	}
	/** Setter for <b>付款查询操作人名称</b> */
	public com.westvalley.tempus.carry.fncl.EACpy setPaymentSearchOperatorName(java.lang.String paymentSearchOperatorName){
		this.paymentSearchOperatorName = paymentSearchOperatorName;
		return this;
	}


	// XXX Fields End.

}