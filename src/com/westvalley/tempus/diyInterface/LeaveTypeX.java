package com.westvalley.tempus.diyInterface;

import java.util.Map;

import crivia.db.common.SQL;
import crivia.db.i.DataSet;
import crivia.eca.Cat;
import crivia.src.EFParamReader;
import crivia.txt.common.Logger;
//com.westvalley.tempus.diyInterface.LeaveTypeX
public class LeaveTypeX extends Cat {

	@Override
	protected String ecaAct() throws Exception {
		//判断是否是提交或者退回 is  然后提示ALT+斜杠
		
		//取主表  String  s = mainValue("字段名");
		Map<String,String> vt4type=selectValueText(getRequestTableName(1), t1.tb_leave_type2);
		Map<String,String> tv4is_annual_leave=selectTextValue(getRequestTableName(), t0.is_annual_leave);
		Logger.log("vt4type > "+vt4type);
		Logger.log("tv4is_annual_leave > "+tv4is_annual_leave);
		//取明细表
		DataSet d1=subData(1);
		boolean leave_type = false;//是否是特殊假
		
		//调休年假天数
		double   tb_special_day=0.00;
		while(d1.next()) {
			String nameType=d1.get(t1.tb_leave_type2);
			if("年假".equals(vt4type.get(nameType))||"调休".equals(vt4type.get(nameType)) ) {
				//TODO.
				tb_special_day =tb_special_day+Double.parseDouble(d1.get(t1.tb_leave_days));//调休或年假天数
			}else {
				leave_type=true;
			}
		}
		//TODO
		if(leave_type){
			SQL.edit(SQL.sql.array("update "+getRequestTableName()
				+" set "+t0.is_annual_leave+" = ?,"+t0.tb_special_day+" =?"
				+" where requestID = ?"
				,tv4is_annual_leave.get("是")
				,tb_special_day
				,requestID
			),defaultCMD);
		}else{
			SQL.edit(SQL.sql.array("update "+getRequestTableName()
			+" set "+t0.is_annual_leave+" = ?,"+t0.tb_special_day+" =?"
			+" where requestID = ?"
			,tv4is_annual_leave.get("否")
			,tb_special_day
			,requestID
		),defaultCMD);
	}
		
		return null;
	}

	//生成下面t0,t1的方法  ，字符串参考http://172.18.240.59/ECADeployer.actionSetting.c
	//public static void main(String[] args) {
		//System.out.println(EFParamReader.toEFT("字符串贴进来"));
	//}

	
	/* Fields (Main Table) */
	public enum t0 { // XXX
		/** 调休或请假 integer 9497 */ is_annual_leave
		,/** 流程编号 varchar2(999) 6551 */ tb_Process_number
		,/** 交接人 clob 6568 */ tb_Transferee
		,/** 所属单位 int 6555 */ tb_affiliation
		,/** 申请人 integer 6552 */ tb_applicant
		,/** 申请日期 char(10) 6554 */ tb_date_of_application
		,/** 天数 number(15,2) 6562 */ tb_days
		,/** 扣薪假 varchar2(999) 7576 */ tb_deduction_leave2
		,/** 部门 integer 6553 */ tb_department
		,/** 入职日期 char(10) 6558 */ tb_entry_date
		,/** 全薪假 number(15,2) 6563 */ tb_full_pay_leave
		,/** 全薪假 varchar2(999) 7575 */ tb_full_pay_leave1
		,/** 工号 varchar2(999) 6556 */ tb_job_number
		,/** 请假结束日期 char(10) 6561 */ tb_leave_end_date
		,/** 请假结束时间 char(5) 7358 */ tb_leave_end_time
		,/** 请假开始日期 char(10) 6560 */ tb_leave_start_date
		,/** 请假开始时间 char(5) 7357 */ tb_leave_start_time
		,/** 请假类型 int 6559 */ tb_leave_type
		,/** 请假类型 integer 7572 */ tb_leave_type1
		,/** 请假事由 varchar2(4000) 6566 */ tb_please_leave_a_fake
		,/** 岗位 integer 6557 */ tb_post
		,/** 相关文档 integer 6570 */ tb_related_documents
		,/** 相关流程 integer 6569 */ tb_related_processes
		,/** 相关附件 varchar2(4000) 6571 */ tb_relevant_attachments
		,/** 共请假天数 number(15,2) 6565 */ tb_total_days_for_leave
		,/** 工作安排 varchar2(4000) 6567 */ tb_working_arrangements
		,tb_applicant_grade
		,tb_special_day
	}
	/** Fields (Sub Table 1) */
	public enum t1 { // XXX
		/** 请假结束日期 char(10) 9060 */ tb_end_date
		,/** 请假结束时间 char(5) 9058 */ tb_end_time
		,/** 请假天数 number(15,2) 9061 */ tb_leave_days
		,/** 请假类型 integer 9062 */ tb_leave_type2
		,/** 备注 varchar2(999) 9063 */ tb_remark
		,/** 请假开始日期 char(10) 9059 */ tb_start_date
		,/** 请假开始时间 char(5) 9057 */ tb_start_time
	}

	

}
