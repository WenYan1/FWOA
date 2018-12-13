package com.westvalley.tempus.diyInterface;

import java.util.Map;


import crivia.db.common.SQL;
import crivia.db.i.DataSet;
import crivia.eca.Cat;
import crivia.txt.common.Logger;
//com.westvalley.tempus.diyInterface.FileType
public class FileType extends Cat {
	
	
	public static void main(String[] args) {
		

	}

	@Override
	protected String ecaAct() throws Exception {
		Map<String,String> vt4type=selectValueText(getRequestTableName(1),t1.tb_file_type1);//档案类型
		Map<String,String> tv4tb_file_type=selectTextValue(getRequestTableName(), t0.tb_file_type);
		Logger.log("vt4type > "+vt4type);
		Logger.log("tv4is_annual_leave > "+tv4tb_file_type);
		DataSet d1=subData(1);
		
		String  type="";
		while(d1.next()) {
			String nameType=d1.get(t1.tb_file_type1);
			if("财务类".equals(vt4type.get(nameType))) {
				
				type ="0";//财务类
			}else {
				type="1";//非财务类
			}
		}
		//TODO
		
			SQL.edit(SQL.sql.array("update "+getRequestTableName()
				+" set "+t0.tb_file_type+" = ?"
				+" where requestID = ?"
				,type
				,requestID
			),defaultCMD);
		
		return null;
	}


	
	/* Fields (Main Table) */
	public enum t0 { // XXX
		  id                      ,
		  requestid             ,
		  tb_process_number      ,
		  tb_applicant            ,
		  tb_department          ,
		  tb_date_of_application ,
		  tb_affiliation         ,
		  tb_job_number           ,
		  tb_post                ,
		  tb_related_processes   ,
		  tb_related_documents   ,
		  tb_relevant_attachments ,
		  tb_reson                ,
		  tb_promiser            ,
		  tb_file_type       
	}
	/** Fields (Sub Table 1) */
	public enum t1 { // XXX
		
		  mainid            ,
		  tb_file_type      ,
		  tb_company        ,
		  tb_characteristic ,
		  tb_method         ,
		  tb_number        ,
		  tb_return_date    ,
		  tb_is_return      ,
		  tb_remank         ,
		  tb_file_type1
	}

	

}
