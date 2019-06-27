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
    private static String merchant_id = "900010000002708";

    //接入机构号
    private static String partner = "900010000002708";

    //请求地址
    private static String url = KeyUtils.DEFAULT_URL;

    //商户私钥
    private static String key = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJGzSjDr3yFxYEVn8UDQvQev1rhq1g/0M8qkr+Bo587UWm18nC6JP/kcmbQ+MP6v00bPYfJMd17IBOEVybmYCNJYmx/5clS03MHOhRXbulQ+8JpNFPk8tXj+GyMfi6JmwzyZsWXcGFB6a7Ac427C3jEnULAtMmOW6rw1rPcQ32hTAgMBAAECgYBdka7LzC734z/YUvB0l5lOHnbe6H2gsUuxkCIDFFwKUAXi2mvS1wHmkZ+ESYxdg71DdFuKp+DhVcMMalmqfyDqOMjpSF8/FWpmcKhZLB0hzfA7A25d3SGSpjLs8szp894PhbceZOayZBrHh7kH73hFARRsWOwhFriZ5RPW9xNPgQJBAOleE24eJj7oCxQ7HJbjBZMp9N/qtuUrXvafXzriXwPt6EH/b4rz0IFj6lyN6uvHcN6gTWvdkiaeEvUHkg3mjXkCQQCf1KksPnuCgEu0s0+142+xQxzQz/A7OQkY0jb84exUI0+NCOR7o/HR7MrxVMmEqFz19VpBf6zgsZQAkx9cio0rAkEA5IKvCfvRvXVgK945/3GratABrSTbFIokgd/K0xEIswNJkx55bZmSyLA6s4hvyZJQbl6PkRi0XMYaj/5qwNvKGQJAAuM0qzf1w0XOGFWk+wRA4FL2Ek+vZ2KAZ5Jkq+zo+BwrM8EVX6Z2l/RPNkzx1xOzmj30g3prBSQwKFKAHoviQQJBAJ68+EjYl1SOqsDV2lc4WTe4HCloNAFlx7WKLIOwSctHNHegS9PtiZcPlOZen1wkrBZbKN029m10Y752fFisxT8=";

    //易生公钥
    private static String easypay_pub_key = KeyUtils.TEST_EASYPAY_PUBLIC_KEY;

    //加密密钥
    private static String DES_ENCODE_KEY = "j9q1x7DQaAGqEtyQNjqgzRKD";

    //创建电子合同
    public static void createCont(){
        JSONObject sParaTemp = new JSONObject();
        sParaTemp.put("merchant_id", merchant_id);
        sParaTemp.put("out_trade_no", KeyUtils.getOutTradeNo());
        sParaTemp.put("contract_name", "创建合同");
        sParaTemp.put("seal_id", "");
        sParaTemp.put("seal_picture", "");
        sParaTemp.put("seal_color", "1");
        sParaTemp.put("text_value_info", "");
        biz_content = sParaTemp.toString();

        service  = "easypay.elecont.createCont";
    }
    
    //增加印章
    public static void addSeal(String oldOrderId) {
    	JSONObject sParaTemp = new JSONObject();
    	sParaTemp.put("merchant_id",merchant_id);
    	sParaTemp.put("old_out_trade_no", oldOrderId);
    	sParaTemp.put("seal_picture", "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");
    	biz_content = sParaTemp.toString();
    	
    	service = "easypay.elecont.addSeal";
    	
    }
    //下载合同
   public static void downloadCont(String oldOrderId) {
	    JSONObject sParaTemp = new JSONObject();
     	sParaTemp.put("merchant_id",merchant_id);
   	    sParaTemp.put("old_out_trade_no", oldOrderId);
   	    
   	    biz_content = sParaTemp.toString();
   	   
     	service = "easypay.elecont.downloadCont";
   }
   
   //合同查询
   public static void queryCont(String oldOrderId) {
	   JSONObject sParaTemp = new JSONObject();
	   sParaTemp.put("merchant_id",merchant_id);
  	   sParaTemp.put("old_out_trade_no", oldOrderId);
  	   
  	   biz_content = sParaTemp.toString();
  	   
  	 service = "easypay.elecont.queryCont";
	   
   }
   //修改印章
   public static void updateSeal(String oldOrderId) {
	   JSONObject sParaTemp = new JSONObject();
	   sParaTemp.put("merchant_id",merchant_id);
  	   sParaTemp.put("old_out_trade_no", oldOrderId);
   	   sParaTemp.put("seal_picture", "iVBORw0KGgoAAAANSUhEUgAAAFoAAAA4CAIAAADy9ngEAAAgAElEQVRogaV7d3xVRfr3M3POvemVkB4IvYXeuyJNQBAEYUWwsBZQVhRde1nbKorYUBFZEQR09YcUWRdEVJotVJEECE1IJ");
       
  	   
  	   biz_content = sParaTemp.toString();
  	   
  	 service = "easypay.elecont.updateSeal";
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

            //创建合同
//            createCont();
            //合同查询
//            queryCont("201906271561600528167");
            //下载合同
//            downloadCont("201906251561454113285");
            //增加印章
//            addSeal("201906271561600528167");
            //修改印章
//            updateSeal("test1559289958251");
           
           
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
