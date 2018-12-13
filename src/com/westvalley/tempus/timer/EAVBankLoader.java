package com.westvalley.tempus.timer;

import java.util.HashMap;
import java.util.Map;

import com.westvalley.tempus.carry.fncl.EACostCategory;
import com.westvalley.tempus.carry.fncl.EAVBank;

import crivia.db.common.SQL;
import crivia.db.i.DataSet;
import crivia.eca.a.EcologyCMD_Trans;
import crivia.ecp.common.ECR;
import crivia.ecp.common.EcologyDB;
import crivia.txt.common.Logger;
import crivia.txt.sc.RK;
import weaver.interfaces.schedule.BaseCronJob;

public class EAVBankLoader extends BaseCronJob{

	
	public void execute() {
		Logger.log("EAVBankLoader Start Up >");
		load();
		Logger.log("EAVBankLoader Finish.");
	} 
	public static void load() {
		DataSet data =SQL.select("select row_id,bank_code,bank_name  from md_bank where deleted_flag='0' order by row_id desc",EcologyDB.db("mbs"));
		Map<String,String> idMapping = new HashMap<String,String>();
		while(data.next()) {
			String bank_code = data.get("bank_code");
			idMapping.put(bank_code, "");
		}
		DataSet dataIds = SQL.select(SQL.sql.array("select id k, bank_code c from "+EAVBank.table()+" where bank_code in(?)",SQL.collection2IDs(idMapping.keySet()).replaceAll(",","','")),EcologyDB.db());
		while(dataIds.next()) {
			idMapping.put(dataIds.get("c"),dataIds.get("k"));
		}
		EcologyCMD_Trans cmd = new EcologyCMD_Trans();
		cmd.begin();cmd.isThrow = true;
		try{
		while(data.next()){
			String bank_code = data.get("bank_code");
			String bank_name = data.get("bank_name");
			String id = idMapping.get(bank_code);
			if(ECR.hasCondition(id)) {
				SQL.edit(SQL.sql.array("update "+EAVBank.table()
				+" set bank_code=?,bank_name=? where id=?"
				,bank_code,bank_name,id),cmd);
				
			}else {
				SQL.edit(SQL.sql.array("insert  into "+EAVBank.table()
				+" (id,bank_code,bank_name) values (?,?,?)",
				RK.rk(),bank_code,bank_name),cmd);
			}		
		}
		cmd.end(true);
		Logger.log("EAVBankLoader Success.");
			
		}catch (Exception e) {
			cmd.isThrow=false;
			cmd.end(false);
			Logger.log("EAVBankLoader Fail.",e);
			
		}
		
		
		
	}
	
	
	
	
}
