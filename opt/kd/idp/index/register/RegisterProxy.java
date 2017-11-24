package kd.idp.index.register;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kd.idp.cms.bean.priv.OrgBean;
import kd.idp.cms.bean.priv.UserBean;
import kd.idp.cms.proxy.PrivProxy;
import kd.idp.common.CommonTools;
import kd.idp.index.register.bean.UserElectricCompanyPersonBean;
import kd.idp.index.register.bean.UserPowerPlantPersonBean;
import kd.idp.index.register.dao.RegisterDao;
import kd.idp.index.register.dao.UserRegisterDao;


import com.spring.ServiceManager;
public class RegisterProxy extends PrivProxy {

	/**
	 * 获得所有类型为单位或者机构的组织机构节点。递归获取所有节点。
	 * @param nodeId  filter 是否过滤掉部门
	 * @return
	 */
	
	
	
	public ArrayList<Map<String,Object>> getOrgInfoByNode(String nodeId,String filter){
		if(filter==null)
			filter="false";
		ArrayList<Map<String,Object>> orgList=new ArrayList<Map<String,Object>> ();
		RegisterDao registerDao = ServiceManager.getUserRegisterOrgDaoBean();
		List<OrgBean> list=registerDao.getOrgInfo(nodeId);
		if(list.size()>0){
		Map<String,Object> map=this.orgNodeConveter(list.get(0));
		map.put("children", getOrgTree(list.get(0).getOrgId(),filter));
		orgList.add(map);
		}
		return orgList;
		
	}
	
	
	/**
	 * 获得一个节点下的所有类型为单位和组织机构子节点，返回列表
	 * @param nodeId
	 * @return
	 */
	public  ArrayList<Map<String,Object>> getOrgTree(String nodeId,String filter){
		ArrayList<Map<String,Object>> orgList=new ArrayList<Map<String,Object>> ();
		RegisterDao registerDao = ServiceManager.getUserRegisterOrgDaoBean();
		List<OrgBean> list=registerDao.getChildOrgInfo(nodeId);
		if(list.size()>0){
			
			for(OrgBean orgBean:list){
//				if(filter.equals("true")&&orgBean.getType().indexOf("部门")>-1)
//					continue;
				Map<String,Object>  map=this.orgNodeConveter(orgBean);
				ArrayList<Map<String,Object>> temp=getOrgTree(orgBean.getOrgId(),filter);
				if(temp.size()>0)
				{
				map.put("children",temp);
				map.put("state", "closed");
				}
				orgList.add(map);
			}
			
		}
		return orgList;
	}
	
	
	public Map<String,Object> orgNodeConveter(OrgBean orgBean){
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("id", orgBean.getOrgId());
		map.put("text", orgBean.getOrgName());
		map.put("type", orgBean.getType());
		//map.put("state", "closed");
		return map;
	}
	
	
	/**
	 * 保持注册电力公司人员信息
	 * @param nodeId
	 * @return
	 */
	
	public void setElectricCompanyPerson(UserElectricCompanyPersonBean user){
		UserRegisterDao userRegisterDao = ServiceManager.getUserRegisterDaoBean();
		userRegisterDao.setElectricCompanyPerson(user);
		
	}
	
	/**
	 * 保持注册发电厂人员信息
	 * @param user
	 */
	public void setPowerPlantPerson(UserPowerPlantPersonBean user){
		UserRegisterDao userRegisterDao = ServiceManager.getUserRegisterDaoBean();
		userRegisterDao.setPowerPlantPerson(user);
	}
	
	
	/**
	 * 判断用户是否存在
	 * @param user
	 */
	public boolean isUserExsit(String userName){
		UserRegisterDao userRegisterDao = ServiceManager.getUserRegisterDaoBean();
		return userRegisterDao.isUserExsit(userName);
	}
	public StringBuffer toJson(ArrayList<Map<String,Object>> list){
		StringBuffer sb=new StringBuffer();
		if(list!=null&&list.size()>0){
			sb.append("\n\t");
			sb.append("[");
	    for(int k=0;k<list.size();k++){
	    	Map<String,Object> map=list.get(k);
	    	sb.append("{");
			sb.append("\n\"id\":"+"\""+map.get("id")+"\",");
			sb.append("\n\"text\":"+"\""+map.get("text")+"\",");
			sb.append("\n\"attributes\":"+"{"+"\"type\":\""+map.get("type")+"\"}");
			if(map.get("state")!=null)
		    sb.append(",\n\"state\":"+"\""+map.get("state")+"\"");
			ArrayList c=(ArrayList)map.get("children");
			if(c!=null&&c.size()>0){
				sb.append(",");
			sb.append("\n\"children\":"+toJson((ArrayList)map.get("children"))+"\n");
			}
			//sb.append("\n");
			if(k==list.size()-1)
				sb.append("}");
			else
			sb.append("},");
	    }
	    sb.append("]");
		}
		return sb;
	}
	
	public UserBean getUserDetail(String userId){
		UserRegisterDao userRegisterDao = ServiceManager.getUserRegisterDaoBean();
		UserBean user = userRegisterDao.getUserDetail(userId);
		return user;
	}
	public void generateOrgJson(){
		try{
			//ArrayList<Map<String,Object>> map=new RegisterProxy().getOrgInfoByNode("ORG_1310708226781_9055","true");//电力公司人员
			ArrayList<Map<String,String>> config=new ArrayList<Map<String,String>>();
			Map<String,String> elec=new HashMap<String,String>();
			elec.put("root","ORG_1310708226781_9055");
			String path=CommonTools.getProjectRealPath()+"/template/common/register/";
			elec.put("filepath",path+"org.json");
			config.add(elec);
			Map<String,String> power=new HashMap<String,String>();
			power.put("root","ORG_1354874084785_8888");
			power.put("filepath",path+"orgPoer.json");
			config.add(power);
			
			for(Map<String,String> conf:config){
				
				ArrayList<Map<String,Object>> map=new RegisterProxy().getOrgInfoByNode(conf.get("root"),"true");//电力公司人员

				StringBuffer sb=new RegisterProxy().toJson(map);
				
				File file=new File(conf.get("filepath"));
		        if(!file.exists())
		            file.createNewFile();
		        FileOutputStream out=new FileOutputStream(file,false);      
		        
		           
		           // sb.append("这是第"+i+"行:前面介绍的各种方法都不关用,为什么总是奇怪的问题 ");
		            out.write(sb.toString().getBytes("utf-8"));
		               
		        out.close();
				//System.out.println(sb.toString());
				
				
				
			}
			
			}
			catch(Exception e){
				e.printStackTrace();
			}
			//System.out.println(map.toString());
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		RegisterProxy test = new RegisterProxy();
//		UserBean user = test.getUserDetail("USER_1367487564932_3510");
//		System.out.println(user.getCompanyType());
//		System.out.println(((UserElectricCompanyPersonBean)user).getAttrElectricCompanyPerson().getUserBirthDate());
		//System.out.println(null);
		try{
		//ArrayList<Map<String,Object>> map=new RegisterProxy().getOrgInfoByNode("ORG_1310708226781_9055","true");//电力公司人员
		ArrayList<Map<String,String>> config=new ArrayList<Map<String,String>>();
		Map<String,String> elec=new HashMap<String,String>();
		elec.put("root","ORG_1310708226781_9055");
		elec.put("filepath","G:\\org.json");
		config.add(elec);
		Map<String,String> power=new HashMap<String,String>();
		power.put("root","ORG_1354874084785_8888");
		power.put("filepath","G:\\orgPoer.json");
		config.add(power);
		
		for(Map<String,String> conf:config){
			
			ArrayList<Map<String,Object>> map=new RegisterProxy().getOrgInfoByNode(conf.get("root"),"true");//电力公司人员

			StringBuffer sb=new RegisterProxy().toJson(map);
			
			File file=new File(conf.get("filepath"));
	        if(!file.exists())
	            file.createNewFile();
	        FileOutputStream out=new FileOutputStream(file,false);      
	        
	           
	           // sb.append("这是第"+i+"行:前面介绍的各种方法都不关用,为什么总是奇怪的问题 ");
	            out.write(sb.toString().getBytes("utf-8"));
	               
	        out.close();
			System.out.println(sb.toString());
			
			
			
		}
		
		}
		catch(Exception e){
			e.printStackTrace();
		}
		//System.out.println(map.toString());
	 
	}

}
