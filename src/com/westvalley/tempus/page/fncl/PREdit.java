package com.westvalley.tempus.page.fncl;

import java.util.List;

import com.westvalley.tempus.carry.fncl.EACpy;
import com.westvalley.tempus.carry.fncl.EAPaybankAccount;
import com.westvalley.tempus.carry.fncl.PR;
import com.westvalley.tempus.carry.fncl.PR.PayStatus;

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

public class PREdit extends EditECC<PR> {
	
	@Override
	protected Page breakPage() {
		if(!ECR.hasCondition(getP().getId()))return Page.redirect(NORIGHT);
		return super.breakPage();
	}

	@Override
	protected PR parameterInit() {
		return new PR();
	}

	@Override
	protected String HTMLBody(Form form) {
		StringBuilder html=new StringBuilder();
		{
			EditTable table=new EditTable();
			table.setHead("收款银行信息");
			if(getP().getPayStatusX().reeditable){
				table.addRow(new Editer("收款银行账号", new Input(
						"p-"+PR.Keys.recBankAccountNumber.key(), getP().getRecBankAccountNumber()
						,"请填写"+X.cs("收款银行账号", "#f00", true)
					.addStyle("width", "90%"))));
				table.addRow(new Editer("收款银行账户名", new Input(
						"p-"+PR.Keys.recBankAccountName.key(), getP().getRecBankAccountName()
						,"请填写"+X.cs("收款银行账户名", "#f00", true)
					.addStyle("width", "90%"))));
				table.addRow(new Editer("收款银行名称", new Input(
						"p-"+PR.Keys.recBankName.key(), getP().getRecBankName()
						,"请填写"+X.cs("收款银行名称", "#f00", true)
					.addStyle("width", "90%"))));
			}else{
				form.addHidden("p-"+PR.Keys.recBankAccountNumber.key(), getP().getRecBankAccountNumber());
				form.addHidden("p-"+PR.Keys.recBankAccountName.key(), getP().getRecBankAccountName());
				form.addHidden("p-"+PR.Keys.recBankName.key(), getP().getRecBankName());
				table.addRow(new Editer("收款银行账号", getP().getRecBankAccountNumber()));
				table.addRow(new Editer("收款银行账户名", getP().getRecBankAccountName()));
				table.addRow(new Editer("收款银行名称", getP().getRecBankName()));
				table.addRow(new Editer("说明", X.cs(getP().getPayStatusX().t, "#f00", true)
						+"状态下的付款信息不能被修改"));
			}
			html.append(table);
		}{
			EditTable table=new EditTable();
			table.setHead("ORACLE凭证数据");
			table.addRow(new Editer("金额",MoneyFormat.toMoneyFormat(getP().getMoney())));
//			table.addRow(new Editer("金额", new Input(
//					"p-"+PR.Keys.money.key(), ""+getP().getMoney()
//					, "请填写"+X.cs("金额", "#f00", true))));
			table.addRow(new Editer("申请日期", new DayPicker(
					"p-"+PR.Keys.createDay.key(), getP().getCreateDay()
					, "请选择"+X.cs("申请日期", "#f00", true))));
//			table.addRow(new Editer("申请人", Browser.s_Hrm(
//					"p-"+PR.Keys.creator.key(), getP().getCreator()
//					, "请选择"+X.cs("申请人", "#f00", true))));
			table.addRow(new Editer("支付状态", getP().getPayStatusX().t));
//			table.addRow(new Editer("支付状态", PR.PayStatus.select(
//					"p-"+PR.Keys.payStatus.key(), getP().getPayStatus())));
//			table.addRow(new Editer("收款供应商名称", new Input(
//					"p-"+PR.Keys.recSupplyName.key(), getP().getRecSupplyName()
//					,"请填写"+X.cs("收款供应商名称", "#f00", true))));
			table.addRow(new Editer("费用报销公司", Browser.create(
					Browser.BrowserURLSystemSingle("FNCL_EA_Company")
					, "p-"+PR.Keys.vdEACompany.key(), getP().getVdEACompany()
					, "请选择"+X.cs("费用报销公司", "#f00", true))
				.setWidth("95%")
				.relation(EACpy.table(), EACpy.Keys.code, EACpy.Keys.name)
				.getText()
			));
//			table.addRow(new Editer("费用报销公司名称（合同付款）", new Input(
//					"p-"+PR.Keys.vdEACompanyName.key(), getP().getVdEACompanyName()
//					, "请填写"+X.cs("费用报销公司名称", "#f00", true))));
			table.addRow(new Editer("GL日期", new DayPicker(
					"p-"+PR.Keys.vdGLDay.key(), getP().getVdGLDay()
					, "请选择"+X.cs("GL日期", "#f00", true))));
			Select selectCurrency=new Select(
					"p-"+PR.Keys.vdCurrency.key(), getP().getVdCurrency(),null);{
				DataSet d=SQL.select("select id v,tb_currency t from uf_hlhzb",defaultCMD);
				while(d.next())selectCurrency.addOption(d.get("v"), d.get("t"));
			}
			table.addRow(new Editer("币种", selectCurrency.getText()));
//			table.addRow(new Editer("摘要（合同付款）", new Input(
//					"p-"+PR.Keys.vdInvoiceDesc.key(), getP().getVdInvoiceDesc()
//					,null// "请填写"+X.cs("摘要", "#f00", true)
//					)));
			table.addRow(new Editer("付款方式", new Select(
					"p-"+PR.Keys.vdPayMentMethod.key(), getP().getVdPayMentMethod()
					, null)
				.addOption("0","CHECK").addOption("1","WIRE").getText()
			));
//			table.addRow(new Editer("付款方式", new Input(
//					"p-"+PR.Keys.vdPayMentMethod.key(), getP().getVdPayMentMethod()
//					, "请填写"+X.cs("付款方式", "#f00", true))));
			table.addRow(new Editer("付款银行账号", Browser.create(
//					Browser.BrowserURLSystemSingle("EA_PaybankAccount")
					EU(PaymentAccountBrowser.class)+"?c="+getP().getVdEACompany()
					, "p-"+PR.Keys.vdPayMentAccount.key(), getP().getVdPayMentAccount()
					, "请选择"+X.cs("付款银行账号", "#f00", true))
				.setWidth("95%")
				.relation(EAPaybankAccount.table(), EAPaybankAccount.Keys.paybankid, EAPaybankAccount.Keys.paybankacount)));
//			table.addRow(new Editer("付款银行账号", new Input(
//					"p-"+PR.Keys.vdPayMentAccount.key(), getP().getVdPayMentAccount()
//					, "请填写"+X.cs("付款银行账号", "#f00", true))));
			html.append(table);
		}
		form.setInner(html);
		StringBuilder reeditableStatus=new StringBuilder();
		for(PR.PayStatus status:PR.PayStatus.values()){
			if(status.reeditable)continue;
			if(reeditableStatus.length()>0)reeditableStatus.append("/");
			reeditableStatus.append(X.cs(status.t, "#f00", true));
		}
		p2j(p2jKey.Msg, "WrongStatus", "_E.e8o.alert('不能修改状态为"
				+reeditableStatus+"的数据;');");
		p2j(p2jKey.Msg, "WrongData", "_E.e8o.alert('被操作的数据不存在;');");
		return ""+form;
	}
	
	@Override
	protected Object save(PR c) {
		if(!ECR.hasCondition(c.getId()))return "WrongData";
		if(!PayStatus.k2e(SQL.relation(c.getId(), PR.table(), PR.Keys.payStatus, PR.pk()
				, defaultCMD)).reeditable)return "WrongStatus";
		c
			.setPayStatus(""+PR.PayStatus.Edited)
			.setVdEACompanyName(SQL.relation(c.getVdEACompany()
					, EACpy.table(), EACpy.Keys.name, EACpy.Keys.code, defaultCMD))
		;
		return SQL.update(c,defaultCMD
				,PR.Keys.payStatus.key()
				,PR.Keys.recBankAccountNumber.key()
				,PR.Keys.recBankAccountName.key()
				,PR.Keys.recBankName.key()
//				,PR.Keys.money.key()
				,PR.Keys.createDay.key()
//				,PR.Keys.creator.key()
//				,PR.Keys.payStatus.key()
//				,PR.Keys.recSupplyName.key()
//				,PR.Keys.vdEACompany.key()
//				,PR.Keys.vdEACompanyName.key()
				,PR.Keys.vdGLDay.key()
//				,PR.Keys.vdCurrency.key()
//				,PR.Keys.vdInvoiceDesc.key()
//				,PR.Keys.vdPayMentMethod.key()
				,PR.Keys.vdPayMentAccount.key()
		) ? p2jValue.Success : p2jValue.Fail;

	}
	@Override
	protected String p2successText() {return ""+X.cs(super.p2successText(), "#97f", true);}
	protected String p2failText() {return ""+X.cs(super.p2failText(), "#970", true);}
	
	@Override
	protected void moreMenus(List<MoreMenu> menus) {
		if(!getP().getPayStatusX().reeditable)return;
		for (SaveType t : SaveType.values()){
			if(SaveType.SaveAndCreateNew.equals(t))continue;
			menus.add(Menu.create(t.t, "_"+t+"()").setInTop(true));
		}
	}
	
	public static final String Title="支付业务管理 - 账户信息调整";
	@Override
	protected TopTitle topTitle() {
		return new TopTitle(Title, Image.Write.file);
	}

}
