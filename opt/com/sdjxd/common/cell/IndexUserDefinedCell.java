package com.sdjxd.common.cell;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.sdjxd.pms.platform.form.service.Form;
import com.sdjxd.pms.platform.form.service.FormInstance;
import com.sdjxd.pms.platform.form.service.cell.DynamicCell;

/**
 * ��ҳ�û��Զ���Ԫ��
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
		// ��õ�ǰҳ����ı��������
		PrintWriter html = form.getRenderHtml();
		// ������ɵ�html���
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
		// ����renderScriptObject���Ա����ɳ�ʼ��flash�Ľű���䣬����formΪ������
		renderScriptObject(form);
	}

	protected void renderScriptObject(FormInstance form) {
		StringBuffer sb = new StringBuffer();
		// ���ɴ�������Ԫ���Ľű����
		sb.append("defaultForm.addChild( ");
		// {}��Ϊ��������js�ű���ʼ��ʱ���Զ���Щ������������
		sb
				.append("new com.sdjxd.common.userDevinedCell.IndexUserDefinedCell({id:");
		sb.append(this.id);
		sb.append(",needSave:false");
		sb.append(",tagId:'");
		sb.append(this.cellId);
		sb.append("',userDefinfo:'").append(this.data.userDefinfo).append(
				"'}));\r\n");
		// ���½���flash������ӵ���ǰ����ȥ
		form.addScript("form_obj_cell_" + this.id, sb.toString());
	}
}
