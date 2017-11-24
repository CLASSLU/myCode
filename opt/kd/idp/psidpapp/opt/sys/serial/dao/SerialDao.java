package kd.idp.psidpapp.opt.sys.serial.dao;


import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.spring.dbservice.DBTemplate;

import kd.idp.psidpapp.opt.sys.dao.BaseDao;
import kd.idp.psidpapp.opt.sys.serial.bean.PageNumberPattern;

public class SerialDao extends BaseDao{
	
	/**
	 * @param patternId		编号模板ID
	 * @return				返回对应编号模板信息
	 * @throws Exception
	 */
	public PageNumberPattern findByPatternId(String patternId) throws Exception{
		PageNumberPattern pageNumberPattern = null;
		StringBuilder sb = new StringBuilder("");
		try {
			sb.setLength(0);
			sb.append("SELECT * FROM OPT.SYS_PAGENUMBER_PATTERN WHERE ID='")
			  .append(patternId).append("'");
			SqlRowSet rs = DBTemplate.getInstance().queryForRowSet(sb.toString());
			if (rs.next()){
				pageNumberPattern = new PageNumberPattern();
				pageNumberPattern.setPatternId(rs.getString("ID"));
				pageNumberPattern.setPatternName(rs.getString("NAME"));
				pageNumberPattern.setSerialCycle(rs.getInt("SERIALCYCLE"));
				pageNumberPattern.setSerialLength(rs.getInt("SERIALLENGTH"));
				pageNumberPattern.setStartNum(rs.getInt("STARTNUM"));
				pageNumberPattern.setPageContent(rs.getString("PAGECONTENT"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return pageNumberPattern;
	}
	
	/**
	 * @param patternId		编号模板ID
	 * @param year			当前年份
	 * @param month			当前月份
	 * @param day			当前日
	 * @return				获取编号目前自增数
	 * @throws Exception
	 */
	public int getSerialNum(String patternId, int year, int month, int day)throws Exception {
	    int serialNum = -1;
	    StringBuilder sb = new StringBuilder();
	    try {
	    	sb.setLength(0);
	    	sb.append("SELECT SERIALNUM FROM OPT.SYS_PAGENUMBER_INSTANCE WHERE PAGEPATTERNID='")
	    	  .append(patternId).append("' AND CURRENTYEAR='")
	    	  .append(year).append("' AND CURRENTMONTH='")
	    	  .append(month).append("' AND CURRENTDAY='")
	    	  .append(day).append("'");
		    SqlRowSet rs = DBTemplate.getInstance().queryForRowSet(sb.toString());
		    if (rs.next()){
		    	serialNum = rs.getInt("SERIALNUM");
		    }
	    }catch (Exception e){
	    	e.printStackTrace();
	    	throw e;
	    }
	    return serialNum;
	}
	
	/**
	 * @param patternId		编号模板ID
	 * @param year			当前年份
	 * @param month			当前月份
	 * @param day			当前日
	 * @param serialNum		更新序列号值
	 * @return				重置serialNum为-1
	 * @throws Exception
	 */
	public int updateSerialNum(String patternId, int year, int month, int day, int serialNum)throws Exception {
		StringBuilder sb = new StringBuilder();
	    try {
	    	sb.setLength(0);
	    	sb.append("UPDATE OPT.SYS_PAGENUMBER_INSTANCE SET SERIALNUM='")
			  .append(serialNum).append("' WHERE PAGEPATTERNID='")
			  .append(patternId).append("' AND CURRENTYEAR='")
			  .append(year).append("' AND CURRENTMONTH='")
			  .append(month).append("' AND CURRENTDAY='")
			  .append(day).append("'");
	    	DBTemplate.getInstance().executeSql(sb.toString());
	    }catch (Exception e){
	    	e.printStackTrace();
	    	throw e;
	    }
	    return -1;
	}
	
	 /**
	* @param patternId		编号模板ID
	 * @param year			当前年份
	 * @param month			当前月份
	 * @param day			当前日
	 * @param serialNum		序列号值
	 * @return
	 * @throws Exception
	 */
	public int createSerial(String patternId, int year, int month, int day, int startNum)throws Exception {
		StringBuilder sb = new StringBuilder();
	    try {
	    	sb.setLength(0);
	    	sb.append("INSERT INTO OPT.SYS_PAGENUMBER_INSTANCE(PAGEPATTERNID,CURRENTYEAR,CURRENTMONTH,CURRENTDAY,SERIALNUM)").append(" VALUES('")
			  .append(patternId).append("','")
			  .append(year).append("','")
			  .append(month).append("','")
			  .append(day).append("','")
			  .append(startNum).append("')");
	    	DBTemplate.getInstance().executeSql(sb.toString());
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	throw e;
	    }
	    return -1;
	  }
}
