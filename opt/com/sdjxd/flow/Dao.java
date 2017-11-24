package com.sdjxd.flow;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.RowSet;

import net.sf.json.JSONArray;

import com.sdjxd.pms.platform.data.DbOper;
import com.sdjxd.pms.platform.organize.User;
import com.sdjxd.pms.platform.workflow.model.FlowOperatorBean;
import com.sdjxd.pms.platform.workflow.service.FlowOperator;
import com.sdjxd.util.ArrayUtil;

public class Dao {
	Sql sql = new Sql();

	/**
	 * 保存提交日志
	 * 
	 * @param formInstanceId
	 * @param f
	 * @param t
	 * @return
	 */
	public boolean saveStartLog(String formInstanceId, User f, FlowOperator t,
			boolean isUntreadNode) {
		if (t == null) {
			return false;
		}
		try {
			return DbOper.executeNonQuery(sql.saveStartLog(formInstanceId, f,
					t, isUntreadNode)) == -1 ? false : true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 保存提交日志
	 * 
	 * @param formInstanceId
	 * @param f
	 * @param t
	 * @return
	 */
	public boolean saveSendDownDept(String formInstanceId, FlowOperator t) {
		if (t == null) {
			return false;
		}
		try {
			return DbOper.executeNonQuery(sql.saveSendDownDept(formInstanceId, t)) == -1 ? false : true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 得到当前结点是否退回过
	 * 
	 * @param flowInstanceId
	 * @param nodeId
	 * @return
	 */
	public boolean isUntreadNode(String flowInstanceId, String nodeId) {
		RowSet rs = null;
		try {
			rs = DbOper.executeQuery(sql.isUntreadNode(flowInstanceId, nodeId));
			if (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			rs = null;
		}
		return false;
	}

	/**
	 * 得到参与者列表中的角色信息
	 * 
	 * @param flowid
	 * @param flownodeid
	 * @return
	 */
	public List<String> getActorsList(String flowid, String flownodeid) {
		List<String> resultList = new ArrayList<String>();
		RowSet rs = null;
		try {
			rs = DbOper.executeQuery(sql.getActorsList(flowid, flownodeid));
			if (rs.next()) {
				JSONArray jsonarray = JSONArray.fromObject(rs
						.getString("ACTORDATA"));
				// System.out.println(jsonarray);
				JSONArray jso = jsonarray;
				if (jso.size() >= 3) {
					JSONArray actorsData = jso.getJSONArray(2);
					JSONArray actorData;
					for (int i = 0; i < actorsData.size(); i++) {
						actorData = actorsData.getJSONArray(i);
						System.out.println(actorData.getString(0) + ","
								+ actorData.getString(1));
						if (actorData.getString(1).equals("1")) {
							resultList.add(actorData.getString(0));
						}
						// this.actors.put(((JSONObject)jsa.get(i)).getString("id"),((JSONObject)jsa.get(i)).getString("type"));
					}
					return resultList;
				}
			}
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			rs = null;
		}
	}

	/**
	 * 保存参与者列表中的角色信息
	 * 
	 * @param flowid
	 * @param flownodeid
	 * @return
	 */
	public List<String> saveActorsList(String flowid, String flownodeid,
			String[] roles) {
		List<String> resultList = new ArrayList<String>();
		RowSet rs = null;
		try {
			rs = DbOper.executeQuery(sql.getActorsList(flowid, flownodeid));
			if (rs.next()) {
				JSONArray jsonarray = JSONArray.fromObject(rs
						.getString("ACTORDATA"));
				// System.out.println(jsonarray);
				JSONArray jso = jsonarray;
				if (jso.size() >= 3) {
					JSONArray actorsData = jso.getJSONArray(2);
					JSONArray actorData;
					for (int i = 0; i < actorsData.size(); i++) {
						actorData = actorsData.getJSONArray(i);
						System.out.println(actorData.getString(0) + ","
								+ actorData.getString(1));
						if (actorData.getString(1).equals("1")) {
							resultList.add(actorData.getString(0));
						}
						// this.actors.put(((JSONObject)jsa.get(i)).getString("id"),((JSONObject)jsa.get(i)).getString("type"));
					}
					return resultList;
				}
			}
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			rs = null;
		}
	}

	/**
	 * 添加新的流程参与者
	 * 
	 * @param flowid
	 *            流程ID
	 * @param flownodeid
	 *            流程结点ID
	 * @param roles
	 *            新角色数组
	 * @return
	 */
	public boolean addActors(String flowid, String flownodeid, String[] roles) {
		List<String> newRolwList = new ArrayList<String>();
		RowSet rs = null;
		try {
			rs = DbOper.executeQuery(sql.getActorsList(flowid, flownodeid));
			if (rs.next()) {
				JSONArray jsonarray = JSONArray.fromObject(rs
						.getString("ACTORDATA"));
				JSONArray jso = jsonarray;
				if (jso.size() >= 3) {
					JSONArray actorsData = jso.getJSONArray(2);
					JSONArray actorData;
					// 如果已有的角色中不存在则添加新的角色到流程参与者中
					for (int i = 0; i < actorsData.size(); i++) {
						actorData = actorsData.getJSONArray(i);
						if (ArrayUtil.isHave(roles, actorData.getString(0))) {
							newRolwList.add(actorData.getString(0));
						}
					}
					for (String roleid : roles) {
						JSONArray tempactorData = JSONArray.fromObject("[\""
								+ roleid + "\",\"1\"]");
						actorsData.add(tempactorData);
					}
					return DbOper.executeNonQuery(sql.updateActors(flowid,
							flownodeid, jsonarray.toString())) == -1 ? false
							: true;
				}
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			rs = null;
		}
	}

	/**
	 * 删除新的流程参与者
	 * 
	 * @param flowid
	 *            流程ID
	 * @param flownodeid
	 *            流程结点ID
	 * @param roles
	 *            新角色数组
	 * @return
	 */
	public boolean delActors(String flowid, String flownodeid, String[] roles) {
		RowSet rs = null;
		try {
			rs = DbOper.executeQuery(sql.getActorsList(flowid, flownodeid));
			if (rs.next()) {
				JSONArray jsonarray = JSONArray.fromObject(rs
						.getString("ACTORDATA"));
				JSONArray jso = jsonarray;
				if (jso.size() >= 3) {
					JSONArray actorsData = jso.getJSONArray(2);
					JSONArray actorData;
					// 查找并删除指定的流程参与都角色
					for (int i = 0; i < actorsData.size(); i++) {
						actorData = actorsData.getJSONArray(i);
						if (ArrayUtil.isHave(roles, actorData.getString(0))) {
							actorsData.remove(actorData);
							i--;
						}
					}
					return DbOper.executeNonQuery(sql.updateActors(flowid,
							flownodeid, jsonarray.toString())) == -1 ? false
							: true;
				}
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			rs = null;
		}
	}

	// 判断当前登录人是否有指定流程结点的权限
	public boolean userInActors(String flowid, String flownodeid) {
		List<String> resultList = new ArrayList<String>();
		RowSet rs = null;
		try {
			rs = DbOper.executeQuery(sql.getActorsList(flowid, flownodeid));
			if (rs.next()) {
				JSONArray jsonarray = JSONArray.fromObject(rs
						.getString("ACTORDATA"));
				// System.out.println(jsonarray);
				JSONArray jso = jsonarray;
				if (jso.size() >= 3) {
					JSONArray actorsData = jso.getJSONArray(2);
					JSONArray actorData;
					for (int i = 0; i < actorsData.size(); i++) {
						actorData = actorsData.getJSONArray(i);
						System.out.println(actorData.getString(0) + ","
								+ actorData.getString(1));
						if (actorData.getString(1).equals("1")) {
							resultList.add(actorData.getString(0));
						}
						// this.actors.put(((JSONObject)jsa.get(i)).getString("id"),((JSONObject)jsa.get(i)).getString("type"));
					}
				}
			}

			StringBuffer sb = new StringBuffer(
					"select * from jxd7_xt_userrole ur where ur.userid = '")
					.append(User.getCurrentUser().getId()).append(
							"' and ur.roleid in ('");
			for (String roleId : resultList) {
				sb.append(roleId).append("','");
			}
			sb.append("附加')");
			rs = DbOper.executeQuery(sb.toString());
			if (rs.next()) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			rs = null;
		}
	}

	/**
	 * 是否为会签流程
	 * 
	 * @param flowid
	 * @return
	 */
	public boolean isSignFlow(String flowid) {
		RowSet rs = null;
		try {
			rs = DbOper.executeQuery(sql.getIsSignFlow(flowid));
			if (rs.next()) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			rs = null;
			return false;
		}
	}
}
