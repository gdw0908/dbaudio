<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="dtf" uri="/WEB-INF/tlds/DateUtil_fn.tld" %>
<%@ taglib prefix="suf" uri="/WEB-INF/tlds/StringUtil_fn.tld" %>
<%@ page import="com.mc.common.util.*" %>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>파츠모아 통합관리 시스템</title>
<link href="/lib/css/cmsbase.css" rel="stylesheet" type="text/css" />
<link href="/lib/css/cmsadmin.css" rel="stylesheet" type="text/css" />
<link href="/lib/css/redmond/jquery-ui-1.9.1.custom.min.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="/lib/js/jquery-1.9.1.js"></script>
<script type = "text/javascript">
function printWindow() {

	if (!factory.object) {
		return;
	} else {
		 document.getElementById("printButton").style.display = "none";
		 factory.printing.header = ""; //머릿말 설정
	     factory.printing.footer = ""; //꼬릿말 설정
	     factory.printing.portrait = true; //출력방향 설정: true-세로, false-가로
	     factory.printing.leftMargin = 1.0; //왼쪽 여백 설정
	     factory.printing.topMargin = 1.0; //위쪽 여백 설정
	     factory.printing.rightMargin = 1.0; //오른쪽 여백 설정
	     factory.printing.bottomMargin = 1.0; //아래쪽 여백 설정
	     // factory.printing.printBackground = true; //배경이미지 출력 설정:라이센스 필요
	     factory.printing.Print(false); //출력하기
	}
}
jQuery(document).ready(function(){
	alert("거래명세서 출력 기능은 Internet Explorer 에서만 가능 합니다.\n업무에 참고 해주시기 바랍니다.");
});

</script>
</head>
<body>
<object id=factory style="display:none" classid="clsid:1663ed61-23eb-11d2-b92f-008048fdd814" codebase="<%=Util.getProperty("home.url")%>/module/smsx.cab#Version=7.4.0.8 "></object>
<div id="main">

  <div class="container">
    <div class="contents">
      <table class="deal_style_1">
        <colgroup>
        <col width="5%">
        <col width="8%">
        <col width="10%">
        <col width="5%">
        <col width="8%">
        <col width="5%">
        <col width="8%">
        <col width="10%">
        <col width="5%">
        <col width="10%">
        </colgroup>
        <thead>
          <tr>
            <th colspan="7" class="title"> <p class="title_n"><span>거래명세서</span></p>
              <p class="t_ps"> <span class="deal_l">주문일자 : ${dtf:simpleDateFormat(specification.orderdate, 'yyyy-MM-dd HH:mm:ss' , 'yyyy-MM-dd') }</span> <span class="deal_r">주문번호 :  ${specification.orderno }</span> </p>
            </th>
            <th>기타</th>
            <th colspan="2">&nbsp;</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <th rowspan="4">공<br />
              급<br />
              자</th>
            <th>상호</th>
            <td>${specification.com_nm }</td>
            <th>성명</th>
            <td>${specification.ceo_nm }</td>
            <th rowspan="4">공<br />
              급<br />
              받<br />
              는<br />
              자</th>
            <th>상호</th>
            <td>${specification.busi_nm }</td>
            <th>성명</th>
            <td>${specification.order_nm }</td>
          </tr>
          <tr>
            <th>연락처</th>
            <td colspan="3">${specification.tel }</td>
            <th>연락처</th>
            <td colspan="3">${specification.rehp }</td>
          </tr>
          <tr>
            <th>주소</th>
            <td colspan="3">[${specification.com_zipcd }] ${specification.addr1 } ${specification.addr2 }</td>
            <th>주소</th>
            <td colspan="3">[${specification.re_zip_cd }] ${specification.re_addr1 } ${specification.re_addr2 }</td>
          </tr>
          <tr>
            <th>업태</th>
            <td>${specification.comptyp1 }</td>
            <th>종목</th>
            <td>${specification.comptyp2 }</td>
            <th>업태</th>
            <td>${specification.user_comptyp1 }</td>
            <th>종목</th>
            <td>${specification.user_comptyp2 }</td>
          </tr>
          <tr>
            <th>비고</th>
            <td colspan="9">&nbsp;</td>
          </tr>
        </tbody>
      </table>
      <table class="deal_style_2">
        <colgroup>
        <col width="8%" />
        <col width="26%" />
        <col width="32%" />
        <col width="13%" />
        <col width="8%" />
        <col width="13%" />
        </colgroup>
        <thead>
          <tr>
            <th rowspan="2">순번</th>
            <th>상품코드</th>
            <th rowspan="2">상품명</th>
            <th rowspan="2">위치</th>
            <th rowspan="2">수량</th>
            <th rowspan="2">금액</th>
          </tr>
          <tr>
            <th>ERP코드</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td rowspan="2" class="cen">1</td>
            <td>${specification.item_code }</td>
            <td rowspan="2">${specification.productnm }</td>
            <td rowspan="2">${specification.part_location }</td>
            <td rowspan="2" class="cen">${specification.qty }</td>
            <td rowspan="2" class="cen">${suf:getThousand(specification.user_price) }</td>
          </tr>
          <tr>
            <td>${specification.erp_code }</td>
          </tr>
        </tbody>
      </table>
	  <div class="deal_tbox">
        <div class="section">
          <h5>교환 및 반품안내</h5>
          <dl class="sec_3">
            <dt>1.교환안내</dt>
            <dd>상품을 배송 받은 날부터 7일 이내 교환은 언제든지 가능합니다. </dd>
            <dd class="c1">(주문하신 상품에 따라 배송기간이 조금 상이 할 수도 있음 / 동일 배송지의 1회만 가능)</dd>
            <dd>단, 고객님의 과실 혹은 단순 변심으로 인해 교환이 불가능할 수 있습니다.</dd>
            <dd class="b1">기타 궁금하신 사항은 1544-6444 고객센터로 문의 주시면 언제든지 상세히 답변 드리도록 하겠습니다.</dd>
            <dt>2.반품안내</dt>
            <dd>상품 특성상 포장을 개봉하시거나 상품을 사용하신 후에는 반품이 불가하오니 이점 유의하시기 바랍니다.</dd>
            <dd>단, 제품하자 시에는 1544-6444 고객센터로 문의 주시면 처리절차에 대해 친절하게 안내해 드리겠습니다.</dd>
            <dd class="c1" style="margin:12px 0 0 26px;">※ 파츠모아에 입고되서 판매하기까지의 절차와 주문, 결제, 배송 등에 대한 설명<br>
              기존 판매 절차와 동일 하며, 파츠모아를 통하지 않고 거래 되는 부분에 대해서는 관련 책임을 지지 않습니다.</dd>
          </dl>
        </div>
      </div>
      <div class="bt_all_box" id = "printButton">
      <span class="bt_all">
          	<span><input type="button" value="인쇄" class="btall" onclick = "printWindow();"/></span>
      </span>
      </div> 
    </div>
    
  </div>
 
</div>

</body>
</html>