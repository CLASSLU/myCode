package kd.idp.common.utility;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapIptoPublicUser {
	/**
	 * ip匹配对应的公共用户 pcj
	 * @param ip
	 * @param  IPConfig
	 * @return String
	 */
	public static String getInitUserByIp(String ip,Map<String, String> IPConfig){
		String template="public2";
		float initSimiliarty=0;
		String resultKey="";
		for(String key:IPConfig.keySet()){
			
			float similarty=getSimilarityRatio(ip,key);
			if(similarty>initSimiliarty){
				initSimiliarty=similarty;
				resultKey=key;
			}
		}
		template=IPConfig.get(resultKey);
	    return template;
	}
	
	/**
	 * 比较两个字符串
	 * @param str
	 * @param  target
	 * @return int
	 */
	public static int compare(String str, String target)
    {
        int d[][];              // 矩阵
        int n = str.length();
        int m = target.length();
        int i;                  // 遍历str的
        int j;                  // 遍历target的
        char ch1;               // str的
        char ch2;               // target的
        int temp;               // 记录相同字符,在某个矩阵位置值的增量,不是0就是1
        if (n == 0) { return m; }
        if (m == 0) { return n; }
        d = new int[n + 1][m + 1];
        for (i = 0; i <= n; i++)
        {                       // 初始化第一列
            d[i][0] = i;
        }

        for (j = 0; j <= m; j++)
        {                       // 初始化第一行
            d[0][j] = j;
        }

        for (i = 1; i <= n; i++)
        {                       // 遍历str
            ch1 = str.charAt(i - 1);
            // 去匹配target
            for (j = 1; j <= m; j++)
            {
                ch2 = target.charAt(j - 1);
                if (ch1 == ch2 || ch1 == ch2+32 || ch1+32 == ch2)
                {
                    temp = 0;
                } else
                {
                    temp = 1;
                }
                // 左边+1,上边+1, 左上角+temp取最小
                d[i][j] = min(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1] + temp);
            }
        }
        return d[n][m];
    }

	/**
	 * 返回三个数中的最大值
	 * @param one
	 * @param two
	 * @param three
	 * @return int
	 */
    public static int min(int one, int two, int three)
    {
        return (one = one < two ? one : two) < three ? one : three;
    }

    /**
	 * 获取两个字符串的相似度 0-1
	 * @param str
	 * @param target
	 * @return int
	 */

    public static float getSimilarityRatio(String str, String target)
    {
        return 1 - (float) compare(str, target) / Math.max(str.length(), target.length());
    }
    /**
     * 匹配IP
     * 时间：2014-01-29
     * @author aviyy
     */
    public static String getInitUserByIP(String ip,Map<String, String> IPConfig){
    	String[] aheadIPAtrr = ip.split("\\.");
    	for(String key:IPConfig.keySet()){
    		boolean isMatching = true;	//默认匹配
    		String[] aheadKeyAtrr = key.split("\\.");
    		for (int i=0; i<aheadIPAtrr.length; i++){
    			if ("*".equals(aheadKeyAtrr[i])){
    				continue;
    			}else{
    				if (!aheadKeyAtrr[i].equals(aheadIPAtrr[i])){
    					isMatching = false;
    					break;
    				}
    			}
    		}
    		if(isMatching){
				return IPConfig.get(key);
			}
		}
    	return null;
    }
    
    
    
/*	public static void main(String[] args) {
		String[] ips = {"10.40.*.*","10.41.*.*","10.39.*.*","192.168.*.*","192.167.*.*","192.169.*.*"};
		float test[] = {0,0,0,0,0,0};
		for(int i=0;i<ips.length;i++){
			int tt = ips[i].indexOf("*", 1);
			String ttt = ips[i].substring(0,tt);
			//test[i]=getSimilarityRatio("10.40.*.*",ips[i]);
			System.out.println(tt+""+ttt);
		}
		
	}*/
	
	
}
