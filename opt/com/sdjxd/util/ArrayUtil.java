package com.sdjxd.util;

public class ArrayUtil {
	/**
	 * �ַ����������Ƿ����ָ�����ַ���
	 * @param strs
	 * @param s
	 * @return
	 */
	public static boolean isContain(String[] strs, String s) {
		/*
		 * �˷�����������������һ����Ҫ���ҵ��ַ������飬�ڶ�����Ҫ���ҵ��ַ����ַ���
		 */
		for (int i = 0; i < strs.length; i++) {
			if (strs[i].indexOf(s) != -1) {// ѭ�������ַ��������е�ÿ���ַ������Ƿ�������в��ҵ�����
				return true;// ���ҵ��˾ͷ����棬���ڼ�����ѯ
			}
		}
		return false;// û�ҵ�����false
	}

	/**
	 * 
	 * @param strs
	 * @param s
	 * @return
	 */
	public static boolean isHave(Object[] strs, Object s) {
		/*
		 * �˷�����������������һ����Ҫ���ҵ��ַ������飬�ڶ�����Ҫ���ҵ��ַ����ַ���
		 */
		for (int i = 0; i < strs.length; i++) {
			if (strs[i].equals(s)) {// ѭ�������ַ��������е�ÿ���ַ������Ƿ�������в��ҵ�����
				return true;// ���ҵ��˾ͷ����棬���ڼ�����ѯ
			}
		}
		return false;// û�ҵ�����false
	}

}
