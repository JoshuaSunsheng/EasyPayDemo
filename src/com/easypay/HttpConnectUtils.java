package com.easypay;

import javax.net.ssl.*;
import java.io.*;
import java.net.*;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;

/**
 * HttpUrlConnect 请求类
 */
public class HttpConnectUtils {

    private static HttpURLConnection createConnection(
            String url,
            String encoding,
            int connectionTimeout,
            int readTimeOut,
            String method,
            Map param,
            SSLSocketFactory context
    ) throws IOException {
        URL u_url = new URL(url);
        HttpURLConnection httpURLConnection = (HttpURLConnection) u_url.openConnection();
        httpURLConnection.setConnectTimeout(connectionTimeout);// 连接超时时间
        httpURLConnection.setReadTimeout(readTimeOut);// 读取结果超时时间
        httpURLConnection.setDoInput(true); // 可读
        httpURLConnection.setDoOutput(true); // 可写
        httpURLConnection.setUseCaches(false);// 取消缓存
        if (method.toUpperCase() == "POST") {
            httpURLConnection.setRequestProperty("Content-type",
                    "application/x-www-form-urlencoded;charset=" + encoding);
        } else if (method.toUpperCase() == "POST_NO_CHARSET") {
            httpURLConnection.setRequestProperty("Content-type",
                    "application/x-www-form-urlencoded");
            method = "POST";
        } else if (method.toUpperCase() == "ZXF") {
            method = "POST";
            httpURLConnection.setRequestProperty("Content-type",
                    "text/plain;charset=" + encoding);
        } else if (method.toUpperCase() == "JSON") {
            method = "POST";
            httpURLConnection.setRequestProperty("Content-type",
                    "application/json;charset=" + encoding);
        } else if (method.toUpperCase() == "WL") {
            method = "POST";
            httpURLConnection.setRequestProperty("Content-type", "application/xml;charset=utf-8");
            httpURLConnection.setRequestProperty("Connection", "keep-alive");
            httpURLConnection.setRequestProperty("MsgTp", String.valueOf(param.get("MsgTp")));

            httpURLConnection.setRequestProperty("OriIssrId", String.valueOf(param.get("OriIssrId")));
            httpURLConnection.setRequestProperty("PyeeAcctTp", "00");
            httpURLConnection.setRequestProperty("PyerAcctTp", "04");
            httpURLConnection.setRequestProperty("ReservedFiedld", "");
        } else {
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        }
        httpURLConnection.setRequestMethod(method);
        if ("https".equalsIgnoreCase(u_url.getProtocol())) {
            HttpsURLConnection connection = (HttpsURLConnection) httpURLConnection;
            if (context != null) {
                connection.setSSLSocketFactory(context);
            } else {
                connection.setSSLSocketFactory(new BaseHttpSSLSocketFactory());
            }
            //解决由于服务器证书问题导致HTTPS无法访问的情况
            connection.setHostnameVerifier(new BaseHttpSSLSocketFactory.TrustAnyHostnameVerifier());
            return connection;
        }
        return httpURLConnection;
    }

    public static int sendRequest(String url,
                                  String encoding,
                                  Object request,
                                  int connectionTimeout,
                                  int readTimeout,
                                  String method,
                                  StringBuilder sb_ret,
                                  Map extraMap
    ) throws IOException {
        Map map = _sendRequest(encoding, request, createConnection(
                url,
                encoding,
                connectionTimeout,
                readTimeout,
                method,
                extraMap, null));
        return _receiveResponse((Integer) map.get("i_ret"), (HttpURLConnection) map.get("connection"), sb_ret, encoding, request);
    }

    /**
     * 支持定制的https证书
     * @param url
     * @param request
     * @param method
     * @param contextFactory
     * @return
     * @throws IOException
     */
    public static String sendHttpSRequest(String url,
                                          Object request,
                                          String method,
                                          SSLSocketFactory contextFactory

    ) throws IOException {

        String encoding = "UTF-8";
        int connectionTimeout = 30000;
        int readTimeout = 60000;
        StringBuilder sb_ret = new StringBuilder();
        Map map = _sendRequest(encoding, request, createConnection(
                url,
                encoding,
                connectionTimeout,
                readTimeout,
                method,
                null,
                contextFactory
        ));
        assert HttpURLConnection.HTTP_OK == _receiveResponse((Integer) map.get("i_ret"), (HttpURLConnection) map.get("connection"), sb_ret, encoding, request) : map.get("i_ret") + "通讯异常";
        return sb_ret.toString();
    }

    //推送send
    private static Map<String, Object> _sendRequest(String encoding, Object request, HttpURLConnection connection) throws UnsupportedEncodingException {
        int i_ret = 0;
        PrintStream out = null;
        String s_request = "";
        if (request instanceof String) {
            s_request = (String) request;
        } else if (request instanceof Map) {
            try {
                s_request = getRequestParamString((Map) request, encoding);
            } catch (UnsupportedEncodingException e) {
                assert false : "不支持的类型:" + e.getLocalizedMessage();
            }
        } else {
            assert false : "不支持的类型:" + request;
        }
        System.out.println("请求报文为：" + s_request);
        try {
            connection.connect();
            out = new PrintStream(connection.getOutputStream(), false, encoding);
            out.print(s_request);
            out.flush();
        } catch (Exception ignore) {
            System.out.print("通讯连接成功.返回错误,超时:" + ignore);
            i_ret = -1;
        } finally {
            if (out != null) {
                out.close();
            }
        }
        Map<String, Object> ret = new HashMap<String, Object>(2);
        ret.put("i_ret", i_ret);
        ret.put("connection", connection);
        return ret;
    }

    //处理并获取请求
    static public int _receiveResponse(int i_ret, HttpURLConnection connection, StringBuilder sb_ret, String encoding, Object request) {
        if (i_ret != -1) {
            InputStream inputStream = null;
            try {
                i_ret = connection.getResponseCode();
                if (i_ret == HttpURLConnection.HTTP_OK) {
                    inputStream = connection.getInputStream();
                    sb_ret.append(new String(read(inputStream), encoding));
                } else {
                    inputStream = connection.getErrorStream();
                    sb_ret.append(new String(read(inputStream), encoding));
                }
            } catch (Exception ignore) {
                i_ret = -2;
                System.out.print("通讯连接成功.返回错误,超时:" + ignore.getMessage());
            } finally {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                } catch (Throwable ignore) {
                    System.out.print("关闭流异常:" + ignore.getMessage());
                }
            }
        }
        if (i_ret != HttpURLConnection.HTTP_OK) {
            if (request instanceof Map)
                System.out.print("原始报文111:" + ((Map) request).toString());
            else
                System.out.print("原始报文:[${request}]");
        }
        return i_ret;
    }

    public static byte[] read(InputStream inputStream) throws IOException {
        byte[] buf = new byte[1024];
        int length = 0;
        ByteArrayOutputStream bout = new ByteArrayOutputStream(1024);
        while ((length = inputStream.read(buf, 0, buf.length)) > 0) {
            bout.write(buf, 0, length);
        }
        bout.flush();
        return bout.toByteArray();
    }

    /**
     * 将Map存储的对象，转换为key=value&key=value的字符 并且做urlencoding
     *
     * @param requestParam
     * @param charset      URL.encoding
     * @return
     */
    public static String getRequestParamString(Map<String, String> requestParam, String charset) throws UnsupportedEncodingException {
        return getRequestParamString(requestParam, charset, true);
    }

    public static String getRequestParamString(Map<String, String> requestParam, String charset, boolean isEncode) throws UnsupportedEncodingException {
        if (charset == null) {
            charset = "UTF-8";
        }
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, String> entry : requestParam.entrySet()) {
            if (entry.getValue() == null) continue;
            if (isEncode)
                sb.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), charset)).append("&");
            else
                sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        String s_ret = sb.toString();
        if (StringUtils.isBlank(s_ret)) return "";
        s_ret = s_ret.substring(0, s_ret.length() - 1);
        return s_ret;
    }
    /**
     * @param file
     * @param pwd
     * @param type PKCS12  jks
     */
    public static KeyStore loadKeyStore(File file, String pwd, String type) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
        KeyStore keyStore = KeyStore.getInstance(type);
        InputStream ksIn = new FileInputStream(file);
        keyStore.load(ksIn, pwd.toCharArray());
        return keyStore;
    }

    /**
     * 初始化https环境  设置秘钥和证书
     * 目前华势使用
     *
     * @param keyFilePath
     * @param keyPW
     * @param trustFilePath
     * @param trustPW
     * @return
     */
    public static SSLSocketFactory getSSLSocketFactory(String keyFilePath, String keyPW, String trustFilePath, String trustPW, String defaultTLS) {
        if (defaultTLS == null) {
            defaultTLS = "TLS";
        }
        try {
            String pwd = keyPW;
            KeyStore keyStore = loadKeyStore(
                    new File("../cert/88888888.p12"),//"/certs/hs/88888888.p12"
                    pwd,
                    "PKCS12");
            KeyStore trustStore = loadKeyStore(
                    new File("../cert/client.truststore"),//"/certs/hs/client.truststore"
                    trustPW,
                    "jks");
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(trustStore);
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(keyStore, pwd.toCharArray());
            SecureRandom rand = new SecureRandom();
            SSLContext sslContext = SSLContext.getInstance(defaultTLS);
            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), rand);
            return sslContext.getSocketFactory();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return null;
    }


}
