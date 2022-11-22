package project.extension.cryptography;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.nio.charset.Charset;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * RSA工具类
 */
public class RSAUtils {
    /**
     * 字节数据转字符串专用集合
     */
    private static final char[] HEX_CHAR = {'0',
                                            '1',
                                            '2',
                                            '3',
                                            '4',
                                            '5',
                                            '6',
                                            '7',
                                            '8',
                                            '9',
                                            'a',
                                            'b',
                                            'c',
                                            'd',
                                            'e',
                                            'f'};

    /**
     * 随机生成密钥对
     *
     * @param filePath 文件保存地址
     */
    public static void genKeyPair(String filePath)
            throws
            NoSuchAlgorithmException {
        genKeyPair(filePath,
                   Charset.defaultCharset());
    }

    /**
     * 随机生成密钥对
     *
     * @param filePath 文件保存地址
     * @param charset  字符集
     */
    public static void genKeyPair(String filePath,
                                  Charset charset)
            throws
            NoSuchAlgorithmException {
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        // 初始化密钥对生成器，密钥大小为96-2048位
        keyPairGen.initialize(2048,
                              new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        // 得到私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        // 得到公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        try {
            // 得到公钥字符串
            String publicKeyString = Base64Utils.encodeBase64(publicKey.getEncoded(),
                                                              charset);
            // 得到私钥字符串
            String privateKeyString = Base64Utils.encodeBase64(privateKey.getEncoded(),
                                                               charset);
            // 将密钥对写入到文件
            File dir = new File(filePath);
            if (!dir.exists())
                dir.mkdirs();

            try (FileWriter pubfw = new FileWriter(filePath + "/publicKey.keystore")) {
                try (FileWriter prifw = new FileWriter(filePath + "/privateKey.keystore")) {
                    try (BufferedWriter pubbw = new BufferedWriter(pubfw)) {
                        try (BufferedWriter pribw = new BufferedWriter(prifw)) {
                            pubbw.write(publicKeyString);
                            pribw.write(privateKeyString);
                            pribw.flush();
                        }
                        pubbw.flush();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从文件中加载公钥
     *
     * @param path 公钥文件
     * @throws Exception 加载公钥时产生的异常
     */
    public static String loadPublicKeyByFile(String path)
            throws
            Exception {
        try (FileReader fileReader = new FileReader(path + "/publicKey.keystore")) {
            try (BufferedReader br = new BufferedReader(fileReader)) {
                String readLine;
                StringBuilder sb = new StringBuilder();
                while ((readLine = br.readLine()) != null) {
                    sb.append(readLine);
                }
                return sb.toString();
            } catch (IOException e) {
                throw new Exception("公钥数据流读取错误");
            } catch (NullPointerException e) {
                throw new Exception("公钥输入流为空");
            }
        }
    }

    /**
     * 从字符串中加载公钥
     *
     * @param publicKeyStr 公钥数据字符串
     * @return 公钥对象
     * @throws Exception 加载公钥时产生的异常
     */
    public static RSAPublicKey loadPublicKeyByStr(String publicKeyStr)
            throws
            Exception {
        return loadPublicKeyByStr(publicKeyStr,
                                  Charset.defaultCharset());
    }

    /**
     * 从字符串中加载公钥
     *
     * @param publicKeyStr 公钥数据字符串
     * @param charset      字符集
     * @return 公钥对象
     * @throws Exception 加载公钥时产生的异常
     */
    public static RSAPublicKey loadPublicKeyByStr(String publicKeyStr,
                                                  Charset charset)
            throws
            Exception {
        try {
            byte[] buffer = Base64Utils.decodeBase64(publicKeyStr,
                                                     charset);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("公钥非法");
        } catch (NullPointerException e) {
            throw new Exception("公钥数据为空");
        }
    }

    /**
     * 从文件中加载私钥
     *
     * @param path 私钥文件
     * @return 是否成功
     * @throws Exception
     */
    public static String loadPrivateKeyByFile(String path)
            throws
            Exception {
        try {
            BufferedReader br = new BufferedReader(new FileReader(path + "/privateKey.keystore"));
            String readLine = null;
            StringBuilder sb = new StringBuilder();
            while ((readLine = br.readLine()) != null) {
                sb.append(readLine);
            }
            br.close();
            return sb.toString();
        } catch (IOException e) {
            throw new Exception("私钥数据读取错误");
        } catch (NullPointerException e) {
            throw new Exception("私钥输入流为空");
        }
    }

    /**
     * 从字符串中加载私钥
     *
     * @param privateKeyStr 私钥数据字符串
     * @return 私钥对象
     * @throws Exception
     */
    public static RSAPrivateKey loadPrivateKeyByStr(String privateKeyStr)
            throws
            Exception {
        return loadPrivateKeyByStr(privateKeyStr,
                                   Charset.defaultCharset());
    }

    /**
     * 从字符串中加载私钥
     *
     * @param privateKeyStr 私钥数据字符串
     * @param charset       字符集
     * @return 私钥对象
     * @throws Exception
     */
    public static RSAPrivateKey loadPrivateKeyByStr(String privateKeyStr,
                                                    Charset charset)
            throws
            Exception {
        try {
            byte[] buffer = Base64Utils.decodeBase64(privateKeyStr,
                                                     charset);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("私钥非法");
        } catch (NullPointerException e) {
            throw new Exception("私钥数据为空");
        }
    }

    /**
     * 使用公钥加密
     *
     * @param plainTextData 明文
     * @param publicKey     公钥
     * @return 密文
     * @throws Exception 加密过程中的异常信息
     */
    public static byte[] encryptWithPublicKey(byte[] plainTextData,
                                              RSAPublicKey publicKey)
            throws
            Exception {
        if (publicKey == null) {
            throw new Exception("加密公钥为空, 请设置");
        }
        Cipher cipher = null;
        try {
            // 使用默认RSA
            cipher = Cipher.getInstance("RSA");
            // cipher= Cipher.getInstance("RSA", new BouncyCastleProvider());
            cipher.init(Cipher.ENCRYPT_MODE,
                        publicKey);
            byte[] output = cipher.doFinal(plainTextData);
            return output;
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此加密算法");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidKeyException e) {
            throw new Exception("加密公钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            throw new Exception("明文长度非法");
        } catch (BadPaddingException e) {
            throw new Exception("明文数据已损坏");
        }
    }

    /**
     * 使用公钥加密
     *
     * @param plainTextData 明文
     * @param publicKey     公钥
     * @return 密文
     * @throws Exception 加密过程中的异常信息
     */
    public static String encryptWithPublicKeyToString(byte[] plainTextData,
                                                      RSAPublicKey publicKey)
            throws
            Exception {
        return encryptWithPublicKeyToString(plainTextData,
                                            publicKey,
                                            Charset.defaultCharset());
    }

    /**
     * 使用公钥加密
     *
     * @param plainTextData 明文
     * @param publicKey     公钥
     * @param charset       字符集
     * @return 密文
     * @throws Exception 加密过程中的异常信息
     */
    public static String encryptWithPublicKeyToString(byte[] plainTextData,
                                                      RSAPublicKey publicKey,
                                                      Charset charset)
            throws
            Exception {
        return Base64Utils.encodeBase64(encryptWithPublicKey(plainTextData,
                                                             publicKey),
                                        charset);
    }

    /**
     * 使用公钥加密
     *
     * @param plainTextData 明文
     * @param publicKey     公钥
     * @return 密文
     * @throws Exception 加密过程中的异常信息
     */
    public static String encryptWithPublicKeyToString(String plainTextData,
                                                      String publicKey)
            throws
            Exception {
        return encryptWithPublicKeyToString(plainTextData,
                                            publicKey,
                                            Charset.defaultCharset());
    }

    /**
     * 使用公钥加密
     *
     * @param plainTextData 明文
     * @param publicKey     公钥
     * @param charset       字符集
     * @return 密文
     * @throws Exception 加密过程中的异常信息
     */
    public static String encryptWithPublicKeyToString(String plainTextData,
                                                      String publicKey,
                                                      Charset charset)
            throws
            Exception {
        RSAPublicKey _publicKey = loadPublicKeyByStr(publicKey,
                                                     charset);
        byte[] _plainTextData = plainTextData.getBytes(charset);
        return Base64Utils.encodeBase64(encryptWithPublicKey(_plainTextData,
                                                             _publicKey),
                                        charset);
    }

    /**
     * 使用私钥加密
     *
     * @param plainTextData 明文
     * @param privateKey    私钥
     * @return 密文
     * @throws Exception 加密过程中的异常信息
     */
    public static byte[] encryptWithPrivateKey(byte[] plainTextData,
                                               RSAPrivateKey privateKey)
            throws
            Exception {
        if (privateKey == null) {
            throw new Exception("加密私钥为空, 请设置");
        }
        Cipher cipher = null;
        try {
            // 使用默认RSA
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE,
                        privateKey);
            byte[] output = cipher.doFinal(plainTextData);
            return output;
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此加密算法");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidKeyException e) {
            throw new Exception("加密私钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            throw new Exception("明文长度非法");
        } catch (BadPaddingException e) {
            throw new Exception("明文数据已损坏");
        }
    }

    /**
     * 使用私钥加密
     *
     * @param plainTextData 明文
     * @param privateKey    私钥
     * @return 密文
     * @throws Exception 加密过程中的异常信息
     */
    public static String encryptWithPrivateKeyToString(byte[] plainTextData,
                                                       RSAPrivateKey privateKey)
            throws
            Exception {
        return encryptWithPrivateKeyToString(plainTextData,
                                             privateKey,
                                             Charset.defaultCharset());
    }

    /**
     * 使用私钥加密
     *
     * @param plainTextData 明文
     * @param privateKey    私钥
     * @param charset       字符集
     * @return 密文
     * @throws Exception 加密过程中的异常信息
     */
    public static String encryptWithPrivateKeyToString(byte[] plainTextData,
                                                       RSAPrivateKey privateKey,
                                                       Charset charset)
            throws
            Exception {
        return Base64Utils.encodeBase64(encryptWithPrivateKey(plainTextData,
                                                              privateKey),
                                        charset);
    }

    /**
     * 使用私钥加密
     *
     * @param plainTextData 明文
     * @param privateKey    私钥
     * @return 密文
     * @throws Exception 加密过程中的异常信息
     */
    public static String encryptWithPrivateKeyToString(String plainTextData,
                                                       String privateKey)
            throws
            Exception {
        return encryptWithPrivateKeyToString(plainTextData,
                                             privateKey,
                                             Charset.defaultCharset());
    }

    /**
     * 使用私钥加密
     *
     * @param plainTextData 明文
     * @param privateKey    私钥
     * @param charset       字符集
     * @return 密文
     * @throws Exception 加密过程中的异常信息
     */
    public static String encryptWithPrivateKeyToString(String plainTextData,
                                                       String privateKey,
                                                       Charset charset)
            throws
            Exception {
        RSAPrivateKey _privateKey = loadPrivateKeyByStr(privateKey,
                                                        charset);
        byte[] _plainTextData = plainTextData.getBytes(charset);
        return Base64Utils.encodeBase64(encryptWithPrivateKey(_plainTextData,
                                                              _privateKey),
                                        charset);
    }

    /**
     * 使用私钥解密
     *
     * @param cipherData 密文数据
     * @param privateKey 私钥
     * @return 明文
     * @throws Exception 解密过程中的异常信息
     */
    public static byte[] decryptWithPrivateKey(byte[] cipherData,
                                               RSAPrivateKey privateKey)
            throws
            Exception {
        if (privateKey == null) {
            throw new Exception("解密私钥为空, 请设置");
        }
        Cipher cipher = null;
        try {
            // 使用默认RSA
            cipher = Cipher.getInstance("RSA");
            // cipher= Cipher.getInstance("RSA", new BouncyCastleProvider());
            cipher.init(Cipher.DECRYPT_MODE,
                        privateKey);
            byte[] decodeCipherData = Base64Utils.decodeBase64(cipherData);
            byte[] output = cipher.doFinal(decodeCipherData);
            return output;
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此解密算法");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidKeyException e) {
            throw new Exception("解密私钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            throw new Exception("密文长度非法");
        } catch (BadPaddingException e) {
            throw new Exception("密文数据已损坏");
        }
    }

    /**
     * 使用私钥解密
     *
     * @param cipherData 密文数据
     * @param privateKey 私钥
     * @return 明文
     * @throws Exception 解密过程中的异常信息
     */
    public static String decryptWithPrivateKeyToString(byte[] cipherData,
                                                       RSAPrivateKey privateKey,
                                                       Charset charset)
            throws
            Exception {
        return new String(decryptWithPrivateKey(cipherData,
                                                privateKey),
                          charset);
    }

    /**
     * 使用私钥解密
     *
     * @param cipherData 密文数据
     * @param privateKey 私钥
     * @return 明文
     * @throws Exception 解密过程中的异常信息
     */
    public static String decryptWithPrivateKeyToString(String cipherData,
                                                       String privateKey)
            throws
            Exception {
        return decryptWithPrivateKeyToString(cipherData,
                                             privateKey,
                                             Charset.defaultCharset());
    }

    /**
     * 使用私钥解密
     *
     * @param cipherData 密文数据
     * @param privateKey 私钥
     * @param charset    字符集
     * @return 明文
     * @throws Exception 解密过程中的异常信息
     */
    public static String decryptWithPrivateKeyToString(String cipherData,
                                                       String privateKey,
                                                       Charset charset)
            throws
            Exception {
        RSAPrivateKey _privateKey = loadPrivateKeyByStr(privateKey,
                                                        charset);
        byte[] _cipherData = cipherData.getBytes(charset);
        return new String(decryptWithPrivateKey(_cipherData,
                                                _privateKey),
                          charset);
    }

    /**
     * 使用公钥解密
     *
     * @param cipherData 密文数据
     * @param publicKey  公钥
     * @return 明文
     * @throws Exception 解密过程中的异常信息
     */
    public static byte[] decryptWithPublicKey(byte[] cipherData,
                                              RSAPublicKey publicKey)
            throws
            Exception {
        if (publicKey == null) {
            throw new Exception("解密公钥为空, 请设置");
        }
        Cipher cipher = null;
        try {
            // 使用默认RSA
            cipher = Cipher.getInstance("RSA");
            // cipher= Cipher.getInstance("RSA", new BouncyCastleProvider());
            cipher.init(Cipher.DECRYPT_MODE,
                        publicKey);
            byte[] decodeCipherData = Base64Utils.decodeBase64(cipherData);
            byte[] output = cipher.doFinal(decodeCipherData);
            return output;
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此解密算法");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidKeyException e) {
            throw new Exception("解密公钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            throw new Exception("密文长度非法");
        } catch (BadPaddingException e) {
            throw new Exception("密文数据已损坏");
        }
    }

    /**
     * 使用公钥解密
     *
     * @param cipherData 密文数据
     * @param publicKey  公钥
     * @return 明文
     * @throws Exception 解密过程中的异常信息
     */
    public static String decryptWithPublicKeyToString(String cipherData,
                                                      String publicKey)
            throws
            Exception {
        return decryptWithPublicKeyToString(cipherData,
                                            publicKey,
                                            Charset.defaultCharset());
    }

    /**
     * 使用公钥解密
     *
     * @param cipherData 密文数据
     * @param publicKey  公钥
     * @param charset    字符集
     * @return 明文
     * @throws Exception 解密过程中的异常信息
     */
    public static String decryptWithPublicKeyToString(String cipherData,
                                                      String publicKey,
                                                      Charset charset)
            throws
            Exception {
        RSAPublicKey _publicKey = loadPublicKeyByStr(publicKey,
                                                     charset);
        byte[] _cipherData = cipherData.getBytes(charset);
        return new String(decryptWithPublicKey(_cipherData,
                                               _publicKey),
                          charset);
    }

    /**
     * 使用公钥解密
     *
     * @param cipherData 密文数据
     * @param publicKey  公钥
     * @return 明文
     * @throws Exception 解密过程中的异常信息
     */
    public static String decryptWithPublicKeyToString(byte[] cipherData,
                                                      RSAPublicKey publicKey)
            throws
            Exception {
        return decryptWithPublicKeyToString(cipherData,
                                            publicKey,
                                            Charset.defaultCharset());
    }

    /**
     * 使用公钥解密
     *
     * @param cipherData 密文数据
     * @param publicKey  公钥
     * @param charset    字符集
     * @return 明文
     * @throws Exception 解密过程中的异常信息
     */
    public static String decryptWithPublicKeyToString(byte[] cipherData,
                                                      RSAPublicKey publicKey,
                                                      Charset charset)
            throws
            Exception {
        return new String(decryptWithPublicKey(cipherData,
                                               publicKey),
                          charset);
    }

    /**
     * 使用私钥获取签名
     * SHA256
     *
     * @param data       数据
     * @param privateKey 私钥
     * @return 签名
     */
    public static String signWithSHA256(String data,
                                        String privateKey) {
        return signWithSHA256(data,
                              privateKey,
                              Charset.defaultCharset());
    }

    /**
     * 使用私钥获取签名
     * SHA256
     *
     * @param data       数据
     * @param privateKey 私钥
     * @return 签名
     */
    public static String signWithSHA256(String data,
                                        String privateKey,
                                        Charset charset) {
        try {
            RSAPrivateKey _privateKey = loadPrivateKeyByStr(privateKey);
            Signature signature = Signature.getInstance("SHA256WithRSA");
            signature.initSign(_privateKey);
            //data为要生成签名的源数据字节数组
            signature.update(data.getBytes(charset));
            return Base64Utils.encodeBase64(signature.sign(),
                                            charset);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 使用公钥验证签名
     * SHA256
     *
     * @param data      数据
     * @param sign      签名
     * @param publicKey 公钥
     * @return 是否正确
     */
    public static boolean verifySignWithSHA256(String data,
                                               String sign,
                                               String publicKey) {
        return verifySignWithSHA256(data,
                                    sign,
                                    publicKey,
                                    Charset.defaultCharset());
    }

    /**
     * 使用公钥验证签名
     * SHA256
     *
     * @param data      数据
     * @param sign      签名
     * @param publicKey 公钥
     * @param charset   字符集
     * @return 是否正确
     */
    public static boolean verifySignWithSHA256(String data,
                                               String sign,
                                               String publicKey,
                                               Charset charset) {
        try {
            RSAPublicKey _publicKey = loadPublicKeyByStr(publicKey);
            Signature signature = Signature.getInstance("SHA256WithRSA");
            signature.initVerify(_publicKey);
            signature.update(data.getBytes(charset));
            //签名需要做base64解码
            byte[] _sign = Base64Utils.decodeBase64(sign,
                                                    charset);
            return signature.verify(_sign);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 字节数据转十六进制字符串
     *
     * @param data 输入数据
     * @return 十六进制内容
     */
    public static String byteArrayToString(byte[] data) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            // 取出字节的高四位 作为索引得到相应的十六进制标识符 注意无符号右移
            stringBuilder.append(HEX_CHAR[(data[i] & 0xf0) >>> 4]);
            // 取出字节的低四位 作为索引得到相应的十六进制标识符
            stringBuilder.append(HEX_CHAR[(data[i] & 0x0f)]);
            if (i < data.length - 1) {
                stringBuilder.append(' ');
            }
        }
        return stringBuilder.toString();
    }
}
