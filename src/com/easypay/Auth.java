package com.easypay;

import net.sf.json.JSONObject;
import org.apache.commons.codec.binary.Base64;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Auth {

    //标记生产还是测试环境
    public static boolean isTest = true;

    //根据接口文档生成对应的json请求字符串
    private static String biz_content = "";

    //接口文档中的方法名
    private static String service = "trade.auth.preauth";

    //商户号
    private static String merchant_id = KeyUtils.TEST_DEFAULT_MERCHANT_ID;

    //接入机构号
    private static String partner = KeyUtils.TEST_DEFAULT_PARTNER;

    //请求地址
    private static String url = KeyUtils.DEFAULT_URL;

    //key密钥
    private static String key = KeyUtils.TEST_MERCHANT_PRIVATE_KEY;

    //加密密钥
    private static String DES_ENCODE_KEY = KeyUtils.TEST_DES_ENCODE_KEY;

    private static String getEncode(String data){
        return StringUtils.bytesToHexStr(DesUtil.desEncode(data, DES_ENCODE_KEY));
    }

    //三、四要素鉴权认证
    public static void authentication(String name, String mobile, String idNo, String acc){
        JSONObject sParaTemp = new JSONObject();
        sParaTemp.put("merchant_id", merchant_id);
        sParaTemp.put("name", getEncode(name));
        sParaTemp.put("id_no", getEncode(idNo));
        sParaTemp.put("acc", getEncode(acc));   //民生
        sParaTemp.put("mobile", getEncode(mobile));
        sParaTemp.put("out_trade_no", KeyUtils.getOutTradeNo());

        biz_content = sParaTemp.toString();
        service  = "easypay.auth.authentication";
    }

    //将file转化成string
    private static String ReaderJson(String filePath)throws IOException {
        //对一串字符进行操作
        StringBuffer fileData = new StringBuffer();
        //
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead=0;
        while((numRead=reader.read(buf)) != -1){
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
        }
        //缓冲区使用完必须关掉
        reader.close();
        return fileData.toString();
    }

    public static boolean isBase64Encode(String content){
        if(content.length()%4!=0){
            return false;
        }
        String pattern = "^[a-zA-Z0-9/+]*={0,2}$";
        return Pattern.matches(pattern, content);
    }


    //识别静态身份证图像上的文字信息
    public static void ocrIDcard(String filePath) throws Exception {
        JSONObject sParaTemp = new JSONObject();
        sParaTemp.put("merchant_id", merchant_id);

        sParaTemp.put("image_str", encodeBase64File(filePath));
//        sParaTemp.put("image_str", ReaderJson(filePath));


        sParaTemp.put("url", "");
        sParaTemp.put("out_trade_no", KeyUtils.getOutTradeNo());

        biz_content = sParaTemp.toString();
        service  = "easypay.auth.ocr.IDcard";
    }

    //人证比对
    public static void verfyFaceAndIDNoByCop(String name, String idNo, String filePath) throws Exception {
        JSONObject sParaTemp = _verfyFaceAndIDNoByCop(name, idNo, filePath);
        sParaTemp.put("image_str", encodeBase64File(filePath));
        sParaTemp.put("url", "");
        biz_content = sParaTemp.toString();
        service  = "easypay.auth.verify.faceAndIdNoByCop";
    }

    //人证比对 配合手机调用SDK 生成protobuf文件
    public static void verfyFaceAndIDNoByCopSDK(String name, String idNo, String filePath) throws Exception {
        JSONObject sParaTemp = _verfyFaceAndIDNoByCop(name, idNo, filePath);
        sParaTemp.put("liveness_data_file_str", encodeBase64File(filePath));
        sParaTemp.put("liveness_data_url", "");
        biz_content = sParaTemp.toString();
        service  = "easypay.auth.verify.faceAndIdNoByCopSDK";
    }

    public static JSONObject _verfyFaceAndIDNoByCop(String name, String idNo, String filePath) throws Exception {
        JSONObject sParaTemp = new JSONObject();
        sParaTemp.put("merchant_id", merchant_id);
        sParaTemp.put("name", getEncode(name));
        sParaTemp.put("id_no", getEncode(idNo));
        sParaTemp.put("out_trade_no", KeyUtils.getOutTradeNo());
        return sParaTemp;
    }


    /**
     * <p>将文件转成base64 字符串</p>
     * @param path 文件路径
     * @return
     * @throws Exception
     */
    public static String encodeBase64File(String path) throws Exception {
        File file = new File(path);
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int)file.length()];
        inputFile.read(buffer);
        inputFile.close();
        return new Base64().encodeToString(buffer);
    }

    public static void main(String[] args) {
        //易生请求示例子
        try {

            //系统入件之后生成的合作伙伴ID（一般会通过邮件发送）
            if (!isTest) {
                //商户号
                merchant_id = KeyUtils.SC_DEFAULT_MERCHANT_ID;
                //接入机构号
                partner = KeyUtils.SC_DEFAULT_PARTNER;
                //请求地址
                url = KeyUtils.SC_URL;
                //key密钥
                key = KeyUtils.SC_MERCHANT_PRIVATE_KEY;
                //加密密钥
                DES_ENCODE_KEY = KeyUtils.SC_DES_ENCODE_KEY;
            }

            //三四要素鉴权
//            Auth.authentication("黄亮", "13201117161", "11302119731003111","1114850293021111");

            //OCR 身份证照片识别
//            Auth.ocrIDcard("D:/handhold.jfif");

            //人证对比SDK
//            Auth.verfyFaceAndIDNoByCopSDK("黄亮", "11302119731003111", "D:/proto_buf_file");

            //人证对比
            Auth.verfyFaceAndIDNoByCop("黄亮", "11302119731003111", "C:\\Users\\suhtc\\Pictures\\Camera Roll\\self.jpg");



            //加密类型，默认RSA
            String sign_type = KeyUtils.TEST_DEFAULT_ENCODE_TYPE;
            //编码类型
            String charset = KeyUtils.TEST_DEFAULT_CHARSET;

            //根据请求参数生成的机密串
            String sign = KeyUtils.getSign(key, charset, biz_content);
            System.out.print("计算签名数据为：" + sign + "\n");
            Map<String, String> reqMap = new HashMap<String, String>(6);
            reqMap.put("biz_content", biz_content);
            reqMap.put("service", service);
            reqMap.put("partner", partner);
            reqMap.put("sign_type", sign_type);
            reqMap.put("charset", charset);
            reqMap.put("sign", sign);

            StringBuilder resultStrBuilder = new StringBuilder();
            int ret = HttpConnectUtils.sendRequest(url, KeyUtils.TEST_DEFAULT_CHARSET, reqMap, 30000, 60000, "POST", resultStrBuilder, null);
            System.out.print(" \n请求地址为：" + url +
                    "\n 请求结果为：" + ret +
                    "\n 请求参数为：" + reqMap.toString() +
                    "\n 返回内容为：" + resultStrBuilder.toString() + "\n");
        }catch (Exception e){
            if(e != null){
                System.out.print(e.getMessage()+ "\n");
            }else {
                System.out.print("-----其他未知错误--------"+ "\n");
            }
        }
    }
}
