package com.westvalley.tempus.timer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.westvalley.tempus.carry.fncl.PR;
import com.westvalley.tempus.carry.fncl.PRB;
import com.westvalley.tempus.carry.oracle.OVD;
import com.westvalley.tempus.carry.oracle.OVM;
import com.westvalley.tempus.oracle.VCR;

import crivia.db.common.SQL;
import crivia.eca.a.EcologyCMD;
import crivia.ecp.carry.html.X;
import crivia.ecp.common.ECR;
import crivia.ecp.common.EcologyDB;
import crivia.txt.common.Logger;

import weaver.interfaces.schedule.BaseCronJob;

public class OracleVoucherCreator extends BaseCronJob {
	
	@Override
	public void execute() {
		Logger.log("Oracle Voucher Creator Running ...");
		List<PRB> prbs=SQL.list(EcologyDB.db(), PRB.class
				, SQL.cd.equals(PRB.Keys.payLine, PR.PayLine.OnLine)
				, "(select count(1) from "+PR.table()+" s"
					+" where s."+PR.Keys.prb+"=t."+PRB.pk()
					+" and s."+PR.Keys.payStatus+"!="+SQL.sql.sv(PR.PayStatus.Success)+")=0"
//				, db.specially().isNull("t."+PRB.Keys.vcr2Status, SQL.sql.sv("N"))+"!="+SQL.sql.sv("Y")
		);
		Logger.log("查询已经支付成功的 "+SQL.LastSQL);
		for(PRB b:prbs){
			List<PR> prs=SQL.list(EcologyDB.db(), PR.class
					, SQL.cd.equals(PR.Keys.prb, b.getId())
			);
			Set<String> rks=new HashSet<String>();
			for(PR r:prs) rks.add(r.getRequestID());
			boolean rebuildVCR=true;
			boolean isVCRX=false,noVCRX=false;
			boolean isContinue=false;
			for(PR r:prs){
				if(ECR.hasCondition(r.getVdInvoiceDesc())){
					isVCRX=true;
				}else{
					noVCRX=true;
				}
				if(!PR.PayStatus.Success.equals(r.getPayStatusX())){
					isContinue=true;
					Logger.log("该业务数据同批次的其他业务存在未成功发支付业务;"
							+X.br()+"需要等待支付状态更新为[支付成功]后才可以进行ORACLE业务;"
							+X.br()+"关联业务流程编号："+SQL.collection2IDs(rks));
				}
				if("S".equals(r.getVcrStatus()))rebuildVCR=false;
				if("SS".equals(r.getVcrStatus()) || "S".equals(r.getVcrStatus())){
					isContinue=true;
					Logger.log("该业务数据同批次的其他业务数据的ORACLE业务已经完成;"
							+X.br()+"请核查！"
							+X.br()+"关联业务流程编号："+SQL.collection2IDs(rks));
				}
//				else rks.add(r.getRequestID());
			}
			if(isContinue)continue;
			if(isVCRX&&noVCRX){
				Logger.log("该业务数据存在两种凭证业务;"
					+X.br()+"请联系管理员核查数据;"
					+X.br()+"关联业务流程编号："+SQL.collection2IDs(rks));
				continue;
			}
			try {
				if(rebuildVCR){
					EcologyCMD db=EcologyDB.db("oracle");
					SQL.edit(SQL.sql.array("delete "+OVD.table()
						+" where "+OVD.Keys.fparentID+" in (?)"
						, SQL.collection2IDs(rks).replaceAll(",", "','")
					),db);
					SQL.edit(SQL.sql.array("delete "+OVM.table()
						+" where "+OVM.Keys.fcombinationid+"=?"
						, b.getId()
					),db);
					for(PR r2:prs)VCR.x(r2, b.getId());
				}
				boolean isVCR2=true;
				for(PR r2:prs){
					if(!"S".equals(r2.getVcrStatus()))isVCR2=false;
				}
				if(isVCR2)VCR.x2(b.getId(),isVCRX);
			} catch (Exception e) {
				Logger.logException("ORACLE业务数据执行失败;"
						+X.br()+"关联业务流程编号："+SQL.collection2IDs(rks),e);
			}
		}
		Logger.log("Oracle Voucher Creator Executed.");
	}

}
