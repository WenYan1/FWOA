package com.westvalley.tempus.page.fncl;

import java.util.List;

import com.westvalley.tempus.carry.fncl.EAProjectSegment;
import com.westvalley.tempus.timer.EAProjectSegmentLoader;

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

public class EAProjectSegmentList extends Lister{

	@Override
	protected TableString table() {
		TableSQL q =new  EAProjectSegment().simpleTableSQL();
		TableString t = new TableString(q);
		t.addTableColumn("项目段编码",EAProjectSegment.Keys.pscode);
		t.addTableColumn("项目段名称",EAProjectSegment.Keys.psname);
		t.addTableColumn("公司编码",EAProjectSegment.Keys.cpcode);
		t.setTableType(Type.None);
		
		
		return t;
	}

	protected TopTitle topTitle() {
		return new TopTitle("项目段查询", Image.Arrow.file);
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
		EAProjectSegmentLoader.load();
		return Page.forward(EU(this));
	}
	
}
