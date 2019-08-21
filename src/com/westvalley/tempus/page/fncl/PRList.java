package com.westvalley.tempus.page.fncl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.westvalley.tempus.carry.fncl.EACpy;
import com.westvalley.tempus.carry.fncl.InvoiceDetail;
import com.westvalley.tempus.carry.fncl.PR;
import com.westvalley.tempus.carry.fncl.PRB;
import com.westvalley.tempus.carry.fncl.Receipt;
import com.westvalley.tempus.carry.oracle.OVD;
import com.westvalley.tempus.carry.oracle.OVM;
import com.westvalley.tempus.oracle.VCR;

import crivia.db.common.SQL;
import crivia.db.exception.NoData;
import crivia.eca.a.EcologyCMD;
import crivia.eca.a.EcologyCMD_Trans;
import crivia.ecp.carry.eb.TabPage.Image;
import crivia.ecp.carry.eb.MoreMenu;
import crivia.ecp.carry.eb.TopTitle;
import crivia.ecp.carry.ec.workflow.WFRequestBase;
import crivia.ecp.carry.em.Browser;
import crivia.ecp.carry.em.DayRange;
import crivia.ecp.carry.em.EDP;
import crivia.ecp.carry.em.EditTable;
import crivia.ecp.carry.em.Editer;
import crivia.ecp.carry.html.X;
import crivia.ecp.carry.html.form.Form;
import crivia.ecp.carry.html.form.Input;
import crivia.ecp.carry.html.form.Select;
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
import crivia.txt.sc.RK;
/**
 * 支付管理主界面<br>
 * 本质上是查询付款数据的主数据列表<br>
 * 用户可在此进行查询、支付、修改账户信息等操作<br>
 */
public class PRList extends Lister {
	
	public enum PRType {
		All,EA,Pay
		;
		public static PRType k2e(String k){
			try {
				return valueOf(k);
			} catch (Exception e) {
				return All;
			}
		}
	}
	
	public enum pay_status {//线下支付状态（成功，失败，初始）
		success,fail,initial
		;
		public static pay_status k2e(String k){
			try {
				return valueOf(k);
			} catch (Exception e) {
				return initial;
			}
		}
	}

	public static final String Title = "付款单明细表";
	
	
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
	public Page _r() {
		//接收被操作的数据ID和付款方式
				String k=rp("k"),n=rp("n");
				//错误的数据ID
				if(!ECR.hasCondition(k))return Page.forward(EU(this)+"?"+p2jUPMsg("WrongData"));
				//根据数据ID查询被操作的数据
				List<PR> prs=SQL.list(defaultCMD, "order by "+defaultCMD.specially()
							.toInt("t."+PR.Keys.requestID)+" asc" //按requestID排序
						, PR.class //查询PR主数据
						,SQL.cd.in(PR.pk(), k) //条件1 ID in K
				); 
				boolean isVCRX=false,noVCRX=false,nocompany=false,ispay=false,noRecBankAccountNumber=false,noRecBankAccountName=false,noRecBankName=false;
				
				String company =prs.get(0).getVdEACompany();
				String recBankAccountNumber = prs.get(0).getRecBankAccountNumber();
				String recBankAccountName = prs.get(0).getRecBankAccountName();
				String recBankName = prs.get(0).getRecBankName();
				double moneys =0.00;
				Set<String> recPsns=new HashSet<String>();
				for(PR r:prs){
					recPsns.add(r.getVdRecPsn());
					if(!company.equals(r.getVdEACompany())){
						nocompany=true;
					}
					if(!recBankAccountNumber.equals(r.getRecBankAccountNumber())){
						noRecBankAccountNumber=true;
					}if(!recBankAccountName.equals(r.getRecBankAccountName())){
						noRecBankAccountName=true;
					}if(!recBankName.equals(r.getRecBankName())){
						noRecBankName=true;
					}
					
					if(!"Unpay".equals(r.getPayStatus())){
						ispay=true;
					}
					if(ECR.hasCondition(r.getVdInvoiceDesc())){
						isVCRX=true;
					}else{
						noVCRX=true;
					}
					moneys+=r.getMoney();
				}
				if(ispay)return Page.forward(EU(this)+"?"+p2jUPMsg("WrongIsPay"));
				if(nocompany)return Page.forward(EU(this)+"?"+p2jUPMsg("WrongCompany"));
				if(noRecBankAccountNumber)return Page.forward(EU(this)+"?"+p2jUPMsg("WrongRecBankAccountNumber"));
				if(noRecBankAccountName)return Page.forward(EU(this)+"?"+p2jUPMsg("WrongRecBankAccountName"));
				if(noRecBankName)return Page.forward(EU(this)+"?"+p2jUPMsg("WrongRecBankName"));
				if(prs.size()>1&&isVCRX)return Page.forward(EU(this)+"?"+p2jUPMsg("WrongPay"));
				if(recPsns.size()>1)return Page.forward(EU(this)+"?"+p2jUPMsg("WrongRecPsn"));
				//存在两种凭证
				if(isVCRX&&noVCRX)return Page.forward(EU(this)+"?"+p2jUPMsg("WrongType"));
				//查询不到数据也会返回错误数据
				
				if(prs.size()<1)return Page.forward(EU(this)+"?"+p2jUPMsg("WrongData"));
				//验证付款方式
				//PR.PayLine payLine;
				//try {
				//	payLine=PR.PayLine.valueOf(n);
				//} catch (Exception e) {
					//错误的付款方式
				//	return Page.forward(EU(this)+"?"+p2jUPMsg("WrongLine"));
				//}
				
				//启动事务
				EcologyCMD_Trans cmd=new EcologyCMD_Trans();
				cmd.begin();cmd.isThrow=true;try {
					//制造批次数据
					PRB b=new PRB()
						.setId(RK.rk())
						.setPayKey(prs.get(0).getRequestID())
						//.setPayLine(""+payLine)
						.setCreator(prs.get(0).getCreator())
						.setCreateDay(""+Day.today())
						.setCreateTime(""+Time.now())
						.setPayOperator(""+user.getUID())
						.setPrbEACompany(prs.get(0).getVdEACompany())
						.setPrbrecBankAccountNumber(prs.get(0).getRecBankAccountNumber())
						.setPrbrecBankAccountName(prs.get(0).getRecBankAccountName())
						.setPrbrecBankName(prs.get(0).getRecBankName())
						.setMoneys(moneys)
						;			
					SQL.insert(b, cmd);
						//线下支付
						for(PR r:prs){
							//直接将相关数据修改为支付成功
							r.setVcrCreateDay(""+Day.today());
							SQL.update(r.setPrb(b).setPayStatus(""+PR.PayStatus.Edited),cmd
									,PR.Keys.prb.key()
									,PR.Keys.payStatus.key()
									,PR.Keys.vcrCreateDay.key());
						}
						cmd.end(true);

				} catch (Exception e){
					cmd.isThrow=false;
					cmd.end(false);
					Logger.logException("生成付款单失败", e);
				}
				
		return Page.forward(EU(this)+"?"+p2jUPMsg(p2jValue.Success));	
	}
	
	
	
	
	/**支付操作的后台POST请求接收函数*/
	@ExecuteMethod(ExecuteMethod.Type.Post)
	public Page _x(){
		//接收被操作的数据ID和付款方式
		String k=rp("k"),n=rp("n");
		//错误的数据ID
		if(!ECR.hasCondition(k))return Page.forward(EU(this)+"?"+p2jUPMsg("WrongData"));
		//根据数据ID查询被操作的数据
		List<PR> prs=SQL.list(defaultCMD, "order by "+defaultCMD.specially()
					.toInt("t."+PR.Keys.requestID)+" asc" //按requestID排序
				, PR.class //查询PR主数据
				,SQL.cd.in(PR.pk(), k) //条件1 ID in K
		); 
		boolean isVCRX=false,noVCRX=false,nocompany=false
				,noGLDay=false,noPaymentAccount=false;
		String company =prs.get(0).getVdEACompany();
		
		Set<String> recPsns=new HashSet<String>();
		for(PR r:prs){
			if(!ECR.hasCondition(r.getVdGLDay()))noGLDay=true;
			if(!ECR.hasCondition(r.getVdPayMentAccount()))noPaymentAccount=true;
			recPsns.add(r.getVdRecPsn());
			if(!company.equals(r.getVdEACompany())){
				nocompany=true;
			}
			if(ECR.hasCondition(r.getVdInvoiceDesc())){
				isVCRX=true;
			}else{
				noVCRX=true;
			}
		}
		if(nocompany)return Page.forward(EU(this)+"?"+p2jUPMsg("WrongCompany"));
		if(prs.size()>1&&isVCRX)return Page.forward(EU(this)+"?"+p2jUPMsg("WrongPay"));
		if(noGLDay)return Page.forward(EU(this)+"?"+p2jUPMsg("NoGLDay"));
		if(noPaymentAccount)return Page.forward(EU(this)+"?"+p2jUPMsg("NoPaymentAccount"));
		if(recPsns.size()>1)return Page.forward(EU(this)+"?"+p2jUPMsg("WrongRecPsn"));
		//存在两种凭证
		if(isVCRX&&noVCRX)return Page.forward(EU(this)+"?"+p2jUPMsg("WrongType"));
		//查询不到数据也会返回错误数据
		
		if(prs.size()<1)return Page.forward(EU(this)+"?"+p2jUPMsg("WrongData"));
		//验证付款方式
		PR.PayLine payLine;
		try {
			payLine=PR.PayLine.valueOf(n);
		} catch (Exception e) {
			//错误的付款方式
			return Page.forward(EU(this)+"?"+p2jUPMsg("WrongLine"));
		}
		
		//启动事务
		EcologyCMD_Trans cmd=new EcologyCMD_Trans();
		cmd.begin();cmd.isThrow=true;try {
			//制造批次数据
			PRB b=new PRB()
				.setId(RK.rk())
				.setPayKey(prs.get(0).getRequestID())
				.setPayLine(""+payLine)
				.setPayDay(""+Day.today())
				.setPayTime(""+Time.now())
				.setPayOperator(""+user.getUID())
				;
			if(PR.PayLine.OnLine.equals(payLine)){			
				String mainRequestID="0";
				for(PR r:prs)mainRequestID=r.getRequestID();
				SQL.insert(b.setPayKey(mainRequestID), cmd);
				//线上支付
				for(PR r:prs){
					//将相关数据修改为支付中
					SQL.update(r.setPrb(b).setPayStatus(""+PR.PayStatus.Payed),cmd
							,PR.Keys.prb.key()
							,PR.Keys.payStatus.key());
				}
				cmd.end(true);
				//并组织数据调用资金系统接口
				try {
				//	com.westvalley.tempus.ps.PSM.x(prs,mainRequestID);
				} catch (Exception e) {
					Logger.log("线上支付出现异常 > "+Logger.hiddenBox(crivia.txt.common.Information.exceptionInfo(e,2)));
					return Page.forward(EU(this)+"?"+p2jUPMsg("PE")+"&_t="+e.getMessage());
				}
			}else{			
				SQL.insert(b, cmd);
				//线下支付
				for(PR r:prs){
					//直接将相关数据修改为支付成功
					SQL.update(r.setPrb(b).setPayStatus(""+PR.PayStatus.Success),cmd
							,PR.Keys.prb.key()
							,PR.Keys.payStatus.key());
				}
				cmd.end(true);
			}
			boolean isVCR2=true;
			for(PR r:prs){
				if(PR.PayStatus.Success.equals(r.getPayStatusX()))VCR.x(r,b.getId());
				if(!"S".equals(r.getVcrStatus()))isVCR2=false;
			}
			if(isVCR2)VCR.x2(b.getId(),isVCRX);
		} catch (Exception e){
			cmd.isThrow=false;
			cmd.end(false);
			Logger.logException("支付操作失败", e);
		}
		return Page.forward(EU(this)+"?"+p2jUPMsg(p2jValue.Success));
	}
	

	/**补写凭证的后台POST请求接收函数*/
	@ExecuteMethod(ExecuteMethod.Type.Post)
	public Page _v(){
		String prbk;
		try {
			PR r=SQL.one(defaultCMD, PR.class, true
					,SQL.cd.equals(PR.pk(), rp("k")));
			if(!PR.PayStatus.Success.equals(r.getPayStatusX()))return Page.write("该业务未支付成功，不能处理ORACLE业务;");
			if("SS".equals(r.getVcrStatus()))return Page.write("该业务数据的ORACLE业务已经完成;");
			prbk=r.getPrb().getId();
		} catch (NoData e) {
			return Page.write("未能取得业务数据;");
		}
		List<PR> prs=SQL.list(defaultCMD, PR.class
				,SQL.cd.equals(PR.Keys.prb, prbk));
		Set<String> rks=new HashSet<String>();
		for(PR r2:prs) rks.add(r2.getRequestID());
		boolean rebuildVCR=true;
		boolean isVCRX=false,noVCRX=false;
		for(PR r2:prs){
			if(ECR.hasCondition(r2.getVdInvoiceDesc())){
				isVCRX=true;
			}else{
				noVCRX=true;
			}
			
			if(!PR.PayStatus.Success.equals(r2.getPayStatusX()))
				return Page.write("该业务数据同批次的其他业务存在未成功发支付业务;"
						+X.br()+"需要等待支付状态更新为[支付成功]后才可以进行ORACLE业务;"
						+X.br()+"关联业务流程编号："+SQL.collection2IDs(rks));
			if("S".equals(r2.getVcrStatus()))rebuildVCR=false;
			if("SS".equals(r2.getVcrStatus()) || "S".equals(r2.getVcrStatus()))
				return Page.write("该业务数据同批次的其他业务数据的ORACLE业务已经完成;"
						+X.br()+"请核查！"
						+X.br()+"关联业务流程编号："+SQL.collection2IDs(rks));
//			else rks.add(r2.getRequestID());
		}
		//存在两种凭证
		if(isVCRX&&noVCRX)return Page.write("该业务数据存在两种凭证业务;"
				+X.br()+"请联系管理员核查数据;"
				+X.br()+"关联业务流程编号："+SQL.collection2IDs(rks));
		if(rebuildVCR){
			EcologyCMD db=EcologyDB.db("oracle");
			SQL.edit(SQL.sql.array("delete "+OVD.table()
				+" where "+OVD.Keys.fparentID+" in (?)"
				, SQL.collection2IDs(rks).replaceAll(",", "','")
			),db);
			SQL.edit(SQL.sql.array("delete "+OVM.table()
				+" where "+OVM.Keys.fcombinationid+"=?"
				, prbk
			),db);
			for(PR r2:prs)VCR.x(r2, prbk);
		}
		boolean isVCR2=true;
		for(PR r2:prs){
			if(!"S".equals(r2.getVcrStatus()))isVCR2=false;
		}
		if(isVCR2)VCR.x2(prbk,isVCRX);
		return Page.write("ORACLE业务已重新处理;"+X.br()+"请注意关注ORACLE业务处理结果;"
				+X.br()+"关联业务流程编号："+SQL.collection2IDs(rks));
	}
	
	private boolean isUnpay=false;
	private Set<String> eaCpys=new HashSet<String>();
	private Set<String> eaSearchCpys=new HashSet<String>();
	private PRType Type=PRType.All;
	private pay_status Status=pay_status.initial;
	@Override
	protected Page breakPage() {
		isUnpay=rpExist("isUnpay");
		for(EACpy c:SQL.list(defaultCMD, EACpy.class
				,SQL.cd.like(
					defaultCMD.specially().concatString(
						SQL.sql.sv(","),""+EACpy.Keys.paymentOperator,SQL.sql.sv(","))
					,","+user.getUID()+","
				)
		)){
			eaCpys.add(c.getCode());
		}
		for(EACpy c:SQL.list(defaultCMD, EACpy.class
				,SQL.cd.like(
					defaultCMD.specially().concatString(
						SQL.sql.sv(","),""+EACpy.Keys.paymentSearchOperator,SQL.sql.sv(","))
					,","+user.getUID()+","
				)
		)){
			eaSearchCpys.add(c.getCode());
		}
		if(isUnpay&&eaCpys.size()<1)return Page.redirect(NORIGHT);
		if(eaCpys.size()<1&&eaSearchCpys.size()<1)return Page.redirect(NORIGHT);
		if(ECR.toNumber(rp("maxMoney"), -9527d, double.class)<-9000)rpSet("maxMoney","");
		if(ECR.toNumber(rp("minMoney"), -9527d, double.class)<-9000)rpSet("minMoney","");
//		rpInit("payStatus", ""+PR.PayStatus.Unpay);
		Type=PRType.k2e(rp("Type"));
		Status=pay_status.k2e(rp("Status"));
		return super.breakPage();
	}
	
	private DayRange drCreate;
	public DayRange getDrCreate() {
		if(null==drCreate){
			drCreate=new DayRange("drCreate");
		}
		return drCreate;
	}
	public void setDrCreate(DayRange drCreate) {this.drCreate = drCreate;}
	
	private DayRange drPay;
	public DayRange getDrPay() {
		if(null==drPay){
			drPay=new DayRange("drPay");
		}
		return drPay;
	}
	public void setDrPay(DayRange drPay) {this.drPay = drPay;}
	
	private DayRange drPayDay;
	public DayRange getDrPayDay() {
		if(null==drPayDay){
			drPayDay=new DayRange("drPayDay");
		}
		return drPayDay;
	}
	public void setDrPayDay(DayRange drPayDay) {this.drPayDay = drPayDay;}	

	
	
	@Override
	protected void selectForm(Form selectForm) {
		selectForm.addHidden("k","");
		selectForm.addHidden("n","");
		selectForm.addHidden("Type");
		selectForm.addHidden("isUnpay");
		selectForm.addHidden("Status");
		selectForm.addHidden("pr");
	}
	@Override
	protected void selectTable(EditTable selectTable) {
		selectTable.addRow(new Editer("关联业务流程编号", new Input(this, "requestKey")));
		selectTable.addRow(new Editer("申请日期", getDrCreate()));
		selectTable.addRow(new Editer("支付日期", getDrPay()));
		selectTable.addRow(new Editer("收款人", Browser.s_HrmMuti(this, "vdRP")));
		selectTable.addRow(new Editer("金额", new Input(this, "minMoney").addStyle("width", "100px")
			+"-"+new Input(this, "maxMoney").addStyle("width", "100px")));
		selectTable.addRow(new Editer("支付方式", PR.PayLine.select(this, "payLine")));
		selectTable.addRow(new Editer("支付状态", PR.PayStatus.select(this, "payStatus")));
		selectTable.addRow(new Editer("支付操作人", Browser.s_HrmMuti(this, "payOprt")));
		selectTable.addRow(new Editer("操作日期", getDrPayDay()));
		selectTable.addRow(new Editer("收款银行账号", new Input(this,"recBAcNb")));
		selectTable.addRow(new Editer("收款银行账户名", new Input(this,"recBAcNm")));
		selectTable.addRow(new Editer("收款银行名称", new Input(this,"recBNm")));
		selectTable.addRow(new Editer("收款供应商名称", new Input(this,"recSupply")));
		selectTable.addRow(new Editer("资金管理平台业务参考号", new Input(this, "payKey")));
		selectTable.addRow(new Editer("费用报销公司", Browser.create(this
				, Browser.BrowserURLSystemMultiple("FNCL_EA_Company"), "eaCpy").relation(EACpy.table(),EACpy.Keys.code, EACpy.Keys.name)));
	}

	@Override
	protected TableString table() {
		
		
		TableSQL q=new PR().simpleTableSQL(
				PR.Keys.payOperator.key()
				,PR.Keys.payKey.key()
				,PR.Keys.payLine.key());
		q.appendField(","+defaultCMD.specially().moneyFormatFloat2("t."+PR.Keys.money)
				+" "+PR.Keys.money+"Text");
		q.appendField(","+PR.PayLine.caseSQL(
				"st_"+PR.Keys.payLine.key()+"."+PR.Keys.payLine)
				+" "+PR.Keys.payLine+"Text");
		q.appendField(","+PR.PayStatus.caseSQL("t."+PR.Keys.payStatus)
		+" "+PR.Keys.payStatus+"Text");
		
		q.appendField(",st_"+PR.Keys.payLine.key()+"."+PRB.Keys.payTime +"  prbPayTime" );
		q.appendField(",(case t."+PR.Keys.vcrStatus
				+" when 'SS' then 'ORACLE业务完全成功'"
				+" when 'S' then 'ORACLE发票生成但付款失败'"
				+" when 'E' then 'ORACLE发票生成失败'"
				+" else '未知' end) vst");
		String requestLinkKey = "rlk";
		q.appendRequestLink("t."+PR.Keys.requestID, requestLinkKey);
		
		q.setFrom(q.getFrom()
				+" left join "+WFRequestBase.table()+" r on r."+WFRequestBase.pk()
					+" = t."+PR.Keys.requestID);
		
		if(isUnpay){
			q.addWhere_in(PR.Keys.vdEACompany, SQL.collection2IDs(eaCpys));
		}else{
			q.addWhere(SQL.cd.or(
				SQL.cd.in(PR.Keys.vdEACompany, SQL.collection2IDs(eaCpys))
				,SQL.cd.in(PR.Keys.vdEACompany, SQL.collection2IDs(eaSearchCpys))
			));
		}
		if(isUnpay){
			q.addWhere_in("t."+PR.Keys.payStatus, 
					""+PR.PayStatus.Unpay 
					+","+PR.PayStatus.Edited
					+","+PR.PayStatus.Fail);
			
		}//else{
			//rpInit("payStatus", 
				//	""+PR.PayStatus.Success 
				//	+","+PR.PayStatus.Payed
			//	+","+PR.PayStatus.Fail);
		//}
		if(!"".equals(rp("pr")) && rp("pr")!=null) {
			q.addWhere_equals(PR.Keys.prb, rp("pr"));
		}
		
		
		if(PRType.EA.equals(Type)){
			q.addWhere(defaultCMD.specially().isNull("t."+PR.Keys.vdEACompanyName));
//			q.addWhere("t."+PR.Keys.vdRecPsn+" in (select id from hrmResource)");
		}else if(PRType.Pay.equals(Type)){
			q.addWhere(defaultCMD.specially().isNotNull("t."+PR.Keys.vdEACompanyName));
//			q.addWhere("t."+PR.Keys.vdRecPsn+" not in (select id from hrmResource)");
		}
		if(pay_status.success.equals(Status)){
			q.addWhere("t."+PR.Keys.vcrStatus+"='SS' and "+"t."+PR.Keys.payStatus +"='"+PR.PayStatus.Success+"'");
		}else if(pay_status.fail.equals(Status)){
			q.addWhere("(t."+PR.Keys.vcrStatus+" in ('S','E') or  "+"t."+PR.Keys.payStatus +"='"+PR.PayStatus.Fail+"')");
		}
		
		
		q.addWhere(SQL.cd.or(
				SQL.cd.in("t."+PR.Keys.requestID, rp("requestKey"), true)
				,SQL.cd.like("r.requestMark", rp("requestKey"), true)
		));
		q.cd4dr(getDrCreate(), "t."+PR.Keys.createDay);
		q.cd4dr(getDrPay(), "t."+PR.Keys.payDay);
		q.cd4dr(getDrPayDay(), "st_"+PR.Keys.payLine.key()+"."+PRB.Keys.payDay);
		if(rpExist("minMoney")) q.addWhere("t."+PR.Keys.money+">="+rp("minMoney"));
		if(rpExist("maxMoney")) q.addWhere("t."+PR.Keys.money+"<="+rp("maxMoney"));
		if(!Select.DefaultValue.equals(rp("payLine")))
			q.addWhere_in("st_"+PR.Keys.payLine.key()+"."+PRB.Keys.payLine, rp("payLine"), true);
		if(!Select.DefaultValue.equals(rp("payStatus")))
			q.addWhere_in("t."+PR.Keys.payStatus, rp("payStatus"), true);
		q.addWhere_in("st_"+PR.Keys.payOperator.key()+"."+PRB.Keys.payOperator, rp("payOprt"), true);
		q.addWhere_like("t."+PR.Keys.recBankAccountNumber, rp("recBAcNb"), true);
		q.addWhere_like("t."+PR.Keys.recBankAccountName, rp("recBAcNm"), true);
		q.addWhere_like("t."+PR.Keys.recBankName, rp("recBNm"), true);
		q.addWhere_like("t."+PR.Keys.recSupplyName, rp("recSupply"), true);
		q.addWhere_in("st_"+PR.Keys.payKey.key()+"."+PRB.Keys.payKey, rp("payKey"), true);
		q.addWhere_in("t."+PR.Keys.vdEACompany, rp("eaCpy"), true);
		q.addWhere_in("t."+PR.Keys.vdRecPsn.key(), rp("vdRP"), true);
		
		TableString t=new TableString(q);
		t.addTableColumn("关联业务流程", requestLinkKey);
		t.addTableColumn("金额",null,PR.Keys.money+"Text","money");
		t.addTableColumn("申请日期", PR.Keys.createDay);
		t.addTableColumn("GL日期", PR.Keys.vdGLDay);
		t.addTableColumn("操作日期", PR.Keys.payDay.key());
		t.addTableColumn("操作时间", "prbPayTime");
		t.addTableColumn("付款银行账号", PR.Keys.vdPayMentAccountName.key());
		if(!PRType.Pay.equals(Type)){
			t.addTableColumn("收款人", PR.Keys.vdRecPsnName.key());
		}
		t.addTableColumn("支付方式", PR.Keys.payLine+"Text");
		t.addTableColumn("支付状态", PR.Keys.payStatus+"Text");
		t.addTableColumn("支付操作人", PR.Keys.payOperatorName.key());
		
		t.addTableColumn("收款银行账号", PR.Keys.recBankAccountNumber);
		t.addTableColumn("收款银行账户名", PR.Keys.recBankAccountName);
		t.addTableColumn("收款银行名称", PR.Keys.recBankName);
		t.addTableColumn("关联供应商名称", PR.Keys.recSupplyName);
		t.addTableColumn("流程ID", PR.Keys.requestID);
		t.addTableColumn("资金管理平台业务参考号", PR.Keys.payKey.key());
		t.addTableColumn("ORACLE凭证号", PR.Keys.vcrN);
		t.addTableColumn("ORACLE凭证生成状态", "vst");
		
//		t.addTableColumn("ORACLE凭证生成状态", PR.Keys.vcrStatus);
		t.addTableColumn("ORACLE凭证生成错误信息", PR.Keys.vcrErrorInfo);

		t.addClickLink("重新处理ORACLE业务", "Vcr()", PR.pk());
		//t.addClickLink("详细/修改", "Edit()", PR.pk());
		t.addClickLink("凭证分录", "InvD()", PR.pk());
		
		if(!isUnpay) t.setTableType(TableString.Type.None);
		else if(PRType.Pay.equals(Type))t.setTableType(TableString.Type.CheckBox);
		
		return t;
	}
	
	@Override
	protected String HTMLAbove() {
		StringBuilder pas=new StringBuilder();
		for(PR.PayLine payLine:PR.PayLine.values()){
			pas.append(""
					+"\n		function _x_"+payLine+"(){_x('"+payLine+"','"+payLine.t+"');}");
			
		}
		pas.append(""
				+"\n		function _r_(){_r();}");
		
		p2j(p2jKey.Msg, "PE", "_E.e8o.alert('线上支付失败，原因：<br/>"+rp("_t")+"');");
		p2j(p2jKey.Msg, "NoGLDay", "_E.e8o.alert('线下支付必须保证所有数据均填写[GL日期];');");
		p2j(p2jKey.Msg, "NoPaymentAccount", "_E.e8o.alert('线下支付必须保证所有数据均填写[付款公司];');");
		p2j(p2jKey.Msg, "WrongRecPsn", "_E.e8o.alert('只能选择相同的[收款人]进行批量支付;');");
		p2j(p2jKey.Msg, "WrongLine", "_E.e8o.alert('非法的付款方式;');");
		p2j(p2jKey.Msg, "WrongIsPay", "_E.e8o.alert('不能重复生成付款单;');");
		p2j(p2jKey.Msg, "WrongPay", "_E.e8o.alert('合同付款不能多条一起选择');");
		p2j(p2jKey.Msg, "WrongCompany", "_E.e8o.alert('只能选择相同的[费用报销公司]进行批量支付;');");
		p2j(p2jKey.Msg, "WrongRecBankAccountNumber", "_E.e8o.alert('只能选择相同的[收款银行账户]进行批量支付;');");
		p2j(p2jKey.Msg, "WrongRecBankAccountName", "_E.e8o.alert('只能选择相同的[收款银行账户名]进行批量支付;');");
		p2j(p2jKey.Msg, "WrongRecBankName", "_E.e8o.alert('只能选择相同的[收款银行]进行批量支付;');");
		
		p2j(p2jKey.Msg, "WrongData", "_E.e8o.alert('非法的付款数据;');");
		p2j(p2jKey.Msg, "WrongType", "_E.e8o.alert('不能同时操作[非预付款]和[预付款]的业务数据;');");
		return ""+X.JavaSctipt(p2j()
				+"\n		function Edit(id){"
				+"\n			var p=id?('?p-id='+id):'';"
				+"\n			_E.e8o.open("+new EDP(EU(PREdit.class)+"'+p+'"
						, 800, 900, PREdit.Title)+");"
				+"\n		}"
				+"\n		function InvD(id){"
				+"\n			var p=id?('?r='+id):'';"
				+"\n			_E.e8o.open("+new EDP(EU(InvoiceDetailList.class)+"'+p+'"
						, 800, 900, InvoiceDetailList.Title)+");"
				+"\n		}"
				+"\n		function _x(t,s){"
				+"\n			_E.v('n',t);"
				+"\n			_selectedItemSender('k','"+EU(this,"_x")+"'"
+",'确定要以"+X.cs("'+s+'", "#f00", true)+"的方式对所选的数据进行付款操作吗?'"
+",'请先选择数据，再进行付款操作;');"
				+"\n		}"
				+"\n		function _r(){"
				+"\n			_selectedItemSender('k','"+EU(this,"_r")+"'"
+",'确定要对所选的数据进行生成付款单吗?'"
+",'请先选择数据，再进行操作;');"
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
				);
	}
	@Override
	protected String p2successText() {return ""+X.cs(super.p2successText(), "#97f", true);}
	protected String p2failText() {return ""+X.cs(super.p2failText(), "#970", true);}
	 
	@Override
	protected void moreMenus(List<MoreMenu> menus) {
		menus.add(menuSelect);
		menus.add(menuReselect);
		if(isUnpay){
			//for(PR.PayLine payLine:PR.PayLine.values()){
				//menus.add(Menu.create(payLine.t+"付款", "_x_"+payLine+"()").setInTop(true));
			//	
			//}
			menus.add(Menu.create("生成付款单", "_r_()").setInTop(true));
		}
		menus.add(menuExport);
	}
	
	@Override
	protected TopTitle topTitle() {
		return new TopTitle(isUnpay?"未支付业务":"支付业务管理", Image.Document.file);
	}

}
