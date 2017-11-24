/*jadclipse*/// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.

package com.sdjxd.pms.platform.form.web;

import com.sdjxd.pms.platform.base.DataStatus;
import com.sdjxd.pms.platform.form.service.Form;
import com.sdjxd.pms.platform.form.service.FormInstance;
import com.sdjxd.pms.platform.organize.User;
import com.sdjxd.pms.platform.tool.StringTool;
import com.sdjxd.pms.platform.workflow.service.FlowInstance;
import com.sdjxd.pms.platform.workflow.service.FlowInstanceSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.*;

public class ShowFormAction123 extends Action
{

    public ShowFormAction123()
    {
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    {
    	try{
    		response.setHeader("Cache-Control", "no-cache");
            User user = User.getCurrentUser();
            if(user == null && request.getParameter("p") != null && !request.getParameter("p").equals("3C9387F3-20B8-4F4C-BB0A-3CDE1881AADC"))
                throw new Exception("\u60A8\u8FD8\u6CA1\u6709\u767B\u5F55\u6216\u8005\u767B\u5F55\u8D85\u65F6,\u8BF7\u91CD\u65B0\u767B\u5F55\uFF01");
            FormInstance formData = (FormInstance)request.getAttribute("formData");
            if(formData == null)
            {
                String flowSessionKey = request.getParameter("h_flowSession");
                String flowId = request.getParameter("h_flowId");
                String flowInstanceId = request.getParameter("h_flowInstanceId");
                int flowNodeInstanceId = StringTool.isEmpty(request.getParameter("h_flowNodeInstanceId")) ? -1 : Integer.parseInt(request.getParameter("h_flowNodeInstanceId"));
                int flowNodeId = StringTool.isEmpty(request.getParameter("h_flowNodeId")) ? -1 : Integer.parseInt(request.getParameter("h_flowNodeId"));
                int flowFormId = StringTool.isEmpty(request.getParameter("h_flowFormId")) ? -1 : Integer.parseInt(request.getParameter("h_flowFormId"));
                String formInstanceId = request.getParameter("h_formInstanceId");
                boolean isCreateForm = request.getParameter("h_formcreate") != null ? request.getParameter("h_formcreate").equals("1") : false;
                boolean isCreateAttach = request.getParameter("h_attach") != null ? request.getParameter("h_attach").equals("1") : false;
                String type = request.getParameter("h_type");
                if(type == null || type.length() == 0)
                    type = (String)request.getAttribute("h_type");
                if(type == null || type.length() == 0)
                    type = "1".equals(request.getParameter("h_formcreate")) ? "create" : "show";
                if(type.length() == 0)
                    type = "show";
                String patternId = request.getParameter("p");
                if(patternId == null || patternId.length() == 0)
                    patternId = request.getParameter("h_formId");
                if(patternId == null || patternId.length() == 0)
                    patternId = (String)request.getAttribute("p");
                if(formInstanceId == null || formInstanceId.length() == 0)
                    formInstanceId = request.getParameter("s");
                if(formInstanceId == null || formInstanceId.length() == 0)
                    formInstanceId = (String)request.getAttribute("s");
                FlowInstanceSession flowSession = null;
                if(!StringTool.isEmpty(flowId) || !StringTool.isEmpty(flowInstanceId))
                {
                    if(!StringTool.isEmpty(flowSessionKey))
                        flowSession = FlowInstanceSession.getSession(flowSessionKey);
                    if(flowSession == null)
                        flowSession = new FlowInstanceSession(flowId, flowInstanceId, flowNodeInstanceId, flowNodeId, flowFormId, formInstanceId, isCreateAttach, isCreateForm);
                }
                if(FlowInstance.formInstanceIdIsKey(formInstanceId) && flowSession != null)
                    formInstanceId = flowSession.getFormInstanceId();
                if(type.equals("create") || flowSession != null && flowSession.isCreateForm())
                {
                    String lastPatternId = Form.getFormIdByVersion(patternId);
                    if(!StringTool.isEmpty(lastPatternId))
                        patternId = lastPatternId;
                    formData = FormInstance.create(patternId, request, response);
                    if(formInstanceId != null && formInstanceId.length() != 0)
                    {
                        formData.init(patternId, formInstanceId);
                        String pk = formData.getPattern().getIndexTablePkCol();
                        formData.initCell(pk, formInstanceId);
                    } else
                    {
                        formInstanceId = formData.getId();
                    }
                } else
                {
                    if(flowSession != null && StringTool.isEmpty(formInstanceId))
                        formInstanceId = flowSession.getFormInstanceId();
                    if(!StringTool.isEmpty(formInstanceId))
                    {
                        formData = FormInstance.fromDb(formInstanceId, patternId);
                    } else
                    {
                        String lastPatternId = Form.getFormIdByVersion(patternId);
                        if(!StringTool.isEmpty(lastPatternId))
                            patternId = lastPatternId;
                        formData = FormInstance.fromDb(patternId);
                    }
                }
                if(formData == null)
                    throw new Exception("\u53C2\u6570\u4F20\u9012\u9519\u8BEF\uFF0C\u6CA1\u6709\u627E\u5230\u5BF9\u5E94\u8868\u5355\uFF01");
                if(formData.getDataStatus() != null && formData.getDataStatus().equals(DataStatus.DELETE))
                    throw new Exception("\u8BE5\u8868\u5355\u5DF2\u88AB\u5220\u9664\uFF01");
                if(flowSession != null)
                {
                    FlowInstance flowInstance = flowSession.getFlowInstance();
                    if(flowInstance != null)
                    {
                        int nodeInstanceId = flowSession.getNodeInstanceId();
                        flowInstance.exec(nodeInstanceId);
                        formData.setFlowInstance(flowInstance, nodeInstanceId, flowSession.getFormId());
                        if(flowInstance.checkLimit(nodeInstanceId))
                        {
                            if(!StringTool.isEmpty(formInstanceId))
                            {
                                flowInstance.addFormInstance(flowSession.getFormId(), formInstanceId, nodeInstanceId);
                                formData.setLimitByFlow(flowInstance.getFormLimit(flowSession.getFormId(), nodeInstanceId));
                            } else
                            {
                                formData.setReadonly();
                            }
                        } else
                        {
                            formData.setRecordOpener(false);
                            log.info("\u60A8\u6CA1\u6709\u5F53\u524D\u6D41\u7A0B\u8282\u70B9\u4E0B\u7684\u6743\u9650\uFF01");
                            formData.addScript("zflow_limit_1", "alert('\u60A8\u6CA1\u6709\u5F53\u524D\u6D41\u7A0B\u8282\u70B9\u4E0B\u7684\u6743\u9650')");
                            formData.setLimitByFlow(flowInstance.getFormWithoutLimit(flowSession.getFormId()));
                        }
                    } else
                    {
                        throw new Exception("\u6CA1\u6709\u627E\u5230\u6307\u5B9A\u7684\u6D41\u7A0B\u5B9E\u4F8B\u7F16\u7801\uFF01");
                    }
                } else
                {
                    formData.setCellLimit();
                    formData.setCellReadOnlyByFields();
                    formData.setButtonCellReadOnly();
                }
            }
            String readOnly = request.getParameter("h_readOnly");
            if("1".equals(readOnly))
                formData.setReadonly();
            if("1".equals(request.getParameter("h_notRecordOpener")))
                formData.setRecordOpener(false);
            formData.setEditDefault();
            request.setAttribute("formName", formData.getName());
            request.setAttribute("formData", formData);
            return mapping.getInputForward();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return mapping.getInputForward();
    }

    public ActionForward error(ActionMapping mapping, HttpServletRequest request, String msg)
    {
        log.info(msg);
        request.setAttribute("error", msg);
        return mapping.findForward("error");
    }

    static Logger log;

    static 
    {
        log = Logger.getLogger(com.sdjxd.pms.platform.form.web.ShowFormAction.class);
    }
}


/*
	DECOMPILATION REPORT

	Decompiled from: D:\MyEclipse\workspace\psidpweb\WebRoot\WEB-INF\lib\hussar.jar
	Total time: 78 ms
	Jad reported messages/errors:
	Exit status: 0
	Caught exceptions:
*/