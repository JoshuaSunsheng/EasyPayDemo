package com.easypay;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

/**
 * 代付测试
 * @author njp
 *
 */
public class DsfMain {
	
	//标记生产还是测试环境
    public static boolean isTest = true;

    //根据接口文档生成对应的json请求字符串
    private static String biz_content = "";

    //接口文档中的方法名
    private static String service = "trade.acc.dsfpay.pay";

    //商户号
    private static String merchant_id = "900029000000354";

    //接入机构号
    private static String partner = "900029000000354";

    //请求地址
    private static String url = KeyUtils.DEFAULT_URL;

    //key密钥
    private static String key = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAIqUuxd92eEBXVneDWhfNP6XCkLcGBO1YAulexKX+OdlfZzB/4NNHkOAQQy84k3ZgIUPIk5hewLbA+XGrk9Wih5HG3ZQeFugeoTcx3vwo7AQv7KnmcKEWFNlOr/EhB3JndmcQnBRsIRRdCP+7nobfBqU0jS8dnpcQX1AtBRZRnkfAgMBAAECgYAe+u70ansZ1Q9EduKycY5MWAHAPqnXRhXppJ3l4zmOqV6ye6Aef1ADsRlZuqQw2S3lESQPN7WjRskRRiBTtjn8Atul9YeC7+QirP1K8seUP5gKB4bcjlzzl1m5dmxldkptJAmdzwYn8PRTW0+tFVyEaD/B8hKGxij4Gew0e8bwCQJBAOboG3ttBESsG2cAtmP1MfKRTjVdY7qRMXzBybcAeobBbmgCQgybVXXgjbGai+qwrQqcVRIp6p1yDWTZxVSuDWsCQQCZpBhcayOCMZR6F8dQJSuSSSIJw/GGN7IXfMYIqLxA2oGzlQ0B1DffOUe2wrid+WdpLuYCz2LYPQHDEgYM1dwdAkEAnfwhEYm9ad73wLnUEQAqdHTGtex316aP3XQZt4Q0UQ73o2IoHsgI6OYDDIlZQfIv8xqTeiIDzEXEtEPrp8yOkQJBAIWAzFZKFqHD2UO6M8vVcKX9fGFF7TH2ZX75Qc82Z9ZmyDs2sgW71QzX5hPN4cQLeqswQFeCw14orMZHfBBdKJUCQQDiWYk85okRugsWtxeJFhMEt2oUT+Kd8Yz5Aiz3J9XIS+zWtJrFlv+hXkVedPJ3xtBF32DZrCbxDn3UjXipRaCP";

    //加密密钥
    private static String DES_ENCODE_KEY = KeyUtils.TEST_DES_ENCODE_KEY;

    //实时代付
    public static void dsfPay(){
        JSONObject sParaTemp = new JSONObject();
        sParaTemp.put("merchant_id", merchant_id);
        sParaTemp.put("out_trade_no", "dsf" + System.currentTimeMillis()+ "");
        sParaTemp.put("type", 1);//代付类型 固定传1
        sParaTemp.put("nbkno", "313100001274"); //联行号
//        sParaTemp.put("bank_code", "308");//民生银行(银行编号请见‘代付’页面底部)
//        sParaTemp.put("bank_name", "上海");//银行网点名称
//        sParaTemp.put("city", "上海");
        sParaTemp.put("acc", "6225881008024282");   //银行卡号
        sParaTemp.put("name", "test");    //账户姓名
        sParaTemp.put("acc_type", 2); //付款账户类型：1 结算账户 2 现金账户, 默认2现金账户
        sParaTemp.put("amount", "180");
        sParaTemp.put("bis_type", 1);//收款账户类型：0 对公 1 对私
        biz_content = sParaTemp.toString();

        service  = "trade.acc.dsfpay.pay";
    }


    private static String getEncode(String data){
        return StringUtils.bytesToHexStr(DesUtil.desEncode(data, DES_ENCODE_KEY));
    }

    //代付查询
    public static void dsfQuery(){
        JSONObject sParaTemp = new JSONObject();
        sParaTemp.put("merchant_id", merchant_id);
        sParaTemp.put("out_trade_no", "dsf1554781247666");

        biz_content = sParaTemp.toString();
        service  = "trade.acc.dsfpay.query";
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

            //实时代付
//            dsfPay();

            //代付查询
            dsfQuery();

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
            System.out.print(e.getMessage()+ "\n");
        }
    }

}
