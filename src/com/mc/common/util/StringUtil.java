package com.mc.common.util;

/*****************************************************************************
 * 프로그램명  : _StringUtil.java
 * 설명 : util
 *****************************************************************************
 * Date       	Author  Version Description
 * ---------- 	------- ------- -----------------------------------------------
 * 2009.03.05	nj		1.0		최초 confirm 받아 수정
 *****************************************************************************/

import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;

import javax.swing.text.Document;
import javax.swing.text.EditorKit;
import javax.swing.text.html.HTMLEditorKit;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

/**
 * 1. class 명   : StringUtil.java<br>
 * 2. class 설명 : 문자열 관련 UTIL함수들의 모음. 모든 메소드는 static함수로 구현되어 있다.<br>
 * 3. 최초 작성일 : 2006.03.20<br>
 * 수정일
 */
public class StringUtil {

	/**
	 * str에서 searchStr를 검색하여 있으면 true반환
	 * @param str
	 * @param searchStr
	 * @return
	 */
	public static boolean contains( String str, String searchStr ){
		return StringUtils.contains( str, searchStr);
	}
	
	/**
     * 파라미터 null 체크
     * @param params
     * @param name
     * @return
     */
    public static boolean isEmptyByParam(Map<String,String> params, String name){
    	
    	if(params.containsKey(name)==false){
    		return true;
    	}
    	
    	if(isEmptyByParam(params.get(name))){
    		return true;
    	}
    	
    	return false;
    }
	
	/**
	 * 파라미터 null 체크(빈값도 포함)
	 * @param parameter
	 * @return
	 */
	public static boolean isEmptyByParam(String parameter){
		if(parameter==null || "".equals(parameter) || "null".equals(parameter)){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * str에서 배열중에 str에 포함되는게 있으면 true반환
	 * ex) '/AAA/test.do'     <   [/test.do],[/test1.do]     이상태는 true 반환
	 * ex) '/AAA/test.do'     <   [/test1.do],[/test2.do]     이상태는 false 반환
	 * @param str
	 * @param searchStr
	 * @return
	 */
	public static boolean containsInStr( String str, String[] searchStrs ){
		
		for( int i=0; i< searchStrs.length; i++ ) {
			
			if( StringUtil.contains( str, searchStrs[i] ) ){
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * searchStrs 을 seperator 로 구분지어 배열로 만든후 각각 str에서 찾아 있으면 true반환
	 * ex) '/AAA/test.do'     <   [/test.do],[/test1.do]     이상태는 true 반환
	 * ex) '/AAA/test.do'     <   [/test1.do],[/test2.do]     이상태는 false 반환
	 * @param str
	 * @param searchStr
	 * @return
	 */
	public static boolean containsInStr( String str, String searchStr, String seperator ){
		
		String[] searchStrs = StringUtils.split( searchStr, seperator );
		
		for( int i=0; i< searchStrs.length; i++ ) {
			
			if( StringUtils.contains( str, searchStrs[i] ) ){
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * 숫자금액을 한글로 표시(금일천일백일십일원정) jth8172 200601
	 */
	
	 public static String stramt(String amt)
	 {
	  String tmpamt ="";
	  if( StringUtil.isEmpty( amt) ) return "";
	  if( StringUtils.equals(amt, "0" ) ) return "";
	  
	  amt = "000000000000" + amt.replaceAll(",", "");
	  int j=0;
	  for(int i=amt.length(); i>0; i--) {
	   j++;
	   	String aaa = amt.substring(i-1,i);
	   if (!aaa.equals("0")) {
	    if (j%4==2) tmpamt ="십"+tmpamt;
	    if (j%4==3) tmpamt ="백"+tmpamt;
	    if (j>1 && j%4==0) tmpamt ="천"+tmpamt;
	   }
	   	String bbb = amt.substring(amt.length()-8,amt.length()-4);
	    if (j==5 && Integer.parseInt(bbb)>0) tmpamt ="만"+tmpamt;
	    String ccc = amt.substring(amt.length()-12,amt.length()-8);
	    if (j==9 && Integer.parseInt(ccc)>0) tmpamt ="억"+tmpamt;
	    String ddd = amt.substring(amt.length()-16,amt.length()-12);
	    if (j==13 && Integer.parseInt(ddd)>0) tmpamt ="조"+tmpamt;
	   if (aaa.equals("1")) tmpamt ="일"+tmpamt;
	   if (aaa.equals("2")) tmpamt ="이"+tmpamt;
	   if (aaa.equals("3")) tmpamt ="삼"+tmpamt;
	   if (aaa.equals("4")) tmpamt ="사"+tmpamt;
	   if (aaa.equals("5")) tmpamt ="오"+tmpamt;
	   if (aaa.equals("6")) tmpamt ="육"+tmpamt;
	   if (aaa.equals("7")) tmpamt ="칠"+tmpamt;
	   if (aaa.equals("8")) tmpamt ="팔"+tmpamt;
	   if (aaa.equals("9")) tmpamt ="구"+tmpamt;
	  }
	  
	  // tmpamt = "금 " + tmpamt + "원 정";
	  return tmpamt;
	 }

	
//    /**
//     * int형으로 변환한다.
//     * @param sStr String
//     * @return int
//     */
//    public static int parseInt(String sStr, int iDefault) {
//        int iResult = iDefault;
//
//        try {
//            if (!isEmpty(sStr)) {
//                iResult = Integer.parseInt(sStr);
//            }
//        } catch (NumberFormatException ex) {}
//
//        return iResult;
//    }

//    /**
//     * Long형으로 변환한다.
//     * @param sStr String
//     * @return long
//     */
//    public static long parseLong(String sStr, int nDefault) {
//        long nResult = nDefault;
//
//        try {
//            if (!isEmpty(sStr)) {
//                nResult = Long.parseLong(sStr);
//            }
//        } catch (NumberFormatException ex) {}
//
//        return nResult;
//    }

//    /**
//     * Float형으로 변환한다.
//     * @param sStr String
//     * @param fDefault float
//     * @return float
//     */
//    public static float parseFloat(String sStr, float fDefault) {
//        float fResult = fDefault;
//
//        try {
//            if (!isEmpty(sStr)) {
//                fResult = Float.parseFloat(sStr);
//            }
//        } catch (NumberFormatException ex) {}
//
//        return fResult;
//    }
	
	/**
	 * 천단위 기호를 붙여서 리턴 1231231 이면   1,231,231 로 리턴
	 */
	public static String getThousand( String strNumber, String rtnChar ) throws Exception{
		
		if( StringUtil.isEmpty( strNumber ) ) return "";
		
		//혹시 모를  ,제거 
		strNumber = strNumber.replace(",", "");
		
		//숫자형태로만  이루어져있으지 않으면  그냥 return;
		if( !StringUtils.isNumeric( strNumber )) return strNumber;
		
		if( Long.parseLong( strNumber ) == 0 ) return rtnChar;
		
		return new DecimalFormat("#,###").format( Long.parseLong( strNumber ) );
	}
	
	/**
	 * 천단위 기호를 붙여서 리턴 1231231 이면   1,231,231 로 리턴
	 */
	public static String getThousand( String strNumber ) throws Exception{
		
		if( StringUtil.isEmpty( strNumber ) ) return "";
		
		//혹시 모를  ,제거 
		strNumber = strNumber.replace(",", "");
		
		//숫자형태로만  이루어져있으지 않으면  그냥 return;
		if( !StringUtils.isNumeric( strNumber )) return strNumber;
		
		return new DecimalFormat("#,###").format( Long.parseLong( strNumber ) );
	}
	 
	/**
	 * 천단위 기호를 붙여서 리턴 1231231 이면   1,231,231 로 리턴
	 */
	public static String getThousand( int intNumber, String rtnChar ) throws Exception{
		
		String strNumber = Integer.toString( intNumber );
		
		if( StringUtil.isEmpty( strNumber ) ) return "";
		
		//혹시 모를  ,제거 
		//	strNumber = strNumber.replace(",", "");
		
		//숫자형태로만  이루어져있으지 않으면  그냥 return;
		//if( !StringUtils.isNumeric( strNumber )) return strNumber;
		
		if( Long.parseLong( strNumber ) == 0 ) return rtnChar;
		
		return new DecimalFormat("#,###").format( Long.parseLong( strNumber ) );
	}
	
	/**
	 * 천단위 기호를 붙여서 리턴 1231231 이면   1,231,231 로 리턴
	 */
	public static String getThousand( int intNumber ) throws Exception{
		
		String strNumber = Integer.toString( intNumber );
		
		if( StringUtil.isEmpty( strNumber ) ) return "";
		
		//혹시 모를  ,제거 
	//	strNumber = strNumber.replace(",", "");
		
		//숫자형태로만  이루어져있으지 않으면  그냥 return;
		//if( !StringUtils.isNumeric( strNumber )) return strNumber;
		
		return new DecimalFormat("#,###").format( Long.parseLong( strNumber ) );
	}
	
	/**
	 * 4자리의 1900 형식을 separator 를 붙여 19:00 형식으로 가져옴
	 * @param time 4자리의 시간형식
	 * @param separator 한자리 문자
	 * @return
	 * @throws Exception
	 */
	public static String getTimeAppendSeparator( String time, String separator ) throws Exception{
		if( time == null ) return "";
		if( time.equals("") ) return "";
		if( time.length() < 4 ) return "";
		
		return left(time, 2) + separator + right(time, 2);
	}
	
	/**
	 * 8자리의 20090101 형식을  separator 를 붙여 2009-01-01 형식으로 가져옴
	 * @param date 8자리의 날짜형식
	 * @param separator 한자리 문자
	 * @return
	 * @throws Exception
	 */
	public static String getDateAppendSeparator( String date, String separator ) throws Exception{
    	if( date == null ) return "";
		if( date.equals("") ) return "";
    	if( date.length() < 8 ) return "";
    	
    	date = left(date, 8);
    	
    	return left(date, 4) + separator + mid( date, 5, 2) + separator + right(date, 2);
    }
	
	/**
	 * 8자리의 20090101 형식을  separator 를 붙여 2009-01-01 형식으로 가져옴
	 * @param date 8자리의 날짜형식
	 * @param separator 한자리 문자
	 * @return
	 * @throws Exception
	 */
	public static String getDateAppendSeparator( String date, String separator, String emptryStr ) throws Exception{
		if( date == null ) return emptryStr;
		if( date.equals("") ) return emptryStr;
		if( date.length() < 8 ) return emptryStr;
		
		date = left(date, 8);
		
		return left(date, 4) + separator + mid( date, 5, 2) + separator + right(date, 2);
	}
	

	/**
	 * sStr1과 sStr2를 비교하여 같으면 rtnStr 반환, 다르면 sStr1반환
	 * @param sStr1 비교문자열1
	 * @param sStr2 비교문자열2
	 * @param rtnStr 반환문자열
	 * @return
	 */
    public static String compareString(String sStr1, String sStr2, String rtnStr ) {
    	if( StringUtils.isBlank(sStr1)) return"";
    	if( StringUtils.isBlank(sStr2)) return"";
    	
    	if( StringUtils.equals(sStr1, sStr2) ){
    		return rtnStr;
    	}else{
    		return sStr1;
    	}
    }
	
	/**
     * 해당 문자열에 값이 있는지 확인한다.
     * @param str 문자열
     * @return
     */
    public static boolean isEmpty( String sStr ) {

        return StringUtils.isBlank( sStr) ;
    }

    /**
     * 해당 문자열이 null인경우 빈문자열("")을 반환한다.
     * @param sStr String
     * @return String
     */
    public static String nvl(String sStr) {
    	
    	if( StringUtils.isBlank( sStr) ){
    		return "";
    	} 
    	
        return StringUtils.defaultIfEmpty( sStr, "" );
    }
 
    /**
     * 해당 문자열이 빈문자열("")인경우 대체문자열을 반환한다.
     * @param sStr String
     * @param sSomething String
     * @return String
     */
    public static String nvl(String sStr, String sSomething) {
    	
    	if( StringUtils.isBlank( sStr) ){
    		return sSomething;
    	}
    	
        return StringUtils.defaultIfEmpty( sStr, sSomething );
    }
    
    /**
     * 해당 문자열이 빈문자열("")인경우 대체문자열을 반환하고 아니면 문자열을 붙인다.
     * @param sStr String
     * @param sSomething String
     * @return String
     */
    public static String nvl(String sStr, String aStr, String sSomething) {
    	
    	if( StringUtils.isBlank( sStr) ){
    		return sSomething;
    	}
    	
        return sStr + aStr;
    }
    
    
    
    

//    public static String emptyToSomething(String sStr, String sSomething) {
//        if (isEmpty(sStr)) {
//            sStr = sSomething;
//        }
//
//        return sStr;
//    }

    /**
     * 해당 문자열이 null일 경우 대체 문자열을 반환한다.
     * @param source String 검사할 문자열
     * @param replace String 대체 문자열
     * @return String 결과 문자열
     */
    public static String ifNull(String source, String replace) {
        if( StringUtils.isBlank( source ) )
            return source;
        else
            return replace;
    }

    public static String isNullDef(String sStr, String sDefault) {
        if( sStr != null && !"".equals(sStr) )
            return sStr;
        else
            return sDefault;
    }
//    public static String isNull(String source, String replace) {
//        if(source != null)
//            return source;
//        else
//            return replace;
//    }
    
    
    /**
     * 긴문자 자르기
     * @param str
     * @param size
     * @param tail
     * @return
     */
	public static String cutString(String str, int size, String tail) {
		if (str == null)
			return "";
		int srcLen = str.getBytes().length;
		if (srcLen < size)
			return str;
		int i = 0;
		int realLen = 0;
		for (i = 0; realLen < size; i++) {
			char a = str.charAt(i);
			if ((a & 0xff00) == 0)
				realLen++;
			else
				realLen += 2;
		}
		return str.substring(0, i) + tail;

	}

	/**
	 * "..."를 포함한 사이즈로 문자열을 축소함. maxWidth 는 4보다 커야함.
	 * @param str
	 * @param maxWidth
	 * @return
	 */    
    public static String cutString(String str, int maxWidth) {
    	if( StringUtils.isBlank( str) ) return "";

        return StringUtils.abbreviate( str, maxWidth );
    }

    /**
     * 문자열을 왼쪽에서 해당 길이만큼 짤라서 반환한다.
     * @param strString 문자열
     * @param nLength 길이
     * @return
     */
    public static String left(String sStrString, int iLength) {
    	if( StringUtils.isBlank( sStrString) ) return "";
    	
        if( sStrString.length() < iLength) return "";

        return StringUtils.left( sStrString, iLength) ;
    }

    /**
     * 오라클의 lpad 와 동일한 기능
     * @param strString 문자열
     * @param nLength 길이
     * @return
     */
    public static String lpad(String sStrString, int iLength, String trans ) {
    	
    	if( sStrString.length() >= iLength ) return sStrString;
    	
    	
    //	if( StringUtils.isBlank( sStrString) ) return "";
    	
        return StringUtils.leftPad(sStrString, iLength, trans);
    }
    
    /**
     * 오라클의 lpad 와 동일한 기능 (일단 숫자형태를 문자형태로 변형함.  01=>"1",  10=>"10"   
     * @param strString 문자열
     * @param nLength 길이
     * @return
     */
    public static String lpad(int intString, int iLength, String trans ) {
    	
    	String sStrString = Integer.toString(intString);
    	
    	if( sStrString.length() >= iLength ) return sStrString;
    	
    	
    	if( StringUtils.isBlank( sStrString) ) return "";
    	
        return StringUtils.leftPad(sStrString, iLength, trans);
    }    
    
    
    /**
     * 오라클의 rpad 와 동일한 기능
     * @param strString 문자열
     * @param nLength 길이
     * @return
     */
    public static String rpad(String sStrString, int iLength, String trans ) {
    	if( sStrString.length() >= iLength ) return sStrString;
    	if( StringUtils.isBlank( sStrString) ) return "";
    	
        return StringUtils.rightPad(sStrString, iLength, trans);
    }
    
    /**
     * 오라클의 rpad나 lpad처럼 가운데를 기준으로 양쪽을 채움.
     */
    public static String cpad(String sStrString, int iLength, String trans ) {
    	if( sStrString.length() >= iLength ) return sStrString;
    	if( StringUtils.isBlank( sStrString) ) return "";
    	
        return StringUtils.center( sStrString, iLength, trans);
    }
    
    
    /**
     * 문자열을 오른쪽에서 해당 길이만큼 짤라서 반환한다.
     * @param strString 문자열
     * @param nLength 길이
     * @return
     */
    public static String right(String sStrString, int iLength) {
    	if( StringUtils.isBlank( sStrString) ) return "";
    	
        if( sStrString.length() < iLength) return "";

        return StringUtils.right( sStrString, iLength) ;
    	
    }

    /**
     * 문자열을 임의의 위치에서 해당 길이만큼 짤라서 반환한다.
     * @param strString 문자열
     * @param nStart 시작위치
     * @param nLength 길이
     * @return
     */
    public static String mid(String sStrString, int iStart, int iLength) {
    	if( StringUtils.isBlank( sStrString)) return "";
    	if( sStrString.length() < iStart ) return "";
    	if( sStrString.length() < iStart+iLength ) return "";
    	
        return StringUtils.mid( sStrString, iStart-1, iLength);
    }

    /**
     * 해당 문자열을 다른 문자열로 대체한다.
     * @param strString 문자열
     * @param strOld 대상 문자열
     * @param strNew 대체할 새로운 문자열
     * @return
     */
    public static String replace(String sStrString, String sStrOld, String sStrNew) {
    	if( StringUtils.isBlank( sStrString)) return "";
    	
        return StringUtils.replace( sStrString, sStrOld, sStrNew);
    }


    /**
     * 주민번호 형태로 잘라서 seperator를 넣는다.
     * @param strString 문자열
     * @param sSomething 대체문자
     * @param showFlag 뒷자리 * 여부  1:전체표시 2:*로표시 
     * @return
     */
    public static String jumin(String sStrString, String sSomething, int showFlag) {
    	
    	sStrString = replace( sStrString, "-", "" );
    	
    	if( StringUtils.isBlank( sStrString)) return sSomething;
    	if( sStrString.length() != 13 ) return sSomething;
    	
    	if( showFlag ==1 ){
    		return left(sStrString,6) + "-" + right(sStrString,7);
    	}else{
    		return left(sStrString,6) + "-*******";
    	}
    	
    }
    


    /**
     * List안의 각각 문자열 앞뒤를 '로 싼다음 , 로 구분된 String으로 변환
     * ex) 값이 a, b, c 인 3row의 List -> 'a','b','c'
     * List가 null 이면 ''을 리턴
     * @param list 변환시킬 List
     * @return
     */
    public static String listToStringWithAmp(List list) {
        StringBuffer sBuf = new StringBuffer();
        if(list != null && list.size()>0) {
           for(int i = 0 ; i < list.size() ; i++) {
               String str = (String)list.get(i);
               if(str != null && str.trim().length() > 0) {
                   sBuf.append("'" + str + "'");
                   //System.out.println("i:: " + i + ", list.size():: " + list.size());
                   if(i < list.size()-1)
                       sBuf.append(",");
               }
           }
        } else {
            sBuf.append("''");
        }
        return sBuf.toString();
    }


    /**
     * 문자열을 구분자로 분리하여 배열에 담아 반환한다.
     * @param sString 문자열
     * @param sDelim 구분자
     * @return String[] 배열
     */
    public static String[] split(String sString, String sDelim) {

        if (isEmpty(sString)) {
            return null;
        }

        StringTokenizer stringtokenizer = new StringTokenizer(sString, sDelim);
        int iTokenCount = stringtokenizer.countTokens();

        if (iTokenCount <= 0) {
            return null;
        }

        String sResults[] = new String[iTokenCount];
        int i = 0;

        while (stringtokenizer.hasMoreTokens()) {
            sResults[i++] = stringtokenizer.nextToken();
        }

        return sResults;
    }

    /**
     * '을 ''로 바꾼다.
     * @param str 문자열
     * @return
     */
    public static String escapeSql(String sStr) {
    	if( StringUtils.isBlank( sStr)) return "";
        return StringEscapeUtils.escapeSql( sStr );
    }

    /**
     * ''을 '로 바꾼다.
     * @param str 문자열
     * @return
     */
    public static String unescapeSql(String sStr) {
        return replace(sStr, "''", "'");
    }

    /**
     * str포맷을 html으로 바꾼다. <는 &lt;, >는 &gt; 등으로 변환시킴.
     * "을 &quot;로 바꾼다.
     * @param str 문자열
     * @return
     */
    public static String DQ2Amp(String sStr) {
    	if( StringUtils.isBlank( sStr)) return "";
    	
    	return StringEscapeUtils.escapeHtml( sStr );
    }

    /**
     * \r\n을 BR>로 바꾼다.
     * @param str 문자열
     * @return
     */
    public static String nl2br(String sStr) {
        if (sStr == null) return "&nbsp;";
        
        if( sStr.equals("") )  return "&nbsp;";
        
        String sDesc = null;
        sDesc = "";
        if (sStr.length() != 0) {
        	sDesc = replace(sStr, "\r\n", "<BR>");
        }

        return sDesc;
    }
    
    /**
     * \r\n을  \\r\\n 로 바꾼다.
     * @param str 문자열
     * @return
     */
    
    public static String nl2nl(String sStr) {
    	String sDesc = null;
    	if (sStr == null)
    		return null;
    	
    	sDesc = "";
    	if (sStr.length() != 0) {
    		sDesc = replace(sStr, "\r\n", "\\r\\n");
    	}
    	
    	return sDesc;
    }

    /**
     * int를 String으로 바꾼다. 한자리 숫자이면 앞에 "0"을 붙인다.
     * @param number 숫자 문자열
     * @return
     */
    public static String toStringNumber(int iNumber) {
    	
    	if ( iNumber < 1 ) return  Integer.toString(iNumber);
    	
    	return lpad( Integer.toString(iNumber), 2, "0");
    	
    }

    /**
     * 한자리 숫자이면 앞에 "0"을 붙인다.
     * @param number 숫자 문자열
     * @return
     */
    public static String toStringNumber(String sNumber) {

    	if( StringUtils.isBlank( sNumber)) return "0";
    	
    	return lpad(sNumber, 2, "0");
    	
    }

    /**
     * KSC5601 인코딩을 8859_1로 변환한다.
     * @param sStr
     * @return
     */
    public static String KoreanToEnglish(String sStr) {
        String sResult = "";

        try {
            if (!isEmpty(sStr)) {
                sResult = new String(sStr.getBytes("KSC5601"), "8859_1");
            }
        } catch (UnsupportedEncodingException ex) {
            sResult = sStr;
        }
 
        return sResult;
    }


    /**
     * 8859_1 인코딩을 KSC5601으로 변환한다.
     * @param sStr
     * @return
     */
    public static String EnglishToKorean(String sStr) {
        String sResult = "";

        try {
            if (!isEmpty(sStr)) {
                sResult = new String(sStr.getBytes("8859_1"), "KSC5601");
            }
        } catch (UnsupportedEncodingException ex) {
            sResult = sStr;
        }

        return sResult;
    }
    
    public static String ascToKsc(String value) {
		try {
			String strRet = new String(value.getBytes("8859_1"), "EUC-KR");
			return strRet;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

    /**
     * 두 문자열을 비교하여 같으면 " selected " 를 반환한다.
     * @param sTarget1 String
     * @param sTarget2 String
     * @return String
     */
    public static String printSelected(String sTarget1, String sTarget2) {
    	if( StringUtils.isBlank( sTarget1)) return "";
    	if( StringUtils.isBlank( sTarget2)) return "";

        return StringUtils.equals( sTarget1, sTarget2)? " selected " : "";
        
    }

    
    /**
     * 두 문자열을 비교하여 같으면 " checked"를 반환한다.
     * @param sTarget1 String
     * @param sTarget2 String
     * @return String
     */
    public static String printChecked(String sTarget1, String sTarget2) {
    	if( StringUtils.isBlank( sTarget1)) return "";
    	if( StringUtils.isBlank( sTarget2)) return "";

        if (sTarget1 == null) sTarget1 = "";

        return StringUtils.equals( sTarget1, sTarget2)? " checked " : "";

    }


    /**
     * 스트링배열안에 해당 문자열이 존재하는지 여부
     * @param strArray String[] 문자열 배열
     * @param str String 찾고자 하는 문자열
     * @return boolean 문자열 존재 여부 존재하면 true,없으면 false
     */
    public static boolean indexOfAny(String str, String[] strArray){
    	if( StringUtils.isBlank( str)) return false;
    	if ( strArray == null ) return false;
    	
    	return StringUtils.indexOfAny(str, strArray) > -1 ? true : false;  
    	
    }
//    public static boolean isStrInArray(String str,String[] strArray){
//    	boolean success = false;
//    	try {
//    		for (int i = 0 ; i < strArray.length ; i++){
//    			if (strArray[i].equals(str)) {
//    				success = true;
//    				break;
//    			}
//    		}
//    	} catch(Exception ex){
//    		System.out.print("isStrInArray 함수 실행중 Exception 발생" + ex);
//    	}
//    	return success;
//    }

    /**
    * single quot (') 제거
    * @param String
    * @return String
    */
    public static String removeQuot (String strVal)
    {
        int quoterIndex = 0;

        if (strVal == null)
        {
            //System.out.println("removeQuot에서 strVal == null");
            return "";
        }

        for (int i = 0; ((quoterIndex = strVal.indexOf("'", quoterIndex)) != -1); i++)
        {
            strVal = strVal.substring(0, quoterIndex) + "''" + strVal.substring(quoterIndex + 1);
            quoterIndex = quoterIndex + 2;
        }

        return strVal;
    }

    /**
    * lt, gt (<>) 제거
    * @param String
    * @return String
    */
    public static String removeLtGt (String strVal)
    {
        int quoterIndex = 0;

        for (int i = 0; ((quoterIndex = strVal.indexOf("<", quoterIndex)) != -1); i++)
        {
            strVal = strVal.substring(0, quoterIndex) + "&lt;" + strVal.substring(quoterIndex + 1);
            quoterIndex = quoterIndex + 4;
        }

        quoterIndex = 0;

        for (int i = 0; ((quoterIndex = strVal.indexOf(">", quoterIndex)) != -1); i++)
        {
            strVal = strVal.substring(0, quoterIndex) + "&gt;" + strVal.substring(quoterIndex + 1);
            quoterIndex = quoterIndex + 4;
        }

        return strVal;
    }

    /**
    * Single quot, lt, gt ('<>) 제거
    * @param String
    * @return String
    */
    public static String removeSpecStr (String strVal)
    {
        return removeLtGt(removeQuot(strVal));
    }

    /**
    * Single quot, lt, gt ('<>) 제거하고 DB String 타입으로 변경
    * @param String
    * @return String
    */
    public static String strDB (String strVal)
    {
        return "'"+removeLtGt(removeQuot(strVal))+"'";
    }
    
    public static String printAuthScript(){
		String outStr = "";
		outStr += ("<script>");
		outStr += ("alert('해당페이지에 대한 권한이 없습니다.');");
		outStr += ("location.href = '/maritime/meeting_schedule.jsp';");
		outStr += ("</script>");
		return outStr;
	}
    
    public static int removeComma(String strVal){
    	strVal= StringUtils.defaultString(strVal);
    	
		if (StringUtils.isBlank(strVal))	strVal = "0";
		else strVal = strVal.replaceAll(",", "");
	
		return Integer.parseInt(strVal);	
		
    }
    
    public static boolean isBlank( String str ) {
		int strLen;
		if( str == null || (strLen = str.length()) == 0 ) {
			return true;
		}
		for( int i = 0; i < strLen; i++ ) {
			if( (Character.isWhitespace( str.charAt( i ) ) == false) ) {
				return false;
			}
		}
		return true;
	}
    
    public static boolean isChecked(String sArray, String sVal) 
    {
    	boolean retVal = false;
    	
		if(sArray != null && !"".equals(sArray))
		{
			String [] sArr = sArray.split(",");
		
			for( int i = 0; i < sArr.length ; i++ ) {
				if( sVal.equals(sArr[i]) ) {
					retVal = true;
					break;
				}
			}
		}
		return retVal;
	}
    public static boolean isChecked2(String sArray, String sVal) 
    {
    	boolean retVal = false;
    	
		if(sArray != null && !"".equals(sArray))
		{
			String [] sArr = sArray.split(";");
		
			for( int i = 0; i < sArr.length ; i++ ) {
				if( sVal.equals(sArr[i]) ) {
					retVal = true;
					break;
				}
			}
		}
		return retVal;
	}
    
	// 임의의 비밀번호 생성. 조건 - 길이
	public static String createPasword(int len) {
		String result = "";
		StringBuffer sb = new StringBuffer();
		String str = new String("QAa0bcLdUK2eHfJgTP8XhiFj61DOklNm9nBoI5pGqYVrs3CtSuMZvwWx4yE7zR");
		int te = 0;
		Random r = new Random();
		try {
			for (int i = 0; i < len; i++) {
				te = r.nextInt(62);
				sb.append(str.charAt(te));
			}
			result = sb.toString();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return result;

	}

	public static String getTextInHTML(String html) {
		String result = "";
		if (html == null)
			return result;

		EditorKit kit = new HTMLEditorKit();
		Document doc = kit.createDefaultDocument();
		Reader reader = new StringReader(html);
		try {
			kit.read(reader, doc, 0);
			result = doc.getText(0, doc.getLength());
		} catch (Exception e) {
			;
		}

		return result;
	}
	public static double ceil(double d) 
	{
		return Math.ceil(d);
	}
	public static void main(String[] args) {
		System.out.println(lpad("2", 2, "0"));
	}


	public static String requestReplace (String paramValue, String gubun) {
        String result = "";
        
        if (paramValue != null) {
        	
        	paramValue = paramValue.replaceAll("<", "&lt;").replaceAll(">", "&gt;");

        	paramValue = paramValue.replaceAll("\\*", "");
        	paramValue = paramValue.replaceAll("\\?", "");
        	paramValue = paramValue.replaceAll("\\[", "");
        	paramValue = paramValue.replaceAll("\\{", "");
        	paramValue = paramValue.replaceAll("\\(", "");
        	paramValue = paramValue.replaceAll("\\)", "");
        	paramValue = paramValue.replaceAll("\\^", "");
        	paramValue = paramValue.replaceAll("\\$", "");
        	paramValue = paramValue.replaceAll("'", "");
        	paramValue = paramValue.replaceAll("@", "");
        	paramValue = paramValue.replaceAll("%", "");
        	paramValue = paramValue.replaceAll(";", "");
        	paramValue = paramValue.replaceAll(":", "");
        	paramValue = paramValue.replaceAll("-", "");
        	paramValue = paramValue.replaceAll("#", "");
        	paramValue = paramValue.replaceAll("--", "");
        	paramValue = paramValue.replaceAll("-", "");
        	paramValue = paramValue.replaceAll(",", "");
        	
        	if(gubun != "encodeData"){
        		paramValue = paramValue.replaceAll("\\+", "");
        		paramValue = paramValue.replaceAll("/", "");
            paramValue = paramValue.replaceAll("=", "");
        	}
        	
        	result = paramValue;
            
        }
        return result;
  }
	
	public static String clearXSS(String str) {
    	String avatag = "div,span,b,strong,ul,li,ol,dl,dd,dt,table,th,tr,td,font,tbody,thead,tfoot,pre,em,p,br,img,a,u,iframe,embed,DIV,SPAN,B,STRONG,UL,LI,OL,DL,DD,DT,TABLE,TH,TR,TD,FONT,TBODY,THEAD,TFOOT,PRE,EM,P,BR,IMG,A,U,IFRAME,EMBED"; //허용할 확장자 리스트
		return clearXSS(str, avatag);
	}
	
	public static String clearXSS(String str, String avatag) {
		str = str.replaceAll("<", "&lt;");
		//str = str.replaceAll(" 0", " ");
		// 허용할 태그를 지정할 경우
		if (!avatag.equals("")) {
			avatag.replaceAll(" ", "");
			String[] st = avatag.split(",");
			// 허용할 태그를 존재 여부를 검사하여 원상태로 변환
			for (int x = 0; x < st.length; x++) {
				str = str.replaceAll("&lt;" + st[x] + " ", "<" + st[x] + " ");
				str = str.replaceAll("&lt;" + st[x] + "/>", "<" + st[x] + "/>");
				str = str.replaceAll("&lt;" + st[x] + ">", "<" + st[x] + ">");
				str = str.replaceAll("&lt;/" + st[x], "</" + st[x]);
			}
		}
		return str;
	}
}
