package com.westvalley.tempus.page.fncl;

import java.util.List;

import com.westvalley.tempus.carry.fncl.EAVCity;
import com.westvalley.tempus.timer.EAVCityLoader;

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

public class EAVCityList extends Lister{

	@Override
	protected TableString table() {
		TableSQL q =new  EAVCity().simpleTableSQL();
		TableString t = new TableString(q);
		t.addTableColumn("县区编码",EAVCity.Keys.city_code);
		t.addTableColumn("县区名称",EAVCity.Keys.city_name);
		t.addTableColumn("市区编码",EAVCity.Keys.district_code);
		t.addTableColumn("市区名称",EAVCity.Keys.district_name);
		t.addTableColumn("省份编码",EAVCity.Keys.province_code);
		t.addTableColumn("省份名称",EAVCity.Keys.province_name);
		t.setTableType(Type.None);
		
		
		return t;
	}

	protected TopTitle topTitle() {
		return new TopTitle("县区查询", Image.Arrow.file);
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
		EAVCityLoader.load();
		return Page.forward(EU(this));
	}
	
}
