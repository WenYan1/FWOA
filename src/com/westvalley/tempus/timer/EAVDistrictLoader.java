package com.westvalley.tempus.timer;

import java.util.HashMap;
import java.util.Map;

import com.westvalley.tempus.carry.fncl.EAVDistrict;

import crivia.db.common.SQL;
import crivia.db.i.DataSet;
import crivia.eca.a.EcologyCMD_Trans;
import crivia.ecp.common.ECR;
import crivia.ecp.common.EcologyDB;
import crivia.txt.common.Logger;
import crivia.txt.sc.RK;
import weaver.interfaces.schedule.BaseCronJob;

public class EAVDistrictLoader extends BaseCronJob{

	
	public void execute() {
		Logger.log("EAVDistrictLoader Start Up >");
		load();
		Logger.log("EAVDistrictLoader Finish.");
	} 
	public static void load() {
		DataSet data =SQL.select("select distinct parent_code as district_code,parent_name as district_name,province_code,province_name " + 
				"  from cd_city where deleted_flag='0' order by province_code,parent_code",EcologyDB.db("mbs"));
		Map<String,String> idMapping = new HashMap<String,String>();
		while(data.next()) {
			String district_code = data.get("parent_code");
			idMapping.put(district_code, "");
		}
		DataSet dataIds = SQL.select(SQL.sql.array("select id k, district_code c from "+EAVDistrict.table()+" where district_code in(?)",SQL.collection2IDs(idMapping.keySet()).replaceAll(",","','")),EcologyDB.db());
		while(dataIds.next()) {
			idMapping.put(dataIds.get("c"),dataIds.get("k"));
		}
		EcologyCMD_Trans cmd = new EcologyCMD_Trans();
		cmd.begin();cmd.isThrow = true;
		try{
		while(data.next()){
			String district_code = data.get("parent_code");
			String district_name = data.get("parent_name");
			String province_code = data.get("province_code");
			String province_name = data.get("province_name");
			String id = idMapping.get(district_code);
			if(ECR.hasCondition(id)) {
				SQL.edit(SQL.sql.array("update "+EAVDistrict.table()
				+" set district_code=?,district_name=?,province_code=?,province_name=? where id=?"
				,district_code,district_name,province_code,province_name,id),cmd);
				
			}else {
				SQL.edit(SQL.sql.array("insert  into "+EAVDistrict.table()
				+" (id,district_code,district_name,province_code,province_name) values (?,?,?,?,?)",
				RK.rk(),district_code,district_name,province_code,province_name),cmd);
			}		
		}
		cmd.end(true);
		Logger.log("EAVDistrictLoader Success.");
			
		}catch (Exception e) {
			cmd.isThrow=false;
			cmd.end(false);
			Logger.log("EAVDistrictLoader Fail.",e);
			
		}
		
		
		
	}
	
	
	
	
}
