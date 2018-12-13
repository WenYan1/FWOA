package com.westvalley.tempus.timer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.westvalley.tempus.carry.fncl.EACostCategory;
import com.westvalley.tempus.carry.fncl.EAPaybankAccount;
import com.westvalley.tempus.carry.fncl.EAProjectSegment;
import com.westvalley.tempus.carry.fncl.EAVCity;
import com.westvalley.tempus.carry.fncl.EAVReceivingUnit;
import com.westvalley.tempus.carry.fncl.PR;

import crivia.db.common.SQL;
import crivia.db.i.DataSet;
import crivia.eca.a.EcologyCMD_Trans;
import crivia.ecp.common.ECR;
import crivia.ecp.common.EcologyDB;
import crivia.txt.common.Logger;
import crivia.txt.sc.RK;
import weaver.interfaces.schedule.BaseCronJob;

public class EAReceivingUnitLoader extends BaseCronJob{

	
	public void execute() {
		Logger.log("EAReceivingUnitLoader Start Up >");
		load();
		Logger.log("EAReceivingUnitLoader Finish.");
	} 
	public static void load() {
		DataSet data =SQL.select("select vendor_id , vendor_name ,segment1   from Ap_Suppliers where (vendor_type_lookup_code <> 'EMPLOYEE' or  vendor_type_lookup_code is null) and (sysdate between start_date_active and end_date_active or end_date_active is null)" 
				,EcologyDB.db("oracle"));
		Map<String,String> idMapping = new HashMap<String,String>();
		while(data.next()) {
			String code = data.get("vendor_id");
			idMapping.put(code, "");
		}
		
		String cd;{//创建一个集合来存储被拼接的条件 BEGIN
			List<String> cds=new ArrayList<String>();
			int k=0;//初始化计数器
			//逗号拼接的ID
			StringBuilder ids=new StringBuilder();
			for(String s:idMapping.keySet()){
				k++;
				if(k>500){//大于500个值时增加一个in
					cds.add(SQL.cd.in("code", ids));
					//重置计数器和ID
					k=1;ids=new StringBuilder();
				}
				if(ids.length()>0)ids.append(",");
				ids.append(s);
			}
			if(ids.length()>0) {
				cds.add(SQL.cd.in("code", ids));
			}
			//以or关键字将所有的in拼接起来
			cd=SQL.cd.or(cds.toArray(new String[]{}));
		}//创建一个集合来存储被拼接的条件 END
		DataSet dataIds = SQL.select(SQL.sql.array("select id k, code c from "+EAVReceivingUnit.table()
			+" where "+cd//此处直接拼接创建好的条件
		),EcologyDB.db());
		
		while(dataIds.next()) {
			idMapping.put(dataIds.get("c"),dataIds.get("k"));
		}
		EcologyCMD_Trans cmd = new EcologyCMD_Trans();
		cmd.begin();cmd.isThrow = true;
		try{
			//SQL.edit(SQL.sql.array("delete  from "+EAVReceivingUnit.table()),cmd);
		while(data.next()){
			String code = data.get("vendor_id");
			String name = data.get("vendor_name").replaceAll("'", "''");;
			String id = idMapping.get(code);
			String segment1 = data.get("segment1");
			if(ECR.hasCondition(id)) {
				SQL.edit(SQL.sql.array("update "+EAVReceivingUnit.table()
				+" set code=?,name=?,segment1=? where id=?"
				,code,name,segment1,id),cmd);
				
			}else {
				SQL.edit(SQL.sql.array("insert  into "+EAVReceivingUnit.table()
				+" (id,code,name,segment1) values (?,?,?,?)",
				RK.rk(),code,name,segment1),cmd);
			}		
		}
		cmd.end(true);
		Logger.log("EAReceivingUnitLoader Success.");
			
		}catch (Exception e) {
			cmd.isThrow=false;
			cmd.end(false);
			Logger.log("EAReceivingUnitLoader Fail.",e);
			
		}
	
	}
	
	
	
	
}
