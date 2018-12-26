package com.westvalley.tempus.page.fncl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.westvalley.tempus.carry.fncl.EACpy;
import com.westvalley.tempus.carry.fncl.PR;
import com.westvalley.tempus.carry.fncl.PRB;
import com.westvalley.tempus.carry.oracle.OVD;
import com.westvalley.tempus.carry.oracle.OVM;
import com.westvalley.tempus.oracle.VCR;

import crivia.db.common.SQL;
import crivia.db.exception.NoData;
import crivia.eca.a.EcologyCMD;
import crivia.eca.a.EcologyCMD_Trans;
import crivia.ecp.ECP.p2jKey;
import crivia.ecp.ECP.p2jValue;
import crivia.ecp.carry.eb.MoreMenu;
import crivia.ecp.carry.eb.TopTitle;
import crivia.ecp.carry.eb.TabPage.Image;
import crivia.ecp.carry.em.Browser;
import crivia.ecp.carry.em.DayRange;
import crivia.ecp.carry.em.EDP;
import crivia.ecp.carry.em.EditTable;
import crivia.ecp.carry.em.Editer;
import crivia.ecp.carry.html.X;
import crivia.ecp.carry.html.form.Form;
import crivia.ecp.carry.html.form.Input;
import crivia.ecp.carry.tableString.TableSQL;
import crivia.ecp.carry.tableString.TableString;
import crivia.ecp.common.ECR;
import crivia.ecp.common.EcologyDB;
import crivia.ecp.common.Menu;
import crivia.ecp.p.Lister;
import crivia.mvc.carry.Page;
import crivia.mvc.i.ExecuteMethod;
import crivia.time.carry.Day;
import crivia.time.carry.Time;
import crivia.txt.common.Logger;

public class PRBList extends Lister {
	private Set<String> eaCpys=new HashSet<String>();
	private Set<String> eaSearchCpys=new HashSet<String>();
	@Override
	protected Page breakPage() {
		//if(!rpExist("r"))return Page.redirect(NORIGHT);
		for(EACpy c:SQL.list(defaultCMD, EACpy.class
				,SQL.cd.like(
					defaultCMD.specially().concatString(
						SQL.sql.sv(","),""+EACpy.Keys.paymentOperator,SQL.sql.sv(","))
					,","+user.getUID()+","
				)
		)){
			eaCpys.add(c.getCode());
		}

		return super.breakPage();
	}
	
	@Override
	protected void selectForm(Form selectForm) {
		selectForm.addHidden("k","");
		selectForm.addHidden("n","");
	}
	
	
	
	private DayRange prbCreate;
	public DayRange getPrbCreate() {
		if(null==prbCreate){
			prbCreate=new DayRange("prbCreate");
		}
		return prbCreate;
	}
	public void setPrbCreate(DayRange prbCreate) {this.prbCreate = prbCreate;}
	
	
	private DayRange prbPay;
	public DayRange getPrbPay() {
		if(null==prbPay){
			prbPay=new DayRange("prbPay");
		}
		return prbPay;
	}
	public void setPrbPay(DayRange prbPay) {this.prbPay = prbPay;}
	
	
	public static void main(String[] args) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
//		for(PRB.Keys k:PRB.Keys.values()){
//			System.out.println("."+crivia.c.a.KeyLink.toSetter(""+k)
//					+"()");
//		}
//		PRList pr=new PRList();
//		java.lang.reflect.Field f=PRList.class
//			.getSuperclass()
//			.getSuperclass()
//			.getSuperclass()
//			.getSuperclass()
//				.getDeclaredField("defaultCMD");
//		f.setAccessible(true);
//		f.set(pr, new crivia.db.jdbc.CMD_jdbc(
//			new crivia.db.jdbc.DB(
//				crivia.db.jdbc.DB.Driver.Oracle
//				, "", "", "")));
//		String s=pr.table().toString();
//		System.out.println(s);
	}
	/**支付操作的后台POST请求接收函数*/
	@ExecuteMethod(ExecuteMethod.Type.Post)
	public Page _x(){
		//接收被操作的数据ID和付款方式
		String k=rp("k"),n=rp("n");
		//错误的数据ID
		Logger.log("开始去线下支付"+k);
		if(!ECR.hasCondition(k))return Page.forward(EU(this)+"?"+p2jUPMsg("WrongData"));
		//根据数据ID查询被操作的数据
		Logger.log("开始查询");
		List<PRB> prbs=SQL.list(defaultCMD, "order by "+defaultCMD.specially()
					.toInt("t."+PRB.Keys.payKey)+" asc" //按requestID排序
				, PRB.class //查询PR主数据
				,SQL.cd.in(PRB.pk(), k) //条件1 ID in K
		); 
		
		
		boolean isVCRX=false,noVCRX=false,ispay=false
				,noGLDay=false,noPaymentAccount=false;

		for(PRB r:prbs){
			if(!ECR.hasCondition(r.getPrbGLDay()))noGLDay=true;
			if(!ECR.hasCondition(r.getPrbPayMentAccount()))noPaymentAccount=true;
			if("Y".equals(r.getVcr2Status())||ECR.hasCondition(r.getPayDay()))ispay=true;
		}
		Logger.log("是否错误开始");
		if(ispay)return Page.forward(EU(this)+"?"+p2jUPMsg("WrongRepeatPay"));
		Logger.log("是否错误结束");
		if(noGLDay)return Page.forward(EU(this)+"?"+p2jUPMsg("NoGLDay"));
		if(noPaymentAccount)return Page.forward(EU(this)+"?"+p2jUPMsg("NoPaymentAccount"));
		//查询不到数据也会返回错误数据
		
		if(prbs.size()<1)return Page.forward(EU(this)+"?"+p2jUPMsg("WrongData"));
		//验证付款方式
		PR.PayLine payLine;
		try {
			payLine=PR.PayLine.valueOf(n);
		} catch (Exception e) {
			//错误的付款方式
			return Page.forward(EU(this)+"?"+p2jUPMsg("WrongLine"));
		}
		for(PRB B:prbs) {
			String prbk=B.getId();
		//启动事务
		EcologyCMD_Trans cmd=new EcologyCMD_Trans();
		cmd.begin();cmd.isThrow=true;try {
			List<PR> prs=SQL.list(defaultCMD, PR.class
					,SQL.cd.equals(PR.Keys.prb, prbk));
			PRB b =B
					.setPayLine(""+payLine)
					.setPayDay(""+Day.today())
					.setPayTime(""+Time.now())
					;
			Logger.log("支付"+payLine);
			if(PR.PayLine.OnLine.equals(payLine)){			
				//for(PR r:prs)mainRequestID=r.getRequestID();
				//SQL.update(b.setPayKey(mainRequestID),cmd
					//	);
				//SQL.insert(prbs.get(0).setPayKey(mainRequestID), cmd);
				//线上支付
				for(PR r:prs){
					//将相关数据修改为支付中
					SQL.update(r.setPrb(B).setPayStatus(""+PR.PayStatus.Payed),cmd
							,PR.Keys.prb.key()
							,PR.Keys.payStatus.key());
				}
				SQL.update(b,cmd);
				cmd.end(true);
				//并组织数据调用资金系统接口
				try {
					com.westvalley.tempus.ps.PSM.x(b,b.getPayKey());
				} catch (Exception e) {
					Logger.log("线上支付出现异常 > "+Logger.hiddenBox(crivia.txt.common.Information.exceptionInfo(e,2)));
					return Page.forward(EU(this)+"?"+p2jUPMsg("PE")+"&_t="+e.getMessage());
				}
			}else{			
				Logger.log("线下支付");
				SQL.update(b,cmd);
				//线下支付
				for(PR r:prs){
					//直接将相关数据修改为支付成功
					SQL.update(r.setPrb(B).setPayStatus(""+PR.PayStatus.Success),cmd
							,PR.Keys.prb.key()
							,PR.Keys.payStatus.key());
				}
	
				cmd.end(true);
			}
			boolean isVCR2=true;
			
				Logger.log("调用线下支付java");
				for(PR r:prs){
					if(PR.PayStatus.Success.equals(r.getPayStatusX()))VCR.x(r,B.getId());
					if(!"S".equals(r.getVcrStatus()))isVCR2=false;
				}
			
				
			if(isVCR2)VCR.x2(B.getId(),isVCRX);
		} catch (Exception e){
			cmd.isThrow=false;
			cmd.end(false);
			Logger.logException("支付操作失败", e);
		}
		}
		return Page.forward(EU(this)+"?"+p2jUPMsg(p2jValue.Success));
	}
	
	
	
	/**补写凭证的后台POST请求接收函数*/
	@ExecuteMethod(ExecuteMethod.Type.Post)
	public Page _v(){
		String prbk;
		//启动事务
		String k=rp("k"),n=rp("n");
		//错误的数据ID
		Logger.log("手工"+k);
		EcologyCMD_Trans cmd=new EcologyCMD_Trans();
		cmd.begin();cmd.isThrow=true;
		try {
			PRB r=SQL.one(defaultCMD, PRB.class, true
					,SQL.cd.equals(PRB.pk(), rp("k").replace(",","")));
			if("Y".equals(r.getVcr2Status()))return Page.forward(EU(this)+"?"+p2jUPMsg("WrongRepeatPay"));
			
			prbk=r.getId();
			List<PR> prs=SQL.list(defaultCMD, PR.class
					,SQL.cd.equals(PR.Keys.prb, prbk));
			PRB b =r
					.setPayDay(""+Day.today())
					.setPayTime(""+Time.now())
					.setPayOperator(""+user.getUID())
					.setVcr2Status("Y")
					.setIsManual("Y")
					
					;
			Logger.log("手工录入开始");
			SQL.update(b,cmd);
			for(PR pr:prs){
				//直接将相关数据修改为支付成功
				SQL.update(pr.setPrb(b).setPayStatus(""+PR.PayStatus.Success).setVcrCreateDay(""+Day.today()).setVcrStatus("SS")
						.setVcrN("000000"),cmd
						,PR.Keys.prb.key()
						,PR.Keys.payStatus.key()
						,PR.Keys.vcrCreateDay.key()
						,PR.Keys.vcrStatus.key()
						,PR.Keys.vcrN.key()
						);
			}

			cmd.end(true);
		
		} catch (NoData e) {
			cmd.isThrow=false;
			cmd.end(false);
			Logger.logException("手工录入失败", e);
			return Page.forward(EU(this)+"?"+p2jUPMsg("WrongPay"));
		}

		return Page.forward(EU(this)+"?"+p2jUPMsg(p2jValue.Success));
	}
	

	
	@Override
	protected void selectTable(EditTable selectTable) {
		selectTable.addRow(new Editer("付款单号", new Input(this, "PRBpaykey")));
		selectTable.addRow(new Editer("生成付款单日期", getPrbCreate()));
		selectTable.addRow(new Editer("支付日期", getPrbPay()));
		selectTable.addRow(new Editer("金额", new Input(this, "minMoney").addStyle("width", "100px")
			+"-"+new Input(this, "maxMoney").addStyle("width", "100px")));
		//selectTable.addRow(new Editer("支付方式", PR.PayLine.select(this, "payLine")));
		selectTable.addRow(new Editer("支付操作人", Browser.s_HrmMuti(this, "payOprt")));
		selectTable.addRow(new Editer("收款银行账号", new Input(this,"recBAcNb")));
		selectTable.addRow(new Editer("收款银行账户名", new Input(this,"recBAcNm")));
		selectTable.addRow(new Editer("收款银行名称", new Input(this,"recBNm")));
		selectTable.addRow(new Editer("费用报销公司", Browser.create(this
				, Browser.BrowserURLSystemMultiple("FNCL_EA_Company"), "eaCpy").relation(EACpy.table(),EACpy.Keys.code, EACpy.Keys.name)));
	}
	@Override
	protected TableString table() {
		TableSQL q=new PRB().simpleTableSQL(
				PRB.Keys.payKey.key()
				,PRB.Keys.prbEACompany.key()
				,PRB.Keys.prbrecBankAccountName.key()
				,PRB.Keys.prbrecBankAccountNumber.key()
				,PRB.Keys.prbPayMentAccount.key()
				,PRB.Keys.prbrecBankName.key()
				,PRB.Keys.prbPayMentAccountName.key()
				,PRB.Keys.vcr2Status.key()
				,PRB.Keys.prbEACompanyName.key()
				,PRB.Keys.payDay.key()
				,PRB.Keys.createDay.key()
				,PRB.Keys.createTime.key()
				,PRB.Keys.payLine.key()
				,PRB.Keys.isManual.key()
				
		);
		q.cd4dr(getPrbCreate(), "t."+PR.Keys.createDay);
		q.cd4dr(getPrbPay(), "t."+PR.Keys.payDay);
		q.appendField(","+defaultCMD.specially().moneyFormatFloat2(
				"t."+PRB.Keys.moneys)+" "+PRB.Keys.moneys+"Text");
		q.appendField(","+PRB.PayLine.caseSQL(
				"t."+PRB.Keys.payLine)
				+" "+PRB.Keys.payLine+"Text");
		
		if(rpExist("minMoney")) q.addWhere("t."+PRB.Keys.moneys+">="+rp("minMoney"));
		if(rpExist("maxMoney")) q.addWhere("t."+PRB.Keys.moneys+"<="+rp("maxMoney"));
		q.addWhere_in("t."+PRB.Keys.payOperator,  rp("payOprt"), true);
		q.addWhere_like("t."+PRB.Keys.prbrecBankAccountNumber, rp("recBAcNb"), true);
		q.addWhere_like("t."+PRB.Keys.prbrecBankAccountName, rp("recBAcNm"), true);
		q.addWhere_like("t."+PRB.Keys.prbrecBankName, rp("recBNm"), true);
		q.addWhere_in("t."+PRB.Keys.payKey, rp("PRBpaykey"), true);
		//if(rpExist("eaCpy")) {
			
			q.addWhere_in("t."+PRB.Keys.prbEACompany,rp("eaCpy"), true);
		//}else {
			q.addWhere_in("t."+PRB.Keys.prbEACompany, SQL.collection2IDs(eaCpys));
	//	}
		
		q.setOrderBy(PRB.Keys.payDay);
		q.setOrderByWay("desc");
		
		//限制仅查询当前凭证的分录
		//q.addWhere_equals(InvoiceDetail.Keys.pr, rp("r"));
		
		TableString s=new TableString(q);
		s.addTableColumn("付款单编号", PRB.Keys.payKey.key());
		//s.addTableColumn("费用报销公司编码", PRB.Keys.prbEACompany);
		s.addTableColumn("支付方式", PRB.Keys.payLine+"Text");
		s.addTableColumn("费用报销公司", PRB.Keys.prbEACompanyName.key());
		s.addTableColumn("总金额",null,PRB.Keys.moneys+"Text","money");
		s.addTableColumn("收款人银行账户名", PRB.Keys.prbrecBankAccountName);
		s.addTableColumn("收款人银行账户", PRB.Keys.prbrecBankAccountNumber);
		s.addTableColumn("收款人银行开户行", PRB.Keys.prbrecBankName);
		s.addTableColumn("付款账户", PRB.Keys.prbPayMentAccount);
		s.addTableColumn("付款账户名称", PRB.Keys.prbPayMentAccountName.key());
		//s.addTableColumn("生成付款单日期", PRB.Keys.createDay);
		s.addTableColumn("付款日期", PRB.Keys.prbGLDay);
		s.addTableColumn("是否付款", PRB.Keys.vcr2Status);
		s.addTableColumn("是否手动付款", PRB.Keys.isManual);
		s.addTableColumn("操作人", PRB.Keys.payOperatorName.key());
		s.addClickLink("修改", "Edit()", PRB.pk());
		s.addClickLink("明细", "InvD()", PRB.pk());
		//s.addClickLink("手动结束流程", "End()", PRB.pk());
		s.setTableType(TableString.Type.CheckBox);
		return s;
	}
	
	
	
	
	
	@Override
	protected String HTMLAbove() {
		StringBuilder pas=new StringBuilder();
		for(PR.PayLine payLine:PR.PayLine.values()){
			pas.append(""
					+"\n		function _x_"+payLine+"(){_x('"+payLine+"','"+payLine.t+"');}");
			
		}
	
		
		p2j(p2jKey.Msg, "PE", "_E.e8o.alert('线上支付失败，原因：<br/>"+rp("_t")+"');");
		p2j(p2jKey.Msg, "NoGLDay", "_E.e8o.alert('线下支付必须保证所有数据均填写[GL日期];');");
		p2j(p2jKey.Msg, "NoPaymentAccount", "_E.e8o.alert('线下支付必须保证所有数据均填写[付款公司];');");
		p2j(p2jKey.Msg, "WrongLine", "_E.e8o.alert('非法的付款方式;');");
		p2j(p2jKey.Msg, "WrongPay", "_E.e8o.alert('只能选择一条数据');");
		p2j(p2jKey.Msg, "WrongData", "_E.e8o.alert('非法的付款数据;');");
		p2j(p2jKey.Msg, "WrongRepeatPay", "_E.e8o.alert('不能重复支付;');");
		p2j(p2jKey.Msg, "WrongType", "_E.e8o.alert('不能同时操作[非预付款]和[预付款]的业务数据;');");
		return ""+X.JavaSctipt(p2j()
				+"\n		function Edit(id){"
				+"\n			var p=id?('?p-id='+id):'';"
				+"\n			_E.e8o.open("+new EDP(EU(PRBEdit.class)+"'+p+'"
						, 500, 600, PRBEdit.Title)+");"
				+"\n		}"
				+"\n		function InvD(id){"
				+"\n			var p=id?('?pr='+id):'';"
				+"\n			_E.e8o.open("+new EDP(EU(PRList.class)+"'+p+'"
						, 1000, 1300, PRList.Title)+");"
				+"\n		}"
				+"\n		function _x(t,s){"
				+"\n			_E.v('n',t);"
				+"\n			_selectedItemSender('k','"+EU(this,"_x")+"'"
+",'确定要以"+X.cs("'+s+'", "#f00", true)+"的方式对所选的数据进行付款操作吗?'"
+",'请先选择数据，再进行付款操作;');"
+"\n		}"
+"\n		"+pas
+"\n		"
+"\n		function Vcr(k){"
+"\n			_E.e8o.alert('确定要重新处理ORACLE业务吗?',function(){"
+"\n				var a={"
+"\n					url:'"+EU(this,"_v")+"?k='+k,type:'post'"
+"\n					,success:function(d){"
+"\n						if(!!d)_E.e8o.alert(d);"
+"\n					}"
+"\n				};"
+"\n				jQuery.ajax(a);"
+"\n			});"
+"\n		}"
+"\n		function End(){"
+"\n			_selectedItemSender('k','"+EU(this,"_v")+"'"
+",'确定已经全部在ORACLE手工录入该业务吗?'"
+",'请先选择数据，再进行操作;');"
+"\n		}"
				);
	}
	
	public static final String Title="付款单";
	
	
	@Override
	protected String p2successText() {return ""+X.cs(super.p2successText(), "#97f", true);}
	protected String p2failText() {return ""+X.cs(super.p2failText(), "#970", true);}
	@Override
	protected void moreMenus(List<MoreMenu> menus) {
		menus.add(menuSelect);
		menus.add(menuReselect);
		//此处应该有控制，已完成的不能再出现按钮
			for(PR.PayLine payLine:PR.PayLine.values()){
				menus.add(Menu.create(payLine.t+"付款", "_x_"+payLine+"()").setInTop(true));
				
			}
			menus.add(Menu.create("手工结束流程", "End()").setInTop(true));
		menus.add(menuExport);
	}
	@Override
	protected TopTitle topTitle() {
		return new TopTitle(Title, Image.Document.file);
	}
	


}
