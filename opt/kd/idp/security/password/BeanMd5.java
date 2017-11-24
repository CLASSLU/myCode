package kd.idp.security.password;

import java.security.MessageDigest;

/**
 * <p>Title: </p>
 * <p>Description: 对数据进行MD5加密</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company:武汉蓝星科技股份有限公司 </p>
 * @author 赵春平
 * @version 1.0
 */
public class BeanMd5 {
    private static final String[] hexDigits = {"0", "1", "2", "3", "4", "5",
                                              "6", "7", "8", "9", "A", "B", "C",
                                              "D", "E", "F"};

    private BeanMd5() {
    }

    /**
     * 把字节变成16进制数显示
     * @param b byte[] 要转换的字节数组
     * @return String 转换后的字符串
     */
    private static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i])); //若使用本函数转换则可得到加密结果的16进制表示，即数字字母混合的形式

        }
        return resultSb.toString();
    }


    /**
     * 把一个字节转换成16进制的字符串
     * @param b byte 要转换的字节
     * @return String 转换后的字符串
     */
    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n = 256 + n;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }


    /**
     * 使用MD5对数据进行加密，返回的字符串是16进制的数字
     * @param origin String 要加密的字符串
     * @return String 加密后的字符串（字母是大写的）
     */
    public static String MD5HexEncode(String origin) {
        String resultString = null;

        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString =
                    byteArrayToHexString(md.digest(resultString.getBytes()));
        } catch (Exception ex) {

        }
        return resultString;
    }

    /**
     * 测试方法
     * @param args String[]
     */
    public static void main(String[] args) {
        String strPwd = "1";
        System.out.println(BeanMd5.MD5HexEncode(strPwd));

    }
}
