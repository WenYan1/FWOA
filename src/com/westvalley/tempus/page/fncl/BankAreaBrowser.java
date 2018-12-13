package com.westvalley.tempus.page.fncl;

import com.westvalley.tempus.carry.fncl.BankArea;

import crivia.db.a.Specially_MySQL;
import crivia.ecp.carry.eb.TabPage.Image;
import crivia.ecp.carry.eb.TopTitle;
import crivia.ecp.carry.em.TreeView;
import crivia.ecp.carry.html.div.Div;
import crivia.ecp.common.EcologyDB;
import crivia.ecp.p.browser.TreeViewBrowser;

public class BankAreaBrowser extends TreeViewBrowser {

	@Override
	protected TreeView treeView() {
		TreeView tv=new TreeView(
				BankArea.Keys.subs.key(),BankArea.class,BankArea.Keys.parent+"<0") {
			@Override
			protected TreeViewItem carry2tvi(Object o) {
				BankArea a=(BankArea) o;
				TreeViewItem e=new TreeViewItem(a.getId(), a.getName());
				return e;
			}
			@Override
			protected void elementStyle(Div e) {
				super.elementStyle(e);
				e.addAttr("onclick", Javascript_ReturnValue);
			}
		};
		//我擦 这里怎么拉了一个 MYSQL的数据源……
		tv.setCmd(EcologyDB.db("mbs",new Specially_MySQL()));
		return tv;
	}
	
	@Override
	protected TopTitle topTitle() {
		return new TopTitle("开户行地区 - 选择", Image.House.file);
	}

}
