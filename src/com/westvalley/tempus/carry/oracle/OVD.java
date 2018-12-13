package com.westvalley.tempus.carry.oracle;

import java.util.List;

import crivia.c.a.KeyLink;
import crivia.c.carry.ValueField;
import crivia.ecp.a.ECC;

public class OVD extends ECC {
	
	public static void main(String[] args) {
		refreshDBIandFields(OVD.class);
	}
	
	
	@Override
	public ValueField primaryKey() {
		return ValueField.c2d("id","fid");
	}

	@Override
	public String tableName() {
		return "tb_fvoa_reimbursement_entry";
	}

	@Override
	public void keyLinks(List<KeyLink> keyLinks) {
		keyLinks.add(ValueField.c2d("fparentID"));
		keyLinks.add(ValueField.c2d("fseq",double.class).setTipText("顺序"));
		keyLinks.add(ValueField.c2d("famount",double.class).setTipText("金额"));
		keyLinks.add(ValueField.c2d("fcontent").setTipText("摘要"));
		keyLinks.add(ValueField.c2d("fcostcategory").setTipText("科目ID"));
		keyLinks.add(ValueField.c2d("fcostcenter").setTipText("成本中心ID"));
		keyLinks.add(ValueField.c2d("fproject").setTipText("项目段ID"));
		keyLinks.add(ValueField.c2d("fassociatedUnit").setTipText("关联单位ID"));
	}


	
	
	


	//XXX DBI Begin.
	public enum Keys{
		fparentID("fparentID","fparentID")
		,/** <b>顺序</b> */fseq("fseq","fseq")
		,/** <b>金额</b> */famount("famount","famount")
		,/** <b>摘要</b> */fcontent("fcontent","fcontent")
		,/** <b>科目ID</b> */fcostcategory("fcostcategory","fcostcategory")
		,/** <b>成本中心ID</b> */fcostcenter("fcostcenter","fcostcenter")
		,/** <b>项目段ID</b> */fproject("fproject","fproject")
		,/** <b>关联单位ID</b> */fassociatedUnit("fassociatedUnit","fassociatedUnit")
		;
		private String _dbKey;
		private String _key;
		Keys(String key,String dbKey){_key=key;_dbKey=dbKey;}
		@Override
		public String toString(){return _dbKey;}
		/**  <b>Key</b> in <b>Carry</b>. */
		public String key(){return _key;}
	}
	private static com.westvalley.tempus.carry.oracle.OVD dbi;
	public synchronized static void initDBI(){if(null!=dbi)return;dbi=new com.westvalley.tempus.carry.oracle.OVD();}
	/**  <b>DB Table</b> of <b>Carry</b>. */
	public static String table(){initDBI();return dbi.tableName();}
	/**  <b>Primary Key</b> of <b>Carry</b>. */
	public static String pk(){initDBI();return dbi.pk4db();}
	/**  <b>Relation SQL</b> of <b>Relation Key</b>. */
	public static String rSQL(com.westvalley.tempus.carry.oracle.OVD.Keys k,String t){initDBI();return dbi.relationSQL(k.key(),t);}
	//XXX DBI End.



	// XXX Fields Begin.



	/** ...(Miss Tip Text.) */
	private java.lang.String id;
	/** Getter for ...(Miss Tip Text.) */
	public java.lang.String getId(){
		return id;
	}
	/** Setter for ...(Miss Tip Text.) */
	public com.westvalley.tempus.carry.oracle.OVD setId(java.lang.String id){
		this.id = id;
		return this;
	}


	/** ...(Miss Tip Text.) */
	private java.lang.String fparentID;
	/** Getter for ...(Miss Tip Text.) */
	public java.lang.String getFparentID(){
		return fparentID;
	}
	/** Setter for ...(Miss Tip Text.) */
	public com.westvalley.tempus.carry.oracle.OVD setFparentID(java.lang.String fparentID){
		this.fparentID = fparentID;
		return this;
	}


	/** <b>顺序</b> */
	private double fseq;
	/** Getter for <b>顺序</b> */
	public double getFseq(){
		return fseq;
	}
	/** Setter for <b>顺序</b> */
	public com.westvalley.tempus.carry.oracle.OVD setFseq(double fseq){
		this.fseq = fseq;
		return this;
	}


	/** <b>金额</b> */
	private double famount;
	/** Getter for <b>金额</b> */
	public double getFamount(){
		return famount;
	}
	/** Setter for <b>金额</b> */
	public com.westvalley.tempus.carry.oracle.OVD setFamount(double famount){
		this.famount = famount;
		return this;
	}


	/** <b>摘要</b> */
	private java.lang.String fcontent;
	/** Getter for <b>摘要</b> */
	public java.lang.String getFcontent(){
		return fcontent;
	}
	/** Setter for <b>摘要</b> */
	public com.westvalley.tempus.carry.oracle.OVD setFcontent(java.lang.String fcontent){
		this.fcontent = fcontent;
		return this;
	}


	/** <b>科目ID</b> */
	private java.lang.String fcostcategory;
	/** Getter for <b>科目ID</b> */
	public java.lang.String getFcostcategory(){
		return fcostcategory;
	}
	/** Setter for <b>科目ID</b> */
	public com.westvalley.tempus.carry.oracle.OVD setFcostcategory(java.lang.String fcostcategory){
		this.fcostcategory = fcostcategory;
		return this;
	}


	/** <b>成本中心ID</b> */
	private java.lang.String fcostcenter;
	/** Getter for <b>成本中心ID</b> */
	public java.lang.String getFcostcenter(){
		return fcostcenter;
	}
	/** Setter for <b>成本中心ID</b> */
	public com.westvalley.tempus.carry.oracle.OVD setFcostcenter(java.lang.String fcostcenter){
		this.fcostcenter = fcostcenter;
		return this;
	}


	/** <b>项目段ID</b> */
	private java.lang.String fproject;
	/** Getter for <b>项目段ID</b> */
	public java.lang.String getFproject(){
		return fproject;
	}
	/** Setter for <b>项目段ID</b> */
	public com.westvalley.tempus.carry.oracle.OVD setFproject(java.lang.String fproject){
		this.fproject = fproject;
		return this;
	}


	/** <b>关联单位ID</b> */
	private java.lang.String fassociatedUnit;
	/** Getter for <b>关联单位ID</b> */
	public java.lang.String getFassociatedUnit(){
		return fassociatedUnit;
	}
	/** Setter for <b>关联单位ID</b> */
	public com.westvalley.tempus.carry.oracle.OVD setFassociatedUnit(java.lang.String fassociatedUnit){
		this.fassociatedUnit = fassociatedUnit;
		return this;
	}


	// XXX Fields End.

}