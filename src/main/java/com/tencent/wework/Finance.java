package com.tencent.wework;

/* sdk��������
typedef struct Slice_t {
    char* buf;
    int len;
} Slice_t;

typedef struct MediaData {
    char* outindexbuf;
    int out_len;
    char* data;
    int data_len;
    int is_finish;
} MediaData_t;
*/

public class Finance {
    public native static long NewSdk();

    public native static int Init(long sdk, String corpid, String secret);

    public native static int GetChatData(long sdk, long seq, long limit, String proxy, String passwd, long timeout, long chatData);

    public native static int GetMediaData(long sdk, String indexbuf, String sdkField, String proxy, String passwd, long timeout, long mediaData);

    public native static int DecryptData(long sdk, String encrypt_key, String encrypt_msg, long msg);

    public native static void DestroySdk(long sdk);

    public native static long NewSlice();

    public native static void FreeSlice(long slice);

    public native static String GetContentFromSlice(long slice);

    public native static int GetSliceLen(long slice);

    public native static long NewMediaData();

    public native static void FreeMediaData(long mediaData);

    public native static String GetOutIndexBuf(long mediaData);

    public native static byte[] GetData(long mediaData);

    public native static int GetIndexLen(long mediaData);

    public native static int GetDataLen(long mediaData);

    public native static int IsMediaDataFinish(long mediaData);

    static {
        if (isWindows()) {
            String path = Finance.class.getResource("").getPath().replaceAll("%20", " ").replaceFirst("/", "").replace("/", "\\\\");
            //����˳��Ҫ��
            System.load(path.concat("libcrypto-1_1-x64.dll"));
            System.load(path.concat("libcurl-x64.dll"));
            System.load(path.concat("libssl-1_1-x64.dll"));
            //   System.load(path.concat("libcurl.dll"));
            System.load(path.concat("WeWorkFinanceSdk.dll"));
        } else {
            String path = Finance.class.getResource("").getPath();
            System.load(path.concat("libWeWorkFinanceSdk_Java.so"));
        }

    }

    private static boolean isWindows() {
        String osName = System.getProperties().getProperty("os.name");
        System.out.println("current system is " + osName);
        return osName.toUpperCase().indexOf("WINDOWS") != -1;
    }
}
