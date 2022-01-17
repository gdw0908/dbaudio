package com.mc.web;


import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Clob;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import oracle.sql.CLOB;

import org.apache.commons.collections.map.ListOrderedMap;

public class MCMap extends ListOrderedMap{
	private static final long serialVersionUID = -2843771190064531473L;

	@Override
	public Object put(Object key, Object value) {
		if(value instanceof CLOB){
			CLOB clob = (CLOB) value;
			StringBuffer sb = new StringBuffer();
			String str = "";
			BufferedReader br = null;
			try {
				br = new BufferedReader(clob.getCharacterStream());
				while((str = br.readLine()) != null){
					sb.append(str + "\n");
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if(br != null)
					try {
						br.close();
					} catch (Exception e) {;}
			}
			
			value = sb.toString();
		}
		return super.put(key.toString().toLowerCase(), value != null? value.toString(): "");
	}
	
	@Override
	public Object get(Object key) {
		return super.get(key.toString().toLowerCase());
	}
	
	/**
	* String 객체중 Null 객체를 ""로 바꿔서 데이터를 저장한다.
	*
	* @param colLabel 라벨명
	* @param colValue 라벨값
	*/
	public void putStrNull(String colLabel, String colValue){
		colValue = (colValue != null) ? colValue.trim() : "";		
		
		super.put(colLabel, colValue);
	}

	/**
	* String객체중에서 데이터가 없는 Null객체를 &nbsp;로 바꿔서 데이터 저장한다.
	*
	* @param colLabel 라벨명
	* @param colValue 라벨값
	*/
	public void putForHtml(String colLabel, String colValue){
		colValue = (colValue != null) ? colValue.trim() : "&nbsp;";
		super.put(colLabel, colValue);
	}

	/**
	* int type의 숫자를 Integer Object type으로 바꿔서 데이터를 저장한다.
	*
	* @param colLabel 라벨명
	* @param colValue 라벨값
	*/
	public void putNumber(String colLabel, int colValue){
		super.put(colLabel, new Integer(colValue));
	}

	/**
	* long type의 숫자를 Long Object type으로 바꿔서 데이터를 저장한다.
	*
	* @param colLabel 라벨명
	* @param colValue 라벨값
	*/
	public void putNumber(String colLabel, long colValue){
		super.put(colLabel, new Long(colValue));
	}

	/**
	* float type의 숫자를 Float Object type으로 바꿔서 데이터를 저장한다.
	*
	* @param colLabel 라벨명
	* @param colValue 라벨값
	*/
	public void putNumber(String colLabel, float colValue){
		super.put(colLabel, new Float(colValue));
	}

	/**
	* double type의 숫자를 Double Object type으로 바꿔서 데이터를 저장한다.
	*
	* @param colLabel 라벨명
	* @param colValue 라벨값
	*/
	public void putNumber(String colLabel, double colValue){
		super.put(colLabel, new Double(colValue));
	}

	/**
	* int type의 숫자를 콤마를 넣어서 데이터를 String 형태로 바꿔서 저장한다.
	*
	* @param colLabel 라벨명
	* @param colValue 라벨값
	*/
	public void putFormattedNumber(String colLabel, int colValue){
		super.put(colLabel, getNumberFormat(colValue));
	}

	/**
	* long type의 숫자를 콤마를 넣어서 데이터를 String 형태로 바꿔서 저장한다.
	*
	* @param colLabel 라벨명
	* @param colValue 라벨값
	*/
	public void putFormattedNumber(String colLabel, long colValue){
		super.put(colLabel, getNumberFormat(colValue));
	}

	/**
	* float type의 숫자를 콤마를 넣어서 데이터를 String 형태로 바꿔서 저장한다.
	*
	* @param colLabel 라벨명
	* @param colValue 라벨값
	*/
	public void putFormattedNumber(String colLabel, float colValue){
		super.put(colLabel, getNumberFormat(colValue));
	}

	/**
	* double type의 숫자를 콤마를 넣어서 데이터를 String 형태로 바꿔서 저장한다.
	*
	* @param colLabel 라벨명
	* @param colValue 라벨값
	*/
	public void putFormattedNumber(String colLabel, double colValue){
		super.put(colLabel, getNumberFormat(colValue));
	}

  /**
  * Formatted String에서 숫자를를 제외하고 데이터를 저장한다.
  *
  * @param colLabel 라벨명
  * @param colValue 라벨값
  */
  public void putUnFormatedNumber(String colLabel, String colValue){
    if(colValue.length() < 1) super.put(colLabel, null);
    else super.put(colLabel, colValue.replaceAll("[^0-9]", ""));
  }
  
	/**
	* 숫자를 콤마를 넣어서 데이터를 String 형태로 바꿔서 저장한다.
	*
	* @param colLabel 라벨명
	*/
	public String getFormattedNumber(String colLabel){
		Object obj = super.get(colLabel);
		if(obj instanceof Integer)
			return getNumberFormat(((Integer)obj).intValue());
		else if(obj instanceof Long)
			return getNumberFormat(((Long)obj).longValue());
		else if(obj instanceof Float)
			return getNumberFormat(((Float)obj).floatValue());
		else if(obj instanceof Double)
			return getNumberFormat(((Double)obj).doubleValue());
		else
			return "&nbsp;";
	}


	/**
	* String타입으로 Casting해서 값을 Return한다 null ==>""
	*
	* @param key	Key값
	* @return 변환된 값
	*/
	public String getStrNull(String key){
		Object obj = super.get(key);
		if(obj == null) return "";
		else if(obj instanceof String) {
		  if("null".equals(obj)) return "";
		  else return ((String)obj).toString().trim();
		}
		else return obj.toString().trim();
	}
	
	/**
	* String타입으로 Casting해서 값을 Return한다 null ==>""
	*
	* @param key	Key값
	* @param nvl	null시값
	* @return 변환된 값
	*/
	public String getStrNullVal(String key, String nvl){
		Object obj = super.get(key);
		if(obj == null) return nvl;
		else if(obj instanceof String) {
		  if("null".equals(obj)) return nvl;
		  else return ((String)obj).toString().trim();
		}
		else return obj.toString().trim();
	}
	
	/**
	* String타입으로 Casting해서 값을 Return한다 null ==> "&nbsp;"
	*
	* @param key	Key값
	* @return 변환된 값
	*/
	public String getStr(String key){
		Object obj = super.get(key);
		if(obj == null) return "&nbsp;";
		else if(obj instanceof String){
			if("null".equals(obj) || obj.toString().trim().equals(""))
				return "&nbsp;";
			else
				return ((String)obj).toString().trim();
		}
		else return obj.toString().trim();
	}

	/**
	* numberKey에 해당하는 Data를 int로 바꾸어 리턴한다. 
	*
	* @param numberKey	numberKey값
	* @return 변환된 값
	*/
	public int getIntNumber(String numberKey){
		Object obj = super.get(numberKey);
		
		if(obj == null || obj.equals("") || obj.equals(null)){
			return 0;
		}else{
			return (Integer.parseInt(super.get(numberKey).toString()));
		}
	} 

	/**
	* numberKey에 해당하는 Data를 int로 바꾸어 리턴한다. 
	* 값이없으면 nvl을 리턴
	* @param numberKey	numberKey값
	* @param nvl을		널일시 값
	* @return 변환된 값
	*/
	public int getIntNullVal(String numberKey, int nvl){
		Object obj = super.get(numberKey);
		
		if(obj == null || obj.equals("") || obj.equals(null)){
			return nvl;
		}else{
			return (Integer.parseInt(super.get(numberKey).toString()));
		}
	}

	/**
	* numberKey에 해당하는 Data를 long로 바꾸어 리턴한다. 
	*
	* @param numberKey	numberKey값
	* @return 변환된 값
	*/
	public long getLongNumber(String numberKey){
		Object obj = super.get(numberKey);

		if(obj == null){
			return 0L;
		}else{
			return (Long.parseLong(super.get(numberKey).toString()));
		}
	}

	/**
	* numberKey에 해당하는 Data를 float로 바꾸어 리턴한다. 
	*
	* @param numberKey	numberKey값
	* @return 변환된 값
	*/
	public float getFloatNumber(String numberKey){
		Object obj = super.get(numberKey);
		if(obj == null){
			return 0.0F;
		}else{
			if(obj instanceof Float){
				return (Float.parseFloat(super.get(numberKey).toString()));
			}else{
				return 0.0f;
			}
		}
	}

    /**
     * numberKey에 해당하는 Data를 double로 바꾸어 리턴한다.
     * 
     * @param numberKey
     *            numberKey값
     * @return 변환된 값
     */
    public double getDoubleNumber(String numberKey) {
        Object obj = super.get(numberKey);
        if (obj == null) {
            return 0.0;
        } else {
            return (Double.parseDouble(super.get(numberKey).toString()));
        }
    }

    /**
     * 
     * @param numberKey numberKey값
     * @param DecimalsCount 소수점 자릿수 지정
     * @return	:	double
     * @author	:	이창기
     */
    public double getDoubleNumber(String numberKey, int DecimalsCount) {
        Object obj = super.get(numberKey);
        if (obj == null) {
            return 0.0;
        } else {
            String C = "#.#";
            for(int i = 1 ; i < DecimalsCount ; i++){
                C += "#";                
            }
            DecimalFormat txtFormat = new DecimalFormat(C);
            return Double.parseDouble(txtFormat.format(getDoubleNumber(numberKey))); 
        }
    }
    	
	/**
	* int숫자값을 콤마를 첨가한다.
	*
	* @param intNum	값
	* @return 변환된 값
	*/
	public String getNumberFormat(int intNum){
		DecimalFormat currency = new DecimalFormat("###,###,##0");
		return (intNum == 0) ? "0" : currency.format(intNum);
	}

	/**
	*  long숫자값을 콤마를 첨가한다.
	*
	* @param longNum	값
	* @return 변환된 값
	*/
	public String getNumberFormat(long longNum){
		DecimalFormat currency = new DecimalFormat("###,###,##0");
		return (longNum == 0L) ? "0" : currency.format(longNum);
	}

	/**
	*  float숫자값을 콤마를 첨가한다.
	*
	* @param floatNum	값
	* @return 변환된 값
	*/
	public String getNumberFormat(float floatNum){
		DecimalFormat currency = new DecimalFormat("###,###,##0.0#");
		return (floatNum == 0.0F) ? "0" : currency.format(floatNum);
	}

	/**
	*  double숫자값을 콤마를 첨가한다.
	*
	* @param doubleNum	값
	* @return 변환된 값
	*/
	public String getNumberFormat(double doubleNum){
		DecimalFormat currency = new DecimalFormat("###,###,##0.0#");
		return (doubleNum == 0.0D) ? "0" : currency.format(doubleNum);
	}	
	
	/**
	 * 문자열 날자를 지정 형태로 변환 반환한다.
	 * @param dateStr String 날짜
	 * @param format 지정형태
	 * @return String
	 */
	public String getDateType(String dateStr , String format){
		Date date = (Date)super.get(dateStr);
		if(date == null || date.equals("")){
			return "";
		}else{
			SimpleDateFormat fm = new SimpleDateFormat(format);
			return fm.format(date);
		}
	}
	
	 /**
	  * 문자열 데이트 형태를 지정형태로 변환 반환한다.
	  * @param dateStr key
	  * @param format DB에 들어가있는 날짜의형태
	  * @param nformat 변환할 날짜 형태
	  * @return
	  */
	 public String getDateFormat(String key , String beforeFormat , String convertFormat){	
		 String date = (String)super.get(key);
		 Date enterDate = null;
		 if(date == ""){
			 return "";
		 }else{					 
			 try {
				 enterDate = new SimpleDateFormat(beforeFormat).parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			 SimpleDateFormat sdf = new SimpleDateFormat(convertFormat);
			 String returnDate = sdf.format(enterDate);
			 return returnDate;
		 }		 
	 }

	public Date getDate(String key, String format) {
		Date rst = null;
		String str = (String)super.get(key);
		try {
			rst = new SimpleDateFormat(format).parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return rst;
	}

	/**
	 * CLOB타입문자열을 가져온다
	 * @param key
	 * @return
	 */
	public String getClob(String key) {
		StringBuffer data = new StringBuffer();
		Object obj = super.get(key);
		if (obj == null)
			return "";
		else {
			try {
				String str = "";
				BufferedReader br = new BufferedReader(((Clob) obj).getCharacterStream());
				while ((str = br.readLine()) != null) {
					data.append(str);					
				}
			} catch (IOException e) {
				System.out.println(e);
			} catch (SQLException ex) {
				System.out.println(ex);
			}
		}
		return data.toString();
	}
}
