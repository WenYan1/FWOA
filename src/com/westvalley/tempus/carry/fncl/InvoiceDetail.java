package com.westvalley.tempus.carry.fncl;

import java.util.List;

import crivia.c.a.KeyLink;
import crivia.c.carry.RelationKey;
import crivia.c.carry.SubCarry;
import crivia.c.carry.ValueField;
import crivia.ecp.a.ECC;

public class InvoiceDetail extends ECC {
	
	public static void main(String[] args) {
		refreshDBIandFields(InvoiceDetail.class);
		printTableCreater4Oracle(InvoiceDetail.class);
	}

	@Override
	public String tableName() {
		return "WV_FNCL_PD_INV";
	}

	@Override
	public void keyLinks(List<KeyLink> keyLinks) {
		keyLinks.add(SubCarry.c2d("pr", PR.class).setTipText("关联付款申请"));
		keyLinks.add(ValueField.c2d("dspOrder",double.class).setTipText("显示顺序"));
		keyLinks.add(ValueField.c2d("money",double.class).setTipText("金额"));
		keyLinks.add(ValueField.c2d("desc","desc_").setPlaces(4000).setTipText("费用描述"));
		keyLinks.add(ValueField.c2d("subjectID").setTipText("科目ID"));
		keyLinks.add(RelationKey.c2d("subjectName", "subjectID"
			, EACostCategory.table(), EACostCategory.Keys.costname, ""+EACostCategory.Keys.costvalue).setTipText("科目名称"));
		keyLinks.add(RelationKey.c2d("subjectCode", "subjectID"
				, EACostCategory.table(), EACostCategory.Keys.costvalue, ""+EACostCategory.Keys.costvalue).setTipText("科目编码"));
		keyLinks.add(ValueField.c2d("costCenterID").setTipText("成本中心ID"));
		keyLinks.add(RelationKey.c2d("costCenterName", "costCenterID"
				, EACostCenter.table(), EACostCenter.Keys.description, ""+EACostCenter.Keys.code).setTipText("成本中心名称"));
			keyLinks.add(RelationKey.c2d("costCenterCode", "costCenterID"
					, EACostCenter.table(), EACostCenter.Keys.code, ""+EACostCenter.Keys.code).setTipText("成本中心编码"));
		keyLinks.add(ValueField.c2d("projectID").setTipText("项目段ID"));
		keyLinks.add(RelationKey.c2d("projectName", "projectID"
				, EAProjectSegment.table(), EAProjectSegment.Keys.psname, ""+EAProjectSegment.Keys.pscode).setTipText("项目段名称"));
			keyLinks.add(RelationKey.c2d("projectCode", "projectID"
					, EAProjectSegment.table(), EAProjectSegment.Keys.pscode, ""+EAProjectSegment.Keys.pscode).setTipText("项目段编码"));
		keyLinks.add(ValueField.c2d("associatedUnitID").setTipText("关联单位ID"));
		keyLinks.add(RelationKey.c2d("associatedUnitName", "associatedUnitID"
					, EAAssociatedUnit.table(), EAAssociatedUnit.Keys.name, ""+EAAssociatedUnit.Keys.code).setTipText("关联单位名称"));
			keyLinks.add(RelationKey.c2d("associatedUnitCode", "associatedUnitID"
						, EAAssociatedUnit.table(), EAAssociatedUnit.Keys.code, ""+EAAssociatedUnit.Keys.code).setTipText("关联单位编码"));
	}


	
	
	
	
	
	

	

	
	//XXX DBI Begin.
	public enum Keys{
		/** <b>关联付款申请</b> */pr("pr","pr")
		,/** <b>显示顺序</b> */dspOrder("dspOrder","dspOrder")
		,/** <b>金额</b> */money("money","money")
		,/** <b>费用描述</b> */desc("desc","desc_")
		,/** <b>科目ID</b> */subjectID("subjectID","subjectID")
		,/** <b>科目名称</b> */subjectName("subjectName","costname")
		,/** <b>科目编码</b> */subjectCode("subjectCode","costvalue")
		,/** <b>成本中心ID</b> */costCenterID("costCenterID","costCenterID")
		,/** <b>成本中心名称</b> */costCenterName("costCenterName","description")
		,/** <b>成本中心编码</b> */costCenterCode("costCenterCode","code")
		,/** <b>项目段ID</b> */projectID("projectID","projectID")
		,/** <b>项目段名称</b> */projectName("projectName","psname")
		,/** <b>项目段编码</b> */projectCode("projectCode","pscode")
		,/** <b>关联单位ID</b> */associatedUnitID("associatedUnitID","associatedUnitID")
		,/** <b>关联单位名称</b> */associatedUnitName("associatedUnitName","name")
		,/** <b>关联单位编码</b> */associatedUnitCode("associatedUnitCode","code")
		;
		private String _dbKey;
		private String _key;
		Keys(String key,String dbKey){_key=key;_dbKey=dbKey;}
		@Override
		public String toString(){return _dbKey;}
		/**  <b>Key</b> in <b>Carry</b>. */
		public String key(){return _key;}
	}
	private static com.westvalley.tempus.carry.fncl.InvoiceDetail dbi;
	public synchronized static void initDBI(){if(null!=dbi)return;dbi=new com.westvalley.tempus.carry.fncl.InvoiceDetail();}
	/**  <b>DB Table</b> of <b>Carry</b>. */
	public static String table(){initDBI();return dbi.tableName();}
	/**  <b>Primary Key</b> of <b>Carry</b>. */
	public static String pk(){initDBI();return dbi.pk4db();}
	/**  <b>Relation SQL</b> of <b>Relation Key</b>. */
	public static String rSQL(com.westvalley.tempus.carry.fncl.InvoiceDetail.Keys k,String t){initDBI();return dbi.relationSQL(k.key(),t);}
	//XXX DBI End.



	

	// XXX Fields Begin.



	/** ...(Miss Tip Text.) */
	private java.lang.String id;
	/** Getter for ...(Miss Tip Text.) */
	public java.lang.String getId(){
		return id;
	}
	/** Setter for ...(Miss Tip Text.) */
	public com.westvalley.tempus.carry.fncl.InvoiceDetail setId(java.lang.String id){
		this.id = id;
		return this;
	}


	/** <b>关联付款申请</b> */
	private com.westvalley.tempus.carry.fncl.PR pr;
	/** Getter for <b>关联付款申请</b> */
	public com.westvalley.tempus.carry.fncl.PR getPr(){
		if (null == pr){
			pr = new com.westvalley.tempus.carry.fncl.PR();
		}
		return pr;
	}
	/** Setter for <b>关联付款申请</b> */
	public com.westvalley.tempus.carry.fncl.InvoiceDetail setPr(com.westvalley.tempus.carry.fncl.PR pr){
		this.pr = pr;
		return this;
	}


	/** <b>显示顺序</b> */
	private double dspOrder;
	/** Getter for <b>显示顺序</b> */
	public double getDspOrder(){
		return dspOrder;
	}
	/** Setter for <b>显示顺序</b> */
	public com.westvalley.tempus.carry.fncl.InvoiceDetail setDspOrder(double dspOrder){
		this.dspOrder = dspOrder;
		return this;
	}


	/** <b>金额</b> */
	private double money;
	/** Getter for <b>金额</b> */
	public double getMoney(){
		return money;
	}
	/** Setter for <b>金额</b> */
	public com.westvalley.tempus.carry.fncl.InvoiceDetail setMoney(double money){
		this.money = money;
		return this;
	}


	/** <b>费用描述</b> */
	private java.lang.String desc;
	/** Getter for <b>费用描述</b> */
	public java.lang.String getDesc(){
		return desc;
	}
	/** Setter for <b>费用描述</b> */
	public com.westvalley.tempus.carry.fncl.InvoiceDetail setDesc(java.lang.String desc){
		this.desc = desc;
		return this;
	}


	/** <b>科目ID</b> */
	private java.lang.String subjectID;
	/** Getter for <b>科目ID</b> */
	public java.lang.String getSubjectID(){
		return subjectID;
	}
	/** Setter for <b>科目ID</b> */
	public com.westvalley.tempus.carry.fncl.InvoiceDetail setSubjectID(java.lang.String subjectID){
		this.subjectID = subjectID;
		return this;
	}


	/** <b>科目名称</b> */
	private java.lang.String subjectName;
	/** Getter for <b>科目名称</b> */
	public java.lang.String getSubjectName(){
		return subjectName;
	}
	/** Setter for <b>科目名称</b> */
	public com.westvalley.tempus.carry.fncl.InvoiceDetail setSubjectName(java.lang.String subjectName){
		this.subjectName = subjectName;
		return this;
	}


	/** <b>科目编码</b> */
	private java.lang.String subjectCode;
	/** Getter for <b>科目编码</b> */
	public java.lang.String getSubjectCode(){
		return subjectCode;
	}
	/** Setter for <b>科目编码</b> */
	public com.westvalley.tempus.carry.fncl.InvoiceDetail setSubjectCode(java.lang.String subjectCode){
		this.subjectCode = subjectCode;
		return this;
	}


	/** <b>成本中心ID</b> */
	private java.lang.String costCenterID;
	/** Getter for <b>成本中心ID</b> */
	public java.lang.String getCostCenterID(){
		return costCenterID;
	}
	/** Setter for <b>成本中心ID</b> */
	public com.westvalley.tempus.carry.fncl.InvoiceDetail setCostCenterID(java.lang.String costCenterID){
		this.costCenterID = costCenterID;
		return this;
	}


	/** <b>成本中心名称</b> */
	private java.lang.String costCenterName;
	/** Getter for <b>成本中心名称</b> */
	public java.lang.String getCostCenterName(){
		return costCenterName;
	}
	/** Setter for <b>成本中心名称</b> */
	public com.westvalley.tempus.carry.fncl.InvoiceDetail setCostCenterName(java.lang.String costCenterName){
		this.costCenterName = costCenterName;
		return this;
	}


	/** <b>成本中心编码</b> */
	private java.lang.String costCenterCode;
	/** Getter for <b>成本中心编码</b> */
	public java.lang.String getCostCenterCode(){
		return costCenterCode;
	}
	/** Setter for <b>成本中心编码</b> */
	public com.westvalley.tempus.carry.fncl.InvoiceDetail setCostCenterCode(java.lang.String costCenterCode){
		this.costCenterCode = costCenterCode;
		return this;
	}


	/** <b>项目段ID</b> */
	private java.lang.String projectID;
	/** Getter for <b>项目段ID</b> */
	public java.lang.String getProjectID(){
		return projectID;
	}
	/** Setter for <b>项目段ID</b> */
	public com.westvalley.tempus.carry.fncl.InvoiceDetail setProjectID(java.lang.String projectID){
		this.projectID = projectID;
		return this;
	}


	/** <b>项目段名称</b> */
	private java.lang.String projectName;
	/** Getter for <b>项目段名称</b> */
	public java.lang.String getProjectName(){
		return projectName;
	}
	/** Setter for <b>项目段名称</b> */
	public com.westvalley.tempus.carry.fncl.InvoiceDetail setProjectName(java.lang.String projectName){
		this.projectName = projectName;
		return this;
	}


	/** <b>项目段编码</b> */
	private java.lang.String projectCode;
	/** Getter for <b>项目段编码</b> */
	public java.lang.String getProjectCode(){
		return projectCode;
	}
	/** Setter for <b>项目段编码</b> */
	public com.westvalley.tempus.carry.fncl.InvoiceDetail setProjectCode(java.lang.String projectCode){
		this.projectCode = projectCode;
		return this;
	}


	/** <b>关联单位ID</b> */
	private java.lang.String associatedUnitID;
	/** Getter for <b>关联单位ID</b> */
	public java.lang.String getAssociatedUnitID(){
		return associatedUnitID;
	}
	/** Setter for <b>关联单位ID</b> */
	public com.westvalley.tempus.carry.fncl.InvoiceDetail setAssociatedUnitID(java.lang.String associatedUnitID){
		this.associatedUnitID = associatedUnitID;
		return this;
	}


	/** <b>关联单位名称</b> */
	private java.lang.String associatedUnitName;
	/** Getter for <b>关联单位名称</b> */
	public java.lang.String getAssociatedUnitName(){
		return associatedUnitName;
	}
	/** Setter for <b>关联单位名称</b> */
	public com.westvalley.tempus.carry.fncl.InvoiceDetail setAssociatedUnitName(java.lang.String associatedUnitName){
		this.associatedUnitName = associatedUnitName;
		return this;
	}


	/** <b>关联单位编码</b> */
	private java.lang.String associatedUnitCode;
	/** Getter for <b>关联单位编码</b> */
	public java.lang.String getAssociatedUnitCode(){
		return associatedUnitCode;
	}
	/** Setter for <b>关联单位编码</b> */
	public com.westvalley.tempus.carry.fncl.InvoiceDetail setAssociatedUnitCode(java.lang.String associatedUnitCode){
		this.associatedUnitCode = associatedUnitCode;
		return this;
	}


	// XXX Fields End.

}