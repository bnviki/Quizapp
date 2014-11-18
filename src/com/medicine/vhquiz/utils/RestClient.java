package com.medicine.vhquiz.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

public class RestClient {

	private static RestClient instance;
	public static final int MAX_TOTAL_CONNECTION = 20;
	public static final int MAX_CONNECTIONS_PER_ROUTE = 20;
	public static final int TIMEOUT_CONNECT = 15000;
	public static final int TIMEOUT_READ = 15000;
	
	private HttpClient client; 
	private CookieStore cookieStore;
	private HttpContext localContext;
	//private String host = "http://54.254.216.45:3000";
	private String host;
	
	public static RestClient getInstance(){
		if(instance == null){
			instance = new RestClient();
		}
		return instance;
	}
	
	private RestClient(){
		setupClient();
	}
	
	public void setupClient(){
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));

		HttpParams connManagerParams = new BasicHttpParams();
		ConnManagerParams.setMaxTotalConnections(connManagerParams, MAX_TOTAL_CONNECTION);
		ConnManagerParams.setMaxConnectionsPerRoute(connManagerParams, new ConnPerRouteBean(MAX_CONNECTIONS_PER_ROUTE));

		HttpConnectionParams.setConnectionTimeout(connManagerParams, TIMEOUT_CONNECT);
		HttpConnectionParams.setSoTimeout(connManagerParams, TIMEOUT_READ);

		ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(new BasicHttpParams(), schemeRegistry);
		client = new DefaultHttpClient(cm, connManagerParams);
		host = "http://" + Constants.HOST;
		cookieStore = new BasicCookieStore();
		localContext = new BasicHttpContext();
		localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
	}
	
	public void resetConnection(){		
		if(client != null){
			client.getConnectionManager().shutdown();
		}			
		setupClient();
	}
	
	public String getCookie(String name){
		for(Cookie ck : cookieStore.getCookies()){
			if(ck.getName().equals(name))
				return ck.getValue();
		}
		return "";
	}
	
	public String getHost(){
		return host;
	}
	
    public static enum RequestMethod{GET, POST, DELETE}
    
    public RestResponse postJsonData(String url, String data){
    	url = host + url;
    	HttpPost request = new HttpPost(url);    	
    	try {
			StringEntity se = new StringEntity(data);
			request.setEntity(se);
			 
            // 7. Set some headers to inform server about the type of the content   
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-type", "application/json");
			
			return executeRequest(request, url);

		} catch (UnsupportedEncodingException e) {			
			RestResponse res = new RestResponse();
			res.responseCode = -1;
			res.message = "request error";							
			e.printStackTrace();
			return res;
		}    	
    }
    
    public RestResponse executeExplicit(String url, RequestMethod method, HashMap<String, String> params, HashMap<String, String> headers){
    	if(url == null || url == "" || method == null)
    		return null;
    	
    	ArrayList<NameValuePair> paramsList = new ArrayList<NameValuePair>();
    	ArrayList<NameValuePair> headersList = new ArrayList<NameValuePair>();
    	
    	if(params != null){
	    	for (Map.Entry<String, String> param : params.entrySet()) {
	    	    paramsList.add(new BasicNameValuePair(param.getKey(), param.getValue()));    	    
	    	}
    	}
    	
    	if(headers != null){
	    	for (Map.Entry<String, String> param : headers.entrySet()) {
	    		headersList.add(new BasicNameValuePair(param.getKey(), param.getValue()));    	    
	    	}
    	}
    	
    	return executeMethod(url, method, paramsList, headersList);
    }
        
    public RestResponse execute(String url, RequestMethod method, HashMap<String, String> params, HashMap<String, String> headers){
    	if(url == null || url == "" || method == null)
    		return null;
    	
    	url = host + url;
    	ArrayList<NameValuePair> paramsList = new ArrayList<NameValuePair>();
    	ArrayList<NameValuePair> headersList = new ArrayList<NameValuePair>();
    	
    	if(params != null){
	    	for (Map.Entry<String, String> param : params.entrySet()) {
	    	    paramsList.add(new BasicNameValuePair(param.getKey(), param.getValue()));    	    
	    	}
    	}
    	
    	if(headers != null){
	    	for (Map.Entry<String, String> param : headers.entrySet()) {
	    		headersList.add(new BasicNameValuePair(param.getKey(), param.getValue()));    	    
	    	}
    	}
    	
    	return executeMethod(url, method, paramsList, headersList);
    }

    public RestResponse executeMethod(String url, RequestMethod method, ArrayList<NameValuePair> params, ArrayList<NameValuePair> headers) 
    {
        switch(method) {
            case GET:
            {
                //add parameters
                String combinedParams = "";
                if(!params.isEmpty()){
                    combinedParams += "?";
                    for(NameValuePair p : params)
                    {                    	
                        String paramString = "";
						try {
							paramString = p.getName() + "=" + URLEncoder.encode(p.getValue(),"UTF-8");
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							RestResponse res = new RestResponse();
							res.responseCode = -1;
							res.message = "request error";							
							e.printStackTrace();
							return res;
						}
                        if(combinedParams.length() > 1)
                        {
                            combinedParams  +=  "&" + paramString;
                        }
                        else
                        {
                            combinedParams += paramString;
                        }
                    }
                }

                HttpGet request = new HttpGet(url + combinedParams);

                //add headers
                for(NameValuePair h : headers)
                {
                    request.addHeader(h.getName(), h.getValue());
                }

                return executeRequest(request, url);                
            }
            case POST:
            {
                HttpPost request = new HttpPost(url);

                //add headers
                for(NameValuePair h : headers)
                {
                    request.addHeader(h.getName(), h.getValue());
                }

                if(!params.isEmpty()){
                    try {
						request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						RestResponse res = new RestResponse();
						res.responseCode = -1;
						res.message = "request error";							
						e.printStackTrace();
						return res;
					}
                }

                return executeRequest(request, url);                
            }
            case DELETE:
            {
            	HttpDelete request = new HttpDelete(url);
            	 //add headers
                for(NameValuePair h : headers)
                {
                    request.addHeader(h.getName(), h.getValue());
                }
            	return executeRequest(request, url);
            }
        }
        
        return null;
    }

    private RestResponse executeRequest(HttpUriRequest request, String url)
    {
    	request.setHeader("Accept", "application/json");
    	RestResponse res = new RestResponse();
    	HttpResponse httpResponse;

        try {
            httpResponse = client.execute(request, localContext);
            res.responseCode = httpResponse.getStatusLine().getStatusCode();
            res.message = httpResponse.getStatusLine().getReasonPhrase();

            HttpEntity entity = httpResponse.getEntity();

            if (entity != null) {

                //InputStream instream = entity.getContent();
                //res.response = convertStreamToString(instream);
            	res.response = EntityUtils.toString(entity);
                //httpResponse.getEntity().consumeContent();
                // Closing the input stream will trigger connection release
                //instream.close();                
            }

        } catch (ClientProtocolException e)  {
            //client.getConnectionManager().shutdown();
            e.printStackTrace();
            res.message = "connection error";
            res.responseCode = 1000;            
        } catch (IOException e) {
            //client.getConnectionManager().shutdown();
            e.printStackTrace();
            res.message = "connection error";
            res.responseCode = 1000;            
        } catch (Exception e) {
            //client.getConnectionManager().shutdown();
            e.printStackTrace();
            res.message = "connection error";
            res.responseCode = 1000;            
        }
        
        
        return res;
    }

    private static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}