package com.xbsd.util;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import sun.misc.BASE64Encoder;


public class SSLHttpConn {

	/**
     * 
     * @param url   需要请求的网关路径
     * @param sendData  请求时需要传入的参数
     * @param urlencode url的编码格式
     * @param connTimeOut   链接超时时间 
     * @param readTimeOut   读取超时时间
     * @param contentType   请求头部  固定输入"application/x-www-form-urlencoded;charset="+urlencode
     * @param header     输入null
     * @return
     */
    public static String sendAndRcvHttpPostBase(String url,String sendData,String urlencode,int connTimeOut,int readTimeOut,String contentType,Map<String,String> header){
        Long curTime = System.currentTimeMillis();
        System.out.println( "SimpleHttpConnUtil Prepare @"+curTime);
        String result = "";
        BufferedReader in = null;
        DataOutputStream out = null;
        int code = 999;
        HttpsURLConnection httpsConn = null;
        HttpURLConnection httpConn = null;
        try{
            URL myURL = new URL(url);
            System.out.println(  "请求地址："+url);
            if(url.startsWith("https://")){
                httpsConn =    (HttpsURLConnection) myURL.openConnection();
                TrustManager[] trustAllCerts = new TrustManager[]{
                        new X509TrustManager() {
                            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                return null;
                            }
                            public void checkClientTrusted(
                                java.security.cert.X509Certificate[] certs, String authType) {
                            }
                            public void checkServerTrusted(
                                java.security.cert.X509Certificate[] certs, String authType) {
                            }
                        }
                    };
                SSLContext sc = SSLContext.getInstance("TLS");
                sc.init(null, trustAllCerts, new java.security.SecureRandom());
                httpsConn.setSSLSocketFactory(sc.getSocketFactory());
                HostnameVerifier hv = new HostnameVerifier() {
                    @Override
                    public boolean verify(String urlHostName, SSLSession session) {
                        return true;
                    }
                }; 
                httpsConn.setHostnameVerifier(hv);
                    
                httpsConn.setRequestProperty("Accept-Charset", urlencode);
                httpsConn.setRequestProperty("User-Agent","java HttpsURLConnection");
                if(header!=null){
                    for(String key:header.keySet()){
                        httpsConn.setRequestProperty(key, (String)header.get(key));
                    }
                }
                httpsConn.setRequestMethod("POST");
                httpsConn.setUseCaches(false);
                httpsConn.setRequestProperty("Content-Type",contentType); 
                httpsConn.setConnectTimeout(connTimeOut);
                httpsConn.setReadTimeout(readTimeOut);
                httpsConn.setDoInput(true);
                httpsConn.setInstanceFollowRedirects(true); 
                if(sendData !=null){
                    httpsConn.setDoOutput(true);
                    // 获取URLConnection对象对应的输出流
                    out = new DataOutputStream(httpsConn.getOutputStream());
                    // 发送请求参数
                    out.write(sendData.getBytes(urlencode));
                    // flush输出流的缓冲
                    out.flush();
                    out.close();
                }
                // 取得该连接的输入流，以读取响应内容
                in = new BufferedReader(new InputStreamReader(httpsConn.getInputStream(),urlencode));
                code = httpsConn.getResponseCode();
            }else{
                httpConn =    (HttpURLConnection) myURL.openConnection();
                httpConn.setRequestProperty("Accept-Charset", urlencode);
                httpConn.setRequestProperty("user-agent","java HttpURLConnection");
                if(header!=null){
                    for(String key:header.keySet()){
                        httpConn.setRequestProperty(key, (String)header.get(key));
                    }
                }
                httpConn.setRequestMethod("POST");
                httpConn.setUseCaches(false);
                httpConn.setRequestProperty("Content-Type",contentType); 
                httpConn.setConnectTimeout(connTimeOut);
                httpConn.setReadTimeout(readTimeOut);
                httpConn.setDoInput(true);
                httpConn.setInstanceFollowRedirects(true); 
                if(sendData !=null){
                    httpConn.setDoOutput(true);
                    // 获取URLConnection对象对应的输出流
                    out = new DataOutputStream(httpConn.getOutputStream());
                    // 发送请求参数
                    out.write(sendData.getBytes(urlencode));
                    // flush输出流的缓冲
                    out.flush();
                    out.close();
                }
                // 取得该连接的输入流，以读取响应内容
                in = new BufferedReader(new InputStreamReader(httpConn.getInputStream(),urlencode));
                code = httpConn.getResponseCode();
            }
            if (HttpURLConnection.HTTP_OK == code){ 
                String line;
                while ((line = in.readLine()) != null) {
                    result += line;
                    
                    
                    System.out.println("=====反回结果====="+ line);
                    
                    
                }
                if(result.length()>2000){
                   System.out.println( "http返回结果 !\n"+result.substring(0,2000)+"...");
                }else{
                	System.out.println( "http返回结果 !\n"+result);
                }
            }else{
                result = null;
                throw new Exception("支付失败,服务端响应码："+code);
            }
        }catch(IOException e){
        	e.printStackTrace();
            result = null;
        }catch(Exception e){
        	e.printStackTrace();
            result = null;
        }finally{
        	 System.out.println("对方地址："+url);
            if(out!=null){
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
            if(httpConn!=null){
                httpConn.disconnect();
            }
            if(httpsConn!=null){
                httpsConn.disconnect();
            }
            if(in!=null){
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
        System.out.println( "SimpleHttpConnUtil "+curTime+" end for "+(System.currentTimeMillis()-curTime)+"ms");
        return result;
    }
    
    public static String getImageStr(String imgFile){
    	InputStream input=null;
    	byte[] data=null;
    		try {
				input=new FileInputStream(imgFile);
				data=new byte[input.available()];
				input.read(data);
				input.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		BASE64Encoder encoder=new BASE64Encoder();
    	return encoder.encode(data);
    }
    
    public static String getUrlImage(String   imgUrl){
    	System.out.println("the imgurl is "+imgUrl);
    	ByteArrayOutputStream out=new ByteArrayOutputStream();
    	byte[] data=new byte[1024];
    	URL url;
		try {
			if ( imgUrl!=null || "".equals(imgUrl)) {
				
				url = new URL(imgUrl);
				HttpURLConnection conn=(HttpURLConnection)url.openConnection();
				conn.setRequestMethod("GET");
				conn.setConnectTimeout(10*1000);
				if(conn.getResponseCode()!= HttpURLConnection.HTTP_OK){
					return "failure";
				}
				InputStream inputStream= conn.getInputStream();
				int len=-1;
				while((len=inputStream.read(data))!=-1){
					out.write(data,0,len);
				}
				inputStream.close();
			}else{
				System.out.println("imgurl is null");
			}
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
		BASE64Encoder encoder=new BASE64Encoder();
    	return encoder.encode(out.toByteArray());
    }
	public static void main(String[] args) {
		String zpfw="-1";
		String kssj="2018-05-12 01:01:01";
		String jssj="2018-05-13 01:01:01";
		//人脸查询
		String url="https://10.95.95.160:443/fms/services/rest/remoteSnapshotService/findSnapFacesFromUploadPic";
		//身份确认
		String url2="https://10.95.95.160:443/fms/services/rest/remoteHumanService/findStaticHumansFromUploadPic";
		String imgstrString=getUrlImage("http://10.95.95.172:6120/pic?0dde44i93-e*454103793349--96143e1ef92a1i1b2*=1d1i5s1*=idp5*=*d3i5t=pe2m501056-310s3ec0z49ci=2=");
//		String sendData="{\"deviceIds\":\"ZPJ15054396329680007148\"}";
//		String sendData="{'deviceIds':'"+zpfw+"','beginDate':'"+kssj+"','endDate':'"+jssj+"','pageNum':'20','pageSize':'10'}";
		String sendData="{'file64':'"+imgstrString+"'}";
		String urlencode="UTF-8";
		int connTimeOut=20000;
		String contentType="application/json;charset=UTF-8";
		int readTimeOut=20000;
		Map<String, String>  header=new HashMap<String, String>();
		header.put("Content-type", "application/json;charset=UTF-8");
		sendAndRcvHttpPostBase(url2, sendData, urlencode, connTimeOut, readTimeOut, contentType, header);

	}

}
