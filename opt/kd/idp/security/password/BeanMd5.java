package kd.idp.security.password;

import java.security.MessageDigest;

/**
 * <p>Title: </p>
 * <p>Description: �����ݽ���MD5����</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company:�人���ǿƼ��ɷ����޹�˾ </p>
 * @author �Դ�ƽ
 * @version 1.0
 */
public class BeanMd5 {
    private static final String[] hexDigits = {"0", "1", "2", "3", "4", "5",
                                              "6", "7", "8", "9", "A", "B", "C",
                                              "D", "E", "F"};

    private BeanMd5() {
    }

    /**
     * ���ֽڱ��16��������ʾ
     * @param b byte[] Ҫת�����ֽ�����
     * @return String ת������ַ���
     */
    private static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i])); //��ʹ�ñ�����ת����ɵõ����ܽ����16���Ʊ�ʾ����������ĸ��ϵ���ʽ

        }
        return resultSb.toString();
    }


    /**
     * ��һ���ֽ�ת����16���Ƶ��ַ���
     * @param b byte Ҫת�����ֽ�
     * @return String ת������ַ���
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
     * ʹ��MD5�����ݽ��м��ܣ����ص��ַ�����16���Ƶ�����
     * @param origin String Ҫ���ܵ��ַ���
     * @return String ���ܺ���ַ�������ĸ�Ǵ�д�ģ�
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
     * ���Է���
     * @param args String[]
     */
    public static void main(String[] args) {
        String strPwd = "1";
        System.out.println(BeanMd5.MD5HexEncode(strPwd));

    }
}
