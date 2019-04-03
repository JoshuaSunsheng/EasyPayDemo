package com.easypay;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Main {

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

    //冻结
    public static void preauth() {
        JSONObject reqMap = new JSONObject();
        reqMap.put("merchant_id", merchant_id);
        reqMap.put("amount", "1");
        reqMap.put("out_trade_no", KeyUtils.getOutTradeNo());
        biz_content = reqMap.toString();
        service = "trade.auth.preauth";
    }

    //获取余额
    public static void balnace() {
        JSONObject reqMap = new JSONObject();
        reqMap.put("merchant_id", merchant_id);
        biz_content = reqMap.toString();
        service = "trade.acc.balance";
    }


    //平台商户推单推送订单
    public static void pushOrder() {
        service = "easypay.merchant.merge.pay";
        JSONObject reqMap = new JSONObject();
        reqMap.put("subject", "测试主订单");
        reqMap.put("body", service);
        reqMap.put("out_merge_no", "M" + merchant_id + KeyUtils.getOutTradeNo());

        JSONArray jsonarray = new JSONArray();
        JSONObject orderDetail = new JSONObject();
        orderDetail.put("subject", "测试子订单1");
        orderDetail.put("body", "测试子订单1");
        orderDetail.put("merchant_id", merchant_id);
        orderDetail.put("out_trade_no", "SUB1" + merchant_id + KeyUtils.getOutTradeNo());
        orderDetail.put("total_amount", "1");
        orderDetail.put("seller_email", "sellerEmail");
        orderDetail.put("buyer_email", "sellerEmail");

        JSONObject orderDetai2 = new JSONObject();
        orderDetai2.put("subject", "测试子订单2");
        orderDetai2.put("body", "测试子订单2");
        orderDetai2.put("merchant_id", merchant_id);
        orderDetai2.put("out_trade_no", "SUB2" + merchant_id + KeyUtils.getOutTradeNo());
        orderDetai2.put("total_amount", "1");
        orderDetai2.put("seller_email", "sellerEmail");
        orderDetai2.put("buyer_email", "sellerEmail");

        jsonarray.add(orderDetail);
        jsonarray.add(orderDetai2);

        reqMap.put("order_details", jsonarray.toString());
        biz_content = reqMap.toString();
        System.out.println(biz_content);
    }


    public static void main(String[] args) {
        //易生请求示例子
        try {
            if (!isTest) {
                //商户号
                merchant_id = KeyUtils.SC_DEFAULT_MERCHANT_ID;
                //接入机构号
                partner = KeyUtils.SC_DEFAULT_PARTNER;
                //请求地址
                url = KeyUtils.SC_URL;
                //key密钥
                key = KeyUtils.SC_MERCHANT_PRIVATE_KEY;
            }
            //根据接口文档生成对应的json请求字符串
            //冻结
            //Main.preauth();

            //获取余额
            //Main.balnace();

            //平台商户推单推送订单
            Main.pushOrder();

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
        } catch (Exception e) {
            if (e != null) {
                System.out.print(e.getMessage() + "\n");
            } else {
                System.out.print("-----其他未知错误--------" + "\n");
            }
        }
    }
}