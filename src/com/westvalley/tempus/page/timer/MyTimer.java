package com.westvalley.tempus.page.timer;

import java.util.List;

import crivia.ecp.ECP;
import crivia.ecp.carry.eb.MoreMenu;
import crivia.ecp.carry.eb.TabPage.Image;
import crivia.ecp.carry.eb.TopTitle;
import crivia.ecp.carry.em.EditTable;
import crivia.ecp.carry.em.Editer;
import crivia.ecp.carry.em.TimeInput;
import crivia.ecp.carry.html.X;
import crivia.ecp.carry.html.div.Div;
import crivia.ecp.carry.html.form.Form;
import crivia.ecp.common.Menu;
import crivia.time.carry.Time;
import crivia.txt.common.Logger;
import crivia.txt.common.Text;
import crivia.txt.sc.RK;

public class MyTimer extends ECP {
	
	private TimeInput t;
	public TimeInput getT() {
		if(null==t){
			t=new TimeInput("t", _time);
		}
		return t;
	}
	public void setT(TimeInput t) {this.t = t;}

	@Override
	protected String HTMLBody() {
		try {
			_time=Time.toTime(getT().getValue()).toString();
			Text.set(MyTimer.class, CFN, _time);
		} catch (Exception e) {
			Logger.logException("Timer Save Fail.", e);
		}
		Time now=Time.now(),x=new Time(getT().getHour(),getT().getMinute(),getT().getSecond())
			,s=Time.toTime(now.toMS()>x.toMS()?Time.FullTimeMS-now.toMS()+x.toMS():x.toMS()-now.toMS());
		String k=RK.rk(11);
		Form f=new Form(this,formKey);{
			EditTable t=new EditTable();
			f.setInner(t);
			t.addRow(new Editer("当前触发时间", _time));
			t.addRow(new Editer("下次触发时间", ""+new Div()
				.setInner(s).addAttr("id", k)+X.TimeBack(k)));
			t.addRow(new Editer("设置触发时间", getT()));
		}
		return ""+f;
	}
	
	@Override
	protected void moreMenus(List<MoreMenu> menus) {
		menus.add(Menu.Select(formKey).setName("保存").setInTop(true));
	}
	
	@Override
	protected TopTitle topTitle() {
		return new TopTitle("触发时间管理", Image.Calendar.file);
	}
	
	public static void x(){
		// TODO 此处写到时间时，你的动作
	}

	private static final String CFN="MyTimer-ExecuteTime";
	private static String _time="0:0:0";
	private static final Thread _=new Thread(){
		public void run() {
			try {
				while(true){
					sleep(1000);
					Time t=Time.toTime(_time);
					Time now=Time.now();
					if(!t.equals(now))continue;
					try {
						x();
					} catch (Throwable e) {
						Logger.logException("Timer Execute Fail.", e);
					}
				}
			} catch (InterruptedException e) {}
		};
	};
	static{
		try{
			String _=Text.get(MyTimer.class, CFN);
			_time=""+Time.toTime(_);
		}catch(Exception e){
			Logger.logException("Timer Init Fail.", e);
		}
		_.start();
	}

}
