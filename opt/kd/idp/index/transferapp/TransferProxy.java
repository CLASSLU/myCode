package kd.idp.index.transferapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import kd.idp.cms.bean.portal.SourceBean;
import kd.idp.cms.bean.priv.PrivAttrBean;
import kd.idp.cms.bean.priv.PrivBean;
import kd.idp.cms.bean.priv.RoleBean;
import kd.idp.cms.bean.priv.UserBean;
import kd.idp.cms.dao.PrivDao;
import kd.idp.cms.dao.RoleDao;
import kd.idp.cms.dao.UserDao;
import kd.idp.cms.mapper.portal.SourceRowMapper;
import kd.idp.common.consts.TableConst;

import com.spring.ServiceManager;
import com.spring.dbservice.DBTemplate;

public class TransferProxy {
	
	public void addCommonAtrr(){
		PrivDao privdao = ServiceManager.getPrivDao();
		RoleDao roledao = ServiceManager.getRoleDao();
		UserDao userdao = ServiceManager.getUserDao();
		List<PrivBean> privlist = privdao.getChildrenPrivInfo("app_root",false,null);
		
		
		for(PrivBean priv:privlist){
			if(priv.getPrivLevel()==2){
				
				
				
				
				PrivAttrBean priattvURL=new PrivAttrBean();
				priattvURL.setPrivId(priv.getPrivId());
				priattvURL.setPrivAttrType("属性");
				priattvURL.setPrivAttrName("URL");
				priattvURL.setPrivAttrValue("/psoms/template/common/jsp/Wrapper.jsp");
				priattvURL.setAttrOrder(10);
				privdao.addNewPrivAttr(priattvURL);
				
				
				PrivAttrBean priattvICON=new PrivAttrBean();
				priattvICON.setPrivId(priv.getPrivId());
				priattvICON.setPrivAttrType("属性");
				priattvICON.setPrivAttrName("ICON");
				priattvICON.setPrivAttrValue("image/web_admin/icon/jkrz.png");
				priattvICON.setAttrOrder(11);
				privdao.addNewPrivAttr(priattvICON);
				
				
				PrivAttrBean priattvTARGET=new PrivAttrBean();
				priattvTARGET.setPrivId(priv.getPrivId());
				priattvTARGET.setPrivAttrType("属性");
				priattvTARGET.setPrivAttrName("TARGET");
				priattvTARGET.setPrivAttrValue("_self");
				priattvTARGET.setAttrOrder(12);
				privdao.addNewPrivAttr(priattvTARGET);
//				
//				List<PrivAttrBean> arrlist= privdao.getAttrsFromPriv(priv.getPrivId());
//				for(PrivAttrBean arr:arrlist){
//					List<RoleBean>  roleList=roledao.getRolesRelPrivAttr(arr.getPrivAttrId());
//					if(roleList.size()>0){
//						privdao.saveRoleRelPrivAttr(priattvURL, roleList);
//						privdao.saveRoleRelPrivAttr(priattvICON, roleList);
//						privdao.saveRoleRelPrivAttr(priattvTARGET, roleList);
//						break;
//					}
//			
//				}
//				
//				for(PrivAttrBean arr:arrlist){
//					
//					List<UserBean> userlist=userdao.getUsersRelPrivAttr(arr.getPrivAttrId());
//					if(userlist.size()>0){
//						privdao.saveUserRelPrivAttr(priattvURL, userlist);
//						privdao.saveUserRelPrivAttr(priattvICON, userlist);
//						privdao.saveUserRelPrivAttr(priattvTARGET, userlist);
//						break;
//					}
//					
//				}
				
				
				
				}
			}
		}
	public boolean isContainsRole(RoleBean role,List<RoleBean> roleslist){
		boolean flag=false;
		for(RoleBean r:roleslist){
			if(r.getRoleId().equals(role.getRoleId())){
				flag=true;
			}
		}
		return flag;
	}
	
	public boolean isContainsUser(UserBean user,List<UserBean> userslist){
		boolean flag=false;
		for(UserBean u:userslist){
			if(u.getUserId().equals(user.getUserId())){
				flag=true;
			}
		}
		return flag;
	}
	public void addPriv(){
		PrivDao privdao = ServiceManager.getPrivDao();
		RoleDao roledao = ServiceManager.getRoleDao();
		UserDao userdao = ServiceManager.getUserDao();
		List<PrivBean> privlist = privdao.getChildrenPrivInfo("app_root",false,null);
		
		
		for(PrivBean priv:privlist){
			if(priv.getPrivLevel()==2){
				
				List<RoleBean>  roleList=new ArrayList<RoleBean>();
				List<UserBean> userlist=new ArrayList<UserBean>();
				List<PrivAttrBean> arrlist= privdao.getAttrsFromPriv(priv.getPrivId());
				
				PrivAttrBean priattvURL=new PrivAttrBean();
				PrivAttrBean priattvICON=new PrivAttrBean();
				PrivAttrBean priattvTARGET=new PrivAttrBean();
				
				for(PrivAttrBean arr:arrlist){
					if(arr.getPrivAttrName().equals("URL")){
						priattvURL=arr;
					}
					if(arr.getPrivAttrName().equals("ICON")){
						priattvICON=arr;
					}
					if(arr.getPrivAttrName().equals("TARGET")){
						priattvTARGET=arr;
					}
				}
				for(PrivAttrBean arr:arrlist){
				
					
					List<RoleBean>  rolesList=roledao.getRolesRelPrivAttr(arr.getPrivAttrId());
					
					for(RoleBean roleBean:rolesList){
						
						if(!this.isContainsRole(roleBean, roleList)){
							roleList.add(roleBean);
						}
					}
					
					
					System.out.print("角色权限");
						
					
			
				}
				privdao.saveRoleRelPrivAttr(priattvURL, roleList);
				privdao.saveRoleRelPrivAttr(priattvICON, roleList);
				privdao.saveRoleRelPrivAttr(priattvTARGET, roleList);
				
				for(PrivAttrBean arr:arrlist){
					
					List<UserBean> userslist=userdao.getUsersRelPrivAttr(arr.getPrivAttrId());
	                    for(UserBean u:userslist){
						
						if(!this.isContainsUser(u, userlist)){
							userlist.add(u);
						}
					}
					
	                    System.out.print("用户权限");	
				}
				
				privdao.saveUserRelPrivAttr(priattvURL, userlist);
				privdao.saveUserRelPrivAttr(priattvICON, userlist);
				privdao.saveUserRelPrivAttr(priattvTARGET, userlist);
			
			
				
				}
			}
	}
	
	public void taransferFiles(){
		PrivDao privdao = ServiceManager.getPrivDao();
		RoleDao roledao = ServiceManager.getRoleDao();
		UserDao userdao = ServiceManager.getUserDao();
		List<PrivBean> privlist = privdao.getChildrenPrivInfo("PRIV_1376292423683_9406",false,null);
		int count=0;
		for(PrivBean priv:privlist){
			List<SourceBean> sourceList=getOldSourcListByPriv(priv.getPrivId());
			count=count+sourceList.size();
			
			System.out.println("当前正在迁移:  "+priv.getPrivName());
			
			addSorceList(sourceList);
			System.out.println("完成迁移 权限ID:"+priv.getPrivId()+"...权限名称:"+priv.getPrivName()+"...文档资料数量:"+sourceList.size());
		}
		System.out.print("总文件数量:"+count);
	}
	public void addSorceList(List<SourceBean> list ){
		for(SourceBean source:list){
			addSource(source);
		}
		
	}
	public void addSource(SourceBean source){
		boolean flag =transferFile(source);
		
		String sql = "INSERT INTO "
			+ TableConst.TABLE_SOURCE
			+ " (内容ID,文档名称,文档类型,文件名称,文档说明,文件大小,存储目录,关联权限,创建人ID,创建人,创建时间,审核人ID,审核人,审核时间,状态) "
			+ " VALUES('" + source.getSourceId() + "','"
			+ source.getSourceName() + "','" + source.getSourceType()
			+ "','" + source.getFileName() + "','"
			+ source.getSourceDetail() + "'," + source.getFileSize() + ",'"
			+ source.getStorePath() + "','" + source.getRelPriv() + "','"
			+ source.getCreaterId() + "','" + source.getCreater()+"','"+source.getCreateTime()+"','"+source.getAssessorId()+"','"+source.getAssessor()+"','"+source.getAssessorTime()+"','"+source.getStatus()+"')";
		if(flag){
		DBTemplate.getInstance().updateSql(sql);
		}
		else{
			System.out.println("没有找到该文件:  "+source.getFileName());
		}
	
	}
	public boolean transferFile(SourceBean source){
		boolean flag=false;
		 List resultList = new ArrayList();  
		 String baseDIR = "D:/华中/file/华中电力调控分中心/"; 
		 String target="D:/华中/文档资料/";
		 String newName=source.getSourceName()+source.getSourceType();
		
		 source.setSourceDetail(source.getSourceName());
	        FileSearcher.findFiles(baseDIR, source.getFileName(), resultList);   
	       if(resultList.size()>0){
	    	  flag=true;
	    	  source.setFileSize(getFileSize(resultList.get(0).toString()));
	    	   moveFile(target+File.separator+source.getStorePath(),resultList.get(0).toString(),newName);
	    	  
	       }else{
	    	   flag=false;
	       }
	       source.setFileName(newName);
	       return flag;
	        
	}
	public void moveFile(String targetDir,String sourceFile,String targetFile){
		// boolean s=false;
		if(!(new File(targetDir).isDirectory()))
		{
		new File(targetDir).mkdir();
		}
		try{
		File file = new File(sourceFile);            
        File newFile = new File(targetDir+ File.separator+targetFile);  
        
        FileInputStream input=new FileInputStream(file);
        FileOutputStream output=new FileOutputStream(newFile);
        int in=input.read();
        while(in!=-1){
        	output.write(in);
        	in=input.read();
        	}
        output.close();
        input.close();
        
       // s= file.renameTo(newFile);  
		}catch(Exception e){
			
			e.printStackTrace();
		}
    
		
	}
	public List<SourceBean> getOldSourcListByPriv(String privId){
		List<SourceBean> sourceList=new ArrayList<SourceBean>();
		String sysId=privId.replace("PRIV", "SYSTEM_OBJ");
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT 内容ID,文档名称,文档类型,内容ID||文档类型 文件名称,文档说明,0 文件大小, ");
		sql.append("'");
		sql.append(privId);
		sql.append("' ");
		sql.append("存储目录,");
		sql.append("'");
		sql.append(privId);
		sql.append("' ");
		sql.append("关联权限, ");
		sql.append("'' 创建人ID,创建人,创建日期 创建时间,'' 审核人ID,创建人 审核人,审核时间 审核时间,'1' 状态 FROM IDP_WEB.门户_内容_文档资料 where 类型='");
		sql.append(sysId);
		sql.append("'");
		try{
		sourceList=DBTemplate.getInstance().getResultRowMapperList(sql.toString(),new SourceRowMapper());
	}
		catch(Exception e){
			
		}
		
		return sourceList;
	}

	public Double getFileSize(String filename){
		Double size=0.0;
		File file=new File(filename);
		FileInputStream fis=null;
		try{
		 fis=new FileInputStream(file);
		size=Double.valueOf(fis.available());
		fis.close();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
			
		}
		return size;
		
		
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//new TransferProxy().addPriv();
		new TransferProxy().taransferFiles();
	
		}
		

	}


