package com.mc.web.common.mail.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeUtility;

import com.mc.web.common.mail.MailHandler;

/**
 * 메일 핸들러 추상 클래스
 *
 * @author sdk
 *
 */
public abstract class AbstractMailHandler implements MailHandler {

	protected String mailServer = null;	// E-MAIL 서버 주소
	protected String mailServerPort = "25";	// E-MAIL 서버 주소
	protected Message message = null;		// 보내는 메일 메시지

	protected String sender = null;	// 보내는 사람 이메일 주소
	protected String senderName = null;	// 보내는 사람 표시 이름
	protected String subject = null;	// 제목
	protected String content = null;	// 본문
	protected String receiver = null;	// 받는 사람 리스트

	List receivers = null;

	protected Address senderAddress = null;		// 보내는 사람 주소
	protected Address[] receiverAddress = null;	// 받는 사람 주소

	public void SendMail() throws UnsupportedEncodingException, MessagingException
	{
		if(sender == null || subject == null || content == null || receiver == null)
			throw new NullPointerException("sender, subject, content, receiver is null.");

		initializeMailServer();
		initializeSender();
		initializeReceiver();

		Send();
	}

	public void SendMails() throws UnsupportedEncodingException, MessagingException {
		if(sender == null || subject == null || content == null || receivers == null)
			throw new NullPointerException("sender, subject, content, receiver is null.");

		initializeMailServer();
		initializeSender();
		initializeReceivers();

		Send();

	}
	/**
	 * 메일 서버 초기화
	 * Message 객체 생성
	 *
	 */
	protected abstract void initializeMailServer();

	/**
	 * 보내는 사람 초기화
	 * @return
	 *
	 * @throws UnsupportedEncodingException 표시 이름이 인식할 수 없는 문자(Base 64 기준)일때 발생
	 */
	protected abstract void initializeSender() throws UnsupportedEncodingException;

	/**
	 * 받는 사람 초기화
	 *
	 * @throws AddressException 받는 사람이 인식 할 수 없는 이름일 때 발생
	 */
	protected abstract void initializeReceiver() throws AddressException;

	protected abstract void initializeReceivers() throws AddressException;

	/**
	 * 메일 전송
	 *
	 * @throws MessagingException 메시지 초기화중 오류 발생
	 * @throws UnsupportedEncodingException 메일 제목이 인식할 수 없는 문자일 때 발생
	 */
	protected void Send() throws MessagingException, UnsupportedEncodingException{
		if(message == null)
			throw new NullPointerException("message is null.");

		message.setHeader("content-type", "text/html;charset=UTF-8");
		message.setFrom(senderAddress);
		message.setRecipients(Message.RecipientType.TO, receiverAddress);	// 받는 사람 타입 (TO, CC, BCC)

		message.setSubject(MimeUtility.encodeText(subject, "UTF-8", "B"));
		message.setContent(content, "text/html;charset=UTF-8");
		message.setSentDate(new java.util.Date());

		Transport.send(message);
	}


	/**
	 * 사용자가 입력한 받는 사람 목록(String)을 Address 형의 목록으로 변환
	 *
	 * @param delim 받는 사람 목록 구분자
	 * @throws AddressException Address 형으로 변경주 발생
	 */
	protected void makeReceiverAddress(String delim) throws AddressException{
		ArrayList receiverList = new ArrayList();
		StringTokenizer stMailAddress = new StringTokenizer(receiver, delim);

		while (stMailAddress.hasMoreTokens()) {
			receiverList.add(stMailAddress.nextToken());
		}

		receiverAddress = new Address[receiverList.size()];

		for (int i = 0; i < receiverList.size(); i++) {
			receiverAddress[i] = new InternetAddress((String) receiverList.get(i));
		}
	}

	protected void makeReceiverAddresss(List list) throws AddressException
	{
		receiverAddress = new Address[list.size()];

		for(int i=0; i<list.size(); i++){
			try {
				receiverAddress[i] = new InternetAddress((String)((Map) list.get(i)).get("email"), (String)((Map) list.get(i)).get("name"), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
	}

	public void setMailServer(String mailServer){
		this.mailServer = mailServer;
	}

	public void setMailServerPort(String mailServerPort){
		this.mailServerPort = mailServerPort;
	}

	public void setSender(String sender){
		this.sender = sender;
	}

	public void setSenderName(String senderName){
		this.senderName = senderName;
	}

	public void setSubject(String subject){
		this.subject = subject;
	}

	public void setContent(String content){
		this.content = content;
	}

	public void setReceiver(String receiver){
		this.receiver = receiver;
	}

	public void setReceivers(List receivers) {
		this.receivers = receivers;
	}

	public String getMailServer() {
		return mailServer;
	}

	public String getMailServerPort() {
		return mailServerPort;
	}

	public String getSender() {
		return sender;
	}

	public String getSenderName() {
		return senderName;
	}

	public String getSubject() {
		return subject;
	}

	public String getContent() {
		return content;
	}

	public String getReceiver() {
		return receiver;
	}

	public List getReceivers() {
		return receivers;
	}
}
