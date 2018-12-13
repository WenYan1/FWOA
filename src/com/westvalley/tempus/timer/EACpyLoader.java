package com.westvalley.tempus.timer;

import java.util.HashMap;
import java.util.Map;

import com.westvalley.tempus.carry.fncl.EACpy;

import crivia.db.common.SQL;
import crivia.db.i.DataSet;
import crivia.eca.a.EcologyCMD_Trans;
import crivia.ecp.common.ECR;
import crivia.ecp.common.EcologyDB;
import crivia.txt.common.Logger;
import crivia.txt.sc.RK;
import weaver.interfaces.schedule.BaseCronJob;

//com.westvalley.tempus.timer.EACpyLoader
public class EACpyLoader extends BaseCronJob {
	
	//系统定时自动执行的方法
	@Override
	public void execute() {
		Logger.log("EACpyLoader Start Up > ");
		load();
		Logger.log("EACpyLoader Finish.");
	}
	//实际的同步动作
	public static void load(){
		//前往ORACLE视图取数
		DataSet data=SQL.select(
"select flex_value c,description n from fnd_flex_values_vl where attribute4='OA'"
				//数据源取系统配置的名为[oracle]的数据源
				, EcologyDB.db("oracle"));
		//建立Map存储编码和ID的对应关系，其中：key=编码，value=ID
		Map<String, String> idMapping=new HashMap<String, String>();
		//初始化code
		while(data.next()){
			String code=data.get("c");
			idMapping.put(code, "");
		}
		{//匹配本地ID开始
			//查询当前本地库中的ID
			DataSet dataIDs=SQL.select(SQL.sql.array("select id k, code c from "+EACpy.table()
					//仅查询oracle中读取到的编码
					+" where code in (?)" , SQL.collection2IDs(idMapping.keySet()).replaceAll(",", "','")
			),EcologyDB.db());//使用本地数据源进行查询
			//设置已有的ID
			while(dataIDs.next()){
				idMapping.put(dataIDs.get("c"),dataIDs.get("k"));
			}
		}//匹配本地ID结束
		//启动事务
		EcologyCMD_Trans cmd=new EcologyCMD_Trans();
		cmd.begin();cmd.isThrow=true;try {//开启事务，开启异常开关
			//遍历oracle读取到的数据
			while(data.next()){
				//取得名称、编码、ID
				String code=data.get("c"),name=data.get("n")
						,id=idMapping.get(code);
				//是否存在ID
				if(ECR.hasCondition(id)){
					//如存在则用现有ID作为条件进行update
					SQL.edit(SQL.sql.array("update "+EACpy.table()
							+" set code=?,name=? where id=?"
							,code,name,id
					),cmd);//使用事务进行操作
				}else{
					//如不存在ID则进行insert
					SQL.edit(SQL.sql.array("insert into "+EACpy.table()
						+"(id,name,code) values (?,?,?)"
						, RK.rk() ,name ,code
					),cmd);//使用事务进行操作
				}
			}
			//尝试提交事务
			cmd.end(true);
			//当事务提交完成后则可以确定操作成功
			Logger.log("EACpyLoader Success.");
		} catch (Exception e){
			//出现异常，关闭异常开关，回滚事务
			cmd.isThrow=false;
			cmd.end(false);
			//打印异常信息
			Logger.logException("EACpyLoader Fail.", e);
		}
		//同步完成
	}

}
