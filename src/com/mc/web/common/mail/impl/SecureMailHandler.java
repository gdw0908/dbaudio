package com.mc.web.common.mail.impl;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;


/**
 * 보안 적용된 메일 처리용 핸들러
 *
 * @author sdk
 *
 */
public class SecureMailHandler extends AbstractMailHandler {

	private String id = null;		// 사용자 아이디
	private String password = null;	// 사용자 암호

	/**
	 * 사용자 아이디, 암호를 입력
	 *
	 * @param id 아이디
	 * @param password 암호
	 */
	public SecureMailHandler(String id, String password){
		this.id = id;
		this.password = password;
	}

	protected void initializeMailServer() {
		Properties properties = new Properties();
		properties.put("mail.smtp.host", mailServer);
		properties.put("mail.smtp.localhost", mailServer);
		//properties.put("mail.smtp.socketFactory.port", "25");
		//properties.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.port", mailServerPort);

		Authenticator auth = new SecureAuthenticator(id, password);
		Session s = Session.getDefaultInstance(properties, auth);
		message = new MimeMessage(s);
	}

	protected void initializeSender() throws UnsupportedEncodingException {
		if(senderName == null)		// 보내는 사람 표시 이름을 초기화 하지 않으면 메일주소로 초기화
			senderName = sender;

		senderAddress = new InternetAddress(sender, MimeUtility.encodeText(senderName, "UTF-8", "B"));	// 표시 이름은 인코딩해서 표현
	}

	protected void initializeReceiver() throws AddressException {
		makeReceiverAddress(";");
	}

	protected void initializeReceivers() throws AddressException {
		makeReceiverAddresss(receivers);
	}
}
