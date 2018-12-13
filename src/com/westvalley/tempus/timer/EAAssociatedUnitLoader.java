package com.westvalley.tempus.timer;

import java.util.HashMap;
import java.util.Map;

import com.westvalley.tempus.carry.fncl.EAAssociatedUnit;
import com.westvalley.tempus.carry.fncl.EACostCenter;


import crivia.db.common.SQL;
import crivia.db.i.DataSet;
import crivia.eca.a.EcologyCMD_Trans;
import crivia.ecp.common.ECR;
import crivia.ecp.common.EcologyDB;
import crivia.txt.common.Logger;
import crivia.txt.sc.RK;
import weaver.interfaces.schedule.BaseCronJob;

public class EAAssociatedUnitLoader extends BaseCronJob{
	
	
	public  void execute() {
		Logger.log("EAAssociatedUnitLoader Start Up >");
		load();
		Logger.log("EAAssociatedUnitLoader Finish >");
	}
	public static void load() {
		DataSet data=SQL.select("      select flex_value,description from  fnd_flex_values_vl a where flex_value_set_id='1014873'  and ENABLED_FLAG='Y' order by flex_value ",EcologyDB.db("oracle"));
		Map<String,String> idMapping = new HashMap<String,String>();
		while(data.next()) {
			String Code = data.get("flex_value");
			idMapping.put(Code, "");
		}
		DataSet dataIds = SQL.select(SQL.sql.array("select id k, code c from "+EAAssociatedUnit.table()+" where code in(?)",SQL.collection2IDs(idMapping.keySet()).replaceAll(",","','")),EcologyDB.db());
		while(dataIds.next()) {
			idMapping.put(dataIds.get("c"),dataIds.get("k"));
		}
		EcologyCMD_Trans cmd = new EcologyCMD_Trans();
		cmd.begin();cmd.isThrow = true;
		try {
			while(data.next()) {
				String code = data.get("flex_value");
				String name = data.get("description");
				String id = idMapping.get(code);
				if(ECR.hasCondition(id)){
					SQL.edit(SQL.sql.array("update "+EAAssociatedUnit.table()
					+" set code=?,name=? where id=?"
					,code,name,id),cmd);
				}else {
					SQL.edit(SQL.sql.array("insert  into "+EAAssociatedUnit.table()
					+" (id,code,name) values (?,?,?)",
					RK.rk(),code,name),cmd);
				}			
			}
			cmd.end(true);
			Logger.log("EAAssociatedUnitLoader Success.");
		}catch (Exception e) {
			cmd.isThrow=false;
			cmd.end(false);
			Logger.log("EAAssociatedUnitLoader Fail.",e);
		}
	}
}
