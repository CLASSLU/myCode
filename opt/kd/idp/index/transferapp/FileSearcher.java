package kd.idp.index.transferapp;

import java.io.File;  
import java.util.ArrayList;  
import java.util.List;  
import java.util.Queue;  
  
/** 
 * @author tiwson 2010-06-02 
 *  
 */  
public class FileSearcher {  
  
    /** 
     * �ݹ�����ļ� 
     * @param baseDirName  ���ҵ��ļ���·�� 
     * @param targetFileName  ��Ҫ���ҵ��ļ��� 
     * @param fileList  ���ҵ����ļ����� 
     */  
    public static void findFiles(String baseDirName, String targetFileName, List fileList) {  
        /** 
         * �㷨������ 
         * ��ĳ������������ҵ��ļ��г������������ļ��е��������ļ��м��ļ��� 
         * ��Ϊ�ļ��������ƥ�䣬ƥ��ɹ��������������Ϊ���ļ��У�������С� 
         * ���в��գ��ظ���������������Ϊ�գ�������������ؽ���� 
         */  
        String tempName = null;  
        //�ж�Ŀ¼�Ƿ����  
        File baseDir = new File(baseDirName);  
        if (!baseDir.exists() || !baseDir.isDirectory()){  
            System.out.println("�ļ�����ʧ�ܣ�" + baseDirName + "����һ��Ŀ¼��");  
        } else {  
            String[] filelist = baseDir.list();  
            for (int i = 0; i < filelist.length; i++) {  
                File readfile = new File(baseDirName + "\\" + filelist[i]);  
                //System.out.println(readfile.getName());  
                if(!readfile.isDirectory()) {  
                    tempName =  readfile.getName();   
                    if (FileSearcher.wildcardMatch(targetFileName, tempName)) {  
                        //ƥ��ɹ������ļ������ӵ������  
                        fileList.add(readfile.getAbsoluteFile());   
                    }  
                } else if(readfile.isDirectory()){  
                    findFiles(baseDirName + "\\" + filelist[i],targetFileName,fileList);  
                }  
            }  
        }  
    }  
      
    /** 
     * ͨ���ƥ�� 
     * @param pattern    ͨ���ģʽ 
     * @param str    ��ƥ����ַ��� 
     * @return    ƥ��ɹ��򷵻�true�����򷵻�false 
     */  
    private static boolean wildcardMatch(String pattern, String str) {  
        int patternLength = pattern.length();  
        int strLength = str.length();  
        int strIndex = 0;  
        char ch;  
        for (int patternIndex = 0; patternIndex < patternLength; patternIndex++) {  
            ch = pattern.charAt(patternIndex);  
            if (ch == '*') {  
                //ͨ����Ǻ�*��ʾ����ƥ���������ַ�  
                while (strIndex < strLength) {  
                    if (wildcardMatch(pattern.substring(patternIndex + 1),  
                            str.substring(strIndex))) {  
                        return true;  
                    }  
                    strIndex++;  
                }  
            } else if (ch == '?') {  
                //ͨ����ʺ�?��ʾƥ������һ���ַ�  
                strIndex++;  
                if (strIndex > strLength) {  
                    //��ʾstr���Ѿ�û���ַ�ƥ��?�ˡ�  
                    return false;  
                }  
            } else {  
                if ((strIndex >= strLength) || (ch != str.charAt(strIndex))) {  
                    return false;  
                }  
                strIndex++;  
            }  
        }  
        return (strIndex == strLength);  
    }  
  
    public static void main(String[] paramert) {  
        //    �ڴ�Ŀ¼�����ļ�  
        String baseDIR = "F:/����/���Ż��Ŀ���/����/��ʱ/���е������ط�����";   
        //    ����չ��Ϊtxt���ļ�  
        String fileName = "1361412092062.xls";   
        List resultList = new ArrayList();  
        FileSearcher.findFiles(baseDIR, fileName, resultList);   
        if (resultList.size() == 0) {  
            System.out.println("No File Fount.");  
        } else {  
            for (int i = 0; i < resultList.size(); i++) {  
                System.out.println(resultList.get(i));//��ʾ���ҽ����   
            }  
        }  
    } 
}