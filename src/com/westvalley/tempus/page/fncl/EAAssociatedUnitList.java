package com.westvalley.tempus.page.fncl;

import java.util.List;

import com.westvalley.tempus.carry.fncl.EAAssociatedUnit;
import com.westvalley.tempus.timer.EAAssociatedUnitLoader;

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

public class EAAssociatedUnitList extends Lister{

	@Override
	protected TableString table() {
		TableSQL q =new  EAAssociatedUnit().simpleTableSQL();
		TableString t = new TableString(q);
		t.addTableColumn("关联单位编码",EAAssociatedUnit.Keys.code);
		t.addTableColumn("关联单位名称",EAAssociatedUnit.Keys.name);
		t.setTableType(Type.None);
		
		
		return t;
	}

	protected TopTitle topTitle() {
		return new TopTitle("关联单位查询", Image.Arrow.file);
	}
	protected void moreMenus(List<MoreMenu> menus) {
		menus.add(Menu.create("立即同步", "Load();").setInTop(true));
		super.moreMenus(menus);
	}
	protected String HTMLAbove() {
		return ""+X.JavaSctipt(""
				+"\n function Load(){"
				+"\n	_form().action='"+EU(this,"load")+"';"
				+"\n	_form().submit();"
				+"\n}");
	}
	
	@ExecuteMethod(ExecuteMethod.Type.Post)
	public Page load(){
		EAAssociatedUnitLoader.load();
		return Page.forward(EU(this));
	}
	
}
