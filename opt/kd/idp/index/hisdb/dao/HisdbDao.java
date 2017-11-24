package kd.idp.index.hisdb.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import kd.idp.index.hisdb.bean.MeasureBean;
import kd.idp.index.hisdb.util.HisdbUtil;
import com.spring.dbservice.DBTemplate;
import com.spring.mapper.HashMapRowMapper;

/**
 * ��ѯHISDB��ʷ��
 * @author Administrator
 *
 */
public class HisdbDao {
	
	private  String dbName = "HISDB";
	private  String schemaName = "HISDB";
	private  String scadaTableNamePre = "MANALOG_DATA_SCADA_";
	private  String dataSource = "DMDataSource";
	private  String emsDataSource = "EMSDataSource";
	private  DBTemplate template = DBTemplate.getInstance(dataSource);
	private  DBTemplate emsTemplate = DBTemplate.getInstance(emsDataSource);
	
	/**
	 * ��������+��㣬�����ʷ���ݱ��
	 * @param date
	 * @param measureBeanList
	 * @return
	 */
	public  List<MeasureBean> getDataListById(Date date,List<MeasureBean> measureBeanList){
		List<MeasureBean> resultList = new ArrayList<MeasureBean>();
		StringBuffer sb = new StringBuffer("SELECT * FROM "+dbName+"."+schemaName+"."+scadaTableNamePre
				+new SimpleDateFormat("yyyyMMdd").format(date)+" where DATA_ID in(");
		try {
			if (measureBeanList.size()>0) {
				for (int i = 0; i < measureBeanList.size(); i++) {
					sb.append("'"+measureBeanList.get(i).getMeasureId()+"'");
					if (i<measureBeanList.size()-1) {
						sb.append(",");
					}
				}
				sb.append(")");
				List<Map<String, Object>>  sourceList = emsTemplate.query(sb.toString(), new HashMapRowMapper());
				return getMeasureBeanListBySourceList(measureBeanList,sourceList);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return resultList;
		}
		return resultList;
	}
	/**
	 * ����sql��ȡ����ʷ���ݱ�����ɲ�����
	 * @param measureBeanList
	 * @param sourceList
	 * @return
	 * @throws Exception
	 */
	private  List<MeasureBean> getMeasureBeanListBySourceList(List<MeasureBean> measureBeanList
			,List<Map<String,Object>> sourceList) throws Exception{
		List<MeasureBean>  resultList = new ArrayList<MeasureBean>();
		List<String> columnList = HisdbUtil.getColumnByTime(1);
		for (int i = 0; i < measureBeanList.size(); i++) {
			MeasureBean measureBean = measureBeanList.get(i);
			MeasureBean measureBeanNew = new MeasureBean(measureBean.getDisplayName(),measureBean.getMeasureId());
			for (int j = 0; j < sourceList.size(); j++) {
				Map<String,Object> sourceMap = sourceList.get(j);
				if (measureBeanNew.getMeasureId().equals(sourceMap.get("DATA_ID").toString())) {
					measureBeanNew.setMeasureName(sourceMap.get("NAME").toString());
					measureBeanNew.setColumnList(columnList);
					List<Float> dataList = new ArrayList<Float>();
					for (String column : columnList) {
						dataList.add(HisdbUtil.getFloatByObject(sourceMap.get(column)));
					}
					measureBeanNew.setDataList(dataList);
					resultList.add(measureBeanNew);
					break;
				}
			}	
		}
		return resultList;
	}
	public  List<Map<String,Object>> findById(String id,String lastDay,String lastYear) throws Exception{
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
	    //���뵱ǰϵͳʱ���ȥ�����ʱ��
		lastDay=PowerTime.getYesterday();
		lastYear=PowerTime.getQnday();
		String sql="select ����,RDL AS ����,ʵʱ���ֵ AS ������,ʵʱ���ʱ�� from IDPDWDATANEW.��������_����_�ջ��� where ID='"+id+"'  AND (���� = '"+lastDay+"' OR ����= '"+lastYear+"')";
		result=template.getResultMapList(sql); 
		return result;
	}
	public  List<Map<String,Object>> findByDataId(String dataId,String useTime){
		List<Map<String,Object>> timeList = new ArrayList<Map<String,Object>>();
		String sql="SELECT * FROM "+schemaName+"."+scadaTableNamePre+useTime+" WHERE DATA_ID='"+dataId+"'";
		timeList=emsTemplate.getResultMapList(sql); 
		return timeList;
	}
	public  void main(String[] args) throws Exception {
		List<Map<String,Object>> resultTest = new ArrayList<Map<String,Object>>();
		resultTest=new HisdbDao().findByDataId("122723133466635093","20160729");
		System.out.println(resultTest.toString());
		
		
	}
	
}
