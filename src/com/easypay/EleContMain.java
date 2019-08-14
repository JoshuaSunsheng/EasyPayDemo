package com.easypay;

import net.sf.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

/**
 * 电子签约测试
 * @author njp
 *
 */
public class EleContMain {
	
	//标记生产还是测试环境
    public static boolean isTest = true;

    //根据接口文档生成对应的json请求字符串
    private static String biz_content = "";

    //接口文档中的方法名
    private static String service = "easypay.elecont.createCont";

    //商户号
    private static String merchant_id = KeyUtils.TEST_DEFAULT_MERCHANT_ID;

    //接入机构号
    private static String partner = KeyUtils.TEST_DEFAULT_PARTNER;

    //请求地址
    private static String url = KeyUtils.DEFAULT_URL;

    //商户私钥
    private static String key = KeyUtils.TEST_MERCHANT_PRIVATE_KEY;

    //易生公钥
    private static String easypay_pub_key = KeyUtils.TEST_EASYPAY_PUBLIC_KEY;

    //加密密钥
    private static String DES_ENCODE_KEY = "j9q1x7DQaAGqEtyQNjqgzRKD";

    //协议预览
    public static void protocolPreview(){
        JSONObject sParaTemp = new JSONObject();
        sParaTemp.put("merchant_id", merchant_id);
        sParaTemp.put("model_name", "双方协议模板test");
        sParaTemp.put("idno", "");
        sParaTemp.put("idno_type", "");
        biz_content = sParaTemp.toString();

        service  = "easypay.elecont.protocolPreview";
    }
    
    //发送短信
    public static void sendSMS(){
        JSONObject sParaTemp = new JSONObject();
        sParaTemp.put("merchant_id", merchant_id);
        sParaTemp.put("random_code", "00");
        sParaTemp.put("model_name", "双方协议模板test");
        sParaTemp.put("idno", "");
        sParaTemp.put("idno_type", "");
        biz_content = sParaTemp.toString();

        service  = "easypay.elecont.sendSMS";
    }
    
    //创建合同
    public static void createCont(){
        JSONObject sParaTemp = new JSONObject();
        sParaTemp.put("merchant_id", merchant_id);
        sParaTemp.put("random_code", "00");
        sParaTemp.put("check_code", "000000");
        sParaTemp.put("location", "172.168.3.21");
        sParaTemp.put("out_trade_no", KeyUtils.getOutTradeNo());
        sParaTemp.put("contract_name", "双方测试协议");
        sParaTemp.put("model_name", "双方协议模板test");
        sParaTemp.put("idno", "");
        sParaTemp.put("idno_type", "");
        biz_content = sParaTemp.toString();

        service  = "easypay.elecont.createContract";
    }
    
    //合同查询
    public static void queryCont(String contractNo) {
 	   JSONObject sParaTemp = new JSONObject();
 	   sParaTemp.put("merchant_id",merchant_id);
   	   sParaTemp.put("contract_no", contractNo);
   	   
   	   biz_content = sParaTemp.toString();
   	   
   	 service = "easypay.elecont.queryCont";
 	   
    }   
    
    //下载合同
   public static void downloadCont(String contractNo) {
	    JSONObject sParaTemp = new JSONObject();
     	sParaTemp.put("merchant_id",merchant_id);
   	    sParaTemp.put("contract_no", contractNo);
   	    
   	    biz_content = sParaTemp.toString();
   	   
     	service = "easypay.elecont.downloadCont";
   }
   
    private static String getEncode(String data){
        return StringUtils.bytesToHexStr(DesUtil.desEncode(data, DES_ENCODE_KEY));
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
                //商户私钥
                key = KeyUtils.SC_MERCHANT_PRIVATE_KEY;
                //易生公钥
                easypay_pub_key = KeyUtils.SC_EASYPAY_PUBLIC_KEY;
                //加密密钥
                DES_ENCODE_KEY = KeyUtils.SC_DES_ENCODE_KEY;
            }

            //协议预览
//            protocolPreview();
            //发送短信
//            sendSMS();
            //创建合同
//            createCont();
            //合同查询
//            queryCont("JS20190814000000015");
            //下载合同
            downloadCont("JS20190814000000015");
           
           
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
            //易生公钥验证返回签名
            StringUtils.rsaVerifySign(resultStrBuilder, easypay_pub_key);
        }catch (Exception e){
            System.out.print(e.getMessage()+ "\n");
        }
    }

}
