package com.sdjxd.flow;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.sdjxd.pms.development.pagenum.service.Pagenum;
import com.sdjxd.pms.platform.base.DataModify;
import com.sdjxd.pms.platform.form.service.FormInstance;
import com.sdjxd.pms.platform.organize.User;
import com.sdjxd.pms.platform.workflow.model.FlowOperatorBean;
import com.sdjxd.pms.platform.workflow.service.Flow;
import com.sdjxd.pms.platform.workflow.service.FlowActor;
import com.sdjxd.pms.platform.workflow.service.FlowFormInstance;
import com.sdjxd.pms.platform.workflow.service.FlowInstance;
import com.sdjxd.pms.platform.workflow.service.FlowNode;
import com.sdjxd.pms.platform.workflow.service.FlowNodeEnd;
import com.sdjxd.pms.platform.workflow.service.FlowNodeInstance;
import com.sdjxd.pms.platform.workflow.service.FlowNodeStart;
import com.sdjxd.pms.platform.workflow.service.FlowOperator;

public class CommonFlowEvent {
	private static Dao dao = new Dao();

	/**
	 * ���浱ǰ��������������Ϣ��������˱���
	 * 
	 * @param flowNodeInstance
	 * @param arg
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean saveStartLog(FlowNodeInstance flowNodeInstance,
			Object arg) {
		try {
			// ����ʵ����Ϊ��
			if (flowNodeInstance != null) {

				String flowInstanceId = flowNodeInstance.getFlowInstanceId();
				// ����ʵ��
				FlowInstance flowInstance = FlowInstance
						.loadFromCache(flowInstanceId);
				// ����
				Flow flow = flowInstance.getTemplet();
				// ���̽��ID
				int flowNodeId = flowNodeInstance.getFlowNodeId();
				// ���̽��
				FlowNode flowNode = flow.getNodeById(flowNodeId);
				// Ĭ��Ʊ��
				int formId = flowNode.getDefaultForm();
				// ���̱�ʵ��
				FlowFormInstance flowFormInstance = flowInstance
						.getForm(formId);
				// ��ʵ��ID
				String formInstanceId = flowFormInstance.getInstanceId();
				boolean start = flowNode instanceof FlowNodeStart;
				int runStatus = flowNodeInstance.getRunStatus();
				boolean isComplete = FlowInstance.RUNSTATUS_COMPLET == runStatus;
				FlowNodeInstance lastNodeInstance = flowInstance
						.getLastNodeInstance();
				boolean isUntreadNode = dao.isUntreadNode(flowInstanceId,
						String.valueOf(flowNode.getId()));
				if (start && isComplete) {
					User curruser = User.getCurrentUser();
					Map receivermap = lastNodeInstance.getOperatorManager()
							.getReceivers();
					Iterator it = receivermap.keySet().iterator();
					while (it.hasNext()) {
						FlowOperator fob = (FlowOperator) receivermap.get(it
								.next());
						dao.saveStartLog(formInstanceId, curruser, fob,
								isUntreadNode);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * ͨ������ҳ�ţ�Ҫ��ҳ���ֶ�Ϊ PAGENUM
	 * 
	 * @param flowNodeInstance
	 *            ���̽��ʵ��
	 * @param arg
	 * @return
	 * @throws Exception
	 */
	public static boolean createPageNum(FlowNodeInstance flowNodeInstance,
			Object arg) throws Exception {
		// ����ʵ����Ϊ��
		if (flowNodeInstance != null) {
			String flowInstanceId = flowNodeInstance.getFlowInstanceId();
			// ����ʵ��
			FlowInstance flowInstance = FlowInstance
					.loadFromCache(flowInstanceId);
			// ����
			Flow flow = flowInstance.getTemplet();
			// ���̽��ID
			int flowNodeId = flowNodeInstance.getFlowNodeId();
			// ���̽��
			FlowNode flowNode = flow.getNodeById(flowNodeId);
			// Ĭ��Ʊ��
			int formId = flowNode.getDefaultForm();
			// ���̱�ʵ��
			FlowFormInstance flowFormInstance = flowInstance.getForm(formId);
			// ��ʵ��ID
			String formInstanceId = flowFormInstance.getInstanceId();
			// ��ģ��ID
			String patternId = flow.getForm(formId).getFormPatternId();
			// FormInstance formInstance =
			// FormInstance.loadFromCache(formInstanceId);
			FormInstance formInstance = FormInstance.load(formInstanceId,
					patternId);
			if (formInstance.getIndex().get("PAGENUM") == null
					|| formInstance.getIndex().get("PAGENUM").equals("")) {
				String pagenum = Pagenum.create(formInstanceId);
				// ��Ʊ��Ԫ���ϰ����ɵ�ҳ��
				formInstance.addIndex("PAGENUM", pagenum);
			}
			return true;
		} else {
			return false;
		}
	}
}
