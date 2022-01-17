package com.mc.common.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.Vector;

public class Util {
	
	/**
     * 실수형 String을 정수로 
     * ex) 0.0 --> 0
     * @param params
     * @param name
     * @return
  */
  public static String removePrimeNumber(String avg){
		if(avg.indexOf(".") == -1)
			return avg;
			
		if(avg.split("\\.")[1].equals("0"))
			avg = avg.split("\\.")[0];
		
		return avg;
  }

	/**
	 * Property에 key에 맞는 값을 찾아서 반환하는 함수.
	 *
	 * @param key - property key
	 * @return key에 해당하는 값을 반환.
	 */
	public static String getProperty(String key){

		ResourceBundle bundle = null;
		String result = null;

		try{
			bundle = ResourceBundle.getBundle("properties.mc");
			return Util.toUTF8(bundle.getString(key));
		}catch(Exception e){
			e.toString();
		}
		return result;
	}

	/**
	 * Property에 key에 맞는 값을 찾아서 반환하는 함수.
	 *
	 * @param key - property key
	 * @return key에 해당하는 값을 반환.
	 */
	public static String getMsgProperty(String key){

		ResourceBundle bundle = null;
		String result = null;

		try{
			bundle = ResourceBundle.getBundle("msg");
			return Util.toUTF8(bundle.getString(key));
		}catch(Exception e){
			e.toString();
		}
		return result;
	}

	/**
	 * String 인코딩을 8859_1에서 KSC5601로 변경하는 함수.
	 *
	 * @param str - 8859_1 String Value
	 * @return KSC5601 String 반환.
	 *
	 * @exception UnsupportedEncodingException
	 */
	public static String toKor(String str){

		String result = "";

		try {
			if(str != null && !str.equals(""))
				result = new String(str.getBytes("8859_1"), "KSC5601");
			else
				result = str;

			return result;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * String 인코딩을 KSC5601에서 8859_1로 변경하는 함수.
	 *
	 * @param str - KSC5601 String Value
	 * @return 8859_1 String 반환.
	 *
	 * @exception UnsupportedEncodingException
	 */
	public static String toEn(String str){

		try {
			return new String(str.getBytes("KSC5601"), "8859_1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * String 인코딩을 KSC5601에서 8859_1로 변경하는 함수.
	 *
	 * @param str - 8859_1 String Value
	 * @return UTF-8 String 반환.
	 *
	 * @exception UnsupportedEncodingException
	 */
	public static String toUTF8(String str){

		try {
			return new String(str.getBytes("8859_1"), "UTF8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * String 인코딩을 KSC5601에서 8859_1로 변경하는 함수.
	 *
	 * @param str - 8859_1 String Value
	 * @return UTF-8 String 반환.
	 *
	 * @exception UnsupportedEncodingException
	 */
	public static String to8859(String str){

		try {
			return new String(str.getBytes("UTF8"), "8859_1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 파라메터 값을 검사하여 "\n" 문자를 "<br>"로 변경하여 반환하는 함수.
	 *
	 * @param str
	 * @return String
	 */
	public static String toBr(String str)
	{
		int len = str.length();
		int linenum = 0, i = 0;

		for(i=0; i<len; i++){
			if(str.charAt(i) == '\n'){
				linenum++;
			}
		}

		StringBuffer result = new StringBuffer(len + linenum * 3);

		for(i=0; i<len; i++){
			if(str.charAt(i) == '\n'){
				result.append("<br>");
			} else {
				result.append(str.charAt(i));
			}
		}
		return result.toString();
	}

	/**
	 * 파라메터 값을 검사하여 "\n" 문자를 "<br>"로 변경하여 반환하며 정해진 라인 값 만큼만 반환하는 함수.
	 *
	 * @param str - 문장
	 * @param line - 표시할 라인 값
	 * @return String
	 */
	public static String toBrLineCut(String str, int line)
	{
		int len = str.length();
		int linenum = 0, i = 0, lenCnt = 0;

		for(i=0; i<len; i++){

			lenCnt++;

			if(str.charAt(i) == '\n'){
				linenum++;
			}
			if(linenum == line){
				break;
			}
		}

		StringBuffer result = new StringBuffer(lenCnt + linenum * 3);

		for(i=0; i<lenCnt; i++){
			if(str.charAt(i) == '\n'){
				result.append("<br>");
			} else {
				result.append(str.charAt(i));
			}
		}
		return result.toString();
	}

	/**
	 * Html 특수문자를 ASCII문자로 변경하는 함수.
	 *
	 * @param str - 문장
	 * @return String
	 */
	public static String htmlspecialchars(String str) {

		str = replace(str, "&", "&amp;");
		str = replace(str, "\"", "&quot;");
		str = replace(str, "'", "&#039;");
		str = replace(str, "<", "&lt;");
		str = replace(str, ">", "&gt;");

		return str;
	}

	/**
	 * ASCII문자를 HTML형태로 변형하는 함수.
	 *
	 * @param str - 문장
	 * @return - String
	 */
	public static String reHtmlSpecialChars(String str) {

		str = replace(str, "&amp;", "&");
		str = replace(str, "&quot;", "\"");
		str = replace(str, "&#039;", "'");
		str = replace(str, "&lt;", "<");
		str = replace(str, "&gt;", ">");

		return str;

	}

	/**
	 * 기준이 되는 String 문장을 정해진 패턴으로 치환하는 함수.
	 *
	 * @param str - 문장
	 * @param pattern - 패턴
	 * @param replace - 치환 문자
	 * @return String
	 */
	public static String replace(String str, String pattern, String replace)
	{
		String resultStr = "";

		if(str != null && !str.equals("")){
			int s = 0;
			int e = 0;

			StringBuffer result = new StringBuffer();

			while((e = str.indexOf(pattern, s)) >= 0)
			{
				result.append(str.substring(s, e));
				result.append(replace);
				s = e+pattern.length();
			}
			resultStr = result.append(str.substring(s)).toString();
		}
		return resultStr;
	}

	/**
	 * 지정한 길이 보다 길경우 지정한 길이에서 자른후 "..."을 붙여 준다.
	 * 그보다 길지 않을때는 그냥 돌려준다. char 단위로 계산 (한글도 1자)
	 * @param str
	 * @param amount String 의 최대 길이 (이보다 길면 이 길이에서 자른다)
	 * @return String
	 */
	public static String crop(String str, int amount)
	{
		if (str==null) return "";
		String result = str;
		if(result.length()>amount) result=result.substring(0,amount)+"...";
		return result;
	}

	/**
	 * 지정한 길이 보다 길경우 지정한 길이에서 자른후 맨뒷부분에 지정한 문자열을 붙여 준다.
	 * 그보다 길지 않을때는 그냥 돌려준다. char 단위로 계산 (한글도 1자)
	 * @param str
	 * @param amount String 의 최대 길이 (이보다 길면 이 길이에서 자른다)
	 * @param trail amount 보다 str 이 길경우 amount 만큼만 자른다음 trail 을 붙여 준다.
	 * @return String
	 */
	public static String crop(String str, int amount, String trail)
	{
		if(str==null) return "";
		String result = str;
		if(result.length() > amount) result=result.substring(0, amount)+trail;
		return result;
	}

	/**
	 * 지정한 길이 보다 길경우 지정한 길이에서 자른후 맨뒷부분에 지정한 문자열을 붙여 준다.
	 * 그보다 길지 않을때는 그냥 돌려준다. Byte 단위로 계산 (한글 = 2자)
	 * @param str
	 * @param amount
	 * @param trail
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String cropByte(String str, int amount, String trail) throws UnsupportedEncodingException
	{
		if(str==null) return "";
		String tmp = str;
		int slen = 0, blen = 0;
		char c;
		if(tmp.getBytes("utf-8").length > amount){
			while(blen+1 < amount){
				c = tmp.charAt(slen);
				blen++;
				slen++;
				if(c > 127) blen++; //Two Byte character
			}
			tmp=tmp.substring(0,slen)+trail;
		}
		return tmp;
	}

    public static boolean getNewContentVal(String contentDate, int limitTime){

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHH");
        int currentDate = Integer.parseInt(dateFormat.format(new java.util.Date()));

        boolean result = false;

        if(currentDate < Integer.parseInt(contentDate) + limitTime){
            result = true;
        }

        return result;
    }

    public static String getDraftCurrentDate(){

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        return dateFormat.format(new java.util.Date());
    }

    public static String getDraftCurrentDate2Time(){

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return dateFormat.format(new java.util.Date());
    }

    public static String getCurrentDateHS(){

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

        return dateFormat.format(new java.util.Date());
    }

    public static String getCurrentDate(){

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

        return dateFormat.format(new java.util.Date());
    }

    public static String getToday(){

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

		return dateFormat.format(new java.util.Date());
	}

    public static long day2Day( String startDate, String endDate, String format) throws Exception
    {
		if (format == null)
			format = "yyyy/MM/dd HH:mm:ss.SSS";

		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date sDate;
		Date eDate;
		long day2day = 0;
		try {
			sDate = sdf.parse(startDate);
			eDate = sdf.parse(endDate);
			day2day = (eDate.getTime() - sDate.getTime()) / (1000*60*60*24);
		} catch(Exception e) {
			throw new Exception("wrong format string");
		}

		return day2day;
    }

    /**
	 * 현재 시간을 포맷된 형태로 돌려 준다.<BR>
	 * 포맷의 형태는 다음을 참조하여 포맷형태를 만들면 된다.
	 * <pre>
	 *  Symbol   Meaning                 Presentation        Example
	 *  ------   -------                 ------------        -------
	 *  G        era designator          (Text)              AD
	 *  y        year                    (Number)            1996
	 *  M        month in year           (Text & Number)     July & 07
	 *  d        day in month            (Number)            10
	 *  h        hour in am/pm (1~12)    (Number)            12
	 *  H        hour in day (0~23)      (Number)            0
	 *  m        minute in hour          (Number)            30
	 *  s        second in minute        (Number)            55
	 *  S        millisecond             (Number)            978
	 *  E        day in week             (Text)              Tuesday
	 *  D        day in year             (Number)            189
	 *  F        day of week in month    (Number)            2 (2nd Wed in July)
	 *  w        week in year            (Number)            27
	 *  W        week in month           (Number)            2
	 *  a        am/pm marker            (Text)              PM
	 *  k        hour in day (1~24)      (Number)            24
	 *  K        hour in am/pm (0~11)    (Number)            0
	 *  z        time zone               (Text)              Pacific Standard Time
	 *  '        escape for text         (Delimiter)
	 *  ''       single quote            (Literal)           '
	 * </pre>
	 * 예를 들어 "2001/10/05 21:30:23"를 만들고 싶으면 포맷 문자열을 "yyyy'/'MM'/'dd' 'HH':'mm':'ss"
	 * 와 같이 하면 된다.
	 *
	 * @param format 시간의 표현할 포맷 형식 문자열
	 * @return 포맷형식으로 포맷된 현재시간이 반환된다.
	 */
	public static String getTime(String format)
	{
		if(format == null || format.equals(""))
			return "";

		return getTime(new Date(), format);
	}

	/**
	 * 지정된 날짜를 포맷된 문자열로 반환한다.<BR>
	 *
	 * @param date java.util.Date의 객체로 지정한 날짜
	 * @param format 반환하고자 하는 문자열의 포맷
	 * @return 포맷형식으로 포맷된 시간이 반환된다.
	 */
	public static String getTime(java.util.Date date, String format)
	{
		if(date == null)
			return "";

		if(format == null || format.equals(""))
			format = "yyyy'년'MM'월'dd'일 'HH'시'mm'분'dd'초'";

		SimpleDateFormat formatter = new SimpleDateFormat(format);

		return formatter.format(date);
	}

	/**
	 * Oracle의 Date 타입 문자열을 ****년**월**일 **시**분**초 형태로 바꿔서 반환한다.<BR>
	 * Oracle의 Date 타입을 getString()으로 받았을 경우 ****-**-** **:**:**.*형태로 받아지는데
	 * 이것을 ****년**월**일 **시**분**초로 바꿔서 반환한 것이다.
	 *
	 * @param oraTime 오라클에서 Date형의 데이터를 String으로 가져왔을 때 문자열
	 * @return ****년**월**일 **시**분**초 형태의 문자열
	 */
	public static String getTimeFromOra(String oraTime)
	{
		if(oraTime == null || oraTime.equals(""))
			return "";

		return getTimeFromOra(oraTime, "yyyy'년'MM'월'dd'일'");
	}

	/**
	 * Oracle의 Date 타입 문자열을 ****년**월**일 **시**분**초 형태로 바꿔서 반환한다.<BR>
	 * Oracle의 Date 타입을 getString()으로 받았을 경우 ****-**-** **:**:**.*형태로 받아지는데
	 * 이것을 지정한 포맷 형태로 바꿔서 반환할 것이다.
	 *
	 * @param oraTime 오라클에서 Date형의 데이터를 String으로 가져왔을 때 문자열
	 * @param format 반환할 데이터의 포맷형태
	 * @return 지정된 포맷으로 변환된 문자열
	 */
	public static String getTimeFromOra(String oraTime, String format)
	{
		if(oraTime == null || oraTime.equals(""))
			return "";

		if(format == null || format.equals(""))
			format = "yyyy'년'MM'월'dd'일 'HH'시'mm'분'dd'초'";

		oraTime = oraTime.replace('-','/');
		oraTime = oraTime.substring(0,oraTime.lastIndexOf('.'));
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = formatter.parse(oraTime, (new ParsePosition(0)));
		formatter = null;

		formatter = new SimpleDateFormat(format);
		return formatter.format(date);
	}

	/**
	 * 지정된 날짜(java.util.Date)에 원하는 값만큼의 날짜를 증감시킨다.<BR>
	 * date에 java.util.Date 객체를 넣고 증감시킬 년수, 또는 개월수 또는 날수를 넣으면
	 * java.util.Date객체가 반환된다. 이때 특정한 값을 얻고 싶으면 현 클래스의
	 * getTime(java.util.Date date, String format) 메서드를 이용해서 받으면 된다.
	 *
	 * @param date 기준이 되는 날짜의 java.util.Date객체
	 * @param year 년도의 증감분 즉 2001년에서 2002년을 얻고 싶으면 year증감분에 1을 넣고
	 * 2000년을 얻고 싶으면 -1을 넣으면 된다.
	 * @param month 월의 증감분 12월일 경우 month에 1을 넣으면 자동으로 년도가 하나 증가하고,
	 * 월을 1월로 바뀔것이고, 1월일 경우 -1을 넣으면 자동으로 년도는 하나 감소하고, 12월로 바뀐다.
	 * @param day 날짜의 증감분 1일일경우 -1을 넣으면 월은 하나 감소하고 감소된 월의 마지막날로 바뀐다.
	 * @return 입력한 증감분으로 바뀐 java.util.Date객체가 반환된다.
	 */
	public static java.util.Date addDate(java.util.Date date, int year, int month, int day)
	{
		if(date == null)
			date = new java.util.Date();

		return addDate(date, year, month, day, 0, 0, 0);
	}

	/**
	 * 지정된 날짜(java.util.Date)에 원하는 값만큼의 날짜를 증감시킨다.<BR>
	 * date에 java.util.Date 객체를 넣고 증감시킬 년수, 개월수, 날수, 시간, 분, 초를 int type data로 넣으면
	 * java.util.Date객체가 반환된다. 이때 특정한 값을 얻고 싶으면 현 클래스의
	 * getTime(java.util.Date date, String format) 메서드를 이용해서 받으면 된다.
	 *
	 * @param date 기준이 되는 날짜의 java.util.Date객체
	 * @param year 년도의 증감분 즉 2001년에서 2002년을 얻고 싶으면 year증감분에 1을 넣고
	 * 2000년을 얻고 싶으면 -1을 넣으면 된다.
	 * @param month 월의 증감분 12월일 경우 month에 1을 넣으면 자동으로 년도가 하나 증가하고,
	 * 월을 1월로 바뀔것이고, 1월일 경우 -1을 넣으면 자동으로 년도는 하나 감소하고, 12월로 바뀐다.
	 * @param day 날짜의 증감분 1일일경우 -1을 넣으면 월은 하나 감소하고 감소된 월의 마지막날로 바뀐다.
	 * @param hour 시간 증감부
	 * @param minute 분의 증감분
	 * @param second 초의 증감분
	 * @return 입력한 증감분으로 바뀐 java.util.Date객체가 반환된다.
	 */
	public static java.util.Date addDate(java.util.Date date, int year, int month, int day, int hour, int minute, int second)
	{
		if(date == null)
			date = new java.util.Date();

		GregorianCalendar gc = new GregorianCalendar();
		gc.clear();
		gc.setTime(date);

		gc.add(Calendar.YEAR, year);
		gc.add(Calendar.MONTH, month);
		gc.add(Calendar.DATE, day);
		gc.add(Calendar.MONTH, month);
		gc.add(Calendar.HOUR, hour);
		gc.add(Calendar.MINUTE, minute);
		gc.add(Calendar.SECOND, second);

		return gc.getTime();
	}

	/**
	 * Exception 발생했을 경우 StackTrace를 통해 Exception의 모든 정보를 반환한다.
	 *
	 * @param e Throwable
	 * @return Exception messages
	 */
	public static String getStackTrace(Throwable e)
	{
		String result = null;
		try
		{
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			PrintWriter writer = new PrintWriter(bos);
			e.printStackTrace(writer);
			writer.flush();

			result = bos.toString();
			writer.close();
			bos.close();
		}
		catch(Exception ie)
		{
			result = "";
		}

		return result;
	}

	public static String toPrice(String str) {

		String result = "";

		if(str != null && !str.equals("")){
			//double val  = (double)Integer.parseInt(str);
			double val  = Long.parseLong(str);

			if(val==0.00)return str;

			DecimalFormat df = new DecimalFormat(",###.##");
			result = df.format(val);
		}
		return result;
	}

	public static String toIntPrice(int str) {

		String result = "";

		if(str > 0){

			//double val  = (double)str;
			double val  = Long.parseLong(String.valueOf(str));

			if(val==0.00)
				return Integer.toString(str);

			DecimalFormat df = new DecimalFormat(",###.##");

			result =  df.format(val);
		}
		return result;
	}

	public static String changeMagazineFreeSocid(String str){

		String val = "";

		for(int i = 0; i < (13 - str.length()); i++){
			val += "0";
		}

		val += str;

		return val;
	}

	public static String getDivisionZipcode(String str, String position){

		if(str.length() == 6){
			if(position.equals("f"))
				str = str.substring(0,3);

			if(position.equals("b"))
				str = str.substring(3);
		} else {
			str = "";
		}

		return str;
	}

	public static String isPopupFlag(String str){

		if(str == null || str.equals("") || str.equals("N"))
			str = "no";
		else
			str = "yes";

		return str;
	}

	public static String isZero(String str){

		if(str.equals("0"))
			str = "";

		return str;
	}

	public static String isNull(String str){

		if(str == null || str.equals(""))
			str = "";
		else
			str = Util.reHtmlSpecialChars(str.trim());

		return str;
	}

	public static String isNullZero(String str){

		if(str == null || str.equals(""))
			str = "0";
		else
			str = Util.reHtmlSpecialChars(str.trim());

		return str;
	}

	public static String isNullEmpty(String str){

		if(str == null || str.equals(""))
			str = "&nbsp;";
		else
			str = str.trim();

		return str;
	}

	public static int isNullReturnInt(String str){

		int result = 0;

		if(str != null && !str.equals("")){
			result = Integer.parseInt(str);
		}

		return result;
	}

	public static int isNullReturnInt(String str, int defaultInt){

		int result = defaultInt;

		if(str != null && !str.equals("")){
			result = Integer.parseInt(str);
		}

		return result;
	}

	public static String convertSingleNumber(int num){
		String result = "";
		if(num < 10)
			result = "0"+Integer.toString(num);
		else
			result = Integer.toString(num);
		return result;
	}

	public static String getFill2Num(int num){
		return new DecimalFormat("00").format(num);
	}

	public static String getFill2StrNum(String num){
		return new DecimalFormat("00").format(Integer.parseInt(num));
	}

	public static String getFill3Num(int num){
		return new DecimalFormat("000").format(num);
	}

	public static String getFill3StrNum(String num){
		return new DecimalFormat("000").format(Integer.parseInt(num));
	}

	public static String getFill4Num(int num){
		return new DecimalFormat("0000").format(num);
	}

	public static String getFill4StrNum(String num){
		return new DecimalFormat("0000").format(Integer.parseInt(num));
	}

	public static String getFill5Num(int num){
		return new DecimalFormat("00000").format(num);
	}

	public static String getFill5StrNum(String num){
		return new DecimalFormat("00000").format(Integer.parseInt(num));
	}

	public static String getDateFormat(String str){
		String result = "";

		if(str != null && !str.equals("") && str.length() == 8){
			result = str.substring(0,4) + "." + str.substring(4,6) + "." + str.substring(6);
		}
		return result;
	}

	public static String getDateFormat(String str, String pattern){
		String result = "";

		if(str != null && !str.equals("") && str.length() == 8){
			if(pattern.equals(""))
				result = str.substring(0,4) + "년" + str.substring(4,6) + "월" + str.substring(6) + "일";
			else
				result = str.substring(0,4) + pattern + str.substring(4,6) + pattern + str.substring(6);
		}
		return result;
	}
	  /**
	   *구분자(token)이 들어있는 문자열을 구분자 별로 문자열 배열로 리턴<BR>
	   *예) Vector tempVector = Tokenizer( isession, "~" );	<BR>
	   * @param    str           String 구분자가 들어있는 문자열
	   * @param    tkn           String 구분자 문자열
	   * @return   tempVector    Vector 구분자를 기준으로 잘린 문자열들
	   *
	   */
	  public Vector Tokenizer(String str, String tkn) {
	    Vector tempVector = new Vector();
	    StringTokenizer token = new StringTokenizer(str, tkn);

	    while (token.hasMoreTokens()) {
	      tempVector.addElement(token.nextToken());
	    }

	    return tempVector;
	  }

	  public static String[] getTokenizer( String str, String tkn) {
	        int i = 0;
	        String t_str[] = null;
	        StringTokenizer token = new StringTokenizer(str, tkn);
	        Vector vec = new Vector();

	        while (token.hasMoreTokens()) {
	           vec.addElement( token.nextToken() );
	        }

	        return (String[]) vec.toArray(new String[vec.size()]);
	  }

	  public static String[] getTokens( String str ,String gubun ) {
	        int iSearch = 0;
	        String stmp = null;
	        Vector vec = new Vector();
	        int itmp = iSearch ;
	        while( iSearch != -1 ){
	            itmp = iSearch ;
	            if( itmp != 0 ) itmp++;
	            iSearch = str.indexOf( gubun, itmp );
	            if( itmp <= iSearch){
	                stmp = str.substring( itmp, iSearch );
	                vec.addElement( stmp );
	            }
	        }
	        stmp = str.substring( itmp , str.length() );
	        vec.addElement( stmp );

	        return (String[]) vec.toArray(new String[vec.size()]);
	  }

	  public static int getLimitNum(String currentPage, String row){
		  if(currentPage != null && currentPage.equals("1"))
			  return 0;
		  else
			  return (Integer.parseInt(currentPage)-1)*Integer.parseInt(row);
	  }

	  public static String unescape(String inp)
	  {
		  String rtnStr = new String();
		  char [] arrInp = inp.toCharArray();
		  int i;
		  for(i=0;i<arrInp.length;i++) {
			  if(arrInp[i] == '%') {
				  String hex;
				  if(arrInp[i+1] == 'u') {    //유니코드.
					  hex = inp.substring(i+2, i+6);
					  i += 5;
				  } else {    //ascii
					  hex = inp.substring(i+1, i+3);
					  i += 2;
				  }
				  try
				  {
					  rtnStr += new String(Character.toChars(Integer.parseInt(hex, 16)));
				  } catch(NumberFormatException e) {
					  rtnStr += "%";
					  i -= (hex.length()>2 ? 5 : 2);
				  }
			  } else {
				  rtnStr += arrInp[i];
			  }
		  }
		  return rtnStr;
	  }
	
}
