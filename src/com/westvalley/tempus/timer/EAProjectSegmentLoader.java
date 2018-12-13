package com.westvalley.tempus.timer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.westvalley.tempus.carry.fncl.EACostCategory;
import com.westvalley.tempus.carry.fncl.EAProjectSegment;
import com.westvalley.tempus.carry.fncl.EAVCity;
import com.westvalley.tempus.carry.fncl.EAVReceivingUnit;

import crivia.db.common.SQL;
import crivia.db.i.DataSet;
import crivia.eca.a.EcologyCMD_Trans;
import crivia.ecp.common.ECR;
import crivia.ecp.common.EcologyDB;
import crivia.txt.common.Logger;
import crivia.txt.sc.RK;
import weaver.interfaces.schedule.BaseCronJob;

public class EAProjectSegmentLoader extends BaseCronJob{

	
	public void execute() {
		Logger.log("EAProjectSegmentLoader Start Up >");
		load();
		Logger.log("EAProjectSegmentLoader Finish.");
	} 
	public static void load() {
		DataSet data =SQL.select("select flex_value pscode, description psname, substr(flex_value,0,5)  cpcode" + 
				"  from fnd_flex_values_vl " + 
				" where flex_value_set_id = '1014872' " + 
				"   and (length(flex_value) = 9 or flex_value like 'AAAAA%' or " + 
				"       flex_value like 'BBBBB%' or flex_value like 'CCCCC%' or " + 
				"       flex_value like 'DDDDD%' or flex_value like 'EEEEE%') " + 
				" order by flex_value",EcologyDB.db("oracle"));
		Map<String,String> idMapping = new HashMap<String,String>();
		while(data.next()) {
			String pscode = data.get("pscode");
			idMapping.put(pscode, "");
		}
		
		String cd;{//创建一个集合来存储被拼接的条件 BEGIN
			List<String> cds=new ArrayList<String>();
			int k=0;//初始化计数器
			//逗号拼接的ID
			StringBuilder ids=new StringBuilder();
			for(String s:idMapping.keySet()){
				k++;
				if(k>500){//大于500个值时增加一个in
					cds.add(SQL.cd.in("pscode", ids));
					//重置计数器和ID
					k=1;ids=new StringBuilder();
				}
				if(ids.length()>0)ids.append(",");
				ids.append(s);
			}
			if(ids.length()>0) {
				cds.add(SQL.cd.in("pscode", ids));
			}
			//以or关键字将所有的in拼接起来
			cd=SQL.cd.or(cds.toArray(new String[]{}));
		}//创建一个集合来存储被拼接的条件 END
		DataSet dataIds = SQL.select(SQL.sql.array("select id k, pscode c from "+EAProjectSegment.table()
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
			String pscode = data.get("pscode");
			String psname = data.get("psname").replaceAll("'", "''");
			String cpcode = data.get("cpcode");
			String id = idMapping.get(pscode);
			if(ECR.hasCondition(id)) {
				SQL.edit(SQL.sql.array("update "+EAProjectSegment.table()
				+" set pscode=?,psname=?, cpcode=? where id=?"
				,pscode,psname,cpcode,id),cmd);
				
			}else {
				SQL.edit(SQL.sql.array("insert  into "+EAProjectSegment.table()
				+" (id,pscode,psname,cpcode) values (?,?,?,?)",
				RK.rk(),pscode,psname,cpcode),cmd);
			}		
		}
		cmd.end(true);
		Logger.log("EAProjectSegment Success.");
			
		}catch (Exception e) {
			cmd.isThrow=false;
			cmd.end(false);
			Logger.log("EAProjectSegment Fail.",e);
			
		}
		
		
		
	}
	
	
	
	
}
