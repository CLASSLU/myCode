package com.sdjxd.common.cell;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.sdjxd.pms.platform.form.service.Form;
import com.sdjxd.pms.platform.form.service.FormInstance;
import com.sdjxd.pms.platform.form.service.cell.DynamicCell;

/**
 * 首页用户自定义元件
 * 
 * @author Administrator
 * 
 */
public class IndexUserDefinedCell extends DynamicCell {

	public IndexUserDefinedCell(Form pattern) {
		super(pattern);
	}

	@SuppressWarnings("unchecked")
	public void render(FormInstance form) throws IOException {
		// 获得当前页面的文本输出对象
		PrintWriter html = form.getRenderHtml();
		// 输出生成的html语句
		html.write("<div id='" + getCellId()
				+ "' style=\"position:absolute;top:" + this.top + ";left:"
				+ this.left + "\">");
		String str = this.data.userDefinfo;
		try {
			int spliti = str.lastIndexOf(".");
			Class c = Class.forName(str.substring(0, spliti));
			Object obj = c.newInstance();
			Method m = c
					.getMethod(str.substring(spliti + 1), DynamicCell.class);
			Object result = m.invoke(obj, this);
			html.write(result.toString());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		html.write("</div>");
		// 调用renderScriptObject，以便生成初始化flash的脚本语句，参数form为表单对象
		renderScriptObject(form);
	}

	protected void renderScriptObject(FormInstance form) {
		StringBuffer sb = new StringBuffer();
		// 生成创建报表元件的脚本语句
		sb.append("defaultForm.addChild( ");
		// {}中为参数，在js脚本初始化时可以对这些参数进行引用
		sb
				.append("new com.sdjxd.common.userDevinedCell.IndexUserDefinedCell({id:");
		sb.append(this.id);
		sb.append(",needSave:false");
		sb.append(",tagId:'");
		sb.append(this.cellId);
		sb.append("',userDefinfo:'").append(this.data.userDefinfo).append(
				"'}));\r\n");
		// 将新建的flash对象添加到当前表单中去
		form.addScript("form_obj_cell_" + this.id, sb.toString());
	}
}
