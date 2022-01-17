package aegis.cashclient;

import java.io.*;
import java.util.*;
import java.net.*;

import javax.servlet.jsp.JspWriter;

/******************************************************************************
	pg클라이언트 통신 모듈
******************************************************************************/
public class CashClientBean
{
	private Socket		socket;
	private DataInputStream   	in;
  	private DataOutputStream  	out;
	
	private String IPAddr;
	private int	   Port;
	
	private String ErrMsg;
	private int    ErrCd;
	private String SplitData[]      = new String[7];	// 수신 데이터 버퍼
	private String SplitData_canc[] = new String[7];	// 수신 데이터 버퍼
	
	
	/****************************************************************
		생성자( 접속주소, 포트 )
	****************************************************************/
 public CashClientBean(String IPAddr, int Port)
	{
		this.IPAddr = IPAddr;
		this.Port   = Port;
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

		if( iRegexNum != RegexNum ) return null;
		
		return SplitData;
	}

	/****************************************************************
		승인요청 전문을 생성하고 암호화 데몬 프로세스로 데이터를 전송하고 응답을 수신한다.

		< 데이터 포멧 >
		결제종류	 | 결제방식		| 업체ID		| 회원ID	| 주문번호   |
		단말기번호   | 공급가액	    | 부가가치세	| 봉사료	| 거래자구분 | 
		신분확인번호 | 이메일주소	| \n
		< 승인응답 데이터 포맷 >
		업체ID       | 주문번호     | 승인번호      | 성공여부  | 응답메세지 |
		알림메시지1  | 알림메시지2  |
	****************************************************************/
	public boolean cashrecipt(String ENCTYPE,String Pay_kind,String Pay_type,String Retailer_id,String Cust_no,String Ord_No,String Cat_id, 
				String Amtcash,String Amttex,String Amtadd,String Gubun_cd,String Confirm_no,String email,String goods_nm )
	{
		String DataMsg = "";
		int    DataLen = 0;
		String SendMsg = "";
		String RecvMsg = "";
		
		try
		{

			//	승인 요청 전문 생성.
			DataMsg =  ENCTYPE + Pay_kind + "|" + Pay_type + "|" + 
				  Retailer_id + "|" + Cust_no + "|" + Ord_No + "|" + Cat_id + "|" + 
				  Amtcash + "|" + Amttex + "|" + Amtadd + "|" + Gubun_cd + "|" + 
				  Confirm_no + "|" + email + "|" + goods_nm + "|";

			SendMsg = formatMsg( Integer.toString( DataMsg.getBytes("MS949").length ), 6, '9' ) + DataMsg;

			System.out.println("DataMsg ==>" +DataMsg);
			System.out.println("SendMsg ==>" + SendMsg);

			// 데이터 전송|수신.
			RecvMsg = ProcessRequest( IPAddr, Port, SendMsg );

			// 데이터 파싱.
			SplitData = splitRecvMsg( RecvMsg, 7, "|" );

		}
		catch( IOException ie )
		{
			ErrMsg = "통신에러로 인한 승인요청 실패";
			ErrCd  = -1;

			System.out.println( ie.toString() );
			System.out.println( "승인요청 실패" );

			return false;
		}
		catch( NumberFormatException nfe )
		{
			ErrMsg = "데이터에러로 인한 승인요청 실패";
			ErrCd  = -2;

			System.out.println( nfe.toString() );
			System.out.println( "승인요청 실패" );

			return false;
		}
		catch( Exception e )
		{
			ErrMsg = "에러로 인한 승인요청 실패";
			ErrCd  = -99;

			System.out.println( e.toString() );
			System.out.println( "승인요청 실패" );

			return false;
		}
		
		return true;
	}

	/****************************************************************
		승인취소 전문을 생성하고 암호화 데몬 프로세스로 데이터를 전송하고 응답을 수신한다.

		< 데이터 포멧 >
		결제종류	 | 결제방식		 | 업체ID		| 회원ID	| 주문번호   |
		단말기번호   | 공급가액	     | 부가가치세	| 봉사료	| 거래자구분 | 
		신분확인번호 | 원거래승인번호| 이메일주소	| \n
		< 승인응답 데이터 포맷 >
		업체ID       | 주문번호     | 승인번호      | 성공여부  | 응답메세지 |
		알림메시지1  | 알림메시지2  |
	****************************************************************/
	public boolean cashreciptcanc(String ENCTYPE,String Pay_kind,String Pay_type,String Retailer_id,String Cust_no,String Ord_No,String Cat_id, 
				String Amtcash,String Amttex,String Amtadd,String Gubun_cd,String Confirm_no, String Org_adm_no,String email,String goods_nm )
	{
		String DataMsg = "";
		int    DataLen = 0;
		String SendMsg = "";
		String RecvMsg = "";
		
		try
		{

			//	취소 요청 전문 생성.
			DataMsg =  ENCTYPE + Pay_kind + "|" + Pay_type + "|" + 
				  Retailer_id + "|" + Cust_no + "|" + Ord_No + "|" + Cat_id + "|" + 
				  Amtcash + "|" + Amttex + "|" + Amtadd + "|" + Gubun_cd + "|" + 
				  Confirm_no + "|" + Org_adm_no + "|" + email + "|" + goods_nm + "|";

			SendMsg = formatMsg( Integer.toString( DataMsg.getBytes("MS949").length ), 6, '9' ) + DataMsg;
			
			// 데이터 전송|수신.
			RecvMsg = ProcessRequest( IPAddr, Port, SendMsg );

			// 데이터 파싱.
			SplitData_canc = splitRecvMsg( RecvMsg, 7, "|" );

		}
		catch( IOException ie )
		{
			ErrMsg = "통신에러로 인한 승인요청 실패";
			ErrCd  = -1;

			System.out.println( ie.toString() );
			System.out.println( "승인요청 실패" );

			return false;
		}
		catch( NumberFormatException nfe )
		{
			ErrMsg = "데이터에러로 인한 승인요청 실패";
			ErrCd  = -2;

			System.out.println( nfe.toString() );
			System.out.println( "승인요청 실패" );

			return false;
		}
		catch( Exception e )
		{
			ErrMsg = "에러로 인한 승인요청 실패";
			ErrCd  = -99;

			System.out.println( e.toString() );
			System.out.println( "승인요청 실패" );

			return false;
		}
		
		return true;
	}

	/****************************************************************
		임시저장(승인) 전문을 생성하고 암호화 데몬 프로세스로 데이터를 전송하고 응답을 수신한다.

		< 데이터 포멧 >
		결제종류	 | 결제방식		| 업체ID		| 회원ID	| 주문번호   |
		단말기번호   | 공급가액	    | 부가가치세	| 봉사료	| 거래자구분 | 
		신분확인번호 | 이메일주소	| \n
		< 승인응답 데이터 포맷 >
		업체ID       | 주문번호     |  성공여부  | 응답메세지 |
	****************************************************************/
	public boolean cashrecipttemp(String ENCTYPE,String Pay_kind,String Pay_type,String Retailer_id,String Cust_no,String Ord_No,String Cat_id, 
				String Amtcash,String Amttex,String Amtadd,String Gubun_cd,String Confirm_no,String email,String goods_nm )
	{
		String DataMsg = "";
		int    DataLen = 0;
		String SendMsg = "";
		String RecvMsg = "";
		
		try
		{

			//	승인 요청 전문 생성.
			DataMsg =  ENCTYPE + Pay_kind + "|" + Pay_type + "|" + 
				  Retailer_id + "|" + Cust_no + "|" + Ord_No + "|" + Cat_id + "|" + 
				  Amtcash + "|" + Amttex + "|" + Amtadd + "|" + Gubun_cd + "|" + 
				  Confirm_no + "|" + email + "|" + goods_nm + "|";

			SendMsg = formatMsg( Integer.toString( DataMsg.getBytes("MS949").length ), 6, '9' ) + DataMsg;

System.out.println("DataMsg ==>" +DataMsg);
System.out.println("SendMsg ==>" + SendMsg);

			// 데이터 전송|수신.
			RecvMsg = ProcessRequest( IPAddr, Port, SendMsg );

			// 데이터 파싱.
			SplitData = splitRecvMsg( RecvMsg, 4, "|" );

		}
		catch( IOException ie )
		{
			ErrMsg = "통신에러로 인한 승인요청 실패";
			ErrCd  = -1;

			System.out.println( ie.toString() );
			System.out.println( "승인요청 실패" );

			return false;
		}
		catch( NumberFormatException nfe )
		{
			ErrMsg = "데이터에러로 인한 승인요청 실패";
			ErrCd  = -2;

			System.out.println( nfe.toString() );
			System.out.println( "승인요청 실패" );

			return false;
		}
		catch( Exception e )
		{
			ErrMsg = "에러로 인한 승인요청 실패";
			ErrCd  = -99;

			System.out.println( e.toString() );
			System.out.println( "승인요청 실패" );

			return false;
		}
		
		return true;
	}

	/****************************************************************
		임시저장(취소) 전문을 생성하고 암호화 데몬 프로세스로 데이터를 전송하고 응답을 수신한다.

		< 데이터 포멧 >
		결제종류	 | 결제방식		 | 업체ID		| 회원ID	| 주문번호   |
		단말기번호   | 공급가액	     | 부가가치세	| 봉사료	| 거래자구분 | 
		신분확인번호 | 원거래승인번호| 이메일주소	| \n
		< 승인응답 데이터 포맷 >
		업체ID       | 주문번호     | 승인번호      | 성공여부  | 응답메세지 |
		알림메시지1  | 알림메시지2  |
	****************************************************************/
	public boolean cashreciptcanctemp(String ENCTYPE,String Pay_kind,String Pay_type,String Retailer_id,String Cust_no,String Ord_No,String Cat_id, 
				String Amtcash,String Amttex,String Amtadd,String Gubun_cd,String Confirm_no, String Org_adm_no,String email,String goods_nm )
	{
		String DataMsg = "";
		int    DataLen = 0;
		String SendMsg = "";
		String RecvMsg = "";
		
		try
		{

			//	취소 요청 전문 생성.
			DataMsg =  ENCTYPE + Pay_kind + "|" + Pay_type + "|" + 
				  Retailer_id + "|" + Cust_no + "|" + Ord_No + "|" + Cat_id + "|" + 
				  Amtcash + "|" + Amttex + "|" + Amtadd + "|" + Gubun_cd + "|" + 
				  Confirm_no + "|" + Org_adm_no + "|" + email + "|" + goods_nm + "|";

			SendMsg = formatMsg( Integer.toString( DataMsg.getBytes("MS949").length ), 6, '9' ) + DataMsg;
			
			// 데이터 전송|수신.
			RecvMsg = ProcessRequest( IPAddr, Port, SendMsg );

			// 데이터 파싱.
			SplitData_canc = splitRecvMsg( RecvMsg, 4, "|" );

		}
		catch( IOException ie )
		{
			ErrMsg = "통신에러로 인한 승인요청 실패";
			ErrCd  = -1;

			System.out.println( ie.toString() );
			System.out.println( "승인요청 실패" );

			return false;
		}
		catch( NumberFormatException nfe )
		{
			ErrMsg = "데이터에러로 인한 승인요청 실패";
			ErrCd  = -2;

			System.out.println( nfe.toString() );
			System.out.println( "승인요청 실패" );

			return false;
		}
		catch( Exception e )
		{
			ErrMsg = "에러로 인한 승인요청 실패";
			ErrCd  = -99;

			System.out.println( e.toString() );
			System.out.println( "승인요청 실패" );

			return false;
		}
		
		return true;
	}

	/****************************************************************
		미등록상점용 승인요청 전문을 생성하고 암호화 데몬 프로세스로 데이터를 전송하고 응답을 수신한다.

		< 데이터 포멧 >
		결제종류	 | 결제방식		| 업체ID		| 회원ID	| 주문번호   |
		단말기번호   | 공급가액	    | 부가가치세	| 봉사료	| 거래자구분 | 
		신분확인번호 | 이메일주소	| 상품명		| 사업자번호| 상점명	 | 
		상점URL주소  | 대표자명	    | 주소			| 연락처	| \n
		< 승인응답 데이터 포맷 >
		업체ID       | 주문번호     | 승인번호      | 성공여부  | 응답메세지 |
		알림메시지1  | 알림메시지2  |
	****************************************************************/
	public boolean noncashrecipt(String ENCTYPE,String Pay_kind,String Pay_type,String Retailer_id,String Cust_no,String Ord_No,String Cat_id, 
				String Amtcash,String Amttex,String Amtadd,String Gubun_cd,String Confirm_no,String email,String goods_nm,String Corp_no,
				String Corp_nm,String Url,String Ceo_nm,String Addr,String Tel_no )
	{
		String DataMsg = "";
		int    DataLen = 0;
		String SendMsg = "";
		String RecvMsg = "";
		
		try
		{

			//	승인 요청 전문 생성.
			DataMsg =  ENCTYPE + Pay_kind + "|" + Pay_type + "|" + 
				  Retailer_id + "|" + Cust_no + "|" + Ord_No + "|" + Cat_id + "|" + 
				  Amtcash + "|" + Amttex + "|" + Amtadd + "|" + Gubun_cd + "|" + 
				  Confirm_no + "|" + email + "|" + goods_nm + "|" + Corp_no + "|" + 
				  Corp_nm + "|" + Url + "|" + Ceo_nm + "|" + Addr + "|" + Tel_no + "|";

			SendMsg = formatMsg( Integer.toString( DataMsg.getBytes("MS949").length ), 6, '9' ) + DataMsg;

			System.out.println("DataMsg ==>" +DataMsg);
			System.out.println("SendMsg ==>" + SendMsg);

			// 데이터 전송|수신.
			RecvMsg = ProcessRequest( IPAddr, Port, SendMsg );

			// 데이터 파싱.
			SplitData = splitRecvMsg( RecvMsg, 7, "|" );

		}
		catch( IOException ie )
		{
			ErrMsg = "통신에러로 인한 승인요청 실패";
			ErrCd  = -1;

			System.out.println( ie.toString() );
			System.out.println( "승인요청 실패" );

			return false;
		}
		catch( NumberFormatException nfe )
		{
			ErrMsg = "데이터에러로 인한 승인요청 실패";
			ErrCd  = -2;

			System.out.println( nfe.toString() );
			System.out.println( "승인요청 실패" );

			return false;
		}
		catch( Exception e )
		{
			ErrMsg = "에러로 인한 승인요청 실패";
			ErrCd  = -99;

			System.out.println( e.toString() );
			System.out.println( "승인요청 실패" );

			return false;
		}
		
		return true;
	}

	/****************************************************************
		승인취소 전문을 생성하고 암호화 데몬 프로세스로 데이터를 전송하고 응답을 수신한다.

		< 데이터 포멧 >
		결제종류	 | 결제방식		 | 업체ID		| 회원ID	| 주문번호   |
		단말기번호   | 공급가액	     | 부가가치세	| 봉사료	| 거래자구분 | 
		신분확인번호 | 원거래승인번호| 이메일주소	| 상품명	| 사업자번호 | 
		상점명		 | 상점URL주소   | 대표자명	    | 주소		| 연락처	 | \n
		< 승인응답 데이터 포맷 >
		업체ID       | 주문번호     | 승인번호      | 성공여부  | 응답메세지 |
		알림메시지1  | 알림메시지2  |
	****************************************************************/
	public boolean noncashreciptcanc(String ENCTYPE,String Pay_kind,String Pay_type,String Retailer_id,String Cust_no,String Ord_No,String Cat_id, 
				String Amtcash,String Amttex,String Amtadd,String Gubun_cd,String Confirm_no, String Org_adm_no,String email,String goods_nm,
				String Corp_no,String Corp_nm,String Url,String Ceo_nm,String Addr,String Tel_no)
	{
		String DataMsg = "";
		int    DataLen = 0;
		String SendMsg = "";
		String RecvMsg = "";
		
		try
		{

			//	취소 요청 전문 생성.
			DataMsg =  ENCTYPE + Pay_kind + "|" + Pay_type + "|" + 
				  Retailer_id + "|" + Cust_no + "|" + Ord_No + "|" + Cat_id + "|" + 
				  Amtcash + "|" + Amttex + "|" + Amtadd + "|" + Gubun_cd + "|" + 
				  Confirm_no + "|" + Org_adm_no + "|" + email + "|" + goods_nm + "|" + 
				  Corp_no + "|" + Corp_nm + "|" + Url + "|" + Ceo_nm + "|" + Addr + "|" + Tel_no + "|";

			SendMsg = formatMsg( Integer.toString( DataMsg.getBytes("MS949").length ), 6, '9' ) + DataMsg;
			
			// 데이터 전송|수신.
			RecvMsg = ProcessRequest( IPAddr, Port, SendMsg );

			// 데이터 파싱.
			SplitData_canc = splitRecvMsg( RecvMsg, 7, "|" );

		}
		catch( IOException ie )
		{
			ErrMsg = "통신에러로 인한 승인요청 실패";
			ErrCd  = -1;

			System.out.println( ie.toString() );
			System.out.println( "승인요청 실패" );

			return false;
		}
		catch( NumberFormatException nfe )
		{
			ErrMsg = "데이터에러로 인한 승인요청 실패";
			ErrCd  = -2;

			System.out.println( nfe.toString() );
			System.out.println( "승인요청 실패" );

			return false;
		}
		catch( Exception e )
		{
			ErrMsg = "에러로 인한 승인요청 실패";
			ErrCd  = -99;

			System.out.println( e.toString() );
			System.out.println( "승인요청 실패" );

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
		int    iRecvLen = 0;
		String RecvLen = "", RecvMsg = "";
		

		//	암호화 데몬에 접속 데이터 전송/수신
		this.connectSocket(addr, port);	
		
		System.out.println( "접속 주소 : "+ addr + " 포트 : " + port );
		

		// Server에 승인/취소요청 데이타를 보낸다.
		this.writeMsg(SendMsg.getBytes("MS949"));	
		
		try
		{

			RecvLen = new String( readMsg(6) );
			
			iRecvLen = Integer.parseInt( RecvLen );

			RecvMsg = new String( readMsg( iRecvLen ) );


			// 수신 데이터 체크
			System.out.println( "수신데이터 길이 : " + RecvLen );
			System.out.println( "수신데이터 : "   + RecvMsg );

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

		return RecvMsg;
	}

	/****************************************************************
		소켓 접속
	****************************************************************/
	private void connectSocket(String addr, int port) throws IOException 
	{
		try
		{

			socket = new Socket(addr, port);
			
			in     = new DataInputStream(socket.getInputStream());
			
			out    = new DataOutputStream(socket.getOutputStream());

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

	public String[] getSplitData_canc( )
	{
		return SplitData_canc;
	}

	public String getErrMsg()
	{
		return ErrMsg;
	}

	public int getErrCd()
	{
		return ErrCd;
	}
}
