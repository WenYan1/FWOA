package com.westvalley.tempus.ps;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONObject;

import com.westvalley.tempus.carry.fncl.EAPaybankAccount;
import com.westvalley.tempus.carry.fncl.PR;
import com.westvalley.tempus.carry.fncl.PRB;

import crivia.db.common.SQL;
import crivia.db.i.CMD;
import crivia.ecp.carry.ec.hrm.HDept;
import crivia.ecp.carry.ec.hrm.HR;
import crivia.ecp.common.EcologyDB;
import crivia.http.Poster;
import crivia.json.common.JSONSwitcher;
import crivia.time.carry.Day;
import crivia.time.carry.Time;
import crivia.txt.common.Logger;
import crivia.txt.common.MoneyFormat;

public class PSM {
	//public static final String URL="http://172.18.240.71:9080/mbs/ERPServlet";//正式环境
	public static final String URL="http://www7.asuscomm.com:8528/mbs/ERPServlet";//    测试环境
	
	
	public static void main(String[] args) throws ClientProtocolException, IOException {
		JO302 jo=_.new JO302();
		jo.BATCH_ID="4248";
//		jo.SERIAL="4248";
		String j=JSONSwitcher.toJSON(jo);
		//Logger.log("调用资金系统接口[地址："+URL+"]/[数据]："+Logger.hiddenBox(j));
		String x=Poster.jx(URL, j);
//		Logger.log("返回结果："+x);
		System.out.println(x);
		
	}
	public static void main2(String[] args) throws ClientProtocolException, IOException {
		
//		String s=crivia.txt.common.Text.get(PSM.clasds,"A");
//		for(String ss:s.replaceAll("\r", "").split("\n")){
//			String[] sss=ss.split("\t");
//			System.out.println("x."+sss[0]+"=\"TEST\";//"+sss[1]);
//		}
		
		JO301 jo=_.new JO301();
		jo.BATCH_ID="TEST"+Day.today().toString().replaceAll("-", "")
			+Time.now().toString().replaceAll(":", "");
		JO301.LIST x=jo.new LIST();
		jo.LIST.add(x);
		x.COMPANY_CODE="TEST";
		x.ACCOUNT_CODE="TEST";//付款单位账号
		x.REC_ACCOUNT_CODE="TEST";//收款账号 
		x.REC_ACCOUNT_NAME="TEST";//收款户名
		x.REC_BANK_CODE="TEST";//收款方联行号
		x.REC_BANK_NAME="TEST";//开户行全称
		x.CURR_CODE="RMB";//币种代码
		x.TRADE_AMOUNT="0.01";//交易金额
		x.PURPOSE="TEST";//事由
		x.LOCAL_FLAG="0";//同城异地
		x.TRADE_SPEED="0";//加急标志
		x.PUBLIC_FLAG="0";//对公对私
		x.SERIAL=jo.BATCH_ID;//流水号
		x.REMARK="TEST";//备注
		x.RESERVED1="";//预留字段1
		x.RESERVED2="";//预留字段2
		x.RESERVED3="";//预留字段3
		x.RESERVED4="";//预留字段4
		x.RESERVED5="";//预留字段5
		x.RESERVED6="";//预留字段6
		
		String json=JSONSwitcher.toJSON(jo);
		System.out.println("数据 >  ");
		System.out.println(json);
		
		String url=URL;
		System.out.println("接口地址 > ");
		System.out.println(url);
		String s=Poster.jx(url, "gbk", json);
		System.out.println("调用结果 > ");
		System.out.println(s);
	}
	
	public static String x(PRB r,String mainRequestID) throws Exception{
		String j;{JO301 jo=_.new JO301();
		jo.BATCH_ID=mainRequestID;
			//for(PRB r:prs){
				JO301.LIST t=jo.new LIST();
				jo.LIST.add(t);
				t.COMPANY_CODE="";
				t.ACCOUNT_CODE=SQL.relation(r.getPrbPayMentAccount()
					, EAPaybankAccount.table()
					, EAPaybankAccount.Keys.paybanknum
					, EAPaybankAccount.Keys.paybankid
					, EcologyDB.db());//付款单位账号
				t.REC_ACCOUNT_CODE=r.getPrbrecBankAccountNumber();//收款账号 
				t.REC_ACCOUNT_NAME=r.getPrbrecBankAccountName();//收款户名
	//			t.REC_BANK_CODE="";//收款方联行号
				t.REC_BANK_NAME=r.getPrbrecBankName();//开户行全称
				t.CURR_CODE="RMB";//币种代码
				t.TRADE_AMOUNT=MoneyFormat.toMoneyFormat(r.getMoneys()).replaceAll(",", "");//交易金额
				t.PURPOSE=r.getApplication();//事由
				t.LOCAL_FLAG="0";//同城异地
				t.TRADE_SPEED="0";//加急标志
				t.PUBLIC_FLAG="0";//对公对私
				t.SERIAL=r.getPayKey();//流水号
				t.APPLICANT=SQL.relation(r.getCreator(), HR.table(),HR.Keys.lastName,HR.Keys.workCode,EcologyDB.db());//r.getCreatorName();//申请人姓名
				t.DEPARTMENT=SQL.relation(SQL.relation(r.getCreator()
						, HR.table()
						, HR.Keys.departmentID
						, HR.Keys.workCode
						, EcologyDB.db())
					, HDept.table()
					, HDept.Keys.departmentName
					, HDept.pk()
					, EcologyDB.db());//申请人部门名称
//				t.CONTRACT_NUM=
				//t.RESERVED1=r.getPayOperator();//支付操作人
			//}
			j=JSONSwitcher.toJSON(jo).replaceAll("'", "\"");
		}
		Logger.log("调用资金系统接口[地址："+URL+"]/[数据]："+Logger.hiddenBox(j));
		String s=Poster.jx(URL, "gbk", j);
		Logger.log("返回结果："+s);
		JSONObject jo=new JSONObject(s);
		if("0000".equals(jo.getString("RESP_CODE")))return null;
		else {
			CMD defaultCMD = EcologyDB.db();
			List<PR> prs=SQL.list(defaultCMD, PR.class
					,SQL.cd.equals(PR.Keys.prb, r.getId()));
			for(PR pr:prs){
				SQL.update(pr.setPayStatus(""+PR.PayStatus.Fail),EcologyDB.db()
						,PR.Keys.payStatus.key());
			}
			throw new Exception(jo.getString("RESP_MSG"));
		}
	}
	
	public static Map<String,String> f(String b) throws Exception{
		Map<String,String> m=new HashMap<String, String>();
		JO302 p=_.new JO302();
		p.BATCH_ID=b;
		String j=JSONSwitcher.toJSON(p).replaceAll("'", "\"");
		Logger.log("调用资金系统接口[地址："+URL+"]/[数据]："+Logger.hiddenBox(j));
		String x=Poster.jx(URL, j);
		Logger.log("返回结果："+x);
		JSONObject jo=new JSONObject(x);
		if(!"0000".equals(jo.getString("RESP_CODE"))){
			Logger.log("接收异常，不返回任何数据");
			return m;
		}
		JSONArray ja=jo.getJSONArray("LIST");
		for(int k=0;k<ja.length();k++){
			jo=ja.getJSONObject(k);
			String r;try {r=jo.getString("SERIAL");
			} catch (Exception e) {r=jo.getString("serial");}
			String v;try {v=jo.getString("RESULT_FLAG");
			} catch (Exception e) {v=jo.getString("result_flag");}
			m.put(r, v);
		}
		return m;
	}
	
	public static PSM _=new PSM();
	public class JO302 {
		public String TRANS_CODE="302";//交易代码
		public String BATCH_ID="";//批次号
		public String SERIAL="";//批次号
	}
	public class JO301 {
		public String TRANS_CODE="301";//交易代码
		public String BATCH_ID="";//批次号
		public List<LIST> LIST=new ArrayList<LIST>();
		public class LIST {
			public String COMPANY_CODE="";//付款单位编号
			public String ACCOUNT_CODE="";//付款单位账号
			public String REC_ACCOUNT_CODE="";//收款账号 
			public String REC_ACCOUNT_NAME="";//收款户名
			public String REC_BANK_CODE="";//收款方联行号
			public String REC_BANK_NAME="";//开户行全称
			public String CURR_CODE="";//币种代码
			public String TRADE_AMOUNT="";//交易金额
			public String PURPOSE="";//事由
			public String LOCAL_FLAG="";//同城异地
			public String TRADE_SPEED="";//加急标志
			public String PUBLIC_FLAG="";//对公对私
			public String SERIAL="";//流水号
			public String REMARK="OA";//备注
			public String APPLICANT="";//申请人
			public String DEPARTMENT="";//申请人部门
			public String CONTRACT_NUM="";//合同编号
			public String RESERVED1="";//预留字段1
			public String RESERVED2="";//预留字段2
			public String RESERVED3="";//预留字段3
			public String RESERVED4="";//预留字段4
			public String RESERVED5="";//预留字段5
			public String RESERVED6="";//预留字段6
		}
	}
	

}
