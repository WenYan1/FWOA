package com.westvalley.tempus.workflow;

import java.util.ArrayList;
import java.util.List;

import com.westvalley.tempus.carry.fncl.InvoiceDetail;
import com.westvalley.tempus.carry.fncl.PR;

import crivia.db.common.SQL;
import crivia.db.exception.NoData;
import crivia.db.i.DataSet;
import crivia.eca.Cat;
import crivia.eca.a.EcologyCMD_Trans;
import crivia.eca.am2.common.AM2;
import crivia.txt.common.Logger;
import crivia.txt.sc.RK;
/**
 * Action Model业务模型<br>
 * 业务模型因为其通用流程，字段、数据表不断变化的特殊性，所有的取值操作都需要通过AM2来进行。<br>
 * 关于Cat类本身的说明：<br>
 * Cat类是泛微流程节点附加接口的加强类，可在此进行取值、回写等操作。<br>
 * ECA ACT方法返回值为错误信息：<br>
 * -如返回<b>null</b>则表示执行没有错误<br>
 * -否则中断流程并显示错误信息到表单。<br>
 */
public class PRSave extends Cat {

	@Override
	protected String ecaAct() throws Exception {
		try {
			//检查是否存在业务数据
			PR r=SQL.one(defaultCMD, PR.class, true
					, SQL.cd.equals(PR.Keys.requestID, requestID));
			//检查业务数据的状态【部分状态不可编辑，将跳过这些数据】
			if(!r.getPayStatusX().reeditable){
				Logger.log("本流程已存在相关支付业务数据，不再写入支付业务数据;");
				return null;
			}
		} catch (NoData e) {}
		//构建新的付款申请
		PR r=new PR()
			.setId(RK.rk())
			.setRequestID(requestID)
			.setMoney(AM2.v(this, null, Keys.MoneySum, 0d))
			.setCreateDay(AM2.v(this, null, Keys.CreateDay))
			.setCreator(AM2.v(this, null, Keys.Creator))
			.setPayStatus(""+PR.PayStatus.Unpay)
			.setRecBankName(AM2.v(this, null, Keys.RecBankName))
			.setRecBankAccountNumber(AM2.v(this, null, Keys.RecBankAccountNumber))
			.setRecBankAccountName(AM2.v(this, null, Keys.RecBankAccountName))
			.setRecBankAccountID(AM2.v(this, null, Keys.RecBankAccountID))
			.setRecSupplyName(AM2.v(this, null, Keys.RecSupplyName))
			.setRecSupplyID(AM2.v(this, null, Keys.RecSupplyID))
			.setVcrCreateDay(AM2.v(this, null, Keys.VD_AcnDay))
			.setVcrHeaderSummary(AM2.v(this, null, Keys.VD_Summary))
			//VD
			.setVdEACompany(AM2.v(this, null, Keys.VD_EACpy))
			.setVdGLDay(AM2.v(this, null, Keys.VD_GLDay))
			.setVdCurrency(AM2.v(this, null, Keys.VD_Currency))
			.setVdEACompanyName(AM2.v(this, null, Keys.VD_EACompanyName))
			.setVdInvoiceDesc(AM2.v(this, null, Keys.VD_InvoiceDesc))
			.setVdPayMentMethod(AM2.v(this, null, Keys.VD_PayMentMethod))
			.setVdPayMentAccount(AM2.v(this, null, Keys.VD_PayMentAccount))
			.setVdRecPsn(AM2.v(this, null, Keys.VD_RecPsn))
			;
		//构建凭证（发票）分录
		List<InvoiceDetail> vds=new ArrayList<InvoiceDetail>();{
			DataSet d=subData(AM2.tableIndex(this, Keys.VD_Money));
			while(d.next()){
				InvoiceDetail vd=new InvoiceDetail()
					.setId(RK.rk())
					.setPr(r)
					.setDspOrder((vds.size()+1)*100)
					.setMoney(AM2.v(this, d, Keys.VD_Money, 0d))
					.setDesc(AM2.v(this, d, Keys.VD_Desc))
					.setSubjectID(AM2.v(this, d, Keys.VD_Subject))
					.setCostCenterID(AM2.v(this, d, Keys.VD_CostCenter))
					.setProjectID(AM2.v(this, d, Keys.VD_Project))
					.setAssociatedUnitID(AM2.v(this, d, Keys.VD_AssociatedUnit))
				;
				vds.add(vd);
			}
		}
		//开启事务
		EcologyCMD_Trans cmd=new EcologyCMD_Trans();
		cmd.begin();cmd.isThrow=true;try {
			//清除原有的业务数据
			SQL.edit(SQL.sql.array("delete "+InvoiceDetail.table()
					+" where "+InvoiceDetail.Keys.pr+" in (select "
						+PR.pk()+" from "+PR.table()
						+" where "+PR.Keys.requestID+" = ?)",requestID
			),cmd);
			SQL.edit(SQL.sql.array("delete "+PR.table()
					+" where "+PR.Keys.requestID+" = ?",requestID
			),cmd);
			//重新写入
			SQL.insert(r,cmd);
			for(InvoiceDetail vd:vds){
				SQL.insert(vd, cmd);
			}
			
			cmd.end(true);
			
			return null;
		} catch (Exception e) {
			cmd.isThrow=false;
			cmd.end(false);
			Logger.logException("PR SAVE FAIL.", e);
			return "支付数据保存失败（数据库操作失败）;";
		}
	}

	public enum Keys {
		/**总金额*/MoneySum
		,/**申请日期*/CreateDay
		,/**申请人*/Creator
		,/**收款银行账户ID*/RecBankAccountID
		,/**收款银行账号*/RecBankAccountNumber
		,/**收款银行账户名*/RecBankAccountName
		,/**收款银行名称*/RecBankName
		,/**收款供应商ID*/RecSupplyID
		,/**收款供应商名称*/RecSupplyName
		,/**凭证-费用报销公司*/VD_EACpy
		,/**凭证-GL日期*/VD_GLDay
		,/**凭证-凭证过账日期*/VD_AcnDay
		,/**凭证-币种*/VD_Currency
		,/**凭证-费用报销公司名称（合同付款）*/VD_EACompanyName
		,/**凭证-摘要（合同付款）*/VD_InvoiceDesc
		,/**凭证-付款方式（对应表单中的付款方式：check支票  win 电汇）*/VD_PayMentMethod
		,/**凭证-付款银行账号*/VD_PayMentAccount
		,/**凭证-收款人*/VD_RecPsn
		,/**凭证-金额*/VD_Money
		,/**凭证-摘要*/VD_Desc
		,/**凭证-科目*/VD_Subject
		,/**凭证-成本中心*/VD_CostCenter
		,/**凭证-项目段*/VD_Project
		,/**凭证-关联单位*/VD_AssociatedUnit
		,/**凭证-头摘要*/VD_Summary
	}

}
