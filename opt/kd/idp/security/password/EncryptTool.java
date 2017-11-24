package kd.idp.security.password;

/**
 * 加密工具，对输入字符串进行加密，要求指定输出字符串长度
 * 加密算法返回密钥的16进制
 * 加密方法encryptToDES(String info)
 * 解密方法decryptByDES(String sInfo)
 * @author li
 *
 */
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

class EncryptTool {
	// 定义 加密算法,可用 DES,DESede,Blowfish
	private String Algorithm = "DES";
	private byte[] keyData = {33,17,7,29,71,45,96,85};		// 声明 密钥对象
	private SecretKey createSecretKey() {
		// 声明KeyGenerator对象
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
	 * 根据密匙进行DES加密
	 * @param info 要加密的信息
	 * @return String 加密后的信息
	 */
	public String encryptToDES(String info) {
		// 加密随机数生成器 (RNG),(可以不写)
		SecureRandom sr = new SecureRandom();
		// 定义要生成的密文
		byte[] cipherByte = null;
		SecretKey key = createSecretKey();
		try {
			// 得到加密/解密器
			Cipher c1 = Cipher.getInstance(Algorithm);
			// 用指定的密钥和模式初始化Cipher对象
			// 参数:(ENCRYPT_MODE, DECRYPT_MODE, WRAP_MODE,UNWRAP_MODE)
			c1.init(Cipher.ENCRYPT_MODE, key, sr);
			// 对要加密的内容进行编码处理,
			cipherByte = c1.doFinal(info.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 返回密文的十六进制形式
		return byte2hex(cipherByte);
	}

	/**
	 * 根据密匙进行DES解密 
	 * @param sInfo 要解密的密文
	 * @return String 返回解密后信息
	 */
	public String decryptByDES(String sInfo) {
		// 加密随机数生成器 (RNG)
		SecureRandom sr = new SecureRandom();
		byte[] cipherByte = null;
		SecretKey key = createSecretKey();
		try {
			// 得到加密/解密器
			Cipher c1 = Cipher.getInstance(Algorithm);
			// 用指定的密钥和模式初始化Cipher对象
			c1.init(Cipher.DECRYPT_MODE, key, sr);
			// 对要解密的内容进行编码处理
			cipherByte = c1.doFinal(hex2byte(sInfo));
		} catch (Exception e) {
			e.printStackTrace();
		}
		// return byte2hex(cipherByte);
		return new String(cipherByte);
	}

	/**
	 * 将二进制转化为16进制字符串
	 * 
	 * @param b
	 *            二进制字节数组
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
	 * 十六进制字符串转化为2进制
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
	 * 将两个ASCII字符合成一个字节； 如："EF"--> 0xEF
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
		// 生成一个DES算法的密匙
		// 用密匙加密信息"Hello"
		String str1 = des.encryptToDES("code111");
		System.out.println("使用des加密信息Hello为:" + str1);
		// 02E7AADB2E1DBCF6
		// 使用这个密匙解密
		String str2 = des.decryptByDES(str1);
		System.out.println("解密后为：" + str2);
	}
}
