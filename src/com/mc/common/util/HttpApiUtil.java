package com.mc.common.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

public class HttpApiUtil {
	Logger log = Logger.getLogger(this.getClass());
	
	private HttpURLConnection http;	
	
	public HttpApiUtil(HttpURLConnection http) {
		this.http = http;
	}	
	
	public void request(String method, String headerName, String headerValue, JSONObject jsonData) throws Exception{
		
		http.setRequestMethod(method);
		http.setRequestProperty("Content-Type","application/json; charset=UTF-8");
		http.setRequestProperty(headerName, headerValue);
		//http.setRequestProperty("Authorization","pk_0e4e-4a7454-401-d4a5d"); //요청하는 클래스에서 셋팅해서 넘겨주면됨
		http.setDoOutput(true);
		
		PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(http.getOutputStream()));
		printWriter.write(jsonData.toString());
		printWriter.flush();			
	}
	
	public String response() throws Exception {
		BufferedReader bufferedReader = null;
		int status = http.getResponseCode();

		if(status == HttpURLConnection.HTTP_OK) {
			bufferedReader = new BufferedReader(new InputStreamReader(http.getInputStream(), "UTF-8"));
		}else {
			bufferedReader = new BufferedReader(new InputStreamReader(http.getErrorStream(), "UTF-8"));
		}
		String line;
		StringBuffer response = new StringBuffer();
		
		while((line = bufferedReader.readLine()) != null) {
			response.append(line);
		}
		bufferedReader.close();
		log.debug("응답값 : "+ response.toString());
		
		return response.toString();
	}
}
