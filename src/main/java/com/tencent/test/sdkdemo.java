package com.tencent.test;

import com.tencent.wework.Finance;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

import static org.apache.catalina.manager.Constants.CHARSET;


public class sdkdemo {
    public static void main(String[] args) throws Exception {
        args = new String[6];
        args[0] = "1";
        args[1] = "0";
        args[2] = "20";
        //不使用代理
        args[3] = "";
        args[4] = "";
        args[5] = "3";

        long sdk = Finance.NewSdk();
        //初始化函数（企业id，会话存档应用密钥）
        System.out.println(Finance.Init(sdk, "wwd08c8e7c775ab44d", "zJ6k0naVVQ--gt9PUSSEvs03zW_nlDVmjLCTOTAfrew"));
        //私钥
        String priKey =
                "MIIEowIBAAKCAQEAjzJfnYgy8gI/UdR9OMzMh2/Svtz6ynPJdgqWX0qCm7361g04\n" +
                        "Sz/g+aJGtqu8jRJNg3rxQhezoy5mc+//QDyL6sM0auASS+eWx2igxVahLhJEWef3\n" +
                        "2woMrb+OHs6nJf2jBcfA494KvgkxSYXU84vl8UCHwUI839SLBSLWofb8ccFEmy9W\n" +
                        "VSWY7EIUVV8pO8sLZ9uVWK1IrHHSrcGkx4SF913RDIxCTExj7bLqxNRklzWZgi3m\n" +
                        "bYoIsTG6dmsGkmxagGPEtuVOI2UjpjQw1WyrN+6o+2+qpOIuoHKF5vGrPbQBL1jU\n" +
                        "nshGs3MnwTWdxVOe+bw8a+VAqxpD5DlfdKamgQIDAQABAoIBAGNRbe3mPGeMVXyd\n" +
                        "I6kUqrs5PPNyc2OdwVpk53z6QfJhZyu1iZjvmkuqWN9z59f0nNyXlePgapDAqwC4\n" +
                        "sdJM7EKM17tU5HvPCc4O7ItSlYJN2yh8cnVy1+5ekOUfMeFwtPRaYpfpNowt9ghn\n" +
                        "kZbGLlsRBddt6KjaUv3h9vnpQ5hlhU53slh+Zsdrsselpy0sTCF0ulVQgioZ6lzg\n" +
                        "/Y61xCGxRWqq71UT/7EOZwIoD3NmLRZLE6vxm8uBrFvZZ5/jw9Z//8S+vgtRUgGj\n" +
                        "/5v55ftTG+EOsaz2Zvt+jghkaEAchcyy5LyUv09Ir9eOYfjNAgeCk9dQv6E+2BiR\n" +
                        "SvcUJEECgYEA5CvcoSpV66qC1T99/JqmqyTjORZ4ZKBJY9vos4uxNYfCbbhNJLDY\n" +
                        "QAbCC234nBuKQ+3cVKkWtjBwfNp90mEmIoN6sYv+SdtVe2ofhJEqLMTiGyZPi2Iv\n" +
                        "+vvRpFmiAUfWXqFzBy+3HSoTkRqbCytUIT7NXZZ3YwKbbIhyzABTzSkCgYEAoKlf\n" +
                        "Gsn4rQEfmitAGJpuiSahih6KV3K+S9olnPbwd04YOw4u5UmouhyC7N0z2K8AO6zo\n" +
                        "IkzCQtWQS1B1uaPE/stG7I/iy0CI6q46nSly5pajBBGQc+Y5ixRf23hEjGgca556\n" +
                        "1pDyMs0Nb0J2AGDcr8olrbT1KwBvg8oWHOlA4ZkCgYAmC+pONXD+SwBl7qBjbqY8\n" +
                        "A3qgGk8Y+GFEdXbn+XMjKfARu5mhdJuakYXpwfyiizUS/qaut0NCPfGD4Cr62Zgy\n" +
                        "SRo8YMuWJSyr15ZJ1KrjrDDHtiutYkH959+dOBT7ga8NOH6lxB8Ujd+VYopX4nG0\n" +
                        "2XQFFwHxUI36GwaJXcSbgQKBgEe4VERZNTHF9p2UASD6j62aGTLXP1qaVmj2ESRo\n" +
                        "+B/KNPbn9fdVUoUChU/Hz4VDWg9JuLbXHUFIpQl5+ZPNj/tOM3MXKF8jh/t7m57d\n" +
                        "CfX1+P+v95RFihqUFdabcb5cG5PPQ3bVbclP0FeCi7rPgrTWwMsypN91alKivAxb\n" +
                        "9CLBAoGBALfb5SEupOoyIUetiWhOc1vlmP+71rJEXUEYwm0CMmfKZRW+I1A8qTE+\n" +
                        "DVT6iUMVt6jj8L/YiMzjUh8y8HLcp4mlldzdfyjssZg6v2hABJcnID6o3DcTXA8k\n" +
                        "8uMmaZZ0qY4oUzwgZa7bf/C//3mKlRUMoBYOT/LAksZlySIBXoST\n";
        long ret = 0;
        if (args[0].equals("1")) {
            //会话
            int seq = Integer.parseInt(args[1]);
            int limit = Integer.parseInt(args[2]);
            long slice = Finance.NewSlice();
            ret = Finance.GetChatData(sdk, seq, limit, args[3], args[4], Integer.parseInt(args[5]), slice);
            if (ret != 0) {
                System.out.println("getchatdata ret " + ret);
                return;
            }
            System.out.println("getchatdata :" + Finance.GetContentFromSlice(slice));
            Finance.FreeSlice(slice);
        } else if (args[0].equals("2")) {
            //媒体

            String indexbuf = "";
            while (true) {
                long media_data = Finance.NewMediaData();
                ret = Finance.GetMediaData(sdk, indexbuf, args[1], args[2], args[3], Integer.parseInt(args[4]), media_data);
                System.out.println("getmediadata ret:" + ret);
                if (ret != 0) {
                    return;
                }
                System.out.printf("getmediadata outindex len:%d, data_len:%d, is_finis:%d\n", Finance.GetIndexLen(media_data), Finance.GetDataLen(media_data), Finance.IsMediaDataFinish(media_data));
                try {
                    FileOutputStream outputStream = new FileOutputStream(new File(args[5]));
                    outputStream.write(Finance.GetData(media_data));
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (Finance.IsMediaDataFinish(media_data) == 1) {
                    Finance.FreeMediaData(media_data);
                    break;
                } else {
                    indexbuf = Finance.GetOutIndexBuf(media_data);
                    Finance.FreeMediaData(media_data);
                }
            }
        } else if (args[0].equals("3")) {
            // notice!
            // // use prikey to decrpyt get args[1]
            //返回的密钥
            String encrypt_radom_key = "";
            //先获取私钥，在decode转换
            String s = decrypt(encrypt_radom_key, getPrivateKey(priKey));
            //返回的加密文本
            String encrypt_chat_msg = "";
            long msg = Finance.NewSlice();
            ret = Finance.DecryptData(sdk, s, encrypt_chat_msg, msg);
            if (ret != 0) {
                System.out.println("getchatdata ret " + ret);
                return;
            }
            System.out.println("decrypt ret:" + ret + " msg:" + Finance.GetContentFromSlice(msg));
            Finance.FreeSlice(msg);
        } else {
            System.out.println("wrong args " + args[0]);
        }
        Finance.DestroySdk(sdk);
    }


    //获取私钥
    public static PrivateKey getPrivateKey(String key) throws Exception {
        byte[] keyBytes;
        keyBytes = (new BASE64Decoder()).decodeBuffer(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    //解析数据
    public static String decrypt(String data, PrivateKey privateKey) throws Exception {
        //先decode 从base64 转出来
        // byte[] dataBytes = decoder64.decode(data.getBytes(CHARSET));
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] dataBytes = decoder.decodeBuffer(data);
        //在去使用RSA PKCS1算法进行解密（长度默认128，不改会报错）
        byte[] decrypt = decrypt(dataBytes, privateKey, 2048, 11, "RSA/ECB/PKCS1Padding");
        String s1 = new String(decrypt);
        return s1;
    }

    public static byte[] decrypt(byte[] encryptedBytes, PrivateKey privateKey, int keyLength, int reserveSize, String cipherAlgorithm) throws Exception {
        int keyByteSize = keyLength / 8;
        int decryptBlockSize = keyByteSize - reserveSize;
        int nBlock = encryptedBytes.length / keyByteSize;
        ByteArrayOutputStream outbuf = null;
        try {
            Cipher cipher = Cipher.getInstance(cipherAlgorithm);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            outbuf = new ByteArrayOutputStream(nBlock * decryptBlockSize);
            for (int offset = 0; offset < encryptedBytes.length; offset += keyByteSize) {
                int inputLen = encryptedBytes.length - offset;
                if (inputLen > keyByteSize) {
                    inputLen = keyByteSize;
                }
                byte[] decryptedBlock = cipher.doFinal(encryptedBytes, offset, inputLen);
                outbuf.write(decryptedBlock);
            }
            outbuf.flush();
            return outbuf.toByteArray();
        } catch (Exception e) {
            throw new Exception("DEENCRYPT ERROR:", e);
        } finally {
            try {
                if (outbuf != null) {
                    outbuf.close();
                }
            } catch (Exception e) {
                outbuf = null;
                throw new Exception("CLOSE ByteArrayOutputStream ERROR:", e);
            }
        }
    }


}
