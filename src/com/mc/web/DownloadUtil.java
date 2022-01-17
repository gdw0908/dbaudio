package com.mc.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.FileCopyUtils;

public class DownloadUtil {
	/**
     * 브라우저 구분 얻기.
     * 
     * @param request
     * @return
     */
    private static String getBrowser(HttpServletRequest request) {
        String header = request.getHeader("User-Agent");
        if (header.indexOf("rv:11.0") > -1 || header.indexOf("MSIE") > -1) {
            return "MSIE";
        } else if (header.indexOf("Chrome") > -1) {
            return "Chrome";
        } else if (header.indexOf("Opera") > -1) {
            return "Opera";
        }
        return "Firefox";
    }
    
    /**
     * Disposition 지정하기.
     * 
     * @param filename
     * @param request
     * @param response
     * @throws Exception
     */
    private static void setDisposition(String filename, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String browser = getBrowser(request);
		
		String dispositionPrefix = "attachment; filename=";
		String encodedFilename = null;
		
		if (browser.equals("MSIE")) {
		    encodedFilename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
		} else if (browser.equals("Firefox")) {
		    encodedFilename = "\"" + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
		} else if (browser.equals("Opera")) {
		    encodedFilename = "\"" + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
		} else if (browser.equals("Chrome")) {
		    StringBuffer sb = new StringBuffer();
		    for (int i = 0; i < filename.length(); i++) {
			char c = filename.charAt(i);
			if (c > '~') {
			    sb.append(URLEncoder.encode("" + c, "UTF-8"));
			} else {
			    sb.append(c);
			}
		    }
		    encodedFilename = sb.toString();
		} else {
		    //throw new RuntimeException("Not supported browser");
		    throw new IOException("Not supported browser");
		}
		
		response.setHeader("Content-Disposition", dispositionPrefix + encodedFilename);
	
		if ("Opera".equals(browser)){
		    response.setContentType("application/octet-stream;charset=UTF-8");
		}
    }
    
    /**
     * 첨부파일로 등록된 파일에 대하여 다운로드를 제공한다.
     * @param filepath
     * @param orgFileName
     * @param request
     * @param response
     * @throws Exception
     */
    public static void Download(String filepath, String orgFileName
    			, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	
	    File uFile = new File(filepath);
	    int fSize = (int)uFile.length();

	    if (fSize > 0) {
			//String mimetype = "application/x-msdownload";
	
			//response.setContentType(mimetype);
			setDisposition(orgFileName, request, response);
			response.setContentLength(fSize);
	
			/*
			 * FileCopyUtils.copy(in, response.getOutputStream());
			 * in.close(); 
			 * response.getOutputStream().flush();
			 * response.getOutputStream().close();
			 */
			BufferedInputStream in = null;
			BufferedOutputStream out = null;
	
			try {
			    in = new BufferedInputStream(new FileInputStream(uFile));
			    out = new BufferedOutputStream(response.getOutputStream());
	
			    FileCopyUtils.copy(in, out);
			    out.flush();
			} catch (Exception ex) {
			    throw ex;
			} finally {
			    if (in != null) {
					try {
					    in.close();
					} catch (Exception ignore) {
						throw ignore;
					}
			    }
			    if (out != null) {
					try {
					    out.close();
					} catch (Exception ignore) {
						throw ignore;
					}
			    }
			}
	    } else {
			response.setContentType("text/html; charset=UTF-8");
	
			PrintWriter printwriter = response.getWriter();
			printwriter.println("<html>");
			printwriter.println("<br><br><br><h2>파일이 존재하지 않습니다.:<br>" + orgFileName + "</h2>");
			printwriter.println("<br><br><br><center><h3><a href='javascript: history.go(-1)'>Back</a></h3></center>");
			printwriter.println("<br><br><br>&copy; partsmoa.com");
			printwriter.println("</html>");
			printwriter.flush();
			printwriter.close();
	    }
    }
    
}
