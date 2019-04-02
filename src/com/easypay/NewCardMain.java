package com.easypay;

import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

//新无卡测试demo
public class NewCardMain {

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

    //新无卡-协议支付-账户认证
    public static void validateAccount(){
        JSONObject sParaTemp = new JSONObject();
        sParaTemp.put("merchant_id", merchant_id);
        sParaTemp.put("name", getEncode("全渠道"));    //账户姓名
        sParaTemp.put("id_no", getEncode("100030033330030044")); //身份证号
        sParaTemp.put("bank_code", "999");//网联测试专用银行(银行编号请见协议支付在线文档)
        sParaTemp.put("acc", getEncode("6226188887788788"));   //银行卡号
        sParaTemp.put("mobile", getEncode("137666666666")); //手机号
        sParaTemp.put("out_trade_no", "demo" + KeyUtils.getOutTradeNo());
        sParaTemp.put("channel_type", 8);
        biz_content = sParaTemp.toString();
        
        service  = "easypay.pay.agreement.validate";
    }


    private static String getEncode(String data){
        return StringUtils.bytesToHexStr(DesUtil.desEncode(data, DES_ENCODE_KEY));
    }

    //新无卡-协议支付-账户签约
    public static void agreementPayBind(String orig_out_trade_no, String sms_code,int channel_type){
        JSONObject sParaTemp = new JSONObject();
        sParaTemp.put("merchant_id", merchant_id);
        sParaTemp.put("orig_out_trade_no", orig_out_trade_no);
        sParaTemp.put("sms_code", sms_code);
        sParaTemp.put("channel_type", channel_type);//7 银联  	8 网联

        biz_content = sParaTemp.toString();
        service  = "easypay.pay.agreement.bind";
    }

    //新无卡-协议支付-支付
    public static void agreementPay(String sign_no,int channelType){
        JSONObject sParaTemp = new JSONObject();
        sParaTemp.put("merchant_id", merchant_id);
        sParaTemp.put("seller_email", "18679106330@gmail.com");
        sParaTemp.put("amount", "1");
        sParaTemp.put("business_time", "2019-04-02 10:55:00");
        sParaTemp.put("notify_url", "https://www.baidu.com");
        sParaTemp.put("order_desc", "Echannell");
        sParaTemp.put("out_trade_no", "demo" + KeyUtils.getOutTradeNo());
        sParaTemp.put("sign_no", sign_no);
        sParaTemp.put("channel_type", channelType);
        
        biz_content = sParaTemp.toString();
        service  = "easypay.pay.agreement";
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

            //新无卡-协议支付-账户认证
//            NewCardMain.validateAccount();

            //新无卡-协议支付-账户签约
//            NewCardMain.agreementPayBind("demo201904021554170171464", "632918",8);

            //新无卡-协议支付-支付
            NewCardMain.agreementPay("170453104",8);

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