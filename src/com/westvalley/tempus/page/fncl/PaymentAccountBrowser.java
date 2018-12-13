package com.westvalley.tempus.page.fncl;

import com.westvalley.tempus.carry.fncl.EAPaybankAccount;

import crivia.ecp.carry.eb.TopTitle;
import crivia.ecp.carry.eb.TabPage.Image;
import crivia.ecp.carry.html.form.Form;
import crivia.ecp.carry.tableString.TableSQL;
import crivia.ecp.carry.tableString.TableString;
import crivia.ecp.p.browser.ListBrowser;

public class PaymentAccountBrowser extends ListBrowser {
	
	@Override
	protected void selectForm(Form selectForm) {
		selectForm.addHidden("c");
	}

	@Override
	protected TableString data() {
		TableSQL q =new  EAPaybankAccount().simpleTableSQL();
		q.addWhere_equals(EAPaybankAccount.Keys.cpcode, rp("c"));
		q.setPk("t."+EAPaybankAccount.Keys.paybankid);
		TableString t = new TableString(q);
		t.addTableColumn("付款银行账号",EAPaybankAccount.Keys.paybankacount);
		t.addTableColumn("付款银行id",EAPaybankAccount.Keys.paybankid);
		//t.addTableColumn("公司编码",EAPaybankAccount.Keys.cpcode);
		
		
		return t;
	}

	@Override
	protected String textColumn() {
		return ""+EAPaybankAccount.Keys.paybankacount;
	}
	
	@Override
	protected TopTitle topTitle() {
		return new TopTitle("选择付款银行",Image.Arrow.file);
	}

}
