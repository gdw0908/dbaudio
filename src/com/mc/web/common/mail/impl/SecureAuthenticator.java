package com.mc.web.common.mail.impl;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * 인증자
 * 아이디와 암호를 입력받아 인증자를 성성
 *
 * @author sdk
 *
 */
public class SecureAuthenticator extends Authenticator {

	private String id = null;
	private String password = null;

	public SecureAuthenticator(String id, String password) {
		this.id = id;
		this.password = password;
	}

	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(id, password);
	}
}
