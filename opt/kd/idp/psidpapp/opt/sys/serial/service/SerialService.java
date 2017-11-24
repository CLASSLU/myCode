package kd.idp.psidpapp.opt.sys.serial.service;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sdjxd.pms.platform.tool.DateTool;
import com.sdjxd.pms.platform.tool.StringTool;

import kd.idp.psidpapp.opt.sys.serial.bean.PageNumberPattern;
import kd.idp.psidpapp.opt.sys.serial.dao.SerialDao;
import kd.idp.psidpapp.opt.sys.util.DateUtil;

public class SerialService {
	
	public static int CYCLE_YEAR = 0;
	public static int CYCLE_MONTH = 1;
	public static int CYCLE_DAY = 2;
	public static int CYCLE_OTHER = 3;
	
	private SerialDao serialDao;
		
	public SerialDao getSerialDao() {
		return serialDao;
	}

	public void setSerialDao(SerialDao serialDao) {
		this.serialDao = serialDao;
	}

	/**
	 * @param patternId	编号模板ID
	 * @return			创建当前序列值
	 * @throws Exception
	 */
	public String createSerialNum(PageNumberPattern pageNumberPattern) throws Exception {
		int serialNum = 0;	
		try {
			int year = 0;
			int month = 0;
			int day = 0;
			year = DateUtil.getCurrYear();
			if (pageNumberPattern.getSerialCycle() > CYCLE_YEAR) {
				month = DateUtil.getCurrMonth();
			}
			if (pageNumberPattern.getSerialCycle() > CYCLE_MONTH) {
				day = DateUtil.getCurrDay();
			}
			serialNum = serialDao.getSerialNum(pageNumberPattern.getPatternId(), year, month, day);
			if (serialNum == -1) {
				serialDao.createSerial(pageNumberPattern.getPatternId(), year, month, day, pageNumberPattern.getStartNum());
				serialNum = pageNumberPattern.getStartNum();
			} else {
				serialNum++;
				serialDao.updateSerialNum(pageNumberPattern.getPatternId(), year, month, day, serialNum);
			}	
		} catch (Exception e) {
				e.printStackTrace();
				throw e;
		} 
		return StringTool.leftPad(String.valueOf(serialNum), pageNumberPattern.getSerialLength(),'0');
	}
	
	/**
	 * 获取编号目前自增数
	 * @param patternId	编号模板ID
	 * @return	获取编号目前自增数【0018】
	 */
	public String getSerialNum(String patternId){
		int serialNum = 0;	
		PageNumberPattern pageNumberPattern = null;
		try {
			pageNumberPattern = serialDao.findByPatternId(patternId);
			int year = 0;
			int month = 0;
			int day = 0;
			year = DateUtil.getCurrYear();
			if (pageNumberPattern.getSerialCycle() > CYCLE_YEAR) {
				month = DateUtil.getCurrMonth();
			}
			if (pageNumberPattern.getSerialCycle() > CYCLE_MONTH) {
				day = DateUtil.getCurrDay();
			}
			serialNum = serialDao.getSerialNum(pageNumberPattern.getPatternId(), year, month, day);
			if (-1 == serialNum){
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return StringTool.leftPad(String.valueOf(serialNum), pageNumberPattern.getSerialLength(),'0');
	}
	
	/**
	 * 获取编号模板对象
	 * @param patternId	编号模板ID
	 * @return	获取编号模板对象
	 */
	public PageNumberPattern getPageNumberPattern(String patternId){
		PageNumberPattern pageNumberPattern = null;
		try {
			pageNumberPattern = serialDao.findByPatternId(patternId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pageNumberPattern;
	}
	
	/**
	 * @param patternId		编号模板ID
	 * @return				创建对应模板编号值
	 * @throws Exception
	 */
	public String createPageNumber(String patternId){
		String pagenum = null;
		PageNumberPattern pageNumberPattern = null;
		try {
			pageNumberPattern = serialDao.findByPatternId(patternId);
			if (null != pageNumberPattern){
				Map<String, String> replacementMap = new HashMap<String, String>();
				String regex = "(\\[(name|py)(\\d*)\\])|(\\[(enum)\\.\\[([^\\]]*)\\]\\])|(\\[serial\\])|(\\[([^\\]]*)\\])";
				Pattern p = Pattern.compile(regex);
				Matcher m = p.matcher(pageNumberPattern.getPageContent());
				while (m.find()){
					String matchStr = m.group(0);
					String matchValue = "";
					if ("[serial]".equals(matchStr)) {
						matchValue = createSerialNum(pageNumberPattern);
					} else {
						if ("name".equals(m.group(2))){
						} else {
							if ("py".equals(m.group(2))) {
							} else {
								if ("enum".equals(m.group(5))) {
								} else {
									matchValue = DateUtil.getNow(m.group(9));
								}
							}
						}
					}
					replacementMap.put(matchStr, matchValue);
				}
				pagenum = StringTool.replace(pageNumberPattern.getPageContent(), replacementMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return pagenum;
	}
	
	/**
	 * @param patternId			编号模板ID
	 * @param serialValue		指定编号值
	 * @return					创建对应模板指定编号值
	 * @throws Exception
	 */
	public String createPageNumberBySerialValue(String patternId, String serialValue){
		String pagenum = null;
		PageNumberPattern pageNumberPattern = null;
		try {
			pageNumberPattern = serialDao.findByPatternId(patternId);
			if (null != pageNumberPattern){
				Map<String, String> replacementMap = new HashMap<String, String>();
				String regex = "(\\[(name|py)(\\d*)\\])|(\\[(enum)\\.\\[([^\\]]*)\\]\\])|(\\[serial\\])|(\\[([^\\]]*)\\])";
				Pattern p = Pattern.compile(regex);
				Matcher m = p.matcher(pageNumberPattern.getPageContent());
				while (m.find()){
					String matchStr = m.group(0);
					String matchValue = "";
					if ("[serial]".equals(matchStr)) {
						matchValue = serialValue;
					} else {
						if ("name".equals(m.group(2))){
						} else {
							if ("py".equals(m.group(2))) {
							} else {
								if ("enum".equals(m.group(5))) {
								} else {
									matchValue = DateUtil.getNow(m.group(9));
								}
							}
						}
					}
					replacementMap.put(matchStr, matchValue);
				}
				pagenum = StringTool.replace(pageNumberPattern.getPageContent(), replacementMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return pagenum;
	}
	
	/**
	 * @return	获取序列号（20170815150233999123456）
	 */
	public static String getSerialIdByDate(String prefix){
		String serialId = "";
		Random random = new Random();
		try {
			serialId = DateTool.getCurrentDate("yyyyMMddHHmmssSSS");
			for (int i=0; i<6; i++){
				int r = random.nextInt(10);
				serialId += r;
			}
			if (null != prefix && !"".equals(prefix)){
				serialId = prefix + serialId;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return serialId;
	}
	
	/**
	 * @return	获取序列号（1502782085435978300）
	 */
	public static String getSerialIdByMillisecond(String prefix){
		String serialId = String.valueOf(Calendar.getInstance().getTimeInMillis());
		Random random = new Random();
		try {
			for (int i=0; i<6; i++){
				int r = random.nextInt(10);
				serialId += r;
			}
			if (null != prefix && !"".equals(prefix)){
				serialId = prefix + serialId;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return serialId;
	}
	
	/**
	 * @return	获取序列号（74B7C42888624CAF8ADE73C4FA281D40）
	 */
	public static String getSerialIdByUUID(String prefix){
		String serialId = String.valueOf(UUID.randomUUID()).replaceAll("-", "").toUpperCase();
		if (null != prefix && !"".equals(prefix)){
			serialId = prefix + serialId;
		}
		return serialId;
	}
	
	//测试方法
	public static void main(String[] args) {
		try {
//			SerialService serialService = (SerialService)SpringBeanFactory.getInstance().getBean("serialService");
//			System.out.println(serialService.createPageNumber("1"));
//			System.out.println(serialService.createPageNumber("2"));
//			System.out.println(serialService.createPageNumber("3"));
			
			System.out.println(getSerialIdByDate("USER_"));
			System.out.println(getSerialIdByDate(""));
			System.out.println(getSerialIdByMillisecond("USER_"));
			System.out.println(getSerialIdByMillisecond(""));
			System.out.println(getSerialIdByUUID("USER_"));
			System.out.println(getSerialIdByUUID(""));
			
			System.out.println(DateUtil.getCurrtWeekDay());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
