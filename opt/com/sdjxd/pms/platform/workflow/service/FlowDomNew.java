package com.sdjxd.pms.platform.workflow.service;

import com.sdjxd.pms.platform.tool.Guid;
import com.sdjxd.pms.platform.tool.StringTool;
import com.sdjxd.pms.platform.workflow.model.FlowDomBean;
import java.util.Iterator;
import java.util.List;

public class FlowDomNew extends FlowDom
{
  private static final long serialVersionUID = 1L;
  FlowDomHtml dom;

  protected FlowDomNew()
  {
    this.dom = new FlowDomHtml();
    this.dom.setTagName("div");
  }

  public FlowDomNew getInstance() {
    return new FlowDomNew();
  }

  public void setData(FlowDomBean model) throws Exception
  {
    if (model == null)
      return;
    this.dom.setPosition("absolute");
    this.dom.setTop(model.getTop());
    this.dom.setLeft(model.getLeft());
    this.dom.setWidth(model.getWidth() + "px");
    this.dom.setHeight(model.getHeight() + "px");
    Iterator it = model.getCssClassName().iterator();

    boolean isTextShow = false;
    while (it.hasNext())
    {
      String className = (String)it.next();
      if (className.equals("F001")) {
        isTextShow = true;
      }
      this.dom.addClass(className, className);
    }
    if (isTextShow) {
      String id = Guid.create();
      int row = model.getHeight() / 20;
      if (row == 0) {
        row = 1;
      }
      int height = row * 20;
      this.dom.setHeight(height + "px");
      model.setInnerText("<div id='" + id + "' onmouseout='flowPicture_showModel_onmouseout(\"" + id + "\");' onmouseover='flowPicture_showModel_onmouseover(\"" + id + "\");' >" + StringTool.replace(model.getInnerText(), " ", "&nbsp;") + "</div>");
    }

    String innerText = model.getInnerText();
    innerText = StringTool.replace(innerText, "\r\n", "<BR>");
    this.dom.setInnerText(innerText);
  }

  public String getHtml() {
    StringBuffer html = new StringBuffer(256);
    this.dom.render(html);
    return html.toString();
  }
}