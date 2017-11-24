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
     * ��������
     * @param num λ��
     */
    private static byte[] generateRandom(int num) {
        byte[] bytes = new byte[num];
        Random rand = new Random(System.currentTimeMillis());
        rand.nextBytes(bytes);
        return bytes;
    }


    /**
     * ����Hashֵ���㷨ʹ��MD5
     * @param message byte[] ԭʼ����
     * @return byte[] Hashֵ
     */

    private static byte[] makeHashWithMD5(byte[] message) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("NoSuchAlgorithmException:" + ex.toString());
            FileEncrypt.addSecurityLog("����Hashֵ[MD5]����", ex);
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
     * ����Hashֵ���㷨ʹ��SHA1
     * @param message byte[] ԭʼ����
     * @return byte[] Hashֵ
     */

    private static byte[] makeHashWithSHA1(byte[] message) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("NoSuchAlgorithmException:" + ex.toString());
            FileEncrypt.addSecurityLog("����Hashֵ[SHA1]����", ex);
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
     * ���ļ��ж�ȡ֤��
     * @param fileName String ֤��·��
     * @return X509Certificate ֤��
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
//                FileEncrypt.addSecurityLog("���ļ��ж�ȡ����֤����� ", ex);
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
     * ʹ������֤���еĹ�Կ����
     * @param String certFileName ����֤���ļ�ȫ·��
     * @param String fileName �����ܵ��ļ���
     * @param String fileName ���ܺ���ļ���
     * @return int ���ܽ�� 0-> �ɹ�
     *                     1-> �ļ����ܣ���ȡ֤��ʧ��
     *                     2-> �ļ����ܣ�Դ�ļ�������
     *                     3-> �ļ����ܣ���ȡԴ�ļ�����
     *                     4-> �ļ����ܣ�������ݸ�ʽ����
     *                     5-> �ļ����ܣ���Ч�ķ����С
     *                     6-> �ļ����ܣ���Ч�ļ���״̬
     *                     7-> �ļ����ܣ���Ч�ļ�����Կ
     *                     8-> �ļ����ܣ������㷨������
     *                     9-> �ļ����ܣ��㷨�ṩ�߲�����
     *                     10-> �ļ����ܣ����ģʽ��֧��
     *                     11-> �ļ����ܣ����ܺ��ļ�������
     *                     12-> �ļ����ܣ����ܽ��д���ļ�����
     */
    public static int SAPI_Ukey_FEenc(String certFileName, String fileName,
                                      String encryptedFileName) {
        X509Certificate cert = getCertificateFromFile(certFileName);
        if (cert == null) {
            //��ȡʧ��
            return 1;
        }
        // ���4�ֽ����������Ϊ������Կ
        byte[] random = generateRandom(4);
        // ��ȡ�ļ�����
        File file = new File(fileName);
        if (!file.exists()) {
            // �ļ�������
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
            FileEncrypt.addSecurityLog("��ȡ�ļ�[" + file.getName() + "]����", ex);
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

        // ʹ������֤���еù�Կ���������(�����Կ)
        byte[] encryptedKey = null;
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
            cipher.init(Cipher.ENCRYPT_MODE, cert.getPublicKey());
            encryptedKey = cipher.doFinal(random);
        } catch (BadPaddingException ex) {
            System.out.println("BadPaddingException:" + ex.toString());
            FileEncrypt.addSecurityLog("�ļ����ܣ�������ݸ�ʽ����", ex);
            return 4;
        } catch (IllegalBlockSizeException ex) {
            System.out.println("IllegalBlockSizeException:" + ex.toString());
            FileEncrypt.addSecurityLog("�ļ����ܣ���Ч�ķ����С", ex);
            return 5;
        } catch (IllegalStateException ex) {
            System.out.println("IllegalStateException:" + ex.toString());
            FileEncrypt.addSecurityLog("�ļ����ܣ���Ч�ļ���״̬", ex);
            return 6;
        } catch (InvalidKeyException ex) {
            System.out.println("InvalidKeyException:" + ex.toString());
            FileEncrypt.addSecurityLog("�ļ����ܣ���Ч�ļ�����Կ", ex);
            return 7;
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("NoSuchAlgorithmException:" + ex.toString());
            FileEncrypt.addSecurityLog("�ļ����ܣ������㷨������", ex);
            return 8;
        } catch (NoSuchProviderException ex) {
            System.out.println("NoSuchProviderException:" + ex.toString());
            FileEncrypt.addSecurityLog("�ļ����ܣ��㷨�ṩ�߲�����", ex);
            return 9;
        } catch (NoSuchPaddingException ex) {
            System.out.println("NoSuchPaddingException:" + ex.toString());
            FileEncrypt.addSecurityLog("�ļ����ܣ����ģʽ��֧��", ex);
            return 10;
        }
        // ����CSP��OpenSSL���ֽ���ͬ����Ҫ�ߵ��ֽ���
        encryptedKey = invertedOrder(encryptedKey);
        // �����ܺ�������Կ�����ļ���β
        System.arraycopy(encryptedKey, 0, encryptedData, fileLength, 128);
        // �����ܽ��д���ļ�
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
            FileEncrypt.addSecurityLog("�ļ����ܣ����ܺ��ļ�������", ex);
            return 11;
        } catch (IOException ex) {
            ex.printStackTrace();
            FileEncrypt.addSecurityLog("�ļ����ܣ����ܽ��д���ļ�����", ex);
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
     * ʹ������֤���еĹ�Կ����
     * @param String certFielName ����֤���ļ�ȫ·��
     * @param String toEnc ��������(��ͨ�ַ�������ʮ����������)
     * @return String ���ܽ��(ʮ������) �ɹ����ؼ��ܽ����ʧ�ܷ���null
     */
    public static String SAPI_Ukey_IEenc(String certFileName, String toEnc) {
        // ��ȡ֤��
        X509Certificate cert = getCertificateFromFile(certFileName);
        if (cert == null) {
            return null;
        }
        byte[] plain = toEnc.getBytes();
        int length = plain.length;
        int pageSize = 117; // һ��ԭʼ���ݷ����С
        int bufferSize = 128; // һ���������ķ����С
        int result = length / pageSize;
        int remain = length % pageSize;
        byte[] encryptedData = null;
        if (remain == 0) {
            encryptedData = new byte[result * bufferSize];
        } else {
            encryptedData = new byte[(result + 1) * bufferSize];
        }
        // ��ʼ�����ܲ���
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
                    // ����CSP��OpenSSL���ֽ���ͬ����Ҫ�ߵ��ֽ���
                    encryptedBytes = invertedOrder(encryptedBytes);
                    if (Debug) {
                        System.out.println("->-after--<- " + i + " -- " +
                                           HexConversion.hexify_haveSpace(
                                encryptedBytes));
                    }
                    System.arraycopy(encryptedBytes, 0, encryptedData,
                                     i * bufferSize, bufferSize);
                } else {
                    FileEncrypt.addSecurityLog("������ܲ���ʧ�ܣ�", null);
                    return null;
                }
            }
            // �������һ����������
            if (remain != 0) {
                byte[] temp = new byte[remain];
                System.arraycopy(plain, result * pageSize, temp, 0, remain);
                byte[] encryptedBytes = cipher.doFinal(temp);
                // ����CSP��OpenSSL���ֽ���ͬ����Ҫ�ߵ��ֽ���
                encryptedBytes = invertedOrder(encryptedBytes);
                System.arraycopy(encryptedBytes, 0, encryptedData,
                                 result * bufferSize, encryptedBytes.length);
            }
        } catch (BadPaddingException ex) {
            System.out.println("BadPaddingException:" + ex.toString());
            FileEncrypt.addSecurityLog("��Կ���ܣ�������ݸ�ʽ����", ex);
            return null;
        } catch (IllegalBlockSizeException ex) {
            System.out.println("IllegalBlockSizeException:" + ex.toString());
            FileEncrypt.addSecurityLog("��Կ���ܣ���Ч�ķ����С", ex);
            return null;
        } catch (IllegalStateException ex) {
            System.out.println("IllegalStateException:" + ex.toString());
            FileEncrypt.addSecurityLog("��Կ���ܣ���Ч�ļ���״̬", ex);
            return null;
        } catch (InvalidKeyException ex) {
            System.out.println("InvalidKeyException:" + ex.toString());
            FileEncrypt.addSecurityLog("��Կ���ܣ���Ч�ļ�����Կ", ex);
            return null;
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("NoSuchAlgorithmException:" + ex.toString());
            FileEncrypt.addSecurityLog("��Կ���ܣ������㷨������", ex);
            return null;
        } catch (NoSuchProviderException ex) {
            System.out.println("NoSuchProviderException:" + ex.toString());
            FileEncrypt.addSecurityLog("��Կ���ܣ��㷨�ṩ�߲�����", ex);
            return null;
        } catch (NoSuchPaddingException ex) {
            System.out.println("NoSuchPaddingException:" + ex.toString());
            FileEncrypt.addSecurityLog("��Կ���ܣ����ģʽ��֧��", ex);
            return null;
        }
        return  HexConversion.hexify(encryptedData);
    }

    /**
     * ʹ������֤���еĹ�Կ��֤ǩ����ǩ����ʽ��MD5_RSA_PKCS1��
     * @param certFileName  ����֤���ļ�ȫ·��
     * @param signData      ԭʼ����(ʮ������)
     * @param signedData    ǩ���������(ʮ������)
     * @return int          ��֤ǩ����� 0->�ɹ�
     *                      1-> ��֤ǩ������ȡ֤��ʧ��
     *                      2-> ��֤ǩ����������ݸ�ʽ����
     *                      3-> ��֤ǩ������Ч�ķ����С
     *                      4-> ��֤ǩ������Ч�ļ���״̬
     *                      5-> ��֤ǩ������Ч�ļ�����Կ
     *                      6-> ��֤ǩ���������㷨������
     *                      7-> ��֤ǩ�����㷨�ṩ�߲�����
     *                      8-> ��֤ǩ�������ģʽ��֧��
     *                      9-> ��֤ǩ��������ժҪ��һ��
     */
    public static int SAPI_Ukey_Verify(String certFileName, String signData,
                                       String signedData) {
        // ��ȡ֤��
        X509Certificate cert = getCertificateFromFile(certFileName);
        if (cert == null) {
            return 1;
        }
        // ��֤ǩ��
        byte[] sign = HexConversion.parseHexString(signedData);
        // ����CSP��OpenSSL���ֽ���ͬ����Ҫ�ߵ��ֽ���
        sign = invertedOrder(sign);
        byte[] decryptData = null;
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
            cipher.init(Cipher.DECRYPT_MODE, cert);
            decryptData = cipher.doFinal(sign);
        } catch (BadPaddingException ex) {
            ex.printStackTrace();
            System.out.println("BadPaddingException:" + ex.toString());
            FileEncrypt.addSecurityLog("��֤ǩ����������ݸ�ʽ����", ex);
            return 2;
        } catch (IllegalBlockSizeException ex) {
            System.out.println("IllegalBlockSizeException:" + ex.toString());
            FileEncrypt.addSecurityLog("��֤ǩ������Ч�ķ����С", ex);
            return 3;
        } catch (IllegalStateException ex) {
            System.out.println("IllegalStateException:" + ex.toString());
            FileEncrypt.addSecurityLog("��֤ǩ������Ч�ļ���״̬", ex);
            return 4;
        } catch (InvalidKeyException ex) {
            System.out.println("InvalidKeyException:" + ex.toString());
            FileEncrypt.addSecurityLog("��֤ǩ������Ч�ļ�����Կ", ex);
            return 5;
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("NoSuchAlgorithmException:" + ex.toString());
            FileEncrypt.addSecurityLog("��֤ǩ���������㷨������", ex);
            return 6;
        } catch (NoSuchProviderException ex) {
            System.out.println("NoSuchProviderException:" + ex.toString());
            FileEncrypt.addSecurityLog("��֤ǩ�����㷨�ṩ�߲�����", ex);
            return 7;
        } catch (NoSuchPaddingException ex) {
            System.out.println("NoSuchPaddingException:" + ex.toString());
            FileEncrypt.addSecurityLog("��֤ǩ�������ģʽ��֧��", ex);
            return 8;
        }
        // ��������������Դ�������ݣ���Ҫ������ܺ�����
        if (decryptData != null) {
            if (decryptData.length > 16) {
                byte[] verifyData = new byte[16];
                System.arraycopy(decryptData, decryptData.length - 16,
                                 verifyData, 0, 16);
                decryptData = verifyData;
            }
        }
        // ����HashֵMD5
        byte[] plain = HexConversion.parseHexString(signData);
        byte[] md5 = makeHashWithMD5(plain);
        if (Debug) {
            System.out.println("MD5 == " + HexConversion.hexify_haveSpace(md5));
            System.out.println("decryptData == " +
                               HexConversion.hexify_haveSpace(decryptData));
        }
        // �Ƚ�Hashֵ
        if (Arrays.equals(md5, decryptData)) {
            return 0;
        } else {
            return 9;
        }
    }
    /**
     * ������־
     * @param message  ��־��Ϣ
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
     * ���ݸ�����ʱ�����ͣ����ָ��ʱ���ʽ
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
     * ���ݸ�����key��������data�������Ԥ��
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
     * ���������ֽ���������������
     * @param src     ԭʼ�ֽ�����
     * @return byte[] �������ֽ�����
     */
    private static byte[] invertedOrder(byte[] src){
        byte[] ret = new byte[src.length];
        int index = src.length - 1;
        for (int i = 0; i < src.length; i++) {
            ret[i] = src[index - i];
        }
        return ret;
    }
    // ���Ժ���
    public static void main(String[] args) {
        String certFileName = "F:/workspace/psoms_ln/src/wdkey/test.cer";
        //��֤ǩ��=
        String plain_Str = "USER_1282113554156_794320130613";
        String signed_str = "668079D7B0263C55035FB02D40A8F0A7B9E82474E7755A91C3BE536814577576E7915376A60E9A1F66F40AD1F4DE4929F3B94D6E4E030000A45FA157E846D7EEAB440269221A8BB381FF3835FC0AA4F5C5D2288DE095B9AC5D90EDE7C31DE5A63D339EC51F56BF79A1D78F959962544F72F4E7BC4E27BB04F6BB7E62080ACC2F";
        int ret = SAPI_Ukey_Verify(certFileName, HexConversion.parseStringToHexString(plain_Str), signed_str);
        System.out.println("ret = " + ret);

        // ��Կ����
        String encryptedData = SAPI_Ukey_IEenc(certFileName, plain_Str);
        System.out.println("SAPI_Ukey_IEenc = " +encryptedData);

        //�ļ�����
        String fileName = "D:\\AVR-170529224017.pdf";
        String encryptedFileName = "D:\\����.pdf";
        ret = SAPI_Ukey_FEenc(certFileName, fileName, encryptedFileName);
        System.out.println("SAPI_Ukey_FEenc = " + ret);
    }
}
