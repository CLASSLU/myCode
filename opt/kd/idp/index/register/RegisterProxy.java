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
	 * �����������Ϊ��λ���߻�������֯�����ڵ㡣�ݹ��ȡ���нڵ㡣
	 * @param nodeId  filter �Ƿ���˵�����
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
	 * ���һ���ڵ��µ���������Ϊ��λ����֯�����ӽڵ㣬�����б�
	 * @param nodeId
	 * @return
	 */
	public  ArrayList<Map<String,Object>> getOrgTree(String nodeId,String filter){
		ArrayList<Map<String,Object>> orgList=new ArrayList<Map<String,Object>> ();
		RegisterDao registerDao = ServiceManager.getUserRegisterOrgDaoBean();
		List<OrgBean> list=registerDao.getChildOrgInfo(nodeId);
		if(list.size()>0){
			
			for(OrgBean orgBean:list){
//				if(filter.equals("true")&&orgBean.getType().indexOf("����")>-1)
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
	 * ����ע�������˾��Ա��Ϣ
	 * @param nodeId
	 * @return
	 */
	
	public void setElectricCompanyPerson(UserElectricCompanyPersonBean user){
		UserRegisterDao userRegisterDao = ServiceManager.getUserRegisterDaoBean();
		userRegisterDao.setElectricCompanyPerson(user);
		
	}
	
	/**
	 * ����ע�ᷢ�糧��Ա��Ϣ
	 * @param user
	 */
	public void setPowerPlantPerson(UserPowerPlantPersonBean user){
		UserRegisterDao userRegisterDao = ServiceManager.getUserRegisterDaoBean();
		userRegisterDao.setPowerPlantPerson(user);
	}
	
	
	/**
	 * �ж��û��Ƿ����
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
			//ArrayList<Map<String,Object>> map=new RegisterProxy().getOrgInfoByNode("ORG_1310708226781_9055","true");//������˾��Ա
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
				
				ArrayList<Map<String,Object>> map=new RegisterProxy().getOrgInfoByNode(conf.get("root"),"true");//������˾��Ա

				StringBuffer sb=new RegisterProxy().toJson(map);
				
				File file=new File(conf.get("filepath"));
		        if(!file.exists())
		            file.createNewFile();
		        FileOutputStream out=new FileOutputStream(file,false);      
		        
		           
		           // sb.append("���ǵ�"+i+"��:ǰ����ܵĸ��ַ�����������,Ϊʲô������ֵ����� ");
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
		//ArrayList<Map<String,Object>> map=new RegisterProxy().getOrgInfoByNode("ORG_1310708226781_9055","true");//������˾��Ա
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
			
			ArrayList<Map<String,Object>> map=new RegisterProxy().getOrgInfoByNode(conf.get("root"),"true");//������˾��Ա

			StringBuffer sb=new RegisterProxy().toJson(map);
			
			File file=new File(conf.get("filepath"));
	        if(!file.exists())
	            file.createNewFile();
	        FileOutputStream out=new FileOutputStream(file,false);      
	        
	           
	           // sb.append("���ǵ�"+i+"��:ǰ����ܵĸ��ַ�����������,Ϊʲô������ֵ����� ");
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
