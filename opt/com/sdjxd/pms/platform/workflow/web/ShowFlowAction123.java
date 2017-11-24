/*jadclipse*/// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.

package com.sdjxd.pms.platform.workflow.web;

import com.sdjxd.pms.platform.tool.StringTool;
import com.sdjxd.pms.platform.workflow.service.FlowInstanceSession;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.*;

public class ShowFlowAction123 extends Action
{

    public ShowFlowAction123()
    {
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    {
    	try{
    		String flowId = request.getParameter("f");
            String flowInstanceId = request.getParameter("i");
            if(StringTool.isEmpty(flowId))
                flowId = request.getParameter("h_flowId");
            if(StringTool.isEmpty(flowInstanceId))
                flowInstanceId = request.getParameter("h_flowInstanceId");
            int flowNodeInstanceId = StringTool.isEmpty(request.getParameter("n")) ? -1 : Integer.parseInt(request.getParameter("n"));
            int flowNodeId = StringTool.isEmpty(request.getParameter("flowNodeId")) ? -1 : Integer.parseInt(request.getParameter("flowNodeId"));
            int flowFormId = StringTool.isEmpty(request.getParameter("flowFormId")) ? -1 : Integer.parseInt(request.getParameter("flowFormId"));
            if(flowFormId == -1)
                flowFormId = StringTool.isEmpty(request.getParameter("h_flowFormId")) ? -1 : Integer.parseInt(request.getParameter("h_flowFormId"));
            String formInstanceId = request.getParameter("formInstanceId");
            boolean isCreateAttach;
            if(request.getParameter("isCreateAttach") == null)
                isCreateAttach = "1".equals(request.getParameter("h_attach"));
            else
                isCreateAttach = request.getParameter("isCreateAttach").equals("1");
            boolean isCreateForm = false;
            if(!isCreateForm)
                isCreateForm = request.getParameter("h_formcreate") != null ? request.getParameter("h_formcreate").equals("1") : false;
            FlowInstanceSession session = new FlowInstanceSession(flowId, flowInstanceId, flowNodeInstanceId, flowNodeId, flowFormId, formInstanceId, isCreateAttach, isCreateForm);
            request.getRequestDispatcher(session.getPageUrl(true)).forward(request, response);
            return null;
    	}catch(Exception e){
    		e.printStackTrace();
    	}
        return null;
    }

    private static Logger log;

    static 
    {
        log = Logger.getLogger(com.sdjxd.pms.platform.workflow.web.ShowFlowAction.class);
    }
}


/*
	DECOMPILATION REPORT

	Decompiled from: D:\MyEclipse\workspace\psidpweb\WebRoot\WEB-INF\lib\hussar.jar
	Total time: 172 ms
	Jad reported messages/errors:
	Exit status: 0
	Caught exceptions:
*/