package com.sdjxd.common;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.sql.RowSet;

import org.apache.log4j.Logger;

import com.sdjxd.common.model.MyForm;
import com.sdjxd.common.model.MyTreeNodeBean;
import com.sdjxd.common.model.Organise;
import com.sdjxd.common.model.User;
import com.sdjxd.pms.platform.base.Global;
import com.sdjxd.pms.platform.data.DbOper;
import com.sdjxd.pms.platform.form.model.TreeNodeBean;
import com.sdjxd.pms.platform.form.service.Form;
import com.sdjxd.pms.platform.form.service.FormInstance;
import com.sdjxd.pms.platform.table.dao.TableDao;
import com.sdjxd.pms.platform.tool.StringTool;
import com.sdjxd.pms.platform.workflow.service.Flow;
import com.sdjxd.pms.platform.workflow.service.FlowActor;
import com.sdjxd.pms.platform.workflow.service.FlowActorList;
import com.sdjxd.pms.platform.workflow.service.FlowInstance;
import com.sdjxd.pms.platform.workflow.service.FlowNode;
import com.sdjxd.pms.platform.workflow.service.FlowNodeInstance;
import com.sdjxd.pms.platform.workflow.service.WaitDo;
import com.sdjxd.util.BeanUtil;

public class CommOperation {
	public static Logger log = Logger.getLogger(CommOperation.class.getName());
	private static CommDao dao = new CommDao();
	//private CommSql sqlHelper = new CommSql();

	/**
	 * 得到配置文件指定的属性
	 * 
	 * @param propertyName
	 * @return
	 */
	public static String getGlobalProperty(String propertyName) {
		return Global.getConfig(propertyName);
	}

	/**
	 * 重新生成转状态人员选择树，添加人员所在部门
	 * 
	 * @param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Object newSubmit(String flowInstanceId, int nodeInstanceId,
			HashMap toNodes, boolean isFreeSubmit) throws Exception {
		CommSql sqlHelper = new CommSql();
		Object result = FlowInstance.submit(flowInstanceId, nodeInstanceId,
				toNodes, isFreeSubmit);
		if (result instanceof List) {
			List treeNodes;

			treeNodes = (List) result;

			for (int i = 0; i < treeNodes.size(); i++) {
				TreeNodeBean flowNode = (TreeNodeBean) treeNodes.get(i);
				TreeNodeBean[] users = flowNode.getChilds();
				TreeNodeBean dept;
				Map<String, TreeNodeBean> depts = new HashMap<String, TreeNodeBean>();
				if (users != null) {
					String icon = "";
					String TSql = sqlHelper.getTableByName("JXD7_XT_ORGANISE");
					RowSet rs = null;
					try {
						rs = DbOper.executeQuery(TSql);
						if (rs.next()) {
							icon = Global.getName() + rs.getString("PICPATH");
						}
						rs = null;
					} catch (Exception e) {
						log.error(e.getMessage());
					}
					StringBuffer userIds = new StringBuffer("'");
					Map userDeptMap = new HashMap();
					Map deptMap = new HashMap();
					for (int j = 0; j < users.length; j++) {
						userIds.append(users[j].getObjectId()).append("','");
						if (j % 998 == 0) {
							userIds.append("') or U.USERID IN ('");
						}
					}
					userIds.append("'");
					String userWithDeptNameSql = sqlHelper
							.getUserOrgainse(userIds.toString());
					RowSet rsUser = null;
					try {
						rsUser = DbOper.executeQuery(userWithDeptNameSql);
						while (rsUser.next()) {
							userDeptMap.put(rsUser.getString("USERID"), rsUser
									.getString("DEPTID"));
							Organise org = new Organise();
							org.setId(rsUser.getString("DEPTID"));
							org.setName(rsUser.getString("ORGANISENAME"));
							org.setShoworder(rsUser.getString("OSHOWORDER"));
							deptMap.put(org.getId(), org);
						}
						rsUser = null;
					} catch (Exception e) {
						log.error(e.getMessage());
					}
					Iterator it = deptMap.keySet().iterator();
					while (it.hasNext()) {
						String deptId = (String) it.next();
						dept = new TreeNodeBean();
						dept.setIconSrc(icon);
						Organise to = (Organise) deptMap.get(deptId);
						dept.setText(to.getName());
						dept.attachField.put("SHOWORDER", to.getShoworder());
						dept.setId(flowNode.getId() + deptId);
						dept.setObjectId(deptId);
						dept.setChilds(FilterByDeptId(users, userDeptMap,
								deptId));
						dept.setTableId("Dept");
						dept.setExpanded(true);
						dept.setChecked(true);
						dept.setDisabled(true);
						dept.setGetData(true);
						depts.put(deptId, dept);
					}
					// for(int j=0;j<users.length;j++)
					// {
					// String userId = users[j].getObjectId();
					// com.sdjxd.pms.platform.organize.User user =
					// com.sdjxd.pms.platform.organize.User.getUser(userId);
					// String deptId = user.getDeptId();
					//						
					// if(!depts.containsKey(deptId))
					// {
					// dept = new TreeNodeBean();
					// dept.setIconSrc(icon);
					// String TSql =
					// sqlHelper.getTableByName("JXD7_XT_ORGANISE");
					// RowSet rs = null;
					// try {
					// rs = DbOper.executeQuery(TSql);
					// if (rs.next()) {
					// dept.setIconSrc(Global.getName() +
					// rs.getString("PICPATH"));
					// }
					// rs = null;
					// } catch (Exception e) {
					// log.error(e.getMessage());
					// }
					// String DSql = sqlHelper.getOrganise(deptId);
					// RowSet rs1 = null;
					// try {
					// rs1 = DbOper.executeQuery(DSql);
					// if (rs1.next()) {
					// dept.setText(rs1.getString("ORGANISENAME"));
					// }
					// rs1 = null;
					// } catch (Exception e) {
					// log.error(e.getMessage());
					// }
					// dept.setId(flowNode.getId()+deptId);
					// dept.setObjectId(deptId);
					// dept.setChilds(FilterByDeptId(users,deptId));
					// dept.setTableId("Dept");
					// dept.setExpanded(true);
					// dept.setChecked(true);
					// dept.setDisabled(true);
					// dept.setGetData(true);
					// depts.put(deptId, dept);
					// }
					//						
					// }
					flowNode.setChilds(Map2Array(depts));
				}
			}

			return treeNodes;
		} else {
			return result;
		}
	}

	/**
	 * 重新生成转状态人员选择树型组织机构人员
	 * 
	 * @param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Object newTreeOrgSubmit(String flowInstanceId,
			int nodeInstanceId, HashMap toNodes, boolean isFreeSubmit)
			throws Exception {
		CommSql sqlHelper = new CommSql();
		Object result = FlowInstance.submit(flowInstanceId, nodeInstanceId,
				toNodes, isFreeSubmit);
		if (result instanceof List) {
			List treeNodes;

			treeNodes = (List) result;
			List resultNodes = new ArrayList();

			FlowInstance flowInstance = FlowInstance.load(flowInstanceId);
			Flow flow = flowInstance.getTemplet();
			for (int i = 0; i < treeNodes.size(); i++) {
				TreeNodeBean flowNode = (TreeNodeBean) treeNodes.get(i);
				MyTreeNodeBean myflowNode = new MyTreeNodeBean(
						(TreeNodeBean) treeNodes.get(i));
				TreeNodeBean[] users = flowNode.getChilds();
				String flowNodeId = flowNode.getObjectId();
				List<FlowActor> flowActors = flow.getNodeById(Integer.parseInt(flowNodeId)).getFlowActors();
				FlowActor actor = flowActors.get(0);
				if (!(actor instanceof FlowActorList) || !((FlowActorList)actor).isMoreSelect()) //flowNode.getChilds() == null
				{
					resultNodes.add(myflowNode);
					continue;
				}
				
				Map roles = ((FlowActorList)actor).getActors();
				MyTreeNodeBean dept = null;
				//Map<String, TreeNodeBean> depts = new HashMap<String, TreeNodeBean>();
				if (users != null && roles != null) {
					String icon = "";
					String TSql = sqlHelper.getTableByName("JXD7_XT_ORGANISE");
					RowSet rs = null;
					try {
						rs = DbOper.executeQuery(TSql);
						if (rs.next()) {
							icon = Global.getName() + rs.getString("PICPATH");
						}
						rs = null;
					} catch (Exception e) {
						log.error(e.getMessage());
					}

					Map userDeptMap = new HashMap();
					
					StringBuffer selRoles = new StringBuffer(1024);
					selRoles.append("'");
					selRoles.append(StringTool.concatKey(roles,"','"));
					selRoles.append("'");
					String userWithDeptNameSql = sqlHelper
							.getUserInfo(selRoles.toString());
					RowSet rsUser = null;
					RowSet rsDept = null;
					try {
						rsDept = DbOper.executeQuery(sqlHelper
								.getFlowActorDepts(selRoles.toString()));
						while (rsDept.next()) {
							String deptid = rsDept.getString("ORGANISEID");
							String preorganiseid = rsDept
									.getString("PREORGANISEID") == null ? ""
									: rsDept.getString("PREORGANISEID");
							MyTreeNodeBean tempDept = new MyTreeNodeBean();
							if (dept != null
									&& dept.getParentObjectId() != null
									&& dept.getParentObjectId().equals(
											preorganiseid)) {
								tempDept = dept;
							} else {
								tempDept = new MyTreeNodeBean();
							}
							tempDept.setIconSrc(icon);
							tempDept.setText(rsDept.getString("ORGANISENAME"));
							tempDept.setParentObjectId(preorganiseid);
							tempDept.setId(flowNode.getId() + deptid);
							tempDept.setObjectId(deptid);
							tempDept.setTableId("Dept");
							tempDept.setExpanded(true);
							tempDept.setChecked(true);
							tempDept.setDisabled(true);
							tempDept.setGetData(true);
							if (dept != null
									&& dept.getParentObjectId() != null
									&& dept.getParentObjectId().equals(
											preorganiseid)) {
								continue;
							}
							if (dept == null) {
								dept = tempDept;
								// tempDept.setParentId(myflowNode.getId());
								// tempDept.setParentObjectId(myflowNode
								// .getParentObjectId());
							} else if (dept
									.getTreeNodeByObjectId(preorganiseid) == null) {
								if (preorganiseid.equals("")) {
									tempDept.addChild(dept);
									dept.setParentId(tempDept.getId());
									dept.setParentObjectId(tempDept
											.getParentObjectId());
									dept = tempDept;
									dept.setParentId(myflowNode.getId());
									dept.setParentObjectId(myflowNode
											.getObjectId());
								} else if (preorganiseid.equals(dept
										.getParentObjectId())) {
									MyTreeNodeBean tempRootDept = new MyTreeNodeBean();
									tempRootDept.setObjectId(preorganiseid);
									tempRootDept.addChild(dept);
									tempRootDept.addChild(tempDept);
									tempDept = tempRootDept;
								} else if (deptid.equals(dept
										.getParentObjectId())) {
									tempDept.addChild(dept);
									dept.setParentId(tempDept.getId());
									dept.setParentObjectId(tempDept
											.getParentObjectId());

								}
							} else {
								MyTreeNodeBean curDept = (MyTreeNodeBean) dept
										.getTreeNodeByObjectId(preorganiseid);
								curDept.addChild(tempDept);
								tempDept.setParentId(curDept.getId());
								tempDept.setParentObjectId(curDept
										.getParentObjectId());
							}

						}
						rsUser = DbOper.executeQuery(userWithDeptNameSql);
						while (rsUser.next()) {
							userDeptMap.put(rsUser.getString("USERID"), rsUser
									.getString("DEPTID"));
						}
						rsUser = null;
						rsDept = null;
					} catch (Exception e) {
						e.printStackTrace();
					}
					for (int j = 0; j < users.length && dept != null; j++) {
						MyTreeNodeBean tnb = new MyTreeNodeBean(users[j]);
						MyTreeNodeBean curdept = (MyTreeNodeBean) dept
								.getTreeNodeByObjectId((String) userDeptMap
										.get(tnb.getObjectId()));
						if (curdept != null) {
							curdept.addChild(tnb);
							tnb.setParentId(flowNode.getId());
							tnb.setParentObjectId(flowNode.getObjectId());
						}
					}
					myflowNode.addChild(dept);
					resultNodes.add(myflowNode);
				}
			}

			return resultNodes;
		} else {
			return result;
		}
	}

	/**
	 * 重新生成转状态人员选择树型组织机构（本级、上级、下级，同级不看）
	 * 
	 * @param
	 * @return
	 */
	public static Object reciveOrgUpAndDown(String flowInstanceId, int nodeInstanceId,
			HashMap toNodes, boolean isFreeSubmit) throws Exception {
		//System.out.println("================================== "+Calendar.getInstance().getTimeInMillis());
		CommSql sqlHelper = new CommSql();
		FlowInstance flowInstance = FlowInstance.load(flowInstanceId);
		Flow flow = flowInstance.getTemplet();
		FlowNodeInstance flowNodeInstance = flowInstance.getFlowNodeInstance(nodeInstanceId);
		int currentFlowNodeId = flowNodeInstance.getFlowNodeId();
		FlowNode currentFlowNode = flow.getNodeById(currentFlowNodeId);
		HashMap nodes = new HashMap();
		List treeNodes = currentFlowNode.getNextNodesNoUser(flowInstance, nodeInstanceId, nodes, isFreeSubmit);

		boolean allnoneuser = true;// 所有结点都没有人
		if (treeNodes!=null && treeNodes.size()>0)
		{
			List resultNodes = new ArrayList();

			for (int i = 0; i < treeNodes.size(); i++) {
				TreeNodeBean flowNode = (TreeNodeBean) treeNodes.get(i);
				MyTreeNodeBean myflowNode = new MyTreeNodeBean(
						(TreeNodeBean) treeNodes.get(i));
				myflowNode.setIconSrc(Global.getName()
						+ "/pms/platform/image/flow.gif");
				String flowNodeId = flowNode.getObjectId();
				List<FlowActor> flowActors = flow.getNodeById(Integer.parseInt(flowNodeId)).getFlowActors();
				FlowActor actor = flowActors.get(0);
				if (!(actor instanceof FlowActorList) || !((FlowActorList)actor).isMoreSelect()) //flowNode.getChilds() == null
				{
					resultNodes.add(myflowNode);
					continue;
				}
				
				Map roles = ((FlowActorList)actor).getActors();
				//TreeNodeBean[] users = flowNode.getChilds();
				MyTreeNodeBean dept = null;
				//Map<String, TreeNodeBean> depts = new HashMap<String, TreeNodeBean>();
				
				if (roles!=null)
				{
					allnoneuser = false;
					StringBuffer selRoles = new StringBuffer(1024);
					selRoles.append("'");
					selRoles.append(StringTool.concatKey(roles,"','"));
					selRoles.append("'");
					
					//Map<String,String> userDeptMap = new HashMap<String,String>();
					String icon = "";
					icon = Global.getName()	+ "/pms/platform/image/home.png";
					//String userWithDeptNameSql = sqlHelper.getUserInfo(selRoles.toString());
					//RowSet rsUser = null;
					RowSet rsDept = null;
					try {
						rsDept = DbOper.executeQuery(sqlHelper
								.getFlowActorDeptsUpAndDown(selRoles.toString()));
						while (rsDept.next()) {
							String deptid = rsDept.getString("ORGANISEID");
							String preorganiseid = rsDept
									.getString("PREORGANISEID") == null ? ""
									: rsDept.getString("PREORGANISEID");
							MyTreeNodeBean tempDept = null;
							if (dept != null
									&& dept.getParentObjectId() != null
									&& dept.getParentObjectId().equals(
											preorganiseid)) {
								//tempDept = dept;
								continue;
							} else {
								tempDept = new MyTreeNodeBean();
								tempDept.setIconSrc(icon);
								tempDept.setText(rsDept.getString("ORGANISENAME"));
								tempDept.setParentObjectId(preorganiseid);
								tempDept.setId(flowNode.getId() + deptid);
								tempDept.setObjectId(deptid);
								tempDept.setTableId("Dept");
								tempDept.setExpanded(true);
								tempDept.setChecked(true);
								tempDept.setDisabled(false);
								tempDept.setGetData(true);
								tempDept.attachField.put("ROLE", selRoles.toString()); //附加该部门的角色
								tempDept.attachField.put("USERS", "附加");// 附加一个用户处理没有人员时过滤条件不管用
							}

							if (dept == null) {
								dept = tempDept;
							} else if (dept
									.getTreeNodeByObjectId(preorganiseid) == null) {
								if (preorganiseid.equals("")) {
									tempDept.addChild(dept);
									dept.setParentId(tempDept.getId());
									dept.setParentObjectId(tempDept
											.getParentObjectId());
									dept = tempDept;
									dept.setParentId(myflowNode.getId());
									dept.setParentObjectId(myflowNode
											.getObjectId());
								} else if (preorganiseid.equals(dept
										.getParentObjectId())) {
									MyTreeNodeBean tempRootDept = new MyTreeNodeBean();
									tempRootDept.setObjectId(preorganiseid);
									tempRootDept.addChild(dept);
									tempRootDept.addChild(tempDept);
									tempDept = tempRootDept;
								} else if (deptid.equals(dept
										.getParentObjectId())) {
									tempDept.addChild(dept);
									dept.setParentId(tempDept.getId());
									dept.setParentObjectId(tempDept
											.getParentObjectId());

								}
							} else {
								MyTreeNodeBean curDept = (MyTreeNodeBean) dept
										.getTreeNodeByObjectId(preorganiseid);
								curDept.addChild(tempDept);
								tempDept.setParentId(curDept.getId());
								tempDept.setParentObjectId(curDept
										.getParentObjectId());
							}

						}

						rsDept = null;
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					if (dept != null)
					{
						myflowNode.addChild(dept);
						dept.setParentId(myflowNode.getId());
					}
					resultNodes.add(myflowNode);
				} else {// 如果接收结点没有接收人
					resultNodes.add(myflowNode);
				}
			}
			if (allnoneuser) {
				return treeNodes;
			}
			return resultNodes;
		} else {
			Object result = FlowInstance.submit(flowInstanceId, nodeInstanceId,
					toNodes, isFreeSubmit);
			return result;
		}
	}
	
	/**
	 * 重新生成转状态人员选择树型组织机构（本级以及上级）
	 * 
	 * @param
	 * @return
	 */
	public static Object reciveOrg(String flowInstanceId, int nodeInstanceId,
			HashMap toNodes, boolean isFreeSubmit) throws Exception {
		//System.out.println("================================== "+Calendar.getInstance().getTimeInMillis());
		CommSql sqlHelper = new CommSql();
		FlowInstance flowInstance = FlowInstance.load(flowInstanceId);
		Flow flow = flowInstance.getTemplet();
		FlowNodeInstance flowNodeInstance = flowInstance.getFlowNodeInstance(nodeInstanceId);
		int currentFlowNodeId = flowNodeInstance.getFlowNodeId();
		FlowNode currentFlowNode = flow.getNodeById(currentFlowNodeId);
		HashMap nodes = new HashMap();
		List treeNodes = currentFlowNode.getNextNodesNoUser(flowInstance, nodeInstanceId, nodes, isFreeSubmit);

		boolean allnoneuser = true;// 所有结点都没有人
		if (treeNodes!=null && treeNodes.size()>0)
		{
			List resultNodes = new ArrayList();

			for (int i = 0; i < treeNodes.size(); i++) {
				TreeNodeBean flowNode = (TreeNodeBean) treeNodes.get(i);
				MyTreeNodeBean myflowNode = new MyTreeNodeBean(
						(TreeNodeBean) treeNodes.get(i));
				myflowNode.setIconSrc(Global.getName()
						+ "/pms/platform/image/flow.gif");
				String flowNodeId = flowNode.getObjectId();
				List<FlowActor> flowActors = flow.getNodeById(Integer.parseInt(flowNodeId)).getFlowActors();
				FlowActor actor = flowActors.get(0);
				if (!(actor instanceof FlowActorList) || !((FlowActorList)actor).isMoreSelect()) //flowNode.getChilds() == null
				{
					resultNodes.add(myflowNode);
					continue;
				}
				
				Map roles = ((FlowActorList)actor).getActors();
				//TreeNodeBean[] users = flowNode.getChilds();
				MyTreeNodeBean dept = null;
				//Map<String, TreeNodeBean> depts = new HashMap<String, TreeNodeBean>();
				
				if (roles!=null)
				{
					allnoneuser = false;
					StringBuffer selRoles = new StringBuffer(1024);
					selRoles.append("'");
					selRoles.append(StringTool.concatKey(roles,"','"));
					selRoles.append("'");
					
					//Map<String,String> userDeptMap = new HashMap<String,String>();
					String icon = "";
					icon = Global.getName()	+ "/pms/platform/image/home.png";
					//String userWithDeptNameSql = sqlHelper.getUserInfo(selRoles.toString());
					//RowSet rsUser = null;
					RowSet rsDept = null;
					try {
						rsDept = DbOper.executeQuery(sqlHelper
								.getFlowActorDepts(selRoles.toString()));
						while (rsDept.next()) {
							String deptid = rsDept.getString("ORGANISEID");
							String preorganiseid = rsDept
									.getString("PREORGANISEID") == null ? ""
									: rsDept.getString("PREORGANISEID");
							MyTreeNodeBean tempDept = null;
							if (dept != null
									&& dept.getParentObjectId() != null
									&& dept.getParentObjectId().equals(
											preorganiseid)) {
								//tempDept = dept;
								continue;
							} else {
								tempDept = new MyTreeNodeBean();
								tempDept.setIconSrc(icon);
								tempDept.setText(rsDept.getString("ORGANISENAME"));
								tempDept.setParentObjectId(preorganiseid);
								tempDept.setId(flowNode.getId() + deptid);
								tempDept.setObjectId(deptid);
								tempDept.setTableId("Dept");
								tempDept.setExpanded(true);
								tempDept.setChecked(true);
								tempDept.setDisabled(false);
								tempDept.setGetData(true);
								tempDept.attachField.put("ROLE", selRoles.toString()); //附加该部门的角色
								tempDept.attachField.put("USERS", "附加");// 附加一个用户处理没有人员时过滤条件不管用
							}

							if (dept == null) {
								dept = tempDept;
							} else if (dept
									.getTreeNodeByObjectId(preorganiseid) == null) {
								if (preorganiseid.equals("")) {
									tempDept.addChild(dept);
									dept.setParentId(tempDept.getId());
									dept.setParentObjectId(tempDept
											.getParentObjectId());
									dept = tempDept;
									dept.setParentId(myflowNode.getId());
									dept.setParentObjectId(myflowNode
											.getObjectId());
								} else if (preorganiseid.equals(dept
										.getParentObjectId())) {
									MyTreeNodeBean tempRootDept = new MyTreeNodeBean();
									tempRootDept.setObjectId(preorganiseid);
									tempRootDept.addChild(dept);
									tempRootDept.addChild(tempDept);
									tempDept = tempRootDept;
								} else if (deptid.equals(dept
										.getParentObjectId())) {
									tempDept.addChild(dept);
									dept.setParentId(tempDept.getId());
									dept.setParentObjectId(tempDept
											.getParentObjectId());

								}
							} else {
								MyTreeNodeBean curDept = (MyTreeNodeBean) dept
										.getTreeNodeByObjectId(preorganiseid);
								curDept.addChild(tempDept);
								tempDept.setParentId(curDept.getId());
								tempDept.setParentObjectId(curDept
										.getParentObjectId());
							}

						}

						rsDept = null;
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					if (dept != null)
					{
						myflowNode.addChild(dept);
						dept.setParentId(myflowNode.getId());
					}
//					myflowNode.addChild(dept);
//					dept.setParentId(myflowNode.getId());
					resultNodes.add(myflowNode);
				} else {// 如果接收结点没有接收人
					resultNodes.add(myflowNode);
				}
			}
			if (allnoneuser) {
				return treeNodes;
			}
			return resultNodes;
		} else {
			Object result = FlowInstance.submit(flowInstanceId, nodeInstanceId,
					toNodes, isFreeSubmit);
			return result;
		}
	}
	/**
	 * 重新生成转状态人员选择树型组织机构（本级以及上级）
	 * 隐患模块专用（根据隐患等级提交到不同的部门）
	 * @param
	 * @return
	 */
	public static Object reciveOrgYhgl(String flowInstanceId, int nodeInstanceId,
			HashMap toNodes, boolean isFreeSubmit) throws Exception {
		//System.out.println("================================== "+Calendar.getInstance().getTimeInMillis())
		String yhdj="";
		String sql="select yhdj from tb_hse_yh_yhgldj where flowobjectid='"+flowInstanceId+"'";
		try {
			ResultSet rs = DbOper.executeQuery(sql);

			if (rs.next()) {
				yhdj = rs.getString("yhdj");
				}
			}
		catch(Exception e)
			{
				e.printStackTrace();
			}
			
		CommSql sqlHelper = new CommSql();
		FlowInstance flowInstance = FlowInstance.load(flowInstanceId);
		Flow flow = flowInstance.getTemplet();
		FlowNodeInstance flowNodeInstance = flowInstance.getFlowNodeInstance(nodeInstanceId);
		int currentFlowNodeId = flowNodeInstance.getFlowNodeId();
		FlowNode currentFlowNode = flow.getNodeById(currentFlowNodeId);
		HashMap nodes = new HashMap();
		List treeNodes = currentFlowNode.getNextNodesNoUser(flowInstance, nodeInstanceId, nodes, isFreeSubmit);

		boolean allnoneuser = true;// 所有结点都没有人
		if (treeNodes!=null && treeNodes.size()>0)
		{
			List resultNodes = new ArrayList();

			for (int i = 0; i < treeNodes.size(); i++) {
				TreeNodeBean flowNode = (TreeNodeBean) treeNodes.get(i);
				MyTreeNodeBean myflowNode = new MyTreeNodeBean(
						(TreeNodeBean) treeNodes.get(i));
				myflowNode.setIconSrc(Global.getName()
						+ "/pms/platform/image/flow.gif");
				String flowNodeId = flowNode.getObjectId();
				List<FlowActor> flowActors = flow.getNodeById(Integer.parseInt(flowNodeId)).getFlowActors();
				FlowActor actor = flowActors.get(0);
				if (!(actor instanceof FlowActorList) || !((FlowActorList)actor).isMoreSelect()) //flowNode.getChilds() == null
				{
					resultNodes.add(myflowNode);
					continue;
				}
				
				Map roles = ((FlowActorList)actor).getActors();
				//TreeNodeBean[] users = flowNode.getChilds();
				MyTreeNodeBean dept = null;
				//Map<String, TreeNodeBean> depts = new HashMap<String, TreeNodeBean>();
				
				if (roles!=null)
				{
					allnoneuser = false;
					StringBuffer selRoles = new StringBuffer(1024);
					selRoles.append("'");
					selRoles.append(StringTool.concatKey(roles,"','"));
					selRoles.append("'");
					
					//Map<String,String> userDeptMap = new HashMap<String,String>();
					String icon = "";
					icon = Global.getName()	+ "/pms/platform/image/home.png";
					//String userWithDeptNameSql = sqlHelper.getUserInfo(selRoles.toString());
					//RowSet rsUser = null;
					RowSet rsDept = null;
					try {
						if(yhdj.equals("3"))
						{
							rsDept = DbOper.executeQuery(sqlHelper
									.getFlowActorDepts(selRoles.toString()));
						}
						else
						{
							rsDept = DbOper.executeQuery(sqlHelper
									.getFlowActorDeptYhdj(selRoles.toString()));
						}
						
						while (rsDept.next()) {
							String deptid = rsDept.getString("ORGANISEID");
							String preorganiseid = rsDept
									.getString("PREORGANISEID") == null ? ""
									: rsDept.getString("PREORGANISEID");
							MyTreeNodeBean tempDept = null;
							if (dept != null
									&& dept.getParentObjectId() != null
									&& dept.getParentObjectId().equals(
											preorganiseid)) {
								//tempDept = dept;
								continue;
							} else {
								tempDept = new MyTreeNodeBean();
								tempDept.setIconSrc(icon);
								tempDept.setText(rsDept.getString("ORGANISENAME"));
								tempDept.setParentObjectId(preorganiseid);
								tempDept.setId(flowNode.getId() + deptid);
								tempDept.setObjectId(deptid);
								tempDept.setTableId("Dept");
								tempDept.setExpanded(true);
								tempDept.setChecked(true);
								tempDept.setDisabled(false);
								tempDept.setGetData(true);
								tempDept.attachField.put("ROLE", selRoles.toString()); //附加该部门的角色
								tempDept.attachField.put("USERS", "附加");// 附加一个用户处理没有人员时过滤条件不管用
							}

							if (dept == null) {
								dept = tempDept;
							} else if (dept
									.getTreeNodeByObjectId(preorganiseid) == null) {
								if (preorganiseid.equals("")) {
									tempDept.addChild(dept);
									dept.setParentId(tempDept.getId());
									dept.setParentObjectId(tempDept
											.getParentObjectId());
									dept = tempDept;
									dept.setParentId(myflowNode.getId());
									dept.setParentObjectId(myflowNode
											.getObjectId());
								} else if (preorganiseid.equals(dept
										.getParentObjectId())) {
									MyTreeNodeBean tempRootDept = new MyTreeNodeBean();
									tempRootDept.setObjectId(preorganiseid);
									tempRootDept.addChild(dept);
									tempRootDept.addChild(tempDept);
									tempDept = tempRootDept;
								} else if (deptid.equals(dept
										.getParentObjectId())) {
									tempDept.addChild(dept);
									dept.setParentId(tempDept.getId());
									dept.setParentObjectId(tempDept
											.getParentObjectId());

								}
							} else {
								MyTreeNodeBean curDept = (MyTreeNodeBean) dept
										.getTreeNodeByObjectId(preorganiseid);
								curDept.addChild(tempDept);
								tempDept.setParentId(curDept.getId());
								tempDept.setParentObjectId(curDept
										.getParentObjectId());
							}

						}

						rsDept = null;
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					if (dept != null)
					{
						myflowNode.addChild(dept);
						dept.setParentId(myflowNode.getId());
					}
//					myflowNode.addChild(dept);
//					dept.setParentId(myflowNode.getId());
					resultNodes.add(myflowNode);
				} else {// 如果接收结点没有接收人
					resultNodes.add(myflowNode);
				}
			}
			if (allnoneuser) {
				return treeNodes;
			}
			return resultNodes;
		} else {
			Object result = FlowInstance.submit(flowInstanceId, nodeInstanceId,
					toNodes, isFreeSubmit);
			return result;
		}
	}
	/**
	 * 重新生成转状态人员选择树型组织机构（本级及本级以下）
	 * 
	 * @param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Object reciveOrgDown(String flowInstanceId,
			int nodeInstanceId, HashMap toNodes, boolean isFreeSubmit)
			throws Exception {
//		System.out.println("================================== "+Calendar.getInstance().getTimeInMillis());
		CommSql sqlHelper = new CommSql();
		FlowInstance flowInstance = FlowInstance.load(flowInstanceId);
		Flow flow = flowInstance.getTemplet();
		FlowNodeInstance flowNodeInstance = flowInstance.getFlowNodeInstance(nodeInstanceId);
		int currentFlowNodeId = flowNodeInstance.getFlowNodeId();
		FlowNode currentFlowNode = flow.getNodeById(currentFlowNodeId);
		HashMap nodes = new HashMap();
		List treeNodes = currentFlowNode.getNextNodesNoUser(flowInstance, nodeInstanceId, nodes, isFreeSubmit);

		boolean allnoneuser = true;// 所有结点都没有人
		if (treeNodes!=null && treeNodes.size()>0)
		{
			List resultNodes = new ArrayList();

			for (int i = 0; i < treeNodes.size(); i++) {
				TreeNodeBean flowNode = (TreeNodeBean) treeNodes.get(i);
				MyTreeNodeBean myflowNode = new MyTreeNodeBean(
						(TreeNodeBean) treeNodes.get(i));
				myflowNode.setIconSrc(Global.getName()
						+ "/pms/platform/image/flow.gif");
				String flowNodeId = flowNode.getObjectId();
				List<FlowActor> flowActors = flow.getNodeById(Integer.parseInt(flowNodeId)).getFlowActors();
				FlowActor actor = flowActors.get(0);
				if (!(actor instanceof FlowActorList) || !((FlowActorList)actor).isMoreSelect()) //flowNode.getChilds() == null
				{
					resultNodes.add(myflowNode);
					continue;
				}
				
				Map roles = ((FlowActorList)actor).getActors();
				//TreeNodeBean[] users = flowNode.getChilds();
				MyTreeNodeBean dept = null;
				//Map<String, TreeNodeBean> depts = new HashMap<String, TreeNodeBean>();
				
				if (roles!=null)
				{
					allnoneuser = false;
					StringBuffer selRoles = new StringBuffer(1024);
					selRoles.append("'");
					selRoles.append(StringTool.concatKey(roles,"','"));
					selRoles.append("'");
					
					//Map<String,String> userDeptMap = new HashMap<String,String>();
					String icon = "";
					icon = Global.getName()	+ "/pms/platform/image/home.png";
					//String userWithDeptNameSql = sqlHelper.getUserInfo(selRoles.toString());
					//RowSet rsUser = null;
					RowSet rsDept = null;
					try {
						rsDept = DbOper.executeQuery(sqlHelper
								.getFlowActorDeptsDown(selRoles.toString()));
						while (rsDept.next()) {
							String deptid = rsDept.getString("ORGANISEID");
							String preorganiseid = rsDept
									.getString("PREORGANISEID") == null ? ""
									: rsDept.getString("PREORGANISEID");
							MyTreeNodeBean tempDept = null;
							if (dept != null
									&& dept.getParentObjectId() != null
									&& dept.getParentObjectId().equals(
											preorganiseid)) {
								//tempDept = dept;
								continue;
							} else {
								tempDept = new MyTreeNodeBean();
								tempDept.setIconSrc(icon);
								tempDept.setText(rsDept.getString("ORGANISENAME"));
								tempDept.setParentObjectId(preorganiseid);
								tempDept.setId(flowNode.getId() + deptid);
								tempDept.setObjectId(deptid);
								tempDept.setTableId("Dept");
								tempDept.setExpanded(true);
								tempDept.setChecked(true);
								tempDept.setDisabled(false);
								tempDept.setGetData(true);
								tempDept.attachField.put("ROLE", selRoles.toString()); //附加该部门的角色
								tempDept.attachField.put("USERS", "附加");// 附加一个用户处理没有人员时过滤条件不管用
							}

							if (dept == null) {
								dept = tempDept;
							} else if (dept
									.getTreeNodeByObjectId(preorganiseid) == null) {
								if (preorganiseid.equals("")) {
									tempDept.addChild(dept);
									dept.setParentId(tempDept.getId());
									dept.setParentObjectId(tempDept
											.getParentObjectId());
									dept = tempDept;
									dept.setParentId(myflowNode.getId());
									dept.setParentObjectId(myflowNode
											.getObjectId());
								} else if (preorganiseid.equals(dept
										.getParentObjectId())) {
									MyTreeNodeBean tempRootDept = new MyTreeNodeBean();
									tempRootDept.setObjectId(preorganiseid);
									tempRootDept.addChild(dept);
									tempRootDept.addChild(tempDept);
									tempDept = tempRootDept;
								} else if (deptid.equals(dept
										.getParentObjectId())) {
									tempDept.addChild(dept);
									dept.setParentId(tempDept.getId());
									dept.setParentObjectId(tempDept
											.getParentObjectId());

								}
							} else {
								MyTreeNodeBean curDept = (MyTreeNodeBean) dept
										.getTreeNodeByObjectId(preorganiseid);
								curDept.addChild(tempDept);
								tempDept.setParentId(curDept.getId());
								tempDept.setParentObjectId(curDept
										.getParentObjectId());
							}

						}

						rsDept = null;
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					if (dept != null)
					{
						myflowNode.addChild(dept);
						dept.setParentId(myflowNode.getId());
					}
//					myflowNode.addChild(dept);
//					dept.setParentId(myflowNode.getId());
					resultNodes.add(myflowNode);
				} else {// 如果接收结点没有接收人
					resultNodes.add(myflowNode);
				}
			}
			if (allnoneuser) {
				return treeNodes;
			}
			return resultNodes;
		} else {
			Object result = FlowInstance.submit(flowInstanceId, nodeInstanceId,
					toNodes, isFreeSubmit);
			return result;
		}
	}
	
	/**
	 * 重新生成转状态人员选择树型组织机构（无过滤）
	 * 
	 * @param
	 * @return
	 */
	public static Object reciveOrgNoFilter(String flowInstanceId, int nodeInstanceId,
			HashMap toNodes, boolean isFreeSubmit) throws Exception {
		//System.out.println("================================== "+Calendar.getInstance().getTimeInMillis());
		CommSql sqlHelper = new CommSql();
		FlowInstance flowInstance = FlowInstance.load(flowInstanceId);
		Flow flow = flowInstance.getTemplet();
		FlowNodeInstance flowNodeInstance = flowInstance.getFlowNodeInstance(nodeInstanceId);
		int currentFlowNodeId = flowNodeInstance.getFlowNodeId();
		FlowNode currentFlowNode = flow.getNodeById(currentFlowNodeId);
		HashMap nodes = new HashMap();
		List treeNodes = currentFlowNode.getNextNodesNoUser(flowInstance, nodeInstanceId, nodes, isFreeSubmit);

		boolean allnoneuser = true;// 所有结点都没有人
		if (treeNodes!=null && treeNodes.size()>0)
		{
			List resultNodes = new ArrayList();

			for (int i = 0; i < treeNodes.size(); i++) {
				TreeNodeBean flowNode = (TreeNodeBean) treeNodes.get(i);
				MyTreeNodeBean myflowNode = new MyTreeNodeBean(
						(TreeNodeBean) treeNodes.get(i));
				myflowNode.setIconSrc(Global.getName()
						+ "/pms/platform/image/flow.gif");
				String flowNodeId = flowNode.getObjectId();
				List<FlowActor> flowActors = flow.getNodeById(Integer.parseInt(flowNodeId)).getFlowActors();
				FlowActor actor = flowActors.get(0);
				if (!(actor instanceof FlowActorList) || !((FlowActorList)actor).isMoreSelect()) //flowNode.getChilds() == null
				{
					resultNodes.add(myflowNode);
					continue;
				}
				
				Map roles = ((FlowActorList)actor).getActors();
				//TreeNodeBean[] users = flowNode.getChilds();
				MyTreeNodeBean dept = null;
				//Map<String, TreeNodeBean> depts = new HashMap<String, TreeNodeBean>();
				
				if (roles!=null)
				{
					allnoneuser = false;
					StringBuffer selRoles = new StringBuffer(1024);
					selRoles.append("'");
					selRoles.append(StringTool.concatKey(roles,"','"));
					selRoles.append("'");
					
					//Map<String,String> userDeptMap = new HashMap<String,String>();
					String icon = "";
					icon = Global.getName()	+ "/pms/platform/image/home.png";
					//String userWithDeptNameSql = sqlHelper.getUserInfo(selRoles.toString());
					//RowSet rsUser = null;
					RowSet rsDept = null;
					try {
						rsDept = DbOper.executeQuery(sqlHelper
								.getFlowActorDeptsNoFilter(selRoles.toString()));
						while (rsDept.next()) {
							String deptid = rsDept.getString("ORGANISEID");
							String preorganiseid = rsDept
									.getString("PREORGANISEID") == null ? ""
									: rsDept.getString("PREORGANISEID");
							MyTreeNodeBean tempDept = null;
							if (dept != null
									&& dept.getParentObjectId() != null
									&& dept.getParentObjectId().equals(
											preorganiseid)) {
								//tempDept = dept;
								continue;
							} else {
								tempDept = new MyTreeNodeBean();
								tempDept.setIconSrc(icon);
								tempDept.setText(rsDept.getString("ORGANISENAME"));
								tempDept.setParentObjectId(preorganiseid);
								tempDept.setId(flowNode.getId() + deptid);
								tempDept.setObjectId(deptid);
								tempDept.setTableId("Dept");
								tempDept.setExpanded(true);
								tempDept.setChecked(true);
								tempDept.setDisabled(false);
								tempDept.setGetData(true);
								tempDept.attachField.put("ROLE", selRoles.toString()); //附加该部门的角色
								tempDept.attachField.put("USERS", "附加");// 附加一个用户处理没有人员时过滤条件不管用
							}

							if (dept == null) {
								dept = tempDept;
							} else if (dept
									.getTreeNodeByObjectId(preorganiseid) == null) {
								if (preorganiseid.equals("")) {
									tempDept.addChild(dept);
									dept.setParentId(tempDept.getId());
									dept.setParentObjectId(tempDept
											.getParentObjectId());
									dept = tempDept;
									dept.setParentId(myflowNode.getId());
									dept.setParentObjectId(myflowNode
											.getObjectId());
								} else if (preorganiseid.equals(dept
										.getParentObjectId())) {
									MyTreeNodeBean tempRootDept = new MyTreeNodeBean();
									tempRootDept.setObjectId(preorganiseid);
									tempRootDept.addChild(dept);
									tempRootDept.addChild(tempDept);
									tempDept = tempRootDept;
								} else if (deptid.equals(dept
										.getParentObjectId())) {
									tempDept.addChild(dept);
									dept.setParentId(tempDept.getId());
									dept.setParentObjectId(tempDept
											.getParentObjectId());

								}
							} else {
								MyTreeNodeBean curDept = (MyTreeNodeBean) dept
										.getTreeNodeByObjectId(preorganiseid);
								curDept.addChild(tempDept);
								tempDept.setParentId(curDept.getId());
								tempDept.setParentObjectId(curDept
										.getParentObjectId());
							}

						}

						rsDept = null;
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					if (dept != null)
					{
						myflowNode.addChild(dept);
						dept.setParentId(myflowNode.getId());
					}
					resultNodes.add(myflowNode);
				} else {// 如果接收结点没有接收人
					resultNodes.add(myflowNode);
				}
			}
			if (allnoneuser) {
				return treeNodes;
			}
			return resultNodes;
		} else {
			Object result = FlowInstance.submit(flowInstanceId, nodeInstanceId,
					toNodes, isFreeSubmit);
			return result;
		}
	}

	@SuppressWarnings("unchecked")
	public static TreeNodeBean[] Map2Array(Map map) {
		List<TreeNodeBean> Arrays = new ArrayList<TreeNodeBean>();
		Iterator it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();

			if (entry.getKey() instanceof String) {
				TreeNodeBean Array = (TreeNodeBean) map.get(entry.getKey());
				Arrays.add(Array);
			}
		}
		Comparator comp = new Mycomparator();
		Collections.sort(Arrays, comp);// 排序
		return (TreeNodeBean[]) Arrays.toArray(new TreeNodeBean[Arrays.size()]);
	}

	public static TreeNodeBean[] FilterByDeptId(TreeNodeBean[] users,
			String DeptId) {
		List<TreeNodeBean> Arrays = new ArrayList<TreeNodeBean>();
		int len = users.length;
		for (int i = 0; i < len; i++) {
			String userId = users[i].getObjectId();
			com.sdjxd.pms.platform.organize.User user = com.sdjxd.pms.platform.organize.User
					.getUser(userId);
			String deptId = user.getDeptId();
			if (deptId.equals(DeptId)) {
				Arrays.add(users[i]);
			}
		}
		TreeNodeBean[] object = (TreeNodeBean[]) Arrays
				.toArray(new TreeNodeBean[Arrays.size()]);
		return object;
	}

	/**
	 * 通过部门ID过滤人员
	 * 
	 * @param users
	 * @param userDept
	 * @param deptId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static TreeNodeBean[] FilterByDeptId(TreeNodeBean[] users,
			Map userDept, String deptId) {
		List<TreeNodeBean> Arrays = new ArrayList<TreeNodeBean>();
		int len = users.length;
		for (int i = 0; i < len; i++) {
			String userId = users[i].getObjectId();

			if (userDept.get(userId).equals(deptId)) {
				Arrays.add(users[i]);
			}
		}
		Comparator comp = new Mycomparator();
		Collections.sort(Arrays, comp);// 排序
		TreeNodeBean[] object = (TreeNodeBean[]) Arrays
				.toArray(new TreeNodeBean[Arrays.size()]);
		return object;
	}

	/**
	 * 同步组织机构方法，包括同步组织机构、设置层次码、设置showorder
	 * @return
	 */
	public static boolean syncOrg(){
		try{
			if(synchronizOrg() && resetOrgLeavl() && CommDao.resetOrgShoworder()){
				int i = DbOper.executeNonQuery("INSERT INTO JXD7_XT_ROLEMORGAN W1  (W1.ROLEID,W1.ORGANID )  SELECT 'B3708176-CD5E-CDF8-68CC-68ABEBAD04F6',F.ORGANISEID    FROM JXD7_XT_ORGANISE F   WHERE F.ORGANISEID NOT IN         (SELECT T.ORGANID            FROM JXD7_XT_ROLEMORGAN T           WHERE T.ROLEID = 'B3708176-CD5E-CDF8-68CC-68ABEBAD04F6')");
				if(i >= 0)
					return true;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 检查当日组织机头是否同步，如果未同步则进行同步。
	 */
	public static void checkOrgSync(){
		try{
			String isOrgSync = Global.getConfig("hse.isOrgSync");
			
			if (isOrgSync.equals("1"))
			{
				String lastUpdateTime = "2000-01-01";
				String nowTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
				String sql = "SELECT TVALUE FROM TB_HSE_COMMONCODE WHERE CODE = 'ORGSYNCTIME'";
				RowSet rs = DbOper.executeQuery(sql);
				if(rs.next()){
					lastUpdateTime = rs.getString("TVALUE");
				}else{
					DbOper.executeNonQuery("INSERT INTO TB_HSE_COMMONCODE (SHEETID, CODE, CODENAME, TVALUE) VALUES ('" + 
							UUID.randomUUID().toString() + "', 'ORGSYNCTIME', '', '')");
				}
				
				if(nowTime.compareTo(lastUpdateTime) > 0){
					syncOrg();
					DbOper.executeNonQuery("UPDATE TB_HSE_COMMONCODE SET TVALUE = '" + nowTime + "' WHERE CODE = 'ORGSYNCTIME'");
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 同步组织机构
	 * 
	 * @return
	 */
	public static boolean synchronizOrg() {
		/**
		 * 同步组织机构SQL
		 */
		String insertOrgSql = "insert into JXD7_XT_ORGANISE W1"
				+ "(ORGANISEID,"
				+ "ORGANISENAME,"
				+ "SHORTNAME,"
				+ "PREORGANISEID,"
				+ "SHOWORDER,"
				+ "ORGANISETYPE,"
				+ "DATASTATUSID,"
				+ "CREATEDATE,"
				+ "CREATEUSERID,"
				+ "U_ERPCODE," 
				+ "BASEPROKIND_ID," 
				+ "EX_ORGANISEID," +
						"BLOCK_ID," +
						"DEPLEVEL_ID," +
						"HSE_CLASS)"
				+ " select t.org_id as ORGANISEID,"
				+ "    T.U_NAME_FULL AS ORGANISENAME,"
				+ "    T.U_NAME_SHORT AS SHORTNAME,"
				+ "    T.ORG_ID_SUPERIOR AS PREORGANISEID,"
				+ "    T.U_ORDER AS SHOWORDER,"
				+ "    T.U_TYPE AS ORGANISETYPE,"
				+ "    T.U_VALIDATE AS DATASTATUSID,"
				+ "    T.U_SUBMITTIME AS CREATEDATE,"
				+ "'',U_ERPCODE, T.BASEPROKIND_ID AS BASEPROKIND_ID," 
				+ "	t.org_id as EX_ORGANISEID,"
				+ " t.BLOCK_ID AS BLOCK_ID,"
				+ " T.DEPLEVEL_ID AS DEPLEVEL_ID,"
				+ "T.U_CLASS AS HSE_CLASS"
				+ " from "
				+ Global.getConfig("hsedbuser")
				+ ".auth_organization_tb T "
				+ " where to_char( T.ORG_ID) NOT IN (SELECT O.ORGANISEID FROM JXD7_XT_ORGANISE O) and t.u_validate = 1";
		/**
		 * 同步组织机构修改信息的SQL
		 */
		String orgInfoSql = "UPDATE JXD7_XT_ORGANISE O"
				+ " SET O.ORGANISENAME  = (SELECT T.U_NAME_FULL FROM "
				+ Global.getConfig("hsedbuser")
				+ ".auth_organization_tb T"
				+ " WHERE TO_CHAR(T.ORG_ID) = O.ORGANISEID),"
				+ " O.SHOWORDER = (SELECT nvl(T.U_ORDER,0) "
				+ "FROM "
				+ Global.getConfig("hsedbuser")
				+ ".auth_organization_tb T WHERE TO_CHAR(T.ORG_ID) = O.ORGANISEID),"
				+ " O.DATASTATUSID  = (SELECT T.U_VALIDATE FROM "
				+ Global.getConfig("hsedbuser")
				+ ".auth_organization_tb T WHERE TO_CHAR(T.ORG_ID) = O.ORGANISEID),"
				+ "O.PREORGANISEID = (SELECT T.ORG_ID_SUPERIOR FROM "
				+ Global.getConfig("hsedbuser")
				+ ".auth_organization_tb T WHERE TO_CHAR(T.ORG_ID) = O.ORGANISEID),"
				+ "O.U_NO = (SELECT T.U_NO FROM "
				+ Global.getConfig("hsedbuser")
				+ ".auth_organization_tb T WHERE TO_CHAR(T.ORG_ID) = O.ORGANISEID),"
				+ "O.U_ERPCODE = (SELECT T.U_ERPCODE FROM "
				+ Global.getConfig("hsedbuser")
				+ ".auth_organization_tb T WHERE TO_CHAR(T.ORG_ID) = O.ORGANISEID),"
				+ "O.BASEPROKIND_ID = (SELECT T.BASEPROKIND_ID FROM "
				+ Global.getConfig("hsedbuser")
				+ ".auth_organization_tb T WHERE TO_CHAR(T.ORG_ID) = O.ORGANISEID),"
				+ "O.EX_ORGANISEID = (SELECT T.ORG_ID FROM "
				+ Global.getConfig("hsedbuser")
				+ ".auth_organization_tb T WHERE TO_CHAR(T.ORG_ID) = O.ORGANISEID),"
				+ "O.BLOCK_ID = (SELECT T.BLOCK_ID FROM "
				+ Global.getConfig("hsedbuser")
				+ ".auth_organization_tb T WHERE TO_CHAR(T.ORG_ID) = O.ORGANISEID),"
				+ "O.DEPLEVEL_ID = (SELECT T.DEPLEVEL_ID FROM "
				+ Global.getConfig("hsedbuser")
				+ ".auth_organization_tb T WHERE TO_CHAR(T.ORG_ID) = O.ORGANISEID),"
				+ "O.HSE_CLASS = (SELECT T.U_CLASS FROM "
				+ Global.getConfig("hsedbuser")
				+ ".auth_organization_tb T WHERE TO_CHAR(T.ORG_ID) = O.ORGANISEID)"
				+ " WHERE O.DATASTATUSID = 1 and O.ORGANISEID IN (SELECT TO_CHAR(T.ORG_ID) FROM "
				+ Global.getConfig("hsedbuser") + ".auth_organization_tb T)";
		List sqlList = new ArrayList();
		sqlList.add(insertOrgSql);
		sqlList.add(orgInfoSql);
		try {
			return DbOper.executeNonQuery(sqlList) == -1 ? false : true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * 重写层次码实际操作
	 * 
	 * @param orgId
	 * @return
	 */
	public static boolean resetLeavlByOrg(String orgId, String rootLevel,
			int nLevelNum) {
		String sql = "SELECT * FROM JXD7_XT_ORGANISE WHERE ORGANISEID = '"
				+ orgId + "'";
		try {
			Map<?, ?> info = DbOper.executeMap(sql);
			if (info.get("PREORGANISEID") == null) {
				String orgInfoSql = "UPDATE JXD7_XT_ORGANISE O SET O.ORGANISELEVEL = '"
						+ rootLevel + "' WHERE O.ORGANISEID= '" + orgId + "'";
				DbOper.executeNonQuery(orgInfoSql);
			}
			String sqlchild = "SELECT * FROM JXD7_XT_ORGANISE WHERE PREORGANISEID = '"
					+ orgId + "' ORDER BY SHOWORDER";
			List childList = DbOper.executeList(sqlchild);
			if (childList != null && childList.size() > 0) {
				for (int i = 1; i <= childList.size(); i++) {
					Map<?, ?> chileinfo = (Map<?, ?>) childList.get(i - 1);
					String orgInfoSql = "UPDATE JXD7_XT_ORGANISE O SET O.ORGANISELEVEL = (SELECT O.ORGANISELEVEL||LPAD('"
							+ i
							+ "',"
							+ nLevelNum
							+ ",'0') FROM JXD7_XT_ORGANISE O WHERE O.ORGANISEID = '"
							+ orgId
							+ "') WHERE O.ORGANISEID= '"
							+ chileinfo.get("ORGANISEID") + "'";
					DbOper.executeNonQuery(orgInfoSql);
					resetLeavlByOrg((String) chileinfo.get("ORGANISEID"),
							rootLevel, nLevelNum);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * 重写层次码 外部接口
	 * 
	 * @param orgId
	 * @return
	 */
	public static boolean resetOrgLeavl() {
		String orgId = "";
		int orgLevelNum = 2;
		java.util.Properties dbProps = new java.util.Properties();
		dbProps = Global.loadProperties("global.properties");

		String strLevelNum = dbProps.getProperty("OrgLevel.num");
		if (strLevelNum == null || strLevelNum == "") {
			orgLevelNum = 2;
		} else {
			orgLevelNum = Integer.parseInt(strLevelNum);
		}
		String format = "";
		int j = orgLevelNum;
		while (j > 0) {
			format += "0";
			j--;
		}

		DecimalFormat df = new DecimalFormat(format);
		int nRootLevelIndex = 1;
		// 查询根节点组织机构
		RowSet rs = null;
		String rootSql = "select ORGANISEID from jxd7_xt_organise t where t.preorganiseid is null and t.datastatusid=1 ORDER BY SHOWORDER";
		try {
			rs = DbOper.executeQuery(rootSql);

			while (rs.next()) {
				orgId = rs.getString("ORGANISEID");
				if (orgId != null && orgId != "") {
					String strRootLevel = df.format(nRootLevelIndex);
					nRootLevelIndex++;
					if (resetLeavlByOrg(orgId, strRootLevel, orgLevelNum) == false) {
						return false;
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * 根据父节点ID，获取新组织机构的层次码
	 * 
	 * @param orgId
	 * @return
	 */
	public static String setNewOrgLevel(String orgId) {
		RowSet rs = null;
		String strLevel = "";
		boolean bHaveChild = true;
		int maxLevel = 1;
		int orgLevelNum = 2;
		java.util.Properties dbProps = new java.util.Properties();
		dbProps = Global.loadProperties("global.properties");

		String strLevelNum = dbProps.getProperty("OrgLevel.num");
		if (strLevelNum == null || strLevelNum == "") {
			orgLevelNum = 2;
		} else {
			orgLevelNum = Integer.parseInt(strLevelNum);
		}

		String sql = "SELECT max(organiselevel) as maxlevel FROM JXD7_XT_ORGANISE t WHERE t.preorganiseid = '"
				+ orgId + "' order by t.organiselevel";

		try {
			rs = DbOper.executeQuery(sql);

			if (rs.next()) {
				strLevel = rs.getString("maxlevel");
				if (strLevel == null || strLevel.length() == 0) {
					bHaveChild = false;
				} else {
					maxLevel = Integer.parseInt(strLevel.substring(strLevel
							.length()
							- orgLevelNum, strLevel.length()));
					strLevel = strLevel.substring(0, strLevel.length()
							- orgLevelNum);
					maxLevel += 1;
				}
			} else {
				bHaveChild = false;
			}

			if (bHaveChild == false) {
				RowSet rs1 = null;
				sql = "SELECT ORGANISELEVEL FROM JXD7_XT_ORGANISE t WHERE t.ORGANISEID = '"
						+ orgId + "'";
				rs1 = DbOper.executeQuery(sql);
				if (rs1.next())
					strLevel = rs1.getString("ORGANISELEVEL");
				else
					return "false";
			}

			String format = "";
			int i = orgLevelNum;
			while (i > 0) {
				format += "0";
				i--;
			}

			DecimalFormat df = new DecimalFormat(format);
			strLevel = strLevel + df.format(maxLevel);
		} catch (SQLException e) {
			e.printStackTrace();
			return "false";
		}

		return strLevel;
	}

	/**
	 * 通过SQL语句返回多选框的选项
	 * 
	 * @param sql
	 * @return
	 */
	public static String checkJSON(String sql) {
		String checkJSON = "";
		checkJSON = dao.checkJSON(sql);
		return checkJSON;
	}

	/**
	 * 删除hse系统中的通知信息
	 * 
	 * @param sheetid
	 * @return
	 */
	public static boolean deleteHseNotice(String sheetid) {
		return dao.deleteHseNotice(sheetid);
	}

	public static boolean isShowUntreadButton(String flowInstanceId) {
		return dao.hasUntreadCount(flowInstanceId);
	}

	/**
	 * 流程提交--列表用
	 * 
	 * @param sheetid
	 * @param flowobjectid
	 * @param selectUserIds
	 * @return
	 */
	public static Object flowSubmit(String[] flowobjectid,
			String[] selectUserIds) {
		return flowSubmit(flowobjectid, selectUserIds, false);
	}

	/**
	 * 流程提交--列表用
	 * 
	 * @param sheetid
	 * @param flowobjectid
	 * @param selectUserIds
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Object flowSubmit(String[] flowobjectid,
			String[] selectUserIds, boolean isFree) {
		int successCount = 0;
		int falseCount = flowobjectid.length;
		/**
		 * 没有选择用户里不能提交
		 */
		if (selectUserIds == null || selectUserIds.length == 0) {
			return null;
		}
		CommDao dao = new CommDao();

		// 得到nodeInstanceId
		List getFlowInfo = dao.getFlowObjectInfo(flowobjectid);

		for (int i = 0; i < getFlowInfo.size(); i++) {
			Map tempMap = new HashMap();
			tempMap = (Map) getFlowInfo.get(i);
			String flowobjectinstanceid = (String) tempMap
					.get("flowinstanceid");
			int nodeinstanceid = Integer.parseInt((String) tempMap
					.get("nodeinstanceid"));

			tempMap = null;

			HashMap nodemap = new HashMap(); // 指定节点和指定接收人

			try {
				FlowInstance flowInstance = FlowInstance.loadFromDb(flowobjectinstanceid);
				FlowNodeInstance nodeInstance = flowInstance
						.getFlowNodeInstance(nodeinstanceid);
				flowInstance.exec(nodeInstance);
				flowInstance.save();
				Object object = FlowInstance.submit(flowobjectinstanceid,
						nodeinstanceid, null, isFree);
				List<TreeNodeBean> ta = (List<TreeNodeBean>) object;
				if (ta != null && ta.size() > 0) {
					TreeNodeBean tn = ta.get(0);
					nodemap.put(tn.getId(), selectUserIds);
				}
				object = FlowInstance.submit(flowobjectinstanceid,
						nodeinstanceid, nodemap, isFree);
				if (object instanceof Boolean) {
					if (object == Boolean.TRUE) {
						successCount++;
						falseCount--;
					} else {

					}
				} else {
					return object;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (falseCount == 0) {
			return "成功提交" + successCount + "条数据";
		} else {
			return "WARN:成功提交" + successCount + "条数据,未成功提交" + falseCount
					+ "条数据";
		}
	}
	
	/**
	 * 检查问题流程提交--列表用
	 * 
	 * @param sheetid
	 * @param flowobjectid
	 * @param selectUserIds
	 * @return
	 */
	public static Object chkProblemFlowSubmit(String[] flowobjectid,String sheetid, String patternid,
			String[] selectUserIds) {
		int successCount = 0;
		int falseCount = flowobjectid.length;
		/**
		 * 没有选择用户里不能提交
		 */
		if (selectUserIds == null || selectUserIds.length == 0) {
			return null;
		}
		CommDao dao = new CommDao();

		// 得到nodeInstanceId
		List getFlowInfo = dao.getFlowObjectInfo(flowobjectid);

		for (int i = 0; i < getFlowInfo.size(); i++) {
			Map tempMap = new HashMap();
			tempMap = (Map) getFlowInfo.get(i);
			String flowobjectinstanceid = (String) tempMap
					.get("flowinstanceid");
			int nodeinstanceid = Integer.parseInt((String) tempMap
					.get("nodeinstanceid"));

			tempMap = null;

			HashMap nodemap = new HashMap(); // 指定节点和指定接收人

			try {
				FormInstance formInstance = FormInstance.fromDb(sheetid, patternid);
				FlowInstance flowInstance = FlowInstance.loadFromDb(flowobjectinstanceid);
				FlowNodeInstance nodeInstance = flowInstance
						.getFlowNodeInstance(nodeinstanceid);
				flowInstance.exec(nodeInstance);
				flowInstance.save();
				Object object = FlowInstance.submit(flowobjectinstanceid,
						nodeinstanceid, null, false);
				List<TreeNodeBean> ta = (List<TreeNodeBean>) object;
				if (ta != null && ta.size() > 0) {
					TreeNodeBean tn = ta.get(0);
					nodemap.put(tn.getId(), selectUserIds);
				}
				object = FlowInstance.submit(flowobjectinstanceid,
						nodeinstanceid, nodemap, false);
				if (object instanceof Boolean) {
					if (object == Boolean.TRUE) {
						successCount++;
						falseCount--;
					} else {

					}
				} else {
					return object;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (falseCount == 0) {
			return "成功提交" + successCount + "条数据";
		} else {
			return "WARN:成功提交" + successCount + "条数据,未成功提交" + falseCount
					+ "条数据";
		}
	}

	public static Object getFlowNowReciver(String flowinstanceid) {
		try {
			FlowInstance flowInstance = FlowInstance.load(flowinstanceid);
			FlowNodeInstance nodeInstance = flowInstance.getLastNodeInstance();
			return nodeInstance.getOperatorManager().getReceivers();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean HQTZ(WaitDo wd, String sheetId, String receivers) {
		if (receivers == null || receivers.length() == 0) {
			return false;
		}
		for (final String receiver : receivers.split(",")) {
			MyWaitDo.addWaitDo(wd.getOperId(), wd.getOperName(), wd
					.getOperUrl(), wd.getSender(), 0, receiver, "", wd
					.getOperContent(), wd.getOpenType());
		}
		return dao.saveHQJL(sheetId, receivers, wd.getOperId());
	}

	/**
	 * 得到系统的主版本号
	 * 
	 * @return
	 */
	public String getMainVersion() {
		String mainVersion = dao.getMainVersion();
		return mainVersion;
	}

	/**
	 * 得到系统的次版本号
	 * 
	 * @return
	 */
	public String getMinorVersion() {
		String minorVersion = dao.getMinorVersion();
		return minorVersion;
	}

	/**
	 * 得到一个部门下所有的人员
	 * 
	 * @param deptId
	 * @return
	 */
	public static User[] getUserByDeptId(String deptId) {
		return dao.getUserByDeptId(deptId);
	}

	/**
	 * 执行查询返回信息
	 * 
	 * @param sql
	 * @return
	 */
	public static Object executeQuery(String sql) {
		return dao.ececuteQuery(sql);
	}

	/**
	 * 执行查询不返回信息
	 * 
	 * @param sql
	 */
	public static void executeNonQuery(String sql1, String sql2) {
		try {

			DbOper.executeNonQuery(sql1);
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println(sql1);
		System.out.println(sql2);
	}
	
	/**
	 * 执行查询,返回是否存在信息
	 * 
	 * @param sql
	 * @return
	 */
	public static boolean isHaveByExecuteQuery(String sql) {
		RowSet rs = null;
		
		try {
			rs = DbOper.executeQuery(sql);
			
			if (rs.next())
			{
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}

	public static int executeNonQuery(String sql) {
		System.out.println(sql);
		int result = 0;
		try {

			// result= DbOper.executeNonQuery(sql);
		} catch (Exception e) {
			e.getMessage();
		}
		return result;

	}

	/**
	 * 得到一个票模板
	 * 
	 * @param patternId
	 * @return
	 */
	public static Object loadForm(String patternId) {
		try {
			Form form = Form.getPattern(patternId);
			MyForm myForm = new MyForm();
			myForm
					.setWidth(BeanUtil.forceGetProperty(form, "width")
							.toString());
			myForm.setHeight(BeanUtil.forceGetProperty(form, "height")
					.toString());
			myForm.setTitle(form.getName());
			return myForm;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 得到数据库服务器当前系统时间
	 * 
	 * @return
	 */
	public static Date getDBSysDate() {
		String dataStr = dao.getDBSysDate();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date nowDate = new Date();
		try {
			nowDate = dateFormat.parse(dataStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		log.info("得到数据库服务器时间！");
		return nowDate;
	}

	public static boolean saveUserOpenPage(String userId, String menuName,
			String url) throws SQLException {
		return dao.saveUserOpenPage(userId, menuName, url);
	}
	
	//判断是否存在审核记录
	public static boolean isHaveReviewRecord(String sheetid)
	{
		String sql = "select * from [S15].XMGG_FLOW_SHHQ t where DYBDID='"+ sheetid + "'";
		
		RowSet rs;
		try
		{
			rs = DbOper.executeQuery(sql);
			
			if(rs.next())
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return true;
		}
	}
	
	//判断人员所属部门与当前选择部门是否一致
	public static boolean isSameDept(String[] users, String deptid)
	{
		String fieldUser = "'";
		for (int i=0; i<users.length; i++)
		{
			fieldUser = fieldUser + users[i] + "','";
		}
		fieldUser = fieldUser + "'";
		
		String sql = "select * from [S].jxd7_xt_user t where t.userid in (" + fieldUser + ") and t.deptid='" + deptid + "'";
		
		RowSet rs;
		try
		{
			rs = DbOper.executeQuery(sql);
			
			if(rs.next())
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return true;
		}
	}

	/**
	 * 交换两行数据的顺序
	 * 
	 * @param tableName
	 * @param sheetid1
	 * @param sheetid2
	 * @return
	 */
	public static boolean listChangeOrder(String tableName, String sheetid1,
			String sheetid2) {
		TableDao tdao = new TableDao();
		try {
			return tdao.changeOrder(tableName, "SHEETID", sheetid1, sheetid2);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// 删除视图公共操作
	public static int deleteView(String tablename, String data, String sheetid) {
		int a = 0;
		String delSql = "delete from " + tablename + " where " + sheetid + "='"
				+ data + "'";
		try {
			a = DbOper.executeNonQuery(delSql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return a;
	}
	/**
	 * @author Key 2011-07-22
	 * 判断SHEETID是否存在于表TABLENAME中
	 * @param tableName
	 * @param sheetId
	 * @return
	 * @throws SQLException
	 */
	public static boolean isValidSheetId(String tableName, String sheetId) throws SQLException
	{
		ResultSet rs = DbOper.executeQuery("SELECT COUNT(1) FROM "+tableName+" WHERE SHEETID='"+sheetId+"'");
		rs.next();
		int r = rs.getInt(1);
		return r==0?false:true;
	}
	
	//获取组织机构根节点信息
	public static Map getDefautRootOrg()
	{
		String sql = "select t.organiseid,t.organisename from [S].jxd7_xt_organise t where t.preorganiseid is null";
		RowSet rs;
		Map<String, String> orgMap = new HashMap<String, String>();
		
		try
		{
			rs = DbOper.executeQuery(sql);
			
			if(rs.next())
			{
				orgMap.put("id", rs.getString("organiseid"));
				orgMap.put("name", rs.getString("organisename"));
			}
			else
			{
				return null;
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
		
		return orgMap;
	}
}

class Mycomparator implements Comparator {
	public int compare(Object o1, Object o2) {
		TreeNodeBean t1 = (TreeNodeBean) o1;
		TreeNodeBean t2 = (TreeNodeBean) o2;
		String st1 = (String) t1.attachField.get("SHOWORDER") == null ? "999999999"
				: (String) t1.attachField.get("SHOWORDER");
		String st2 = (String) t2.attachField.get("SHOWORDER") == null ? "999999999"
				: (String) t2.attachField.get("SHOWORDER");
		return st1.compareTo(st2);

	}

}
