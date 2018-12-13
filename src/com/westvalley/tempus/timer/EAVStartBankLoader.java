package com.westvalley.tempus.timer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.westvalley.tempus.carry.fncl.EACostCategory;
import com.westvalley.tempus.carry.fncl.EAProjectSegment;
import com.westvalley.tempus.carry.fncl.EAVCity;
import com.westvalley.tempus.carry.fncl.EAVReceivingUnit;
import com.westvalley.tempus.carry.fncl.EAVStartBank;

import crivia.db.common.SQL;
import crivia.db.i.DataSet;
import crivia.eca.a.EcologyCMD_Trans;
import crivia.ecp.common.ECR;
import crivia.ecp.common.EcologyDB;
import crivia.txt.common.Logger;
import crivia.txt.sc.RK;
import weaver.interfaces.schedule.BaseCronJob;

public class EAVStartBankLoader extends BaseCronJob{

	
	public void execute() {
		Logger.log("EAStartBankLoader Start Up >");
		load();
		Logger.log("EAStartBankLoader Finish.");
	} 
	public static void load() {
		DataSet data =SQL.select("select bank_id,house_bank_code,house_bank_name,city_code from v_house_bank",EcologyDB.db("mbs"));
		Map<String,String> idMapping = new HashMap<String,String>();
		while(data.next()) {
			String house_bank_code = data.get("house_bank_code");
			idMapping.put(house_bank_code, "");
		}
		 
		String cd;{//创建一个集合来存储被拼接的条件 BEGIN
			List<String> cds=new ArrayList<String>();
			int k=0;//初始化计数器
			//逗号拼接的ID
			StringBuilder ids=new StringBuilder();
			for(String s:idMapping.keySet()){
				k++;
				if(k>500){//大于500个值时增加一个in
					cds.add(SQL.cd.in("house_bank_code", ids));
					//重置计数器和ID
					k=1;ids=new StringBuilder();
				}
				if(ids.length()>0)ids.append(",");
				ids.append(s);
			}
			if(ids.length()>0) {
				cds.add(SQL.cd.in("house_bank_code", ids));
			}
			//以or关键字将所有的in拼接起来
			cd=SQL.cd.or(cds.toArray(new String[]{}));
		}//创建一个集合来存储被拼接的条件 END
		DataSet dataIds = SQL.select(SQL.sql.array("select id k, house_bank_code c from "+EAVStartBank.table()
			+" where "+cd//此处直接拼接创建好的条件
		),EcologyDB.db());
		
		
		while(dataIds.next()) {
			idMapping.put(dataIds.get("c"),dataIds.get("k"));
		}
		EcologyCMD_Trans cmd = new EcologyCMD_Trans();
		cmd.begin();cmd.isThrow = true;
		try{
			//SQL.edit(SQL.sql.array("delete  from "+EAProjectSegment.table()),cmd);
		while(data.next()){
			String bank_id = data.get("bank_id");
			String house_bank_code = data.get("house_bank_code");
			String house_bank_name = data.get("house_bank_name").replaceAll("'", "''");
			String city_code = data.get("city_code");
			String id = idMapping.get(house_bank_code);
			if(ECR.hasCondition(id)) {
				SQL.edit(SQL.sql.array("update "+EAVStartBank.table()
				+" set bank_id=?, house_bank_code=?,house_bank_name=?, city_code=? where id=?"
				,bank_id,house_bank_code,house_bank_name,city_code,id),cmd);
				
			}else {
				SQL.edit(SQL.sql.array("insert  into "+EAVStartBank.table()
				+" (id,bank_id,house_bank_code,house_bank_name,city_code) values (?,?,?,?,?)",
				RK.rk(),bank_id,house_bank_code,house_bank_name,city_code),cmd);
			}		
		}
		cmd.end(true);
		Logger.log("EAVStartBank Success.");
			
		}catch (Exception e) {
			cmd.isThrow=false;
			cmd.end(false);
			Logger.log("EAVStartBank Fail.",e);
			
		}
		
		
		
	}
	
	
	
	
}
