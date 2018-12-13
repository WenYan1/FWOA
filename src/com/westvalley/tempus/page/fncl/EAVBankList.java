package com.westvalley.tempus.page.fncl;

import java.util.List;

import com.westvalley.tempus.carry.fncl.EAVBank;
import com.westvalley.tempus.timer.EAVBankLoader;

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

public class EAVBankList extends Lister{

	@Override
	protected TableString table() {
		TableSQL q =new  EAVBank().simpleTableSQL();
		TableString t = new TableString(q);
		t.addTableColumn("银行编码",EAVBank.Keys.bank_code);
		t.addTableColumn("银行名称",EAVBank.Keys.bank_name);
		t.setTableType(Type.None);
		
		
		return t;
	}

	protected TopTitle topTitle() {
		return new TopTitle("银行类别查询", Image.Arrow.file);
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
		EAVBankLoader.load();
		return Page.forward(EU(this));
	}
	
}
