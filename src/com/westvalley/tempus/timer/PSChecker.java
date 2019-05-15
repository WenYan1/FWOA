package com.westvalley.tempus.timer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.westvalley.tempus.carry.fncl.PR;
import com.westvalley.tempus.carry.fncl.PRB;
import com.westvalley.tempus.ps.PSM;

import crivia.db.common.SQL;
import crivia.ecp.common.EcologyDB;
import crivia.txt.common.Logger;
import weaver.interfaces.schedule.BaseCronJob;

public class PSChecker extends BaseCronJob {

	@Override
	public void execute() {
		Logger.log("PS CHECKER EXECUTE BEGIN >");
		try{
			List<PR> prs=SQL.list(EcologyDB.db(), PR.class
					, SQL.cd.equals(PR.rSQL(PR.Keys.payLine, "t"), PR.PayLine.OnLine)
					, SQL.cd.equals(PR.Keys.payStatus, PR.PayStatus.Payed)
			);
			Logger.log("查询已支付的线上付款数据SQL > "+SQL.LastSQL);
			Set<String> prbks=new HashSet<String>();
			for(PR r:prs)prbks.add(r.getPayKey());
			Map<String,String> prr=new HashMap<String, String>();
			for(String b:prbks){
				try {
					Map<String,String> m=PSM.f(b);
					Logger.log("查询返回的值为> "+m);
					for(String r:m.keySet())prr.put(r, m.get(r));
				} catch (Exception e) {
					Logger.logException("查询付款状态异常",e);
				}
			}
			for(String r:prr.keySet()){
				PR.PayStatus s=
						"5".equals(prr.get(r))?PR.PayStatus.Success
								:"6".equals(prr.get(r))?PR.PayStatus.Fail
										:PR.PayStatus.Payed
										;
				Logger.log("开始更新状态"+PR.Keys.payKey+"111111111"+r);
				PR prbId=SQL.one(EcologyDB.db(), PR.class, true
						,SQL.cd.equals(PR.Keys.requestID,r));
				Logger.log("测试pr"+prbId);
				Logger.log("prb数值"+PR.Keys.prb+"fengexian"+prbId.getPrb().getId());
				SQL.edit(SQL.sql.array("update "+PR.table()
						+" set "+PR.Keys.payStatus+"=?"
						+" where "+PR.Keys.prb+"=?"
						, s , prbId.getPrb().getId()
						),EcologyDB.db()); 
			}
		}catch(Throwable e){
			Logger.logException("PS CHECKER EXECUTE EXCEPTION",e);
		}
		Logger.log("PS CHECKER EXECUTE END.");
	}
	
}
