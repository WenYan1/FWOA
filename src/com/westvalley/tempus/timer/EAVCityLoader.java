package com.westvalley.tempus.timer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.westvalley.tempus.carry.fncl.EAVCity;

import crivia.db.common.SQL;
import crivia.db.i.DataSet;
import crivia.eca.a.EcologyCMD_Trans;
import crivia.ecp.common.ECR;
import crivia.ecp.common.EcologyDB;
import crivia.txt.common.Logger;
import crivia.txt.sc.RK;
import weaver.interfaces.schedule.BaseCronJob;

public class EAVCityLoader extends BaseCronJob{

	
	public void execute() {
		Logger.log("EAVCityLoader Start Up >");
		load();
		Logger.log("EAVCityLoader Finish.");
	} 
	public static void load() {
		DataSet data =SQL.select("select city_code,city_name,parent_code district_code,parent_name district_name,province_code,province_name " + 
				"  from cd_city where deleted_flag='0' " + 
				"  order by province_code,parent_code,city_code",EcologyDB.db("mbs"));
		Map<String,String> idMapping = new HashMap<String,String>();
		while(data.next()) {
			String city_code = data.get("city_code");
			idMapping.put(city_code, "");
		}
		
		
		String cd;{//创建一个集合来存储被拼接的条件 BEGIN
			List<String> cds=new ArrayList<String>();
			int k=0;//初始化计数器
			//逗号拼接的ID
			StringBuilder ids=new StringBuilder();
			for(String s:idMapping.keySet()){
				k++;
				if(k>500){//大于500个值时增加一个in
					cds.add(SQL.cd.in("city_code", ids));
					//重置计数器和ID
					k=1;ids=new StringBuilder();
				}
				if(ids.length()>0)ids.append(",");
				ids.append(s);
			}
			//以or关键字将所有的in拼接起来
			cd=SQL.cd.or(cds.toArray(new String[]{}));
		}//创建一个集合来存储被拼接的条件 END
		DataSet dataIds = SQL.select(SQL.sql.array("select id k, city_code c from "+EAVCity.table()
			+" where "+cd//此处直接拼接创建好的条件
		),EcologyDB.db());
		while(dataIds.next()) {
			idMapping.put(dataIds.get("c"),dataIds.get("k"));
		}
		EcologyCMD_Trans cmd = new EcologyCMD_Trans();
		cmd.begin();cmd.isThrow = true;
		try{
		while(data.next()){
			String city_code = data.get("city_code");
			String city_name = data.get("city_name");
			String district_code = data.get("parent_code");
			String district_name = data.get("parent_name");
			String province_code = data.get("province_code");
			String province_name = data.get("province_name");
			String id = idMapping.get(city_code);
			if(ECR.hasCondition(id)) {
				SQL.edit(SQL.sql.array("update "+EAVCity.table()
				+" set city_code=?, city_name=?,district_code=?,district_name=?,province_code=?,province_name=? where id=?"
				,city_code,city_name,district_code,district_name,province_code,province_name,id),cmd);
				
			}else {
				SQL.edit(SQL.sql.array("insert  into "+EAVCity.table()
				+" (id,city_code,city_name,district_code,district_name,province_code,province_name) values (?,?,?,?,?,?,?)",
				RK.rk(),city_code,city_name,district_code,district_name,province_code,province_name),cmd);
			}		
		}
		cmd.end(true);
		Logger.log("EAVCityLoader Success.");
			
		}catch (Exception e) {
			cmd.isThrow=false;
			cmd.end(false);
			Logger.log("EAVCityLoader Fail.",e);
			
		}
		
		
		
	}
	
	
	
	
}
