package com.westvalley.tempus.page.fncl;

import java.util.List;

import com.westvalley.tempus.carry.fncl.EAVStartBank;
import com.westvalley.tempus.timer.EAVStartBankLoader;

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
import crivia.txt.common.Logger;

public class EAVStartBankList extends Lister{

	@Override
	protected TableString table() {
		TableSQL q =new  EAVStartBank().simpleTableSQL();
		TableString t = new TableString(q);
		t.addTableColumn("银行id",EAVStartBank.Keys.bank_id);
		t.addTableColumn("开户行编码",EAVStartBank.Keys.house_bank_code);
		t.addTableColumn("开户行名称",EAVStartBank.Keys.house_bank_name);
		t.addTableColumn("城市id",EAVStartBank.Keys.city_code);
		t.setTableType(Type.None);
		
		 
		return t;
	}

	protected TopTitle topTitle() {
		return new TopTitle("开户行银行查询", Image.Arrow.file);
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
		
		Logger.log("LOAD PAGE BEGIN ...");
		EAVStartBankLoader.load();
		return Page.forward(EU(this));
	}
	
}
