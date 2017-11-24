package com.sdjxd.flow;

import java.util.List;

import com.sdjxd.pms.platform.workflow.service.Flow;
import com.sdjxd.pms.platform.workflow.service.FlowInstance;
import com.sdjxd.pms.platform.workflow.service.FlowNode;
import com.sdjxd.pms.platform.workflow.service.FlowNodeInstance;

public class FlowOperation {
	private static Dao dao = new Dao();

	/**
	 * �õ����붼�б��еĽ�ɫ��Ϣ
	 * 
	 * @param flowid
	 * @param flownodeid
	 * @return
	 */
	public static List getActorsList(String flowid, String flownodeid) {
		return dao.getActorsList(flowid, flownodeid);
	}

	/**
	 * 
	 * @param flowid
	 * @param flownodeid
	 * @param roles
	 * @return
	 */
	public static boolean addActors(String flowid, String flownodeid,
			String[] roles) {
		Flow.afterModify(flowid);
		return dao.addActors(flowid, flownodeid, roles);
	}

	// public static List saveActorsList(String flowid, String flownodeid) {

	// }
	public static boolean delActors(String flowid, String flownodeid,
			String[] roles) {
		Flow.afterModify(flowid);
		return dao.delActors(flowid, flownodeid, roles);

	}

	/**
	 * 
	 * @param flowid
	 * @param flownodeid
	 * @return
	 */
	public static boolean userInActors(String flowversion, String flownodeid) {
		String flowid = null;
		try {
			flowid = Flow.getFlowVersion(flowversion).getId();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Flow.afterModify(flowid);
		return dao.userInActors(flowid, flownodeid);

	}

	/**
	 * �Ƿ�Ϊ��ǩ����
	 * 
	 * @param flowId
	 * @return
	 */
	public static boolean isSignFlow(String flowId) {
		return dao.isSignFlow(flowId);
	}

	/**
	 * ���̳���
	 * @param flowInstanceId
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "static-access" })
	public static String withdraw(String flowInstanceId) {
		try {
			FlowInstance fi = FlowInstance.load(flowInstanceId);
			FlowNodeInstance cni = fi.getLastNodeInstance();
			int nodeid = cni.getFlowNodeId();
			Flow flow = fi.getTemplet();
			FlowNode currentNode = flow.getNodeById(nodeid);
			FlowNode prNode = null;
			List pnodeList = currentNode.getPreNode();
			if(pnodeList==null||pnodeList.size()==0){
				return "��ǰ���̲��ܱ����أ�";
			}else if (pnodeList.size()>1){
				return "��ǰ���̲��ܱ����أ�";
			}else{
				prNode = (FlowNode) pnodeList.get(0);
			}
			@SuppressWarnings("unused")
			FlowNodeInstance lastni = fi.getLastNodeInstance(prNode.getId());
			if(fi.callbackToCurrent(flowInstanceId, prNode.getId())){
				return "���سɹ���";
			}else{
				return "����ʧ�ܣ�";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
}
