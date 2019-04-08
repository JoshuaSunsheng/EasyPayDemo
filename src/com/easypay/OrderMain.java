package com.easypay;

import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

//新无卡测试demo
public class OrderMain {

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

    //二维码下单
    public static void qrcodePayPush(String payType){
        JSONObject sParaTemp = new JSONObject();
        sParaTemp.put("merchant_id", merchant_id);
        sParaTemp.put("seller_email", "18679106330@gmail.com");
        sParaTemp.put("amount", "1");
        sParaTemp.put("business_time", "2017-12-07 15:35:00");
        sParaTemp.put("notify_url", "https://www.baidu.com");
        sParaTemp.put("order_desc", "Echannell");
        sParaTemp.put("pay_type", payType);

        sParaTemp.put("out_trade_no", KeyUtils.getOutTradeNo());
        biz_content = sParaTemp.toString();

        service  = "easypay.qrcode.pay.push";
    }
    //二维码下单
    public static void netBankPay(){
        JSONObject sParaTemp = new JSONObject();
        sParaTemp.put("merchant_id", merchant_id);
        sParaTemp.put("amount", "100");
        sParaTemp.put("business_time", "2017-12-07 15:35:00");
        sParaTemp.put("front_url", "https://www.baidu.com");
        sParaTemp.put("notify_url", "https://www.baidu.com");
        sParaTemp.put("subject", "Echannell");
        sParaTemp.put("body", "商品信息");
        sParaTemp.put("account_type", "1");
        sParaTemp.put("bank_code", "ICBC");

        sParaTemp.put("out_trade_no", KeyUtils.getOutTradeNo());
        biz_content = sParaTemp.toString();

        service  = "easypay.merchant.netBankPay";
    }


    //订单查询
    public static void orderQuery(String out_trade_no){
        JSONObject sParaTemp = new JSONObject();
        sParaTemp.put("merchant_id", "900029000002554");

        sParaTemp.put("out_trade_no", out_trade_no);

        biz_content = sParaTemp.toString();
        service  = "easypay.merchant.query";
    }

    //退款
    public static void refund(String origin_trade_no){
        JSONObject sParaTemp = new JSONObject();
        sParaTemp.put("merchant_id", merchant_id);
        sParaTemp.put("refund_amount", "1");
        sParaTemp.put("out_trade_no", KeyUtils.getOutTradeNo());
        sParaTemp.put("origin_trade_no", origin_trade_no);
        sParaTemp.put("subject", "testRefund");

        biz_content = sParaTemp.toString();
        service  = "easypay.merchant.refund";
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

            //二维码订单推送
//            OrderMain.qrcodePayPush("wxNative");//unionNative

            //直连网银推单
//            OrderMain.netBankPay();

            //订单查询
            OrderMain.orderQuery("20190403c2bPay1554279662651");

            //订单退款
//            OrderMain.refund("2018060114615570");


            //加密类型，默认RSA
            String sign_type = KeyUtils.TEST_DEFAULT_ENCODE_TYPE;
            //编码类型
            String charset = KeyUtils.TEST_DEFAULT_CHARSET;

            //根据请求参数生成的机密串
            String sign = KeyUtils.getSign(KeyUtils.TEST_MERCHANT_PRIVATE_KEY, charset, biz_content);
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