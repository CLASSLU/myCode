package com.sdjxd.common.model;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import org.apache.commons.beanutils.BeanUtils;

import com.sdjxd.pms.platform.form.model.TreeNodeBean;

public class MyTreeNodeBean extends TreeNodeBean {

	public MyTreeNodeBean() {

	}

	public MyTreeNodeBean(TreeNodeBean tnb) {
		this.setObjectId(tnb.getObjectId());
		this.setChecked(tnb.isChecked());
		this.setIconSrc(tnb.getIconSrc());
		this.setId(tnb.getId());
		this.setTableId(tnb.getTableId());
		this.setText(tnb.getText());
		this.setExpanded(tnb.isExpanded());
		this.setDisabled(tnb.isDisabled());
		this.setGetData(tnb.isGetData());
		this.attachField = tnb.attachField;
	}

	public void addChild(MyTreeNodeBean tnb) {
		MyTreeNodeBean[] tempChilds = (MyTreeNodeBean[]) this.getChilds();
		if (tempChilds == null) {
			tempChilds = new MyTreeNodeBean[1];
			tempChilds[0] = tnb;
			this.setChilds(tempChilds);
		} else {
			// tempChilds = Arrays.copyOf(tempChilds, tempChilds.length + 1,
			// tempChilds.getClass());
			MyTreeNodeBean[] newtempChilds = new MyTreeNodeBean[tempChilds.length + 1];
			System
					.arraycopy(tempChilds, 0, newtempChilds, 0,
							tempChilds.length);
			newtempChilds[tempChilds.length] = tnb;
			this.setChilds(newtempChilds);
		}

	}

	public TreeNodeBean getTreeNodeByObjectId(String objectId) {
		if (this.getObjectId() != null && this.getObjectId().equals(objectId)) {
			return this;
		} else {
			TreeNodeBean temp = null;
			if (this.getChilds() != null && this.getChilds().length > 0) {

				for (TreeNodeBean tnb : this.getChilds()) {

					temp = ((MyTreeNodeBean) tnb)
							.getTreeNodeByObjectId(objectId);
					if (temp != null) {
						break;
					}
				}
				return temp;
			} else {
				return null;
			}
		}
	}

	public boolean removeChild(MyTreeNodeBean o) {
		MyTreeNodeBean[] childs = (MyTreeNodeBean[]) this.getChilds();
		if (childs != null && childs.length > 0) {
			for (int index = 0; index < childs.length; index++)
				if (o.equals(childs[index])) {
					fastRemove(index);
					return true;
				}
		}
		return false;
	}

	private void fastRemove(int index) {
		int numMoved = this.getChilds().length - index - 1;
		if (numMoved > 0)
			System.arraycopy(this.getChilds(), index + 1, this.getChilds(),
					index, numMoved);
	}

}
