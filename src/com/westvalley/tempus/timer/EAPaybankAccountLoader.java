package com.westvalley.tempus.timer;

import java.util.HashMap;
import java.util.Map;

import com.westvalley.tempus.carry.fncl.EACostCategory;
import com.westvalley.tempus.carry.fncl.EAPaybankAccount;
import com.westvalley.tempus.carry.fncl.EAProjectSegment;

import crivia.db.common.SQL;
import crivia.db.i.DataSet;
import crivia.eca.a.EcologyCMD_Trans;
import crivia.ecp.common.ECR;
import crivia.ecp.common.EcologyDB;
import crivia.txt.common.Logger;
import crivia.txt.sc.RK;
import weaver.interfaces.schedule.BaseCronJob;

public class EAPaybankAccountLoader extends BaseCronJob{

	
	public void execute() {
		Logger.log("EAPaybankAccountLoader Start Up >");
		load();
		Logger.log("EAPaybankAccountLoader Finish.");
	} 
	public static void load() {
		DataSet data =SQL.select("select b.flex_value cpcode,e.bank_account_id paybankid, e.bank_account_name paybankacount,bank_account_num  paybanknum " + 
				"  from " + 
				"   fnd_flex_values_vl b " + 
				"  inner join HR_ALL_ORGANIZATION_UNITS c " + 
				"    on c.name = b.description " + 
				"  inner join CE_BANK_ACCT_USES_ALL d " + 
				"    on d.org_id = c.organization_id " + 
				"  inner join CE.CE_BANK_ACCOUNTS e " + 
				"    on e.bank_account_id = d.bank_account_id " + 
				"  where flex_value_set_id = 1014867 " + 
				"   and b.attribute4 = 'OA' and (d.end_date>sysdate or d.end_date is null)",EcologyDB.db("oracle"));
		Map<String,String> idMapping = new HashMap<String,String>();
		while(data.next()) {
			String paybankid = data.get("paybankid");
			idMapping.put(paybankid, "");
		}
		DataSet dataIds = SQL.select(SQL.sql.array("select id k, paybankid c from "+EAPaybankAccount.table()+" where paybankid in(?)",SQL.collection2IDs(idMapping.keySet()).replaceAll(",","','")),EcologyDB.db());
		while(dataIds.next()) {
			idMapping.put(dataIds.get("c"),dataIds.get("k"));
		}
		EcologyCMD_Trans cmd = new EcologyCMD_Trans();
		cmd.begin();cmd.isThrow = true;
		try{
		while(data.next()){
			String cpcode = data.get("cpcode");
			String paybankid = data.get("paybankid");
			String paybankacount = data.get("paybankacount");
			String paybanknum = data.get("paybanknum");
			String id = idMapping.get(paybankid);
			if(ECR.hasCondition(id)) {
				SQL.edit(SQL.sql.array("update "+EAPaybankAccount.table()
				+" set cpcode=?,paybankid=?, paybankacount=?,paybanknum=? where id=?"
				,cpcode,paybankid,paybankacount,paybanknum,id),cmd);
				
			}else {
				SQL.edit(SQL.sql.array("insert  into "+EAPaybankAccount.table()
				+" (id,cpcode,paybankid,paybankacount,paybanknum) values (?,?,?,?,?)",
				RK.rk(),cpcode,paybankid,paybankacount,paybanknum),cmd);
			}		
		}
		cmd.end(true);
		Logger.log("EAPaybankAccountLoader Success.");
			
		}catch (Exception e) {
			cmd.isThrow=false;
			cmd.end(false);
			Logger.log("EAPaybankAccountLoader Fail.",e);
			
		}
	
	}
	
	
	
	
}
