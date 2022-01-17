package com.mc.web.common.mail;


import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mc.common.util.Util;
import com.mc.web.Globals;
import com.mc.web.mail.MailSendDAO;
import com.mc.web.common.mail.impl.PlainMailHandler;
import com.mc.web.common.mail.impl.SecureMailHandler;

@Repository
public class SendMailThread
{
	@Resource(name = "globals")
	private Globals globals;
	
	@Autowired
	private MailSendDAO mailSendDAO;
	 
	public void run()
	{
		MailHandler mail = null;
		if("Y".equals(globals.getEMAIL_AUTH_YN()))
			mail = new SecureMailHandler(globals.getEMAIL_ID(), globals.getEMAIL_PASSWD());
		else
			mail = new PlainMailHandler();
		
		mail.setMailServer(globals.getEMAIL_IP());
		mail.setMailServerPort(globals.getEMAIL_PORT());
		mail.setSenderName(fromNm);
		mail.setSender(fromEmail);
		mail.setSubject(title);
		mail.setContent(Util.reHtmlSpecialChars(conts));

		try
		{
			for(int i=0; i<targetList.size(); i++)
			{
				Thread.sleep(1);
				mail.setReceiver((String)((Map) targetList.get(i)).get("email"));
				try{
					mail.SendMail();
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally{
			if(!"Pass".equals(qPass)) mailSendDAO.updateEnd(seq);
		}
	}
	private List targetList;

	private String fromNm;
	private String fromEmail;
	private String title;
	private String conts;
	private String seq;
	private String qPass;

	public List getTargetList() {
		return targetList;
	}
	public void setTargetList(List targetList) {
		this.targetList = targetList;
	}
	public String getFromNm() {
		return fromNm;
	}
	public void setFromNm(String fromNm) {
		this.fromNm = fromNm;
	}
	public String getFromEmail() {
		return fromEmail;
	}
	public void setFromEmail(String fromEmail) {
		this.fromEmail = fromEmail;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getConts() {
		return conts;
	}
	public void setConts(String conts) {
		this.conts = conts;
	}
	public String getSeq() {
		return seq;
	}
	public void setSeq(String seq) {
		this.seq = seq;
	}
	public void qPass(String qPass)
	{
		this.qPass = qPass;
	}
}
