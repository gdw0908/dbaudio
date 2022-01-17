package com.mc.web.common;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ShorturlController {
	Logger log = Logger.getLogger(this.getClass());
	
	@Value("#{config['shorturl.key']}")
	private String SHORTURL_KEY;
	
	@ResponseBody
	@RequestMapping("/shorturl.do")
	public String shorturl(@RequestParam Map<String, String> params, HttpServletRequest request) throws Exception{
		
		Document doc = null;
		try {
			doc = Jsoup.parse(getHttpClient(params.get("fullurl")+"&key="+SHORTURL_KEY));
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		Elements content = doc.select("url");
		
		return content.text();
	}
	
	public String getHttpClient(String url) throws ClientProtocolException, IOException{
		HttpClient httpClient = new DefaultHttpClient(); 
		httpClient.getParams().setIntParameter("http.connection.timeout", 3000);

        HttpGet httpGet = new HttpGet(url); 
        HttpResponse httpResponse = httpClient.execute(httpGet); 
         
        return EntityUtils.toString(httpResponse.getEntity());
	}
}
