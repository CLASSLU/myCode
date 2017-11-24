package kd.idp.security.subProtect;

import java.io.*;
import java.security.*;
import java.security.cert.*;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.crypto.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: </p>
 * @author zerone
 * @version 1.0
 */

public class FileEncrypt {
    private static boolean Debug = true;
    public FileEncrypt() {
    }

    /**
     * 获得随机数
     * @param num 位数
     */
    private static byte[] generateRandom(int num) {
        byte[] bytes = new byte[num];
        Random rand = new Random(System.currentTimeMillis());
        rand.nextBytes(bytes);
        return bytes;
    }


    /**
     * 计算Hash值，算法使用MD5
     * @param message byte[] 原始数据
     * @return byte[] Hash值
     */

    private static byte[] makeHashWithMD5(byte[] message) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("NoSuchAlgorithmException:" + ex.toString());
            FileEncrypt.addSecurityLog("计算Hash值[MD5]出错！", ex);
            return null;
        }

        if (md != null) {
            md.update(message);
            return md.digest();
        } else {
            return null;
        }
    }

    /**
     * 计算Hash值，算法使用SHA1
     * @param message byte[] 原始数据
     * @return byte[] Hash值
     */

    private static byte[] makeHashWithSHA1(byte[] message) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("NoSuchAlgorithmException:" + ex.toString());
            FileEncrypt.addSecurityLog("计算Hash值[SHA1]出错！", ex);
            return null;
        }

        if (md != null) {
            md.update(message);
            return md.digest();
        } else {
            return null;
        }
    }

    /**
     * 从文件中读取证书
     * @param fileName String 证书路径
     * @return X509Certificate 证书
     */
    private static X509Certificate getCertificateFromFile(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println("The Certificate[" + fileName +
                               "] is not exist.");
            return null;
        }
//        X509Certificate cert = null;
//        FileReader fr = null;
//        try {
//            PEMReader reader = null;
//            try {
//                fr = new FileReader(fileName);
//                reader = new PEMReader(fr, null);
//                cert = (X509Certificate) reader.readObject();
//            } catch (IOException ex) {
//                System.out.println("IOException:" + ex.toString());
//                FileEncrypt.addSecurityLog("从文件中读取数字证书出错！ ", ex);
//                return null;
//            } finally {
//                if (reader != null) {
//                    try {
//                        reader.close();
//                    } catch (IOException ex) {
//                    }
//                    reader = null;
//                }
//            }
//        } finally {
//            if (fr != null) {
//                try {
//                    fr.close();
//                } catch (IOException ex) {
//                }
//                fr = null;
//            }
//        }
//        return cert;
        X509Certificate cert = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(fileName);
            CertificateFactory cf = CertificateFactory.getInstance("X509", "BC");
            cert = (X509Certificate) cf.generateCertificate(fis);
        } catch (NoSuchProviderException ex) {
            ex.printStackTrace();
        } catch (CertificateException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }  finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ex) { /* Ignore */
                }
            }
        }
        return cert;
    }

    /**
     * 使用数字证书中的公钥加密
     * @param String certFileName 数字证书文件全路径
     * @param String fileName 欲加密的文件名
     * @param String fileName 加密后的文件名
     * @return int 加密结果 0-> 成功
     *                     1-> 文件加密：读取证书失败
     *                     2-> 文件加密：源文件不存在
     *                     3-> 文件加密：读取源文件出错
     *                     4-> 文件加密：填充数据格式错误
     *                     5-> 文件加密：无效的分组大小
     *                     6-> 文件加密：无效的加密状态
     *                     7-> 文件加密：无效的加密密钥
     *                     8-> 文件加密：加密算法不存在
     *                     9-> 文件加密：算法提供者不存在
     *                     10-> 文件加密：填充模式不支持
     *                     11-> 文件加密：加密后文件不存在
     *                     12-> 文件加密：加密结果写入文件出错
     */
    public static int SAPI_Ukey_FEenc(String certFileName, String fileName,
                                      String encryptedFileName) {
        X509Certificate cert = getCertificateFromFile(certFileName);
        if (cert == null) {
            //读取失败
            return 1;
        }
        // 获得4字节随机数，作为加密密钥
        byte[] random = generateRandom(4);
        // 读取文件内容
        File file = new File(fileName);
        if (!file.exists()) {
            // 文件不存在
            return 2;
        }
        int fileLength = (int) (file.length());
        byte[] fileBytes = new byte[fileLength];
        byte[] encryptedData = new byte[fileLength + 128];
        FileInputStream reader = null;
        try {
            reader = new FileInputStream(file);
            int ret = reader.read(fileBytes);
            int result = ret / 4;
            int remain = ret % 4;
            byte[] temp = new byte[4];
            for (int i = 0; i < result; i++) {
                System.arraycopy(fileBytes, i * 4, temp, 0, temp.length);
                byte[] en = xor(temp, random);
                System.arraycopy(en, 0, encryptedData, i * 4, en.length);
            }
            if (remain != 0) {
                temp = new byte[remain];
                System.arraycopy(fileBytes, result * 4, temp, 0, temp.length);
                byte[] en = xor(temp, random);
                System.arraycopy(en, 0, encryptedData, result * 4, en.length);
            }
        } catch (IOException ex) {
            FileEncrypt.addSecurityLog("读取文件[" + file.getName() + "]出错！", ex);
            return 3;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ioex) {
                }
                reader = null;
            }
        }

        // 使用数字证书中得公钥加密随机数(异或密钥)
        byte[] encryptedKey = null;
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
            cipher.init(Cipher.ENCRYPT_MODE, cert.getPublicKey());
            encryptedKey = cipher.doFinal(random);
        } catch (BadPaddingException ex) {
            System.out.println("BadPaddingException:" + ex.toString());
            FileEncrypt.addSecurityLog("文件加密：填充数据格式错误", ex);
            return 4;
        } catch (IllegalBlockSizeException ex) {
            System.out.println("IllegalBlockSizeException:" + ex.toString());
            FileEncrypt.addSecurityLog("文件加密：无效的分组大小", ex);
            return 5;
        } catch (IllegalStateException ex) {
            System.out.println("IllegalStateException:" + ex.toString());
            FileEncrypt.addSecurityLog("文件加密：无效的加密状态", ex);
            return 6;
        } catch (InvalidKeyException ex) {
            System.out.println("InvalidKeyException:" + ex.toString());
            FileEncrypt.addSecurityLog("文件加密：无效的加密密钥", ex);
            return 7;
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("NoSuchAlgorithmException:" + ex.toString());
            FileEncrypt.addSecurityLog("文件加密：加密算法不存在", ex);
            return 8;
        } catch (NoSuchProviderException ex) {
            System.out.println("NoSuchProviderException:" + ex.toString());
            FileEncrypt.addSecurityLog("文件加密：算法提供者不存在", ex);
            return 9;
        } catch (NoSuchPaddingException ex) {
            System.out.println("NoSuchPaddingException:" + ex.toString());
            FileEncrypt.addSecurityLog("文件加密：填充模式不支持", ex);
            return 10;
        }
        // 由于CSP与OpenSSL的字节序不同，需要颠倒字节序
        encryptedKey = invertedOrder(encryptedKey);
        // 将加密后的异或密钥赋予文件结尾
        System.arraycopy(encryptedKey, 0, encryptedData, fileLength, 128);
        // 将加密结果写入文件
        encryptedFileName = encryptedFileName.replace('\\', '/');
        File path = new File(encryptedFileName.substring(0,
                encryptedFileName.lastIndexOf("/")));
        if (!path.exists()) {
            path.mkdirs();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(encryptedFileName);
            fos.write(encryptedData);
            fos.flush();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            FileEncrypt.addSecurityLog("文件加密：加密后文件不存在", ex);
            return 11;
        } catch (IOException ex) {
            ex.printStackTrace();
            FileEncrypt.addSecurityLog("文件加密：加密结果写入文件出错", ex);
            return 12;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException ex1) {
                }
            }
        }
        return 0;
    }

    /**
     * 使用数字证书中的公钥加密
     * @param String certFielName 数字证书文件全路径
     * @param String toEnc 明文数据(普通字符串，非十六进制数据)
     * @return String 加密结果(十六进制) 成功返回加密结果，失败返回null
     */
    public static String SAPI_Ukey_IEenc(String certFileName, String toEnc) {
        // 读取证书
        X509Certificate cert = getCertificateFromFile(certFileName);
        if (cert == null) {
            return null;
        }
        byte[] plain = toEnc.getBytes();
        int length = plain.length;
        int pageSize = 117; // 一个原始数据分组大小
        int bufferSize = 128; // 一个加密密文分组大小
        int result = length / pageSize;
        int remain = length % pageSize;
        byte[] encryptedData = null;
        if (remain == 0) {
            encryptedData = new byte[result * bufferSize];
        } else {
            encryptedData = new byte[(result + 1) * bufferSize];
        }
        // 初始化加密操作
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
            cipher.init(Cipher.ENCRYPT_MODE, cert.getPublicKey());

            for (int i = 0; i < result; i++) {
                byte[] temp = new byte[pageSize];
                System.arraycopy(plain, i * pageSize, temp, 0, pageSize);

                if (Debug) {
                    System.out.println("-<-before->- " + i + " -- "
                                       + HexConversion.hexify_haveSpace(temp));
                }
                byte[] encryptedBytes = cipher.doFinal(temp);
                if (encryptedBytes != null) {
                    // 由于CSP与OpenSSL的字节序不同，需要颠倒字节序
                    encryptedBytes = invertedOrder(encryptedBytes);
                    if (Debug) {
                        System.out.println("->-after--<- " + i + " -- " +
                                           HexConversion.hexify_haveSpace(
                                encryptedBytes));
                    }
                    System.arraycopy(encryptedBytes, 0, encryptedData,
                                     i * bufferSize, bufferSize);
                } else {
                    FileEncrypt.addSecurityLog("分组加密操作失败！", null);
                    return null;
                }
            }
            // 加密最后一个分组数据
            if (remain != 0) {
                byte[] temp = new byte[remain];
                System.arraycopy(plain, result * pageSize, temp, 0, remain);
                byte[] encryptedBytes = cipher.doFinal(temp);
                // 由于CSP与OpenSSL的字节序不同，需要颠倒字节序
                encryptedBytes = invertedOrder(encryptedBytes);
                System.arraycopy(encryptedBytes, 0, encryptedData,
                                 result * bufferSize, encryptedBytes.length);
            }
        } catch (BadPaddingException ex) {
            System.out.println("BadPaddingException:" + ex.toString());
            FileEncrypt.addSecurityLog("公钥加密：填充数据格式错误", ex);
            return null;
        } catch (IllegalBlockSizeException ex) {
            System.out.println("IllegalBlockSizeException:" + ex.toString());
            FileEncrypt.addSecurityLog("公钥加密：无效的分组大小", ex);
            return null;
        } catch (IllegalStateException ex) {
            System.out.println("IllegalStateException:" + ex.toString());
            FileEncrypt.addSecurityLog("公钥加密：无效的加密状态", ex);
            return null;
        } catch (InvalidKeyException ex) {
            System.out.println("InvalidKeyException:" + ex.toString());
            FileEncrypt.addSecurityLog("公钥加密：无效的加密密钥", ex);
            return null;
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("NoSuchAlgorithmException:" + ex.toString());
            FileEncrypt.addSecurityLog("公钥加密：加密算法不存在", ex);
            return null;
        } catch (NoSuchProviderException ex) {
            System.out.println("NoSuchProviderException:" + ex.toString());
            FileEncrypt.addSecurityLog("公钥加密：算法提供者不存在", ex);
            return null;
        } catch (NoSuchPaddingException ex) {
            System.out.println("NoSuchPaddingException:" + ex.toString());
            FileEncrypt.addSecurityLog("公钥加密：填充模式不支持", ex);
            return null;
        }
        return  HexConversion.hexify(encryptedData);
    }

    /**
     * 使用数字证书中的公钥验证签名（签名方式：MD5_RSA_PKCS1）
     * @param certFileName  数字证书文件全路径
     * @param signData      原始数据(十六进制)
     * @param signedData    签名后的数据(十六进制)
     * @return int          验证签名结果 0->成功
     *                      1-> 验证签名：读取证书失败
     *                      2-> 验证签名：填充数据格式错误
     *                      3-> 验证签名：无效的分组大小
     *                      4-> 验证签名：无效的加密状态
     *                      5-> 验证签名：无效的加密密钥
     *                      6-> 验证签名：加密算法不存在
     *                      7-> 验证签名：算法提供者不存在
     *                      8-> 验证签名：填充模式不支持
     *                      9-> 验证签名：数据摘要不一致
     */
    public static int SAPI_Ukey_Verify(String certFileName, String signData,
                                       String signedData) {
        // 读取证书
        X509Certificate cert = getCertificateFromFile(certFileName);
        if (cert == null) {
            return 1;
        }
        // 验证签名
        byte[] sign = HexConversion.parseHexString(signedData);
        // 由于CSP与OpenSSL的字节序不同，需要颠倒字节序
        sign = invertedOrder(sign);
        byte[] decryptData = null;
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
            cipher.init(Cipher.DECRYPT_MODE, cert);
            decryptData = cipher.doFinal(sign);
        } catch (BadPaddingException ex) {
            ex.printStackTrace();
            System.out.println("BadPaddingException:" + ex.toString());
            FileEncrypt.addSecurityLog("验证签名：填充数据格式错误", ex);
            return 2;
        } catch (IllegalBlockSizeException ex) {
            System.out.println("IllegalBlockSizeException:" + ex.toString());
            FileEncrypt.addSecurityLog("验证签名：无效的分组大小", ex);
            return 3;
        } catch (IllegalStateException ex) {
            System.out.println("IllegalStateException:" + ex.toString());
            FileEncrypt.addSecurityLog("验证签名：无效的加密状态", ex);
            return 4;
        } catch (InvalidKeyException ex) {
            System.out.println("InvalidKeyException:" + ex.toString());
            FileEncrypt.addSecurityLog("验证签名：无效的加密密钥", ex);
            return 5;
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("NoSuchAlgorithmException:" + ex.toString());
            FileEncrypt.addSecurityLog("验证签名：加密算法不存在", ex);
            return 6;
        } catch (NoSuchProviderException ex) {
            System.out.println("NoSuchProviderException:" + ex.toString());
            FileEncrypt.addSecurityLog("验证签名：算法提供者不存在", ex);
            return 7;
        } catch (NoSuchPaddingException ex) {
            System.out.println("NoSuchPaddingException:" + ex.toString());
            FileEncrypt.addSecurityLog("验证签名：填充模式不支持", ex);
            return 8;
        }
        // 由于握奇的数据自带填充数据，需要处理解密后数据
        if (decryptData != null) {
            if (decryptData.length > 16) {
                byte[] verifyData = new byte[16];
                System.arraycopy(decryptData, decryptData.length - 16,
                                 verifyData, 0, 16);
                decryptData = verifyData;
            }
        }
        // 计算Hash值MD5
        byte[] plain = HexConversion.parseHexString(signData);
        byte[] md5 = makeHashWithMD5(plain);
        if (Debug) {
            System.out.println("MD5 == " + HexConversion.hexify_haveSpace(md5));
            System.out.println("decryptData == " +
                               HexConversion.hexify_haveSpace(decryptData));
        }
        // 比较Hash值
        if (Arrays.equals(md5, decryptData)) {
            return 0;
        } else {
            return 9;
        }
    }
    /**
     * 错误日志
     * @param message  日志信息
     */
    private static void addSecurityLog(String message, Exception ex) {
        File file = new File("log/error_" + getCurrentDate(1) + ".log");
        if (!file.exists()) {
            File dir = new File("log/");
            if (!dir.exists()) {
                dir.mkdirs();
            }
        }

        FileOutputStream fos = null;
        try {
            PrintStream ps = null;
            try {
                fos = new FileOutputStream(file, true);
                ps = new PrintStream(fos, true);
                ps.println("[" + getCurrentDate(0) + "] " + message);
                ps = new PrintStream(fos, true);
                if (ex != null) {
                    ex.printStackTrace(ps);
                }
            } catch (IOException myex) {
                System.out.println("SystemLog.addSecurityLog:" +
                                   myex.getMessage());
            } finally {
                if (ps != null) {
                    ps.close();
                    ps = null;
                }
            }
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException ioex) {
                }
                fos = null;
            }
        }
    }

    /**
     * 根据给定的时间类型，获得指定时间格式
     */
    private static String getCurrentDate(int type) {
        String currentDate = "";
        long time = System.currentTimeMillis();
        Date date = new Date(time);
        if (type == 0) {
            SimpleDateFormat dataFormat = new SimpleDateFormat(
                    "yyyy/MM/dd HH:mm:ss");
            currentDate = dataFormat.format(date);
        } else if (type == 1) {
            SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMdd");
            currentDate = dataFormat.format(date);
        }
        return currentDate;
    }

    /**
     * 根据给定的key，对数据data进行异或预算
     */
    private static byte[] xor(byte[] data, byte[] key) {
        byte[] retData = new byte[data.length];
        if (data.length <= key.length) {
            for (int i = 0; i < data.length; i++) {
                retData[i] = (byte) (data[i] ^ key[i]);
            }
        } else {
            for (int i = 0; i < data.length; i++) {
                int k = i % 4;
                retData[i] = (byte) (data[i] ^ key[k]);
            }
        }
        return retData;
    }

    /**
     * 将给定的字节数组进行逆序操作
     * @param src     原始字节数组
     * @return byte[] 逆序后的字节数组
     */
    private static byte[] invertedOrder(byte[] src){
        byte[] ret = new byte[src.length];
        int index = src.length - 1;
        for (int i = 0; i < src.length; i++) {
            ret[i] = src[index - i];
        }
        return ret;
    }
    // 测试函数
    public static void main(String[] args) {
        String certFileName = "F:/workspace/psoms_ln/src/wdkey/test.cer";
        //验证签名=
        String plain_Str = "USER_1282113554156_794320130613";
        String signed_str = "668079D7B0263C55035FB02D40A8F0A7B9E82474E7755A91C3BE536814577576E7915376A60E9A1F66F40AD1F4DE4929F3B94D6E4E030000A45FA157E846D7EEAB440269221A8BB381FF3835FC0AA4F5C5D2288DE095B9AC5D90EDE7C31DE5A63D339EC51F56BF79A1D78F959962544F72F4E7BC4E27BB04F6BB7E62080ACC2F";
        int ret = SAPI_Ukey_Verify(certFileName, HexConversion.parseStringToHexString(plain_Str), signed_str);
        System.out.println("ret = " + ret);

        // 公钥加密
        String encryptedData = SAPI_Ukey_IEenc(certFileName, plain_Str);
        System.out.println("SAPI_Ukey_IEenc = " +encryptedData);

        //文件加密
        String fileName = "D:\\AVR-170529224017.pdf";
        String encryptedFileName = "D:\\解密.pdf";
        ret = SAPI_Ukey_FEenc(certFileName, fileName, encryptedFileName);
        System.out.println("SAPI_Ukey_FEenc = " + ret);
    }
}
