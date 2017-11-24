package kd.idp.security.password;

/**
 * ���ܹ��ߣ��������ַ������м��ܣ�Ҫ��ָ������ַ�������
 * �����㷨������Կ��16����
 * ���ܷ���encryptToDES(String info)
 * ���ܷ���decryptByDES(String sInfo)
 * @author li
 *
 */
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

class EncryptTool {
	// ���� �����㷨,���� DES,DESede,Blowfish
	private String Algorithm = "DES";
	private byte[] keyData = {33,17,7,29,71,45,96,85};		// ���� ��Կ����
	private SecretKey createSecretKey() {
		// ����KeyGenerator����
		try{
			DESKeySpec desKeySpec = new DESKeySpec(keyData);
			SecretKeyFactory factory = SecretKeyFactory.getInstance(Algorithm);
			SecretKey secretkey = factory.generateSecret(desKeySpec);
			return secretkey;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * �����ܳ׽���DES����
	 * @param info Ҫ���ܵ���Ϣ
	 * @return String ���ܺ����Ϣ
	 */
	public String encryptToDES(String info) {
		// ��������������� (RNG),(���Բ�д)
		SecureRandom sr = new SecureRandom();
		// ����Ҫ���ɵ�����
		byte[] cipherByte = null;
		SecretKey key = createSecretKey();
		try {
			// �õ�����/������
			Cipher c1 = Cipher.getInstance(Algorithm);
			// ��ָ������Կ��ģʽ��ʼ��Cipher����
			// ����:(ENCRYPT_MODE, DECRYPT_MODE, WRAP_MODE,UNWRAP_MODE)
			c1.init(Cipher.ENCRYPT_MODE, key, sr);
			// ��Ҫ���ܵ����ݽ��б��봦��,
			cipherByte = c1.doFinal(info.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		// �������ĵ�ʮ��������ʽ
		return byte2hex(cipherByte);
	}

	/**
	 * �����ܳ׽���DES���� 
	 * @param sInfo Ҫ���ܵ�����
	 * @return String ���ؽ��ܺ���Ϣ
	 */
	public String decryptByDES(String sInfo) {
		// ��������������� (RNG)
		SecureRandom sr = new SecureRandom();
		byte[] cipherByte = null;
		SecretKey key = createSecretKey();
		try {
			// �õ�����/������
			Cipher c1 = Cipher.getInstance(Algorithm);
			// ��ָ������Կ��ģʽ��ʼ��Cipher����
			c1.init(Cipher.DECRYPT_MODE, key, sr);
			// ��Ҫ���ܵ����ݽ��б��봦��
			cipherByte = c1.doFinal(hex2byte(sInfo));
		} catch (Exception e) {
			e.printStackTrace();
		}
		// return byte2hex(cipherByte);
		return new String(cipherByte);
	}

	/**
	 * ��������ת��Ϊ16�����ַ���
	 * 
	 * @param b
	 *            �������ֽ�����
	 * @return String
	 */
	private String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1) {
				hs = hs + "0" + stmp;
			} else {
				hs = hs + stmp;
			}
		}
		return hs.toUpperCase();
	}

	/**
	 * ʮ�������ַ���ת��Ϊ2����
	 * 
	 * @param hex
	 * @return
	 */
	private byte[] hex2byte(String hex) {
		byte[] ret = new byte[8];
		byte[] tmp = hex.getBytes();
		for (int i = 0; i < 8; i++) {
			ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
		}
		return ret;
	}

	/**
	 * ������ASCII�ַ��ϳ�һ���ֽڣ� �磺"EF"--> 0xEF
	 * 
	 * @param src0
	 *            byte
	 * @param src1
	 *            byte
	 * @return byte
	 */
	public static byte uniteBytes(byte src0, byte src1) {
		byte _b0 = Byte.decode("0x" + new String(new byte[] { src0 }))
				.byteValue();
		_b0 = (byte) (_b0 << 4);
		byte _b1 = Byte.decode("0x" + new String(new byte[] { src1 }))
				.byteValue();
		byte ret = (byte) (_b0 ^ _b1);
		return ret;
	}

	public static void main(String[] args) {
		EncryptTool des = new EncryptTool();
		// ����һ��DES�㷨���ܳ�
		// ���ܳ׼�����Ϣ"Hello"
		String str1 = des.encryptToDES("code111");
		System.out.println("ʹ��des������ϢHelloΪ:" + str1);
		// 02E7AADB2E1DBCF6
		// ʹ������ܳ׽���
		String str2 = des.decryptByDES(str1);
		System.out.println("���ܺ�Ϊ��" + str2);
	}
}
