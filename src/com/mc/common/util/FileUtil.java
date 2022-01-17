package com.mc.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class FileUtil {

	public static boolean isFile(String fileName, String dir){
		
		boolean result = false;
		
		File file = new File(dir , fileName);
		if(file.exists()){
			result = true;
		}
		return result;
	}
	
	public static String Rename(String oldFileName, String newFileName, String dir){
		
		String result = null;
		
		String fileExtName = oldFileName.substring(oldFileName.lastIndexOf(".")+1);
		
		File file = new File(dir , oldFileName);
		
		if(file.exists()){
			file.renameTo(new File(dir , newFileName + "." + fileExtName));
			result = newFileName + "." + fileExtName;
		}
		
		return result;
	}
	
	public static String Rename(String oldSocid, String oldFileName, String newFileName, String dir){
		
		String result = null;
		
		String fileExtName = oldFileName.substring(oldFileName.lastIndexOf(".")+1);
		
		File file = new File(dir , oldFileName);
		
		if(file.exists()){
			if(!oldSocid.equals(newFileName)){
				file.renameTo(new File(dir , newFileName + "." + fileExtName));
				result = newFileName + "." + fileExtName;
			}else{
				file.renameTo(new File(dir , oldSocid + "." + fileExtName));
				result = oldSocid + "." + fileExtName;				
			}
		}
		
		return result;
	}
	
	 /**
	   * 디렉토리에 서브디렉토리 존재하지 않을시 모두 삭제됨, 서브 디렉토리 존재할 경우
	   * 파일만 삭제됨
	   * @param targetDir 삭제할 디렉토리
	   * @return 성공여부
	   */
	  public static boolean delDirAll( String targetDir ){
	      return delDirAll(targetDir, false);
	  }

	  /**
	   * 디렉토리를 통째로 삭제한다. !! 주위 - 통째로 다 삭제 될수 있음...신중히 사용
	   * ==> 왠만하면 : delDirAll( 경로, false ) 로 사용
	   * @param targetDir 삭제할 디렉토리
	   * @param bstate 서브디렉토리 삭제여부
	   * @return 성공여부
	   */
	  public static boolean delDirAll(String targetDir, boolean bstate){
	      try{
	        File tFile = new File(targetDir);
	        if( ! tFile.isDirectory() ){            
	            return false;
	        }

	        String lists[] = tFile.list() ; // 해당디렉토리에 있는 리스트
	        for( int i=0 ; i<lists.length ; i++){
	            File tmpFileDir = new File(targetDir + File.separator + lists[i]);
	            if( tmpFileDir.isDirectory() ){
	                if( bstate == true){
	                    delDirAll( targetDir + File.separator + lists[i] , bstate ); //재귀호출
	                }
	            }else{
	                tmpFileDir.delete() ; //해당파일삭제
	            }
	        }
	        tFile.delete();

	        return true;
	      }catch(Exception e){
	        System.out.println("Exception]" + e );
	        return false;
	      }

	  }
	
	public static boolean deleteFile(String fileName, String dir){
		
		boolean result = false;
		
		File deleteFile = new File(dir + "/" + fileName);
		if(deleteFile.exists()){
			deleteFile.delete();
			
			result = true;
		}
		
		return result;
	}
	
	public static boolean createDir(String path) {
		    
		String reName = "";
		boolean flage  = false;
		File createDir = new File(path);
		
		if( ! createDir.isDirectory() ){
			createDir.mkdirs();
			flage  = true;
		}
		return flage ;
	}
	
	public static boolean saveFile(String dir, String file, String txt){
		boolean result = false;
		
		FileUtil.createDir(dir);
		
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		try {
			createDir(dir);
			fos = new FileOutputStream(dir+file);
			osw = new OutputStreamWriter(fos, "UTF-8");
			
			osw.write(txt);
			
			result = true;
		} catch (Exception e) {

		}finally{
			try {
				osw.flush();
				osw.close();
				fos.flush();
				fos.close();				
			} catch (Exception e) {

			}
		}
		return result;
	}
	
	public static String saveFileAutoRename(String dir, String txt){
		String result = null;
		String fileNm = Util.getCurrentDate();
		String ext = ".jsp";
		int count = 0;

		FileUtil.createDir(dir);
		
        while (isFile(fileNm+ext, dir) && count < 9999) {
            count++;
            fileNm = Util.getCurrentDate() + "_" + count;
        }
        
		//BufferedWriter out = null;
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		
		try {
			fos = new FileOutputStream(dir+fileNm+ext);
			osw = new OutputStreamWriter(fos, "UTF-8");
			//out = new BufferedWriter(osw);
			
			osw.write(txt);
			//out.write(Util.toUTF8(txt));
			
			result = fileNm+ext;
		} catch (Exception e) {
			
		}finally{
			try {
				//out.flush();
				//out.close();
				osw.flush();
				osw.close();
				fos.flush();
				fos.close();				
			} catch (Exception e) {
				
			}
		}
		return result;
	}
	
	 //파일을 복사하는 메소드
	 public static void fileCopy(String inFileName, String outFileName) {
	  try {
	   FileInputStream fis = new FileInputStream(inFileName);
	   FileOutputStream fos = new FileOutputStream(outFileName);
	   
	   int data = 0;
	   while((data=fis.read())!=-1) {
	    fos.write(data);
	   }
	   fis.close();
	   fos.close();
	   
	  } catch (IOException e) {
	   // TODO Auto-generated catch block
	   e.printStackTrace();
	  }
	 }
}
