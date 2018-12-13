package com.westvalley.tempus.timer;

import java.util.HashMap;
import java.util.Map;

import com.westvalley.tempus.carry.fncl.EACostCenter;


import crivia.db.common.SQL;
import crivia.db.i.DataSet;
import crivia.eca.a.EcologyCMD_Trans;
import crivia.ecp.common.ECR;
import crivia.ecp.common.EcologyDB;
import crivia.txt.common.Logger;
import crivia.txt.sc.RK;
import weaver.interfaces.schedule.BaseCronJob;

public class EACostCenterLoader extends BaseCronJob{
	
	
	public  void execute() {
		Logger.log("EAEACostCenterLoader Start Up >");
		load();
		Logger.log("EAEACostCenterLoader Finish >");
	}
	public static void load() {
		DataSet data=SQL.select("select flex_value ,description , substr(flex_value,0,5)  x  from fnd_flex_values_vl where 1=1 " + 
				" and (length(flex_value) = 9" + 
				" and substr(flex_value,0,5) in (" + 
				" select flex_value  from fnd_flex_values_vl where attribute4='OA') or flex_value ='0')" + 
				" AND flex_value_set_id='1014868' and ENABLED_FLAG='Y' order by flex_value",EcologyDB.db("oracle"));
		Map<String,String> idMapping = new HashMap<String,String>();
		while(data.next()) {
			String Code = data.get("flex_value");
			idMapping.put(Code, "");
		}
		DataSet dataIds = SQL.select(SQL.sql.array("select id k, code c from "+EACostCenter.table()+" where code in(?)",SQL.collection2IDs(idMapping.keySet()).replaceAll(",","','")),EcologyDB.db());
		while(dataIds.next()) {
			idMapping.put(dataIds.get("c"),dataIds.get("k"));
		}
		EcologyCMD_Trans cmd = new EcologyCMD_Trans();
		cmd.begin();cmd.isThrow = true;
		try {
			while(data.next()) {
				String code = data.get("flex_value");
				String description = data.get("description");
				String cpCode = data.get("x");
				String id = idMapping.get(code);
				if(ECR.hasCondition(id)){
					SQL.edit(SQL.sql.array("update "+EACostCenter.table()
					+" set code=?,description=?,cpCode=? where id=?"
					,code,description,cpCode,id),cmd);
				}else {
					SQL.edit(SQL.sql.array("insert  into "+EACostCenter.table()
					+" (id,code,description,cpCode) values (?,?,?,?)",
					RK.rk(),code,description,cpCode),cmd);
				}			
			}
			cmd.end(true);
			Logger.log("EACostCenterLoader Success.");
		}catch (Exception e) {
			cmd.isThrow=false;
			cmd.end(false);
			Logger.log("EACostCenterLoader Fail.",e);
		}
	}
}
