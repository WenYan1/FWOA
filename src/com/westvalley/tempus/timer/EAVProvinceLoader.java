package com.westvalley.tempus.timer;

import java.util.HashMap;
import java.util.Map;

import com.westvalley.tempus.carry.fncl.EAVProvince;

import crivia.db.common.SQL;
import crivia.db.i.DataSet;
import crivia.eca.a.EcologyCMD_Trans;
import crivia.ecp.common.ECR;
import crivia.ecp.common.EcologyDB;
import crivia.txt.common.Logger;
import crivia.txt.sc.RK;
import weaver.interfaces.schedule.BaseCronJob;

public class EAVProvinceLoader extends BaseCronJob{

	
	public void execute() {
		Logger.log("EAVProvinceLoader Start Up >");
		load();
		Logger.log("EAVProvinceLoader Finish.");
	} 
	public static void load() {
		DataSet data =SQL.select("select distinct province_code,province_name  from cd_city where deleted_flag='0' order by province_code",EcologyDB.db("mbs"));
		Map<String,String> idMapping = new HashMap<String,String>();
		while(data.next()) {
			String province_code = data.get("province_code");
			idMapping.put(province_code, "");
		}
		DataSet dataIds = SQL.select(SQL.sql.array("select id k, province_code c from "+EAVProvince.table()+" where province_code in(?)",SQL.collection2IDs(idMapping.keySet()).replaceAll(",","','")),EcologyDB.db());
		while(dataIds.next()) {
			idMapping.put(dataIds.get("c"),dataIds.get("k"));
		}
		EcologyCMD_Trans cmd = new EcologyCMD_Trans();
		cmd.begin();cmd.isThrow = true;
		try{
		while(data.next()){
			String province_code = data.get("province_code");
			String province_name = data.get("province_name");
			String id = idMapping.get(province_code);
			if(ECR.hasCondition(id)) {
				SQL.edit(SQL.sql.array("update "+EAVProvince.table()
				+" set province_code=?,province_name=? where id=?"
				,province_code,province_name,id),cmd);
				
			}else {
				SQL.edit(SQL.sql.array("insert  into "+EAVProvince.table()
				+" (id,province_code,province_name) values (?,?,?)",
				RK.rk(),province_code,province_name),cmd);
			}		
		}
		cmd.end(true);
		Logger.log("EAVProvinceLoader Success.");
			
		}catch (Exception e) {
			cmd.isThrow=false;
			cmd.end(false);
			Logger.log("EAVProvinceLoader Fail.",e);
			
		}
		
		
		
	}
	
	
	
	
}
