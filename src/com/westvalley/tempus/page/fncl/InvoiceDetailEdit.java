package com.westvalley.tempus.page.fncl;

import java.util.List;

import com.westvalley.tempus.carry.fncl.EAAssociatedUnit;
import com.westvalley.tempus.carry.fncl.EACostCategory;
import com.westvalley.tempus.carry.fncl.EACostCenter;
import com.westvalley.tempus.carry.fncl.EAProjectSegment;
import com.westvalley.tempus.carry.fncl.InvoiceDetail;

import crivia.db.common.SQL;
import crivia.ecp.carry.eb.MoreMenu;
import crivia.ecp.carry.eb.TopTitle;
import crivia.ecp.carry.eb.TabPage.Image;
import crivia.ecp.carry.em.Browser;
import crivia.ecp.carry.em.EditTable;
import crivia.ecp.carry.em.Editer;
import crivia.ecp.carry.html.X;
import crivia.ecp.carry.html.form.Form;
import crivia.ecp.carry.html.form.Input;
import crivia.ecp.common.ECR;
import crivia.ecp.common.Menu;
import crivia.ecp.p.EditECC;
import crivia.txt.sc.RK;

public class InvoiceDetailEdit extends EditECC<InvoiceDetail> {

	@Override
	protected InvoiceDetail parameterInit() {
		return new InvoiceDetail();
	}

	@Override
	protected String HTMLBody(Form form) {
		EditTable t=new EditTable();
		t.addRow(new Editer("显示顺序", new Input("p-"+InvoiceDetail.Keys.dspOrder.key()
				,""+getP().getDspOrder(),"请输入"+X.cs("显示顺序", "#f00", true))));
		t.addRow(new Editer("金额", new Input("p-"+InvoiceDetail.Keys.money.key()
				,""+getP().getMoney(),"请输入"+X.cs("金额", "#f00", true))));
		t.addRow(new Editer("摘要", new Input("p-"+InvoiceDetail.Keys.desc.key()
				,""+getP().getDesc(),"请输入"+X.cs("摘要", "#f00", true))));
		t.addRow(new Editer("科目", Browser.create(
				Browser.BrowserURLSystemSingle("FNCL_EA_CostCategory")
				, "p-"+InvoiceDetail.Keys.subjectID.key()
				,""+getP().getSubjectID(),"请选择"+X.cs("科目", "#f00", true)+"(费用类别)")
			.relation(EACostCategory.table(), EACostCategory.Keys.costvalue, EACostCategory.Keys.costname)));
		t.addRow(new Editer("成本中心", Browser.create(
				Browser.BrowserURLSystemSingle("FNCL_EA_CostCenter")
				, "p-"+InvoiceDetail.Keys.costCenterID.key()
				,""+getP().getCostCenterID(),"请选择"+X.cs("成本中心", "#f00", true))
			.relation(EACostCenter.table(), EACostCenter.Keys.code, EACostCenter.Keys.description)));
		t.addRow(new Editer("项目段", Browser.create(
				Browser.BrowserURLSystemSingle("FNCL_EA_ProjectSegme")
				, "p-"+InvoiceDetail.Keys.projectID.key()
				,""+getP().getProjectID()
//				,"请选择"+X.cs("项目段", "#f00", true)
				)
			.relation(EAProjectSegment.table(), EAProjectSegment.Keys.pscode, EAProjectSegment.Keys.psname)));
		t.addRow(new Editer("关联单位", Browser.create(
				Browser.BrowserURLSystemSingle("EAAssociatedUnit")
				, "p-"+InvoiceDetail.Keys.associatedUnitID.key()
				,""+getP().getAssociatedUnitID())
			.relation(EAAssociatedUnit.table(), EAAssociatedUnit.Keys.code, EAAssociatedUnit.Keys.name)));
		form.setInner(t);
		return ""+form;
	}
	
	@Override
	protected Object save(InvoiceDetail c) {
		boolean x;
		if (ECR.hasCondition(c.getId())){
			x = SQL.update(c,defaultCMD
					,InvoiceDetail.Keys.dspOrder.key()
					,InvoiceDetail.Keys.money.key()
					,InvoiceDetail.Keys.desc.key()
					,InvoiceDetail.Keys.subjectID.key()
					,InvoiceDetail.Keys.costCenterID.key()
					,InvoiceDetail.Keys.projectID.key()
					,InvoiceDetail.Keys.associatedUnitID.key()
					);
		} else {
			x = SQL.insert(c.setId(RK.rk()),defaultCMD);
		}
		return x ? p2jValue.Success : p2jValue.Fail;
	}
	
	@Override
	protected void moreMenus(List<MoreMenu> menus) {
		for (SaveType t : SaveType.values()){
			if(t.equals(SaveType.SaveAndCreateNew))continue;
			menus.add(Menu.create(t.t, "_"+t+"()").setInTop(true));
		}
	}
	
	public static final String Title="凭证(ORACLE发票)分录 - 编辑";
	@Override
	protected TopTitle topTitle() {
		return new TopTitle(Title, Image.Write.file);
	}

}
