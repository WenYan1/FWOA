package com.westvalley.tempus.page.fncl;

import java.util.List;

import crivia.db.common.SQL;
import crivia.db.i.DataSet;
import crivia.ecp.ECP;
import crivia.ecp.carry.eb.MoreMenu;
import crivia.ecp.carry.eb.TopTitle;
import crivia.ecp.carry.eb.TabPage.Image;
import crivia.ecp.carry.em.Browser;
import crivia.ecp.carry.em.EditTable;
import crivia.ecp.carry.em.Editer;
import crivia.ecp.carry.html.form.Form;
import crivia.ecp.carry.html.form.Input;
import crivia.ecp.common.Menu;
import crivia.mvc.carry.Page;

public class PEAE extends ECP {
	
	public Page save(){
		DataSet d=SQL.select(SQL.sql.array("select"
			+" field4,field3,field5 from cus_fieldData"
			+" where  id = ? and scopeID = ?"
			, user.getUID() , -1
		),defaultCMD);
		if(d.next()){
			SQL.edit(SQL.sql.array("update cus_fieldData"
					+" set field4=?"
					+" , field3=?"
					+" , field5=?"
					+" where id = ? and scopeID = ?"
					, rp("bank_code"), rp("f1"), rp("house_bank_name")
					, user.getUID() , -1
			),defaultCMD);
		}else{
			SQL.edit(SQL.sql.array("insert into cus_fieldData"
					+" ( field4 , field3 , field5 , id , scopeID , scope )"
					+" values ( ? , ? , ? , ? , ? , ? ) "
					, rp("bank_code"), rp("f1"), rp("house_bank_name")
					, user.getUID() , -1 , "HrmCustomFieldByInfoType"
			),defaultCMD);
		}
		return Page.forward(EU(this));
	}

	@Override
	protected String HTMLBody() {
		{
			DataSet d=SQL.select(SQL.sql.array("select"
				+" field4,field3,field5 from cus_fieldData"
				+" where id = ? and scopeID = ?"
				, user.getUID() , -1
			),defaultCMD);
			if(d.next()){
				rpInit("f1", d.get("field3"));
				rpInit("bank_code", d.get("field4"));
				rpInit("house_bank_name", d.get("field5"));
			}
		}
		Form f=new Form(this, formKey, EU(this,"save"));
		EditTable t=new EditTable();
		f.setInner(t); 
		t.setHead("个人报销信息设定");
		t.addRow(new Editer("银行名称", Browser.create(this
				, Browser.BrowserURLSystemMultiple("WV_FNCL_EA_V_BANK"), "bank_code","银行名称必填")
			.relation("WV_FNCL_EA_V_BANK","bank_code", "bank_name")));
		t.addRow(new Editer("银行账号", new Input(this, "f1", "银行账号必填")));
		t.addRow(new Editer("银行开户行", Browser.create(this
				, Browser.BrowserURLSystemMultiple("contractAccount_bank"), "house_bank_name","银行开户行必填")
			.relation("WV_FNCL_EA_START_BANK","house_bank_name", "house_bank_name")));
		return ""+f;
	}
	
	@Override
	protected void moreMenus(List<MoreMenu> menus) {
		menus.add(Menu.Select(formKey).setName("保存").setInTop(true));
	}
	
	@Override
	protected TopTitle topTitle() {
		return new TopTitle("个人报销信息设定", Image.Write.file);
	}

}
