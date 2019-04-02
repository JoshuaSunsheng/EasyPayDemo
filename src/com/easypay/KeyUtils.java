package com.easypay;

import java.text.SimpleDateFormat;
import java.util.Date;

public class KeyUtils {

    /**
     * 测试参数 ###############
     */
    public static final String TEST_DEFAULT_MERCHANT_ID = "900029000002554";
    public static final String TEST_DEFAULT_PARTNER = "900029000002554";
    //测试商户私钥
    public static final String  TEST_MERCHANT_PRIVATE_KEY= "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJzaAhULP9enizbfkF/3+8TCfXNruSqVyME8XTaf10Eb0+0PN2Q0OMQ0w20ZVvKdYykuGVkNr4AuJnxpGZlh9t9Ojn6aobOu1/t3QW43MD0gN/ANS4GDFUpEB9d5PEJDVcOwwdZXQ33Lys+UdqSWZCqqzQzjei3TIg7tws6WerhbAgMBAAECgYEAit0DfOgBtIYzXPhelGgysMbTU+0p+Ma0/kbX63Vll9HwNO8ExiTpoONppNwLOy4VzS5v6ISSPLQfBIPz0vSFH1lKxYlWrvggHX9lBONHWMFM/PMW3mP3jF7xGJncGLEO6ZAkv7Mrh6tI3YLMPK53NXa/MhU86EIw3aHbTUcb67ECQQDL0mYuTZ/bnDc8/zvHN++8wPA4Nv7WdoTqXogSc0XFsfx+KDoFYBl/HiPmU8p6Xdi5Jcge2F7iTT7xY+AhquqzAkEAxQFhgHpbNfyiUm9k8UXPqiG3BH1uKsmGCZh5qWW5Ao3ywAybOvzpowp2KWn7uQ9DkJdMFyYkIql+ibT8gL7vuQJAFlVGhvpQctLKJq/cz/ZsGWWWrMIx6DPyWm/jPwpMcd4PyY8E1IP0Jz6ZNmql/AR+c8MVC3Qv6WIHbJHCBE5jCwJAJRghd8OSMRQkrEj1RMjUVUCL6XiEqeHQLzZakh7pOyXnM2osQGsmwuBRZ3LhRvMs0ajrVuCDpVMs1FkTwloxYQJABdWeEKtML6ysG+ldyERAg71JVo2y3DqCu/whaArv/yKMT+l8JowF9+mKGrqVsvXdKZc+M2Ax9qDMeIsOC5ki4w==";
    //测试访问地址
    public static String DEFAULT_URL = "https://test_nucc.bhecard.com:9088/api_gateway.do";
//    public static String DEFAULT_URL = "http://localhost:8080/api_gateway.do";
//    public static String DEFAULT_URL = "https://180.168.215.67:9088/api_gateway.do";
    public static String TEST_DES_ENCODE_KEY = "VntassPfhRL9HCKO5ExOIrrt";


    /**
     * 生产参数 ###############
     */
    public static final String SC_DEFAULT_PARTNER = "900029000000354";
    public static final String SC_MERCHANT_PRIVATE_KEY = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBALLfMUbf4u8uDSeG0WR//LSvxv7qglKsHwws3mpyqUWJau1ZXcfMeNQf+OhGTFrKsyP1WS3kXa1ZErjIhdeX5Jq2TphgXWZ+HdNVtd/rmHo84cjHJZOOBSvSmVYzqsJT253LoX1ip2sx/zbobU+Sm//4I5Zo4/yuufWBElCL2cuPAgMBAAECgYEApkJ3Fx27Xf48E+VodDXSulA4c3GeuSFrqnF6Ow9g71WPohZS6QfRt7oQLjZJeoq2gFHpFpMRz7LfiAo6/e4deXEWtMRg4UHcZbdBlTR4oM8au1TTEq446VhllSNZ8qeHxL4zO4peVDNupp8rElOMTXiQLKOph+fioLi++tvqZGECQQDrvtI3QD68BEQXlwMcUHkczppTs3Gxtd5uElQ7BQCTpM1fBhFIzv+TNxhNLGo5+2z1MKjWNE1KBo1ZXZnJdSfLAkEAwj1ykXJkYWbNrTRGzh0ZjSg784c20TeEl2HZ4BvSdCwSRxZUdZYx4x6MUWISlnokaifXSJhnsU9vXoZR4xEKzQJBAN5SJdNfLgqIB2Mr0g4owh7tpFLdPpJ2Tl8FwBOswv96AwfjI/fC5vmBktRs13z45KdSjVb9Ggp+pVyqzfZUGwMCQGdPWWVEq2Em1ZQe7t3nmlR6ptBTBXPnjG0bzU8mXRwO6LXIial09hmvgMA0YmCInF+dyyJAdT5YWoqy9FDKGq0CQQCBstxGn3LUWcGxEmTtmDt3pkHVy2IYQl/xFCgWYW1xIrC7dO8GfLZLzUbq/yBGO1KCRaQpFYKbJNTETB0TlKSY";
    public static final String SC_DEFAULT_MERCHANT_ID = "900029000000354";
    //生产访问地址
    public static String SC_URL = "https://newpay.bhecard.com/api_gateway.do";
    public static String SC_DES_ENCODE_KEY = "s6yaiIycSFXufo4jEg3VmLs4";

    public static final String TEST_DEFAULT_ENCODE_TYPE = "RSA";
    public static final String TEST_DEFAULT_CHARSET = "UTF-8";


    public static String getSign(String key, String charset, String bizContent) throws Exception {
        return AlipaySignature.rsaSign(bizContent, key, charset);
    }


    public static String getOutTradeNo(){
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(d) + + System.currentTimeMillis();
    }
}
