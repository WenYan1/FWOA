package com.westvalley.tempus.page.fncl;

import java.util.List;

import com.westvalley.tempus.carry.fncl.EACostCenter;
import com.westvalley.tempus.timer.EACostCenterLoader;


import crivia.ecp.carry.eb.MoreMenu;
import crivia.ecp.carry.eb.TopTitle;
import crivia.ecp.carry.eb.TabPage.Image;
import crivia.ecp.carry.html.X;
import crivia.ecp.carry.tableString.TableSQL;
import crivia.ecp.carry.tableString.TableString;
import crivia.ecp.carry.tableString.TableString.Type;
import crivia.ecp.common.Menu;
import crivia.ecp.p.Lister;
import crivia.mvc.carry.Page;
import crivia.mvc.i.ExecuteMethod;

public class EACostCenterList extends Lister{

	
	
	
	@ExecuteMethod(ExecuteMethod.Type.Post)
	public Page load(){
		EACostCenterLoader.load();
		return Page.forward(EU(this));
	}
	
	@Override
	protected TableString table() {
		TableSQL q =new EACostCenter().simpleTableSQL();
		
		TableString t = new TableString(q);
		
		t.addTableColumn("成本中心编码",EACostCenter.Keys.code);
		t.addTableColumn("成本中心描述",EACostCenter.Keys.description);
		t.addTableColumn("公司编码",EACostCenter.Keys.cpCode);
		t.setTableType(Type.None);
		return t;
	}
	
	protected String HTMLAbove() {
		return ""+X.JavaSctipt(""
				+"\n function Load(){"
				+"\n	_form().action='"+EU(this,"load")+"';"
				+"\n	_form().submit();"
				+"\n}");
	}
	
	
	protected void moreMenus(List<MoreMenu> menus) {
		menus.add(Menu.create("立即同步", "Load();").setInTop(true));
		super.moreMenus(menus);
	}
	
	protected TopTitle topTitle() {
		return new TopTitle("成本中心查询", Image.Arrow.file);
	}

}
