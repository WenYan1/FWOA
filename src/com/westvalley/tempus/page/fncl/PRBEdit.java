package com.westvalley.tempus.page.fncl;

import java.util.List;

import com.westvalley.tempus.carry.fncl.EAAssociatedUnit;
import com.westvalley.tempus.carry.fncl.EACostCategory;
import com.westvalley.tempus.carry.fncl.EACostCenter;
import com.westvalley.tempus.carry.fncl.EACpy;
import com.westvalley.tempus.carry.fncl.EAPaybankAccount;
import com.westvalley.tempus.carry.fncl.EAProjectSegment;
import com.westvalley.tempus.carry.fncl.InvoiceDetail;
import com.westvalley.tempus.carry.fncl.PR;
import com.westvalley.tempus.carry.fncl.PRB;

import crivia.db.common.SQL;
import crivia.db.i.DataSet;
import crivia.ecp.carry.eb.MoreMenu;
import crivia.ecp.carry.eb.TopTitle;
import crivia.ecp.carry.eb.TabPage.Image;
import crivia.ecp.carry.em.Browser;
import crivia.ecp.carry.em.DayPicker;
import crivia.ecp.carry.em.EditTable;
import crivia.ecp.carry.em.Editer;
import crivia.ecp.carry.html.X;
import crivia.ecp.carry.html.form.Form;
import crivia.ecp.carry.html.form.Input;
import crivia.ecp.carry.html.form.Select;
import crivia.ecp.common.ECR;
import crivia.ecp.common.Menu;
import crivia.ecp.p.EditECC;
import crivia.mvc.carry.Page;
import crivia.txt.common.MoneyFormat;
import crivia.txt.sc.RK;

public class PRBEdit extends EditECC<PRB> {

	
	
	@Override
	protected Page breakPage() {
		if(!ECR.hasCondition(getP().getId()))return Page.redirect(NORIGHT);
		return super.breakPage();
	}
	@Override
	protected PRB parameterInit() {
		return new PRB();
	}

	@Override
	protected String HTMLBody(Form form) {
		StringBuilder html=new StringBuilder();
		
		{
			EditTable table=new EditTable();
			table.setHead("收款银行信息");
			if(!ECR.hasCondition(getP().getPayDay())){
				
			
				table.addRow(new Editer("收款银行账号", new Input(
						"p-"+PRB.Keys.prbrecBankAccountNumber.key(), getP().getPrbrecBankAccountNumber()
						,"请填写"+X.cs("收款银行账号", "#f00", true)
					.addStyle("width", "90%"))));
				table.addRow(new Editer("收款银行账户名", new Input(
						"p-"+PRB.Keys.prbrecBankAccountName.key(), getP().getPrbrecBankAccountName()
						,"请填写"+X.cs("收款银行账户名", "#f00", true)
					.addStyle("width", "90%"))));
				table.addRow(new Editer("收款银行名称", new Input(
						"p-"+PRB.Keys.prbrecBankName.key(), getP().getPrbrecBankName()
						,"请填写"+X.cs("收款银行名称", "#f00", true)
					.addStyle("width", "90%"))));
			}else {
				form.addHidden("p-"+PRB.Keys.prbrecBankAccountNumber.key(), getP().getPrbrecBankAccountNumber());
				form.addHidden("p-"+PRB.Keys.prbrecBankAccountName.key(), getP().getPrbrecBankAccountName());
				form.addHidden("p-"+PRB.Keys.prbrecBankName.key(), getP().getPrbrecBankName());
				table.addRow(new Editer("收款银行账号", getP().getPrbrecBankAccountNumber()));
				table.addRow(new Editer("收款银行账户名", getP().getPrbrecBankAccountName()));
				table.addRow(new Editer("收款银行名称", getP().getPrbrecBankName()));
				table.addRow(new Editer("说明", X.cs("已支付", "#f00", true)
						+"状态下的付款信息不能被修改"));
				
			}
		
			
			html.append(table);
		}{
			EditTable table=new EditTable();
			table.setHead("ORACLE凭证数据");
			table.addRow(new Editer("用途", new Input(
			"p-"+PRB.Keys.application.key(), getP().getApplication()
			, "请填写"+X.cs("用途", "#f00", true)
			)));
			table.addRow(new Editer("GL日期", new DayPicker(
					"p-"+PRB.Keys.prbGLDay.key(), getP().getPrbGLDay()
					, "请选择"+X.cs("GL日期", "#f00", true))));
	
			table.addRow(new Editer("付款银行账号", Browser.create(
//					Browser.BrowserURLSystemSingle("EA_PaybankAccount")
					EU(PaymentAccountBrowser.class)+"?c="+getP().getPrbEACompany()
					, "p-"+PRB.Keys.prbPayMentAccount.key(), getP().getPrbPayMentAccount()
					, "请选择"+X.cs("付款银行账号", "#f00", true))
				.setWidth("95%")
				.relation(EAPaybankAccount.table(), EAPaybankAccount.Keys.paybankid, EAPaybankAccount.Keys.paybankacount)));
			html.append(table);
		}
		form.setInner(html);
		
		return ""+form;
	}
	
	@Override
	protected Object save(PRB c) {
		boolean x;
		if (ECR.hasCondition(c.getId())){
			x = SQL.update(c,defaultCMD
					,PRB.Keys.prbrecBankAccountName.key()
					,PRB.Keys.prbrecBankAccountNumber.key()
					,PRB.Keys.prbrecBankName.key()
					,PRB.Keys.prbGLDay.key()
					,PRB.Keys.application.key()
					,PRB.Keys.prbPayMentAccount.key()
					);
			for(PR r:SQL.list(defaultCMD,PR.class,SQL.cd.equals(PR.Keys.prb, c.getId()))){
				//直接将所有一起的数据都修改
				r.setRecBankAccountName(c.getPrbrecBankAccountName())
				.setRecBankAccountNumber(c.getPrbrecBankAccountNumber())
				.setRecBankName(c.getPrbrecBankName())
				.setVdGLDay(c.getPrbGLDay())
				.setVdPayMentAccount(c.getPrbPayMentAccount());
				SQL.update(r.setPayStatus(""+PR.PayStatus.Success),defaultCMD
						,PR.Keys.recBankAccountName.key()
						,PR.Keys.recBankAccountNumber.key()
						,PR.Keys.recBankName.key()
						,PR.Keys.vdGLDay.key()
						,PR.Keys.vdPayMentAccount.key());
			}
		} else {
			x = SQL.insert(c.setId(RK.rk()),defaultCMD);
		}
		return x ? p2jValue.Success : p2jValue.Fail;
	}
	
	@Override
	protected void moreMenus(List<MoreMenu> menus) {
		if(ECR.hasCondition(getP().getPayDay()))return;
		for (SaveType t : SaveType.values()){
			if(t.equals(SaveType.SaveAndCreateNew))continue;
			menus.add(Menu.create(t.t, "_"+t+"()").setInTop(true));
		}
	}
	
	public static final String Title="付款单 - 编辑";
	@Override
	protected TopTitle topTitle() {
		return new TopTitle(Title, Image.Write.file);
	}

}
