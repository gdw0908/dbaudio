<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>상품권 통합관리 시스템</title>
<link href="/lib/css/cmsbase.css" rel="stylesheet" type="text/css" />
<link href="/lib/css/cmsadmin.css" rel="stylesheet" type="text/css" />
<link href="/lib/css/redmond/jquery-ui-1.9.1.custom.min.css" rel="stylesheet" type="text/css" />
<script type = "text/javascript">
function goBody(target_url){
	//alert(parent.leftFrame.src);
	//parent.leftFrame.style.display = "none";
	//parent.bodyFrame.location.href = target_url;
	parent.bodyFrame.location.href = target_url;
	
}

</script>
</head>
<body style="background-color:#f3f3f3;">

<div id="main">
  <!-- div style="margin: 0 auto; width: 757px; height: 418px; padding-top: 200px; padding-bottom: 270px;"><img src="/images/admin/contents/MC_CMS_Custom_03.gif" alt="관리자시스템" /></div-->
  <div class="main_link">
  	<ul>
	  <c:choose>
	  	<c:when test = "${sessionScope.member.group_seq ne '8'}">
	  		<c:if test="${sessionScope.member.carall eq 'N' or sessionScope.member.group_seq eq '1' }">
<!-- 				<li> -->
<!-- 					<a href="javascript:;" onclick = "goBody('/admin/resources/index.do');"> -->
<!-- 						<img src="/images/admin/contents/main_img1.png" alt="4대자원관리" /> -->
<!-- 					</a> -->
<!-- 				</li> -->
				<li>
					<a href="javascript:;" onclick = "goBody('/giftcard/admin/goods/part/index.do');">
						<img src="/images/admin/contents/main_icon1.png" alt="상품관리" />
						<span>상품관리</span>
					</a>
				</li>
<!-- 				<li> -->
<!-- 					<a href="javascript:;" onclick = "goBody('/admin/supply/cooperation/index.do');"> -->
<!-- 						<img src="/images/admin/contents/main_img3.png" alt="협력업체관리" /> -->
<!-- 					</a> -->
<!-- 				</li> -->
				<c:if test = "${sessionScope.member.group_seq eq '1'}">
				<li>
					<a href="javascript:;" onclick = "goBody('/giftcard/admin/member/index.do');">
						<img src="/images/admin/contents/main_icon2.png" alt="회원관리" />
						<span>회원관리</span>
					</a>
				</li>
				<li>
					<a href="javascript:;" onclick = "goBody('<c:choose><c:when test = "${sessionScope.member.group_seq eq '1'}">/giftcard/admin/calculate/deadline.do</c:when><c:otherwise>/giftcard/admin/calculate/day_search.do</c:otherwise></c:choose>');">
						<img src="/images/admin/contents/main_icon3.png" alt="정산관리" />
						<span>정산관리</span>
					</a>
				</li>
				<li>
					<a href="javascript:;" onclick = "goBody('/giftcard/admin/system/board/notice/index.do');">
						<img src="/images/admin/contents/main_icon4.png" alt="시스템관리" />
						<span>시스템관리</span>
					</a>
				</li>
				<li>
					<a href="javascript:;" onclick = "goBody('/giftcard/admin/statistics/statistics.do');">
						<img src="/images/admin/contents/main_icon5.png" alt="통계관리" />
						<span>통계관리</span>
					</a>
				</li>
				</c:if>
			</c:if>
	  	</c:when>
	  </c:choose>
	  </ul>
  </div>
</div>
</body>
</html>
