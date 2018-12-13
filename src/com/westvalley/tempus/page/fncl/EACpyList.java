package com.westvalley.tempus.page.fncl;

import java.util.List;

import com.westvalley.tempus.carry.fncl.EACpy;
import com.westvalley.tempus.timer.EACpyLoader;

import crivia.ecp.carry.eb.MoreMenu;
import crivia.ecp.carry.eb.TopTitle;
import crivia.ecp.carry.eb.TabPage.Image;
import crivia.ecp.carry.em.EDP;
import crivia.ecp.carry.html.X;
import crivia.ecp.carry.tableString.TableSQL;
import crivia.ecp.carry.tableString.TableString;
import crivia.ecp.carry.tableString.TableString.Type;
import crivia.ecp.common.Menu;
import crivia.ecp.p.Lister;
import crivia.mvc.carry.Page;
import crivia.mvc.i.ExecuteMethod;

public class EACpyList extends Lister {
	
	//接收POST请求
	@ExecuteMethod(ExecuteMethod.Type.Post)
	public Page load(){
		//进行同步操作
		EACpyLoader.load();
		//jsp转发到当前页，EU(this)指本控制器(即this)的默认访问地址
		return Page.forward(EU(this));
	}

	//列表方法，要求在此构建并返回tableString对象
	@Override
	protected TableString table() {
		// 构造tableSQL，本例子直接使用实体类生成简单SQL
		TableSQL q=new EACpy().simpleTableSQL();
		//将tableSQL作为tableString的查询条件
		TableString t=new TableString(q);
		//加入显示列
		t.addTableColumn("名称", EACpy.Keys.name);
		t.addTableColumn("编码", EACpy.Keys.code);
		t.addTableColumn("付款操作人", EACpy.Keys.paymentOperatorName.key());
		t.addTableColumn("付款查询操作人", EACpy.Keys.paymentSearchOperatorName.key());
		
		t.addClickLink("设置付款操作人", "Edit()", EACpy.pk());
		//去掉记录开头的checkbox
		t.setTableType(Type.None);
		
		return t;
	}
	
	//此处可新增额外的html代码（本例子用于增加自定义的javascript）
	@Override
	protected String HTMLAbove() {
		return ""+X.JavaSctipt(""
				//javascript函数，post表单到后台load方法，立即执行同步
				+"\nfunction Load(){"
				//EU(this,"load")指本控制器(即this)的"load"方法的访问地址
				+"\n	_form().action='"+EU(this,"load")+"';"
				+"\n	_form().submit();"
				+"\n}"
				+"\n		function Edit(id){"
				+"\n			_E.e8o.open("+new EDP(EU(EACpyEdit.class)
						+"?p-id='+id+'", 500, 600, "设置付款操作人")+");"
				+"\n		}");
	}
	
	//右键菜单，可通过setInTop转化为功能按钮
	@Override
	protected void moreMenus(List<MoreMenu> menus) {
		menus.add(Menu.create("立即同步", "Load();").setInTop(true));
		super.moreMenus(menus);
	}
	
	//页面标题
	@Override
	protected TopTitle topTitle() {
		return new TopTitle("费用报销公司查询", Image.Arrow.file);
	}

}
