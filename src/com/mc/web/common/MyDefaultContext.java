package com.mc.web.common;

import org.springframework.stereotype.Component;


/**
 *
 * @Description : 파일삭제 스케쥴러에서 쓸 웹루트 경로 저장소
 * @ClassName   : com.vd.web.MyDefaultContext.java
 * @Modification Information
 *
 * @author 이창기
 * @since 2014. 4. 6.
 * @version 1.0 *  
 * Copyright (C)  All right reserved.
 */
@Component
public class MyDefaultContext {

	private static String base_path;
	private volatile static MyDefaultContext uniqueInstance;
	private MyDefaultContext() {}

	public static MyDefaultContext getInstance(String path) {
		if (uniqueInstance == null) {
			synchronized (MyDefaultContext.class) {
				if (uniqueInstance == null) {
					uniqueInstance = new MyDefaultContext(path);
				}
			}
		}
		return uniqueInstance;
	}
	
	private MyDefaultContext(String path) {
		base_path = path;
    }

	public String getBase(){
		return base_path;
	}
	
}