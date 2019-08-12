package com.easypay;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class DesUtil {
    /**
     * 卡盟加密工具类
     * 3des加密后，使用base64，utf8编码做加密。
     * @author Administrator
     *
     */
    /** 编码字符集 **/
    private static String CHAR_ENCODING = "UTF-8";

    private static final String ALGORITHM = "DES";

    private static final int CACHE_SIZE = 1024;

    /**
     * Base64编码
     * @param key
     * @param data
     * @return
     */
    public static String encode(String key, String data) {
        try {
            byte[] keyByte = key.getBytes(CHAR_ENCODING);
            byte[] dataByte = data.getBytes(CHAR_ENCODING);
            byte[] valueByte = DesUtil.des3Encryption(keyByte, dataByte);
            String value = new String(Base64.encodeBase64(valueByte), CHAR_ENCODING);
            return value;
        } catch (Exception e) {
            return null;
        }
    }

    public static String decode(String key, String data) {
        try {
            byte[] keyByte = key.getBytes(CHAR_ENCODING);
            byte[] dataByte = Base64.decodeBase64(data.getBytes(CHAR_ENCODING));
            byte[] valueByte = DesUtil.des3Decryption(keyByte, dataByte);
            String value = new String(valueByte);
            return value;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * des3Encryption加密
     * @param key
     * @param data
     * @return
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws IllegalStateException
     */
    public static byte[] des3Encryption(byte[] key, byte[] data) throws
            NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException, IllegalStateException {
        final String Algorithm = "DESede";

        SecretKey deskey = new SecretKeySpec(key, Algorithm);

        Cipher c1 = Cipher.getInstance(Algorithm);
        c1.init(Cipher.ENCRYPT_MODE, deskey);
        return c1.doFinal(data);
    }

    /**
     *
     * 方法描述：3des 解密
     * 创建人：huangxue
     * 创建时间：2017-3-21 下午2:59:27
     * @param key
     * @param data
     * @return
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws IllegalStateException: byte[]
     * 修改备注：
     * @version
     */
    public static byte[] des3Decryption(byte[] key, byte[] data) throws
            NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException, IllegalStateException {
        try {
            final String Algorithm = "DESede";
            // 生成密钥
            SecretKey deskey = new SecretKeySpec(key, Algorithm);

            // 解密
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.DECRYPT_MODE, deskey);
            return c1.doFinal(data);
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }
    
  
	/**
	 * 单des加密
	 * java 使用ECB/PKCS5Padding
     * C# 使用ECB   PKCS7
	 * @param data
	 * @param key
	 * @return
	 */
	public static byte[] desEncode(String data, String key) {
		try {
			Cipher cipher = getCipher(Cipher.ENCRYPT_MODE, key);// 加密
			return cipher.doFinal(data.getBytes("UTF-8"));
		} catch (Exception e) {
			return null;
		}
	}


    /**
     * 单des文件加密
     *
     * @param key
     * @param sourceFilePath
     * @param destFilePath
     * @throws Exception
     */
    public static void encryptFile(String key, String sourceFilePath, String destFilePath) throws Exception {
        File sourceFile = new File(sourceFilePath);
        File destFile = new File(destFilePath);
        if (sourceFile.exists() && sourceFile.isFile()) {
            if (!destFile.getParentFile().exists()) {
                destFile.getParentFile().mkdirs();
            }
            destFile.createNewFile();
            InputStream in = new FileInputStream(sourceFile);
            OutputStream out = new FileOutputStream(destFile);
//            Key k = toKey(key);
//            Cipher cipher = Cipher.getInstance(ALGORITHM);
//            cipher.init(Cipher.ENCRYPT_MODE, k);
            Cipher cipher = getCipher(Cipher.ENCRYPT_MODE, key);// 加密
            CipherInputStream cin = new CipherInputStream(in, cipher);
            byte[] cache = new byte[CACHE_SIZE];
            int nRead = 0;
            while ((nRead = cin.read(cache)) != -1) {
                out.write(cache, 0, nRead);
                out.flush();
            }
            out.close();
            cin.close();
            in.close();
        }
    }

	/**
	 * 单des解密
	 * 
	 * @param data
	 * @param key
	 * @return
	 */
	public static String desDecode(String data, String key) {
		try {
			Cipher cipher = getCipher(Cipher.DECRYPT_MODE, key);// 解密
			return new String(cipher.doFinal(StringUtils.hexStrToBytes(data)));
		} catch (Exception e) {
			return null;
		}
	}

	private static Cipher getCipher(int mode, String key)
			throws NoSuchPaddingException, NoSuchAlgorithmException,
			InvalidKeyException, BadPaddingException,
			IllegalBlockSizeException, IllegalStateException,
			UnsupportedEncodingException, InvalidKeySpecException {
		DESKeySpec keySpec = new DESKeySpec(key.getBytes(CHAR_ENCODING));
//		Cipher cipher = Cipher.getInstance("DES"); //默认ECB/PKCS5Padding
		Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
//        java的PKCS5Padding 对应C#的des.Padding = PaddingMode.PKCS7;
//        还需要设置C#的des.Mode = CipherMode.ECB;
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		cipher.init(mode, keyFactory.generateSecret(keySpec));
		return cipher;
	}


    /**
     * 解密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(byte[] data, byte[] key) throws Exception {
        Key k = toKey(key);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, k);
        return cipher.doFinal(data);
    }

    /**
     * 转换密钥
     *
     * @param key
     * @return
     * @throws Exception
     */
    private static Key toKey(byte[] key) throws Exception {
        DESKeySpec dks = new DESKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        SecretKey secretKey = keyFactory.generateSecret(dks);
        return secretKey;
    }
}
