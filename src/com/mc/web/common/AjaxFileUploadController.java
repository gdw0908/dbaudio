package com.mc.web.common;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class AjaxFileUploadController {
	
	@Value("#{config['home.url']}")
	private String HOME_URL;
	
	@Value("#{config['upload.temp']}")
	private String TEMP_PATH;
	
	@Value("#{config['upload.editor']}")
	private String EDITOR_PATH;
	
	@Autowired
	private FileUtil fileUtil;
	
	@RequestMapping("/ajaxUpload.do")
	public String ajaxUpload(HttpServletRequest request, 
			@RequestParam Map<String, String> params,
			@RequestParam(value = "file") MultipartFile file, 
			@RequestParam(value = "width", defaultValue="0") int width, 
			@RequestParam(value = "height", defaultValue="0") int height,
			@RequestParam(value = "ratio", defaultValue="true") boolean ratio) throws Exception{
		Map rst = null;
		if(width != 0 && height != 0){	//이미지 파일 사이즈 고정 조정
			rst = fileUtil.imageResize(request.getSession().getServletContext().getRealPath(TEMP_PATH), file, width, height, ratio);
		}else{
			rst = fileUtil.uploadFile(request.getSession().getServletContext().getRealPath(TEMP_PATH), file);
		}
		request.setAttribute("json", rst);
		return "json";
	}
	
	@RequestMapping("/ajaxUpload_for_android.do")
	public String ajaxUploadForAndroid(HttpServletRequest request, 
			@RequestParam Map<String, String> params,
			@RequestParam(value = "file") MultipartFile file, 
			@RequestParam(value = "width", defaultValue="0") int width, 
			@RequestParam(value = "height", defaultValue="0") int height,
			@RequestParam(value = "ratio", defaultValue="true") boolean ratio) throws Exception{
		Map rst = null;
		//if(width != 0 && height != 0){	//이미지 파일 사이즈 고정 조정
			rst = fileUtil.imageResize2(request.getSession().getServletContext().getRealPath(TEMP_PATH), file, width, height, ratio);
		//}else{
		//	rst = fileUtil.uploadFile(request.getSession().getServletContext().getRealPath(TEMP_PATH), file);
		//}
		request.setAttribute("json", rst);
		return "json";
	}
	
	@RequestMapping("/ajaxUploadCar.do")
	public String ajaxUploadList(HttpServletRequest request, 
			@RequestParam Map<String, String> params,
			@RequestParam(value = "file") List<MultipartFile> fileList, 
			@RequestParam(value = "width", defaultValue="0") int width, 
			@RequestParam(value = "height", defaultValue="0") int height,
			@RequestParam(value = "ratio", defaultValue="true") boolean ratio) throws Exception{
		Map rst = null;
		
		for(MultipartFile file : fileList){
			if(file.isEmpty()) continue;
			
			if(width != 0 && height != 0){	//이미지 파일 사이즈 고정 조정
				rst = fileUtil.imageResize(request.getSession().getServletContext().getRealPath(TEMP_PATH), file, width, height, ratio);
			}else{
				rst = fileUtil.uploadFile(request.getSession().getServletContext().getRealPath(TEMP_PATH), file);
			}
		}
		request.setAttribute("json", rst);
		return "json";
	}
	
	@ResponseBody
	@RequestMapping("/ajaxUploadCarAll.do")
	public void ajaxUploadCarAll(HttpServletRequest request, HttpServletResponse response, 
			@RequestParam Map<String, String> params,
			@RequestParam(value = "file") MultipartFile file, 
			@RequestParam(value = "width", defaultValue="0") int width, 
			@RequestParam(value = "height", defaultValue="0") int height,
			@RequestParam(value = "ratio", defaultValue="true") boolean ratio) throws Exception{
		Map rst = null;
		if(width != 0 && height != 0){	//이미지 파일 사이즈 고정 조정
			rst = fileUtil.imageResize(request.getSession().getServletContext().getRealPath(TEMP_PATH), file, width, height, ratio);
		}else{
			rst = fileUtil.uploadFile(request.getSession().getServletContext().getRealPath(TEMP_PATH), file);
		}
		response.setStatus(200);
	    response.setContentType("text/html");
	    response.setCharacterEncoding("UTF-8");
	    response.getWriter().print("<script type=\"text/javascript\">window.parent.ImageDraw('"+params.get("fileNumber")+"','"+rst.get("yyyy")+"','"+rst.get("mm")+"','"+rst.get("uuid")+"','"+rst.get("attach_nm")+"')</script>");
	}
	
	@RequestMapping("/smartEditorUpload.do")
	public void smartEditorUpload(HttpServletRequest request, HttpServletResponse response,
			@RequestParam Map<String, String> params,
			@RequestParam(value = "Filedata") MultipartFile file) throws Exception{
		Map<String, String> map = fileUtil.imageResize(request.getSession().getServletContext().getRealPath(EDITOR_PATH), file, 1025, 9999, true);
		String rst = params.get("callback")+"?callback_func=" + params.get("callback_func");
		rst += "&bNewLine=true";
		rst += "&sFileName=" + map.get("attach_nm");
		rst += "&sFileURL="+HOME_URL+"/upload/editor/" + map.get("uuid");
		response.sendRedirect(rst);
	}
	
	@RequestMapping("/excelUpload.do")
	public String excelUpload(HttpServletRequest request, 
			@RequestParam Map<String, String> params,
			@RequestParam(value = "file") MultipartFile file, 
			@RequestParam(value = "width", defaultValue="0") int width, 
			@RequestParam(value = "height", defaultValue="0") int height,
			@RequestParam(value = "ratio", defaultValue="true") boolean ratio) throws Exception{
		Map rst = null;
		rst = fileUtil.uploadExcelFile(request.getSession().getServletContext().getRealPath(TEMP_PATH), file);
		request.setAttribute("json", rst);
		return "json";
	}
}
