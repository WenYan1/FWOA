package com.westvalley.tempus.page.fncl;


import com.westvalley.tempus.carry.fncl.InvoiceDetail;

import crivia.ecp.carry.eb.TopTitle;
import crivia.ecp.carry.eb.TabPage.Image;
import crivia.ecp.carry.em.EDP;
import crivia.ecp.carry.html.X;
import crivia.ecp.carry.html.form.Form;
import crivia.ecp.carry.tableString.TableSQL;
import crivia.ecp.carry.tableString.TableString;
import crivia.ecp.p.Lister;
import crivia.mvc.carry.Page;

public class InvoiceDetailList extends Lister {
	
	@Override
	protected Page breakPage() {
		if(!rpExist("r"))return Page.redirect(NORIGHT);
		return super.breakPage();
	}
	
	@Override
	protected void selectForm(Form selectForm) {
		selectForm.addHidden("r");
	}

	@Override
	protected TableString table() {
		TableSQL q=new InvoiceDetail().simpleTableSQL(
				InvoiceDetail.Keys.subjectName.key()
				,InvoiceDetail.Keys.subjectCode.key()
				,InvoiceDetail.Keys.costCenterName.key()
				,InvoiceDetail.Keys.costCenterCode.key()
				,InvoiceDetail.Keys.projectName.key()
				,InvoiceDetail.Keys.projectCode.key()
				,InvoiceDetail.Keys.associatedUnitName.key()
				,InvoiceDetail.Keys.associatedUnitCode.key()
				
		);
		q.appendField(","+defaultCMD.specially().moneyFormatFloat2(
				"t."+InvoiceDetail.Keys.money)+" "+InvoiceDetail.Keys.money+"Text");
		q.setOrderBy(InvoiceDetail.Keys.dspOrder);
		q.setOrderByWay("asc");
		
		//限制仅查询当前凭证的分录
		q.addWhere_equals(InvoiceDetail.Keys.pr, rp("r"));
		
		TableString s=new TableString(q);
		s.addTableColumn("显示顺序", InvoiceDetail.Keys.dspOrder);
		s.addTableColumn("摘要", InvoiceDetail.Keys.desc);
		s.addTableColumn("金额", InvoiceDetail.Keys.money+"Text");
		s.addTableColumn("科目名称", InvoiceDetail.Keys.subjectName.key());
		s.addTableColumn("科目编码", InvoiceDetail.Keys.subjectCode.key());
		s.addTableColumn("成本中心名称", InvoiceDetail.Keys.costCenterName.key());
		s.addTableColumn("成本中心编码", InvoiceDetail.Keys.costCenterCode.key());
		s.addTableColumn("项目段名称", InvoiceDetail.Keys.projectName.key());
		s.addTableColumn("项目段编码", InvoiceDetail.Keys.projectCode.key());
		s.addTableColumn("关联单位名称", InvoiceDetail.Keys.associatedUnitName.key());
		s.addTableColumn("关联单位编码", InvoiceDetail.Keys.associatedUnitCode.key());
		s.addClickLink("修改", "Edit()", InvoiceDetail.pk());
		s.setTableType(TableString.Type.None);
		return s;
	}
	
	@Override
	protected String HTMLAbove() {
		return ""+X.JavaSctipt(""
				+"\n		function Edit(id){"
				+"\n			var p=id?('?p-id='+id):'';"
				+"\n			_E.e8o.open("+new EDP(EU(InvoiceDetailEdit.class)+"'+p+'"
						, 500, 600, InvoiceDetailEdit.Title)+");"
				+"\n		}");
	}
	
	public static final String Title="凭证(ORACLE发票)分录";
	@Override
	protected TopTitle topTitle() {
		return new TopTitle(Title, Image.Document.file);
	}

}
