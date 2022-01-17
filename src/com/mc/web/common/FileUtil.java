package com.mc.web.common;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

import net.coobird.thumbnailator.Thumbnails;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.mc.common.util.DateUtil;
import com.mc.web.Globals;
/**
 *
 * @Description : 
 * @ClassName   : com.vd.web.common.FileUtil.java
 * @Modification Information
 *
 * @author 이창기
 * @since 2014. 9. 26.
 * @version 1.0 *  
 * Copyright (C)  All right reserved.
 */
@Component
public class FileUtil {
	Logger log = Logger.getLogger(this.getClass());
	/**
	 * 
	 * Comment  :  
	 * @version : 1.0
	 * @tags    : @param path
	 * @tags    : @param file
	 * @tags    : @return
	 * @tags    : @throws Exception
	 * @date    : 2014. 9. 26.
	 *
	 */
	public Map<String, String> uploadFile(String path, MultipartFile file) throws Exception{
		Map<String, String> map = new LinkedHashMap<String, String>();
		if(file == null || file.getSize() == 0)
			return null;
		String uuid = UUID.randomUUID().toString();
		File dir = new File(path);
		if(dir.exists() == false){
			dir.mkdirs();
		}
		file.transferTo(new File(path + "/" + uuid));
		map.put("uuid", uuid);
		map.put("attach_nm", file.getOriginalFilename());
		map.put("size", String.valueOf(file.getSize()));
		map.put("yyyy", DateUtil.getTime("yyyy"));
		map.put("mm", DateUtil.getTime("MM"));
		return map;
	}
	
	/**
	 * 
	 * Comment  : 이미지 파일체크 썸네일
	 * @version : 1.0
	 * @tags    : @param path
	 * @tags    : @param file
	 * @tags    : @return
	 * @tags    : @throws Exception
	 * @date    : 2014. 11. 13.
	 *
	 */
	public Map<String, String> uploadFileImage(String path, MultipartFile file) throws Exception{
		String[] fileSplit = file.getOriginalFilename().split("\\.");
		String extension = fileSplit[fileSplit.length - 1].toLowerCase();
		if (file.getContentType().startsWith("image") == false 
				|| !Globals.IMAGE_EXTENTSIONS.contains(extension)){//이미지가 아니면 삭제
			File delFile = new File(path, file.getName());
			delFile.delete();
			throw new Exception("이미지 파일이 아닙니다.");
		}
		
		Map<String, String> map = new LinkedHashMap<String, String>();
		if(file == null || file.getSize() == 0)
			return null;
		String uuid = UUID.randomUUID().toString();
		File dir = new File(path);
		if(dir.exists() == false){
			dir.mkdirs();
		}
		file.transferTo(new File(path + "/" + uuid));
		map.put("uuid", uuid);
		map.put("attach_nm", file.getOriginalFilename());
		map.put("size", String.valueOf(file.getSize()));
		map.put("yyyy", DateUtil.getTime("yyyy"));
		map.put("mm", DateUtil.getTime("MM"));
		return map;
	}
	
	/**
	 * 
	 * Comment  : 이미지 리사이징 
	 * @version : 1.0
	 * @tags    : @param path
	 * @tags    : @param file
	 * @tags    : @param width
	 * @tags    : @param height
	 * @tags    : @return
	 * @tags    : @throws Exception
	 * @date    : 2014. 12. 3.
	 *
	 */
	public Map<String, String> imageResize(String path, MultipartFile file, int width, int height, boolean ratio) throws Exception{
		String[] fileSplit = file.getOriginalFilename().split("\\.");
		String extension = fileSplit[fileSplit.length - 1].toLowerCase();
		if (file.getContentType().startsWith("image") == false 
				|| !Globals.IMAGE_EXTENTSIONS.contains(extension)){//이미지가 아니면 삭제
			File delFile = new File(path, file.getName());
			delFile.delete();
			throw new Exception("이미지 파일이 아닙니다.");
		}
		
		Map<String, String> map = new LinkedHashMap<String, String>();
		if(file == null || file.getSize() == 0)
			return null;
		String uuid = UUID.randomUUID().toString();
		File dir = new File(path);
		if(dir.exists() == false){
			dir.mkdirs();
		}
		
		BufferedImage bi = ImageIO.read(file.getInputStream());
		if(bi.getWidth() > width || bi.getHeight() > height){
			File thumb = new File(dir, uuid);
			Thumbnails.of(bi).size(width, height).keepAspectRatio(ratio).outputFormat("jpg").toFile(thumb);
		}else{
			file.transferTo(new File(path + "/" + uuid));
		}
		map.put("uuid", uuid);
		map.put("attach_nm", file.getOriginalFilename());
		map.put("size", String.valueOf(file.getSize()));
		map.put("yyyy", DateUtil.getTime("yyyy"));
		map.put("mm", DateUtil.getTime("MM"));
		return map;
	}
	

	public Map<String, String> imageResize2(String path, MultipartFile file, int width, int height, boolean ratio) throws Exception{
		String[] fileSplit = file.getOriginalFilename().split("\\.");
		String extension = fileSplit[fileSplit.length - 1].toLowerCase();
		if (file.getContentType().startsWith("image") == false 
				|| !Globals.IMAGE_EXTENTSIONS.contains(extension)){//이미지가 아니면 삭제
			File delFile = new File(path, file.getName());
			delFile.delete();
			throw new Exception("이미지 파일이 아닙니다.");
		}
		
		Map<String, String> map = new LinkedHashMap<String, String>();
		if(file == null || file.getSize() == 0)
			return null;
		String uuid = UUID.randomUUID().toString();
		File dir = new File(path);
		if(dir.exists() == false){
			dir.mkdirs();
		}
		
		BufferedImage bi1 = ImageIO.read(file.getInputStream());
		int width2 = bi1.getWidth();
		int height2 = bi1.getHeight();
		BufferedImage bi = rotateMyImage(bi1, 90.0);
		ImageIO.write(bi, extension, new File(dir, uuid));
		map.put("uuid", uuid);
		map.put("attach_nm", file.getOriginalFilename());
		map.put("size", String.valueOf(file.getSize()));
		map.put("yyyy", DateUtil.getTime("yyyy"));
		map.put("mm", DateUtil.getTime("MM"));
		return map;
	}
	
	public BufferedImage rotateMyImage(BufferedImage img, double angle) {
		int w = img.getWidth();
		int h = img.getHeight();
		BufferedImage dimg =new BufferedImage(w, h, img.getType());
		Graphics2D g = dimg.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,	RenderingHints.VALUE_ANTIALIAS_ON);
		g.rotate(Math.toRadians(angle), w/2, h/2);		 
		g.drawImage(img, null, 0, 0);
		return dimg;
	}
	
	
	public int copy(String fileFrom, String fileTo, String path) throws IOException{
		int rst = 0;
        InputStream in = null;
        OutputStream out = null;
        try{
            //파일을 읽고 쓸 때 2048Byte의 크기를 갖는 버퍼스트림으로 버퍼링하면서 파일을 복사한다.
        	File dir = new File(path);
    		if(dir.exists() == false){
    			dir.mkdirs();
    		}
            in = new FileInputStream(fileFrom);
            out = new FileOutputStream(fileTo);
            //파일크기를 계산한다.
            int availableLength = in.available();
            //파일 크기만큼의 byte[] 형태의 버퍼를 생성한다.
            byte[] buffer = new byte[availableLength];
            //버퍼로 파일을 읽어들인다.

            int bytedata = in.read(buffer);
            //버퍼에 읽어들인 내용을 파일로 저장한다.
            out.write(buffer);
            rst=1;
        }catch(FileNotFoundException e){
        	rst=-1;
        	log.warn(fileFrom+"(지정된 파일을 찾을 수 없습니다.) 임시테이블에서 복사해오는중에 파일이 없을수도 있습니다.");
        }finally{
            if(in != null) in.close();;
            if(out != null) out.close();
        }
        return rst;
    }
	
	public void delete(String path) throws IOException{
		File dir = new File(path);
		if(dir.isDirectory() == false){
			dir.delete();
		}
	}
	
	
	
	public List<Map<String, String>> upload(List<MultipartFile> fileList, int limit_file_size, String upload_path) throws Exception{
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		if(fileList == null) return result;
		
		String yyyy = DateUtil.getTime("yyyy");
		String mm = DateUtil.getTime("MM");
		String path = upload_path + "/" + yyyy + "/" + mm + "/";
		File dir = new File(path);
		if(dir.isDirectory() == false){
			dir.mkdirs();
		}
		
		File destinationFile;
		String uuid;
		Map<String, String> fileMap;
		for(MultipartFile file : fileList){
			if(file.isEmpty()) continue;
			if(file.getSize() > (limit_file_size * 1024 * 1024)) continue;
			fileMap = new HashMap<String, String>();
			fileMap.put("yyyy", yyyy);
			fileMap.put("mm", mm);
			uuid = UUID.randomUUID().toString();
			destinationFile = new File(path + uuid);
			file.transferTo(destinationFile);
			fileMap.put("uuid", uuid);
			fileMap.put("attach_nm", file.getOriginalFilename());
			result.add(fileMap);
		}
		
		return result;
	}
	
	public Map<String, String> upload(MultipartFile file, String path, String upload_path) throws Exception{
		Map<String, String> result = new HashMap<String, String>();
		if(file == null) return result;
		
		path = upload_path + "/" + path + "/";
		File dir = new File(path);
		if(dir.isDirectory() == false){
			dir.mkdirs();
		}
		
		File destinationFile;
		if(file.isEmpty()) return result;
		
		String org_file_name = file.getOriginalFilename();
		int idx = org_file_name.lastIndexOf(".");
		String file_name = UUID.randomUUID().toString() + org_file_name.substring(idx);
		String uuid = UUID.randomUUID().toString();
		destinationFile = new File(path + uuid);
		
		file.transferTo(destinationFile);
		
		result.put("storage_file_name", file_name);
		result.put("uuid", uuid);
		result.put("real_file_name", file.getOriginalFilename());
		
		return result;
	}
	
	
	public Map<String, String> uploadExcelFile(String path, MultipartFile file) throws Exception{
		Map<String, String> map = new LinkedHashMap<String, String>();
		if(file == null || file.getSize() == 0)
			return null;
		String uuid = UUID.randomUUID().toString();
		File dir = new File(path);
		if(dir.exists() == false){
			dir.mkdirs();
		}
		String org_file_name = file.getOriginalFilename();
		int idx = org_file_name.lastIndexOf(".");
		String file_name = uuid+org_file_name.substring(idx);
		file.transferTo(new File(path + "/" + file_name));
		map.put("attach_nm", file_name);
		return map;
	}
	
	public int thumb(String orgpath, String thumbpath, int width, int height, boolean ratio) {
		log.info("Start Orignal ===> " + orgpath);
		
		int rst = 0; //-1 실패 1 성공
		InputStream in = null;
		try {
			if(orgpath == null || orgpath.length() == 0) return -1; //file 이 없으면 메소드 탈출
			in = new FileInputStream(orgpath);
			BufferedImage bi = ImageIO.read(in);
			
			if(bi.getWidth() > width || bi.getHeight() > height) //원본 가로,세로 가 전달받은 가로,세로 보다 크면
			{
				File thumb = new File(thumbpath);
				Thumbnails.of(bi).size(width, height).keepAspectRatio(ratio).outputFormat("jpg").toFile(thumb);
				log.info("End Thumb ===> " + thumbpath);
				rst = 1;
			}
        } catch (Exception e) {
        	rst = -1;
        	log.warn("이미지 파일이 아닙니다.");
        	//e.printStackTrace();
        }finally{
		}

		return rst;
	}
	
	public int compulsion_thumb(String orgpath, String thumbpath, int width, int height, boolean ratio) {
		log.info("Start Orignal ===> " + orgpath);
		
		int rst = 0; //-1 실패 1 성공
		InputStream in = null;
		try {
			if(orgpath == null || orgpath.length() == 0) return -1; //file 이 없으면 메소드 탈출
			in = new FileInputStream(orgpath);
			BufferedImage bi = ImageIO.read(in);
			
			//if(bi.getWidth() > width || bi.getHeight() > height) //원본 가로,세로 가 전달받은 가로,세로 보다 크면
			//{
				File thumb = new File(thumbpath);
				Thumbnails.of(bi).size(width, height).keepAspectRatio(ratio).outputFormat("jpg").toFile(thumb);
				log.info("End Thumb ===> " + thumbpath);
				rst = 1;
			//}
        } catch (Exception e) {
        	rst = -1;
        	log.warn("이미지 파일이 아닙니다.");
        	//e.printStackTrace();
        }finally{
		}

		return rst;
	}
}
