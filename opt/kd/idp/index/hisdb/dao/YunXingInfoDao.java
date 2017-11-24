package kd.idp.index.hisdb.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;

import com.spring.dbservice.DBTemplate;
import com.spring.mapper.HashMapStrRowMapper;
import com.sun.org.apache.bcel.internal.generic.NEW;

public class YunXingInfoDao {
	private  DBTemplate template = DBTemplate.getInstance();
	
	/**
	 * ����
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,String>> getJxList() throws Exception{
		String sql = "select top 8 Ʊ��,����,�ƻ�����,�ƻ��깤 from IDP_DDCZ.ZZ_����Ʊ order by �ʱ�� desc ";
		return template.query(sql, new HashMapStrRowMapper());
	}
	/**
	 * ����
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,String>> getGzList() throws Exception{
		String sql = "select top 8 ��������,�����豸����,����ʱ��,������� from IDP_DDRZGX.�������ϱ� where ��������='���е���' and ����ʱ�� is not null and   �Ƿ���='��' and (�Ƿ�ɾ��!='��' or �Ƿ�ɾ�� is null) order by ��¼ʱ�� desc  ";
		List<Map<String,String>> resultList = template.query(sql, new HashMapStrRowMapper());
		for (Map<String, String> map : resultList) {
			map.put("�������", map.get("�������").replaceAll(" ", ""));
		}
		return resultList;
	}
	/**
	 * ���м���
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,String>> getYxjsList() throws Exception{
		String sql = "select top 8 ����,����,��� from IDP_DDCZ.ZZ_���м��� order by �ʱ�� desc ";
		return template.query(sql, new HashMapStrRowMapper());
	}
	/**
	 * ����
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,String>> getHqList() throws Exception{
		String sql = "select top 8 ������·����,���豸Ӱ��̶�,ɽ��ص����˺�,��������ʱ�� from IDP_DDCZ.FZ_���� order by �ʱ�� desc ";
		return template.query(sql, new HashMapStrRowMapper());
	}
	/**
	 * ����
	 * @return
	 */
	public List<Map<String,String>> getZzList() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = simpleDateFormat.format(new Date(new Date().getTime()-24000*3600));
		String sql = "select a.NAME ��������,a.���ش���,a.����8ʱ��,a.����9ʱ�� from IDPDWDATANEW.��������_������� a, IDPDWIDNEW.��������_������� b "
		+" where a.����='"+date+"' and a.���ش���>=6 and b.����='����' and b.�Ƿ�����='��'  and a.ID=b.ID order by ���ش��� desc ";
		List<Map<String,String>> resultList = template.query(sql, new HashMapStrRowMapper());
		for (int i = resultList.size(); i < 8; i++) {
			resultList.add(new HashMap<String, String>());
		}
		return resultList;
	}
	/**
	 * ˮ��
	 * @return
	 */
	public List<Map<String,String>> getSqList() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = simpleDateFormat.format(new Date(new Date().getTime()-24000*3600));
		String sql = "select top 100 ��λ, ����ˮλ,����ˮλ,�������,��������,����ֵ from IDPDWDATANEW.��������_ˮ�� where ����='"+date+"' "
		+" and ��λ in('���ް�','��Ͽ','ˮ����','������','����Ϫ','����','�п�','��ǿϪ') ";
		List<Map<String,String>> resultList = template.query(sql, new HashMapStrRowMapper());
		for (int i = resultList.size(); i < 8; i++) {
			resultList.add(new HashMap<String, String>());
		}
		return resultList;
	}
}
