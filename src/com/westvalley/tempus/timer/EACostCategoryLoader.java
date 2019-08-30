package com.westvalley.tempus.timer;

import java.util.HashMap;
import java.util.Map;

import com.westvalley.tempus.carry.fncl.EACostCategory;

import crivia.db.common.SQL;
import crivia.db.i.DataSet;
import crivia.eca.a.EcologyCMD_Trans;
import crivia.ecp.common.ECR;
import crivia.ecp.common.EcologyDB;
import crivia.txt.common.Logger;
import crivia.txt.sc.RK;
import weaver.interfaces.schedule.BaseCronJob;

public class EACostCategoryLoader extends BaseCronJob{

	
	public void execute() {
		Logger.log("EACostCategoryLoader Start Up >");
		load();
		Logger.log("EACostCategoryLoader Finish.");
	} 
	public static void load() {
		DataSet data =SQL.select("select flex_value costvalue,description costname from fnd_flex_values_vl where "
				+ "flex_value_set_id='1014869' and (flex_value like '6601______' or flex_value like '6602______' or flex_value='2221100301'  or"
				+ " flex_value='1221020101'  or flex_value like '7001______' or flex_value like '6603______' or "
				+ " flex_value='2241010101' or  flex_value='6405010101' or flex_value='1221010101' or flex_value='6401030101' or flex_value='2202020101' or flex_value='6711990101' )",EcologyDB.db("oracle"));
		Map<String,String> idMapping = new HashMap<String,String>();
		while(data.next()) {
			String costvalue = data.get("costvalue");
			idMapping.put(costvalue, "");
		}
		DataSet dataIds = SQL.select(SQL.sql.array("select id k, costvalue c from "+EACostCategory.table()+" where costvalue in(?)",SQL.collection2IDs(idMapping.keySet()).replaceAll(",","','")),EcologyDB.db());
		while(dataIds.next()) {
			idMapping.put(dataIds.get("c"),dataIds.get("k"));
		}
		EcologyCMD_Trans cmd = new EcologyCMD_Trans();
		cmd.begin();cmd.isThrow = true;
		try{
		while(data.next()){
			String costvalue = data.get("costvalue");
			String costname = data.get("costname");
			String id = idMapping.get(costvalue);
			if(ECR.hasCondition(id)) {
				SQL.edit(SQL.sql.array("update "+EACostCategory.table()
				+" set costvalue=?,costname=? where id=?"
				,costvalue,costname,id),cmd);
				
			}else {
				SQL.edit(SQL.sql.array("insert  into "+EACostCategory.table()
				+" (id,costvalue,costname) values (?,?,?)",
				RK.rk(),costvalue,costname),cmd);
			}		
		}
		cmd.end(true);
		Logger.log("EACostCategoryLoader Success.");
			
		}catch (Exception e) {
			cmd.isThrow=false;
			cmd.end(false);
			Logger.log("EACostCategoryLoader Fail.",e);
			
		}
		
		
		
	}
	
	
	
	
}
