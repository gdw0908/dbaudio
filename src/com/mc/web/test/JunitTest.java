package com.mc.web.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/com/mc/resource/applicationContext.xml",
		"classpath:/com/mc/resource/context-common.xml",
		"classpath:/com/mc/resource/context-datasource.xml",
		"classpath:/com/mc/resource/context-sqlMap.xml" })
public class JunitTest {

	@Autowired
	WebApplicationContext wac;

	MockMvc mockMvc;

	@Before
	public void init() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	@Test
	public void test() throws Exception{
		mockMvc.perform(get("/test.do").param("aaa", "fefe"))
			.andDo(MockMvcResultHandlers.print());
	}

	@Test
	public void json() throws Exception{
		mockMvc.perform(get("/test.json"))
		.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void text() throws Exception{
		mockMvc.perform(get("/text.do"))
		.andDo(MockMvcResultHandlers.print());
	}
}