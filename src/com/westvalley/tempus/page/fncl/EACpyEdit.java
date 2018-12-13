package com.westvalley.tempus.page.fncl;

import java.util.List;

import com.westvalley.tempus.carry.fncl.EACpy;

import crivia.db.common.SQL;
import crivia.ecp.carry.eb.MoreMenu;
import crivia.ecp.carry.eb.TopTitle;
import crivia.ecp.carry.eb.TabPage.Image;
import crivia.ecp.carry.em.Browser;
import crivia.ecp.carry.em.EditTable;
import crivia.ecp.carry.em.Editer;
import crivia.ecp.carry.html.X;
import crivia.ecp.carry.html.form.Form;
import crivia.ecp.common.Menu;
import crivia.ecp.p.EditECC;

public class EACpyEdit extends EditECC<EACpy> {

	@Override
	protected EACpy parameterInit() {
		return new EACpy();
	}

	@Override
	protected String HTMLBody(Form form) {
		{
			EditTable t=new EditTable();
			form.setInner(t);
			t.addRow(new Editer("名称", getP().getName()));
			t.addRow(new Editer("编码", getP().getCode()));
			t.addRow(new Editer("付款操作人", Browser.s_HrmMuti(
					"p-"+EACpy.Keys.paymentOperator.key()
					, getP().getPaymentOperator()
					, "请选择"+X.cs("付款操作人", "#f00", true))));
			t.addRow(new Editer("付款查询操作人", Browser.s_HrmMuti(
					"p-"+EACpy.Keys.paymentSearchOperator.key()
					, getP().getPaymentSearchOperator()
					, "请选择"+X.cs("付款查询操作人", "#f00", true))));
		}
		return ""+form;
	}
	
	@Override
	protected Object save(EACpy c) {
		return SQL.update(c,defaultCMD
				,EACpy.Keys.paymentOperator.key()
				,EACpy.Keys.paymentSearchOperator.key())
			?p2jValue.Success:p2jValue.Fail;
	}
	
	@Override
	protected void moreMenus(List<MoreMenu> menus) {
		for (SaveType t : SaveType.values()){
			if(SaveType.SaveAndCreateNew.equals(t))continue;
			menus.add(Menu.create(t.t, "_"+t+"()").setInTop(true));
		}
	}
	
	@Override
	protected TopTitle topTitle() {
		return new TopTitle("设置付款操作人", Image.Write.file);
	}

}
