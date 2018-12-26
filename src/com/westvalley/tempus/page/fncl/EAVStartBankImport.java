package com.westvalley.tempus.page.fncl;

import java.util.List;

import com.westvalley.tempus.carry.fncl.EAVStartBank;

import crivia.db.common.SQL;
import crivia.eca.a.EcologyCMD_Trans;
import crivia.ecp.p.AutoExampleImport;
import crivia.txt.sc.RK;
//导入页面
public class EAVStartBankImport extends AutoExampleImport {

	@Override
	protected void titles(List<Title> titles) {
		titles.add(new Title("列1", "这是列1", ""));
		titles.add(new Title("列2", "这是列2", ""));
		titles.add(new Title("列3", "这是列3", ""));
		titles.add(new Title("列4", "这是列4", ""));
	}

	@Override
	protected String report(List<String[]> datas) {
		EcologyCMD_Trans cmd = new EcologyCMD_Trans();
		cmd.begin();cmd.isThrow = true;
		try{
		for (String[] data:datas) {
			String bank_id=data[0];
			String house_bank_code=data[1];
			String house_bank_name=data[2];
			String city_code =data[3];
			
			//SQL 操作
			SQL.edit(SQL.sql.array("insert  into "+EAVStartBank.table()
			+" (id,bank_id,house_bank_code,house_bank_name,city_code) values (?,?,?,?,?)",
			RK.rk(),bank_id,house_bank_code,house_bank_name,city_code),cmd);
		}
		
		}catch (Exception e) {
			cmd.isThrow=false;
			cmd.end(false);
		}
		return "导入成功";
	}

}
