package com.westvalley.tempus.oracle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.westvalley.tempus.carry.fncl.EAPaybankAccount;
import com.westvalley.tempus.carry.fncl.InvoiceDetail;
import com.westvalley.tempus.carry.fncl.PR;
import com.westvalley.tempus.carry.fncl.PRB;
import com.westvalley.tempus.carry.oracle.OVD;
import com.westvalley.tempus.carry.oracle.OVM;

import crivia.db.common.SQL;
import crivia.ecp.carry.ec.hrm.HR;
import crivia.ecp.common.ECR;
import crivia.ecp.common.EcologyDB;
import crivia.time.carry.Day;
import crivia.txt.common.Logger;
import crivia.txt.sc.RK;

public class VCR {
	
	public static String x(PR r,String prbk){
		//准备数据
		OVM vm;
		List<OVD> vds=new ArrayList<OVD>();
		//将各流程中的数据映射到ORACLE定义的数据结构中
		{
			crivia.db.i.CMD db=EcologyDB.db();
			String recPsnCode=p2c(r.getVdRecPsn(),r.getVdEACompanyName());
			vm=new OVM()
				.setId(RK.rk())
				.setFprocessinstanceid(r.getRequestID())
//				.setFcostcompany(SQL.relation(r.getVdEACompany()
//					, EACpy.table(), EACpy.Keys.code, EACpy.pk(), db))
				.setFinvoicedate(RK.rk())
				.setFcostcompany(r.getVdEACompany())
				.setFtotalamount(r.getMoney())
				.setFcreator(p2r(r.getVcrCreator()))
				.setFinvoicedate(r.getVcrCreateDay())
				.setFpaymentdate(RK.rk())
				.setFheaderSummary(r.getVcrHeaderSummary())
				.setFcurrency(SQL.relation(r.getVdCurrency()
					, "uf_hlhzb", "tb_currency", "id", db))
				.setFcompanyname(r.getVdEACompanyName())
				.setFinvoicedescription(r.getVdInvoiceDesc())
				.setFpaymentmethod(r.getVdPayMentMethod())
				.setFpayee(ECR.hasCondition(recPsnCode)
						?recPsnCode:r.getVdRecPsn())
				.setFpaymentaccount(SQL.relation(r.getVdPayMentAccount()
					, EAPaybankAccount.table()
					, EAPaybankAccount.Keys.paybankacount
					, EAPaybankAccount.Keys.paybankid
					, db))
				.setFcombinationid(prbk)
				;
			for(InvoiceDetail d:SQL.list(db, InvoiceDetail.class
					,SQL.cd.equals(InvoiceDetail.Keys.pr,r.getId()))){
				vds.add(new OVD()
					.setId(RK.rk())
					.setFparentID(r.getRequestID())
					.setFseq(d.getDspOrder())
					.setFamount(d.getMoney())
					.setFcontent(d.getDesc())
					.setFcostcategory(d.getSubjectCode())
					.setFcostcenter(d.getCostCenterCode())
					.setFproject(d.getProjectCode())
					.setFassociatedUnit(d.getAssociatedUnitCode())
					
				);
			}
		}//数据准备完毕
		//对接ORACLE数据库
		crivia.db.i.CMD db=EcologyDB.db("oracle"
			,new crivia.db.a.Specially_Oracle());
		//写入ORACLE数据库
		String sql=SQL.insertSQL(vm,db)
			.replaceAll(SQL.sql.sv(vm.getFinvoicedate())
				, db.specially().toDate(SQL.sql.sv(r.getVcrCreateDay())))
			.replaceAll(SQL.sql.sv(vm.getFpaymentdate())
				, db.specially().toDate(SQL.sql.sv(r.getVdGLDay())));
		SQL.edit(sql,db);
		for(OVD vd:vds){
			SQL.insert(vd,db);
		}
		//准备调用存储过程生成发票
		java.sql.Connection connection=null;
		java.sql.CallableStatement call=null;
//		java.sql.CallableStatement call2=null;
		try {
			connection=EcologyDB.jdbc("oracle").connection();
			if(ECR.hasCondition(r.getVdInvoiceDesc())){
				call=connection.prepareCall("{call TB_OA_INVOICE_IMPORT_PKG.IMPORT_FVOA_INVOICE_PAYMENT(?,?,?,?)}");
			}else{
				call=connection.prepareCall("{call TB_OA_INVOICE_IMPORT_PKG.IMPORT_FVOA_INVOICE(?,?,?,?)}");
			}
			call.setString(1,r.getRequestID());
			call.registerOutParameter(2, java.sql.Types.VARCHAR);
			call.registerOutParameter(3, java.sql.Types.VARCHAR);
			call.registerOutParameter(4, java.sql.Types.VARCHAR);
			call.execute();
//			if(call.execute()){
			vm=SQL.one(db, OVM.class, true
					,SQL.cd.equals(OVM.Keys.fprocessinstanceid, r.getRequestID()));
			r
				.setVcrCreateDay(Day.toDay(vm.getFinvoicedate()).toString())
				.setVcrCreator(vm.getFvcrCreator())
				.setVcrErrorInfo(vm.getFvcrErrorInfo())
				.setVcrN(vm.getFinvoiceID())
				.setVcrStatus(vm.getFvcrStatus());
			SQL.update(r, EcologyDB.db()
					
					,PR.Keys.vcrCreator.key()
					,PR.Keys.vcrErrorInfo.key()
					,PR.Keys.vcrN.key()
					,PR.Keys.vcrStatus.key());
//			}
			if("S".equals(vm.getFvcrStatus())){
//				call2=connection.prepareCall("{call TB_OA_PAYMENT_IMPORT_PKG.PAYMENT_FVOA_REIMBURSEMENT(?,?,?,?)}");
//				call2.setString(1,r.getRequestID());
//				call2.registerOutParameter(2, java.sql.Types.VARCHAR);
//				call2.registerOutParameter(3, java.sql.Types.VARCHAR);
//				call2.registerOutParameter(4, java.sql.Types.VARCHAR);
//				call2.execute();
			}
		} catch (java.sql.SQLException e) {
			Logger.logException("ORACLE INVOICE CREATE FAIL", e);
			throw new RuntimeException(e);
		} finally {
			try {
				if(null!=call)call.close();
			} catch (java.sql.SQLException e2) {}
//			try {
//				if(null!=call2)call2.close();
//			} catch (java.sql.SQLException e2) {}
			try {
				if(null!=connection)connection.close();
			} catch (java.sql.SQLException e2) {}
		}
//		weaver.conn.RecordSetDataSource rs=new weaver.conn.RecordSetDataSource("oracle");
		//调用存储过程，传入requestID作为参数
//		rs.executeProc("IMPORT_FVOA_INVOICE", r.getRequestID());
//		if(rs.next()){
//			//接收发票ID并记录凭证号
//			@SuppressWarnings("unused")
//			String vID=rs.getString(1);
//			vm=SQL.one(db, OVM.class, true
//					,SQL.cd.equals(OVM.Keys.fprocessinstanceid, r.getRequestID()));
//			r
//				.setVcrCreateDay(vm.getFinvoicedate())
//				.setVcrCreator(vm.getFvcrCreator())
//				.setVcrErrorInfo(vm.getFvcrErrorInfo())
//				.setVcrN(vm.getFinvoiceID())
//				.setVcrStatus(vm.getFvcrStatus());
//			SQL.update(r, EcologyDB.db()
//					,PR.Keys.vcrCreateDay.key()
//					,PR.Keys.vcrCreator.key()
//					,PR.Keys.vcrErrorInfo.key()
//					,PR.Keys.vcrN.key()
//					,PR.Keys.vcrStatus.key());
//		}
		return null;
	}
	
	public static String x2(String prbk,boolean isVCRX){
		//准备调用存储过程进行oracle付款操作
		java.sql.Connection connection=null;
		java.sql.CallableStatement call2=null;
		try {
			//对接ORACLE数据库
			connection=EcologyDB.jdbc("oracle").connection();
			if(isVCRX){
				call2=connection.prepareCall("{call TB_OA_PAYMENT_IMPORT_PKG.FVOA_PAYMENT_CONTRACT(?,?,?)}");
			}else{
				call2=connection.prepareCall("{call TB_OA_PAYMENT_IMPORT_PKG.PAYMENT_FVOA_REIMBURSEMENT(?,?,?)}");
			}
			call2.setString(1,prbk);
			call2.registerOutParameter(2, java.sql.Types.VARCHAR);
			call2.registerOutParameter(3, java.sql.Types.VARCHAR);
//			call2.registerOutParameter(4, java.sql.Types.VARCHAR);
			call2.execute();
			
		} catch (java.sql.SQLException e) {
			Logger.logException("ORACLE PAYMENT CREATE FAIL", e);
			throw new RuntimeException(e);
		} finally {
			try {
				if(null!=call2)call2.close();
			} catch (java.sql.SQLException e2) {}
			try {
				if(null!=connection)connection.close();
			} catch (java.sql.SQLException e2) {}
		}
		
		
		SQL.edit(SQL.sql.array("update "+PRB.table()
			+" set "+PRB.Keys.vcr2Status+"=?"
			+" where "+PRB.pk()+"=?"
			, "Y" , prbk
		),EcologyDB.db());
		for(OVM v:SQL.list(EcologyDB.db("oracle",new crivia.db.a.Specially_Oracle()), OVM.class
				,SQL.cd.equals(OVM.Keys.fcombinationid, prbk))){
			SQL.edit(SQL.sql.array("update "+PR.table()
				+" set "+PR.Keys.vcrStatus+"=?"
				+" , "+PR.Keys.vcrErrorInfo+"=?"
				+" where "+PR.Keys.requestID+"=?"
				, v.getFvcrStatus() 
				, v.getFvcrErrorInfo()
				, v.getFprocessinstanceid()
			),EcologyDB.db());
		}
		return null;
	}
	
	private static String p2c(String p,String cpy){
		Logger.log("判断费用报销公司是否为空"+cpy);
		if(!ECR.hasCondition(cpy)){
			
		int pp=ECR.toNumber(SQL.relation(p
			, HR.table(), "BeLongTo", HR.pk(), EcologyDB.db()), 0, int.class);
		Logger.log("查询"+pp);
		if(pp>0)p=""+pp;
		return SQL.relation(p, HR.table(), HR.Keys.workCode, HR.pk(), EcologyDB.db());
		}else {
			return null;
		}
	}
	private static String p2r(String p){
		
		int pp=ECR.toNumber(SQL.relation(p
			, HR.table(), "BeLongTo", HR.pk(), EcologyDB.db()), 0, int.class);
		if(pp>0)p=""+pp;
		return SQL.relation(p, HR.table(), HR.Keys.workCode, HR.pk(), EcologyDB.db());
		
	}
}
