package com.mc.web.schedule;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mc.web.common.MyDefaultContext;

/**
 * 
 * @Description : 임시저장파일 하루지난거 삭제 스케쥴러
 * @ClassName   : com.mc.web.schedule.TempFileCleaning.java
 * @Modification Information
 *
 * @author 이창기
 * @since 2014. 11. 20.
 * @version 1.0 *  
 * Copyright (C)  All right reserved.
 */
@Controller
public class TempFileCleaning {
	protected final Logger logger = Logger.getLogger( this.getClass() );
	
	private String BASE_PATH;
	
	@Value("#{config['upload.temp']}")
	private String TEMP_PATH;

	@Resource(name = "myDefaultContext")
	private MyDefaultContext myDefaultContext;

	@ResponseBody
	@RequestMapping("/TempFileCleaning.do")
	public String now() {
		long start = System.currentTimeMillis();
		delFiles();
		long end = System.currentTimeMillis();
		return "걸린 시간 : " + (end - start) + " Millisecond";
	}
	
	//second, minute, hour, day, month, weekday
//	@Scheduled(cron="*/10 * * * * ?")//test 10초마다
	@Scheduled(cron="0 0 01 * * ?")//매일 새벽 1시 작어
	public void delFiles() {
		BASE_PATH = myDefaultContext.getBase();
		try {
			if(BASE_PATH == null){
				new Exception();
			}
			String full_path = BASE_PATH + TEMP_PATH;
			long start = System.currentTimeMillis();
			delFiles(full_path);
			long end = System.currentTimeMillis();
			logger.info(full_path + " 걸린 시간 : " + (end - start) + " Millisecond");
		} catch (Exception e) {
        	logger.error(e);
		}
	}
	
	public void delFiles(String path) throws Exception {
        File dir = new File(path);
        File[] files = dir.listFiles();
        for (File file : files) {
             if (file.isFile()) {
             	if(isDel(file)){
             		file.delete();
             		logger.info("삭제:"+file.getName());
             	}
             } else if (file.isDirectory()) {
                 try {
                     //showFileList(file.getCanonicalPath().toString());
                 } catch (Exception e) {
                 	logger.error(e);
                 }
             }
		}
    }
	
	/**
	 * Comment  : 삭제할 파일검증
	 * 				하루지난 파일인지와 글자수가 36자인 파일만 삭제 
	 * @version : 1.0
	 * @tags    : @param f
	 * @tags    : @return
	 * @date    : 2014. 11. 20.
	 *
	 */
	public boolean isDel(File f){
		Calendar cal = new GregorianCalendar();
		cal.add(Calendar.DATE, -1);
		Date sdate = cal.getTime();
		Date fdate = new Date(f.lastModified());
		return (sdate.getTime()  > fdate.getTime()) && f.getName().length()==36;
	}
}
