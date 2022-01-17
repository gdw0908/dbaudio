package com.mc.web.attach;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mc.web.DownloadUtil;
import com.mc.web.Globals;
import com.mc.web.MCController;
import com.mc.web.MCMap;

@Controller
public class AttachController extends MCController{
	@Autowired
	private AttachDAO attachDAO;
	
	@Resource(name = "globals")
	private Globals globals;
/*	
	@RequestMapping("/upload.do")
	public String upload(@RequestParam Map<String, String> params, @RequestParam("file") List<MultipartFile> fileList, HttpSession session, HttpServletRequest request) throws Exception{
		MCMap member = (MCMap) session.getAttribute("member");
		if(member == null){
			request.setAttribute("message", globals.REQUIRED_LOGIN);
			request.setAttribute("redirect", globals.getACTION_NAME()+"?menu_id="+globals.LOGIN_MENU_ID+"&return_menu_id="+params.get("menu_id"));
			return "message";
		}
		params.put("session_member_seq", (String) member.get("member_seq"));
		params.put("session_member_id", (String) member.get("member_id"));
		params.put("session_member_nm", (String) member.get("member_nm"));
		
		
		Calendar cal = Calendar.getInstance();
		params.put("yyyy", String.valueOf(cal.get(Calendar.YEAR)));
		
		String path = globals.getFILE_IN_PATH() + "/" + params.get("yyyy") + "/";
		File dir = new File(path);
		if(dir.isDirectory() == false){
			dir.mkdirs();
		}
		
		JSONArray array = new JSONArray();
		JSONObject obj;
		String uuid;
		BufferedImage bi;
		int width = Integer.parseInt(params.get("width"));
		File destinationFile;
		for(MultipartFile file : fileList){
			if(file.isEmpty()) continue;
			
			uuid = UUID.randomUUID().toString();
			destinationFile = new File(path + uuid);
			
			if(file.getContentType().startsWith("image") == false){
				file.transferTo(destinationFile);
			}else{
				bi = ImageIO.read(file.getInputStream());
				if(bi.getWidth() > width)
					Thumbnails.of(file.getInputStream()).width(width) 
					.toFile(destinationFile);
				else
					file.transferTo(destinationFile);
			}
			
			params.put("uuid", uuid);
			params.put("attach_nm", file.getOriginalFilename());
			attachDAO.regist(params);
			
			obj = new JSONObject();
			obj.putAll(params);
			array.add(obj);
		}
		request.setAttribute("json", array.toString());
		return "json";
	}
	
	@RequestMapping("/imageUpload.do")
	public String imageUpload(@RequestParam Map<String, String> params, @RequestParam("file") List<MultipartFile> fileList, HttpServletRequest request) throws Exception{
		String path = request.getSession().getServletContext().getRealPath("/upload/image");
		File dir = new File(path);
		if(dir.isDirectory() == false){
			dir.mkdirs();
		}
		
		JSONArray array = new JSONArray();
		JSONObject obj;
		String file_name;
		BufferedImage bi;
		int width = Integer.parseInt(params.get("width"));
		File destinationFile;
		int idx;
		String org_file_name;
		for(MultipartFile file : fileList){
			if(file.isEmpty() || file.getContentType().startsWith("image") == false) continue;
			org_file_name = file.getOriginalFilename();
			idx = org_file_name.lastIndexOf(".");
			file_name = UUID.randomUUID().toString() + org_file_name.substring(idx);
			destinationFile = new File(path + "/" + file_name);
			
			bi = ImageIO.read(file.getInputStream());
			if(bi.getWidth() > width)
				Thumbnails.of(file.getInputStream()).width(width) 
				.toFile(destinationFile);
			else
				file.transferTo(destinationFile);
			
			params.put("uuid", "/upload/image/" + file_name);
			params.put("attach_nm", org_file_name);
			
			obj = new JSONObject();
			obj.putAll(params);
			array.add(obj);
		}
		request.setAttribute("json", array.toString());
		return "json";
	}
	
	@ResponseBody
	@RequestMapping("/ckUploadImage.do")
	public String uploadImg(@RequestParam Map<String, String> params, @RequestParam("upload") MultipartFile file, HttpServletRequest request) throws Exception {
		String uploadPath = request.getSession().getServletContext().getRealPath(globals.getCkeditorImage());
		String result = System.currentTimeMillis() + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
		
		File f = new File(uploadPath);
		if(f.exists() == false){
			f.mkdirs();
		}
		file.transferTo(new File(uploadPath,result));

		String funcNum = params.get("CKEditorFuncNum");
		String imageUPathHost = "http://" + request.getHeader("host");
		String fileUrl = imageUPathHost + globals.getCkeditorImage() + "/" + result;
		String[] fileSplit = result.split("\\.");
		if (!Globals.IMAGE_EXTENTSIONS.contains(fileSplit[fileSplit.length - 1].toLowerCase())){//이미지가 아니면 삭제
			File delFile = new File(uploadPath, result);
			delFile.delete();
		}
		
		String output = "<script>window.parent.CKEDITOR.tools.callFunction('"+ funcNum +"', '"+ fileUrl +"');</script>";
		return output;
	}
*/	
	@RequestMapping("/download.do")
	public void download(@RequestParam Map<String, String> params, HttpServletRequest request, HttpServletResponse response) throws Exception{
		MCMap article = attachDAO.getArticle(params);
		
		if(article == null){
			response.setContentType("text/plain; charset=UTF-8");
			response.getWriter().print("파일 정보가 없습니다.");
			return;
		}
		String path = request.getSession().getServletContext().getRealPath(globals.getFILE_IN_PATH()+"/"+article.get("yyyy")+"/"+article.get("mm")+ "/" + params.get("uuid"));
		DownloadUtil.Download(path , (String) article.get("attach_nm"), request, response);
	}
	
	@RequestMapping("/attachRemove.do")
	public String attachRemove(@RequestParam Map<String, String> params, HttpServletRequest request) throws Exception{
		attachDAO.remove(params);
		request.setAttribute("json", "{\"result\" : true}");
		return "json";
	} 
	
	@RequestMapping("/pre_download.do")
	public void pre_download(@RequestParam Map<String, String> params, HttpServletRequest request, HttpServletResponse response) throws Exception{
		MCMap article = attachDAO.getPreArticle(params);
		
		if(article == null){
			response.setContentType("text/plain; charset=UTF-8");
			response.getWriter().print("파일 정보가 없습니다.");
			return;
		}
		
		DownloadUtil.Download(globals.getFILE_IN_PATH() + "/" + article.get("yyyy") + "/" + article.get("attach_nm"), (String) article.get("attach_nm"), request, response);
	}
	
	@RequestMapping("/direct_download.do")
	public void direct_download(@RequestParam Map<String, String> params, HttpServletRequest request, HttpServletResponse response) throws Exception{
		String sfile_nm = (String) params.get("file_nm");
		String rfile_nm = sfile_nm;
		if(params.containsKey("rfile_nm"))
			rfile_nm = (String) params.get("rfile_nm");
		
		DownloadUtil.Download(globals.getFILE_IN_PATH() + "/" + params.get("path") + "/" + sfile_nm, rfile_nm, request, response);
			
	}
	
	@RequestMapping("/mail_download.do")
	public void mail_download(@RequestParam Map<String, String> params, HttpServletRequest request, HttpServletResponse response) throws Exception{
		String sfile_nm = (String) params.get("file_nm");
		String rfile_nm = sfile_nm;
		String path = request.getSession().getServletContext().getRealPath(globals.getMAIL_FILE_IN_PATH()+ "/" + sfile_nm);
		DownloadUtil.Download(path, sfile_nm, request, response);
	}

}
