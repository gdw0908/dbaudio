<%@ page import="aegis.cashclient.*,java.text.*,java.net.*,java.lang.*" contentType="text/html; charset=utf-8" %>
<%
/****************************************************************************
*
* ENCTYPE	: C 현금영수증
* LOCALADDR	: PG서버와 통신을 담당하는 암호화Process가 위치해 있는 IP (220.85.12.74)
* LOCALPORT	: 포트
*
****************************************************************************/
String LOCALADDR = "220.85.12.74";
int LOCALPORT = 29760;
String ENCTYPE = "0";

/****************************************************************************
*
* 승인/취소에 사용될 클래스 객체 생성
*
****************************************************************************/

CashClientBean ccb = new CashClientBean(LOCALADDR, LOCALPORT );

/*************************************************************************
* AGSCash.html 으로 부터 넘겨받을 데이터
**************************************************************************/

String Pay_kind = ccb.null2Space( request.getParameter("Pay_kind") );					//결제종류

String Pay_type = ccb.null2Space( request.getParameter("Pay_type") );				   	//결제방식

String Retailer_id = ccb.null2Space( request.getParameter("Retailer_id") );				//상점아이디

String Cust_no = ccb.null2Space( request.getParameter("Cust_no") );						//고객아이디

String Ord_No = ccb.null2Space( request.getParameter("Order_no") );						//주문번호

String Cat_id = ccb.null2Space( request.getParameter("Cat_id") );						//단말기번호

String deal_won = ccb.null2Space( request.getParameter("deal_won") );					//공급가액

String Amtcash = ccb.null2Space( request.getParameter("Amtcash") );						//거래금액

String Amttex = ccb.null2Space( request.getParameter("Amttex") );						//부가가치세

String Amtadd = ccb.null2Space( request.getParameter("Amtadd") );			            //봉사료

String prod_set = ccb.null2Space( request.getParameter("prod_set") );			        //상품갯수

String prod_nm = ccb.null2Space( request.getParameter("prod_nm") );	   //상품명

String Gubun_cd = ccb.null2Space( request.getParameter("Gubun_cd") );			        //거래자구분

String Confirm_no = ccb.null2Space( request.getParameter("Confirm_no") );			    //신분확인번호

String Org_adm_no = ccb.null2Space( request.getParameter("Org_adm_no") );	            //취소시 원거래 승인번호

String Email = ccb.null2Space( request.getParameter("Email") );							//이메일주소

String Corp_no = ccb.null2Space( request.getParameter("Corp_no") );						//사업자번호

String Corp_nm = ccb.null2Space( request.getParameter("Corp_nm") );						//상점명

String Url = ccb.null2Space( request.getParameter("Url") );								//URL

String Ceo_nm = ccb.null2Space( request.getParameter("Ceo_nm") );						//대표자명

String Addr = ccb.null2Space( request.getParameter("Addr") );							//주소

String Tel_no = ccb.null2Space( request.getParameter("Tel_no") );						//연락처

/**************************************************************************			
* 리턴변수 선언 - 알림메시지, 응답메시지1, 응답메시지2 승인번호,주문번호, 오류사유
***************************************************************************/
String Alert_msg1 = "";     
String Alert_msg2 = "";
String Adm_no = "";
String Dealno = "";
String Success = "";
String rResMsg = "";


	/*******************************************************************************************
	* 
	* Pay_kind = "cash-appr" 현금영수증 승인요청시 
	*
	******************************************************************************************/
	if( Pay_kind.toString().equals( "cash-appr" )){

		/**************************************************************************
		* 승인요청시 전문 생성
		***************************************************************************/	
		ENCTYPE = "C";
		
		try
		{
			/** 승인 요청 처리 **/
			
			if( ccb.cashrecipt( ENCTYPE, Pay_kind,Pay_type, Retailer_id, Cust_no, Ord_No, Cat_id, 
				Amtcash, Amttex, Amtadd, Gubun_cd, Confirm_no, Email, prod_nm ) )
			{
			
				String rSplitData[] = new String[7];
				rSplitData = ccb.getRecvData();

				Retailer_id = rSplitData[0];
				Dealno = rSplitData[1];
				Adm_no = rSplitData[2];
				Success = rSplitData[3];
				rResMsg = rSplitData[4];
				Alert_msg1 = rSplitData[5];
				Alert_msg2 = rSplitData[6];
										
			}
			else
			{
				if( ccb.getErrCd() == -1 )
				{
					Success = "n";
					rResMsg = "통신오류(sock)로 인한 거절";
				}
				else if( ccb.getErrCd() == -2 )
				{
					Success = "n";
					rResMsg = "통신오류(msg)로 인한 거절";
				}
			}
		}
		catch( Exception e )
		{
			/** 기타 이유로 인한 요청 실패 처리 **/
			
			Success = "n";
			rResMsg = "시스템오류로 인한  거절";
		}
	/*******************************************************************************************
	* 
	* Pay_kind = "cash-cncl" 현금영수증 취소요청시 
	*
	******************************************************************************************/
	}else if( Pay_kind.toString().equals( "cash-cncl" )){

		/**************************************************************************
		* 취소요청시 전문 생성
		***************************************************************************/
		ENCTYPE = "C";
		
		try
		{

			/** 취소 요청 처리 **/
			if( ccb.cashreciptcanc( ENCTYPE, Pay_kind,Pay_type, Retailer_id, Cust_no, Ord_No, Cat_id, 
				Amtcash, Amttex, Amtadd, Gubun_cd, Confirm_no, Org_adm_no, Email, prod_nm ) )
			{
					
					String rSplitData[] = new String[7];
					rSplitData = ccb.getSplitData_canc();

					Retailer_id = rSplitData[0];
					Dealno = rSplitData[1];
					Adm_no = rSplitData[2];
					Success = rSplitData[3];
					rResMsg = rSplitData[4];
					Alert_msg1 = rSplitData[5];
					Alert_msg2 = rSplitData[6];
														
			} else	{
				if( ccb.getErrCd() == -1 )
				{
					Success = "n";
					rResMsg = "통신오류(sock)로 인한 거절";
				} else if( ccb.getErrCd() == -2 ) {
					Success = "n";
					rResMsg = "통신오류(msg)로 인한 거절";
				}
			}
		}
		catch( Exception e )
		{
		/** 기타 이유로 인한 요청 실패 처리 **/
		
				Success = "n";
				rResMsg = "시스템오류로 인한  거절";
		}
	/*******************************************************************************************
	* 
	* Pay_kind = "cash-appr-temp" 현금영수증 임시발행 승인요청시 
	*
	******************************************************************************************/
	}else if( Pay_kind.toString().equals( "cash-appr-temp" ) )	   {

		/**************************************************************************
		* 임시발행 승인요청시 전문 생성
		***************************************************************************/	
		ENCTYPE = "C";
		
		try
		{
			/** 임시발행 승인 요청 처리 **/
			
			if( ccb.cashrecipttemp( ENCTYPE, Pay_kind,Pay_type, Retailer_id, Cust_no, Ord_No, Cat_id, 
				Amtcash, Amttex, Amtadd, Gubun_cd, Confirm_no, Email, prod_nm ) )
			{
			
				String rSplitData[] = new String[4];
				rSplitData = ccb.getRecvData();

				Retailer_id = rSplitData[0];
				Dealno = rSplitData[1];
				Success = rSplitData[2];
				rResMsg = rSplitData[3];

			}
			else
			{
				if( ccb.getErrCd() == -1 )
				{
					Success = "n";
					rResMsg = "통신오류(sock)로 인한 거절";
				}
				else if( ccb.getErrCd() == -2 )
				{
					Success = "n";
					rResMsg = "통신오류(msg)로 인한 거절";
				}
			}
		}
		catch( Exception e )
		{
			/** 기타 이유로 인한 요청 실패 처리 **/
			
			Success = "n";
			rResMsg = "시스템오류로 인한  거절";
		}
	/*******************************************************************************************
	* 
	* Pay_kind = "cash-cncl-temp" 현금영수증 임시발행 취소요청시 
	*
	******************************************************************************************/
	}else if( Pay_kind.toString().equals( "cash-cncl-temp" )){

		/**************************************************************************
		* 임시발행 취소요청시 전문 생성
		***************************************************************************/
		ENCTYPE = "C";

		try
		{
		/** 임시발행 취소 요청 처리 **/
				
		if( ccb.cashreciptcanctemp( ENCTYPE, Pay_kind,Pay_type, Retailer_id, Cust_no, Ord_No, Cat_id, 
			Amtcash, Amttex, Amtadd, Gubun_cd, Confirm_no, Org_adm_no, Email, prod_nm ) )
		{
				
				String rSplitData[] = new String[4];
				rSplitData = ccb.getSplitData_canc();

				Retailer_id = rSplitData[0];
				Dealno = rSplitData[1];
				Success = rSplitData[2];
				rResMsg = rSplitData[3];
													
		} else	{
			if( ccb.getErrCd() == -1 )
			{
				Success = "n";
				rResMsg = "통신오류(sock)로 인한 거절";
			} else if( ccb.getErrCd() == -2 ) {
				Success = "n";
				rResMsg = "통신오류(msg)로 인한 거절";
			}
				}
		}
		catch( Exception e )
		{
		/** 기타 이유로 인한 요청 실패 처리 **/

				Success = "n";
				rResMsg = "시스템오류로 인한  거절";
		}
	/*******************************************************************************************
	* 
	* Pay_kind = "non-cash-appr" 미등록 상점 현금영수증 승인요청시 
	*
	******************************************************************************************/
	}else if( Pay_kind.toString().equals( "non-cash-appr" )){

		/**************************************************************************
		* 승인요청시 전문 생성
		***************************************************************************/	
		ENCTYPE = "C";
		
		try
		{
			/** 승인 요청 처리 **/
			
			if( ccb.noncashrecipt( ENCTYPE, Pay_kind,Pay_type, Retailer_id, Cust_no, Ord_No, Cat_id, 
				Amtcash, Amttex, Amtadd, Gubun_cd, Confirm_no, Email, prod_nm, Corp_no, Corp_nm, Url, Ceo_nm, Addr, Tel_no ) )
			{
			
				String rSplitData[] = new String[7];
				rSplitData = ccb.getRecvData();

				Retailer_id = rSplitData[0];
				Dealno = rSplitData[1];
				Adm_no = rSplitData[2];
				Success = rSplitData[3];
				rResMsg = rSplitData[4];
				Alert_msg1 = rSplitData[5];
				Alert_msg2 = rSplitData[6];
										
			}
			else
			{
				if( ccb.getErrCd() == -1 )
				{
					Success = "n";
					rResMsg = "통신오류(sock)로 인한 거절";
				}
				else if( ccb.getErrCd() == -2 )
				{
					Success = "n";
					rResMsg = "통신오류(msg)로 인한 거절";
				}
			}
		}
		catch( Exception e )
		{
			/** 기타 이유로 인한 요청 실패 처리 **/
			
			Success = "n";
			rResMsg = "시스템오류로 인한  거절";
		}
	/*******************************************************************************************
	* 
	* Pay_kind = "non-cash-cncl" 현금영수증 취소요청시 
	*
	******************************************************************************************/
	}else if( Pay_kind.toString().equals( "non-cash-cncl" )){

		/**************************************************************************
		* 취소요청시 전문 생성
		***************************************************************************/
		ENCTYPE = "C";
		
		try
		{

			/** 취소 요청 처리 **/
			if( ccb.noncashreciptcanc( ENCTYPE, Pay_kind,Pay_type, Retailer_id, Cust_no, Ord_No, Cat_id, 
				Amtcash, Amttex, Amtadd, Gubun_cd, Confirm_no, Org_adm_no, Email, prod_nm, Corp_no, Corp_nm, Url, Ceo_nm, Addr, Tel_no ) )
			{
					
					String rSplitData[] = new String[7];
					rSplitData = ccb.getSplitData_canc();

					Retailer_id = rSplitData[0];
					Dealno = rSplitData[1];
					Adm_no = rSplitData[2];
					Success = rSplitData[3];
					rResMsg = rSplitData[4];
					Alert_msg1 = rSplitData[5];
					Alert_msg2 = rSplitData[6];
														
			} else	{
				if( ccb.getErrCd() == -1 )
				{
					Success = "n";
					rResMsg = "통신오류(sock)로 인한 거절";
				} else if( ccb.getErrCd() == -2 ) {
					Success = "n";
					rResMsg = "통신오류(msg)로 인한 거절";
				}
			}
		}
		catch( Exception e )
		{
		/** 기타 이유로 인한 요청 실패 처리 **/
		
				Success = "n";
				rResMsg = "시스템오류로 인한  거절";
		}
	}
%>
<html>
<head>
</head>
<body onload="javascript:cash_pay.submit();">
<form name=cash_pay method=post action=AGSCash_result.jsp>
<input type=hidden name=Retailer_id value="<%=Retailer_id%>">
<input type=hidden name=Ord_No value="<%=Ord_No%>">
<input type=hidden name=Dealno value="<%=Dealno%>">
<input type=hidden name=Cust_no value="<%=Cust_no%>">
<input type=hidden name=Adm_no value="<%=Adm_no%>">
<input type=hidden name=Success value="<%=Success%>">
<input type=hidden name=rResMsg value="<%=rResMsg%>">
<input type=hidden name=Alert_msg1 value="<%=Alert_msg1%>">
<input type=hidden name=Alert_msg2 value="<%=Alert_msg2%>">
<input type=hidden name=deal_won value="<%=deal_won%>">
<input type=hidden name=Amttex value="<%=Amttex%>">
<input type=hidden name=Amtadd value="<%=Amtadd%>">
<input type=hidden name=prod_nm value="<%=prod_nm%>">
<input type=hidden name=prod_set value="<%=prod_set%>">
<input type=hidden name=Amtcash value="<%=Amtcash%>">
<input type=hidden name=Gubun_cd value="<%=Gubun_cd%>">
<input type=hidden name=Pay_kind value="<%=Pay_kind%>">
<input type=hidden name=Pay_type value="<%=Pay_type%>">
<input type=hidden name=Confirm_no value="<%=Confirm_no%>">
<input type=hidden name=Email value="<%=Email%>">
</body>
</html>
