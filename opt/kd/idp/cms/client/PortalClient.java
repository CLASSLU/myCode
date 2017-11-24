package kd.idp.cms.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import com.spring.dbservice.DBTemplate;


import kd.idp.cms.bean.portal.NewsBean;
import kd.idp.cms.bean.portal.SourceBean;
import kd.idp.cms.bean.priv.PrivBean;
import kd.idp.cms.mapper.portal.SourceRowMapper;
import kd.idp.common.config.WebConfigUtil;
import kd.idp.common.consts.TableConst;

public class PortalClient {

	
	
	/**
	 * 获得本地新闻HTML内容
	 * 
	 * @param news
	 * @return
	 * @throws FileNotFoundException 
	 */
	public String getNewsContent(NewsBean news) throws FileNotFoundException{
		StringBuffer sb = new StringBuffer();
		try {
			if (news != null) {
				String path = WebConfigUtil.getNewsFilePath() + "/"
						+ news.getStorePath();
				File f = new File(path, news.getNewsId() + ".html");
				BufferedReader br = new BufferedReader(new FileReader(f));
				String str = "";
				while ((str = br.readLine()) != null) {
					sb.append(str);
				}
			}
		}catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new FileNotFoundException();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	
	/**
	 * 获得远程新闻内容
	 * @param queryUrl
	 * @return
	 */
	public String getRemoteNewsContent(String queryUrl){
		StringBuffer sb = new StringBuffer();
		BufferedReader br = null;
		try {
			URL url = new URL(queryUrl);
			br = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));
			String str = "";
			while((str = br.readLine()) != null){
				sb.append(str);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(br != null){
				try {
					br.close();
				} catch (Exception e) {
					
				}
			}
		}
		return sb.toString();
	}
	
	
	/**
	 * 通过权限列表 获得 文档资料
	 * @param privlist
	 * @return
	 */
	public List<SourceBean> getResourceListFromPrivs(List<PrivBean> privlist){
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT * FROM "+TableConst.TABLE_SOURCE+" WHERE 关联权限 IN ( ");
		for(int i=0;i<privlist.size();i++){
			sb.append("'"+privlist.get(i).getPrivId()+"'");
			if(i < privlist.size() - 1){
				sb.append(",");
			}
		}
		sb.append(" ) AND 状态=1 ORDER BY 审核时间 desc ");
		return DBTemplate.getInstance().getResultRowMapperList(sb.toString(), new SourceRowMapper());
	}
	
}
