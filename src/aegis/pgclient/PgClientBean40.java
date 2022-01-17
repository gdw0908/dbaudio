/*
컴파일 방법 예) javac -classpath /usr/local/resin/lib/jsdk22.jar:/usr/local/resin/lib/log4j-1.2.1.jar:. PgClientBean.java
*/

package aegis.pgclient;

import java.io.*;
import java.util.*;
import java.net.*;
import java.text.*;

import javax.servlet.jsp.JspWriter;

import org.apache.log4j.*;

/******************************************************************************
	pg클라이언트 통신 모듈
******************************************************************************/

/****************************************************************************
* ※ 결제 형태 변수의 값에 따른 결제 구분
*
* ＊ AuthTy  = "card"		신용카드결제
*	 - SubTy = "isp"		안전결제ISP
*	 - SubTy = "visa3d"		안심클릭
*	 - SubTy = "normal"		일반결제
*
* ＊ AuthTy  = "iche"		계좌이체
*
* ＊ AuthTy  = "virtual"	가상계좌(무통장입금)
* 
* ＊ AuthTy  = "hp"			핸드폰결제
* 
* ＊ AuthTy  = "ars"		ARS결제
*
****************************************************************************/

public class PgClientBean40
{
	private Socket		socket;
	private DataInputStream   	in;
  	private DataOutputStream  	out;
	
	private String IPAddr;
	private int	   Port;
	
	private String ErrMsg;
	private int    ErrCd;
	private String SplitData[]     = new String[15];	// 수신 데이터 버퍼
	private String ISP_AuthSplitData[] = new String[9];
	private String ISP_CnclSplitData[] = new String[7];
	private String HP_SplitData[]  = new String[5];
	private String HP_CnclSplitData[]  = new String[6];
	private String ARS_SplitData[]  = new String[5];
    private String ARS_CnclSplitData[]  = new String[6];
    private String VIR_SplitData[] = new String[6];
    private String VirAcctReturnData[] = new String[6];
	private String Iche_Ibk_SplitData[] = new String[7];
	private String Iche_Ibk_CnclSplitData[] = new String[6];
	private String Iche_Tbk_SplitData[] = new String[6];
	private String Iche_Tbk_CnclSplitData[] = new String[5];
	private Hashtable requestData;
	private Hashtable resultData;
	private String ENCTYPE = "";
	private String nowDate = "";
	
	private static Layout layout ;
    private static FileAppender appender;
    private Logger logger;
    private static int processid;
    private static StringBuffer temp;
	private String pid;
    


	/****************************************************************
		생성자( 접속주소, 포트 )
	****************************************************************/
	public PgClientBean40(String IPAddr, int Port)
	{
		this.IPAddr = IPAddr;
		this.Port   = Port;
		requestData = new Hashtable();
		resultData = new Hashtable();
		Calendar c = Calendar.getInstance();
		String month = "" + (c.get(Calendar.MONTH) + 1);
		if(month.length()==1) month="0"+month;
		String date=""+c.get(Calendar.DATE);
		if(date.length()==1) date="0"+date;
		nowDate = ""+c.get(Calendar.YEAR) + month + date;
	}

	/****************************************************************
		Aegis 결제/취소 처리
	****************************************************************/
	public boolean startPay()
	{
		pid = this.getPID();
		//logger 객체생성
		if( !createLogger() ) { 
			System.out.println("로그파일 생성 실패");			
		}
		if( logger == null )
		{
			setPayResult( "rSuccYn", "n");
			setPayResult( "rResMsg",  "Log Object Create Fail");
		}
		if( getValue("Type").equals( "Pay" ) ) 
		{
			logger.info(pid + "Start Pay Transaction !!! " );
		}else if( getValue("Type").equals( "Cancel" ) ) 
		{
			logger.warn(pid + "Start Cancel Transaction !!! " );
		}
		logger.info(pid + "Start Send Data To PG  [" );
		printHashtable(requestData);
		logger.info(pid + "] End  Send Data To PG " );

				
		if( getValue("Type").equals( "Pay" ) )	// Pay Process
		{
			//Make NetCancelID
			if (!MakeNetCancID())
			{
				logger.error(pid + "MakeNetCancID Fail" );
			}

			if(!pay())
			{
				logger.error(pid + "Pay Process Fail" );
				netCancel();
			}

		}else if(getValue("Type").equals( "Cancel" ) )	// Cancel Process
		{
			//가상계좌는 취소할 수 없음.
			if(getValue("AuthTy").equals( "virtual" ) ){
				setPayResult( "rCancSuccYn", "n");
				setPayResult( "rCancResMsg",  "cannot cancel AuthTy[" + getValue("AuthTy") + "]");
				logger.warn(pid + "cannot cancel AuthTy[" + getValue("AuthTy") + "]" );
				logger.warn(pid + "Cancel Process Fail" );
				return false;
			}
			
			if(!netCancel())
			{
				logger.error(pid + "Cancel Process Fail" );
			}
		}else{
			logger.error(pid + "Unknown Type[" + getValue("Type") + "]");
			setPayResult( "rSuccYn", "n");
			setPayResult( "rResMsg",  "Unknown Type[" + getValue("Type") + "]");
			return false;
		}

		logger.info(pid + "Start Result Data From PG  [" );
		printHashtable(resultData);
		logger.info(pid + "] End  Result Data From PG " );

		return true;
	}	

	/****************************************************************
		Aegis 결제 처리
	****************************************************************/
	public boolean pay()
	{	
		try{

			/****************************************************************************
			*  데이타의 유효성을 검사합니다.
			****************************************************************************/

			StringBuffer ERRMSG = new StringBuffer( "" );
			boolean result = false;
			
			if( checkNull(  getValue("StoreId") ) )
			{
				ERRMSG.append( "상점아이디 입력 여부 확인요망 <br>" );	//상점아이디
			}

			if( checkNull(  getValue("OrdNo") ) )
			{
				ERRMSG.append( "주문번호 입력 여부 확인요망 <br>" );	//주문번호
			}

			if( checkNull(  getValue("ProdNm") ) )
			{
				ERRMSG.append( "상품명 입력 여부 확인요망 <br>" );		//상품명
			}

			if( checkNull(  getValue("Amt") ) )
			{
				ERRMSG.append( "금액 입력 여부 확인요망 <br>" );		//금액
			}

			if( checkNull(  getValue("DeviId") ) )
			{
				ERRMSG.append( "단말기아이디 입력 여부 확인요망 <br>" );//단말기아이디
			}

			if( checkNull(  getValue("AuthYn") ) )
			{
				ERRMSG.append( "인증여부 입력 여부 확인요망 <br>" );	//인증여부
			}

			if( ERRMSG.toString().equals( "" ) )
			{			
				logger.info(pid + "Start Pay[" + getValue("AuthTy") + "] process" );

				if( getValue("AuthTy").equals( "card" ) )
				{
					if( getValue("SubTy").equals( "isp" ) )
					{
						/****************************************************************************
						* [4] 신용카드결제 - ISP
						****************************************************************************/						
						ENCTYPE = "2";						
						result = ISP_ApproveRequest( ENCTYPE, "plug15" ) ;
					}
					else if( getValue("SubTy").equals( "visa3d" ) || getValue("SubTy").equals( "normal" ) )
					{
						/****************************************************************************
						* [5] 신용카드결제 - VISA3D, 일반
						****************************************************************************/						
						ENCTYPE = "0";						
						result = ApproveRequest( ENCTYPE, "plug15") ;							
					}
				}
				else if( getValue("AuthTy").equals( "iche" ) && getValue("ICHE_SOCKETYN").equals( "Y" ))
				{
					/****************************************************************************
					* [6] 인터넷뱅킹 계좌이체(소켓) 처리
					****************************************************************************/
					ENCTYPE = "R";						
					result = Iche_Ibk_ApproveRequest( ENCTYPE, "RB-PayReq") ;							
					
				}else if( getValue("AuthTy").equals( "iche" ) && getValue("ICHEARS_SOCKETYN").equals( "Y" ))
				{
					/****************************************************************************
					* [7] 텔레뱅킹 계좌이체(소켓) 처리
					****************************************************************************/
					ENCTYPE = "B";						
					result = Iche_Tbk_ApproveRequest( ENCTYPE, "TB-PayReq") ;							
					
				}else if( getValue("AuthTy").equals( "virtual" ) )
				{
					/****************************************************************************
					* [8] 가상계좌 결제
					****************************************************************************/					
					ENCTYPE = "V";					
					result = VIR_ApproveRequest( ENCTYPE, "vir_n" );						
				}
				
				else if( getValue("AuthTy").equals( "hp" ) )
				{
					/***************************************************************************
					* [9]핸드폰결제
					****************************************************************************/					
					ENCTYPE = "h";					
					result = HP_ApproveRequest( ENCTYPE, "Bill" );						
				}
				else if( getValue("AuthTy").equals( "ars" ) )
				{
					/***************************************************************************
					* [10]ARS결제
					****************************************************************************/					
					ENCTYPE = "A";					
					result = ARS_ApproveRequest( ENCTYPE, "ABill" );						
				}

				if( result == false)
				{
					if( getErrCd() == -1 )
					{
						setPayResult( "rSuccYn", "n");
						setPayResult( "rResMsg", "통신오류(sock)로 인한 승인거절");
						return false;
					}
					else if( getErrCd() == -2 )
					{
						setPayResult( "rSuccYn", "n");
						setPayResult( "rResMsg", "통신오류(msg)로 인한 승인거절");
						return false;
					}
				}else{
					/** 응답메세지를 Parsing 처리하여 결과값을 변수에 할당한다. **/
					parseMsg();
				}

				logger.info(pid + "End   Pay[" + getValue("AuthTy") + "] process" );
			}
			else
			{
				logger.error(pid + "Pay Transaction Fail [" + ERRMSG.toString() + "]" );
				setPayResult( "rSuccYn", "n");
				setPayResult( "rResMsg",  ERRMSG.toString());
			}

		}catch( Exception e )
		{
			/** 기타 이유로 인한 요청 실패 처리 **/
			
			setPayResult( "rSuccYn", "n");
			setPayResult( "rResMsg", "시스템오류로 인한 승인 거절");
			return false;
		}
		return true;
	}	/* startpay() End. */


	/****************************************************************
		Aegis 취소 처리
	****************************************************************/

	public boolean netCancel()
	{	
		logger.warn(pid + "Start netCancel" );
		try{
			boolean result = false;
		
			if( !(getValue("UseNetCancel") == "true" || getValue("Type") == "Cancel") )
			{
				logger.error(pid + "UseNetCancel Value is false" );
				setPayResult( "rCancelSuccYn", "n");
				setPayResult( "rCancelResMsg",  "UseNetCancel Value is false");
				return false;
			}

			
			if( getValue("AuthTy").equals( "card" ) && getValue("SubTy").equals( "isp" ) )
			{
				/****************************************************************************
				* [1] 신용카드승인취소 - ISP
				****************************************************************************/
				ENCTYPE = "2";	
				result = ISP_CancelRequest( ENCTYPE, getValue("AuthTy")) ;
					
			}
			else if( getValue("AuthTy").equals( "card" ) && (getValue("SubTy").equals( "visa3d" ) || getValue("SubTy").toString().equals( "normal" )) )
			{
				/****************************************************************************
				* [2] 신용카드승인취소 - VISA3D, 일반
				****************************************************************************/			
				ENCTYPE = "0";	
				result = CancelRequest( ENCTYPE, getValue("AuthTy") ) ;
								
			}
			else if( getValue("AuthTy").equals( "iche" ) && getValue("ICHE_SOCKETYN").equals("Y"))
			{
				/****************************************************************************
				* [3] 계좌이체취소 - 인터넷뱅킹
				****************************************************************************/			
				ENCTYPE = "R";	
				result = Iche_Ibk_CancelRequest( ENCTYPE, "RB-CanReq" ) ;
			
			}else if( getValue("AuthTy").equals( "iche" ) && getValue("ICHEARS_SOCKETYN").equals("Y"))
			{
				/****************************************************************************
				* [4] 계좌이체취소 - 텔레뱅킹
				****************************************************************************/	
				ENCTYPE = "B";	
				result = Iche_Tbk_CancelRequest( ENCTYPE, "TB-CanReq" ) ;
				
			}else if( getValue("AuthTy").equals( "hp" ) )
			{
				/****************************************************************************
				* [5] 핸드폰결제취소
				****************************************************************************/			
				ENCTYPE = "h";	
				result = HP_CancelRequest( ENCTYPE, getValue("AuthTy") ) ;
				
			}else if( getValue("AuthTy").equals( "ars" ) )
			{
				/****************************************************************************
				* [6] ARS결제취소
				****************************************************************************/			
				ENCTYPE = "A";	
				result = ARS_CancelRequest( ENCTYPE, getValue("AuthTy") ) ;
				
			}else{
				logger.warn(pid + "Cannot Cancel AuthTy[" + getValue("AuthTy") + "]");
				setPayResult( "rCancelSuccYn", "n");
				setPayResult( "rCancelResMsg", "Cannot Cancel AuthTy[" + getValue("AuthTy") + "]");
				return false;
			}

			if( result == false)
			{
				if( getErrCd() == -1 )
				{
					logger.error(pid + "통신오류(sock)로 인한 승인거절" );
					setPayResult( "rCancelSuccYn", "n");
					setPayResult( "rCancelResMsg", "통신오류(sock)로 인한 승인거절");
					return false;
				}
				else if( getErrCd() == -2 )
				{
					logger.error(pid + "통신오류(msg)로 인한 승인거절" );
					setPayResult( "rCancelSuccYn", "n");
					setPayResult( "rCancelResMsg", "통신오류(msg)로 인한 승인거절");
					return false;
				}
			}else{
				parseCancelMsg();
			}
		}
		catch( Exception e )
		{
			/** 기타 이유로 인한 요청 실패 처리 **/
			setPayResult( "rCancelSuccYn", "n");
			setPayResult( "rCancelResMsg",  "시스템오류로 인한 승인 거절 [" + e.toString() + "]");
			return false;
		}
		logger.warn(pid + "End netCancel" );
		
		//결제결과값 실패로 설정
		setPayResult( "rSuccYn", "n" );
		setPayResult( "rResMsg", getValue("CancelMsg") );
		
		return true;

	}	/*	netCancel() End	*/

	/****************************************************************
		수신메세지 Parsing
	****************************************************************/
	public boolean parseMsg()
	{	
		try{
			logger.info(pid + "Start parseMsg[" + getValue("AuthTy") + "] " );

			if( getValue("AuthTy").equals( "card" ) )
			{
				if( getValue("SubTy").equals( "isp" ) )
				{
					/****************************************************************************
					*
					* [4] 신용카드결제 - ISP
					* 
					* -- 승인 응답 전문 포멧
					* + 데이터길이(6) + 데이터
					* + 데이터 포멧(데이터 구분은 "|"로 한다.
					* 업체ID(20)		| 전문코드(4)	| 거래고유번호(6)	| 승인번호(8)		| 
					* 거래금액(12)	| 성공여부(1)	| 실패사유(20)	| 승인시각(14)	| 
					* 카드사코드(4) 	|
					*    
					****************************************************************************/
					
						
					String rSplitData[] = new String[9];
					rSplitData = getISP_AuthRecvData();
	
					setPayResult( "rStoreId", rSplitData[0] );
					setPayResult( "rBusiCd", rSplitData[1] );
					setPayResult( "rOrdNo", getValue("OrdNo") );
					setPayResult( "rOrdNm", getValue("OrdNm") );
					setPayResult( "rDealNo", rSplitData[2] );
					setPayResult( "rApprNo", rSplitData[3] );
					setPayResult( "rProdNm", getValue("ProdNm") );
					setPayResult( "rAmt", rSplitData[4] );
					setPayResult( "rInstmt", getValue("KVP_QUOTA") );
					setPayResult( "rSuccYn", rSplitData[5] );
					setPayResult( "rResMsg", rSplitData[6] );
					setPayResult( "rApprTm", rSplitData[7] );
					setPayResult( "rCardCd", rSplitData[8] );
							

				}
				else if( getValue("SubTy").equals( "visa3d" ) || getValue("SubTy").equals( "normal" ) )
				{
					/****************************************************************************
					* 
					* [5] 신용카드결제 - VISA3D, 일반
					* 
					* -- 승인 응답 전문 포멧
					* + 데이터길이(6) + 데이터
					* + 데이터 포멧(데이터 구분은 "|"로 하며 암호화Process에서 해독된후 실데이터를 수신하게 된다.
					* 업체ID(20)		| 전문코드(4)		 | 주문번호(40)	| 승인번호(8)	 	| 거래금액(12)  |
					* 성공여부(1)		| 실패사유(20)	  | 카드사명(20) 	| 승인시각(14)	| 카드사코드(4) |
					* 가맹점번호(15)	| 매입사코드(4)	 | 매입사명(20)	| 전표번호(6)	 	|
					* 
					****************************************************************************/
					
						
					String rSplitData[] = new String[14];
					rSplitData = getRecvData();
					
					setPayResult( "rStoreId", rSplitData[0] );
					setPayResult( "rBusiCd", rSplitData[1] );
					setPayResult( "rOrdNo", rSplitData[2] );
					setPayResult( "rOrdNm", getValue("OrdNm") );
					setPayResult( "rApprNo", rSplitData[3] );
					setPayResult( "rInstmt", getValue("Instmt") );
					setPayResult( "rAmt", rSplitData[4] );
					setPayResult( "rSuccYn", rSplitData[5] );
					setPayResult( "rResMsg", rSplitData[6] );
					setPayResult( "rCardNm", rSplitData[7] );
					setPayResult( "rApprTm", rSplitData[8] );
					setPayResult( "rCardCd", rSplitData[9] );
					setPayResult( "rMembNo", rSplitData[10] );
					setPayResult( "rAquiCd", rSplitData[11] );
					setPayResult( "rAquiNm", rSplitData[12] );
					setPayResult( "rDealNo", rSplitData[13] );

				}
			}
			else if( getValue("AuthTy").equals( "iche" ) && getValue("ICHE_SOCKETYN").equals( "Y" ))
			{
				/****************************************************************************
				* 
				* [6] 인터넷뱅킹 계좌이체(소켓) 처리
				* 
				* -- 인터넷뱅킹 결제 요청 응답 전문 포멧
				* + 데이터길이(6) + 데이터
				* + 데이터 포멧(데이터 구분은 "|"로 하며 암호화Process에서 해독된후 실데이터를 수신하게 된다.
				* 결제종류(10)	| 상점아이디(20)	| 주문번호(40)	| 이용기관주문번호(50)	| 결과코드(4)  | 결과메시지(300)  |
				* 
				****************************************************************************/

				String rSplitData[] = new String[7];
				rSplitData = getIche_Ibk_RecvData();
				
				setPayResult("rStoreId", rSplitData[1] );
				setPayResult("rOrdNo", rSplitData[2] );
				setPayResult("rMTid", rSplitData[3] );
				setPayResult("rOrdNm", getValue("OrdNm") );
				setPayResult("ES_SENDNO", rSplitData[4] );
				setPayResult("rSuccYn", rSplitData[5] );
				setPayResult("rResMsg", rSplitData[6] );
				setPayResult("rAmt", getValue("Amt") );
			}				
			else if( getValue("AuthTy").equals( "iche" ) && getValue("ICHEARS_SOCKETYN").equals( "Y" ))
			{
				/****************************************************************************
				*
				* [7] 텔레뱅킹 계좌이체(소켓) 처리 
				*
				* -- 텔레뱅킹 결제 요청 응답 전문 포멧
				* + 데이터길이(6) + 데이터
				* + 데이터 포멧(데이터 구분은 "|"로 하며 암호화Process에서 해독된후 실데이터를 수신하게 된다.
				* 결제종류(10)	| 상점아이디(20)	| 주문번호(40)	| 이용기관주문번호(50)	| 결과코드(4)  | 결과메시지(300)  |
				* 
				****************************************************************************/
				String rSplitData[] = new String[6];
				rSplitData = getIche_Tbk_RecvData();
				
				setPayResult("rStoreId", rSplitData[1] );
				setPayResult("rOrdNo", rSplitData[2] );
				setPayResult("rOrdNm", getValue("OrdNm") );
				setPayResult("rMTid", rSplitData[3] );
				setPayResult("rSuccYn", rSplitData[4] );
				setPayResult("rResMsg", rSplitData[5] );
				setPayResult("rAmt", getValue("Amt") );
				setPayResult("rProdNm", getValue("ProdNm") );
				
				int strPos = 0;				
				if(  ( strPos = getValue("rResMsg").indexOf( ":" ) ) != -1 	)
				{
					setPayResult("ES_SENDNO", getValue("rResMsg").substring( strPos+1,6 ) );
				}
			}

			else if( getValue("AuthTy").equals( "virtual" ) )
			{
				/****************************************************************************
				*
				* [8] 가상계좌 결제
				* 
				* -- 승인 응답 전문 포멧
				* + 데이터길이(6) + 암호화 구분(1) + 데이터
				* + 데이터 포멧(데이터 구분은 "|"로 한다.
				* 결제종류(10)	| 업체ID(20)		| 승인일자(14)	| 가상계좌번호(20)	| 결과코드(1)		| 결과메시지(100)	 | 
				* 가상계좌 일반 : vir_n 유클릭 : vir_u 에스크로 : vir_s   
				* 가상계좌번호 및 상품명 추가 2005-11-10
				*
				****************************************************************************/
				
				String rSplitData[] = new String[6];
				rSplitData = getVIR_RecvData();
				
				setPayResult( "rAuthTy",rSplitData[0] );
				setPayResult( "rStoreId",rSplitData[1] );
				setPayResult( "rApprTm",rSplitData[2] );
				setPayResult( "rVirNo",rSplitData[3] );
				setPayResult( "rSuccYn",rSplitData[4] );
				setPayResult( "rResMsg",rSplitData[5] );
				
				setPayResult( "rOrdNo", getValue("OrdNo") );
				setPayResult( "rOrdNm", getValue("OrdNm") );
				setPayResult( "rProdNm", getValue("ProdNm") );
				setPayResult( "rAmt", getValue("Amt") );

				int strPos = 0;				
				if(  ( strPos = getValue("rResMsg").indexOf( ":" ) ) != -1 	)
				{
					setPayResult("ES_SENDNO", getValue("rResMsg").substring( strPos+1,6 ) );
				}
						
			}
			
			else if( getValue("AuthTy").equals( "hp" ) )
			{
				/***************************************************************************
				*
				* [9]핸드폰결제
				*
				* -- 승인 응답 전문 포멧
				* + 데이터길이(6) + 데이터
				* + 데이터 포멧(데이터 구분은 "|"로 한다.
				*
				****************************************************************************/
				
				String rSplitData[] = new String[5];
				rSplitData = getHP_RecvData();

				setPayResult( "rStoreId", rSplitData[0] );
				setPayResult( "rSuccYn", rSplitData[1] );
				setPayResult( "rResMsg", rSplitData[2] );
				setPayResult( "rHP_DATE", rSplitData[3] );
				setPayResult( "rHP_TID", rSplitData[4] );
				setPayResult( "rOrdNo", getValue("OrdNo") );
				setPayResult( "rOrdNm", getValue("OrdNm") );
				setPayResult( "rAmt", getValue("Amt") );
				setPayResult( "rProdNm", getValue("ProdNm") );
						
			}
			else if( getValue("AuthTy").equals( "ars" ) )
			{
				/***************************************************************************
				*
				* [10]ARS결제
				*
				* -- 승인 응답 전문 포멧
				* + 데이터길이(6) + 데이터
				* + 데이터 포멧(데이터 구분은 "|"로 한다.
				*
				****************************************************************************/
				
				String rSplitData[] = new String[5];
				rSplitData = getARS_RecvData();

				setPayResult( "rStoreId", rSplitData[0] );
				setPayResult( "rSuccYn", rSplitData[1] );
				setPayResult( "rResMsg", rSplitData[2] );
				setPayResult( "rHP_DATE", rSplitData[3] );
				setPayResult( "rHP_TID", rSplitData[4] );
				setPayResult( "rOrdNo", getValue("OrdNo") );
				setPayResult( "rOrdNm", getValue("OrdNm") );
				setPayResult( "rAmt", getValue("Amt") );
				setPayResult( "rProdNm", getValue("ProdNm") );
						
			}
			
			logger.info(pid + "End   parseMsg" );
		}catch ( Exception e )
		{
			logger.error(pid + e.toString() );	
			setPayResult( "rSuccYn", "n");
			setPayResult( "rResMsg",  "Parse ResultMsg Fail");
			return false;
		}			
			
		return true;
	}	/* parseMsg() End. */



	public boolean parseCancelMsg()
	{	
		logger.info(pid + "Start parseCancelMsg" );
		if( getValue("AuthTy").equals( "card" ) && getValue("SubTy").equals( "isp" ) )
		{
			/****************************************************************************
			*
			* [1] 신용카드승인취소 - ISP
			*
			* -- 취소 승인 응답 전문 포멧
			* + 데이터길이(6) + 데이터
			* + 데이터 포멧(데이터 구분은 "|"로 한다.
			* 업체ID(20)	| 승인번호(20)	| 승인시각(8)	| 전문코드(4)	| 거래고유번호(6)	| 성공여부(1)	|
			*		   
			****************************************************************************/
			
			String rSplitData[] = new String[7];
			rSplitData = getISP_CnclRecvData();	
			
			setPayResult("rStoreId", rSplitData[0]);
			setPayResult("rApprNo", rSplitData[1]);
			setPayResult("rApprTm", rSplitData[2]);
			setPayResult("rBusiCd", rSplitData[3]);
			setPayResult("rDealNo", rSplitData[4]);
			setPayResult("rCancelSuccYn", rSplitData[5]);
			setPayResult("rCancelResMsg", rSplitData[6]);
					
		}
		else if( getValue("AuthTy").equals( "card" ) && (getValue("SubTy").equals( "visa3d" ) || getValue("SubTy").equals( "normal" )) )
		{
			/****************************************************************************
			*
			* [2] 신용카드승인취소 - VISA3D, 일반
			*
			* -- 취소 승인 응답 전문 포멧
			* + 데이터길이(6) + 데이터
			* + 데이터 포멧(데이터 구분은 "|"로 하며 암호화Process에서 해독된후 실데이터를 수신하게 된다.
			* 업체ID(20)	| 승인번호(8)	| 승인시각(14)	| 전문코드(4)	| 성공여부(1)	|
			* 주문번호(20)	| 할부개월(2)	| 결제금액(20)	| 카드사명(20)	| 카드사코드(4) 	|
			* 가맹점번호(15)	| 매입사코드(4)	| 매입사명(20)	| 전표번호(6)
			*		   
			****************************************************************************/
			
			String rSplitData[] = new String[15];
			rSplitData = getRecvData();
			
			setPayResult("rStoreId", rSplitData[0]);
			setPayResult("rApprNo", rSplitData[1]);
			setPayResult("rApprTm", rSplitData[2]);
			setPayResult("rBusiCd", rSplitData[3]);
			setPayResult("rCancelSuccYn", rSplitData[4]);
			setPayResult("rOrdNo", rSplitData[5]);
			setPayResult("rInstmt", rSplitData[6]);
			setPayResult("rAmt", rSplitData[7]);
			setPayResult("rCardNm", rSplitData[8]);
			setPayResult("rCardCd", rSplitData[9]);
			setPayResult("rMembNo", rSplitData[10]);
			setPayResult("rAquiCd", rSplitData[11]);
			setPayResult("rAquiNm", rSplitData[12]);
			setPayResult("rDealNo", rSplitData[13]);
			setPayResult("rCancelResMsg", rSplitData[14]);

			if (getResult("rCancelSuccYn").equals("y") ){
				setPayResult("rCancelResMsg", "정상취소");
			}else{
				setPayResult("rCancelResMsg", "취소실패");
			}


							
		}else if( getValue("AuthTy").equals( "hp" )  )
		{
			/****************************************************************************
			*
			* [3] 핸드폰 결제 취소
			*
			* -- 취소 응답 전문 포멧
			* + 데이터길이(6) + 데이터
			* + 데이터 포멧(데이터 구분은 "|"로 하며 암호화Process에서 해독된후 실데이터를 수신하게 된다.
			* |	MobileCanRes	|	업체ID(20)	| 성공여부(1)	|	결과메세지	|	취소처리일시	|	이용기관주문번호	|
			*		   
			****************************************************************************/
			
			String rSplitData[] = new String[6];
			rSplitData = getHP_CnclRecvData();
			
			setPayResult("rStoreId", rSplitData[1]);
			setPayResult("rCancelSuccYn", rSplitData[2]);
			setPayResult("rCancelResMsg", rSplitData[3]);
			setPayResult("rCancelDate", rSplitData[4]);
			setPayResult("rTid", rSplitData[5]);
			
			
		}else if( getValue("AuthTy").equals( "ars" )  )
		{
			/****************************************************************************
			*
			* [4] ARS 결제 취소
			*
			* -- 취소 응답 전문 포멧
			* + 데이터길이(6) + 데이터
			* + 데이터 포멧(데이터 구분은 "|"로 하며 암호화Process에서 해독된후 실데이터를 수신하게 된다.
			* |	ARSCanRes	|	업체ID(20)	| 성공여부(1)	|	결과메세지	|	취소처리일시	|	이용기관주문번호	|
			*		   
			****************************************************************************/
			
			String rSplitData[] = new String[6];
			rSplitData = getARS_CnclRecvData();
			
			setPayResult("rStoreId", rSplitData[1]);
			setPayResult("rCancelSuccYn", rSplitData[2]);
			setPayResult("rCancelResMsg", rSplitData[3]);
			setPayResult("rCancelDate", rSplitData[4]);
			setPayResult("rTid", rSplitData[5]);
			
			
		}else if( getValue("AuthTy").equals( "iche" ) && getValue("ICHE_SOCKETYN").equals( "Y" ) )
		{
			/****************************************************************************
			*
			* [5] 계좌이체 결제 취소 -인터넷 뱅킹
			*
			* -- 취소 응답 전문 포멧
			* + 데이터길이(6) + 데이터
			* + 데이터 포멧(데이터 구분은 "|"로 하며 암호화Process에서 해독된후 실데이터를 수신하게 된다.
			* |	MobileCanRes	|	업체ID(20)	| 성공여부(1)	|	결과메세지	|	취소처리일시	|	이용기관주문번호	|
			*		   
			****************************************************************************/
			
			String rSplitData[] = new String[6];
			rSplitData = getIche_Ibk_CnclRecvData();
			
			setPayResult("rStoreId", rSplitData[1]);
			setPayResult("ICHE_POSMTID", rSplitData[2]);
			setPayResult("rOrdNo", rSplitData[3]);
			setPayResult("rCancelSuccYn", rSplitData[4]);
			setPayResult("rCancelResMsg", rSplitData[5]);
			
														
		}else if( getValue("AuthTy").equals( "iche" ) && getValue("ICHEARS_SOCKETYN").equals( "Y" ) )
		{
			/****************************************************************************
			*
			* [6] 계좌이체 결제 취소 -텔레뱅킹
			*
			* -- 취소 응답 전문 포멧
			* + 데이터길이(6) + 데이터
			* + 데이터 포멧(데이터 구분은 "|"로 하며 암호화Process에서 해독된후 실데이터를 수신하게 된다.
			* |	TB-PayRes	|	업체ID(20)	| 이용기관주문번호	|	성공여부(1)	|	결과메세지	|
			*		   
			****************************************************************************/
			
			String rSplitData[] = new String[5];
			rSplitData = getIche_Tbk_CnclRecvData();
			
			setPayResult("rStoreId", rSplitData[1]);
			setPayResult("rMTid", rSplitData[2]);
			setPayResult("rCancelSuccYn", rSplitData[3]);
			setPayResult("rCancelResMsg", rSplitData[4]);
			
													
		}else{
			logger.error(pid + "AuthTy[" +getValue("AuthTy") + "]" + "SubTy[" +getValue("SubTy") + "] Can't Used for Cancel");
			return false;
		}
		logger.info(pid + "End parseCancelMsg" );
		return true;

	}	/*	parseCancelMsg() End	*/


	/****************************************************************
		승인요청 전문을 생성하고 암호화 데몬 프로세스로 데이터를 전송하고 응답을 수신한다.

		< 데이터 포멧 >
		결제종류(6)	| 업체ID(20)		| 회원ID(20)		| 결제금액(12)	| 주문번호(40) |
		단말기번호(10) | 카드번호(37)	| 유효기간(6)		| 할부기간(4)		| 인증유무(1)	| 
		카드비밀번호(2) | 주민등록번호(10) | 수신인(40)	| 수신인전호(21)	| 배송지(100)	|
		주문자명(40)	| 주문자연락처(100) | 기타요구사항(350) | \n(1)
		< 승인응답 데이터 포맷 >
		업체ID(20)	| 전문코드(4)	 | 거래고유번호(20) | 승인번호(4)	 | 거래금액(12) |
		성공여부(1)	| 실패사유(20) | 카드사명(20)	| 승인시각(14)	| 카드사코드(4) |
		가맹점번호(15)	 | 매입사코드(4) | 매입사명(20)| 전표번호(6)	| \n(1)
	****************************************************************/
	public boolean ApproveRequest(String EncType, String AuthTy)
	{
		logger.info(pid + "Start ApproveRequest [ " + getValue("AuthTy") + "] " );

		String DataMsg = "";
		
		// 승인 요청 전문 생성.
		DataMsg = EncType + 
			AuthTy + "|" + 
			getValue("StoreId") + "|" + 
			getValue("UserId") + "|" + 
			getValue("Amt") + "|" + 
			getValue("OrdNo") + "|" + 
			getValue("DeviId") + "|" + 
			encryptAegis( getValue("CardNo") ) + "|" + 
			encryptAegis( getValue("ExpYear") + getValue("ExpMon") ) + "|" + 
			getValue("Instmt") + "|" + 
			getValue("AuthYn") + "|" + 
			encryptAegis( getValue("Passwd") ) + "|" + 
			encryptAegis( getValue("SocId") ) + "|" + 
			getValue("RcpNm") + "|" + 
			getValue("RcpPhone") + "|" + 
			getValue("DlvAddr") + "|" + 
			getValue("OrdNm") + "|" +
			getValue("UserIp") + ";" + 
			getValue("OrdPhone") + "|" + 
			getValue("UserEmail") + ";" + 
			getValue("Remark") + "|" + 
			getValue("ProdNm") + "|" + 
			getValue("MPI_CAVV") + "|" + 
			getValue("MPI_MD64") + "|" + 
			getValue("MPI_ECI") + "|" +
			getValue("UserEmail") + "|" +
			getValue("NetCancID") + "|" ;

		if (!msgSend(DataMsg))
		{
			logger.error(pid + "msgSend Fail" );
			setPayResult( "rSuccYn", "n");
			setPayResult( "rResMsg",  "MsgSend Fail");
			return false;
		}

		logger.info(pid + "End   ApproveRequest " );
		
		return true;
	}

	/****************************************************************
	  취소요청 전문을 생성하고 암호화 데몬 프로세스로 데이터를 전송하고 응답을 수신한다.
		<전문 포멧>
			데이터길이(6) + 암호화여부(1) + 데이터
		<데이터 포멧>
			결제종류(6) + 업체아이디(20) + 승인번호(20) + 승인시간(14) + 카드번호(16) + Terminate(1)
		
		취소승인 응답 데이터
		< 데이터 포맷 >
		업체ID(20)	| 승인번호(4)		| 승인시각(14)	| 전문코드(4)		| 성공여부(1)	| 
		주문번호(20)	| 할부개월(2)		| 결제금액(20	)	| 카드사명(20)	| 카드사코드(4) |
		가맹점번호(15)	 | 매입사코드(4)	| 매입사명(20)	| 전표번호(6)		| Terminate(1)
	****************************************************************/
	public boolean CancelRequest(String EncType, String AuthTy )
	{
		logger.info(pid + "Start CancelRequest [" + getValue("AuthTy") + "] " );
		String DataMsg = "";
		String ApprTm = "";

		if(getValue("rApprTm").length() > 7) ApprTm = getValue("rApprTm").substring(0,8);
		
		// 취소 요청 전문 생성.
		DataMsg = EncType + 
			"cancel" + "|" + 
			getValue("StoreId") + "|" +
			getValue("rApprNo") + "|" + 
			ApprTm + "|" + 
			getValue("rDealNo") + "|" +
			getValue("NetCancID") + "|" +
			getValue("PartCancelAmt") + "|" ;

		if (!cancelMsgSend(DataMsg))
		{
			logger.error(pid + "msgSend Fail" );
			setPayResult( "rSuccYn", "n");
			setPayResult( "rResMsg",  "MsgSend Fail");
			return false;
		}
		logger.info(pid + "End   CancelRequest " );
		return true;
	}

	/****************************************************************
		ISP승인요청 전문을 생성하고 암호화 데몬 프로세스로 데이터를 전송하고 응답을 수신한다.

	-- 승인 요청 전문 포멧
	   + 데이터길이(6) + ISP구분코드(1) + 데이터
	   + 데이터 포멧(데이터 구분은 "|"로 한다.)
	      결제종류(6)	| 업체ID(20)		| 회원ID(20)	 		| 결제금액(12)	| 
		  주문번호(40)	| 단말기번호(10)	| 수신인(40)			| 수신인전화(21)	| 
		  배송지(100)	| 주문자명(40)	| 주문자연락처(100)	| 기타요구사항(350)|
		  상품명(300)	| 통화코드(3)	| 일반할부기간(2)	| 무이자할부기간(2)| 
		  KVP카드코드(22)| 세션키(256)	| 암호화데이터(2048) | 카드명(50)		|
		  회원 IP(20)	| 회원 Email(50 ) |

	****************************************************************/
	public boolean ISP_ApproveRequest(String EncType, String AuthTy)
	{
		logger.info(pid + "Start ISP_ApproveRequest [" + getValue("AuthTy") + "] " );
		String DataMsg = "";
		
		// 승인 요청 전문 생성.
		DataMsg = EncType + 
			AuthTy + "|" + 
			getValue("StoreId") + "|" + 
			getValue("UserId") + "|" + 
			getValue("Amt") + "|" + 
			getValue("OrdNo") + "|" + 
			getValue("DeviId") + "|" + 
			getValue("RcpNm")    + "|" + 
			getValue("RcpPhone") + "|" + 
			getValue("DlvAddr") + "|" + 
			getValue("OrdNm")  + "|" + 
			getValue("OrdPhone") + "|" + 
			getValue("Remark")   + "|" + 
			getValue("ProdNm")  + "|" + 
			getValue("KVP_CURRENCY") + "|" + 
			getValue("partial_mm") + "|" + 
			getValue("noIntMonth") + "|" + 
			getValue("KVP_CARDCODE") + "|" + 
			getValue("KVP_SESSIONKEY") + "|" + 
			getValue("KVP_ENCDATA") + "|" + 
			getValue("KVP_CONAME") + "|" + 
			getValue("UserIp") + "|" + 
			getValue("UserEmail") + "|" +
			getValue("NetCancID") + "|" ;

		if (!msgSend(DataMsg))
		{
			logger.error(pid + "msgSend Fail" );
			setPayResult( "rSuccYn", "n");
			setPayResult( "rResMsg",  "MsgSend Fail");
			return false;
		}
		logger.info(pid + "End   ISP_ApproveRequest " );
		
		return true;
	}

	/****************************************************************************
	'	ISP취소 승인 처리 START
	'	-- 이부분은 취소 승인 처리를 위해 PG서버Process와 Socket통신하는 부분이다.
	'	   가장 핵심이 되는 부분이므로 수정후에는 실제 서비스전까지 적절한 테스트를 하여야 한다.
	'   -- 데이터 길이는 매뉴얼 참고
	'	    
	'	-- 취소 승인 요청 전문 포멧
	'	   + 데이터길이(6) + 암호화여부(1) + 데이터
	'	   + 데이터 포멧(데이터 구분은 "|"로 한다.
	'		  결제종류(6)	| 업체아이디(20) | 승인번호(20) |	 승인시간(8)	 | 거래고유번호(6) |
	'
	****************************************************************************/
	public boolean ISP_CancelRequest(String EncType, String AuthTy )
	{
		logger.info(pid + "Start ISP_CancelRequest [" + getValue("AuthTy") + "] " );
		String DataMsg = "";
		String ApprTm = "";

		if(getValue("rApprTm").length() > 7) ApprTm = getValue("rApprTm").substring(0,8);
		
		// 취소 요청 전문 생성.
		DataMsg = EncType + 
			"cancel" + "|" + 
			getValue("StoreId") + "|" + 
			getValue("rApprNo") + "|" + 
			ApprTm + "|" + 
			getValue("rDealNo") + "|" +
			getValue("NetCancID") + "|" +
			getValue("PartCancelAmt") + "|" ;

		if (!cancelMsgSend(DataMsg))
		{
			logger.error(pid + "msgSend Fail" );
			setPayResult( "rSuccYn", "n");
			setPayResult( "rResMsg",  "MsgSend Fail");
			return false;
		}
		logger.info(pid + "End   ISP_CancelRequest " );
		return true;
	}

	/****************************************************************
		핸드폰 결제요청 전문을 생성하고 암호화 데몬 프로세스로 데이터를 전송하고 응답을 수신한다.

	-- 승인 요청 전문 포멧
	   + 데이터길이(6) + 핸드폰구분코드(1) + 데이터
	   + 데이터 포멧(데이터 구분은 "|"로 한다.)
	      결제종류	| 결제종류		| 서비스아이디 		| 서버INFO		| 
		
	****************************************************************/
	public boolean HP_ApproveRequest(String EncType, String AuthTy)
	{
		logger.info(pid + "Start HP_ApproveRequest [" + getValue("AuthTy") + "] " );
		String DataMsg = "";
		
		// 승인 요청 전문 생성.
		DataMsg = EncType + 
			AuthTy + "|" + 
			getValue("StoreId") + "|" + 
			getValue("HP_SERVERINFO") + "|"+
			getValue("HP_ID") + "|"+
			getValue("HP_SUBID") + "|"+
			getValue("OrdNo") + "|"+
			getValue("Amt") + "|"+
			getValue("HP_UNITType") + "|"+
			getValue("HP_HANDPHONE") + "|"+
			getValue("HP_COMPANY") + "|"+
			getValue("HP_IDEN") + "|"+
			getValue("UserId") + "|"+
			getValue("UserEmail") + "|"+
			getValue("HP_IPADDR") + "|"+
			getValue("ProdNm") + "|"+
			getValue("NetCancID") +"|";

		if (!msgSend(DataMsg))
		{
			logger.error(pid + "msgSend Fail" );
			setPayResult( "rSuccYn", "n");
			setPayResult( "rResMsg",  "MsgSend Fail");
			return false;
		}
		logger.info("End   HP_ApproveRequest " );
		return true;
	}
	/****************************************************************
		핸드폰 결제취소요청 전문을 생성하고 암호화 데몬 프로세스로 데이터를 전송하고 응답을 수신한다.

	-- 취소요청 전문 포멧
	   + 데이터길이(6) + 핸드폰구분코드(1) + 데이터
	   + 데이터 포멧(데이터 구분은 "|"로 한다.)
	      결제종류	| 서비스아이디 	| NetCancID		| 
		
	****************************************************************/
	public boolean HP_CancelRequest(String EncType, String AuthTy)
	{
		logger.info(pid + "Start HP_CancelRequest [" + getValue("AuthTy") + "] " );
		String DataMsg = "";
		
		// 승인 요청 전문 생성.
		DataMsg = EncType + 
			"MobileCanReq" + "|" + 
			getValue("StoreId") + "|" + 
			getValue("NetCancID") +"|";

		if (!cancelMsgSend(DataMsg))
		{
			logger.error(pid + "cancelMsgSend Fail" );
			setPayResult( "rSuccYn", "n");
			setPayResult( "rResMsg",  "cancelMsgSend Fail");
			return false;
		}
		logger.info("End   HP_CancelRequest " );
		return true;
	}
	/****************************************************************
		ARS 결제요청 전문을 생성하고 암호화 데몬 프로세스로 데이터를 전송하고 응답을 수신한다.

	-- 승인 요청 전문 포멧
	   + 데이터길이(6) + ARS구분코드(1) + 데이터
	   + 데이터 포멧(데이터 구분은 "|"로 한다.)
	      결제종류	| 결제종류		| 서비스아이디 		| 서버INFO		| 
		
	****************************************************************/
	public boolean ARS_ApproveRequest(String EncType, String AuthTy)
	{
		logger.info(pid + "Start ARS_ApproveRequest [" + getValue("AuthTy") + "] " );
		String DataMsg = "";
		// 승인 요청 전문 생성.
		DataMsg = EncType + 
			AuthTy + "|" + 
			getValue("StoreId") + "|" + 
			getValue("HP_SERVERINFO") + "|"+
			getValue("HP_ID") + "|"+
			getValue("HP_UNITType") + "|"+
			getValue("Amt") + "|"+
			getValue("ProdNm") + "|"+
			getValue("UserEmail") + "|"+
			getValue("HP_SUBID") + "|"+
			getValue("OrdNo") + "|"+
			getValue("UserId") + "|"+
			getValue("ARS_PHONE") + "|"+
			getValue("HP_IDEN") + "|"+
			getValue("ARS_NAME") + "|"+
			getValue("HP_COMPANY") + "|"+
			getValue("HP_IPADDR") + "|"+
			getValue("NetCancID") +"|";

		if (!msgSend(DataMsg))
		{
			logger.error(pid + "msgSend Fail" );
			setPayResult( "rSuccYn", "n");
			setPayResult( "rResMsg",  "MsgSend Fail");
			return false;
		}
		logger.info(pid + "End   ARS_ApproveRequest "  );
		return true;
	}

	/****************************************************************
		ARS 결제취소요청 전문을 생성하고 암호화 데몬 프로세스로 데이터를 전송하고 응답을 수신한다.

	-- 취소요청 전문 포멧
	   + 데이터길이(6) + ARS구분코드(1) + 데이터
	   + 데이터 포멧(데이터 구분은 "|"로 한다.)
	      결제종류	| 서비스아이디 	| NetCancID		| 
		
	****************************************************************/
	public boolean ARS_CancelRequest(String EncType, String AuthTy)
	{
		logger.info(pid + "Start ARS_CancelRequest [" + getValue("AuthTy") + "] " );
		String DataMsg = "";
		
		// 승인 요청 전문 생성.
		DataMsg = EncType + 
			"ARSCanReq" + "|" + 
			getValue("StoreId") + "|" + 
			getValue("NetCancID") +"|";

		if (!cancelMsgSend(DataMsg))
		{
			logger.error(pid + "cancelMsgSend Fail" );
			setPayResult( "rSuccYn", "n");
			setPayResult( "rResMsg",  "cancelMsgSend Fail");
			return false;
		}
		logger.info("End   ARS_CancelRequest " );
		return true;
	}

	 /****************************************************************
		*
			* [8] 가상계좌 결제
			* 
			* -- 이부분은 승인 처리를 위해 암호화Process와 Socket통신하는 부분이다.
			* 가장 핵심이 되는 부분이므로 수정후에는 테스트를 하여야 한다.
			* -- 데이터 길이는 매뉴얼 참고
			* 
			* -- 승인 요청 전문 포멧
			* + 데이터길이(6) + 암호화 구분(1) + 데이터
			* + 데이터 포멧(데이터 구분은 "|"로 한다.)
			* 결제종류(10)	| 업체ID(20)	| 주문번호(40)	 	| 은행코드(4)		| 가상계좌번호(20)
			* 거래금액(13)	| 입금예정일(8)	| 구매자명(20)		| 주민번호(13)		| 
			* 이동전화(21)	| 이메일(50)	| 구매자주소(100)	| 수신자명(20)	|
			* 수신자연락처(21)	| 배송지주소(100)	| 상품명(100) | 기타요구사항(300)		| 상점 도메인(50)|상점 페이지(100)|
			* 
	****************************************************************/
	public boolean VIR_ApproveRequest(String EncType, String AuthTy)
	{
		logger.info(pid + "Start VIR_ApproveRequest [" + getValue("AuthTy") + "] " );
		
		String DataMsg = "";
		
		// 승인 요청 전문 생성.
		DataMsg = EncType+
			AuthTy + "|" +
			getValue("StoreId") +"|"+
			getValue("OrdNo") +"|"+
			getValue("VIRTUAL_CENTERCD") +"|"+
			getValue("VIRTUAL_NO") +"|"+ 
			getValue("Amt") +"|"+
			getValue("VIRTUAL_DEPODT") +"|"+
			getValue("OrdNm") +"|"+
			getValue("ZuminCode") +"|"+
			getValue("OrdPhone") +"|"+
			getValue("UserEmail") +"|"+
			getValue("OrdAddr") +"|"+
			getValue("RcpNm") +"|"+
			getValue("RcpPhone") +"|"+
			getValue("DlvAddr") +"|"+
			getValue("ProdNm") +"|"+
			getValue("Remark") +"|"+
			getValue("MallUrl") +"|"+
			getValue("MallPage") +"|"+
			getValue("NetCancID") +"|";

		if (!msgSend(DataMsg))
		{
			logger.error(pid + "msgSend Fail" );
			setPayResult( "rSuccYn", "n");
			setPayResult( "rResMsg",  "MsgSend Fail");
			return false;
		}
		logger.info(pid + "End   VIR_ApproveRequest " );
		
		return true;
	}


    /****************************************************************************
	'	가상계좌 환불 통보
	'	-- 환불 처리
	'	-- 환불 처리 결과 송신 전문 포멧
	'	   + 데이터길이(6) + 암호화여부(1) + 데이터
	'	   + 데이터 포멧(데이터 구분은 "|"로 한다.
	'		  상점아이디(20)	| Return Ip(20) | Retrun Port (20) |	 거래코드(8)	 | 주문시간(14) | 주문번호(6) |
	'		   
	****************************************************************************/
	public boolean VirAcct_ReturnRequest(String EncType, String AuthTy)
	{
		logger.info(pid + "Start VirAcct_ReturnRequest [" + getValue("AuthTy") + "] " );
		String DataMsg = "";

		// 취소 요청 전문 생성.
		DataMsg = EncType + 
			AuthTy + "|" + 
			getValue("StoreId") + "|" + 
			getValue("ReturnIp") + "|" + 
			getValue("ReturnPort") + "|" + 
			getValue("TrCode") + "|" +
			getValue("Deal_time") + "|" +
			getValue("OrdNo") + "|";

		if (!msgSend(DataMsg))
		{
			logger.error(pid + "msgSend Fail" );
			setPayResult( "rSuccYn", "n");
			setPayResult( "rResMsg",  "MsgSend Fail");
			return false;
		}
		logger.info(pid + "End   VirAcct_ReturnRequest " );
		
		return true;
	}

	/****************************************************************
	*
	* [9] 계좌이체 소켓 결제 - 인터넷뱅킹
	* 
	* -- 이부분은 승인 처리를 위해 암호화Process와 Socket통신하는 부분이다.
	* 가장 핵심이 되는 부분이므로 수정후에는 테스트를 하여야 한다.
	* -- 데이터 길이는 매뉴얼 참고
	* 
	* -- 인터넷뱅킹 결제 요청 전문 포멧
	* + 데이터길이(6) + 암호화여부(1) + 데이터
	* + 데이터 포멧(데이터 구분은 "|"로 한다.)
	* 결제종류(10)			| 업체ID(20)			| 주문번호(40)			| 예금주명(20)			| 거래금액(8)		|
	* 은행코드(2)			| 예금주주민번호(13)	| 주문자연락처(16)		| 이메일주소(50)		| 상품명(100)		| 
	* 이용기관주문번호(50)	| FNBC 거래번호(20)		| 이체시각(14)			| 현금영수증발행여부(1)	| 회원아이디(20)	|
	* 거래자구분(2)			| 신분확인번호(13)		| 에스크로사용여부(1)	| 에스크로회원번호(17)	| 에스크로결제금액(8)|
	* 에스크로수수료금액(8) |
	* 
	****************************************************************/
	public boolean Iche_Ibk_ApproveRequest(String EncType, String AuthTy)
	{
		logger.info(pid + "Start Iche_Ibk_ApproveRequest [" + getValue("AuthTy") + "] " );
		String DataMsg = "";

		// 승인 요청 전문 생성.
		DataMsg = EncType+
			AuthTy + "|" +
			getValue("StoreId") +"|"+
			getValue("OrdNo") +"|"+
			getValue("ICHE_OUTBANKMASTER") +"|"+
			getValue("Amt") +"|"+ 
			getValue("ICHE_OUTBANKNAME") +"|"+
			getValue("ICHE_OUTACCTNO") +"|"+
			getValue("OrdPhone") +"|"+
			getValue("UserEmail") +"|"+
			getValue("ProdNm") +"|"+
			getValue("ICHE_POSMTID") +"|"+
			getValue("ICHE_FNBCMTID") +"|"+
			getValue("ICHE_APTRTS") +"|"+
			getValue("ICHE_CASHYN") +"|"+
			getValue("UserId") +"|"+
			getValue("ICHE_CASHGUBUN_CD") +"|"+
			getValue("ICHE_CASHID_NO") +"|"+
			getValue("ICHE_ECWYN") +"|"+
			getValue("ICHE_ECWID") +"|"+
			getValue("ICHE_ECWAMT1") +"|"+
			getValue("ICHE_ECWAMT2") +"|"+
			getValue("NetCancID") +"|";

		if (!msgSend(DataMsg))
		{
			logger.error(pid + "msgSend Fail" );
			setPayResult( "rSuccYn", "n");
			setPayResult( "rResMsg",  "MsgSend Fail");
			return false;
		}
		logger.info(pid + "End   Iche_Ibk_ApproveRequest " );
		
		return true;
	}


	/****************************************************************
	*
	* [10] 계좌이체 소켓 결제 - 텔레뱅킹
	* 
	* -- 이부분은 승인 처리를 위해 암호화Process와 Socket통신하는 부분이다.
	* 가장 핵심이 되는 부분이므로 수정후에는 테스트를 하여야 한다.
	* -- 데이터 길이는 매뉴얼 참고
	* 
	* -- 텔레뱅킹 결제 요청 전문 포멧
	* + 데이터길이(6) + 암호화여부(1) + 데이터
	* + 데이터 포멧(데이터 구분은 "|"로 한다.)
	* 결제종류(10)			| 업체ID(20)			| 주문번호(40)			| 예금주명(20)			| 거래금액(8)		|
	* 은행코드(2)			| 예금주주민번호(13)	| 주문자연락처(16)		| 이메일주소(50)		| 상품명(100)		| 
	* 이용기관주문번호(50)	| FNBC 거래번호(20)		| 이체시각(14)			| 현금영수증발행여부(1)	| 회원아이디(20)	|
	* 거래자구분(2)			| 신분확인번호(13)		| 에스크로사용여부(1)	| 에스크로회원번호(17)	| 에스크로결제금액(8)|
	* 에스크로수수료금액(8) |
	* 
	****************************************************************/
	public boolean Iche_Tbk_ApproveRequest(String EncType, String AuthTy)
	{
		logger.info(pid + "Start Iche_Tbk_ApproveRequest [" + getValue("AuthTy") + "] " );
		String DataMsg = "";

		// 승인 요청 전문 생성.
		DataMsg = EncType+
			AuthTy + "|" +
			getValue("StoreId") +"|"+
			getValue("OrdNo") +"|"+
			getValue("ICHEARS_POSMTID") +"|"+
			getValue("ICHEARS_ADMNO") +"|"+ 
			getValue("ICHEARS_CENTERCD") +"|"+
			getValue("Amt") +"|"+
			getValue("ICHE_OUTACCTNO") +"|"+
			getValue("ICHE_OUTBANKMASTER") +"|"+
			getValue("ICHEARS_HPNO") +"|"+
			getValue("UserEmail") +"|"+
			getValue("ProdNm") +"|"+
			getValue("ICHE_ECWYN") +"|"+
			getValue("ICHE_ECWID") +"|"+
			getValue("ICHE_ECWAMT1") +"|"+
			getValue("ICHE_ECWAMT2") +"|"+
			getValue("ICHE_CASHYN") +"|"+
			getValue("ICHE_CASHGUBUN_CD") +"|"+
			getValue("ICHE_CASHID_NO") +"|"+
			getValue("NetCancID") +"|";

		if (!msgSend(DataMsg))
		{
			logger.error(pid + "msgSend Fail" );
			setPayResult( "rSuccYn", "n");
			setPayResult( "rResMsg",  "MsgSend Fail");
			return false;
		}
		logger.info(pid + "End   Iche_Tbk_ApproveRequest " );
		
		return true;
	}


	/****************************************************************
	*
	* [9] 계좌이체 소켓 취소 - 인터넷뱅킹
	* 
	* -- 이부분은 취소 처리를 위해 암호화Process와 Socket통신하는 부분이다.
	* 가장 핵심이 되는 부분이므로 수정후에는 테스트를 하여야 한다.
	* -- 데이터 길이는 매뉴얼 참고
	* 
	* -- 인터넷뱅킹 취소 요청 전문 포멧
	* + 데이터길이(6) + 암호화여부(1) + 데이터
	* + 데이터 포멧(데이터 구분은 "|"로 한다.)
	* 결제종류(10)	| 업체ID(20)	| 이용기관주문번호(50)	| 거래금액(8)	|	* 은행코드(2)	|	NetCancID	|
	* 
	****************************************************************/
	public boolean Iche_Ibk_CancelRequest(String EncType, String AuthTy)
	{
		logger.info(pid + "Start Iche_Ibk_CancelRequest [" + getValue("AuthTy") + "] " );
		String DataMsg = "";

		// 승인 요청 전문 생성.
		DataMsg = EncType+
			AuthTy + "|" +
			getValue("StoreId") +"|"+
			getValue("ICHE_POSMTID") +"|"+
			getValue("Amt") +"|"+
			getValue("ICHE_OUTBANKNAME") +"|"+
			getValue("NetCancID") +"|";

		if (!cancelMsgSend(DataMsg))
		{
			logger.error(pid + "cancelMsgSend Fail" );
			setPayResult( "rSuccYn", "n");
			setPayResult( "rResMsg",  "cancelMsgSend Fail");
			return false;
		}
		logger.info(pid + "End   Iche_Ibk_CancelRequest " );
		
		return true;
	}

	/****************************************************************
	*
	* [10] 계좌이체 소켓 취소 - 텔레뱅킹
	* 
	* -- 이부분은 취소 처리를 위해 암호화Process와 Socket통신하는 부분이다.
	* 가장 핵심이 되는 부분이므로 수정후에는 테스트를 하여야 한다.
	* -- 데이터 길이는 매뉴얼 참고
	* 
	* -- 텔레뱅킹 취소 요청 전문 포멧
	* + 데이터길이(6) + 암호화여부(1) + 데이터
	* + 데이터 포멧(데이터 구분은 "|"로 한다.)
	* 결제종류(10)	| 업체ID(20)	| 이용기관주문번호(50)	| 거래금액(8)	|	* 은행코드(2)	|	NetCancID	|
	* 
	****************************************************************/
	public boolean Iche_Tbk_CancelRequest(String EncType, String AuthTy)
	{
		logger.info(pid + "Start Iche_Tbk_CancelRequest [" + getValue("AuthTy") + "] " );
		String DataMsg = "";

		// 승인 요청 전문 생성.
		DataMsg = EncType+
			AuthTy + "|" +
			getValue("StoreId") +"|"+
			"" +"|"+						//send_dt
			"" +"|"+						//mTid
			getValue("NetCancID") +"|";

		if (!cancelMsgSend(DataMsg))
		{
			logger.error(pid + "cancelMsgSend Fail" );
			setPayResult( "rSuccYn", "n");
			setPayResult( "rResMsg",  "cancelMsgSend Fail");
			return false;
		}
		logger.info(pid + "End   Iche_Tbk_CancelRequest " );
		
		return true;
	}


	/****************************************************************
		암호화 데몬으로 승인 요청/취소 전문을 전송하고 결과를 수신해서 수신전문을 리턴한다..
	****************************************************************/
	public boolean msgSend(String DataMsg )
	{
		logger.info(pid + "Start msgSend " );

		String SendMsg = "";
		String RecvMsg = "";
		
		try
		{

			SendMsg = formatMsg( Integer.toString( DataMsg.getBytes("MS949").length ), 6, '9' ) + DataMsg;
			
			logger.info(pid + "SendMsg : [" + SendMsg + "]" );
		
			// 데이터 전송|수신.
			RecvMsg = ProcessRequest( IPAddr, Port, SendMsg );

			// 데이터 파싱.			
			if( getValue("AuthTy").equals( "card" ) )
			{
				if( getValue("SubTy").equals( "isp" ) )
				{
					ISP_AuthSplitData = splitRecvMsg( RecvMsg, 9, "|" );
				}
				else if( getValue("SubTy").equals( "visa3d" ) || getValue("SubTy").equals( "normal" ) )
				{
					SplitData = splitRecvMsg( RecvMsg, 14, "|" );
				}
			}
			else if( getValue("AuthTy").equals( "iche" ) && getValue("ICHE_SOCKETYN").equals( "Y" ))
			{
				Iche_Ibk_SplitData = splitRecvMsg( RecvMsg, 7, "|" );					
			}
			else if( getValue("AuthTy").equals( "iche" ) && getValue("ICHEARS_SOCKETYN").equals( "Y" ))
			{
				Iche_Tbk_SplitData = splitRecvMsg( RecvMsg, 6, "|" );					
			}
			else if( getValue("AuthTy").equals( "virtual" ) )
			{
				VIR_SplitData = splitRecvMsg( RecvMsg, 6, "|" );						
			}
			
			else if( getValue("AuthTy").equals( "hp" ) )
			{
				HP_SplitData = splitRecvMsg( RecvMsg, 5, "|" );
			}
			else if( getValue("AuthTy").equals( "ars" ) )
			{
				ARS_SplitData = splitRecvMsg( RecvMsg, 5, "|" );						
			}			
			logger.info(pid + "End   msgSend " );
		}
		catch( IOException ie )
		{
			ErrMsg = "통신에러로 인한 승인요청 실패";
			ErrCd  = -1;
			
			setPayResult( "rSuccYn", "n");
			setPayResult( "rResMsg",  ie.toString() + "승인요청 실패");
			
			return false;
		}
		catch( NumberFormatException nfe )
		{
			ErrMsg = "데이터에러로 인한 승인요청 실패";
			ErrCd  = -2;

			setPayResult( "rSuccYn", "n");
			setPayResult( "rResMsg",  nfe.toString() + "승인요청 실패");
			
			return false;
		}
		catch( Exception e )
		{
			ErrMsg = "에러로 인한 승인요청 실패";
			ErrCd  = -99;

			setPayResult( "rSuccYn", "n");
			setPayResult( "rResMsg",  e.toString() + "승인요청 실패");

			return false;
		}
		
		return true;
	}	


	/****************************************************************
		암호화 데몬으로 승인 요청/취소 전문을 전송하고 결과를 수신해서 수신전문을 리턴한다..
	****************************************************************/
	public boolean cancelMsgSend(String DataMsg )
	{
		logger.info(pid + "Start cancelMsgSend " );
		String SendMsg = "";
		String RecvMsg = "";
		
		try
		{
			SendMsg = formatMsg( Integer.toString( DataMsg.getBytes("MS949").length ), 6, '9' ) + DataMsg;
			logger.info(pid + "SendMsg : [" + SendMsg + "]" );
		
			// 데이터 전송|수신.
			RecvMsg = ProcessRequest( IPAddr, Port, SendMsg );

			// 데이터 파싱.	
			if( getValue("AuthTy").equals( "card" ) )
			{
				if( getValue("SubTy").equals( "isp" ) )
				{
					ISP_CnclSplitData = splitRecvMsg( RecvMsg, 7, "|" );
				}
				else if( getValue("SubTy").equals( "visa3d" ) || getValue("SubTy").equals( "normal" ) )
				{
					SplitData = splitRecvMsg( RecvMsg, 15, "|" );
				}
			}else if( getValue("AuthTy").equals( "hp" ) )
			{
				HP_CnclSplitData = splitRecvMsg( RecvMsg, 6, "|" );
			}else if( getValue("AuthTy").equals( "ars" ) )
			{
				ARS_CnclSplitData = splitRecvMsg( RecvMsg, 6, "|" );
			}else if( getValue("AuthTy").equals( "iche" ) && getValue("ICHE_SOCKETYN").equals( "Y" ))
			{
				Iche_Ibk_CnclSplitData = splitRecvMsg( RecvMsg, 6, "|" );
			}else if( getValue("AuthTy").equals( "iche" ) && getValue("ICHEARS_SOCKETYN").equals( "Y" ))
			{
				Iche_Tbk_CnclSplitData = splitRecvMsg( RecvMsg, 5, "|" );
			}
			logger.info(pid + "End   cancelMsgSend " );
		}
		catch( IOException ie )
		{
			ErrMsg = "통신에러로 인한 취소요청 실패";
			ErrCd  = -1;
			
			setPayResult( "rCancelSuccYn", "n");
			setPayResult( "rCancelResMsg",  ie.toString() + "취소요청 실패");
			
			return false;
		}
		catch( NumberFormatException nfe )
		{
			ErrMsg = "데이터에러로 인한 취소요청 실패";
			ErrCd  = -2;

			setPayResult( "rCancelSuccYn", "n");
			setPayResult( "rCancelResMsg",  nfe.toString() + "취소요청 실패");
			
			return false;
		}
		catch( Exception e )
		{
			ErrMsg = "에러로 인한 취소요청 실패";
			ErrCd  = -99;

			setPayResult( "rCancelSuccYn", "n");
			setPayResult( "rCancelResMsg",  e.toString() + "취소요청 실패");

			return false;
		}
		
		return true;
	}	

	/****************************************************************
		암호화 데몬으로 승인 요청/취소 전문을 전송하고 결과를 수신해서 수신전문을 리턴한다..
	****************************************************************/
	private String ProcessRequest(String addr, int port, String SendMsg) 
		throws IOException, NumberFormatException
	{
		logger.info(pid + "Start ProcessRequest " );
		
		int    iRecvLen = 0;
		String RecvLen = "", RecvMsg = "";
		

		// 암호화 데몬에 접속 데이터 전송/수신
		this.connectSocket(addr, port);	
		
		logger.info(pid +  "connect IP : "+ addr + " port : " + port );
		

		// Server에 승인/취소요청 데이타를 보낸다.
		this.writeMsg(SendMsg.getBytes("MS949"));	
		
		try
		{
			RecvLen = new String( readMsg(6) );
			
			iRecvLen = Integer.parseInt( RecvLen );

			RecvMsg = new String( readMsg( iRecvLen ), "MS949" );


			// 수신 데이터 체크
			logger.info(pid +  "RecvData Length : " + RecvLen );
			logger.info(pid +  "RecvData : "   + RecvMsg );

		}
		catch( NumberFormatException nfe )
		{
			throw new NumberFormatException( "수신 전문길이 변환 에러!!" );
		}
		finally
		{
			closeSocket();
		}


		closeSocket();
		logger.info(pid + "End   ProcessRequest " );
		return RecvMsg;
	}

    public static String getPID()
    {
        DateFormat df = new SimpleDateFormat("SSS");
		processid = Integer.parseInt(df.format(new Date()) + String.valueOf(Math.random()).substring(2, 5));
        temp = new StringBuffer();
        temp.append("<");
        for(int i = 0; i < 6 - ("" + processid).length(); i++)
            temp.append("0");

        temp.append(processid);
        temp.append("> ");
        return temp.toString();
    }

	public void printHashtable(Hashtable ht)
	{
		Enumeration keyEnum = ht.keys();
		Enumeration valueEnum = ht.elements();
		while(valueEnum.hasMoreElements()){
		String key=(String)keyEnum.nextElement();
		String value=(String)valueEnum.nextElement();
		logger.info(pid + key + ":" + value );
		}
	}

	/****************************************************************
		createLogger
	****************************************************************/
    public boolean createLogger()
	{
		/*
			로그레벨 : debug, info, warn, error, fatal
		*/
	    String filename = "";
	    String logType = "AgsPay40_jsp";	
		layout = new PatternLayout("[%d{yyyy-MM-dd HH:mm:ss:SSS }]%5p %m%n");
		try{
			filename = getValue("AgsPayHome") + "/log/" + logType + "_" + getValue("StoreId") + "_" + nowDate + ".log";
			logger = Logger.getLogger(logType);
			appender = (FileAppender)logger.getAppender(logType);
			if(appender == null)
			{
				appender = new FileAppender(layout, filename);
				appender.setName(logType);
				logger.addAppender(appender);

			}else if(!appender.getFile().endsWith(nowDate + ".log"))
			{
				logger.removeAppender(logType);
				appender.close();            
				appender = new FileAppender(layout, filename);
				appender.setName(logType);
				logger.addAppender(appender);

			}else if(!appender.getFile().equals(filename))
			{
				logger.removeAppender(logType);
				appender.close();            
				appender = new FileAppender(layout, filename);
				appender.setName(logType);
				logger.addAppender(appender);

			}
			if(getValue("logLevel").toUpperCase().equals("DEBUG"))
			{
				logger.setLevel(Level.DEBUG);
			}else if(getValue("logLevel").toUpperCase().equals("INFO"))
			{
				logger.setLevel(Level.INFO);
			}else if(getValue("logLevel").toUpperCase().equals("WARN"))
			{
				logger.setLevel(Level.WARN);
			}else if(getValue("logLevel").toUpperCase().equals("ERROR"))
			{
				logger.setLevel(Level.ERROR);
			}else if(getValue("logLevel").toUpperCase().equals("FATAL"))
			{
				logger.setLevel(Level.FATAL);
			}else{
				logger.setLevel(Level.INFO);
			}

			logger.info("===========================================================");
			logger.info(pid + "START " + logType + "(OS:" + System.getProperty("os.name") + ", JAVA:" + System.getProperty("java.version") + ")");
			logger.info(pid + "log file : " + filename );
			
			return true;
	    }catch(Exception e){
			System.out.println("로그설정에 문제가 있습니다. 실행권한 체크등을 해보길 바랍니다.");
			return false;
		}	    
	}

	/****************************************************************
		Make NetCancel ID();
	****************************************************************/

	public boolean MakeNetCancID()
	{
		logger.info(pid + "Start Make NetCancel ID" );
		String TidTp = "";
		String NetCancelID = "";

		if(getValue("AuthTy").equals("card")) {	// 신용카드
		  	if( getValue("SubTy").equals("isp") )	TidTp = "ISP_"; else  TidTp = "VISA"; 
		}else if(getValue("AuthTy").equals("iche") ) { 		// 은행 계좌 이체 (IBK:인터넷뱅킹, TBK:텔레뱅킹)
			if( getValue("ICHE_SOCKETYN").equals("Y") ){
				TidTp = "IBK_";			
			}else if( getValue("ICHEARS_SOCKETYN").equals("Y") ) {
				TidTp = "TBK_";
			}else{
				TidTp = "IBK_";	
			}
		}else if(getValue("AuthTy").equals("virtual") ) {	// 가상계좌
			TidTp = "VIR_";
		}else if(getValue("AuthTy").equals("hp") ) {	// 휴대폰 결제
			TidTp = "HPP_";
		}else if(getValue("AuthTy").equals("ars") ) {	// ARS 전화결제
			TidTp = "ARS_";
		}else{
		  TidTp = "UNKW";	//미확인 결제방식
		}

		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");

		//TID는 최소 31자리 ,최대 51자리를 넘지 않는다.
		setPayResult("NetCancID", "AEGIS_"+TidTp + getValue("StoreId") + "_" + df.format(new Date()) + String.valueOf(Math.random()).substring(2, 5));
		if( !(getResult("NetCancID").length() >= 31 && getResult("NetCancID").length() <= 51) )
		{
			logger.error(pid + getResult("NetCancID") );
			return false;
		}
		logger.info(pid + "NetCancID[" + getResult("NetCancID")+ "]" );
		logger.info(pid + "End Make NetCancel ID " );
		return true;
	}

	/****************************************************************
		데이터 확인요청
	****************************************************************/
 	public boolean checkPayResult( String netCancID ) 
	{
		logger.info("===========================================================");
		logger.info(pid + "[" + netCancID + "] Check PayResult Start !!");
		
		String AuthTy = "";
		String DataMsg = "";

		if(netCancID == null || netCancID.equals(""))	netCancID = getResult("NetCancID");
			
		if (netCancID != null && netCancID.length() > 10){
			AuthTy = netCancID.substring(6,10);
		}else{
			logger.error(pid + "NetCancID Error[" + netCancID + "] ");
			return false;
		}
		
		if( AuthTy.equals("ISP_") ){ 			// 신용카드 ISP
			setValue("AuthTy","card") ;
			setValue("SubTy","isp") ;
		}else if( AuthTy.equals("VISA") ){ 			// 신용카드 안심클릭
		  	setValue("AuthTy","card") ;
			setValue("SubTy","visa3d") ;
		}else if( AuthTy.equals("IBK_") ){ 			// 계좌이체(인터넷뱅킹)
		  	setValue("AuthTy","iche") ;
		}else if( AuthTy.equals("TBK_") ){ 			// 계좌이체(텔레뱅킹)
			setValue("AuthTy","iche") ;
		}else if( AuthTy.equals("VIR_") ){ 			// 가상계좌
		  	setValue("AuthTy","virtual") ;
		}else if( AuthTy.equals("HPP_") ){ 			// 핸드폰
		  	setValue("AuthTy","hp") ;
		}else if( AuthTy.equals("ARS_") ){ 			// ARS
		  	setValue("AuthTy","ars") ;
		}else{ 										// 미확인결제방식
		  	logger.error(pid + "Check PayResult Fail for Unknown AuthTy[" + AuthTy + "]");
			return false;
		}  
	
		//가상계좌는 payCheck 처리하지 않음.
		/*
		if(getValue("AuthTy").equals("virtual"))
		{
			logger.info(pid + "Check PayResult Passed AuthTy[" + getValue("AuthTy") + "]");
			return false;
		}
		*/

		/****************************************************************************
		* 전송 전문 Make
		****************************************************************************/
		ENCTYPE = "I";
		
		DataMsg = ENCTYPE +
			"PayInfo"+"|"+
			getValue("StoreId")+"|"+
			netCancID+"|";
		
		if (!msgSend(DataMsg))
		{
			if( getErrCd() == -1 )
			{
				setPayResult( "rSuccYn", "n");
				setPayResult( "rResMsg", "통신오류(sock)로 인한 승인거절");
				return false;
			}
			else if( getErrCd() == -2 )
			{
				setPayResult( "rSuccYn", "n");
				setPayResult( "rResMsg", "통신오류(msg)로 인한 승인거절");
				return false;
			}else{
				setPayResult( "rSuccYn", "n");
				setPayResult( "rResMsg",  "MsgSend Fail");
			}
			logger.error(pid + "Check PayResult msgSend Fail" );
			
			return false;
		}

		/** 응답메세지를 Parsing 처리하여 결과값을 변수에 할당한다. **/
		if( !(parseMsg()) ){
			logger.error(pid + "Check PayResult parseMsg Fail" );
			return false;
		}

		logger.info(pid + "Check PayResult End");
		
		logger.info(pid + "Start Result Data From PG  [" );
		printHashtable(resultData);
		logger.info(pid + "] End  Result Data From PG " );

		return true;
	}

	/****************************************************************
		소켓 접속
	****************************************************************/
	private void connectSocket(String addr, int port) throws IOException 
	{
		try
		{
			logger.info(pid + "Start connectSocket " );

			socket = new Socket(addr, port);
			
			in     = new DataInputStream(socket.getInputStream());
			
			out    = new DataOutputStream(socket.getOutputStream());

			logger.info(pid + "End   connectSocket " );

		}
		catch( IOException e )
		{
			throw new IOException("Cannot connect the server : " + addr + ":" + port + "!!");
		}
	}

	/****************************************************************
		데이터 전송
	****************************************************************/
	private void writeMsg(byte[] msg) throws IOException
	{
		try
		{
			out.write(msg);
			out.flush();
		}
		catch( IOException e )
		{
			throw new IOException("Cannot write to socket!!");
		}
	}

	/****************************************************************
		데이터 수신
	****************************************************************/
	private byte[] readMsg(int size) throws IOException
	{
		try
		{
			byte[] msg = new byte[size];
			in.read(msg);
			return msg;
		}
		catch( IOException e )
		{
			throw new IOException("Cannot read from socket!!");
		}
	}

	/****************************************************************
		소켓 Close
	****************************************************************/
	private void closeSocket() throws IOException
	{
		try
		{
			socket.close();
		}
		catch( IOException e )
		{
			throw new IOException("Cannot close socket!!");
		}
	}

	/****************************************************************
		문자열을 지정된 길이로 지정된 문자로 채워서 포멧을 한다.
	 * @throws UnsupportedEncodingException 
	****************************************************************/
	private String formatMsg(String str, int len, char ctype) throws UnsupportedEncodingException
	{
		String formattedstr = new String();
		byte[] buff;
		int filllen = 0;

		buff = str.getBytes("MS949");

		filllen = len - buff.length;
		formattedstr = "";
		if(ctype == '9')
		{
			// 숫자열인 경우
			for(int i = 0; i<filllen;i++)
			{
					formattedstr += "0";
			}

			formattedstr = formattedstr + str;
		}
		else 
		{
			// 문자열인 경우
			for(int i = 0; i<filllen;i++)
			{
					formattedstr += " ";
			}

			formattedstr = str + formattedstr;
		}

		return formattedstr;
	}

	private String setTrim(String str, int len) throws UnsupportedEncodingException
	{
		byte[] subbytes;
		String tmpStr;
		
		subbytes = new byte[len];
		
		System.arraycopy(str.getBytes("MS949"), 0, subbytes, 0, len);
		
		tmpStr = new String(subbytes);
		
		if(tmpStr.length() == 0) 
		{
			subbytes = new byte[len-1];
			System.arraycopy(str.getBytes("MS949"), 0, subbytes, 0, len-1);
			tmpStr = new String(subbytes);
		}

		return tmpStr;
	}

	/****************************************************************
		지정된 포멧 순서대로 포멧된 데이터를 리턴
	****************************************************************/
	public String[] getRecvData( )
	{
		return SplitData;
	}

	public String[] getISP_AuthRecvData( )
	{
		return ISP_AuthSplitData;
	}

	public String[] getISP_CnclRecvData( )
	{
		return ISP_CnclSplitData;
	}

	public String[] getHP_RecvData( )
	{
		return HP_SplitData;
	}
	
	public String[] getHP_CnclRecvData( )
	{
		return HP_CnclSplitData;
	}

	public String[] getARS_RecvData( )
	{
		return ARS_SplitData;
	}

	public String[] getARS_CnclRecvData( )
	{
		return ARS_CnclSplitData;
	}

	public String[] getVIR_RecvData( )
	{
		return VIR_SplitData;
	}

	public String[] getIche_Ibk_RecvData( )
	{
		return Iche_Ibk_SplitData;
	}

	public String[] getIche_Ibk_CnclRecvData( )
	{
		return Iche_Ibk_CnclSplitData;
	}

	public String[] getIche_Tbk_RecvData( )
	{
		return Iche_Tbk_SplitData;
	}

	public String[] getIche_Tbk_CnclRecvData( )
	{
		return Iche_Tbk_CnclSplitData;
	}

	public String[] getVirAcctReturnData()
    {
        return VirAcctReturnData;
    }
	
	public String getErrMsg()
	{
		return ErrMsg;
	}

	public int getErrCd()
	{
		return ErrCd;
	}


	/*
		결제데이터 Get                      
	*/
    public String getResult(String name)
    {
		String result = "";
        if(resultData.get(name) != null)
            result = (String)resultData.get(name);
        else
            result = (String)requestData.get(name);

		if (result == null)	result = "";

		return result;
    }

	/*
		결제데이터 Get                      
	*/
    public String getValue(String name)
    {
		String result = "";
        if(requestData.get(name) != null)
            result = (String)requestData.get(name);
        else
            result = (String)resultData.get(name);

		if (result == null)	result = "";

		return result;
    }

	/*
		결제데이터 Set                      
	*/
    public void setValue(String name, String value)
    {
        	
		if(value != null){
			requestData.put(name, value);
        }else{
            requestData.put(name, "");
		}
    }

	/*
		결제결과 rSuccYn Set                      
	*/
 	public void setPayResult( String name, String value ) 
	{
		if(value != null)
            resultData.put(name, value);
        else
            resultData.put(name, "");
	}


	/****************************************************************
		문자열에 한글이 있는지 체크
	****************************************************************/
	public boolean checkHangul( String OrgData )
	{
		char oneChar;

		for( int i = 0; i < OrgData.length(); i++ )
		{
			oneChar = OrgData.charAt( i );
			
			if( oneChar >= '\uAC00' && oneChar <= '\uD7A3' )
			{
				return true;
			}
		}

		return false;
	}

	/****************************************************************
		스트링이 숫자인지 체크
	****************************************************************/
	public boolean checkNumber( String OrgData )
	{
		long iNum;
		
		try 
		{
			iNum = Long.parseLong( OrgData );
		}
		catch( NumberFormatException nfe )
		{
			return false;
		}

		return true;
	}

	/****************************************************************
		공백,null값 체크
	****************************************************************/
	public boolean checkNull( String OrgData )
	{
		if( OrgData == null || OrgData.equals( "null" ) || OrgData.equals( "NULL" ) || OrgData.equals( "" ) )
		{
			return true;
		}

		return false;
	}
	
	public String null2Space( String OrgData )
	{

		if( checkNull( OrgData ) ) return "";
		
		return OrgData.trim();

	}

	/****************************************************************
		경고창을 띄우고 옵션에따라 페이지 이동
	****************************************************************/
	public void alertMsg( JspWriter out, String ErrMsg, String Go )
	{
		try
		{
			out.println( "<script language='javascript'>" );
			out.println( "alert( '" + ErrMsg + "' );" );

			if( Go != null )
			{
				out.println( "history.go( " + Go + " );" );
			}

			out.println( "</script>" );
		}
		catch( IOException ie )
		{
			System.out.println( ie.toString() );
		}

	}

	public void alertGoBack( JspWriter out, String ErrMsg )
	{
		alertMsg( out, ErrMsg, "-1" );
	}
	
	/****************************************************************
		이지스PG에서 사용하는 카드 데이터 Encrypt메소드
	****************************************************************/
	private String encryptAegis( String OrgData ) throws NumberFormatException 
	{
		int    iDataLen, iOneEnc;
		String ReverseData = "", EncData = "";

		if( OrgData == null || OrgData.equals("") )
			return "";
		

		//ReverseData = new String();
		iDataLen = OrgData.length();

		for( int i = 0; i < iDataLen ; i++ )
		{
			ReverseData += OrgData.substring( ( iDataLen - 1 ) - i , ( iDataLen  ) - i );
		}

		//System.out.println( "Reverse Data : " + ReverseData );

		try
		{
			//EncData = new String();
			iDataLen = ReverseData.length();

			for( int i = 0; i < iDataLen; i++ )
			{
				iOneEnc = ( Integer.parseInt( ReverseData.substring( i, i + 1 ) ) + i * 77 ) % 10;
				EncData += Integer.toString( iOneEnc );
			}
		}
		catch ( NumberFormatException nfe )
		{
			throw new NumberFormatException( "데이터(" + OrgData + ") Encrypt 실패" );			
			//throw NumberFormatException( nfe.toString() );
		}

		return EncData;
	}

	

	/****************************************************************
		수신 전문을 Delimeter로 분할한다.
	****************************************************************/
	private String[] splitRecvMsg( String RecvMsg, int RegexNum, String Regex)
	{
		logger.info(pid + "Start splitRecvMsg" );
		String SplitData[];
		int    iRegexNum   = 0;
		int    iRegexPosOld = 0;
		int    iRegexPosNew = 0;

		//
		//	문자열을 Regex문자 기준으로 분할
		//	Regex 갯수를 카운트해서 RegexNum고 같지 않으면 메세지 포멧 에러로 보고 false 리턴
		//
	
		SplitData = new String[RegexNum];

		while( ( iRegexPosNew = RecvMsg.indexOf( Regex, iRegexPosOld ) ) != -1 )
		{
			SplitData[iRegexNum] = RecvMsg.substring( iRegexPosOld, iRegexPosNew );

			if( checkNull( SplitData[iRegexNum] ) )
			{
				SplitData[iRegexNum] = "";
			}
			if( checkNumber( SplitData[iRegexNum] ) )
			{
				if( Long.parseLong( SplitData[iRegexNum] ) == 0 )
				{
					SplitData[iRegexNum] = "";
				}
			}

			//System.out.println( "SplitData " + Integer.toString(iRegexNum) + 
			//					  " : " + SplitData[iRegexNum] );

			iRegexPosOld = iRegexPosNew + 1;		
			iRegexNum++;
		}

		if( iRegexNum != RegexNum ) {
			logger.error(pid + "splitRecvMsg Fail " );
		}
		logger.info(pid + "End   splitRecvMsg" );

		return SplitData;
	}



}
